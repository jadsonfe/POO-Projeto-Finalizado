package banco.dao;

import java.util.List;

import banco.logica.Cliente;

public interface ClienteDAO {

	
	    void salvarCliente(Cliente cliente);
	    Cliente localizarClientePorCPF(String cpf);
	    void atualizarCliente(Cliente cliente);
	    void removerCliente(String cpf);
	    List<Cliente> listarClientes();
	

}
