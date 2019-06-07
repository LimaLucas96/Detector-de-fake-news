package br.imd.ufrn.lpii.controller;

import java.io.File;
import java.util.ArrayList;

import br.imd.ufrn.lpii.modelo.Arquivo;
import br.imd.ufrn.lpii.modelo.BancoDeDados;
import br.imd.ufrn.lpii.modelo.Cosine;
import br.imd.ufrn.lpii.modelo.JaroWinkler;
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
	ArrayList<Integer> dadosGraficoJaro;
	
	private int maxPorcentCos;
	private int maxPorcentLev;
	private int maxPorcentJaro;
	
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
    private Label porcentJaro;
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
    	dadosGraficoJaro = new ArrayList<Integer>();
    	
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
    		ArrayList<String> dadosArquivo = arquivo.abrir(url);
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
    	
    	closePopUp(event);
    	
    	maxPorcentCos = 0;
    	maxPorcentLev = 0;
    	maxPorcentJaro = 0;
    	
    	if(!checkBoxCosine.isSelected() && 
    			!checkBoxLevens.isSelected() &&
    			!checkBoxTrigram.isSelected() &&
    			!checkBoxJaro.isSelected()) {
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
						
						textoSite = site.abrir(barraUrlSite.getText());
						
						if(checkBoxLevens.isSelected()) {
							boolean temp = checarLevens();
							
							if(!result) { result = temp; }
							
						}if(checkBoxCosine.isSelected()) {
							boolean temp = checarCosine();
				    		
							if(!result) { result = temp; }
							
						}
//						if(checkBoxTrigram.isSelected()) {
//							boolean temp = checarCosine();
//				    		
//							if(!result) { result = temp; }
//							
//						}
						if(checkBoxJaro.isSelected()) {
							boolean temp = checarJaro();
				    		
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
    		porcentJaro.setText(maxPorcentJaro + "%");
    		geraGrafico();
        	
    	});
    	
    	process.setOnFailed(e -> {
    		telaCarregando.setVisible(false);
    		popUpError.setVisible(true);
			mensagemError.setText( process.getException().getMessage() );
			
    	});
    	process.start();
    	
    }
    
    private void geraGrafico() {
    	graficoLinha.getData().clear();
    	XYChart.Series serieCos = new XYChart.Series();
    	XYChart.Series serieLev = new XYChart.Series();
    	XYChart.Series serieJaro = new XYChart.Series();
    	
		System.out.println(dadosGraficoCos.size());
		System.out.println(textoSite.size());
		
		for(int i = 0; i < textoSite.size();i++) {
			if(checkBoxCosine.isSelected()) {
				serieCos.getData().add(new XYChart.Data(""+(i+1),dadosGraficoCos.get(i)));
			
			}if(checkBoxLevens.isSelected()) {
				serieLev.getData().add(new XYChart.Data(""+(i+1),dadosGraficoLev.get(i)));
			}
			if(checkBoxJaro.isSelected()) {
				serieJaro.getData().add(new XYChart.Data(""+(i+1),dadosGraficoJaro.get(i)));
			}
			
		}
		
		graficoLinha.getData().addAll(serieCos, serieLev, serieJaro);
    }
    @FXML
    void closePopUp(MouseEvent event) {
    	popUpError.setVisible(false);
    	popUpResultado.setVisible(false);
    }
    
    private String procurarDados() {
    	
    	FileChooser fileChoose = new FileChooser();
    	
    	FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
    	
    	File diretorioIicial = new File("./");
    	
    	fileChoose.getExtensionFilters().add(filter);
    	
    	fileChoose.setTitle("Data Set");
    	fileChoose.setInitialDirectory(diretorioIicial);
    	Stage stage = (Stage)telaGeral.getScene().getWindow();
    	
    	
    	return fileChoose.showOpenDialog(stage).toString();
    }

    private boolean checarLevens() throws Exception {
    	Similaridade similaridade = new Levensthein(bd);
    	System.out.println("iniciando Levens");
    	
    	dadosGraficoLev.clear();
    	
    	boolean validador = false;
    	for(int i = 0; i < textoSite.size(); i++) {
			
    		String hash = processar.ProcessarConteudo(textoSite.get(i));
    			
    		if(bd.buscaBancoDeDados(processar.criarHash(hash))) {
    			validador = true;
    			maxPorcentLev = 100;
    			dadosGraficoLev.add(100);
    			//break;
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
    	System.out.println("iniciando Cosine");
    	
    	dadosGraficoCos.clear();
    	
    	boolean validador = false;
    	for(int i = 0; i < textoSite.size(); i++) {
    			
    		String hash = processar.ProcessarConteudo(textoSite.get(i));
    		
    		if(bd.buscaBancoDeDados(processar.criarHash(hash))) {
    			validador = true;
    			maxPorcentCos = 100;
    			dadosGraficoCos.add(100);
    			//break;
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
    //FALTA O TRIGRAM ----- LEMBRE DISSO
    private boolean checarJaro() throws Exception {
    	Similaridade similaridade = new JaroWinkler(bd);
    	System.out.println("iniciando Jaro-Winkler");
    	dadosGraficoJaro.clear();
    	
    	boolean validador = false;
    	for(int i = 0; i < textoSite.size(); i++) {
    			
    		String hash = processar.ProcessarConteudo(textoSite.get(i));
    		
    		if(bd.buscaBancoDeDados(processar.criarHash(hash))) {
    			validador = true;
    			maxPorcentJaro = 100;
    			break;
    		}else {
    			int temp = (int) similaridade.verificarSimilaridade(hash);
    			dadosGraficoJaro.add(temp);
    			if(temp > maxPorcentJaro) {
    				maxPorcentJaro = temp;
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
