package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InterfaceViewController implements Initializable {

	@FXML
	private CustomMenuBar customMenuBar;

	@FXML
	private ImageView mainImage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		customMenuBar.initialize(this);
		// File file = new
		// File("/Users/FMZ/Documents/workspace/ATI/resources/a.png");

	}

	public void loadImage(File file) {
		try {
			final BufferedImage image = ImageIO.read(file);
			mainImage.setImage(SwingFXUtils.toFXImage(image, null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
