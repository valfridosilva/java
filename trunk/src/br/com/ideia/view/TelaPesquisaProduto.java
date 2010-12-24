package br.com.ideia.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
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
import br.com.ideia.util.ModeloRelatorio;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class TelaPesquisaProduto extends JFrame {
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

	public TelaPesquisaProduto(List<ProdutoVO> produtos, List<CategoriaVO> categorias, List<FabricanteVO> fabricantes) {
		setTitle("Pesquisa!");

		inputText = new JTextField(80);
		inputText.setEditable(true);	
		
		this.categorias = categorias;
		this.fabricantes = fabricantes;
				
		mapa = new HashMap<Integer, ProdutoVO>();	
		modelo = new ModeloRelatorio(new String[]{"Seq.","Código","Descrição","Valor","Categoria","Fabricante"});
		
		int index = 0;
		for (ProdutoVO prod : produtos) {
			modelo.add(transformaToArray(prod, index+1));
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

		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(true);

		inputText.requestFocus();
	}

	private JPanel getpanelform() {

		FormLayout formlayout = new FormLayout("2dlu, 700px, 2dlu", "4dlu, pref , 4dlu, pref, 4dlu");
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
			filtro = RowFilter.regexFilter("(?i)" + nome);
		} catch (PatternSyntaxException e) {
			logger.debug("Regex inválida",e);
		}
		sorter.setRowFilter(filtro);
		if (sorter.getViewRowCount() > 0) {
			tabela.addRowSelectionInterval(0, 0);
		}
	}

	public Object[] transformaToArray(ProdutoVO produto, int seq) {
		Object[] linha = new Object[QTD_COLUNA];
		int index = 0;
		linha[index++] = seq;
		linha[index++] = produto.getCodigo();
		linha[index++] = produto.getDescricao();
		linha[index++] = produto.getValor();
		linha[index++] = produto.getCategoria()==null?"":produto.getCategoria().getDescricao();
		linha[index++] = produto.getFabricante()==null?"":produto.getFabricante().getDescricao();
		return linha;
	}
	
	public TelaProduto getTelaProduto() {
		if (telaProduto == null) {			
			telaProduto = new TelaProduto(categorias,fabricantes);
			telaProduto.setSize(600, 260);
			telaProduto.setLocationRelativeTo(null);	
		}		
		return telaProduto;
	}

	public void dimensionaColuna(TableColumnModel modelo) {
		int index = 0;
		modelo.getColumn(index++).setPreferredWidth(4);
		modelo.getColumn(index++).setPreferredWidth(50);
		modelo.getColumn(index++).setPreferredWidth(300);
		modelo.getColumn(index++).setPreferredWidth(50);
		modelo.getColumn(index++).setPreferredWidth(60);
		modelo.getColumn(index++).setPreferredWidth(60);
	}	
}


