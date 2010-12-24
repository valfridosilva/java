package br.com.ideia.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
/**
 * Cria e fecha conex�es com o BD
 *
 */
public class FabricaConexao {

	 private static Logger logger = Logger.getLogger(FabricaConexao.class);

	private static EntityManager instance;

	public static EntityManager getEntityManager() {
		if (instance == null) {
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("ideia");			
			instance = factory.createEntityManager();
		}
		return instance;
	}
	
	public static void close() {
		if (instance != null && instance.isOpen()) {
			instance.close();
			logger.debug("Conex�o com o Banco de Dados fechada com sucesso!");
		}
	}
	
}
