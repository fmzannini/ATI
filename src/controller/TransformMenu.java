package controller;

import java.io.File;
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
import model.file.ImageFileManager;
import model.image.Image;
import model.image.ImageColorHSV;
import model.image.ImageColorRGB;
import model.image.ImageGray;
import utils.HistogramEqualization;

public class TransformMenu extends Menu {

	@FXML
	private MenuItem rgbToHSV;

	@FXML
	private MenuItem setPixel;

	@FXML
	private MenuItem negative;

	@FXML
	private MenuItem thresholding;

	@FXML
	private MenuItem scalarMultiplication;

	@FXML
	private MenuItem gammaPower;

	@FXML
	private MenuItem increaseContrast;

	@FXML
	private MenuItem equalize;

	@FXML
	private MenuItem sumImages;

	@FXML
	private MenuItem multiplyImages;

	@FXML
	private MenuItem substractImages;

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

	public void initialize(InterfaceViewController controller) {
		rgbToHSV.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				// llamada a la función transformar de RGB a HSV
				Image img = controller.getImage();
				if (img == null)
					return;
				if (!(img instanceof ImageColorRGB))
					return;
				ImageColorRGB imgRGB = (ImageColorRGB) img;
				try {
					saveBand("red_band", imgRGB.getBandOnlyGray(0));
					saveBand("green_band", imgRGB.getBandOnlyGray(1));
					saveBand("blue_band", imgRGB.getBandOnlyGray(2));
				} catch (IOException e) {
					e.printStackTrace();
				}

				ImageColorHSV imgHSV = imgRGB.passToHSV();
				try {
					saveBand("hue_band", imgHSV.getBandOnlyGray(0));
					saveBand("saturation_band", imgHSV.getBandOnlyGray(1));
					saveBand("value_band", imgHSV.getBandOnlyGray(2));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			private void saveBand(String filename, ImageGray band) throws IOException {
				File file = new File(System.getProperty("user.dir") + "/" + filename + ".pgm");
				ImageFileManager ifm = new ImageFileManager(file);
				ifm.writeImagePGM(band.showImage());
			}
		});

		setPixel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();

				Dialog<String> dialog = new TextInputDialog();
				dialog.setTitle("Set pixel");
				dialog.setHeaderText("Type x and y,for example: 5,10");
				Optional<String> result = dialog.showAndWait();
				if (!result.isPresent())
					return;
				String input = result.get();
				String[] inputs = input.split(",");
				int x = Integer.parseInt(inputs[0]);
				int y = Integer.parseInt(inputs[1]);

				if (x >= img.getWidth() || y >= img.getHeight())
					return;

				switch (img.getType()) {
				case IMAGE_GRAY:
					Dialog<String> dialogGray = new TextInputDialog();
					dialogGray.setTitle("Set pixel");
					dialogGray.setHeaderText("Type value, for example: 250.23");
					Optional<String> resultGray = dialogGray.showAndWait();
					if (!resultGray.isPresent())
						return;
					String inputGray = resultGray.get();
					double value = Double.parseDouble(inputGray);

					ImageGray imgGray = (ImageGray) img;
					imgGray.setPixel(x, y, value);
					break;
				case IMAGE_RGB:
					Dialog<String> dialogRGB = new TextInputDialog();
					dialogRGB.setTitle("Set pixel");
					dialogRGB.setHeaderText("Type value rgb,for example: 150,200,50");
					Optional<String> resultRGB = dialogRGB.showAndWait();
					if (!resultRGB.isPresent())
						return;
					String inputRGB = resultRGB.get();
					String[] inputsRGB = inputRGB.split(",");
					double[] rgb = new double[3];
					rgb[0] = Double.parseDouble(inputsRGB[0]);
					rgb[1] = Double.parseDouble(inputsRGB[1]);
					rgb[2] = Double.parseDouble(inputsRGB[2]);

					ImageColorRGB imgRGB = (ImageColorRGB) img;
					imgRGB.setPixel(x, y, rgb);
					break;
				case IMAGE_HSV:
					Dialog<String> dialogHSV = new TextInputDialog();
					dialogHSV.setTitle("Set pixel");
					dialogHSV.setHeaderText("Type value hsv,for example: 0.5,0.3,0.7");
					Optional<String> resultHSV = dialogHSV.showAndWait();
					if (!resultHSV.isPresent())
						return;
					String inputHSV = resultHSV.get();
					String[] inputsHSV = inputHSV.split(",");
					double[] hsv = new double[3];
					hsv[0] = Double.parseDouble(inputsHSV[0]);
					hsv[1] = Double.parseDouble(inputsHSV[1]);
					hsv[2] = Double.parseDouble(inputsHSV[2]);

					ImageColorHSV imgHSV = (ImageColorHSV) img;
					imgHSV.setPixel(x, y, hsv);
					break;
				}

