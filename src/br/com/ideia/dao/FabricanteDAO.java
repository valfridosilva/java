package br.com.ideia.dao;

import java.util.List;

import javax.persistence.Query;

import br.com.ideia.bean.FabricanteVO;
import br.com.ideia.util.BancoDeDadosException;
import br.com.ideia.util.FabricaConexao;

public class FabricanteDAO extends GenericDAO<FabricanteVO>{
	
	@SuppressWarnings("unchecked")
	public List<FabricanteVO> getFabricanteByNome(String descricao) throws BancoDeDadosException {
		manager = FabricaConexao.getEntityManager();
		Query query = manager.createQuery("SELECT f FROM fabricante f WHERE f.descricao like ?1 ORDER BY f.descricao");
		query.setParameter(1, descricao+"%");		
		return (List<FabricanteVO>)query.getResultList();
	}

}
