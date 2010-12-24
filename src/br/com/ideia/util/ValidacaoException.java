package br.com.ideia.util;
/**
 * 
 * Exce��o espec�fica lan�ada para erros de valida��o de campos
 */
public class ValidacaoException extends Exception {

	private static final long serialVersionUID = -6380478878894573365L;
	
	public ValidacaoException(){
		super();
	}

	public ValidacaoException(String message){
		super(message);
	}
	public ValidacaoException(Throwable cause){
		super(cause);
	}
	public ValidacaoException(String message, Throwable cause){
		super(message, cause);
	}
	

}
