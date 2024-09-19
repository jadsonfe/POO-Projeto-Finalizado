package banco.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import banco.db.IConnection;
import banco.logica.ContaCorrente;
import banco.logica.ContaPoupanca;
import banco.logica.IConta;

public class ContaDAOImpl implements ContaDAO {
	private IConnection conn;

    public ContaDAOImpl(IConnection conn) {
        this.conn = conn;
    }
    
    private void registrarExtrato(String numeroConta, String tipoOperacao, double valor, String descricao) {
        String sql = "INSERT INTO ExtratoFinanceiro (numero_conta, data_operacao, tipo_operacao, valor, descricao) VALUES (?, NOW(), ?, ?, ?)";
        try (Connection conn = this.conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroConta);
            stmt.setString(2, tipoOperacao);
            stmt.setDouble(3, valor);
            stmt.setString(4, descricao);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void criarConta(String cpf, IConta conta) {
        String sql = "INSERT INTO Conta (numero, cliente_cpf, tipo_conta) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.getConnection().prepareStatement(sql)) {
            stmt.setString(1, (String) conta.getNumero());  
            stmt.setString(2, cpf);  
            stmt.setString(3, conta instanceof ContaCorrente ? "Conta Corrente" : "Conta Poupança");  
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void depositar(String numeroConta, double valor) {
        String sql = "UPDATE Conta SET saldo = saldo + ? WHERE numero = ?";
        try (PreparedStatement stmt = conn.getConnection().prepareStatement(sql)) {
            stmt.setDouble(1, valor);
            stmt.setString(2, numeroConta);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        registrarExtrato(numeroConta, "DEPOSITO", valor, "Depósito realizado");
    }

    @Override
    public void sacar(String numeroConta, double valor) {
        String sql = "UPDATE Conta SET saldo = saldo - ? WHERE numero = ? AND saldo >= ?";
        try (PreparedStatement stmt = conn.getConnection().prepareStatement(sql)) {
            stmt.setDouble(1, valor);
            stmt.setString(2, numeroConta);
            stmt.setDouble(3, valor);  
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Saldo insuficiente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        registrarExtrato(numeroConta, "SAQUE", valor, "Saque realizado");
    }

    @Override
    public void transferir(String contaOrigem, String contaDestino, double valor) {
        String sqlSaque = "UPDATE Conta SET saldo = saldo - ? WHERE numero = ? AND saldo >= ?";
        String sqlDeposito = "UPDATE Conta SET saldo = saldo + ? WHERE numero = ?";
        
        try (Connection conn = this.conn.getConnection()) {
            conn.setAutoCommit(false);  

            try (PreparedStatement stmtSaque = conn.prepareStatement(sqlSaque);
                 PreparedStatement stmtDeposito = conn.prepareStatement(sqlDeposito)) {

              
                stmtSaque.setDouble(1, valor);
                stmtSaque.setString(2, contaOrigem);
                stmtSaque.setDouble(3, valor);
                int rowsAffectedSaque = stmtSaque.executeUpdate();

                if (rowsAffectedSaque == 0) {
                    conn.rollback();  
                    System.out.println("Saldo insuficiente para transferência.");
                    return;
                }

                
                stmtDeposito.setDouble(1, valor);
                stmtDeposito.setString(2, contaDestino);
                stmtDeposito.executeUpdate();

                conn.commit();  
            } catch (SQLException e) {
                conn.rollback();  
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        registrarExtrato(contaOrigem, "TRANSFERENCIA", -valor, "Transferência para " + contaDestino);
        registrarExtrato(contaDestino, "TRANSFERENCIA", valor, "Transferência recebida de " + contaOrigem);
    }
    
    @Override
    public List<IConta> listarContasPorCliente(String cpf) {
        List<IConta> contas = new ArrayList<>();
        String sql = "SELECT numero, tipo_conta FROM Conta WHERE cliente_cpf = ?";

        try (Connection conn = this.conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String numeroConta = rs.getString("numero");
                String tipoConta = rs.getString("tipo_conta");

                if ("Conta Corrente".equals(tipoConta)) {
                    contas.add(new ContaCorrente(numeroConta));
                } else if ("Conta Poupança".equals(tipoConta)) {
                    contas.add(new ContaPoupanca(numeroConta));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contas;
    }


    @Override
    public void removerConta(String cpf, String numeroConta) {
        String sql = "DELETE FROM Conta WHERE cliente_cpf = ? AND numero = ?";

        try (Connection conn = this.conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, numeroConta);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getExtrato(String numeroConta, String mes, String ano) {
        StringBuilder extrato = new StringBuilder();
        String sql = "SELECT data_operacao, descricao, valor FROM ExtratoFinanceiro WHERE numero_conta = ? AND MONTH(data_operacao) = ? AND YEAR(data_operacao) = ?";

        try (Connection conn = this.conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroConta);
            stmt.setString(2, mes);
            stmt.setString(3, ano);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                extrato.append("Data: ").append(rs.getDate("data_operacao"))
                        .append(", Descrição: ").append(rs.getString("descricao"))
                        .append(", Valor: ").append(rs.getBigDecimal("valor")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return extrato.toString();
    }

    @Override
    public double consultarSaldo(String numeroConta) {
        double saldo = 0.0;
        String sql = "SELECT saldo FROM Conta WHERE numero = ?";

        try (Connection conn = this.conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroConta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                saldo = rs.getDouble("saldo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saldo;
    }

    @Override
    public double consultarBalanco(String cpf) {
        double balanco = 0.0;
        String sql = "SELECT SUM(saldo) AS balanco FROM Conta WHERE cliente_cpf = ?";

        try (Connection conn = this.conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                balanco = rs.getDouble("balanco");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balanco;
    }

}
