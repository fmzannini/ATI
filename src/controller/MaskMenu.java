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
import model.mask.GaussianMask;
import model.mask.HighPassMask;
import model.mask.MeanMask;
import model.mask.MedianMask;
import model.mask.MedianWeightsMask;
import model.mask.ScrollableWindowRepeat;
import model.mask.edge_detector.ApplyOperator;
import model.mask.edge_detector.directionals.DirectionalMatrixAOp;
import model.mask.edge_detector.directionals.DirectionalMatrixKirsh;
import model.mask.edge_detector.directionals.DirectionalMatrixPrewitt;
import model.mask.edge_detector.directionals.DirectionalMatrixSobel;
import model.mask.edge_detector.gradient.PrewittOp;
import model.mask.edge_detector.gradient.PrewittOpX;
import model.mask.edge_detector.gradient.PrewittOpY;
import model.mask.edge_detector.gradient.SobelOp;

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
	@FXML
	private MenuItem prewittOpXY;

	@FXML
	private MenuItem prewittOp;
	@FXML
	private MenuItem sobelOp;
	
	@FXML
	private MenuItem directionalMatrixAOp;
	@FXML
	private MenuItem directionalKirshOp;
	@FXML
	private MenuItem directionalPrewittOp;
	@FXML
	private MenuItem directionalSobelOp;

	
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
				Image copy=img.copy();
				String [] inputs=getInputs("Filtro de la Media",
						"Ingrese el tamaño de la ventana (número impar)."
						+ "\n Ej:3",
						",");
				if(inputs==null || inputs.length!=1)
					return;
				
				int n=Integer.parseInt(inputs[0]);

				switch(img.getType()){
				case IMAGE_GRAY:
					ImageGray imgGray=(ImageGray)copy;
					applyMeanMask(imgGray,n);
					break;
				case IMAGE_RGB:
					ImageColorRGB imgColor=(ImageColorRGB) copy;
					for(int i=0;i<ImageColorRGB.RGB_QTY;i++){
						ImageGray band=imgColor.getBandOnlyGray(i);
						applyMeanMask(band,n);
						imgColor.setBand(band,i);
					}
					break;
				}
				controller.setSecondaryImage(copy);
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}
			private void applyMeanMask(ImageGray imgGray,int n){
				MeanMask mm=new MeanMask(new ScrollableWindowRepeat(imgGray, n, n));
				ImageGray imgWithMask=mm.applyMask();
				imgGray.setRegion(imgWithMask, new Point(0,0));
			}
		});
		maskMedian.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img=controller.getImage();
				if(img==null)
					return;
				Image copy=img.copy();
				String [] inputs=getInputs("Filtro de la Mediana",
						"Ingrese el tamaño de la ventana (número impar)."
						+ "\n Ej:3",
						",");
				if(inputs==null || inputs.length!=1)
					return;
				
				int n=Integer.parseInt(inputs[0]);

				switch(img.getType()){
				case IMAGE_GRAY:
					ImageGray imgGray=(ImageGray)copy;
					applyMedianMask(imgGray,n);
					break;
				case IMAGE_RGB:
					ImageColorRGB imgColor=(ImageColorRGB) copy;
					for(int i=0;i<ImageColorRGB.RGB_QTY;i++){
						ImageGray band=imgColor.getBandOnlyGray(i);
						applyMedianMask(band,n);
						imgColor.setBand(band,i);
					}
					break;
				}
				controller.setSecondaryImage(copy);
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}

			private void applyMedianMask(ImageGray imgGray, int n) {
				MedianMask mm=new MedianMask(new ScrollableWindowRepeat(imgGray, n, n));
				ImageGray imgWithMask=mm.applyMask();
				imgGray.setRegion(imgWithMask, new Point(0,0));				
			}
		});
		maskMedianWeights.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img=controller.getImage();
				if(img==null)
					return;
				Image copy=img.copy();
				
				switch(img.getType()){
				case IMAGE_GRAY:
					ImageGray imgGray=(ImageGray)copy;
					applyMedianWeightsMask(imgGray);
					break;
				case IMAGE_RGB:
					ImageColorRGB imgColor=(ImageColorRGB) copy;
					for(int i=0;i<ImageColorRGB.RGB_QTY;i++){
						ImageGray band=imgColor.getBandOnlyGray(i);
						applyMedianWeightsMask(band);
						imgColor.setBand(band,i);
					}
					break;
				}
				controller.setSecondaryImage(copy);
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}

			private void applyMedianWeightsMask(ImageGray imgGray) {
				MedianWeightsMask mwm=new MedianWeightsMask(new ScrollableWindowRepeat(imgGray, 3, 3));
				ImageGray imgWithMask=mwm.applyMask();
				imgGray.setRegion(imgWithMask, new Point(0,0));				
			}
		});
		maskGauss.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img=controller.getImage();
				if(img==null)
					return;
				Image copy=img.copy();
				
				String [] inputs=getInputs("Filtro de Gauss",
						"Ingrese el tamaño de la ventana (número impar) y el valor de sigma."
						+ "\n Ej:7,1",
						",");
				if(inputs==null || inputs.length!=2)
					return;
				
				int n=Integer.parseInt(inputs[0]);
				double sigma=Double.parseDouble(inputs[1]);
				
				switch(img.getType()){
				case IMAGE_GRAY:
					ImageGray imgGray=(ImageGray)copy;
					applyGaussianMask(imgGray,n,sigma);
					break;
				case IMAGE_RGB:
					ImageColorRGB imgColor=(ImageColorRGB) copy;
					for(int i=0;i<ImageColorRGB.RGB_QTY;i++){
						ImageGray band=imgColor.getBandOnlyGray(i);
						applyGaussianMask(band,n,sigma);
						imgColor.setBand(band,i);
					}
					break;
				}
				controller.setSecondaryImage(copy);
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}

			private void applyGaussianMask(ImageGray imgGray, int n, double sigma) {
				GaussianMask gm=new GaussianMask(new ScrollableWindowRepeat(imgGray, n, n),sigma);
				ImageGray imgWithMask=gm.applyMask();
				imgGray.setRegion(imgWithMask, new Point(0,0));
			}
		});
		maskHighPass.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img=controller.getImage();
				if(img==null)
					return;
				Image copy=img.copy();
				
				switch(img.getType()){
				case IMAGE_GRAY:
					ImageGray imgGray=(ImageGray)copy;
					applyHighPassMask(imgGray);
					break;
				case IMAGE_RGB:
					ImageColorRGB imgColor=(ImageColorRGB) copy;
					for(int i=0;i<ImageColorRGB.RGB_QTY;i++){
						ImageGray band=imgColor.getBandOnlyGray(i);
						applyHighPassMask(band);
						imgColor.setBand(band,i);
					}
					break;
				}
				controller.setSecondaryImage(copy);
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}

			private void applyHighPassMask(ImageGray imgGray) {
				HighPassMask gm=new HighPassMask(new ScrollableWindowRepeat(imgGray, 3, 3));
				ImageGray imgWithMask=gm.applyMask();
				imgGray.setRegion(imgWithMask, new Point(0,0));
			}
		});
		prewittOpXY.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img=controller.getImage();
				if(img==null)
					return;
				Image copyX=img.copy();
				Image copyY=img.copy();
				
				switch(img.getType()){
				case IMAGE_GRAY:
					ImageGray imgGrayX=(ImageGray)copyX;
					ImageGray imgGrayY=(ImageGray)copyY;

					applyPrewittOpX(imgGrayX);
					applyPrewittOpY(imgGrayY);

					break;
				case IMAGE_RGB:
					ImageColorRGB imgColorX=(ImageColorRGB) copyX;
					ImageColorRGB imgColorY=(ImageColorRGB) copyY;

					for(int i=0;i<ImageColorRGB.RGB_QTY;i++){
						ImageGray bandX=imgColorX.getBandOnlyGray(i);
						applyPrewittOpX(bandX);
						imgColorX.setBand(bandX,i);
						ImageGray bandY=imgColorY.getBandOnlyGray(i);
						applyPrewittOpX(bandY);
						imgColorY.setBand(bandY,i);
					}
					break;
				}
				controller.setSecondaryImage(copyX);
				controller.refreshSecondaryImage();
				controller.setResultImage(copyY);
				controller.refreshResultImage();
				controller.refreshImage();
			}

			private void applyPrewittOpX(ImageGray imgGray) {
				PrewittOpX pox=new PrewittOpX(new ScrollableWindowRepeat(imgGray, 3, 3));
				ImageGray imgWithMask=pox.applyMask();
				imgGray.setRegion(imgWithMask, new Point(0,0));
			}
			private void applyPrewittOpY(ImageGray imgGray) {
				PrewittOpY poy=new PrewittOpY(new ScrollableWindowRepeat(imgGray, 3, 3));
				ImageGray imgWithMask=poy.applyMask();
				imgGray.setRegion(imgWithMask, new Point(0,0));
			}

		});

		prewittOp.setOnAction(new ButtonApplyMask(new PrewittOp(), controller));
		sobelOp.setOnAction(new ButtonApplyMask(new SobelOp(),controller));

		directionalMatrixAOp.setOnAction(new ButtonApplyMask(new DirectionalMatrixAOp(), controller));
		directionalKirshOp.setOnAction(new ButtonApplyMask(new DirectionalMatrixKirsh(), controller));
		directionalPrewittOp.setOnAction(new ButtonApplyMask(new DirectionalMatrixPrewitt(), controller));
		directionalSobelOp.setOnAction(new ButtonApplyMask(new DirectionalMatrixSobel(), controller));
		
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

	public static class ButtonApplyMask implements EventHandler<ActionEvent>{
		private ApplyOperator operator;
		private InterfaceViewController controller;
		
		public ButtonApplyMask(ApplyOperator operator, InterfaceViewController controller) {
			super();
			this.operator=operator;
			this.controller = controller;
		}

		@Override
		public void handle(ActionEvent event) {
			Image img=controller.getImage();
			if(img==null)
				return;
			Image copy=img.copy();
			
			copy=operator.apply(copy);

			controller.setSecondaryImage(copy);
			controller.refreshSecondaryImage();
			controller.refreshImage();
		}
	}
}
