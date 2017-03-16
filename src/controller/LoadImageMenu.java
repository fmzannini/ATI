package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class LoadImageMenu extends Menu {
	
	@FXML
	private MenuItem rawImage;
	
	@FXML
	private MenuItem bmpImage;
	
	public LoadImageMenu() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/loadImageMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	public void initialize() {
		rawImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				//llamada a la carga de raw image
			}
		});
		bmpImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				//llamada a la carga de bmp image
			}
		});
	}
	
	public MenuItem getRawImageMenuItem() {
		return this.rawImage;
	}
	
	public MenuItem getBMPImageMenuItem() {
		return this.bmpImage;
	}
}
