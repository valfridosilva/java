package br.com.ideia.importacao;

import java.util.Date;
import java.util.Map;

import com.blackbear.flatworm.ConversionOption;
import com.blackbear.flatworm.converters.CoreConverters;
import com.blackbear.flatworm.errors.FlatwormConversionException;


public class FlatWormConverter extends CoreConverters {
		
	@Override
	public String convertChar(String str, Map<String, ConversionOption> options) {	
		if(str != null && !str.trim().isEmpty()){
			return super.convertChar(str, options);
		}		
		return null;	
	}
	
	@Override
	public String convertChar(Object obj, Map<String, ConversionOption> options) {
		if(obj != null){
			return super.convertChar(obj, options);
		}		
		return "";	
	}
	
	@Override
	public String convertDate(Object obj, Map<String, ConversionOption> options) {	
		String res = super.convertDate(obj, options);
		return res == null?"":res;
	}
	
	@Override
	public Date convertDate(String arg0, Map<String, ConversionOption> arg1) throws FlatwormConversionException {
		if(arg0 != null && !arg0.trim().isEmpty()){
			return super.convertDate(arg0, arg1);
		}
		return null;
	}		
}
