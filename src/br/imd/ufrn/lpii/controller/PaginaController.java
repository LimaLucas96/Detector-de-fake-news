package br.imd.ufrn.lpii.controller;

import java.io.File;
import java.util.ArrayList;

import br.imd.ufrn.lpii.modelo.Arquivo;
import br.imd.ufrn.lpii.modelo.BancoDeDados;
import br.imd.ufrn.lpii.modelo.Cosine;
import br.imd.ufrn.lpii.modelo.Levensthein;
import br.imd.ufrn.lpii.modelo.Processar;
import br.imd.ufrn.lpii.modelo.Site;
import br.imd.ufrn.lpii.modelo.abstratics.Externos;
import br.imd.ufrn.lpii.modelo.abstratics.Similaridade;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PaginaController {
	
	private static Externos arquivo;
	private static BancoDeDados bd;
	private static Externos site;
	private static Processar processar;
	
	ArrayList<String> textoSite;
	ArrayList<Integer> dadosGraficoCos;
	ArrayList<Integer> dadosGraficoLev;
	private int maxPorcentCos;
	private int maxPorcentLev;
	
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
    private Label mensagemPopUpResultado;
    
    @FXML
    private CheckBox checkBoxCosine;

    @FXML
    private CheckBox checkBoxLevens;

    @FXML
    private CheckBox checkBoxTrigram;

    @FXML
    private CheckBox checkBoxJaro;
    
    @FXML
    private Label porcentCos;

    @FXML
    private Label porcentLev;
    
    @FXML
    private Slider sliderSimiliaridade;
    
    @FXML
    private LineChart<?, ?> graficoLinha;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;
    
    @FXML
    void initialize() {
    	dadosGraficoCos = new ArrayList<Integer>();
    	dadosGraficoLev = new ArrayList<Integer>();
    	telaInicial.setVisible(true);
    	telaPrincial.setVisible(false);
    	arquivo = new Arquivo();
		bd = new BancoDeDados();
		processar = new Processar(bd);
		site = new Site();
    }
    
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
    	maxPorcentCos = 0;
    	maxPorcentLev = 0;
    	if(!checkBoxCosine.isSelected() && !checkBoxLevens.isSelected()) {
    		popUpError.setVisible(true);
			mensagemError.setText( "Nenhum algoritmo selecionado");
			return;
		}
    	
    	telaCarregando.setVisible(true);
    	 
    	
    	Service<Boolean> process = new Service<Boolean>(){
			@Override
			protected Task<Boolean> createTask() {
				return new Task<Boolean>() {
					@Override
					protected Boolean call() throws Exception {
						boolean result = false;
						
						textoSite = site.Abrir(barraUrlSite.getText());
						
						if(checkBoxLevens.isSelected()) {
							boolean temp = checarLevens();
							XYChart.Series serie = new XYChart.Series();
							
//							for(int i = 0; i < dadosGrafico.size();i++) {
//								serie.getData().add(new XYChart.Data(""+i,dadosGrafico.get(i)));
//							}
//							
//				    		graficoLinha.getData().add(serie);
				    		
							if(!result) { result = temp; }
							
						}if(checkBoxCosine.isSelected()) {
							boolean temp = checarCosine();
//							XYChart.Series serie = new XYChart.Series();
//							
//							for(int i = 0; i < dadosGrafico.size();i++) {
//								serie.getData().add(new XYChart.Data(""+i,70));
//							}
//							
//				    		graficoLinha.getData().add(serie);
				    		
							if(!result) { result = temp; }
							
						}
						return result;
					}
				};
			}
    		
    	};
    	
    	process.setOnSucceeded(e -> {
    		telaCarregando.setVisible(false);
    		boolean processResult = process.getValue();
    		
    		if(processResult) {
    			mensagemPopUpResultado.setText("É uma Fake News");
    			popUpResultado.setVisible(true);
    		}else {
    			mensagemPopUpResultado.setText("Nao é uma Fake News");
    			popUpResultado.setVisible(true);
    		}
    		
    		porcentCos.setText(maxPorcentCos + "%");
    		porcentLev.setText(maxPorcentLev + "%");
    		teste();
        	
    	});
    	
    	process.setOnFailed(e -> {
    		telaCarregando.setVisible(false);
    		popUpError.setVisible(true);
			mensagemError.setText( process.getException().getMessage() );
			
    	});
    	process.start();
    	
    }
    
    private void teste() {
    	graficoLinha.getData().clear();
    	XYChart.Series serieCos = new XYChart.Series();
    	XYChart.Series serieLev = new XYChart.Series();
    	
		System.out.println(dadosGraficoCos.size());
		System.out.println(textoSite.size());
		for(int i = 0; i < dadosGraficoCos.size();i++) {
			if(checkBoxCosine.isSelected()) {
				serieCos.getData().add(new XYChart.Data(""+(i+1),dadosGraficoCos.get(i)));
			
			}if(checkBoxLevens.isSelected()) {
			
				serieLev.getData().add(new XYChart.Data(""+(i+1),dadosGraficoLev.get(i)));
			}
			
		}
		
		graficoLinha.getData().addAll(serieCos,serieLev);
    }
    @FXML
    void closePopUp(MouseEvent event) {
    	popUpError.setVisible(false);
    	popUpResultado.setVisible(false);
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

    private boolean checarLevens() throws Exception {
    	Similaridade similaridade = new Levensthein(bd);
    	
    	dadosGraficoLev.clear();
    	
    	boolean validador = false;
    	for(int i = 0; i < textoSite.size(); i++) {
			
    		String hash = processar.ProcessarConteudo(textoSite.get(i));
    			
    		if(bd.buscaBancoDeDados(processar.criarHash(hash))) {
    			validador = true;
    			maxPorcentLev = 100;	
    			break;
    		}else {
    			int temp = (int) similaridade.verificarSimilaridade(hash);
    			
    			dadosGraficoLev.add(temp);
    			
    			if(temp > maxPorcentLev) {
    				maxPorcentLev = temp;
    			}
    			if( temp >= sliderSimiliaridade.getValue()) {
    				validador = true;
    				//break;
    			}
    		}
    	}
    	return validador;
    }
    
    private boolean checarCosine() throws Exception {
    	Similaridade similaridade = new Cosine(bd);
    	
    	dadosGraficoCos.clear();
    	
    	boolean validador = false;
    	for(int i = 0; i < textoSite.size(); i++) {
    			
    		String hash = processar.ProcessarConteudo(textoSite.get(i));
    		
    		if(bd.buscaBancoDeDados(processar.criarHash(hash))) {
    			validador = true;
    			maxPorcentCos = 100;
    			break;
    		}else {
    			int temp = (int) similaridade.verificarSimilaridade(hash);
    			dadosGraficoCos.add(temp);
    			if(temp > maxPorcentCos) {
    				maxPorcentCos = temp;
    			}
    			if( temp >= sliderSimiliaridade.getValue()) {
    				validador = true;
    				//break;
    			}
    		}
    	}
    	
    	return validador;
    }
}
