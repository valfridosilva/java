package br.com.ideia.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;

/**
 * Armazena informações sobre as imagens usados no sistema
 * 
 */
public class Utilitaria {

	private static final String caminhoImagem = "imagens/";
	public static final String PATTERN_DDMMYYYY = "dd/MM/yyyy";
	private static SimpleDateFormat format = new SimpleDateFormat();
		
	public ImageIcon getImagemLogo() {
		return new ImageIcon(getClass().getClassLoader().getResource(caminhoImagem + "logo.png"));
	}
	
	public ImageIcon getImagemJava() {
		return new ImageIcon(getClass().getClassLoader().getResource(caminhoImagem + "logo.png"));
	}
	
	public static Date convertStringToDate(String data, String pattern) throws ParseException {
		if(data == null || data.isEmpty()){
			return null;
		}
		format.applyPattern(pattern);
		format.setLenient(false);
		return format.parse(data);
	}	
	
	public static String convertDateToString(Date data, String pattern) throws ParseException {
		if(data == null){
			return "";
		}
		format.applyPattern(pattern); 
		format.setLenient(false);
		return format.format(data);
	}
	
}
