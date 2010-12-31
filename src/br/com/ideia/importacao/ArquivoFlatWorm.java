package br.com.ideia.importacao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
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

	public void exportFile(ArquivoAgregado arquivo, String pathFileDestino) throws FlatwormException, ValidacaoException {
		File file = null;
		try {
			file = new File(pathFileDestino + getExtensionFile());
			exportFile(arquivo, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			logger.error(e);
			throw new ValidacaoException(Mensagem.ARQUIVO_INEXISTENTE, e);
		} catch (Exception e) {
			if(file != null){
				file.delete();
			}	
			throw new FlatwormException();
		}		
	}

	@SuppressWarnings("unchecked")
	public void exportFile(ArquivoAgregado arquivo, OutputStream out) throws FlatwormException, ValidacaoException {
		FileCreator fileCreator = new FileCreator(getPathFileConfig(), out);
		fileCreator.setRecordSeperator("\n");
		try {
			fileCreator.open();
			if (!arquivo.getClass().isAnnotationPresent(FlatWormAgregate.class)) {
				throw new FlatwormException("Annotation @FlatWormAgregate não está presente!");
			}
			for (Method metodo : arquivo.getClass().getDeclaredMethods()) {
				if (metodo.getName().startsWith("get")) {
					if (metodo.getReturnType().isAssignableFrom(List.class)) {
						List<FlatWormBean> result = (List<FlatWormBean>) metodo.invoke(arquivo);
						if (metodo.isAnnotationPresent(FlatWorm.class)) {
							FlatWorm annotation = metodo.getAnnotation(FlatWorm.class);
							for (FlatWormBean obj : result) {
								fileCreator.setBean(annotation.beanName(), obj);
								fileCreator.write(annotation.recordName());
							}
						} else {
							throw new FlatwormException("Annotation @FlatWorm não está presente!");
						}
					}else {
						Object result = metodo.invoke(arquivo);
						if (metodo.isAnnotationPresent(FlatWorm.class)) {
							FlatWorm annotation = metodo.getAnnotation(FlatWorm.class);							
							fileCreator.setBean(annotation.beanName(), result);
							fileCreator.write(annotation.recordName());							
						} else {
							throw new FlatwormException("Annotation @FlatWorm não está presente!");
						}
					}
				}
			}			
		} catch (IOException e) {
			logger.error(e);
			throw new ValidacaoException(Mensagem.ERRO_EXPORTACAO_ARQUIVO, e);	
		} catch (Exception e) {
			logger.error(e);
			throw new ValidacaoException(Mensagem.ERRO_EXPORTACAO_ARQUIVO, e);
		} finally {
			try {
				if (fileCreator != null) {
					fileCreator.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.error("erro ao fechar o fileCreator",e);
			}
		}
	}

	public ArquivoAgregado importFile(File outputFile, Class<ArquivoAgregado> clazz) throws FlatwormException, ValidacaoException {
		validaImport(outputFile);
		try {
			ConfigurationReader parser = new ConfigurationReader();
			FileFormat ff = parser.loadConfigurationFile(getPathFileConfig());
			BufferedReader bufIn;

			bufIn = new BufferedReader(new InputStreamReader(new FileInputStream(outputFile)));

			MatchedRecord results;
			ArquivoAgregado dadosArquivo = new ArquivoAgregado();
			while ((results = ff.getNextRecord(bufIn)) != null) {
				if (clazz.isAnnotationPresent(FlatWorm.class)) {
					FlatWorm tipo = clazz.getAnnotation(FlatWorm.class);
					if (results.getRecordName().equals(tipo.recordName())) {
						// dadosArquivo.add(clazz.cast(results.getBean(tipo.beanName())));
					}
				} else {
					logger.debug(String.format(Mensagem.ARQUIVO_ANOTACAO, clazz.getName()));
					throw new FlatwormException();
				}
			}
			return dadosArquivo;
		} catch (FileNotFoundException e) {
			throw new ValidacaoException(Mensagem.ARQUIVO_INEXISTENTE, e);
		}
	}

	public ArquivoAgregado importFile(String pathFileOut, Class<ArquivoAgregado> clazz) throws FlatwormException, ValidacaoException {
		File fileOut = new File(pathFileOut);
		return importFile(fileOut, clazz);
	}

	public void validaImport(File file) throws ValidacaoException {
		if (file != null && file.exists() && file.canRead()) {
			if (!file.getName().endsWith(getExtensionFile().toLowerCase()) && !file.getName().endsWith(getExtensionFile())) {
				throw new ValidacaoException(String.format(Mensagem.ARQUIVO_EXTENSAO, getExtensionFile()));
			}
		} else {
			throw new ValidacaoException(Mensagem.ARQUIVO_INEXISTENTE);
		}
	}
}
