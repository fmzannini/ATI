package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuBar;

public class CustomMenuBar extends MenuBar {

	@FXML
	private FileMenu fileMenu;

	@FXML
	private CreateMenu createMenu;
	
	@FXML
	private TransformMenu transformMenu;

	@FXML
	private InformationMenu informationMenu;
	
	@FXML
	private HistogramMenu histogramMenu;
	
	@FXML
	private ResultsMenu resultsMenu;

	@FXML
	private NoiseMenu noiseMenu;

	@FXML
	private MaskMenu maskMenu;
	
	public CustomMenuBar() {
		super();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/customMenuBar.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void initialize(InterfaceViewController controller) {
		fileMenu.initialize(controller);
		createMenu.initialize();
		transformMenu.initialize(controller);
		informationMenu.initialize(controller);
		histogramMenu.initialize(controller);
		resultsMenu.initialize(controller);
		noiseMenu.initialize(controller);
		maskMenu.initialize(controller);
	}

}
