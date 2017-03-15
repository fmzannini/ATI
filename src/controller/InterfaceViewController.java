package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

public class InterfaceViewController implements Initializable {

	@FXML
	private CustomMenuBar customMenuBar;

	@FXML
	private ImageView mainImage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		customMenuBar.initialize(this);
	}

}
