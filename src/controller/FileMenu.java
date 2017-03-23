package controller;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

public class FileMenu extends Menu {
	
//	@FXML
//	private Menu loadImage;
	
	@FXML
	private MenuItem loadImage;
	
	@FXML
	private MenuItem saveImage;
	
	public FileMenu() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/fileMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void initialize(InterfaceViewController controller) {
		loadImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				File file = fileChooser.showOpenDialog(ATIApplication.primaryStage);
				controller.loadImage(file);
			}
		});
		saveImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				File file = new File("/Users/FMZ/Documents/workspace/ATI/resources/a.png");
				controller.loadImage(file);
			}
		});
	}
}
