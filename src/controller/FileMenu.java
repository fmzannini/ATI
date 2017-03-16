package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class FileMenu extends Menu {
	
	@FXML
	private Menu loadImage;
	
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
		loadImage = new LoadImageMenu();
		((LoadImageMenu)loadImage).initialize();
		saveImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				//llamada a la función de guardado
			}
		});
	}
}
