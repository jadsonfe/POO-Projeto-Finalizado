package banco.logica;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class ContaPoupanca implements IConta {

    private LocalDateTime dataAniversarioPoupanca;
    private BigDecimal saldo;
    private boolean status;
    private String numero;
    private List<Transacao> transacoes;

    public ContaPoupanca(String numero) {
        this.dataAniversarioPoupanca = LocalDateTime.now();
        this.numero = numero;
        this.saldo = BigDecimal.ZERO;
        this.status = true;
        this.transacoes = new ArrayList<>();
    }

    private static final long serialVersionUID = 1L;

    @Override
    public void sacar(BigDecimal quantia) {
        if (isStatus() && quantia.compareTo(getSaldo()) <= 0) {
            setSaldo(getSaldo().subtract(quantia));
            getTransacoes().add(new Transacao(quantia, 'D', null));
        } else {
            System.out.println("Operação inválida");
        }
    }

    @Override
    public void depositar(BigDecimal quantia) {
        if (isStatus() && quantia.compareTo(BigDecimal.ZERO) > 0) {
            setSaldo(getSaldo().add(quantia));
            getTransacoes().add(new Transacao(quantia, 'C', null));
        } else {
            System.out.println("Operação inválida");
        }
    }

    @Override
    public void transferir(BigDecimal quantia, IConta destino) {
        if (getSaldo().compareTo(quantia) >= 0 && this.isStatus() && destino.isStatus()) {
            if (destino instanceof ContaCorrente) {
                setSaldo(getSaldo().subtract(quantia));
                getTransacoes().add(new Transacao(quantia, 'T', destino));
                destino.getTransacoes().add(new Transacao(quantia, 'T', this));
                destino.setSaldo(destino.getSaldo().add(quantia));
            } else if (destino instanceof ContaPoupanca) {
                if (getSaldo().multiply(new BigDecimal("1.02")).compareTo(quantia) >= 0) {
                    setSaldo(getSaldo().subtract(quantia.add(quantia.multiply(new BigDecimal("0.02")))));
                    getTransacoes().add(new Transacao(quantia, 'T', destino));
                    destino.getTransacoes().add(new Transacao(quantia, 'T', this));
                    destino.setSaldo(destino.getSaldo().add(quantia));
                } else {
                    System.out.println("Operação inválida");
                }
            }
        } else {
            System.out.println("Operação inválida");
        }
    }

    @Override
    public void emitirExtrato(Month mes, Integer ano) {
        System.out.println("Extrato - Lista de Movimentações");
        for (Transacao t : getTransacoes()) {
            if (t.getDataEfetivacao().getMonth().equals(mes) && t.getDataEfetivacao().getYear() == ano) {
                System.out.println(t);
            }
        }
    }

    @Override
    public boolean isStatus() {
        return status;
    }

    @Override
    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    @Override
    public BigDecimal getSaldo() {
        return saldo;
    }

    @Override
    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    @Override
    public String getNumero() {
        return numero;
    }
}
