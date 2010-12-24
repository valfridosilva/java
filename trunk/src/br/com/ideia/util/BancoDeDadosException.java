package br.com.ideia.util;
/**
 * 
 * Exceção específica para erros de banco de dados
 */
public class BancoDeDadosException extends Exception {

	private static final long serialVersionUID = -6380478878894573365L;
	
	public BancoDeDadosException(){
		super();
	}

	public BancoDeDadosException(String message){
		super(message);
	}
	public BancoDeDadosException(Throwable cause){
		super(cause);
	}
	public BancoDeDadosException(String message, Throwable cause){
		super(message, cause);
	}
	

}
