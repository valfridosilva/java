package br.com.ideia.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
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
public class TelaCliente extends JFrame {

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
	private static final String TIPO_OBJETO = "Cliente";

	public TelaCliente() {
		super(TIPO_OBJETO);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout());
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		boolean flag = true;

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
		labelDataNascimento = new JLabel("Data Nascimento:");
		labelEndereco = new JLabel(" Endereço:");
		labelTelefone = new JLabel("* Telefone:");
		labelEmail = new JLabel(" E-mail:");
		
		campoNome = new JTextField(100);
		campoEndereco = new JTextField(100);
		campoDataNascimento = new JFormattedTextField(formatData);
		campoEmail = new JTextField(50);
		campoTelefone = new JFormattedTextField(formatTelefone);

		campoNome.setEditable(flag);
		campoEndereco.setEditable(flag);
		campoDataNascimento.setEditable(flag);
		campoEmail.setEditable(flag);
		campoTelefone.setEditable(flag);

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
		jpanel.add(labelNome, cellconstraints.xy(2, 2));
		jpanel.add(campoNome, cellconstraints.xyw(4, 2, 5));
		jpanel.add(botaoPesquisar, cellconstraints.xy(10, 2));

		jpanel.add(labelTelefone, cellconstraints.xy(2, 4));
		jpanel.add(campoTelefone, cellconstraints.xy(4, 4));
		jpanel.add(labelDataNascimento, cellconstraints.xy(6, 4));
		jpanel.add(campoDataNascimento, cellconstraints.xy(8, 4));

		jpanel.add(labelEndereco, cellconstraints.xy(2, 6));
		jpanel.add(campoEndereco, cellconstraints.xyw(4, 6, 5));
		jpanel.add(labelEmail, cellconstraints.xy(2, 8));
		jpanel.add(campoEmail, cellconstraints.xyw(4, 8, 5));

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
						TelaListagemCliente tela = new TelaListagemCliente(lista, this);
						tela.setLocationRelativeTo(null);
					}
				} else {
					JOptionPane.showMessageDialog(null, Mensagem.NENHUM_REGISTRO, Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, Mensagem.CAMPO_PESQUISA, Mensagem.ALERTA, JOptionPane.WARNING_MESSAGE);
			}
		} catch (BancoDeDadosException e) {
			logger.error(Mensagem.ERRO_BANCO_DADOS, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_BANCO_DADOS, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error(Mensagem.ERRO_SISTEMA, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_SISTEMA, Mensagem.ERRO, JOptionPane.ERROR_MESSAGE);
		}

	}

	public void habilitaBotoes(boolean flag) {
		botaoSalvar.setVisible(flag);
		botaoAlterar.setVisible(!flag);
		botaoExcluir.setVisible(!flag);
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

		if (campoDataNascimento.getText().trim().length() == 10) {
			try {
				cli.setDataNascimento(Utilitaria.convertStringToDate(campoDataNascimento.getText(), Utilitaria.PATTERN_DDMMYYYY));
			} catch (ParseException e) {
				campoDataNascimento.requestFocus();
				throw new ValidacaoException(String.format(Mensagem.DATA_INVALIDA, campoDataNascimento.getText()));
			}
		} else {
			cli.setDataNascimento(null);
		}
		if (!campoEmail.getText().isEmpty()) {
			cli.setEmail(campoEmail.getText());
		}
		if (!campoEndereco.getText().isEmpty()) {
			cli.setEndereco(campoEndereco.getText());
		}

		cli.setTelefone(campoTelefone.getText());
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
		campoTelefone.setText(cli.getTelefone());
		campoEmail.setText(cli.getEmail());
		campoEndereco.setText(cli.getEndereco());
		try {
			campoDataNascimento.setText(Utilitaria.convertDateToString(cli.getDataNascimento(), Utilitaria.PATTERN_DDMMYYYY));
		} catch (ParseException e) {
			throw new ValidacaoException(String.format(Mensagem.DATA_INVALIDA, campoDataNascimento.getText()));
		}
		cliente = cli;
	}

	private void excluir() {
		try {
			Object[] options = { Mensagem.SIM, Mensagem.NAO };
			int res = JOptionPane.showOptionDialog(null, String.format(Mensagem.CONFIRMA_EXCLUSAO, cliente.getNome()), Mensagem.CONFIRMA,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (res == JOptionPane.YES_OPTION) {
				getClienteBO().excluiCliente(cliente);
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
			validaCliente();
			getClienteBO().insereCliente(getClienteFomTela());
			limpar();
			JOptionPane.showMessageDialog(null, String.format(Mensagem.REGISTRO_INSERIDO, TIPO_OBJETO), Mensagem.SUCESSO, JOptionPane.INFORMATION_MESSAGE);
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

	private void validaCliente() throws ValidacaoException {

		if (campoNome.getText().trim().isEmpty()) {
			campoNome.requestFocus();
			throw new ValidacaoException(Mensagem.CAMPO_OBRIGATORIO);
		}
		if (campoTelefone.getText().trim().length() != 13) {
			campoTelefone.requestFocus();
			throw new ValidacaoException(Mensagem.CAMPO_OBRIGATORIO);
		}
	}

	private void alterar() {
		try {
			validaCliente();
			getClienteBO().alteraCliente(getClienteFomTela());
			limpar();
			JOptionPane.showMessageDialog(null, String.format(Mensagem.REGISTRO_ALTERADO, TIPO_OBJETO), Mensagem.SUCESSO, JOptionPane.INFORMATION_MESSAGE);
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
