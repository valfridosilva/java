package br.com.ideia.negocio;

import java.io.File;
import java.util.List;

import javax.persistence.EntityExistsException;

import br.com.ideia.bean.ClienteVO;
import br.com.ideia.bean.ProdutoVO;
import br.com.ideia.dao.CategoriaDAO;
import br.com.ideia.dao.ClienteDAO;
import br.com.ideia.dao.FabricanteDAO;
import br.com.ideia.dao.ProdutoDAO;
import br.com.ideia.importacao.ArquivoAgregado;
import br.com.ideia.importacao.ArquivoFlatWorm;
import br.com.ideia.importacao.ArquivoSER;
import br.com.ideia.util.BancoDeDadosException;
import br.com.ideia.util.Mensagem;
import br.com.ideia.util.ValidacaoException;

import com.blackbear.flatworm.errors.FlatwormException;

public class ArquivoBO {

	private ClienteDAO clienteDAO;
	private ProdutoDAO produtoDAO;
	private CategoriaDAO categoriaDAO;
	private FabricanteDAO fabricanteDAO;
	
	public ArquivoBO() {
		clienteDAO = new ClienteDAO();
		produtoDAO = new ProdutoDAO();	
		categoriaDAO = new CategoriaDAO();
		fabricanteDAO = new FabricanteDAO();
	}
	
	public void importarArquivo(File arquivoSelecionado) throws ValidacaoException, FlatwormException, EntityExistsException, BancoDeDadosException{
		ArquivoFlatWorm arquivoFlatWorm = new ArquivoSER();
		arquivoFlatWorm.validaImport(arquivoSelecionado);
		ArquivoAgregado arquivo = arquivoFlatWorm.importFile(arquivoSelecionado, ArquivoAgregado.class);
		//clienteDAO.insereOuAtualiza(clientes);
	}
	
	public void exportarArquivo(String pathFile) throws BancoDeDadosException, FlatwormException, ValidacaoException{
		ArquivoFlatWorm arquivoFlatWorm = new ArquivoSER();		
		List<ClienteVO> clientes = clienteDAO.getClienteByNome("");
		List<ProdutoVO> produtos = produtoDAO.getProdutoByNome("");
		if(clientes.isEmpty() && produtos.isEmpty()){
			throw new ValidacaoException(Mensagem.NENHUM_REGISTRO);
		}
		ArquivoAgregado arquivo = new ArquivoAgregado();
		arquivo.setClientes(clientes);
		arquivo.setProdutos(produtos);
		arquivoFlatWorm.exportFile(arquivo, pathFile);		
	}
}
