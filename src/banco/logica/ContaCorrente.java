package banco.logica;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class ContaCorrente implements IConta {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String numero;
	private BigDecimal saldo;
	private LocalDateTime dataAbertura;
	private boolean status;
	
	
	protected List<Transacao> transacoes = new ArrayList<>();

	public ContaCorrente(String numero) {
		this.numero = numero;
		this.saldo = new BigDecimal("0");
		this.dataAbertura = LocalDateTime.now();
		this.status = true;
	}




	@Override
	public void sacar(BigDecimal quantia) {
		if(isStatus() && quantia.compareTo(getSaldo()) <= 0 ) {
			setSaldo(getSaldo().add(quantia));
			
			
			getTransacoes().add(new Transacao(quantia, 'D', null));
		}else
			System.out.println("Operação inválida");
	}

	@Override
	public void depositar(BigDecimal quantia) {
		if(isStatus() && quantia.compareTo(getSaldo()) >= 0) {
			setSaldo(getSaldo().add(quantia));
			getTransacoes().add(new Transacao(quantia,'C' , null));
		}else
			System.out.println("Operação Inválida");
		
	}

	@Override
	public void transferir(BigDecimal quantia, IConta destino) {
		
		if(getSaldo().compareTo(quantia) > 0 && this.isStatus() 
				&& destino.isStatus()) {
			if(destino instanceof ContaCorrente) {
				setSaldo(getSaldo().subtract(quantia));
				getTransacoes().add(new Transacao(quantia, 'T', destino));
				destino.getTransacoes().add(new Transacao(quantia, 'T', destino));
				destino.setSaldo(destino.getSaldo().add(quantia));
			}else if(destino instanceof ContaPoupanca) {
				if(getSaldo().multiply(new BigDecimal("1.02")).compareTo(quantia) >0) {
					setSaldo(getSaldo().subtract(quantia.add(quantia.multiply(new BigDecimal("0.02")))));
					getTransacoes().add(new Transacao(quantia,'C', destino));
					destino.getTransacoes().add(new Transacao(quantia, 'T', destino));
					destino.setSaldo(destino.getSaldo().add(quantia));
				} else 
					System.out.println("Operação inválida");
				  
            } 
            
		}else 
        	System.out.println("Operação inválida");

	}

	@Override
	public void emitirExtrato(Month mes, Integer year) {
		// TODO Auto-generated method stub
		 System.out.println("Extrato - Lista de Movimentações");
		    for (Transacao t : getTransacoes()) {
		        if (t.getDataEfetivacao().getMonth().equals(mes) && t.getDataEfetivacao().getYear() == year) {
		            System.out.println(t);
		        }
		    }
	}




	@Override
	public boolean isStatus() {
		// TODO Auto-generated method stub
		return status;
	}

	@Override
	public List<Transacao> getTransacoes() {
		// TODO Auto-generated method stub
		return transacoes;
	}




	public String getNumero() {
		return numero;
	}




	public void setNumero(String numero) {
		this.numero = numero;
	}




	public BigDecimal getSaldo() {
		return saldo;
	}




	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}




	public LocalDateTime getDataAbertura() {
		return dataAbertura;
	}




	public void setDataAbertura(LocalDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}




	public void setStatus(boolean status) {
		this.status = status;
	}




	public void setTransacoes(List<Transacao> transacoes) {
		this.transacoes = transacoes;
	}



	
	
}
