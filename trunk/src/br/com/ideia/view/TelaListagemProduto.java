package br.com.ideia.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import br.com.ideia.bean.ProdutoVO;
import br.com.ideia.util.Mensagem;
import br.com.ideia.util.ModeloRelatorio;
import br.com.ideia.util.Utilitaria;
/**
 * Utilizada quando a pesquisa retorna mais de um resultado
 *
 */
public class TelaListagemProduto extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTable tabela;	
	private ModeloRelatorio modelo; 
	private Map<Integer, ProdutoVO> mapa;
			
	public TelaListagemProduto(TelaMenu telaMenu, List<ProdutoVO> lista,final TelaProduto tela){		
		telaMenu.desktop.add(this);
		modelo = new ModeloRelatorio(new String[]{"Código","Descrição","Valor","Categoria","Fabricante"});
		mapa = new HashMap<Integer, ProdutoVO>();		
		int index = 0;
		for(ProdutoVO obj: lista){
			modelo.add(transformaToArray(obj));// adiciona o objeto ao relatório
			mapa.put(index, obj);
			index++;
		}		
		tabela = new JTable(modelo);
		tabela.setRowHeight(22); // tamanho da linha
		dimensionaColuna(tabela.getColumnModel()); 
		tabela.getTableHeader().setReorderingAllowed(false); // impede que o usuário mova as colunas 
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		tabela.setPreferredScrollableViewportSize(new Dimension(600,200));		
		tabela.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				trataEvento(tela);				
			}
		});
		
		InputMap im = tabela.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		im.put(enter, im.get(KeyStroke.getKeyStroke(KeyEvent.VK_GREATER, 0)));
		Action enterAction = new AbstractAction() {		
			private static final long serialVersionUID = -7691518916575740081L;
			public void actionPerformed(ActionEvent e) {
				modelo = (ModeloRelatorio)((JTable) e.getSource()).getModel();
				trataEvento(tela);
			}
		};
		tabela.getActionMap().put(im.get(enter), enterAction);
				
		setLayout(new FlowLayout());
		
		JButton botaoVoltar = new JButton(Mensagem.LABEL_VOLTAR);
					
		botaoVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				voltar();				
			}			
		});

		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane);
		add(BorderLayout.SOUTH,botaoVoltar);			
		setSize(650, 300);
		setTitle("Resultados encontrados!");		
		setVisible(true);
	}
		
	public Object[] transformaToArray(ProdutoVO produto) {		
		Object[] linha = new Object[5];		
		int index = 0;		
		linha[index++] = produto.getCodigo();
		linha[index++] = produto.getDescricao();
		linha[index++] = Utilitaria.formataValor(produto.getValor());	
		linha[index++] = produto.getCategoria()!=null?produto.getCategoria().getDescricao():"";
		linha[index++] = produto.getFabricante()!=null?produto.getFabricante().getDescricao():"";
		return linha;
	}
	
	public void dimensionaColuna(TableColumnModel modelo){
		modelo.getColumn(0).setPreferredWidth(40);			
	}
	
	private void voltar() {
		dispose();		
	}

	private void trataEvento(final TelaProduto tela) {
		int linha = tabela.getSelectedRow();	
		tela.setVisible(true);
		tela.setObjectToTela(mapa.get(linha));				
		tela.habilitaBotoes(false);
		voltar();
	}		
}


