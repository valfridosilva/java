package br.com.ideia.dao;

import java.util.List;

import javax.persistence.Query;

import br.com.ideia.bean.ProdutoVO;
import br.com.ideia.util.BancoDeDadosException;
import br.com.ideia.util.FabricaConexao;

@SuppressWarnings("unchecked")
public class ProdutoDAO extends GenericDAO<ProdutoVO>{

	
	public List<ProdutoVO> getProdutoByNome(String descricao) throws BancoDeDadosException {
		manager = FabricaConexao.getEntityManager();
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT p FROM produto p ");
		hql.append("left join fetch p.categoria c ");
		hql.append("left join fetch p.fabricante f ");
		hql.append("WHERE p.descricao like ?1 ORDER BY p.descricao");
		Query query = manager.createQuery(hql.toString());			
		query.setParameter(1, descricao+"%");		
		return (List<ProdutoVO>)query.getResultList();
	}
	
	public boolean hasReferenciaCategoria(Integer idCategoria) throws BancoDeDadosException {
		manager = FabricaConexao.getEntityManager();
		Query query = manager.createQuery("SELECT p FROM produto p WHERE p.categoria.id = ?1");
		query.setParameter(1, idCategoria);		
		List<ProdutoVO> lista = (List<ProdutoVO>)query.getResultList();
		return !lista.isEmpty();
	}
	
	public boolean hasReferenciaFabricante(Integer idFabricante) throws BancoDeDadosException {
		manager = FabricaConexao.getEntityManager();
		Query query = manager.createQuery("SELECT p FROM produto p WHERE p.fabricante.id = ?1");
		query.setParameter(1, idFabricante);		
		List<ProdutoVO> lista = (List<ProdutoVO>)query.getResultList();
		return !lista.isEmpty();
	}
	
}
