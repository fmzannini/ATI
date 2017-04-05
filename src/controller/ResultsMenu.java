package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import model.image.Image;

public class ResultsMenu extends Menu {

	@FXML
	private MenuItem secondaryToMain;

	public ResultsMenu() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/resultsMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void initialize(InterfaceViewController controller) {
		secondaryToMain.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(controller.getSecondaryImage() == null) {
					return;
				}
				controller.setMainImage(controller.getSecondaryImage());
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}
		});
	}

}
