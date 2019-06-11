package br.imd.ufrn.lpii.controller;

import java.io.File;
import java.util.ArrayList;

import br.imd.ufrn.lpii.model.Arquivo;
import br.imd.ufrn.lpii.model.BancoDeDados;
import br.imd.ufrn.lpii.model.Cosine;
import br.imd.ufrn.lpii.model.JaroWinkler;
import br.imd.ufrn.lpii.model.Levensthein;
import br.imd.ufrn.lpii.model.Processar;
import br.imd.ufrn.lpii.model.Site;
import br.imd.ufrn.lpii.model.Trigram;
import br.imd.ufrn.lpii.model.abstratics.Externos;
import br.imd.ufrn.lpii.model.abstratics.Similaridade;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
/**
 * 
 * @author Lucas Lima
 *
 */
public class PaginaController {
//	Archors{
	@FXML
    private AnchorPane telaPrincial;
	@FXML
    private AnchorPane telaInicial;
  	@FXML
    private AnchorPane telaCarregando;
    @FXML
    private TextField barraUrlSite;
    @FXML
    private AnchorPane popUpError;
    @FXML
    private AnchorPane popUpResultado;   
    @FXML
    private AnchorPane telaGeral;  
    @FXML
    private AnchorPane botaoAvancaTela;
//	}
//	Labels{
    @FXML
    private Label mensagemError;
    @FXML
    private Label mensagemPopUpResultado;
    @FXML
    private Label porcentLev;
    @FXML
    private Label porcentJaro;
    @FXML
    private Label porcentTri;
    @FXML
    private Slider sliderSimiliaridade;
    @FXML
    private Label porcentCos;
//	}
//	CheckBoxs{
    @FXML
    private CheckBox checkBoxCosine;
    @FXML
    private CheckBox checkBoxLevens;
    @FXML
    private CheckBox checkBoxTrigram;
    @FXML
    private CheckBox checkBoxJaro;
//	}
//	TextFields{
    @FXML
    private TextField barraBuscaData;
    @FXML
    private TextField barraSlider;
//	}
//	ProgressBar{
    @FXML
    private ProgressBar barraPorcentLev;
    @FXML
    private ProgressBar barraPorcentTri; 
    @FXML
    private ProgressBar barraPorcentCos;    
    @FXML
    private ProgressBar barraPorcentJW;
//	}
//	Grafico{
    @FXML
    private LineChart<?, ?> graficoLinha;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;
//	}
    
	private static Externos arquivo;
	private static BancoDeDados bd;
	private static Externos site;
	private static Processar processar;
	
	ArrayList<String> textoSite;
	ArrayList<Integer> dadosGraficoCos;
	ArrayList<Integer> dadosGraficoLev;
	ArrayList<Integer> dadosGraficoTri;
	ArrayList<Integer> dadosGraficoJaro;
	
	private int maxPorcentCos;
	private int maxPorcentLev;
	private int maxPorcentTri;
	private int maxPorcentJaro;

    @FXML
    void initialize() {
    	dadosGraficoCos = new ArrayList<Integer>();
    	dadosGraficoLev = new ArrayList<Integer>();
    	dadosGraficoJaro = new ArrayList<Integer>();
    	dadosGraficoTri = new ArrayList<Integer>();
    	
    	telaInicial.setVisible(true);
    	telaPrincial.setVisible(false);
    	arquivo = new Arquivo();
		bd = new BancoDeDados();
		processar = new Processar(bd);
		site = new Site();
    }
    
    @FXML
    /*
     * Troca a tela inicial para a tela princial.
     */
    void avancaPagina(MouseEvent event) {
    	telaInicial.setVisible(false);
    	telaPrincial.setVisible(true);
    }

    @FXML
    /*
     * Função para buscar um arquivo no sistema operacional.
     */
    void buscaData(MouseEvent event) {
    	
    	System.out.println("clicou");
    	
    	String url = procurarDados(); //chama funcao ondem tem o FileChoose
    	barraBuscaData.setText(url);
    	
    	try {
    		ArrayList<String> dadosArquivo = arquivo.abrir(url); // Verifica se o arquivo é compativel
    		processar.processarArquivo(dadosArquivo); // Processa os dados dos arquivos.
			
    		// se a barra de busca estiver com algum endereco, o botao de avancar tela é validado.
    		if(!barraBuscaData.getText().isEmpty()) {
        		botaoAvancaTela.setDisable(false);
        	}
		} catch (Exception e) { // se ocorrer algum erro, o sistema abre um pop-up de erro.
			popUpError.setVisible(true); 
			mensagemError.setText(e.getMessage()); // memsagem do erro tratado.
		}
    }

