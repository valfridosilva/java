package br.com.ideia.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ideia.bean.ClienteVO;
import br.com.ideia.util.BancoDeDadosException;
import br.com.ideia.util.FabricaConexao;

public class ClienteDAO extends GenericDAO<ClienteVO>{

	private EntityManager manager;
		
	/**
	 * Recupera os clientes por nome
	 * @param nome
	 * @return
	 * @throws BancoDeDadosException
	 */
	@SuppressWarnings("unchecked")
	public List<ClienteVO> getClienteByNome(String nome) throws BancoDeDadosException {
		manager = FabricaConexao.getEntityManager();
		Query query = manager.createQuery("SELECT c FROM cliente c WHERE c.nome like ?1 ORDER BY c.nome");
		query.setParameter(1, nome+"%");		
		return query.getResultList();
	}

}
