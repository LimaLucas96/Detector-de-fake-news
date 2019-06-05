package br.imd.ufrn.lpii.visao;

import java.awt.Desktop.Action;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import br.imd.ufrn.lpii.modelo.Arquivo;
import br.imd.ufrn.lpii.modelo.BancoDeDados;
import br.imd.ufrn.lpii.modelo.Levensthein;
import br.imd.ufrn.lpii.modelo.Processar;
import br.imd.ufrn.lpii.modelo.Site;
import br.imd.ufrn.lpii.modelo.abstratics.Externos;
import br.imd.ufrn.lpii.modelo.abstratics.Similaridade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Pagina1{
	 	
//		Buttons{
		@FXML
		private Button botaoProximo1;
		@FXML
	    private Button botaoFinalizar;
//		}
//		Layers{
		@FXML
	    private AnchorPane Arco;
		@FXML
	    private AnchorPane layer2;
	    @FXML
	    private AnchorPane layer1;
//		}
//		CheckBox{
	    @FXML
	    private CheckBox checkBoxTrigram;
	    @FXML
	    private CheckBox checkBoxLevens;
	    @FXML
	    private CheckBox checkBoxCosine;
	    @FXML
	    private CheckBox checkBoxJaro;
//		} 
		@FXML
	    private ResourceBundle resources;
	    @FXML
	    private Label mensagemError; 
	    @FXML
	    private URL location;
	    @FXML
	    private TextField buscaBar;
	    
	    @FXML
	    private BarChart<?, ?> graficoLinha;

	    @FXML
	    private CategoryAxis x;

	    @FXML
	    private NumberAxis y;
	    
	    private static Externos arquivo;
		private static BancoDeDados bd;
		private static Externos site;
		private static Similaridade similaridade;
		private static Processar processar;
	    
	    @FXML
	    void buscarData(ActionEvent event) {
	    	
	    	mensagemError.setText("");
	    	
	    	System.out.println("clicou");
	    	
	    	String url = procurarDados();
	    	buscaBar.setText(url);
	    	
	    	try {
	    		ArrayList<String> dadosArquivo = arquivo.Abrir(url);
	    		processar.processarArquivo(dadosArquivo);
				
			} catch (Exception e) {
				
				mensagemError.setText(e.getMessage());
				
			}
	    	
	    	if(!buscaBar.getText().isEmpty()) {
	    		botaoProximo1.setDisable(false);
	    	}
	    	
	    }
	    @FXML
	    void botaoCancelar(ActionEvent event) {
	    	System.out.println("Bye...");
	    	Stage stage = (Stage)Arco.getScene().getWindow();
	    	stage.close();
	    }

	    @FXML
	    void botaoProximo1(ActionEvent event) {
	    	
	    	Stage stage = (Stage)Arco.getScene().getWindow();
	    	
	    	stage.setHeight(470);
	    	stage.setWidth(700);
	    	layer1.setVisible(false);
	    	layer2.setVisible(true);
	    }
	    @FXML
	    void acaoBotaoAnterior(ActionEvent event) {
	    	Stage stage = (Stage)Arco.getScene().getWindow();
	    	
	    	
	    	stage.setHeight(225);
	    	stage.setWidth(600);
	    	layer1.setVisible(true);
	    	layer2.setVisible(false);
	    }
	    
	    @FXML
	    void acaoCheckBox(ActionEvent event) {
	    	if(checkBoxCosine.isSelected() ||
	    			checkBoxLevens.isSelected() ||
	    			checkBoxTrigram.isSelected() ||
	    			checkBoxJaro.isSelected()) {
	    		botaoFinalizar.setDisable(false);
	    	}else {
	    		botaoFinalizar.setDisable(true);
	    	}
	    }
	    
	    @FXML
	    void gerarGrafico(ActionEvent event) {
	    
	    	graficoLinha.getData().clear();
	    	
	    	if(checkBoxCosine.isSelected()) {
	    		XYChart.Series serie1 = new XYChart.Series();
	    		serie1.setName("Cosine");
	    		serie1.getData().add(new XYChart.Data("",70));
	    		graficoLinha.getData().add(serie1);
	    	}
	    	if(checkBoxLevens.isSelected()) {
	    		XYChart.Series serie2 = new XYChart.Series();
	    		serie2.setName("Levens");
	    		serie2.getData().add(new XYChart.Data("",40));
	    		graficoLinha.getData().add(serie2);
	    		
	    	}
	    	if(checkBoxTrigram.isSelected()) {
	    		XYChart.Series serie3 = new XYChart.Series();
	    		serie3.setName("Trigram");
	    		serie3.getData().add(new XYChart.Data("",50));
	    		graficoLinha.getData().add(serie3);
	    	}
	    	if(checkBoxJaro.isSelected()) {
	    		XYChart.Series serie4 = new XYChart.Series();
	    		serie4.setName("Jaro");
	    		serie4.getData().add(new XYChart.Data("",85));
	    		graficoLinha.getData().add(serie4);
	    	}
	    		
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
	    	Stage stage = (Stage)Arco.getScene().getWindow();
	    	
	    	
	    	return fileChoose.showOpenDialog(stage).toString();
	    }

}
