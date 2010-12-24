package br.com.ideia.util;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ModeloRelatorio extends AbstractTableModel {

	public static String tipo;
	private static final long serialVersionUID = 1L;
	private String[] colunas;
	private ArrayList<LinhaRelatorio> dados;
	int cont = 0;

	public ModeloRelatorio(String[] colunas) {
		this.colunas = colunas;
		this.dados = new ArrayList<LinhaRelatorio>();
	}

	public void add(Object[] linha) {
		dados.add(new LinhaRelatorio(linha));
	}

	public int getColumnCount() {
		return colunas.length;
	}

	public int getRowCount() {
		return dados.size();
	}

	public String getColumnName(int col) {
		return colunas[col];
	}

	public Object getValueAt(int row, int col) {
		return ((LinhaRelatorio) dados.get(row)).getCelula(col);
	}

	public Class<? extends Object> getColumnClass(int c) {
		return !dados.isEmpty() ? ((LinhaRelatorio) dados.get(0)).getCelula(c).getClass() : null;
	}

	public void setValueAt(Object value, int row, int col) {
		((LinhaRelatorio) dados.get(row)).setCelula(value, col);
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

}
