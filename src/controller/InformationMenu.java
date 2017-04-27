package controller;

import java.awt.Point;
import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import model.image.Image;
import model.image.ImageColorHSV;
import model.image.ImageColorRGB;
import model.image.ImageGray;

public class InformationMenu extends Menu{

	@FXML
	private MenuItem infoOfRegion;

	@FXML
	private MenuItem getPixel;

	public InformationMenu() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/informationMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void initialize(InterfaceViewController controller) {
		infoOfRegion.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				// llamada a la función obtener info de la region
				Image img=controller.getImage();
				Point origin=controller.getSelectionOrigin();
				Point dest=controller.getSelectionEnd();
				if(origin==null || dest==null || img==null)
					return;
				Image region=img.getRegion(origin, dest);
				String width="width="+region.getWidth();
				String height="height="+region.getHeight();
				String qtyPixels="total size=";
				String meanPixelsValue="mean of pixels values=";
				switch(region.getType()){
				case IMAGE_GRAY:
					ImageGray imgGray=(ImageGray) region;
					qtyPixels+=imgGray.getQtyPixels();
					meanPixelsValue+=formatPixelValue(imgGray.getMeanValuePixels());
					break;
				case IMAGE_RGB:
					ImageColorRGB imgRGB=(ImageColorRGB) region;
					qtyPixels+=imgRGB.getQtyPixels();
					meanPixelsValue+=formatPixelValue(imgRGB.getMeanValuePixels());
					break;
				case IMAGE_HSV:
					ImageColorHSV imgHSV=(ImageColorHSV) region;
					qtyPixels+=imgHSV.getQtyPixels();
					meanPixelsValue+=formatPixelValue(imgHSV.getMeanValuePixels());
					break;
				}
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText("Info of region selected");
				alert.setContentText("The region size is "+width+" "+height+" "+qtyPixels+"\n"+"The mean value of pixels is "+meanPixelsValue);
				alert.getDialogPane().setPrefSize(300, 300);
				alert.showAndWait();
				
				controller.resetSelection();
			}
		});
		getPixel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img=controller.getImage();
				if(img==null)
					return;
				
				Dialog<String> dialog=new TextInputDialog();
				dialog.setTitle("Get pixel");
				dialog.setHeaderText("Type x and y ,for example: 5,10");
				Optional<String> result=dialog.showAndWait();
				if(!result.isPresent())
					return;
				String input=result.get();
				String[] inputs=input.split(",");
				int x=Integer.parseInt(inputs[0]);
				int y=Integer.parseInt(inputs[1]);

				if(x>=img.getWidth()||y>=img.getHeight())
					return;
				
				String pixelValue="value=";
				switch(img.getType()){
				case IMAGE_GRAY:
					ImageGray imgGray=(ImageGray)img;
					pixelValue+=formatPixelValue(imgGray.getPixel(x, y));
					break;
				case IMAGE_RGB:
					ImageColorRGB imgRGB=(ImageColorRGB)img;
					pixelValue+=formatPixelValue(imgRGB.getPixel(x, y));
					break;
				case IMAGE_HSV:
					ImageColorHSV imgHSV=(ImageColorHSV)img;
					pixelValue+=formatPixelValue(imgHSV.getPixel(x, y));
					break;
				}
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText("Info of pixel");
				alert.setContentText("The pixel value at ("+x+","+y+") is "+pixelValue);
				alert.getDialogPane().setPrefSize(300, 300);
				alert.showAndWait();				
			}
		});
	}
	
	private String formatPixelValue(double[] pixel){
		return "("+String.format("%.2f", (float)pixel[0])+"; "+String.format("%.2f", (float)pixel[1])+"; "+String.format("%.2f", (float)pixel[2])+")";
	}
	private String formatPixelValue(double pixel){
		return String.format("%.2f", (float)pixel);
	}

}
