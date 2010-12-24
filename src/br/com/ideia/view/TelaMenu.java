package br.com.ideia.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import br.com.ideia.bean.CategoriaVO;
import br.com.ideia.bean.FabricanteVO;
import br.com.ideia.bean.ProdutoVO;
import br.com.ideia.importacao.ArquivoSER;
import br.com.ideia.negocio.ClienteBO;
import br.com.ideia.negocio.ProdutoBO;
import br.com.ideia.util.BancoDeDadosException;
import br.com.ideia.util.FabricaConexao;
import br.com.ideia.util.Mensagem;
import br.com.ideia.util.Utilitaria;
import br.com.ideia.util.ValidacaoException;

import com.blackbear.flatworm.errors.FlatwormException;

/**
 * Menu do sistema
 * 
 */
public class TelaMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel painel;
	private Icon logo;
	private ClienteBO clienteBO;
	private ProdutoBO produtoBO;
	private List<CategoriaVO> categorias;
	private List<FabricanteVO> fabricantes;	
	private static Logger logger = Logger.getLogger(TelaMenu.class);

	// método construtor
	public TelaMenu() {
		super("Ideia - Sistema de Cadastro de Clientes");
		Utilitaria util = new Utilitaria();
		JMenuBar menuBar = new JMenuBar();
		JMenu cadastro = new JMenu("Cadastro");
		cadastro.setMnemonic('C');
		JMenuItem manterCliente = new JMenuItem("Cliente");
		manterCliente.setMnemonic('L');
		manterCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				cadastrarCliente();
			}
		});
		cadastro.add(manterCliente);
		
		
		JMenuItem manterProduto = new JMenuItem("Produto");
		manterProduto.setMnemonic('P');
		manterProduto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				cadastrarProduto();
			}
		});
		cadastro.add(manterProduto);
			
		JMenuItem manterCategoria = new JMenuItem("Categoria");
		manterCategoria.setMnemonic('T');
		manterCategoria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				cadastrarCategoria();
			}
		});
		cadastro.add(manterCategoria);
		
		JMenuItem manterFabricante = new JMenuItem("Fabricante");
		manterFabricante.setMnemonic('F');
		manterFabricante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				cadastrarFabricante();
			}
		});
		cadastro.add(manterFabricante);

		// Sistema
		JMenu sistema = new JMenu("Sistema");
		sistema.setMnemonic('T');

		JMenuItem gerarBackup = new JMenuItem("Gerar Backup");
		gerarBackup.setMnemonic('G');
		gerarBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				gerarBackup();
			}
		});
		sistema.add(gerarBackup);

		JMenuItem restaurarBackup = new JMenuItem("Restaurar Backup");
		restaurarBackup.setMnemonic('R');
		restaurarBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				restaurarBackup();
			}
		});
		sistema.add(restaurarBackup);
		sistema.addSeparator();
		
		JMenuItem sobre = new JMenuItem("Sobre");
		sobre.setMnemonic('S');
		sobre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				JOptionPane.showMessageDialog(null, "Sistema Desenvolvido por:\nValfrido Ferreira\nVersão 1.0","",1,new Utilitaria().getImagemJava());
			}
		});
		sistema.add(sobre);
		sistema.addSeparator();

		JMenuItem sair = new JMenuItem("Sair");
		sair.setMnemonic('S');
		sair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sair();
			}
		});
		sistema.add(sair);
		// Pesquisa
		JMenu consultar = new JMenu("Consultar");
		consultar.setMnemonic('S');
		
		JMenuItem consultarProduto = new JMenuItem("Produto");
		consultarProduto.setMnemonic('P');
		consultarProduto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				consultarProduto();
			}		
		});
		consultar.add(consultarProduto);


		menuBar.add(sistema);
		menuBar.add(cadastro);
		menuBar.add(consultar);

		logo = util.getImagemLogo();
		painel = new JLabel("Sistema de Clientes..Seja Bem Vindo!", logo, SwingConstants.CENTER);
		painel.setVerticalTextPosition(JLabel.TOP);
		painel.setHorizontalTextPosition(JLabel.CENTER);
		painel.setForeground(Color.BLUE);
		painel.setFont(new Font("Arial", Font.ITALIC, 15));
		add(painel, BorderLayout.CENTER);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				sair();
			}
		});

		setJMenuBar(menuBar);

		setLocationRelativeTo(null);
		setVisible(true);

	} // fim do construtor

	protected void cadastrarCliente() {
		TelaCliente t = new TelaCliente();
		t.setSize(550, 230);
		t.setLocationRelativeTo(null);		
	}
	
	protected void cadastrarProduto() {		
		try {			
			TelaProduto t = new TelaProduto(getCategorias(),getFabricantes());
			t.setSize(600, 260);
			t.setLocationRelativeTo(null);	
		} catch (BancoDeDadosException e) {
			logger.error("Não foi possível consultar Categorias ou Fabricantes",e);			
		}				
	}
	
	protected void cadastrarCategoria() {		
		TelaCategoria t = new TelaCategoria();
		t.setSize(480, 190);
		t.setLocationRelativeTo(null);		
		setCategorias(null);
		setFabricantes(null);
	}
	
	protected void cadastrarFabricante() {
		TelaFabricante t = new TelaFabricante();
		t.setSize(480, 190);
		t.setLocationRelativeTo(null);	
		setFabricantes(null);
		setCategorias(null);
	}

	private void gerarBackup() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new MyFilter());
		try {
			fileChooser.setCurrentDirectory(new File(new File(".").getCanonicalPath()));
			if (fileChooser.showSaveDialog(fileChooser) == javax.swing.JFileChooser.APPROVE_OPTION) {
				File arquivoSelecionado = fileChooser.getSelectedFile();
				getClienteBO().exportarCliente(arquivoSelecionado.getAbsolutePath());
				JOptionPane.showMessageDialog(null, Mensagem.ARQUIVO_EXPORTADO, "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (ValidacaoException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Alerta!", JOptionPane.WARNING_MESSAGE);
		} catch (FlatwormException e) {
			logger.debug(Mensagem.ERRO_IMPORTACAO_ARQUIVO, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_IMPORTACAO_ARQUIVO, "Erro!", JOptionPane.ERROR_MESSAGE);
		} catch (BancoDeDadosException e) {
			logger.error(Mensagem.ERRO_BANCO_DADOS, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_BANCO_DADOS, "Erro!", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error(Mensagem.ERRO_SISTEMA, e);
			JOptionPane.showMessageDialog(null, Mensagem.ERRO_SISTEMA, "Erro!", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void restaurarBackup() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new MyFilter());
		if (fileChooser.showOpenDialog(fileChooser) == javax.swing.JFileChooser.APPROVE_OPTION) {
			try {
				File arquivoSelecionado = fileChooser.getSelectedFile();
				getClienteBO().importarCliente(arquivoSelecionado);
				JOptionPane.showMessageDialog(null, Mensagem.ARQUIVO_IMPORTADO, "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
			} catch (ValidacaoException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Alerta!", JOptionPane.WARNING_MESSAGE);
			} catch (EntityExistsException e) {
				JOptionPane.showMessageDialog(null, Mensagem.CLIENTE_DUPLICADO, "Alerta!", JOptionPane.WARNING_MESSAGE);
			} catch (FlatwormException e) {
				logger.debug(Mensagem.ERRO_IMPORTACAO_ARQUIVO, e);
				JOptionPane.showMessageDialog(null, Mensagem.ERRO_IMPORTACAO_ARQUIVO, "Erro!", JOptionPane.ERROR_MESSAGE);
			} catch (BancoDeDadosException e) {
				logger.error(Mensagem.ERRO_BANCO_DADOS, e);
				JOptionPane.showMessageDialog(null, Mensagem.ERRO_BANCO_DADOS, "Erro!", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				logger.error(Mensagem.ERRO_SISTEMA, e);
				JOptionPane.showMessageDialog(null, Mensagem.ERRO_SISTEMA, "Erro!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void consultarProduto() {
		try {			
			List<ProdutoVO> produtos = getProdutoBO().getProdutoByNome("");
			TelaPesquisaProduto t = new TelaPesquisaProduto(produtos,getCategorias(),getFabricantes());
			t.setSize(730, 530);
			t.setLocationRelativeTo(null);	
		} catch (BancoDeDadosException e) {
			logger.error("Não foi possível consultar Produtos, Categorias ou Fabricantes",e);			
		}				
	}
	
	public ClienteBO getClienteBO() {
		if (clienteBO == null) {
			clienteBO = new ClienteBO();
		}
		return clienteBO;
	}		
	
	public List<CategoriaVO> getCategorias() throws BancoDeDadosException{		
		if (categorias == null) {
			categorias = getProdutoBO().getCategoriaByNome("");		
		}
		return categorias;
	}

	public void setCategorias(List<CategoriaVO> categorias) {
		this.categorias = categorias;
	}

	public void setFabricantes(List<FabricanteVO> fabricantes) {
		this.fabricantes = fabricantes;
	}

	public List<FabricanteVO> getFabricantes() throws BancoDeDadosException{
		if (fabricantes == null) {
			fabricantes = getProdutoBO().getFabricanteByNome("");		
		}
		return fabricantes;
	}

	public ProdutoBO getProdutoBO() {
		if (produtoBO == null) {
			produtoBO = new ProdutoBO();
		}
		return produtoBO;
	}

	// método executado quando clica no botão sair
	private void sair() {
		Object[] options = { "Sim", "Não" };
		int res = JOptionPane.showOptionDialog(null, "Tem certeza que deseja sair?", "Saída", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (res == JOptionPane.YES_OPTION) {
			FabricaConexao.close();
			System.exit(0);
		}
	}
}

class MyFilter extends javax.swing.filechooser.FileFilter {
	public boolean accept(File file) {
		String filename = file.getName();
		return filename.endsWith(ArquivoSER.EXTENSION_FILE);
	}

	public String getDescription() {
		return "*" + ArquivoSER.EXTENSION_FILE;
	}
}