package br.com.ideia.util;

public class LinhaRelatorio {

	private Object[] linha;

	public LinhaRelatorio(Object[] linha) {
		this.linha = linha;
	}

	public Object getCelula(int coluna) {
		if (linha[coluna] == null) {
			return "";
		}
		return linha[coluna];
	}

	public void setCelula(Object valor, int coluna) {
		if (valor == null) {
			linha[coluna] = "";
		} else {
			linha[coluna] = valor;
		}
	}
}
