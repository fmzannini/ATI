package controller;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import model.image.Image;
import model.image.ImageColorRGB;
import model.image.ImageGray;
import model.thresholding.GlobalThresholding;
import model.thresholding.OtsuThresholding;

public class ThresholdingMenu extends Menu {

	@FXML
	private MenuItem globalThresholding;
	@FXML
	private MenuItem otsuThresholding;

	public ThresholdingMenu() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/thresholdingMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void initialize(InterfaceViewController controller) {
		globalThresholding.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();
				if (img == null)
					return;
				Image copy = img.copy();

				String[] inputs = getInputs("Delta", "Ingrese un valor de delta" + "\n Ej:35", ",");
				if (inputs == null || inputs.length != 1)
					return;
				int delta = Integer.parseInt(inputs[0]);

				switch (copy.getType()) {
				case IMAGE_GRAY:
					GlobalThresholding gt = new GlobalThresholding((ImageGray) copy);
					copy = gt.calculateGlobalThreshold(delta);
					break;
				case IMAGE_RGB:
					GlobalThresholding gtColor = new GlobalThresholding((ImageColorRGB) copy);
					copy = gtColor.calculateGlobalThresholdColor(delta);
				}
				controller.setSecondaryImage(copy);
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}
		});
		otsuThresholding.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();
				if (img == null)
					return;
				Image copy = img.copy();

				switch (copy.getType()) {
				case IMAGE_GRAY:
					OtsuThresholding gt = new OtsuThresholding((ImageGray) copy);
					copy = gt.calculateOtsuThreshold();
					break;
				case IMAGE_RGB:
					OtsuThresholding gtColor = new OtsuThresholding((ImageColorRGB) copy);
					copy = gtColor.calculateOtsuThresholdColor();
					break;
				}
				controller.setSecondaryImage(copy);
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}
		});

	}
	
	private String[] getInputs(String title, String header, String pattern) {
		Dialog<String> dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		Optional<String> result = dialog.showAndWait();
		if (!result.isPresent())
			return null;
		String input = result.get();
		String[] inputs = input.split(pattern);
		return inputs;
	}
}