    @FXML
    /*
     * Evento chamado quando o usuario clica para verificar o link da fake news
     */
    void verificaSite(MouseEvent event) {
    	
    	closePopUp(event); //fecha qualquer pop-pu aberto no momento.
    	
    	// zera todos os contadores
    	maxPorcentCos = 0;
    	maxPorcentLev = 0;
    	maxPorcentTri = 0;
    	maxPorcentJaro = 0;
    	
    	// verifica se algum checkbox foi marcado
    	if(!checkBoxCosine.isSelected() && 
    			!checkBoxLevens.isSelected() &&
    			!checkBoxTrigram.isSelected() &&
    			!checkBoxJaro.isSelected()) {
    		
    		popUpError.setVisible(true);
			mensagemError.setText( "Nenhum algoritmo selecionado"); // memsagem do pop-up
			
			return; //para o evento
		}
    	
    	telaCarregando.setVisible(true); // abre a tela com a mensagem "carregando".
    	 
    	//inicia um processo em segundo plano, e assim poder finalizar o evento com mais velocidade.
    	Service<Boolean> process = new Service<Boolean>(){
			@Override
			protected Task<Boolean> createTask() {
				return new Task<Boolean>() {
					@Override
					protected Boolean call() throws Exception {
						boolean result = false;
						
						textoSite = site.abrir(barraUrlSite.getText()); //executa o web scraping
						
						if(checkBoxLevens.isSelected()) {
							boolean temp = checarLevens();
							
							if(!result) { result = temp; }
							
						}if(checkBoxCosine.isSelected()) {
							boolean temp = checarCosine();
				    		
							if(!result) { result = temp; }
							
						}
						if(checkBoxTrigram.isSelected()) {
							boolean temp = checarTrigram();
				    		
							if(!result) { result = temp; }
							
						}
						if(checkBoxJaro.isSelected()) {
							boolean temp = checarJaro();
				    		
							if(!result) { result = temp; }
							
						}
						return result;
					}
				};
			}
    		
    	};
    	
    	// se tudo ocorreu bem no segundo plano
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
    		porcentTri.setText(maxPorcentTri + "%");
    		
    		barraPorcentCos.setProgress(maxPorcentCos*0.01);
    		barraPorcentLev.setProgress(maxPorcentLev*0.01);
    		barraPorcentTri.setProgress(maxPorcentTri*0.01);
    		barraPorcentJW.setProgress(maxPorcentJaro*0.01);
    		geraGrafico();
        	
    	});
    	// se tiver o corrido algum erro
    	process.setOnFailed(e -> {
    		telaCarregando.setVisible(false);
    		popUpError.setVisible(true);
			mensagemError.setText( process.getException().getMessage() );
			
    	});
    	process.start(); // inicia o processo em segundo plano
    	
    }
    
   
    @FXML
    void closePopUp(MouseEvent event) {
    	popUpError.setVisible(false);
    	popUpResultado.setVisible(false);
    }
    
    @FXML
    void mudarValorSlider(ActionEvent event) {
    	
    	int valor =Integer.parseInt(barraSlider.getText());
    	
    	if(valor >= 85 && valor <=100) {
    		System.out.println("passou aqui");
    		sliderSimiliaridade.setValue(valor);
    	}else {
    		barraSlider.setText(""+sliderSimiliaridade.getValue());
    	}
    }
    
    @FXML
    void mudaValorBarraSlider(MouseEvent event) {
    	
    	int valorTemp = (int)sliderSimiliaridade.getValue();
    	barraSlider.setText(""+valorTemp);
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
    	System.out.println("fim Levens");
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
    	System.out.println("fim Cosine");
    	return validador;
    }
    
    private boolean checarTrigram() throws Exception {
    	Similaridade similaridade = new Trigram(bd);
    	
    	System.out.println("iniciando Trigram");
    	
    	dadosGraficoTri.clear();
    	
    	boolean validador = false;
    	for(int i = 0; i < textoSite.size(); i++) {
    			
    		String hash = processar.ProcessarConteudo(textoSite.get(i));
    		
    		if(bd.buscaBancoDeDados(processar.criarHash(hash))) {
    			validador = true;
    			maxPorcentTri = 100;
    			dadosGraficoTri.add(100);
    			//break;
    		}else {
    			
    			int temp = (int) similaridade.verificarSimilaridade(hash);
    			
    			dadosGraficoTri.add(temp);
    			
    			if(temp > maxPorcentTri) {
    				maxPorcentTri = temp;
    			}
    			if( temp >= sliderSimiliaridade.getValue()) {
    				validador = true;
    				//break;
    			}
    		}
    	}
    	System.out.println("fim Trigram");
    	return validador;
    }
     
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
    			dadosGraficoJaro.add(100);
    			//break;
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
    	
    	System.out.println("fim Jaro-Winkler");
    	return validador;
    }
    
    private void geraGrafico() {
    	graficoLinha.getData().clear();
    	XYChart.Series serieCos = new XYChart.Series();
    	XYChart.Series serieLev = new XYChart.Series();
    	XYChart.Series serieTri = new XYChart.Series();
    	XYChart.Series serieJaro = new XYChart.Series();
		
		for(int i = 0; i < textoSite.size();i++) {
			if(checkBoxCosine.isSelected()) {
				serieCos.getData().add(new XYChart.Data(""+(i+1),dadosGraficoCos.get(i)));
			
			}if(checkBoxLevens.isSelected()) {
				serieLev.getData().add(new XYChart.Data(""+(i+1),dadosGraficoLev.get(i)));
			}
			if(checkBoxJaro.isSelected()) {
				serieJaro.getData().add(new XYChart.Data(""+(i+1),dadosGraficoJaro.get(i)));
			}
			if(checkBoxTrigram.isSelected()) {
				serieTri.getData().add(new XYChart.Data(""+(i+1),dadosGraficoTri.get(i)));
			}
		}
		
		graficoLinha.getData().addAll(serieCos, serieLev, serieTri, serieJaro);
    }
}
