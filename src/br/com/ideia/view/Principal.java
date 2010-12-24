package br.com.ideia.view;

import javax.persistence.EntityManager;
import javax.swing.UIManager;

import br.com.ideia.util.FabricaConexao;

public class Principal {

	public static void main(String[] args) {		
		 javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	TelaMenu tela = new TelaMenu();
	            	tela.setSize(800, 600);
	            	tela.setLocationRelativeTo(null);
	            	tela.setVisible(true);	         	
	            	translateButton();
	            }
	        }); 
		
		EntityManager manager = FabricaConexao.getEntityManager();
		manager.clear();

	}
	
	private static void translateButton(){
		UIManager.put ("FileChooser.lookInLabelText", "Examinar:");
	    UIManager.put ("FileChooser.lookInLabelMnemonic", "e");   
	    UIManager.put ("FileChooser.fileNameLabelText", "Nome:");   
	    UIManager.put ("FileChooser.fileNameLabelMnemonic", "N");   
	    UIManager.put ("FileChooser.filesOfTypeLabelText", "Tipo:");   
	    UIManager.put ("FileChooser.filesOfTypeLabelMnemonic", "t");   
	    UIManager.put ("FileChooser.upFolderToolTipText", "Um Nível Acima");   
	    UIManager.put ("FileChooser.upFolderAccessibleName", "Para Cima");   
	    UIManager.put ("FileChooser.homeFolderToolTipText", "Inicio");   
	    UIManager.put ("FileChooser.homeFolderAccessibleName", "Inicio");   
	    UIManager.put ("FileChooser.newFolderToolTipText", "Criar uma Nova Pasta");   
	    UIManager.put ("FileChooser.newFolderAccessibleName", "Nova Pasta");   
	    UIManager.put ("FileChooser.listViewButtonToolTipText", "Lista");   
	    UIManager.put ("FileChooser.listViewButtonAccessibleName", "Lista");   
	    UIManager.put ("FileChooser.detailsViewButtonToolTipText", "Detalhes");   
	    UIManager.put ("FileChooser.detailsViewButtonAccessibleName", "Detalhes");         
	    UIManager.put ("FileChooser.cancelButtonText", "Cancelar");   
	    UIManager.put ("FileChooser.cancelButtonMnemonic", " C");   
	    UIManager.put ("FileChooser.cancelButtonToolTipText","Cancelar");  
	    UIManager.put ("FileChooser.openButtonText", "Abrir");
	    UIManager.put ("FileChooser.openButtonToolTipText","Abrir o arquivo selecionado");
	    UIManager.put ("FileChooser.openButtonMnemonic", "A");   
	    UIManager.put ("FileChooser.saveButtonText","Salvar");  
	    UIManager.put ("FileChooser.saveButtonToolTipText","Salvar Arquivo");		
	    UIManager.put ("FileChooser.acceptAllFileFilterText","Todos os arquivos (*.*)");
	    UIManager.put ("FileChooser.directoryOpenButtonText","Abrir");  
	    UIManager.put ("FileChooser.directoryOpenButtonToolTipText","Abrir diretório selecionado"); 
	    UIManager.put ("FileChooser.openDialogTitleText","Abrir"); 
	    UIManager.put ("FileChooser.saveInLabelText","Salvar em"); 
	    UIManager.put ("FileChooser.saveDialogTitleText","Salvar");
	    // ConfirmDialog
	    UIManager.put ("OptionPane.yesButtonText","Sim"); 
		UIManager.put ("OptionPane.noButtonText","Não"); 
		UIManager.put ("OptionPane.cancelButtonText","Cancelar");
		UIManager.put ("OptionPane.titleText","Selecione uma opção");
	}
}
