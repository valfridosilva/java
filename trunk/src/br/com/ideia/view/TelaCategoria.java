package br.com.ideia.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import br.com.ideia.bean.CategoriaVO;
import br.com.ideia.negocio.ProdutoBO;
import br.com.ideia.util.BancoDeDadosException;
import br.com.ideia.util.Mensagem;
import br.com.ideia.util.MyJButton;
import br.com.ideia.util.RegistroEmUsoException;
import br.com.ideia.util.ValidacaoException;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class TelaCategoria extends JInternalFrame {

	private static Logger logger = Logger.getLogger(TelaCategoria.class);

	private static final long serialVersionUID = 1L;

	private JButton botaoPesquisar;
	private JButton botaoSalvar;
	private JButton botaoAlterar;
	private JButton botaoExcluir;
	private JButton botaoLimpar;
	private JLabel labelDescricao;
	private JTextField campoDescricao;
	private CategoriaVO categoria;
	private ProdutoBO produtoBO;
	private TelaMenu telaMenu;
	private static final String TIPO_OBJETO = "Categoria";

	public TelaCategoria(TelaMenu telaMenu) {
		super(TIPO_OBJETO, true, true, true, true);	
		setLayout(new FlowLayout());
		setVisible(true);
		boolean flag = true;
		this.telaMenu = telaMenu;
		telaMenu.desktop.add(this);
		botaoPesquisar = new MyJButton(Mensagem.LABEL_PESQUISAR);
		botaoSalvar = new MyJButton(Mensagem.LABEL_SALVAR);
		botaoAlterar = new MyJButton(Mensagem.LABEL_ALTERAR);
		botaoExcluir = new MyJButton(Mensagem.LABEL_EXCLUIR);
		botaoLimpar = new MyJButton(Mensagem.LABEL_LIMPAR);
		labelDescricao = new JLabel("* Descrição:");
		labelDescricao.setToolTipText("Descrição");
		campoDescricao = new JTextField(100);

		campoDescricao.setEditable(flag);

		habilitaBotoes(true);

		JPanel painel = getpanelform();

		super.add(painel);

		JPanel painelBotao = new JPanel();
		
		painelBotao.add(botaoSalvar);
		painelBotao.add(botaoAlterar);
		painelBotao.add(botaoExcluir);
		painelBotao.add(botaoLimpar);
				
		super.add(painelBotao);
		super.pack();

	}
	
	public JPanel getpanelform() {

		FormLayout formlayout = new FormLayout("2dlu, pref, 2dlu, 100px, 2dlu, 50px, 2dlu, 90px, 2dlu, 50px, 2dlu, 70px, 2dlu, 70px, 2dlu, 50px, 2dlu, 40px, 2dlu",
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
		jpanel.add(labelDescricao, cellconstraints.xy(2, 2));
		jpanel.add(campoDescricao, cellconstraints.xyw(4, 2, 9));
		jpanel.add(botaoPesquisar, cellconstraints.xyw(14, 2, 3));

		return jpanel;
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
		List<CategoriaVO> lista = getProdutoBO().getCategoriaByNome(chavePesquisa);
		if (!lista.isEmpty()) {
			if (lista.size() == 1) {
				setObjectToTela(lista.get(0));
				habilitaBotoes(false);
			} else {
				TelaListagemCategoria tela = new TelaListagemCategoria(lista, this);
				tela.setLocationRelativeTo(null);
			}
		} else {
			throw new ValidacaoException(Mensagem.NENHUM_REGISTRO);
		}
	}

	public void habilitaBotoes(boolean flag) {
		botaoSalvar.setVisible(flag);
		botaoAlterar.setVisible(!flag);
		botaoExcluir.setVisible(!flag);
	}

	public CategoriaVO getObjectFomTela() throws ValidacaoException {
		CategoriaVO obj = null;
		if (this.categoria != null) {
			obj = this.categoria;
		} else {
			obj = new CategoriaVO();
		}
		obj.setDescricao(campoDescricao.getText());

		return obj;
	}

	public void setObjectToTela(CategoriaVO obj) {
		campoDescricao.setText(obj.getDescricao());
		categoria = obj;
	}
	
	public void restaura(){
		this.setVisible(true);
		try {
			this.setIcon(false);
			this.setMaximum(false);
		} catch (PropertyVetoException e) {
			logger.error("erro ao restaurar a tela",e);
		}
	}

	private void excluir() {
		try {
			Object[] options = { Mensagem.SIM,Mensagem.NAO };
			int res = JOptionPane.showOptionDialog(null, String.format(Mensagem.CONFIRMA_EXCLUSAO, categoria.getDescricao()), Mensagem.CONFIRMA,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (res == JOptionPane.YES_OPTION) {
				getProdutoBO().excluiCategoria(categoria);
				limpar();
				JOptionPane.showMessageDialog(null, String.format(Mensagem.REGISTRO_EXCLUIDO, TIPO_OBJETO), Mensagem.SUCESSO,
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (RegistroEmUsoException e) {			
			JOptionPane.showMessageDialog(null, Mensagem.REGISTRO_EM_USO, Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
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
			validaCategoria();
			getProdutoBO().insereCategoria(getObjectFomTela());
			limpar();
			JOptionPane.showMessageDialog(null, String.format(Mensagem.REGISTRO_INSERIDO, TIPO_OBJETO), "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
		} catch (ValidacaoException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
		} catch (EntityExistsException e) {
			JOptionPane.showMessageDialog(null, String.format(Mensagem.REGISTRO_DUPLICADO,TIPO_OBJETO), Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
		} catch (BancoDeDadosException e) {
			logger.error(Mensagem.ERRO_BANCO_DADOS, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_BANCO_DADOS, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error(Mensagem.ERRO_SISTEMA, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_SISTEMA, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		}
	}

	private void validaCategoria() throws ValidacaoException {

		if (campoDescricao.getText().trim().isEmpty()) {
			campoDescricao.requestFocus();
			throw new ValidacaoException(String.format(Mensagem.CAMPO_OBRIGATORIO,labelDescricao.getToolTipText()));
		}
	}

	private void alterar() {
		try {
			validaCategoria();
			getProdutoBO().alteraCategoria(getObjectFomTela());
			limpar();
			telaMenu.atualizaPesquisaProduto();
			JOptionPane.showMessageDialog(null, String.format(Mensagem.REGISTRO_ALTERADO, TIPO_OBJETO), Mensagem.SUCESSO, JOptionPane.INFORMATION_MESSAGE);
		} catch (ValidacaoException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
		} catch (EntityExistsException e) {
			JOptionPane.showMessageDialog(null, String.format(Mensagem.REGISTRO_DUPLICADO,TIPO_OBJETO), Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
		} catch (BancoDeDadosException e) {
			logger.error(Mensagem.ERRO_BANCO_DADOS, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_BANCO_DADOS, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error(Mensagem.ERRO_SISTEMA, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_SISTEMA, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		} 
	}

	public void limpar() {
		campoDescricao.setText("");
		habilitaBotoes(true);
		campoDescricao.requestFocus();
		categoria = null;
	}

	public ProdutoBO getProdutoBO() {
		if (produtoBO == null) {
			produtoBO = new ProdutoBO();
		}
		return produtoBO;
	}

}
