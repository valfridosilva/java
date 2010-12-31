package br.com.ideia.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import org.apache.log4j.Logger;

import br.com.ideia.bean.ClienteVO;
import br.com.ideia.negocio.ClienteBO;
import br.com.ideia.util.BancoDeDadosException;
import br.com.ideia.util.Mensagem;
import br.com.ideia.util.MyJButton;
import br.com.ideia.util.Utilitaria;
import br.com.ideia.util.ValidacaoException;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Tela de manter usuário
 * 
 */
public class TelaCliente extends JInternalFrame {

	private static Logger logger = Logger.getLogger(TelaCliente.class);

	private static final long serialVersionUID = 1L;

	private JButton botaoPesquisar;
	private JButton botaoSalvar;
	private JButton botaoAlterar;
	private JButton botaoExcluir;
	private JButton botaoLimpar;
	private JLabel labelNome;
	private JLabel labelDataNascimento;
	private JLabel labelEndereco;
	private JLabel labelTelefone;
	private JLabel labelEmail;
	private JTextField campoNome;
	private JTextField campoEndereco;
	private JFormattedTextField campoTelefone;
	private JFormattedTextField campoDataNascimento;
	private JTextField campoEmail;
	private MaskFormatter formatData;
	private MaskFormatter formatTelefone;
	private ClienteVO cliente;
	private ClienteBO clienteBO;
	private TelaMenu telaMenu;
	private static final String TIPO_OBJETO = "Cliente";

	public TelaCliente(TelaMenu telaMenu) {
		super(TIPO_OBJETO, true, true, true, true);
		setVisible(true);
		setLayout(new FlowLayout());
		boolean flag = true;
		this.telaMenu = telaMenu;
		telaMenu.desktop.add(this);
		try {
			formatData = new MaskFormatter("##/##/####");
			formatTelefone = new MaskFormatter("(##)####-####");
		} catch (ParseException ex) {
			logger.debug("Erro ao criar parser", ex);
		}

		botaoPesquisar = new MyJButton(Mensagem.LABEL_PESQUISAR);
		botaoSalvar = new MyJButton(Mensagem.LABEL_SALVAR);
		botaoAlterar = new MyJButton(Mensagem.LABEL_ALTERAR);
		botaoExcluir = new MyJButton(Mensagem.LABEL_EXCLUIR);
		botaoLimpar = new MyJButton(Mensagem.LABEL_LIMPAR);

		labelNome = new JLabel("* Nome:");
		labelNome.setToolTipText("Nome");
		labelDataNascimento = new JLabel("Data Nascimento:");
		labelDataNascimento.setToolTipText("Data Nascimento");
		labelEndereco = new JLabel(" Endereço:");
		labelEndereco.setToolTipText("Endereço");
		labelTelefone = new JLabel("* Telefone:");
		labelTelefone.setToolTipText("Telefone");
		labelTelefone.setToolTipText("Telefone");
		labelEmail = new JLabel(" E-mail:");
		labelEmail.setToolTipText("Email");

		campoNome = new JTextField(100);
		campoEndereco = new JTextField(100);
		campoDataNascimento = new JFormattedTextField(formatData);
		campoDataNascimento.setFocusLostBehavior(JFormattedTextField.COMMIT);
		campoEmail = new JTextField(50);
		campoTelefone = new JFormattedTextField(formatTelefone);
		campoTelefone.setFocusLostBehavior(JFormattedTextField.COMMIT);

		campoNome.setEditable(flag);
		campoEndereco.setEditable(flag);
		campoDataNascimento.setEditable(flag);
		campoEmail.setEditable(flag);
		campoTelefone.setEditable(flag);

		habilitaBotoes(true);

		JPanel painel = getpanelform();

		super.add(painel);

		JPanel painelBotao = new JPanel();

		painelBotao.add(botaoSalvar);
		painelBotao.add(botaoAlterar);
		painelBotao.add(botaoExcluir);
		painelBotao.add(botaoLimpar);

		super.add(painelBotao);

		campoNome.requestFocus();

		super.pack();

	}

