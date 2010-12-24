package br.com.ideia.negocio;

import java.io.File;
import java.util.List;

import javax.persistence.EntityExistsException;

import br.com.ideia.bean.ClienteVO;
import br.com.ideia.dao.ClienteDAO;
import br.com.ideia.importacao.ArquivoFlatWorm;
import br.com.ideia.importacao.ArquivoSER;
import br.com.ideia.util.BancoDeDadosException;
import br.com.ideia.util.ValidacaoException;

import com.blackbear.flatworm.errors.FlatwormException;

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
		clienteDAO.exclui(cliente);
	}
	
	public List<ClienteVO> getClienteByNome(String nome) throws BancoDeDadosException{
		return clienteDAO.getClienteByNome(nome);
	}
	
	public void importarCliente(File arquivoSelecionado) throws ValidacaoException, FlatwormException, EntityExistsException, BancoDeDadosException{
		ArquivoFlatWorm arquivoFlatWorm = new ArquivoSER();
		arquivoFlatWorm.validaImport(arquivoSelecionado);
		List<ClienteVO> clientes = arquivoFlatWorm.importFile(arquivoSelecionado, ClienteVO.class);
		clienteDAO.insere(clientes);
	}
	
	public void exportarCliente(String pathFile) throws BancoDeDadosException, FlatwormException, ValidacaoException{
		ArquivoFlatWorm arquivoFlatWorm = new ArquivoSER();		
		List<ClienteVO> clientes = getClienteByNome("");
		arquivoFlatWorm.exportFile(clientes, pathFile);		
	}
}
