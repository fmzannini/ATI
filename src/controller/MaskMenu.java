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
import model.image.ImageGray;
import model.mask.GaussianMask;
import model.mask.HighPassMask;
import model.mask.MeanMask;
import model.mask.MedianMask;
import model.mask.MedianWeightsMask;
import model.mask.ScrollableWindowRepeat;

public class MaskMenu extends Menu{

	@FXML
	private MenuItem maskMean;
	@FXML
	private MenuItem maskMedian;
	@FXML
	private MenuItem maskMedianWeights;
	@FXML
	private MenuItem maskGauss;
	@FXML
	private MenuItem maskHighPass;

	public MaskMenu() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/maskMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void initialize(InterfaceViewController controller) {
		maskMean.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img=controller.getImage();
				if(img==null)
					return;
				String [] inputs=getInputs("Filtro de la Media",
						"Ingrese el tamaño de la ventana (número impar)."
						+ "\n Ej:3",
						",");
				if(inputs==null || inputs.length!=1)
					return;
				
				int n=Integer.parseInt(inputs[0]);

				if(img.getType()!=Image.ImageType.IMAGE_GRAY)
					return;
				ImageGray imgGray=(ImageGray)img;
				MeanMask mm=new MeanMask(new ScrollableWindowRepeat(imgGray, n, n));
				ImageGray imgWithMask=mm.applyMask();
				imgGray.setRegion(imgWithMask, new Point(0,0));
				controller.refreshImage();

			}
		});
		maskMedian.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img=controller.getImage();
				if(img==null)
					return;
				String [] inputs=getInputs("Filtro de la Mediana",
						"Ingrese el tamaño de la ventana (número impar)."
						+ "\n Ej:3",
						",");
				if(inputs==null || inputs.length!=1)
					return;
				
				int n=Integer.parseInt(inputs[0]);

				if(img.getType()!=Image.ImageType.IMAGE_GRAY)
					return;
				ImageGray imgGray=(ImageGray)img;
				MedianMask mm=new MedianMask(new ScrollableWindowRepeat(imgGray, n, n));
				ImageGray imgWithMask=mm.applyMask();
				imgGray.setRegion(imgWithMask, new Point(0,0));
				controller.refreshImage();
			}
		});
		maskMedianWeights.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img=controller.getImage();
				if(img==null)
					return;

				if(img.getType()!=Image.ImageType.IMAGE_GRAY)
					return;
				ImageGray imgGray=(ImageGray)img;
				MedianWeightsMask mwm=new MedianWeightsMask(new ScrollableWindowRepeat(imgGray, 3, 3));
				ImageGray imgWithMask=mwm.applyMask();
				imgGray.setRegion(imgWithMask, new Point(0,0));
				controller.refreshImage();	
			}
		});
		maskGauss.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img=controller.getImage();
				if(img==null)
					return;
				String [] inputs=getInputs("Filtro de Gauss",
						"Ingrese el tamaño de la ventana (número impar) y el valor de sigma."
						+ "\n Ej:3,5",
						",");
				if(inputs==null || inputs.length!=2)
					return;
				
				int n=Integer.parseInt(inputs[0]);
				double sigma=Double.parseDouble(inputs[1]);
				
				if(img.getType()!=Image.ImageType.IMAGE_GRAY)
					return;
				ImageGray imgGray=(ImageGray)img;
				GaussianMask gm=new GaussianMask(new ScrollableWindowRepeat(imgGray, n, n),sigma);
				ImageGray imgWithMask=gm.applyMask();
				imgGray.setRegion(imgWithMask, new Point(0,0));
				controller.refreshImage();	
			}
		});
		maskHighPass.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img=controller.getImage();
				if(img==null)
					return;
				
				if(img.getType()!=Image.ImageType.IMAGE_GRAY)
					return;
				
				ImageGray imgGray=(ImageGray)img;
				HighPassMask gm=new HighPassMask(new ScrollableWindowRepeat(imgGray, 3, 3));
				ImageGray imgWithMask=gm.applyMask();
				imgGray.setRegion(imgWithMask, new Point(0,0));
				controller.refreshImage();					
			}
		});
	}
	private String[] getInputs(String title,String header,String pattern){
		Dialog<String> dialog=new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		Optional<String> result=dialog.showAndWait();
		if(!result.isPresent())
			return null;
		String input=result.get();
		String[] inputs=input.split(pattern);
		return inputs;
	}

}
