package controller;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ATIApplication extends Application {
	public static Stage primaryStage;
	private Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = FXMLLoader.load(ATIApplication.class.getResource("/view/index.fxml"));
			Scene scene = new Scene(root, screen.width / 2, 500);

//			File f = new File("resources/application.css");
//			scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

			primaryStage.setTitle("ATI");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			ATIApplication.primaryStage = primaryStage;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}