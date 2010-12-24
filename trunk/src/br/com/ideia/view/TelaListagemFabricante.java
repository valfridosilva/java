package br.com.ideia.view;

import java.awt.BorderLayout;
import java.awt.Container;
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
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import br.com.ideia.bean.FabricanteVO;
import br.com.ideia.util.Mensagem;
import br.com.ideia.util.ModeloRelatorio;
/**
 * Utilizada quando a pesquisa retorna mais de um resultado
 *
 */
public class TelaListagemFabricante extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable tabela;	
	private ModeloRelatorio modelo; 
	private Map<Integer, FabricanteVO> mapa;
			
	public TelaListagemFabricante(List<FabricanteVO> lista,final TelaFabricante tela){		
		modelo = new ModeloRelatorio(new String[]{"Descricao"});
		mapa = new HashMap<Integer, FabricanteVO>();		
		int index = 0;
		for(FabricanteVO obj: lista){
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

		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		
		JButton botaoVoltar = new JButton(Mensagem.LABEL_VOLTAR);
					
		botaoVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				voltar();				
			}			
		});

		JScrollPane scrollPane = new JScrollPane(tabela);
		c.add(scrollPane);
		c.add(BorderLayout.SOUTH,botaoVoltar);			
		setSize(650, 300);
		setTitle("Resultados encontrados!");
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(true);
	}
		
	public Object[] transformaToArray(FabricanteVO obj) {		
		Object[] linha = new Object[1];		
		linha[0] = obj.getDescricao();	
		return linha;
	}
	
	public void dimensionaColuna(TableColumnModel modelo){
		modelo.getColumn(0).setPreferredWidth(40);			
	}
	
	private void voltar() {
		dispose();		
	}		
	
	private void trataEvento(final TelaFabricante tela) {
		int linha = tabela.getSelectedRow();	
		tela.setVisible(true);
		tela.setObjectToTela(mapa.get(linha));				
		tela.habilitaBotoes(false);
		voltar();
	}	
}


