package controller;

import java.awt.Point;
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
import model.noise.AnisotropicDiffusion;
import model.noise.ExponentialNoiseMultiplicative;
import model.noise.GaussianNoiseAdditive;
import model.noise.IsotropicDiffusion;
import model.noise.Noise;
import model.noise.RayleighNoiseMultiplicative;
import model.noise.SaltAndPepperNoise;

public class NoiseMenu extends Menu {

	@FXML
	private MenuItem noiseGaussAdditive;
	@FXML
	private MenuItem noiseRayleighMultiplicative;
	@FXML
	private MenuItem noiseExponentialMultiplicative;
	@FXML
	private MenuItem noiseSaltAndPepper;
	@FXML
	private MenuItem isotropicDiffusion;
	@FXML
	private MenuItem anisotropicDiffusion;

	public NoiseMenu() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/noiseMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void initialize(InterfaceViewController controller) {
		noiseGaussAdditive.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();
				if (img == null)
					return;
				Image copy = img.copy();

				String[] inputs = getInputs("Ruido Gaussiano Aditivo",
						"Ingrese la densidad de contaminación (entre 0 y 1), el valor de mu y el valor de sigma."
								+ "\n Ej:0.5,0,5",
						",");
				if (inputs == null || inputs.length != 3)
					return;

				double density = Double.parseDouble(inputs[0]);
				double mu = Double.parseDouble(inputs[1]);
				double sigma = Double.parseDouble(inputs[2]);

				GaussianNoiseAdditive gna = new GaussianNoiseAdditive(density, mu, sigma);
				applyNoise(gna, copy);
				controller.setSecondaryImage(copy);
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}
		});
		noiseExponentialMultiplicative.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();
				if (img == null)
					return;
				Image copy = img.copy();
				String[] inputs = getInputs("Ruido Exponencial Multiplicativo",
						"Ingrese la densidad de contaminación (entre 0 y 1) y el valor de lambda." + "\n Ej:0.5,5",
						",");
				if (inputs == null || inputs.length != 2)
					return;

				double density = Double.parseDouble(inputs[0]);
				double lambda = Double.parseDouble(inputs[1]);
				ExponentialNoiseMultiplicative enm = new ExponentialNoiseMultiplicative(density, lambda);
				applyNoise(enm, copy);
				controller.setSecondaryImage(copy);
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}
		});
		noiseRayleighMultiplicative.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();
				if (img == null)
					return;
				Image copy = img.copy();
				String[] inputs = getInputs("Ruido Rayleigh Multiplicativo",
						"Ingrese la densidad de contaminación (entre 0 y 1) y el valor de psi." + "\n Ej:0.5,5", ",");
				if (inputs == null || inputs.length != 2)
					return;

				double density = Double.parseDouble(inputs[0]);
				double psi = Double.parseDouble(inputs[1]);
				RayleighNoiseMultiplicative rnm = new RayleighNoiseMultiplicative(density, psi);
				applyNoise(rnm, copy);
				controller.setSecondaryImage(copy);
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}
		});
		noiseSaltAndPepper.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();
				if (img == null)
					return;
				Image copy = img.copy();
				String[] inputs = getInputs("Ruido Sal y Pimienta",
						"Ingrese la densidad de contaminación (entre 0 y 1), el valor de p0 y el valor de p1."
								+ "\n Ej:0.5,0.3,0.7",
						",");
				if (inputs == null || inputs.length != 3)
					return;

				double density = Double.parseDouble(inputs[0]);
				double p0 = Double.parseDouble(inputs[1]);
				double p1 = Double.parseDouble(inputs[2]);

				SaltAndPepperNoise spn = new SaltAndPepperNoise(density, p0, p1);
				applyNoise(spn, copy);
				controller.setSecondaryImage(copy);
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}
		});
		isotropicDiffusion.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();
				if (img == null)
					return;
				Image copy = img.copy();

				String[] inputs = getInputs("Ruido de Difusión Isotrópica",
						"Ingrese el valor de sigma y la cantidad de iteraciones"
								+ "\n Ej:5,3",
						",");
				if (inputs == null || inputs.length != 2)
					return;

				double sigma = Double.parseDouble(inputs[0]);
				int iterations = Integer.parseInt(inputs[1]);

				IsotropicDiffusion id;
				for (int i = 0; i < iterations; i++) {
					System.out.println("a");
					switch (copy.getType()) {
					case IMAGE_GRAY: 
						id = new IsotropicDiffusion((ImageGray)copy, sigma);
						copy = id.diffuseGrayImage();
						break;
					case IMAGE_RGB:
						id = new IsotropicDiffusion((ImageColorRGB)copy, sigma);
						copy = id.diffuseColorImage();
						break;
					}
					
				}
				controller.setSecondaryImage(copy);
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}
		});
		anisotropicDiffusion.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();
				if (img == null)
					return;
				Image copy = img.copy();

				String[] inputs = getInputs("Ruido de Difusión Anisotrópica",
						"Ingrese el valor de sigma, la funcion para calcular coeficientes y la cantidad de iteraciones"
								+ "\n Ej:5,leclarc,3",
						",");
				if (inputs == null || inputs.length != 3)
					return;

				double sigma = Double.parseDouble(inputs[0]);
				boolean leclarc = inputs[1].toLowerCase() == "leclarc";
				int iterations = Integer.parseInt(inputs[2]);

				AnisotropicDiffusion id;
				for (int i = 0; i < iterations; i++) {
					switch (copy.getType()) {
					case IMAGE_GRAY: 
						id = new AnisotropicDiffusion((ImageGray)copy, leclarc, sigma);
						copy = id.diffuseGrayImage();
						break;
					case IMAGE_RGB:
						id = new AnisotropicDiffusion((ImageColorRGB)copy, leclarc, sigma);
						copy = id.diffuseColorImage();
						break;
					}
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

	private void applyNoise(Noise noise, Image img) {
		switch (img.getType()) {
		case IMAGE_GRAY:
			ImageGray imgGray = (ImageGray) img;
			ImageGray imgWithNoise = noise.applyNoise(imgGray);
			imgGray.setRegion(imgWithNoise, new Point(0, 0));
			break;
		case IMAGE_RGB:
			ImageColorRGB imgColor = (ImageColorRGB) img;
			for (int i = 0; i < Image.RGB_QTY; i++) {
				ImageGray imgColorWithNoise = noise.applyNoise(imgColor.getBandOnlyGray(i));
				imgColor.setBand(imgColorWithNoise, i);
			}
			break;
		}
	}

}
