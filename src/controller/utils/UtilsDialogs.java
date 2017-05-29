package controller.utils;


import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;

public class UtilsDialogs {

	public static void showAlert(String title, String header) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.showAndWait();
	}

	
	public static String[] getInputs(String title, String header, String pattern) {
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
