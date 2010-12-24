package br.com.ideia.util;
/**
 * 
 * Exceção específica lançada para erros de validação de campos
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
