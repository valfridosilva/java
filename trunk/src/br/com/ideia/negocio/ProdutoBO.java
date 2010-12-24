package br.com.ideia.negocio;

import java.util.List;

import javax.persistence.EntityExistsException;

import br.com.ideia.bean.CategoriaVO;
import br.com.ideia.bean.FabricanteVO;
import br.com.ideia.bean.ProdutoVO;
import br.com.ideia.dao.CategoriaDAO;
import br.com.ideia.dao.FabricanteDAO;
import br.com.ideia.dao.ProdutoDAO;
import br.com.ideia.util.BancoDeDadosException;

public class ProdutoBO {

	private ProdutoDAO produtoDAO;
	private CategoriaDAO categoriaDAO;
	private FabricanteDAO fabricanteDAO;
	
	public ProdutoBO() {
		produtoDAO = new ProdutoDAO();	
		categoriaDAO = new CategoriaDAO();
		fabricanteDAO = new FabricanteDAO();
	}

	public void insereProduto(ProdutoVO produto) throws BancoDeDadosException, EntityExistsException{
		produtoDAO.insere(produto);
	}
	
	public void alteraProduto(ProdutoVO produto) throws BancoDeDadosException, EntityExistsException{
		produtoDAO.altera(produto);
	}
	
	public void excluiProduto(ProdutoVO produto) throws BancoDeDadosException {
		produtoDAO.exclui(produto);
	}
		
	public void insereCategoria(CategoriaVO categoria) throws BancoDeDadosException, EntityExistsException{
		categoriaDAO.insere(categoria);
	}
	
	public void alteraCategoria(CategoriaVO categoria) throws BancoDeDadosException, EntityExistsException{
		categoriaDAO.altera(categoria);
	}
	
	public void excluiCategoria(CategoriaVO categoria) throws BancoDeDadosException {
		categoriaDAO.exclui(categoria);
	}
		
	public void insereFabricante(FabricanteVO fabricante) throws BancoDeDadosException, EntityExistsException{
		fabricanteDAO.insere(fabricante);
	}
	
	public void alteraFabricante(FabricanteVO fabricante) throws BancoDeDadosException, EntityExistsException{
		fabricanteDAO.altera(fabricante);
	}
	
	public void excluiFabricante(FabricanteVO fabricante) throws BancoDeDadosException {
		fabricanteDAO.exclui(fabricante);
	}
	
	public List<CategoriaVO> getCategoriaByNome(String descricao) throws BancoDeDadosException {
		return categoriaDAO.getCategoriaByNome(descricao);
	}
	
	public List<FabricanteVO> getFabricanteByNome(String descricao) throws BancoDeDadosException {
		return fabricanteDAO.getFabricanteByNome(descricao);
	}
	
	public List<ProdutoVO> getProdutoByNome(String descricao) throws BancoDeDadosException {
		return produtoDAO.getProdutoByNome(descricao);
	}
			
}
