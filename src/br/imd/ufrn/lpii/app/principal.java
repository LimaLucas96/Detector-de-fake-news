package br.imd.ufrn.lpii.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Principal extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		Pane root = FXMLLoader.load(getClass().getResource("../view/TelaFakeNews.fxml"));
		
		Scene scene = new Scene(root,1142,682); //instancia a tela para abrir no tamanho 1142x682
		primaryStage.setTitle("FakeNews");	//titulo da tela
		primaryStage.setScene(scene); 
		
		primaryStage.show(); //inicia a tela fake news
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
