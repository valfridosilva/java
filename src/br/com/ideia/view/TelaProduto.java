package br.com.ideia.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import org.apache.log4j.Logger;

import br.com.ideia.bean.CategoriaVO;
import br.com.ideia.bean.FabricanteVO;
import br.com.ideia.bean.ProdutoVO;
import br.com.ideia.negocio.ProdutoBO;
import br.com.ideia.util.BancoDeDadosException;
import br.com.ideia.util.Mensagem;
import br.com.ideia.util.MyJButton;
import br.com.ideia.util.ValidacaoException;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Tela de manter usuário
 * 
 */
public class TelaProduto extends JFrame {

	private static Logger logger = Logger.getLogger(TelaProduto.class);

	private static final long serialVersionUID = 1L;

	private JButton botaoPesquisar;
	private JButton botaoSalvar;
	private JButton botaoAlterar;
	private JButton botaoExcluir;
	private JButton botaoLimpar;
	private JLabel labelCodigo;
	private JLabel labelDescricao;
	private JLabel labelValor;
	private JLabel labelCategoria;
	private JLabel labelFabricante;
	private JTextField campoCodigo;
	private JTextField campoDescricao;
	private JFormattedTextField campoValor;
	private JComboBox campoCategoria;
	private JComboBox campoFabricante;
	private MaskFormatter formatValor;
	private ProdutoVO produto;
	private ProdutoBO produtoBO;
	private static final String TIPO_OBJETO = "Produto";

	public TelaProduto(List<CategoriaVO> categorias, List<FabricanteVO> fabricantes) {
		super(TIPO_OBJETO);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout());
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		boolean flag = true;

		try {
			formatValor = new MaskFormatter("######.##");
		} catch (ParseException ex) {
			logger.debug("Erro ao criar parser", ex);
		}

		botaoPesquisar = new MyJButton(Mensagem.LABEL_PESQUISAR);
		botaoSalvar = new MyJButton(Mensagem.LABEL_SALVAR);
		botaoAlterar = new MyJButton(Mensagem.LABEL_ALTERAR);
		botaoExcluir = new MyJButton(Mensagem.LABEL_EXCLUIR);
		botaoLimpar = new MyJButton(Mensagem.LABEL_LIMPAR);

		labelDescricao = new JLabel("* Descrição:");
		labelCodigo = new JLabel("Código:");
		labelValor = new JLabel("Valor:");
		labelCategoria = new JLabel("Categoria:");
		labelFabricante = new JLabel("Fabricante:");

		campoCodigo = new JTextField(100);
		campoDescricao = new JTextField(100);
		campoValor = new JFormattedTextField(formatValor);
		categorias.add(0, null);
		fabricantes.add(0, null);
		campoCategoria = new JComboBox(categorias.toArray());
		campoFabricante = new JComboBox(fabricantes.toArray());

		campoCodigo.setEditable(flag);
		campoDescricao.setEditable(flag);
		campoValor.setEditable(flag);

		habilitaBotoes(true);

		JPanel painel = getpanelform();

		super.add(painel);

