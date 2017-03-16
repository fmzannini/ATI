package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class TransformMenu extends Menu {

	@FXML
	private MenuItem rgbToHSV;

	public TransformMenu() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/transformMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void initialize() {
		rgbToHSV.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				// llamada a la función transformar de RGB a HSV
			}
		});
	}

}
