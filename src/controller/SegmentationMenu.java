package controller;

import java.awt.Point;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import controller.segmentation.ButtonLevelSetToImage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import model.image.Image;
import model.image.ImageColorRGB;
import model.image.ImageGray;
import model.levelset.AlgorithmLevelSet;
import model.levelset.LevelSetImage;
import model.levelset.LevelSetImageGray;

public class SegmentationMenu extends Menu{

	@FXML
	private MenuItem levelSetToImage;
	@FXML
	private MenuItem levelSetToVideo;

	public SegmentationMenu() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/segmentationMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	
	public void initialize(InterfaceViewController controller) {
		levelSetToImage.setOnAction(new EventHandler<ActionEvent>() {
			private ButtonLevelSetToImage buttonHandler=new ButtonLevelSetToImage(controller);
			@Override
			public void handle(ActionEvent event) {
				buttonHandler.call();
			}
		});
		levelSetToVideo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				
			}
		
		});

	}


}
