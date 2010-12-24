package br.com.ideia.importacao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.ideia.bean.FlatWormBean;
import br.com.ideia.util.Mensagem;
import br.com.ideia.util.ValidacaoException;

import com.blackbear.flatworm.ConfigurationReader;
import com.blackbear.flatworm.FileCreator;
import com.blackbear.flatworm.FileFormat;
import com.blackbear.flatworm.MatchedRecord;
import com.blackbear.flatworm.errors.FlatwormException;

public abstract class ArquivoFlatWorm {
	
	protected Logger logger = Logger.getLogger(getClass());
	
	protected abstract String getPathFileConfig();
	
	protected abstract String getExtensionFile();

	public <E extends FlatWormBean> void exportFile(List<E> dadosArquivo, String pathFileDestino) throws FlatwormException, ValidacaoException {
		try {
			exportFile(dadosArquivo, new FileOutputStream(new File(pathFileDestino+getExtensionFile())));
		} catch (FileNotFoundException e) {			
			logger.error(e);
			throw new ValidacaoException(Mensagem.ARQUIVO_INEXISTENTE,e);
		}
	}

	public <E extends FlatWormBean> void exportFile(List<E> dadosArquivo, OutputStream out) throws FlatwormException, ValidacaoException {
		FileCreator fileCreator = new FileCreator(getPathFileConfig(), out);
		fileCreator.setRecordSeperator("\n");
		try {
			fileCreator.open();
			for (E dado : dadosArquivo) {
				if (dado.getClass().isAnnotationPresent(FlatWorm.class)) {
					FlatWorm tipo = dado.getClass().getAnnotation(FlatWorm.class);
					fileCreator.setBean(tipo.beanName(), dado);
					fileCreator.write(tipo.recordName());
				} else {
					logger.debug("Nenhuma @FlatWorm foi encontrado na classe: " + dado.getClass().getName());
					throw new ValidacaoException();
				}
			}
		} catch (IOException e) {
			logger.error(e);
			throw new ValidacaoException(Mensagem.ERRO_EXPORTACAO_ARQUIVO,e);
		} finally {
			try {
				if(fileCreator != null){
					fileCreator.close();
				}
				if(out != null){
					out.close();
				}				
			} catch (IOException e) {
				logger.error(e);
			}
			
		}
	}

	public <E extends FlatWormBean> List<E> importFile(File outputFile, Class<E> clazz) throws FlatwormException, ValidacaoException {
		validaImport(outputFile);
		try {
			ConfigurationReader parser = new ConfigurationReader();
			FileFormat ff = parser.loadConfigurationFile(getPathFileConfig());
			BufferedReader bufIn;

			bufIn = new BufferedReader(new InputStreamReader(new FileInputStream(outputFile)));

			MatchedRecord results;
			List<E> dadosArquivo = new ArrayList<E>();
			while ((results = ff.getNextRecord(bufIn)) != null) {
				if (clazz.isAnnotationPresent(FlatWorm.class)) {
					FlatWorm tipo = clazz.getAnnotation(FlatWorm.class);
					if (results.getRecordName().equals(tipo.recordName())) {
						dadosArquivo.add(clazz.cast(results.getBean(tipo.beanName())));
					}
				} else {
					logger.debug(String.format(Mensagem.ARQUIVO_ANOTACAO,clazz.getName()));
					throw new FlatwormException();
				}
			}
			return dadosArquivo;
		} catch (FileNotFoundException e) {
			throw new ValidacaoException(Mensagem.ARQUIVO_INEXISTENTE, e);
		}
	}

	public <E extends FlatWormBean> List<E> importFile(String pathFileOut, Class<E> clazz) throws FlatwormException,
			ValidacaoException {
		File fileOut = new File(pathFileOut);
		return importFile(fileOut, clazz);
	}

	public void validaImport(File file) throws ValidacaoException {
		if (file != null && file.exists() && file.canRead()) {
			if (!file.getName().endsWith(getExtensionFile().toLowerCase()) && !file.getName().endsWith(getExtensionFile())) {
				throw new ValidacaoException(String.format(Mensagem.ARQUIVO_EXTENSAO,getExtensionFile()));
			}
		} else {
			throw new ValidacaoException(Mensagem.ARQUIVO_INEXISTENTE);
		}
	}
}
