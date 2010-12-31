package br.com.ideia.util;
/**
 * 
 * Exceção específica para registro em uso no BD
 */
public class RegistroEmUsoException extends Exception {

	private static final long serialVersionUID = -6380478878894573365L;
	
	public RegistroEmUsoException(){
		super();
	}

	public RegistroEmUsoException(String message){
		super(message);
	}
	public RegistroEmUsoException(Throwable cause){
		super(cause);
	}
	public RegistroEmUsoException(String message, Throwable cause){
		super(message, cause);
	}
}
