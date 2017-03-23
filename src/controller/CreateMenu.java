package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import model.file.ImageFileManager;
import model.graphics.Gradient;
import model.graphics.ImageTest;
import model.image.ImageColorRGB;
import model.image.ImageGray;

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
				ImageTest imageGenerator=new ImageTest();
				BufferedImage img=imageGenerator.generateSquare();
				File file=new File(System.getProperty("user.dir")+"/square.pgm");
				ImageFileManager fileManager=new ImageFileManager(file);
				try {
					fileManager.writeImagePGM(img);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		whiteCircle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				//llamada a la función de círculo blanco
				ImageTest imageGenerator=new ImageTest();
				BufferedImage img=imageGenerator.generateCircle();
				File file=new File(System.getProperty("user.dir")+"/circle.pgm");
				ImageFileManager fileManager=new ImageFileManager(file);
				try {
					fileManager.writeImagePGM(img);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		grayGradient.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				//llamada a la función del degradé de grises
				Gradient gradient=new Gradient();
				ImageGray img=gradient.grayLinearGradient(1024, 256);
				File file=new File(System.getProperty("user.dir")+"/gradientGray.pgm");
				ImageFileManager fileManager=new ImageFileManager(file);
				try {
					fileManager.writeImagePGM(img.showImage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		colorGradient.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				//llamada a la función del degradé de colores
				Gradient gradient=new Gradient();
				ImageColorRGB img=gradient.colorLinearGradient(1024, 256);
				File file=new File(System.getProperty("user.dir")+"/gradientColor.ppm");
				ImageFileManager fileManager=new ImageFileManager(file);
				try {
					fileManager.writeImagePPM(img.showImage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
}
