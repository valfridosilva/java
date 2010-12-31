package br.com.ideia.negocio;

import java.util.List;

import javax.persistence.EntityExistsException;

import br.com.ideia.bean.ClienteVO;
import br.com.ideia.dao.ClienteDAO;
import br.com.ideia.util.BancoDeDadosException;
import br.com.ideia.util.RegistroEmUsoException;

public class ClienteBO {

	private ClienteDAO clienteDAO;
	
	public ClienteBO() {
		clienteDAO = new ClienteDAO();		
	}

	public void insereCliente(ClienteVO cliente) throws BancoDeDadosException, EntityExistsException{
		clienteDAO.insere(cliente);
	}
	
	public void alteraCliente(ClienteVO cliente) throws BancoDeDadosException, EntityExistsException{
		clienteDAO.altera(cliente);
	}
	
	public void excluiCliente(ClienteVO cliente) throws BancoDeDadosException {
		try {
			clienteDAO.exclui(cliente.getIdCliente(),ClienteVO.class);
		} catch (RegistroEmUsoException e) {
			throw new BancoDeDadosException(e);
		}
	}
	
	public List<ClienteVO> getClienteByNome(String nome) throws BancoDeDadosException{
		return clienteDAO.getClienteByNome(nome);
	}		
}