				controller.refreshImage();
			}
		});

		negative.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();

				switch (img.getType()) {
				case IMAGE_GRAY: {
					ImageGray imgGray = (ImageGray) img.copy();
					controller.setSecondaryImage(imgGray);
					imgGray = imgGray.getNegative();
					break;
				}
				case IMAGE_RGB: {
					ImageColorRGB imgRGB = (ImageColorRGB) img;
					imgRGB = imgRGB.getNegative();
					break;
				}
				}
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}

		});

		thresholding.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();
				controller.setSecondaryImage(img);

				Dialog<String> dialog = new TextInputDialog();
				dialog.setTitle("Elegir valor de Umbralización");
				dialog.setHeaderText("Elegir un valor entre 0 y 255");
				Optional<String> result = dialog.showAndWait();
				if (!result.isPresent())
					return;
				String input = result.get();
				double threshold = Double.parseDouble(input);
				if (threshold > 255.0 || threshold < 0) {
					return;
				}

				switch (img.getType()) {
				case IMAGE_GRAY: {
					ImageGray imgGray = (ImageGray) img.copy();
					controller.setSecondaryImage(imgGray);
					imgGray = imgGray.applyThresholding((int) threshold);
					break;
				}
				case IMAGE_RGB: {
					ImageColorRGB imgRGB = (ImageColorRGB) img.copy();
					controller.setSecondaryImage(imgRGB);
					imgRGB = imgRGB.applyThresholding((int) threshold);
					break;
				}
				}
				controller.refreshSecondaryImage();
			}

		});

		scalarMultiplication.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();
				controller.setSecondaryImage(img);

				Dialog<String> dialog = new TextInputDialog();
				dialog.setTitle("Elegir Escalar");
				dialog.setHeaderText("Elegir un valor entero mayor a 1");
				Optional<String> result = dialog.showAndWait();
				if (!result.isPresent())
					return;
				String input = result.get();
				int scalar = Integer.parseInt(input);
				if (scalar < 1) {
					return;
				}

				switch (img.getType()) {
				case IMAGE_GRAY: {
					ImageGray imgGray = (ImageGray) img.copy();
					controller.setSecondaryImage(imgGray);
					imgGray = imgGray.multiply(scalar);
					break;
				}
				}
				controller.refreshSecondaryImage();
			}

		});

		gammaPower.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();
				controller.setSecondaryImage(img);

				Dialog<String> dialog = new TextInputDialog();
				dialog.setTitle("Elegir un valor para Gamma");
				Optional<String> result = dialog.showAndWait();
				if (!result.isPresent())
					return;
				String input = result.get();
				double gamma = Double.parseDouble(input);

				switch (img.getType()) {
				case IMAGE_GRAY: {
					ImageGray imgGray = (ImageGray) img.copy();
					controller.setSecondaryImage(imgGray);
					imgGray = imgGray.power(gamma);
					break;
				}
				}
				controller.refreshSecondaryImage();
			}

		});

		increaseContrast.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();
				controller.setSecondaryImage(img);

				// Obtaining r1 and r2
				Dialog<String> dialog = new TextInputDialog();
				dialog.setTitle("Seleccionar parámetros");
				dialog.setHeaderText("Indicar parámetros de r1 y r2. Por ejemplo: 50,180");
				Optional<String> result = dialog.showAndWait();
				if (!result.isPresent())
					return;
				String input = result.get();
				String[] inputs = input.split(",");
				int r1 = Integer.parseInt(inputs[0]);
				int r2 = Integer.parseInt(inputs[1]);

				// Obtaining s1 and s2
				Dialog<String> dialog2 = new TextInputDialog();
				dialog.setTitle("Seleccionar parámetros");
				dialog.setHeaderText("Indicar parámetros de s1 y s2. Por ejemplo: 10,200");
				Optional<String> result2 = dialog2.showAndWait();
				if (!result.isPresent())
					return;
				String input2 = result2.get();
				String[] inputs2 = input2.split(",");
				int s1 = Integer.parseInt(inputs2[0]);
				int s2 = Integer.parseInt(inputs2[1]);

				switch (img.getType()) {
				case IMAGE_GRAY: {
					ImageGray imgGray = (ImageGray) img.copy();
					controller.setSecondaryImage(imgGray);
					imgGray = imgGray.increaseContrast(r1, r2, s1, s2);
					break;
				}
				}
				controller.refreshSecondaryImage();
			}

		});

		equalize.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();
				controller.setSecondaryImage(img);

				switch (img.getType()) {
				case IMAGE_GRAY: {
					ImageGray imgGray = (ImageGray) img.copy();
					controller.setSecondaryImage(imgGray);
					double[] data = HistogramEqualization.equalizeGrayImageHistogram(imgGray);
					imgGray = HistogramEqualization.applyHistogramEqualization(imgGray, data);
					break;
				}
				case IMAGE_RGB: {
					break;
				}
				}
				controller.refreshSecondaryImage();
			}

		});

		sumImages.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Image img1 = controller.getImage();
				Image img2 = controller.getSecondaryImage();
				ImageGray result = (ImageGray) img1.copy();
				controller.setSecondaryImage(result);

				if (img2 == null) {
					return;
				}

				switch (img1.getType()) {
				case IMAGE_GRAY: {
					ImageGray imgGray1 = (ImageGray) img1;
					ImageGray imgGray2 = (ImageGray) img2;
					result = imgGray1.sum(imgGray2);
					controller.setMainImage(result);
					break;
				}
				case IMAGE_RGB: {
					break;
				}
				}
				controller.refreshResultImage();
				controller.refreshImage();
			}

		});

		multiplyImages.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Image img1 = controller.getImage();
				Image img2 = controller.getSecondaryImage();
				if (img2 == null) {
					return;
				}

				switch (img1.getType()) {
				case IMAGE_GRAY: {
					ImageGray imgGray1 = (ImageGray) img1;
					ImageGray imgGray2 = (ImageGray) img2;
					ImageGray result = imgGray1.multiply(imgGray2);
					controller.setResultImage(result);
					break;
				}
				case IMAGE_RGB: {
					break;
				}
				}
				controller.refreshResultImage();
			}

		});

		substractImages.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Image img1 = controller.getImage();
				Image img2 = controller.getSecondaryImage();
				if (img2 == null) {
					return;
				}

				switch (img1.getType()) {
				case IMAGE_GRAY: {
					ImageGray imgGray1 = (ImageGray) img1;
					ImageGray imgGray2 = (ImageGray) img2;
					ImageGray result = imgGray1.substract(imgGray2);
					controller.setResultImage(result);
					break;
				}
				case IMAGE_RGB: {
					break;
				}
				}
				controller.refreshResultImage();
			}

		});

	}

}
