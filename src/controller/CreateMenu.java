package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class CreateMenu extends Menu {
	
	@FXML
	private MenuItem whiteSquare;
	
	@FXML
	private MenuItem whiteCircle;
	
	@FXML
	private MenuItem grayGradient;
	
	@FXML
	private MenuItem colorGradient;

	public CreateMenu() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/createMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	public void initialize() {
		whiteSquare.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				//llamada a la función de cuadrado blanco
			}
		});
		whiteCircle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				//llamada a la función de círculo blanco
			}
		});
		grayGradient.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				//llamada a la función del degradé de grises
			}
		});
		colorGradient.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				//llamada a la función del degradé de colores
			}
		});
		
	}
}