	public JPanel getpanelform() {

		FormLayout formlayout = new FormLayout(
				"2dlu, pref, 2dlu, 100px, 2dlu, pref, 2dlu, 90px, 2dlu, pref, 2dlu, 70px, 2dlu, 50px, 2dlu, 50px, 2dlu, 60px, 2dlu",
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
		jpanel.add(labelNome, cellconstraints.xy(2, 2));
		jpanel.add(campoNome, cellconstraints.xyw(4, 2, 9));
		jpanel.add(botaoPesquisar, cellconstraints.xyw(14, 2, 3));

		jpanel.add(labelTelefone, cellconstraints.xy(2, 4));
		jpanel.add(campoTelefone, cellconstraints.xy(4, 4));
		jpanel.add(labelDataNascimento, cellconstraints.xy(6, 4));
		jpanel.add(campoDataNascimento, cellconstraints.xy(8, 4));

		jpanel.add(labelEndereco, cellconstraints.xy(2, 6));
		jpanel.add(campoEndereco, cellconstraints.xyw(4, 6, 9));
		jpanel.add(labelEmail, cellconstraints.xy(2, 8));
		jpanel.add(campoEmail, cellconstraints.xyw(4, 8, 9));

		return jpanel;
	}

	protected void pesquisaPorNome() {
		try {
			if (!campoNome.getText().isEmpty()) {
				List<ClienteVO> lista = getClienteBO().getClienteByNome(campoNome.getText());
				if (!lista.isEmpty()) {
					if (lista.size() == 1) {
						setClienteToTela(lista.get(0));
						habilitaBotoes(false);
					} else {
						TelaListagemCliente tela = new TelaListagemCliente(telaMenu, lista, this);
						tela.setBounds(telaMenu.ESPACO_ENTRE_JANELA / 2, (int) (telaMenu.ESPACO_ENTRE_JANELA / 1.5), telaMenu.desktop.getWidth() - telaMenu.ESPACO_ENTRE_JANELA, telaMenu.desktop
								.getHeight()
								- (telaMenu.ESPACO_ENTRE_JANELA * 2));
						telaMenu.desktop.moveToFront(tela);
					}
				} else {
					JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), Mensagem.NENHUM_REGISTRO, Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), Mensagem.CAMPO_PESQUISA, Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
			}
		} catch (BancoDeDadosException e) {
			logger.error(Mensagem.ERRO_BANCO_DADOS, e);
			JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), Mensagem.ERRO_BANCO_DADOS, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error(Mensagem.ERRO_SISTEMA, e);
			JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), Mensagem.ERRO_SISTEMA, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		}

	}

	public void habilitaBotoes(boolean flag) {
		botaoSalvar.setVisible(flag);
		botaoAlterar.setVisible(!flag);
		botaoExcluir.setVisible(!flag);
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

	/**
	 * Recupera os campos da tela e seta no objeto cliente
	 * 
	 * @return
	 * @throws ValidacaoException
	 */
	public ClienteVO getClienteFomTela() throws ValidacaoException {
		ClienteVO cli = null;
		if (this.cliente != null) {
			cli = this.cliente;
		} else {
			cli = new ClienteVO();
		}
		cli.setNome(campoNome.getText());

		if (campoDataNascimento.getText().equals(campoDataNascimento.getValue())) {
			try {
				cli.setDataNascimento(Utilitaria.convertStringToDate(campoDataNascimento.getText(), Utilitaria.PATTERN_DDMMYYYY));
			} catch (ParseException e) {
				campoDataNascimento.requestFocus();
				throw new ValidacaoException(String.format(Mensagem.CAMPO_INVALIDO, campoDataNascimento.getText(),labelDataNascimento.getToolTipText()));
			}
		} else {
			if (campoDataNascimento.getText().trim().equals("/  /")) {
				cli.setDataNascimento(null);
			} else {
				throw new ValidacaoException(String.format(Mensagem.CAMPO_INVALIDO, campoDataNascimento.getText(), labelDataNascimento.getToolTipText()));
			}
		}
		if (!campoEmail.getText().isEmpty()) {
			cli.setEmail(campoEmail.getText());
		}
		if (!campoEndereco.getText().isEmpty()) {
			cli.setEndereco(campoEndereco.getText());
		}

		if(campoTelefone.getText().equals(campoTelefone.getValue())){
			cli.setTelefone(campoTelefone.getText());
		} else {
			if (campoTelefone.getText().trim().equals("(  )    -")) {
				cli.setTelefone(null);
			} else {
				throw new ValidacaoException(String.format(Mensagem.CAMPO_INVALIDO, campoTelefone.getText(), labelTelefone.getToolTipText()));
			}
		}
		return cli;
	}

	/**
	 * Seta nos campos da tela os dados do objeto cliente preenchido
	 * 
	 * @param user
	 * @param usuarioLogado
	 * @throws ValidacaoException
	 */
	public void setClienteToTela(ClienteVO cli) throws ValidacaoException {
		campoNome.setText(cli.getNome());
		campoTelefone.setValue(cli.getTelefone());
		campoEmail.setText(cli.getEmail());
		campoEndereco.setText(cli.getEndereco());
		try {
			campoDataNascimento.setValue(Utilitaria.convertDateToString(cli.getDataNascimento(), Utilitaria.PATTERN_DDMMYYYY));
		} catch (ParseException e) {
			throw new ValidacaoException(String.format(Mensagem.CAMPO_INVALIDO, campoDataNascimento.getText(), labelDataNascimento.getToolTipText()));
		}
		cliente = cli;
	}

	private void excluir() {
		try {
			Object[] options = { Mensagem.SIM, Mensagem.NAO };
			int res = JOptionPane.showOptionDialog(telaMenu.desktop.getSelectedFrame(), String.format(Mensagem.CONFIRMA_EXCLUSAO, cliente.getNome()), Mensagem.CONFIRMA,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (res == JOptionPane.YES_OPTION) {
				getClienteBO().excluiCliente(cliente);
				limpar();
				JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), String.format(Mensagem.REGISTRO_EXCLUIDO, TIPO_OBJETO), Mensagem.SUCESSO,
						JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (BancoDeDadosException e) {
			logger.error(Mensagem.ERRO_BANCO_DADOS, e);
			JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), Mensagem.ERRO_BANCO_DADOS, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error(Mensagem.ERRO_SISTEMA, e);
			JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), Mensagem.ERRO_SISTEMA, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		}

	}

	private void salvar() {
		try {
			ClienteVO cliente = getClienteFomTela();
			validaCliente(cliente);
			getClienteBO().insereCliente(cliente);
			limpar();
			JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), String.format(Mensagem.REGISTRO_INSERIDO, TIPO_OBJETO), Mensagem.SUCESSO,
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ValidacaoException e) {
			JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), e.getMessage(), Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
		} catch (EntityExistsException e) {
			JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), Mensagem.CLIENTE_DUPLICADO, Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
		} catch (BancoDeDadosException e) {
			logger.error(Mensagem.ERRO_BANCO_DADOS, e);
			JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), Mensagem.ERRO_BANCO_DADOS, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error(Mensagem.ERRO_SISTEMA, e);
			JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), Mensagem.ERRO_SISTEMA, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		}
	}

	private void validaCliente(ClienteVO cliente) throws ValidacaoException {
		if (cliente.getNome().isEmpty()) {
			campoNome.requestFocus();
			throw new ValidacaoException(String.format(Mensagem.CAMPO_OBRIGATORIO,labelNome.getToolTipText()));
		}
		if (cliente.getTelefone() == null) {
			campoTelefone.requestFocus();
			throw new ValidacaoException(String.format(Mensagem.CAMPO_OBRIGATORIO,labelTelefone.getToolTipText()));
		}
	}

	private void alterar() {
		try {
			ClienteVO cliente = getClienteFomTela();
			validaCliente(cliente);
			getClienteBO().alteraCliente(getClienteFomTela());
			limpar();
			JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), String.format(Mensagem.REGISTRO_ALTERADO, TIPO_OBJETO), Mensagem.SUCESSO,
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ValidacaoException e) {
			JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), e.getMessage(), Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
		} catch (EntityExistsException e) {
			JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), Mensagem.CLIENTE_DUPLICADO, Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
		} catch (BancoDeDadosException e) {
			logger.error(Mensagem.ERRO_BANCO_DADOS, e);
			JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), Mensagem.ERRO_BANCO_DADOS, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error(Mensagem.ERRO_SISTEMA, e);
			JOptionPane.showMessageDialog(telaMenu.desktop.getSelectedFrame(), Mensagem.ERRO_SISTEMA, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		}
	}

	public void limpar() {
		campoNome.setText("");
		campoTelefone.setText("");
		campoEmail.setText("");
		campoEndereco.setText("");
		campoDataNascimento.setText("");
		habilitaBotoes(true);
		campoNome.requestFocus();
		cliente = null;
	}

	public ClienteBO getClienteBO() {
		if (clienteBO == null) {
			clienteBO = new ClienteBO();
		}
		return clienteBO;
	}
}
