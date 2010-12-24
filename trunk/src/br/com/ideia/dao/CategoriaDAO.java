package br.com.ideia.dao;

import java.util.List;

import javax.persistence.Query;

import br.com.ideia.bean.CategoriaVO;
import br.com.ideia.util.BancoDeDadosException;
import br.com.ideia.util.FabricaConexao;

public class CategoriaDAO extends GenericDAO<CategoriaVO>{

	@SuppressWarnings("unchecked")
	public List<CategoriaVO> getCategoriaByNome(String descricao) throws BancoDeDadosException {
		manager = FabricaConexao.getEntityManager();
		Query query = manager.createQuery("SELECT c FROM categoria c WHERE c.descricao like ?1 ORDER BY c.descricao");
		query.setParameter(1, descricao+"%");		
		return (List<CategoriaVO>)query.getResultList();
	}
}
