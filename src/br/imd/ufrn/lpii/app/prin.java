package br.imd.ufrn.lpii.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class prin extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		Pane root = FXMLLoader.load(getClass().getResource("../visao/fakeNewsTemp.fxml"));
		
		Scene scene = new Scene(root,1142,682);
		primaryStage.setTitle("FakeNews");
		primaryStage.setScene(scene);
		//primaryStage.setResizable(false);
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
