package banco.logica;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class ContaPoupanca implements IConta {

	private LocalDateTime dataAniversarioPoupanca;

	public ContaPoupanca(String numero) {
		super();
		this.dataAniversarioPoupanca = LocalDateTime.now();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
		
		 System.out.println("Extrato - Lista de Movimentações");
		    for (Transacao t : getTransacoes()) {
		        if (t.getDataEfetivacao().getMonth().equals(mes) && t.getDataEfetivacao().getYear() == year) {
		            System.out.println(t);
		        }
		    }
		/*
		System.out.println("Extrato - Lista de Movimetacoes");
		for(Transacao t : transacoes) {
			if(t.getDataEfetivacao().getMonth().compareTo(mes) == 0 && t.getDataEfetivacao().getYear() == year) {
				
				System.out.println(t);
			}
		
	}
*/
}

	@Override
	public boolean isStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Transacao> getTransacoes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getSaldo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSaldo(BigDecimal saldo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getNumero() {
		// TODO Auto-generated method stub
		return null;
	}
}
