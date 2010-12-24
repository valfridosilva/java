package br.com.ideia.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import br.com.ideia.bean.FabricanteVO;
import br.com.ideia.negocio.ProdutoBO;
import br.com.ideia.util.BancoDeDadosException;
import br.com.ideia.util.Mensagem;
import br.com.ideia.util.MyJButton;
import br.com.ideia.util.ValidacaoException;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class TelaFabricante extends JFrame {

	private static Logger logger = Logger.getLogger(TelaFabricante.class);

	private static final long serialVersionUID = 1L;

	private JButton botaoPesquisar;
	private JButton botaoSalvar;
	private JButton botaoAlterar;
	private JButton botaoExcluir;
	private JButton botaoLimpar;
	private JLabel labelDescricao;
	private JTextField campoDescricao;
	private FabricanteVO fabricante;
	private ProdutoBO produtoBO;
	private static final String TIPO_OBJETO = "Fabricante";

	public TelaFabricante() {
		super(TIPO_OBJETO);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout());
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		boolean flag = true;

		botaoPesquisar = new MyJButton(Mensagem.LABEL_PESQUISAR);
		botaoSalvar = new MyJButton(Mensagem.LABEL_SALVAR);
		botaoAlterar = new MyJButton(Mensagem.LABEL_ALTERAR);
		botaoExcluir = new MyJButton(Mensagem.LABEL_EXCLUIR);
		botaoLimpar = new MyJButton(Mensagem.LABEL_LIMPAR);
		labelDescricao = new JLabel("* Descrição:");

		campoDescricao = new JTextField(100);

		campoDescricao.setEditable(flag);

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

		FormLayout formlayout = new FormLayout("2dlu, pref, 2dlu, 100px, 2dlu, pref, 2dlu, 90px, 2dlu, pref, 2dlu, 70px, 2dlu",
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
		jpanel.add(campoDescricao, cellconstraints.xyw(4, 2, 5));
		jpanel.add(botaoPesquisar, cellconstraints.xy(10, 2));

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
		List<FabricanteVO> lista = getProdutoBO().getFabricanteByNome(chavePesquisa);
		if (!lista.isEmpty()) {
			if (lista.size() == 1) {
				setObjectToTela(lista.get(0));
				habilitaBotoes(false);
			} else {
				TelaListagemFabricante tela = new TelaListagemFabricante(lista, this);
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

	public FabricanteVO getObjectFomTela() throws ValidacaoException {
		FabricanteVO obj = null;
		if (this.fabricante != null) {
			obj = this.fabricante;
		} else {
			obj = new FabricanteVO();
		}
		obj.setDescricao(campoDescricao.getText());

		return obj;
	}

	public void setObjectToTela(FabricanteVO obj) {
		campoDescricao.setText(obj.getDescricao());
		fabricante = obj;
	}

	private void excluir() {
		try {
			Object[] options = { Mensagem.SIM, Mensagem.NAO };
			int res = JOptionPane.showOptionDialog(null, String.format(Mensagem.CONFIRMA_EXCLUSAO, fabricante.getDescricao()), Mensagem.CONFIRMA,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (res == JOptionPane.YES_OPTION) {
				getProdutoBO().excluiFabricante(fabricante);
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
			validaFabricante();
			getProdutoBO().insereFabricante(getObjectFomTela());
			limpar();
			JOptionPane.showMessageDialog(null, String.format(Mensagem.REGISTRO_INSERIDO, TIPO_OBJETO), Mensagem.SUCESSO, JOptionPane.INFORMATION_MESSAGE);
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

	private void validaFabricante() throws ValidacaoException {

		if (campoDescricao.getText().trim().isEmpty()) {
			campoDescricao.requestFocus();
			throw new ValidacaoException(Mensagem.CAMPO_OBRIGATORIO);
		}
	}

	private void alterar() {
		try {
			validaFabricante();
			getProdutoBO().alteraFabricante(getObjectFomTela());
			limpar();
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
		fabricante = null;
	}

	public ProdutoBO getProdutoBO() {
		if (produtoBO == null) {
			produtoBO = new ProdutoBO();
		}
		return produtoBO;
	}

}
