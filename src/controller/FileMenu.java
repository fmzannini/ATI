package controller;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import controller.utils.MouseSelectionController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import model.file.ImageFileManager;
import model.image.Image;

public class FileMenu extends Menu {
	
//	@FXML
//	private Menu loadImage;
	
	@FXML
	private MenuItem loadImage;

	@FXML
	private MenuItem loadSecondaryImage;
	
	@FXML
	private MenuItem saveImage;
	
	@FXML
	private MenuItem saveSecondaryImage;

	@FXML
	private MenuItem copyRegion;

	
	public FileMenu() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/fileMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void initialize(InterfaceViewController controller) {
		loadImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				File file = fileChooser.showOpenDialog(ATIApplication.primaryStage);
				controller.loadImage(file);
			}
		});
		
		loadSecondaryImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				File file = fileChooser.showOpenDialog(ATIApplication.primaryStage);
				controller.loadSecondaryImage(file);				
			}
		});
		saveImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				Image img=controller.getImage();
				
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save Resource File");
				File file = fileChooser.showSaveDialog(ATIApplication.primaryStage);
				saveImage(file,img);

			}
		});
		
		saveSecondaryImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				Image img=controller.getSecondaryImage();
				
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save Resource File");
				File file = fileChooser.showSaveDialog(ATIApplication.primaryStage);
				saveImage(file,img);

			}
		});
		
		copyRegion.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img=controller.getImage();
				MouseSelectionController selectionController=controller.getMouseSelectionController();
				Point origin=selectionController.getSelectionOrigin();
				Point dest=selectionController.getSelectionEnd();
				if(origin==null || dest==null || img==null)
					return;
				Image region=img.getRegion(origin, dest);
				
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save Resource File");
				File file = fileChooser.showSaveDialog(ATIApplication.primaryStage);
				saveImage(file,region);
				
				selectionController.resetSelection();

			}
		});
	}
	public void saveImage(File file, Image img) {
		try {
			ImageFileManager ifm=new ImageFileManager(file);
			String filename=file.getAbsolutePath();
			String extension=filename.substring(filename.lastIndexOf(".")+1);
			switch(extension.toUpperCase()){
			case "PGM":
				ifm.writeImagePGM(img.showImage());
				break;
			case "RAW":
				BufferedImage bi=img.showImage();
				ifm.writeImageRAW(bi);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText("Image RAW saved");
				alert.setContentText("The image saved "+file.getName()+" is width="+bi.getWidth()+" and height="+bi.getHeight());
				alert.getDialogPane().setPrefSize(300, 300);
				alert.showAndWait();
				break;
			case "PPM":
				ifm.writeImagePPM(img.showImage());
				break;
			case "BMP":
				ifm.writeImageBMP(img.showImage());
				break;
			default:
				ifm.writeImage(img.showImage(), extension);
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
