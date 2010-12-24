package br.com.ideia.importacao;

public class ArquivoSER extends ArquivoFlatWorm {
	
	private static final String PATH_FILE_CONFIG = "arquivo-mapping.xml";
	public static final String EXTENSION_FILE = ".ser";

	@Override
	protected String getExtensionFile() {		
		return EXTENSION_FILE;
	}

	@Override
	protected String getPathFileConfig() {		
		return PATH_FILE_CONFIG;
	}
}
