package br.com.ideia.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
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

import org.apache.log4j.Logger;

import br.com.ideia.bean.ClienteVO;
import br.com.ideia.util.Mensagem;
import br.com.ideia.util.ModeloRelatorio;
import br.com.ideia.util.Utilitaria;
import br.com.ideia.util.ValidacaoException;
/**
 * Utilizada quando a pesquisa de cliente retorna mais de um resultado
 *
 */
public class TelaListagemCliente extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTable tabela;	
	private ModeloRelatorio modelo; 
	private Map<Integer, ClienteVO> mapa;
	private Logger logger;
		
	public TelaListagemCliente(TelaMenu telaMenu, List<ClienteVO> lista,final TelaCliente telaCliente){
		telaMenu.desktop.add(this);
		logger = Logger.getLogger(getClass());
		modelo = new ModeloRelatorio(new String[]{ "Nome", "Telefone", "Dt Nascim.", "Endereço", "Email"});
		mapa = new HashMap<Integer, ClienteVO>();		
		int index = 0;
		for(ClienteVO cliente: lista){
			modelo.add(transformaToArray(cliente));// adiciona o objeto ao relatório
			mapa.put(index, cliente);
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
				trataEvento(telaCliente);				
			}
		});
		
		InputMap im = tabela.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		im.put(enter, im.get(KeyStroke.getKeyStroke(KeyEvent.VK_GREATER, 0)));
		Action enterAction = new AbstractAction() {		
			private static final long serialVersionUID = -7691518916575740081L;
			public void actionPerformed(ActionEvent e) {
				modelo = (ModeloRelatorio)((JTable) e.getSource()).getModel();
				trataEvento(telaCliente);
			}
		};
		tabela.getActionMap().put(im.get(enter), enterAction);
		
		super.setLayout(new FlowLayout());
		
		JButton botaoVoltar = new JButton(Mensagem.LABEL_VOLTAR);
					
		botaoVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				voltar();				
			}			
		});

		JScrollPane scrollPane = new JScrollPane(tabela);
		super.add(scrollPane);
		super.add(BorderLayout.SOUTH,botaoVoltar);			
		setSize(650, 300);
		setTitle("Resultados encontrados!");	
		setVisible(true);
	}
		
	public Object[] transformaToArray(ClienteVO cliente) {		
		Object[] linha = new Object[5];	
		int index = 0;
		linha[index++] = cliente.getNome();
		linha[index++] = cliente.getTelefone();
		try {
			linha[index++] = Utilitaria.convertDateToString(cliente.getDataNascimento(),Utilitaria.PATTERN_DDMMYYYY);
		} catch (ParseException e) {			
			logger.error(e);
		}
		linha[index++] = cliente.getEndereco();
		linha[index++] = cliente.getEmail();		
		return linha;
	}
	
	public void dimensionaColuna(TableColumnModel modelo){
		int index = 0;
		modelo.getColumn(index++).setPreferredWidth(40);		
		modelo.getColumn(index++).setPreferredWidth(35);		
		modelo.getColumn(index++).setPreferredWidth(15);
		modelo.getColumn(index++).setPreferredWidth(15);	
		modelo.getColumn(index++).setPreferredWidth(5);		
	}
	
	private void voltar() {
		dispose();		
	}		
	
	private void trataEvento(final TelaCliente tela) {
		int linha = tabela.getSelectedRow();				
		try {
			tela.setClienteToTela(mapa.get(linha));
		} catch (ValidacaoException e1) {
			logger.debug(e1.getMessage(),e1);
		}
		tela.habilitaBotoes(false);
		voltar();
	}	
}

