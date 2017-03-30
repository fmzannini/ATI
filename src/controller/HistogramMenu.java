package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import model.image.Image;
import model.image.ImageGray;
import plot.Histogram;

public class HistogramMenu extends Menu {

	@FXML
	private MenuItem mainHistogram;
	
	@FXML
	private MenuItem secondaryHistogram;

	public HistogramMenu() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/histogramMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void initialize(InterfaceViewController controller) {
		mainHistogram.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Histogram h = new Histogram();
				Image image = controller.getImage();
				
				switch(image.getType()) {
				case IMAGE_GRAY: {
					ImageGray grayImage = (ImageGray) image;
					h.grayScalePlot(grayImage, "mainImageHistogram");
					break;
				}
				}
			}
		});
		
		secondaryHistogram.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Histogram h = new Histogram();
				Image image = controller.getSecondaryImage();
				if(image == null) {
					return;
				}
				
				switch(image.getType()) {
				case IMAGE_GRAY: {
					ImageGray grayImage = (ImageGray) image;
					h.grayScalePlot(grayImage, "secondaryImageHistogram");
				}
				}
			}
		});
	}
}