		getContentPane().add(botaoSalvar);
		getContentPane().add(botaoAlterar);
		getContentPane().add(botaoExcluir);
		getContentPane().add(botaoLimpar);
		super.pack();

	}

	public JPanel getpanelform() {

		FormLayout formlayout = new FormLayout("2dlu, pref, 2dlu, 100px, 2dlu, pref, 2dlu, 90px, 2dlu, pref, 2dlu, 100px, 2dlu, 100px, 2dlu, 50px",
				"2dlu, top:pref, 2dlu, top:pref, 2dlu, top:pref, 2dlu, pref, 2dlu, pref, 8dlu, pref, 8dlu");
		JPanel jpanel = new JPanel(formlayout);
		jpanel.setBorder(BorderFactory.createTitledBorder("Dados "));
		CellConstraints cellconstraints = new CellConstraints();

		botaoSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				salvar();
			}
		});
		botaoAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				alterar();
			}
		});
		botaoExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				excluir();
			}
		});
		botaoLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				limpar();
			}
		});
		botaoPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pesquisaPorNome();
			}
		});
		jpanel.add(labelCodigo, cellconstraints.xy(2, 2));
		jpanel.add(campoCodigo, cellconstraints.xyw(4, 2, 5));
		jpanel.add(labelDescricao, cellconstraints.xy(2, 4));
		jpanel.add(campoDescricao, cellconstraints.xyw(4, 4, 9));
		jpanel.add(botaoPesquisar, cellconstraints.xy(14, 4));
		// jpanel.add(botaoPesquisar, cellconstraints.xy(10, 2));

		jpanel.add(labelValor, cellconstraints.xyw(2, 6, 3));
		jpanel.add(campoValor, cellconstraints.xyw(4, 6, 3));
		jpanel.add(labelCategoria, cellconstraints.xy(8, 6));
		jpanel.add(campoCategoria, cellconstraints.xyw(10, 6, 5));

		jpanel.add(labelFabricante, cellconstraints.xy(2, 8));
		jpanel.add(campoFabricante, cellconstraints.xyw(4, 8, 5));

		return jpanel;
	}

	public void habilitaBotoes(boolean flag) {
		botaoSalvar.setVisible(flag);
		botaoAlterar.setVisible(!flag);
		botaoExcluir.setVisible(!flag);
	}

	/**
	 * Recupera os campos da tela e seta no objeto
	 * 
	 * @return
	 * @throws ValidacaoException
	 */
	public ProdutoVO getObjectFomTela() throws ValidacaoException {
		ProdutoVO prod = null;
		if (this.produto != null) {
			prod = this.produto;
		} else {
			prod = new ProdutoVO();
		}
		if (!campoCodigo.getText().trim().isEmpty()) {
			try {
				prod.setCodigo(Long.parseLong(campoCodigo.getText()));
			} catch (NumberFormatException e) {
				throw new ValidacaoException(String.format(Mensagem.VALOR_INVALIDO, "Código"));
			}
		}else{
			prod.setCodigo(null);
		}

		prod.setDescricao(campoDescricao.getText());
		if (!campoValor.getText().trim().isEmpty()) {
			try {
				prod.setValor(Double.parseDouble(campoValor.getText()));
			} catch (NumberFormatException e) {
				throw new ValidacaoException(String.format(Mensagem.VALOR_INVALIDO, "Valor"));
			}
		}
		if(campoCategoria.getSelectedItem()!=null){
			prod.setCategoria((CategoriaVO) campoCategoria.getSelectedItem());
		}else{
			prod.setCategoria(null);
		}
		if(campoFabricante.getSelectedItem()!=null){
			prod.setFabricante((FabricanteVO) campoFabricante.getSelectedItem());
		}else{
			prod.setFabricante(null);
		}
		
		return prod;
	}

	/**
	 * Seta nos campos da tela os dados do objeto preenchido
	 * 
	 * @param user
	 * @throws ValidacaoException
	 */
	public void setObjectToTela(ProdutoVO prod) {
		if(prod.getCodigo()!=null){
			campoCodigo.setText(String.valueOf(prod.getCodigo()));
		}		
		campoDescricao.setText(prod.getDescricao());
		campoValor.setText(String.valueOf(prod.getValor()));
		if(prod.getCategoria() != null){
			campoCategoria.setSelectedIndex(prod.getCategoria().getId());
		}
		if(prod.getFabricante() != null){
			campoFabricante.setSelectedIndex(prod.getFabricante().getId());
		}	
		produto = prod;
	}
	
	protected void pesquisaPorNome() {
		try {				
			montaPesquisa(campoDescricao.getText());			
		} catch (ValidacaoException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
		} catch (BancoDeDadosException e) {
			logger.error(Mensagem.ERRO_BANCO_DADOS, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_BANCO_DADOS, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error(Mensagem.ERRO_SISTEMA, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_SISTEMA, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void montaPesquisa(String chavePesquisa) throws ValidacaoException,BancoDeDadosException {
		if(chavePesquisa.trim().isEmpty()){
			campoDescricao.requestFocus();
			throw new ValidacaoException(Mensagem.CAMPO_PESQUISA);
		}
		List<ProdutoVO> lista = getProdutoBO().getProdutoByNome(chavePesquisa);
		if (!lista.isEmpty()) {
			if (lista.size() == 1) {
				setObjectToTela(lista.get(0));
				habilitaBotoes(false);
			} else {
				TelaListagemProduto tela = new TelaListagemProduto(lista,this);
				tela.setLocationRelativeTo(null);
			}
		} else {
			throw new ValidacaoException(Mensagem.NENHUM_REGISTRO);
		}
	}

	private void excluir() {
		try {
			Object[] options = { Mensagem.SIM, Mensagem.NAO };
			int res = JOptionPane.showOptionDialog(null, String.format(Mensagem.CONFIRMA_EXCLUSAO, produto.getDescricao()), Mensagem.CONFIRMA,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (res == JOptionPane.YES_OPTION) {
				getProdutoBO().excluiProduto(produto);
				limpar();
				JOptionPane.showMessageDialog(null, String.format(Mensagem.REGISTRO_EXCLUIDO, TIPO_OBJETO), Mensagem.SUCESSO,
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (BancoDeDadosException e) {
			logger.error(Mensagem.ERRO_BANCO_DADOS, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_BANCO_DADOS, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error(Mensagem.ERRO_SISTEMA, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_SISTEMA, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		}

	}

	private void salvar() {
		try {
			validaProduto();
			getProdutoBO().insereProduto(getObjectFomTela());
			limpar();
			JOptionPane.showMessageDialog(null, String.format(Mensagem.REGISTRO_INSERIDO, TIPO_OBJETO), Mensagem.SUCESSO,
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ValidacaoException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
		} catch (EntityExistsException e) {
			JOptionPane.showMessageDialog(null, Mensagem.CLIENTE_DUPLICADO, Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
		} catch (BancoDeDadosException e) {
			logger.error(Mensagem.ERRO_BANCO_DADOS, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_BANCO_DADOS, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error(Mensagem.ERRO_SISTEMA, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_SISTEMA, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		}
	}

	private void validaProduto() throws ValidacaoException {

		if (campoDescricao.getText().trim().isEmpty()) {
			campoDescricao.requestFocus();
			throw new ValidacaoException(Mensagem.CAMPO_OBRIGATORIO);
		}
		if (campoValor.getText().trim().isEmpty()) {
			campoValor.requestFocus();
			throw new ValidacaoException(Mensagem.CAMPO_OBRIGATORIO);
		}
	}

	private void alterar() {
		try {
			validaProduto();
			getProdutoBO().alteraProduto(getObjectFomTela());
			limpar();
			JOptionPane.showMessageDialog(null, String.format(Mensagem.REGISTRO_ALTERADO, TIPO_OBJETO), Mensagem.SUCESSO,
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ValidacaoException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
		} catch (EntityExistsException e) {
			JOptionPane.showMessageDialog(null, Mensagem.CLIENTE_DUPLICADO, Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
		} catch (BancoDeDadosException e) {
			logger.error(Mensagem.ERRO_BANCO_DADOS, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_BANCO_DADOS, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error(Mensagem.ERRO_SISTEMA, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_SISTEMA, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		}
	}

	public void limpar() {
		campoCodigo.setText("");
		campoDescricao.setText("");
		campoValor.setText("");
		campoCategoria.setSelectedIndex(0);
		campoFabricante.setSelectedIndex(0);
		habilitaBotoes(true);
		campoDescricao.requestFocus();
		produto = null;
	}

	public ProdutoBO getProdutoBO() {
		if (produtoBO == null) {
			produtoBO = new ProdutoBO();
		}
		return produtoBO;
	}
}
