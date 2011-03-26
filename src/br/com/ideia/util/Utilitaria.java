package br.com.ideia.util;

import java.awt.Image;
import java.text.DecimalFormat;
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
	public static final String PATTERN_VALOR = "#,###,##0.00";
	private static SimpleDateFormat sdf = new SimpleDateFormat();
	private static DecimalFormat df = new DecimalFormat();
		
	public Image getImagemLogo() {
		return new ImageIcon(getClass().getClassLoader().getResource(caminhoImagem + "logo.png")).getImage();
	}
	
	public ImageIcon getImagemJava() {
		return new ImageIcon(getClass().getClassLoader().getResource(caminhoImagem + "logoJava.png"));
	}
	
	public static Date convertStringToDate(String data, String pattern) throws ParseException {
		if(data == null || data.isEmpty()){
			return null;
		}
		sdf.applyPattern(pattern);
		sdf.setLenient(false);
		return sdf.parse(data);
	}	
	
	public static String convertDateToString(Date data, String pattern) throws ParseException {
		if(data == null){
			return "";
		}
		sdf.applyPattern(pattern); 
		sdf.setLenient(false);
		return sdf.format(data);
	}
	
	public static String formataValor(Double valor) {
		if(valor == null){
			return "";
		}		
		try {
			df.applyPattern(PATTERN_VALOR);			
			return df.format(valor);
		} catch (Exception e) {
			return valor+"";
		}		
	}
	
}
