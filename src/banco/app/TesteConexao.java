package banco.app;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import banco.dao.ClienteDAO;
import banco.dao.ClienteDAOImpl;
import banco.dao.ContaDAO;
import banco.dao.ContaDAOImpl;
import banco.db.ConexaoMySQL;
import banco.logica.Cliente;
import banco.logica.ContaCorrente;
import banco.logica.IConta;

public class TesteConexao extends JFrame {
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ContaDAO contaDAO;
	private ClienteDAO clienteDAO;
	
	public TesteConexao() {
        clienteDAO = new ClienteDAOImpl(new ConexaoMySQL()); // Inicializa a conexão com o BD
        contaDAO = new ContaDAOImpl(new ConexaoMySQL());

        setTitle("Sistema Bancário");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JButton btnCadastrarCliente = new JButton("Cadastrar Cliente");
        JButton btnAcoesCliente = new JButton("Ações com Clientes");

        panel.add(btnCadastrarCliente);
        panel.add(btnAcoesCliente);

        btnCadastrarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirJanelaCadastroCliente();
            }
        });

        btnAcoesCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirJanelaAcoesCliente();
            }
        });

        add(panel);
        setVisible(true);
    }

    // Método para abrir a janela de cadastro de cliente
    private void abrirJanelaCadastroCliente() {
        JFrame frameCadastro = new JFrame("Cadastro de Cliente");
        frameCadastro.setSize(300, 200);
        frameCadastro.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel labelCpf = new JLabel("CPF:");
        JTextField textCpf = new JTextField();
        JLabel labelNome = new JLabel("Nome:");
        JTextField textNome = new JTextField();
        JButton btnSalvar = new JButton("Salvar");

        panel.add(labelCpf);
        panel.add(textCpf);
        panel.add(labelNome);
        panel.add(textNome);
        panel.add(btnSalvar);

        frameCadastro.add(panel);
        frameCadastro.setVisible(true);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpf = textCpf.getText();
                String nome = textNome.getText();
                Cliente novoCliente = new Cliente(cpf, nome);
                clienteDAO.salvarCliente(novoCliente);
                JOptionPane.showMessageDialog(frameCadastro, "Cliente cadastrado com sucesso!");
                frameCadastro.dispose();
            }
        });
    }

    // Método para abrir a janela de ações com clientes existentes
    private void abrirJanelaAcoesCliente() {
        JFrame frameAcoes = new JFrame("Ações com Cliente");
        frameAcoes.setSize(400, 300);
        frameAcoes.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(11, 1));

        JButton btnListarClientes = new JButton("Listar Clientes");
        JButton btnConsultarCPF = new JButton("Consultar Cliente por CPF");
        JButton btnRemoverCliente = new JButton("Remover Cliente");
        JButton btnGerenciarContas = new JButton("Gerenciar Contas");
        JButton btnDepositoSaque = new JButton("Depósito/Saque");
        JButton btnTransferencia = new JButton("Transferência");
        JButton btnConsultarSaldo = new JButton("Consultar Saldo");
        JButton btnConsultarBalanco = new JButton("Consultar Balanço");
        JButton btnRemoverConta = new JButton("Remover Conta");
        JButton btnListarConta = new JButton("Listar Conta");
        JButton btnExtrato = new JButton("Extrato");

        panel.add(btnListarClientes);
        panel.add(btnConsultarCPF);
        panel.add(btnRemoverCliente);
        panel.add(btnGerenciarContas);
        panel.add(btnDepositoSaque);
        panel.add(btnTransferencia);
        panel.add(btnConsultarSaldo);
        panel.add(btnConsultarBalanco);
        panel.add(btnRemoverConta);
        panel.add(btnListarConta);
        panel.add(btnExtrato);

        frameAcoes.add(panel);
        frameAcoes.setVisible(true);

        btnListarClientes.addActionListener(e -> listarClientes());
        btnConsultarCPF.addActionListener(e -> consultarClientePorCPF());
        btnRemoverCliente.addActionListener(e -> removerCliente());
        btnGerenciarContas.addActionListener(e -> criarConta());
        btnDepositoSaque.addActionListener(e -> depositoSaque());
        btnTransferencia.addActionListener(e -> transferencia());
        btnConsultarSaldo.addActionListener(e -> consultarSaldo());
        btnConsultarBalanco.addActionListener(e -> consultarBalanco());
        btnRemoverConta.addActionListener(e -> removerConta()); 
        btnListarConta.addActionListener(e -> listarContas()); 
        btnExtrato.addActionListener(e -> extratoFinanceiro());
    }

    private void criarConta() {
        JFrame frameConta = new JFrame("Criar Conta");
        frameConta.setSize(300, 200);
        frameConta.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel labelCpf = new JLabel("CPF do Cliente:");
        JTextField textCpf = new JTextField();
        JLabel labelNumeroConta = new JLabel("Número da Conta:");
        JTextField textNumeroConta = new JTextField();
        JButton btnCriar = new JButton("Criar Conta");

        panel.add(labelCpf);
        panel.add(textCpf);
        panel.add(labelNumeroConta);
        panel.add(textNumeroConta);
        panel.add(btnCriar);

        frameConta.add(panel);
        frameConta.setVisible(true);

        btnCriar.addActionListener(e -> {
            String cpf = textCpf.getText();
            String numeroConta = textNumeroConta.getText();

            IConta novaConta = new ContaCorrente(numeroConta);
            contaDAO.criarConta(cpf, novaConta);
            JOptionPane.showMessageDialog(frameConta, "Conta criada com sucesso!");
            frameConta.dispose();
        });
    }

    private void depositoSaque() {
        JFrame frameDepositoSaque = new JFrame("Depósito/Saque");
        frameDepositoSaque.setSize(300, 200);
        frameDepositoSaque.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel labelNumeroConta = new JLabel("Número da Conta:");
        JTextField textNumeroConta = new JTextField();
        JLabel labelValor = new JLabel("Valor:");
        JTextField textValor = new JTextField();
        JButton btnDepositar = new JButton("Depositar");
        JButton btnSacar = new JButton("Sacar");

        panel.add(labelNumeroConta);
        panel.add(textNumeroConta);
        panel.add(labelValor);
        panel.add(textValor);
        panel.add(btnDepositar);
        panel.add(btnSacar);

        frameDepositoSaque.add(panel);
        frameDepositoSaque.setVisible(true);

        btnDepositar.addActionListener(e -> {
            String numeroConta = textNumeroConta.getText();
            double valor = Double.parseDouble(textValor.getText());

            contaDAO.depositar(numeroConta, valor);
            JOptionPane.showMessageDialog(frameDepositoSaque, "Depósito realizado com sucesso!");
        });

        btnSacar.addActionListener(e -> {
            String numeroConta = textNumeroConta.getText();
            double valor = Double.parseDouble(textValor.getText());

            contaDAO.sacar(numeroConta, valor);
            JOptionPane.showMessageDialog(frameDepositoSaque, "Saque realizado com sucesso!");
        });
    }

    private void transferencia() {
        JFrame frameTransferencia = new JFrame("Transferência");
        frameTransferencia.setSize(300, 200);
        frameTransferencia.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel labelContaOrigem = new JLabel("Conta de Origem:");
        JTextField textContaOrigem = new JTextField();
        JLabel labelContaDestino = new JLabel("Conta de Destino:");
        JTextField textContaDestino = new JTextField();
        JLabel labelValor = new JLabel("Valor:");
        JTextField textValor = new JTextField();
        JButton btnTransferir = new JButton("Transferir");

        panel.add(labelContaOrigem);
        panel.add(textContaOrigem);
        panel.add(labelContaDestino);
        panel.add(textContaDestino);
        panel.add(labelValor);
        panel.add(textValor);
        panel.add(btnTransferir);

        frameTransferencia.add(panel);
        frameTransferencia.setVisible(true);

        btnTransferir.addActionListener(e -> {
            String contaOrigem = textContaOrigem.getText();
            String contaDestino = textContaDestino.getText();
            double valor = Double.parseDouble(textValor.getText());

            contaDAO.transferir(contaOrigem, contaDestino, valor);
            JOptionPane.showMessageDialog(frameTransferencia, "Transferência realizada com sucesso!");
        });
    }

    // Novo método para listar as contas de um cliente
    private void listarContas() {
        String cpf = JOptionPane.showInputDialog(this, "Digite o CPF do cliente:");
        List<IConta> contas = contaDAO.listarContasPorCliente(cpf);
        if (contas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma conta encontrada para o cliente.");
        } else {
            StringBuilder sb = new StringBuilder("Contas do Cliente:\n");
            for (IConta conta : contas) {
                sb.append("Número da Conta: ").append(conta.getNumero()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        }
    }

    // Novo método para remover uma conta
    private void removerConta() {
        String cpf = JOptionPane.showInputDialog(this, "Digite o CPF do cliente:");
        String numeroConta = JOptionPane.showInputDialog(this, "Digite o número da conta a ser removida:");
        contaDAO.removerConta(cpf, numeroConta);
        JOptionPane.showMessageDialog(this, "Conta removida com sucesso!");
    }

    // Novo método para imprimir o extrato de movimentação financeira
    private void extratoFinanceiro() {
        String numeroConta = JOptionPane.showInputDialog(this, "Digite o número da conta:");
        String mes = JOptionPane.showInputDialog(this, "Digite o mês (MM):");
        String ano = JOptionPane.showInputDialog(this, "Digite o ano (AAAA):");
        String extrato = contaDAO.getExtrato(numeroConta, mes, ano);
        JOptionPane.showMessageDialog(this, extrato);
    }

    // Novo método para consultar saldo
    private void consultarSaldo() {
        String numeroConta = JOptionPane.showInputDialog(this, "Digite o número da conta:");
        double saldo = contaDAO.consultarSaldo(numeroConta);
        JOptionPane.showMessageDialog(this, "Saldo da Conta: " + saldo);
    }

    // Novo método para consultar balanço das contas de um cliente
    private void consultarBalanco() {
        String cpf = JOptionPane.showInputDialog(this, "Digite o CPF do cliente:");
        double balanco = contaDAO.consultarBalanco(cpf);
        JOptionPane.showMessageDialog(this, "Balanço Total das Contas do Cliente: " + balanco);
    }

    private void listarClientes() {
        List<Cliente> clientes = clienteDAO.listarClientes();
        StringBuilder sb = new StringBuilder("Clientes Cadastrados:\n");
        for (Cliente cliente : clientes) {
            sb.append("CPF: ").append(cliente.getCpf()).append(", Nome: ").append(cliente.getNome()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    private void consultarClientePorCPF() {
        String cpf = JOptionPane.showInputDialog(this, "Digite o CPF do cliente:");
        Cliente cliente = clienteDAO.localizarClientePorCPF(cpf);
        if (cliente != null) {
            JOptionPane.showMessageDialog(this, "Cliente encontrado: \nNome: " + cliente.getNome());
        } else {
            JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
        }
    }

    private void removerCliente() {
        String cpf = JOptionPane.showInputDialog(this, "Digite o CPF do cliente a ser removido:");
        clienteDAO.removerCliente(cpf);
        JOptionPane.showMessageDialog(this, "Cliente removido com sucesso!");
    }

    public static void main(String[] args) {
        new TesteConexao();
    }
/*
	    public TesteConexao() {
	        clienteDAO = new ClienteDAOImpl(new ConexaoMySQL()); // Inicializa a conexão com o BD
	        contaDAO = new ContaDAOImpl(new ConexaoMySQL());
	        
	        

	        setTitle("Sistema Bancário");
	        setSize(400, 200);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);

	        // Painel principal
	        JPanel panel = new JPanel();
	        panel.setLayout(new GridLayout(2, 1));

	        JButton btnCadastrarCliente = new JButton("Cadastrar Cliente");
	        JButton btnAcoesCliente = new JButton("Ações com Clientes");

	        panel.add(btnCadastrarCliente);
	        panel.add(btnAcoesCliente);

	        btnCadastrarCliente.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                abrirJanelaCadastroCliente();
	            }
	        });

	        btnAcoesCliente.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                abrirJanelaAcoesCliente();
	            }
	        });

	        add(panel);
	        setVisible(true);
	    }

	    // Método para abrir a janela de cadastro de cliente
	    private void abrirJanelaCadastroCliente() {
	        JFrame frameCadastro = new JFrame("Cadastro de Cliente");
	        frameCadastro.setSize(300, 200);
	        frameCadastro.setLocationRelativeTo(null);

	        JPanel panel = new JPanel(new GridLayout(4, 2));

	        JLabel labelCpf = new JLabel("CPF:");
	        JTextField textCpf = new JTextField();
	        JLabel labelNome = new JLabel("Nome:");
	        JTextField textNome = new JTextField();
	        JButton btnSalvar = new JButton("Salvar");

	        panel.add(labelCpf);
	        panel.add(textCpf);
	        panel.add(labelNome);
	        panel.add(textNome);
	        panel.add(btnSalvar);

	        frameCadastro.add(panel);
	        frameCadastro.setVisible(true);

	        btnSalvar.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                String cpf = textCpf.getText();
	                String nome = textNome.getText();
	           

	                Cliente novoCliente = new Cliente(cpf, nome);
	                clienteDAO.salvarCliente(novoCliente);
	                JOptionPane.showMessageDialog(frameCadastro, "Cliente cadastrado com sucesso!");
	                frameCadastro.dispose();
	            }
	        });
	    }

	    // Método para abrir a janela de ações com clientes existentes
	    private void abrirJanelaAcoesCliente() {
	        JFrame frameAcoes = new JFrame("Ações com Cliente");
	        frameAcoes.setSize(400, 300);
	        frameAcoes.setLocationRelativeTo(null);

	        JPanel panel = new JPanel(new GridLayout(6, 1));

	        JButton btnListarClientes = new JButton("Listar Clientes");
	        JButton btnConsultarCPF = new JButton("Consultar Cliente por CPF");
	        JButton btnRemoverCliente = new JButton("Remover Cliente");
	        JButton btnGerenciarContas = new JButton("Gerenciar Contas");
	        JButton btnDepositoSaque = new JButton("Depósito/Saque");
	        JButton btnTransferenciaExtrato = new JButton("Transferência/Extrato");

	        panel.add(btnListarClientes);
	        panel.add(btnConsultarCPF);
	        panel.add(btnRemoverCliente);
	        panel.add(btnGerenciarContas);
	        panel.add(btnDepositoSaque);
	        panel.add(btnTransferenciaExtrato);

	        frameAcoes.add(panel);
	        frameAcoes.setVisible(true);

	        // Aqui você pode adicionar as ações para cada botão, como abrir novas janelas para cada funcionalidade
	        btnListarClientes.addActionListener(e -> listarClientes());
	        btnConsultarCPF.addActionListener(e -> consultarClientePorCPF());
	        btnRemoverCliente.addActionListener(e -> removerCliente());
	        btnGerenciarContas.addActionListener(e -> criarConta());
	        btnDepositoSaque.addActionListener(e -> depositoSaque());
	        btnTransferenciaExtrato.addActionListener(e -> transferencia());

	        // Continue implementando as outras ações...
	    }
	    
	    private void criarConta() {
	        JFrame frameConta = new JFrame("Criar Conta");
	        frameConta.setSize(300, 200);
	        frameConta.setLocationRelativeTo(null);

	        JPanel panel = new JPanel(new GridLayout(3, 2));

	        JLabel labelCpf = new JLabel("CPF do Cliente:");
	        JTextField textCpf = new JTextField();
	        JLabel labelNumeroConta = new JLabel("Número da Conta:");
	        JTextField textNumeroConta = new JTextField();
	        JButton btnCriar = new JButton("Criar Conta");

	        panel.add(labelCpf);
	        panel.add(textCpf);
	        panel.add(labelNumeroConta);
	        panel.add(textNumeroConta);
	        panel.add(btnCriar);

	        frameConta.add(panel);
	        frameConta.setVisible(true);

	        btnCriar.addActionListener(e -> {
	            String cpf = textCpf.getText();
	            String numeroConta = textNumeroConta.getText();

	            IConta novaConta = new ContaCorrente(numeroConta);
	            contaDAO.criarConta(cpf, novaConta);
	            JOptionPane.showMessageDialog(frameConta, "Conta criada com sucesso!");
	            frameConta.dispose();
	        });
	    }
	    
	    private void depositoSaque() {
	        JFrame frameDepositoSaque = new JFrame("Depósito/Saque");
	        frameDepositoSaque.setSize(300, 200);
	        frameDepositoSaque.setLocationRelativeTo(null);

	        JPanel panel = new JPanel(new GridLayout(3, 2));

	        JLabel labelNumeroConta = new JLabel("Número da Conta:");
	        JTextField textNumeroConta = new JTextField();
	        JLabel labelValor = new JLabel("Valor:");
	        JTextField textValor = new JTextField();
	        JButton btnDepositar = new JButton("Depositar");
	        JButton btnSacar = new JButton("Sacar");

	        panel.add(labelNumeroConta);
	        panel.add(textNumeroConta);
	        panel.add(labelValor);
	        panel.add(textValor);
	        panel.add(btnDepositar);
	        panel.add(btnSacar);

	        frameDepositoSaque.add(panel);
	        frameDepositoSaque.setVisible(true);

	        btnDepositar.addActionListener(e -> {
	            String numeroConta = textNumeroConta.getText();
	            double valor = Double.parseDouble(textValor.getText());

	            contaDAO.depositar(numeroConta, valor);
	            JOptionPane.showMessageDialog(frameDepositoSaque, "Depósito realizado com sucesso!");
	        });

	        btnSacar.addActionListener(e -> {
	            String numeroConta = textNumeroConta.getText();
	            double valor = Double.parseDouble(textValor.getText());

	            contaDAO.sacar(numeroConta, valor);
	            JOptionPane.showMessageDialog(frameDepositoSaque, "Saque realizado com sucesso!");
	        });
	    }
	    private void transferencia() {
	        JFrame frameTransferencia = new JFrame("Transferência");
	        frameTransferencia.setSize(300, 200);
	        frameTransferencia.setLocationRelativeTo(null);

	        JPanel panel = new JPanel(new GridLayout(4, 2));

	        JLabel labelContaOrigem = new JLabel("Conta de Origem:");
	        JTextField textContaOrigem = new JTextField();
	        JLabel labelContaDestino = new JLabel("Conta de Destino:");
	        JTextField textContaDestino = new JTextField();
	        JLabel labelValor = new JLabel("Valor:");
	        JTextField textValor = new JTextField();
	        JButton btnTransferir = new JButton("Transferir");

	        panel.add(labelContaOrigem);
	        panel.add(textContaOrigem);
	        panel.add(labelContaDestino);
	        panel.add(textContaDestino);
	        panel.add(labelValor);
	        panel.add(textValor);
	        panel.add(btnTransferir);

	        frameTransferencia.add(panel);
	        frameTransferencia.setVisible(true);

	        btnTransferir.addActionListener(e -> {
	            String contaOrigem = textContaOrigem.getText();
	            String contaDestino = textContaDestino.getText();
	            double valor = Double.parseDouble(textValor.getText());

	            contaDAO.transferir(contaOrigem, contaDestino, valor);
	            JOptionPane.showMessageDialog(frameTransferencia, "Transferência realizada com sucesso!");
	        });
	    }



	    // Método para listar clientes
	    private void listarClientes() {
	        List<Cliente> clientes = clienteDAO.listarClientes();
	        StringBuilder sb = new StringBuilder("Clientes Cadastrados:\n");
	        for (Cliente cliente : clientes) {
	            sb.append("CPF: ").append(cliente.getCpf()).append(", Nome: ").append(cliente.getNome()).append("\n");
	        }
	        JOptionPane.showMessageDialog(this, sb.toString());
	    }

	    // Método para consultar cliente por CPF
	    private void consultarClientePorCPF() {
	        String cpf = JOptionPane.showInputDialog(this, "Digite o CPF do cliente:");
	        Cliente cliente = clienteDAO.localizarClientePorCPF(cpf);
	        if (cliente != null) {
	            JOptionPane.showMessageDialog(this, "Cliente encontrado: \nNome: " + cliente.getNome());
	        } else {
	            JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
	        }
	    }

	    // Método para remover cliente
	    private void removerCliente() {
	        String cpf = JOptionPane.showInputDialog(this, "Digite o CPF do cliente a ser removido:");
	        clienteDAO.removerCliente(cpf);
	        JOptionPane.showMessageDialog(this, "Cliente removido com sucesso!");
	    }

	    public static void main(String[] args) {
	        new TesteConexao();
	    }
	
	
	*/
}
