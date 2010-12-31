package br.com.ideia.dao;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import br.com.ideia.util.BancoDeDadosException;
import br.com.ideia.util.FabricaConexao;
import br.com.ideia.util.RegistroEmUsoException;

public abstract class GenericDAO<T> {

	protected EntityManager manager;

	public void insere(T entity) throws BancoDeDadosException, EntityExistsException {
		try {
			manager = FabricaConexao.getEntityManager();
			manager.getTransaction().begin();
			manager.persist(entity);
			manager.getTransaction().commit();
		} catch (EntityExistsException e) {
			rollBack();
			throw e;
		} catch (PersistenceException e) {
			rollBack();
			throw new BancoDeDadosException(e);
		} catch (Exception e) {
			rollBack();
			throw new BancoDeDadosException(e);
		}
	}

	protected void rollBack() {
		if (manager != null && manager.getTransaction().isActive()) {
			manager.getTransaction().rollback();
		}
	}

	public void insereOuAtualiza(List<T> elements) throws BancoDeDadosException, EntityExistsException {
		try {
			manager = FabricaConexao.getEntityManager();
			manager.getTransaction().begin();
			int index = 0;
			for (T element : elements) {
				manager.merge(element);
				index++;
				if (index % 20 == 0) { // a cada 20 registros efetua o commit
					manager.flush();
					manager.clear();
				}
			}
			manager.getTransaction().commit();
		} catch (EntityExistsException e) {
			rollBack();
			throw e;
		} catch (PersistenceException e) {
			rollBack();
			throw new BancoDeDadosException(e);
		} catch (Exception e) {
			rollBack();
			throw new BancoDeDadosException(e);
		}
	}

	public void altera(T element) throws BancoDeDadosException, EntityExistsException {
		try {
			manager = FabricaConexao.getEntityManager();
			manager.getTransaction().begin();
			manager.merge(element);
			manager.getTransaction().commit();
		} catch (EntityExistsException e) {
			rollBack();
			throw e;
		} catch (PersistenceException e) {
			rollBack();
			throw new BancoDeDadosException(e);
		} catch (Exception e) {
			rollBack();
			throw new BancoDeDadosException(e);
		}
	}
	
	public void exclui(Integer id, Class<T> clazz) throws BancoDeDadosException, RegistroEmUsoException{
		try {
			manager = FabricaConexao.getEntityManager();
			manager.getTransaction().begin();				
			manager.remove(manager.find(clazz, id));
			manager.getTransaction().commit();			
		} catch (PersistenceException e) {
			rollBack();			
			throw new BancoDeDadosException(e);
		} catch (Exception e) {
			rollBack();
			throw new BancoDeDadosException(e);
		}
	}

}
