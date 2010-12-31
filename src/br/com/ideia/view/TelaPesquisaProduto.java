package br.com.ideia.view;

import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import br.com.ideia.bean.CategoriaVO;
import br.com.ideia.bean.FabricanteVO;
import br.com.ideia.bean.ProdutoVO;
import br.com.ideia.negocio.ProdutoBO;
import br.com.ideia.util.BancoDeDadosException;
import br.com.ideia.util.ModeloRelatorio;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class TelaPesquisaProduto extends JInternalFrame {
	private static Logger logger = Logger.getLogger(TelaPesquisaProduto.class);
	private static final long serialVersionUID = 1L;
	private JTable tabela;
	private ModeloRelatorio modelo;
	private JTextField inputText;
	private JScrollPane scroll;	
	private TableRowSorter<ModeloRelatorio> sorter;
	private RowFilter<ModeloRelatorio, Object> filtro;
	private Map<Integer, ProdutoVO> mapa;
	private final Integer QTD_COLUNA = 6;
	private TelaProduto telaProduto;
	private List<CategoriaVO> categorias;
	private List<FabricanteVO> fabricantes;
	private ProdutoBO produtoBO;
	private String[] caracteresEspeciais = new String[]{"\\","(",")","[","^","$",".","|","?","*","+"};

	public TelaPesquisaProduto(TelaMenu telaMenu, List<ProdutoVO> produtos, List<CategoriaVO> categorias, List<FabricanteVO> fabricantes) {
		super("Pesquisa!", true, true, true, true);
		setLayout(new FlowLayout());
		setVisible(true);

		telaMenu.desktop.add(this);
		inputText = new JTextField(80);
		inputText.setEditable(true);
		
		this.categorias = categorias;
		this.fabricantes = fabricantes;

		modelo = new ModeloRelatorio(new String[] { "Seq.", "Código", "Descrição", "Valor", "Categoria", "Fabricante" });

		mapa = new HashMap<Integer, ProdutoVO>();
		int index = 0;
		for (ProdutoVO prod : produtos) {
			modelo.add(transformaToArray(prod, index + 1));
			mapa.put(index, prod);
			index++;
		}

		tabela = new JTable(modelo);
		tabela.setRowHeight(22); // tamanho da linha
		/**
		 * permite selecionar apenas uma linha
		 */
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabela.setColumnSelectionAllowed(false);

		/**
		 * Tecla ENTER foca no próximo componente
		 */
		inputText.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_DOWN) {
					tabela.requestFocus();
				}
			}
		});

		sorter = new TableRowSorter<ModeloRelatorio>(modelo);
		tabela.setRowSorter(sorter);

		dimensionaColuna(tabela.getColumnModel());
		/**
		 * impede que o usuário mova as colunas
		 */
		tabela.getTableHeader().setReorderingAllowed(false);

		scroll = new JScrollPane(tabela);

		super.add(getpanelform());

		super.pack();

		setVisible(true);

		inputText.requestFocus();
	}

	private JPanel getpanelform() {

		FormLayout formlayout = new FormLayout("2dlu, 730px, 2dlu", "4dlu, pref, 4dlu, pref, 4dlu");
		JPanel jpanel = new JPanel(formlayout);
		jpanel.setBorder(BorderFactory.createTitledBorder("Consulta "));
		CellConstraints cellconstraints = new CellConstraints();

		DocumentListener myListener = new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				aplicaFiltro(inputText.getText());
			}
			public void insertUpdate(DocumentEvent e) {
				aplicaFiltro(inputText.getText());
			}
			public void removeUpdate(DocumentEvent e) {
				aplicaFiltro(inputText.getText());
			}
		};
		inputText.getDocument().addDocumentListener(myListener);

		jpanel.add(inputText, cellconstraints.xy(2, 2));
		jpanel.add(scroll, cellconstraints.xy(2, 4));

		return jpanel;
	}

	private void aplicaFiltro(String nome) {
		try {
			for(String caract: caracteresEspeciais){
				if(nome.contains(caract)){					
					nome = nome.replace(caract, "\\"+caract);									
				}		
			}
			filtro = RowFilter.regexFilter("(?i)" + nome);
		} catch (PatternSyntaxException e) {
			logger.debug(e.getMessage());
		}
		sorter.setRowFilter(filtro);
		if (sorter.getViewRowCount() > 0) {
			tabela.addRowSelectionInterval(0, 0);
		}
	}

	public TelaProduto getTelaProduto() {
		if (telaProduto == null) {
			telaProduto = new TelaProduto(null, categorias, fabricantes);
		}
		return telaProduto;
	}

	public void restaura() {
		this.setVisible(true);
		try {
			this.setIcon(false);
			this.setMaximum(false);
		} catch (PropertyVetoException e) {
			logger.error("erro ao restaurar a tela", e);
		}
	}

	public Object[] transformaToArray(ProdutoVO produto, int seq) {
		Object[] linha = new Object[QTD_COLUNA];
		int index = 0;
		linha[index++] = seq;
		linha[index++] = produto.getCodigo();
		linha[index++] = produto.getDescricao();
		linha[index++] = produto.getValor();
		linha[index++] = produto.getCategoria() == null ? "" : produto.getCategoria().getDescricao();
		linha[index++] = produto.getFabricante() == null ? "" : produto.getFabricante().getDescricao();
		return linha;
	}

	public void atualiza() throws BancoDeDadosException {
		List<ProdutoVO> produtos = getProdutoBO().getProdutoByNome("");
		mapa.clear();
		int index = 0;
		modelo.clean();
		for (ProdutoVO prod : produtos) {
			modelo.add(transformaToArray(prod, index + 1));
			mapa.put(index, prod);
			index++;
		}				
		sorter = new TableRowSorter<ModeloRelatorio>(modelo);
		tabela.setRowSorter(sorter);
	}

	public void dimensionaColuna(TableColumnModel modelo) {
		int index = 0;
		modelo.getColumn(index++).setPreferredWidth(4);
		modelo.getColumn(index++).setPreferredWidth(50);
		modelo.getColumn(index++).setPreferredWidth(300);
		modelo.getColumn(index++).setPreferredWidth(50);
		modelo.getColumn(index++).setPreferredWidth(70);
		modelo.getColumn(index++).setPreferredWidth(70);
	}

	public ProdutoBO getProdutoBO() {
		if (produtoBO == null) {
			produtoBO = new ProdutoBO();
		}
		return produtoBO;
	}

}
