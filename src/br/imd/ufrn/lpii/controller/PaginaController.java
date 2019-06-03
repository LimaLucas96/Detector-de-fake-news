package br.imd.ufrn.lpii.controller;

import java.io.File;
import java.util.ArrayList;

import br.imd.ufrn.lpii.app.Similaridade;
import br.imd.ufrn.lpii.modelo.Arquivo;
import br.imd.ufrn.lpii.modelo.BancoDeDados;
import br.imd.ufrn.lpii.modelo.Externos;
import br.imd.ufrn.lpii.modelo.Levensthein;
import br.imd.ufrn.lpii.modelo.Processar;
import br.imd.ufrn.lpii.modelo.Site;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PaginaController {
	
	private static Externos arquivo;
	private static BancoDeDados bd;
	private static Externos site;
	private static Similaridade similaridade;
	private static Processar processar;
    @FXML
    private AnchorPane telaPrincial;

    @FXML
    private TextField barraUrlSite;

    @FXML
    private AnchorPane telaInicial;

    @FXML
    private TextField barraBuscaData;

    @FXML
    private AnchorPane telaCarregando;

    @FXML
    private AnchorPane popUpError;

    @FXML
    private AnchorPane popUpResultado;
    
    @FXML
    private AnchorPane telaGeral;
    
    @FXML
    private AnchorPane botaoAvancaTela;
    
    @FXML
    private Label mensagemError;

    @FXML
    void avancaPagina(MouseEvent event) {
    	telaInicial.setVisible(false);
    	telaPrincial.setVisible(true);
    }

    @FXML
    void buscaData(MouseEvent event) {
    	mensagemError.setText("");
    	
    	System.out.println("clicou");
    	
    	String url = procurarDados();
    	barraBuscaData.setText(url);
    	
    	try {
    		ArrayList<String> dadosArquivo = arquivo.Abrir(url);
    		processar.processarArquivo(dadosArquivo);
			
    		if(!barraBuscaData.getText().isEmpty()) {
        		botaoAvancaTela.setDisable(false);
        	}
		} catch (Exception e) {
			popUpError.setVisible(true);
			mensagemError.setText(e.getMessage());
			
		}
    	
    	
    }

    @FXML
    void verificaSite(MouseEvent event) {

    }
    
    @FXML
    void initialize() {
    	arquivo = new Arquivo();
		bd = new BancoDeDados();
		processar = new Processar(bd);
		site = new Site();
		similaridade = new Levensthein(bd);
    }
    
    private String procurarDados() {
    	
    	FileChooser fileChoose = new FileChooser();
    	
    	FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
    	
    	File diretorioIicial = new File("/Users/macosx/eclipse-workspace/ProjetoFinal/");
    	
    	fileChoose.getExtensionFilters().add(filter);
    	
    	fileChoose.setTitle("Data Set");
    	fileChoose.setInitialDirectory(diretorioIicial);
    	Stage stage = (Stage)telaGeral.getScene().getWindow();
    	
    	
    	return fileChoose.showOpenDialog(stage).toString();
    }

}
