package controller;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import controller.segmentation.ButtonLevelSetToImage;
import controller.segmentation.ButtonLevelSetToVideo;
import controller.utils.UtilsDialogs;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import model.graphics.GraphicLibrary;
import model.hough.EquationCircle;
import model.hough.EquationRect;
import model.hough.HoughTransform;
import model.hough.Param;
import model.image.Image;
import model.image.Image.ImageType;
import model.image.ImageColorRGB;
import model.image.ImageGray;
import model.mask.edge_detector.gradient.PrewittOp;

public class SegmentationMenu extends Menu{

	@FXML
	private MenuItem houghTransformRects;
	@FXML
	private MenuItem houghTransformCircle;

	
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
		houghTransformRects.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();
				if (img == null)
					return;

				Image copy = img.copy();
				int maxDim=Math.max(copy.getWidth(), copy.getHeight());
				double rLeftRange=-Math.sqrt(2)*maxDim;
				double rRightRange=Math.sqrt(2)*maxDim;
				double angleLeftRange=-90;
				double angleRightRange=90;
				String[] inputs=UtilsDialogs.getInputs("Transformada Hough rectas", ""
						+ "Rangos:"
						+ "\n\t r=["+rLeftRange+","+rRightRange+")"
						+ "\n\t angle=["+angleLeftRange+","+angleRightRange+")"
						+ "\n Ingrese el paso de discretización (r,angle)"
						+ "\n ej:1,1", ",");
				
				if(inputs==null || inputs.length!=2)
					return;
				double rStep=Math.abs(Double.parseDouble(inputs[0]));
				double angleStep=Math.abs(Double.parseDouble(inputs[1]));
				
				EquationRect equationRect=new EquationRect();
				Param rParam=new Param(rLeftRange, rRightRange, rStep); 
				Param angleParam=new Param(angleLeftRange, angleRightRange, angleStep); 

				Param[] params=equationRect.getParamArray(rParam, angleParam);
				switch(copy.getType()){
				case IMAGE_GRAY:
					ImageGray binaryImg=binarizeImg((ImageGray)copy);
					HoughTransform houghTransform=new HoughTransform(binaryImg, equationRect, params);
					houghTransform.apply();
					List<Double[]> paramsRects=houghTransform.getResults();
					
					copy=drawRects(paramsRects, equationRect, copy);
					break;
				case IMAGE_RGB:
					ImageColorRGB imgColor=(ImageColorRGB)copy;
					for(int i=0;i<Image.RGB_QTY;i++){
						ImageGray binaryImgBand=binarizeImg(imgColor.getBandOnlyGray(i));
						HoughTransform houghTransformBand=new HoughTransform(binaryImgBand, equationRect, params);
						houghTransformBand.apply();
						List<Double[]> paramsRectsBand=houghTransformBand.getResults();
						
						copy=drawRects(paramsRectsBand, equationRect, copy);						
					}

					break;
				}
				
				controller.setSecondaryImage(copy);
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}


			private Image drawRects(List<Double[]> paramsRects, EquationRect equationRect, Image copy) {
				int leftX=0;
				int rightX=copy.getWidth()-1;
				int topY=0;
				int bottomY=copy.getHeight()-1;
				List<List<Point>> rects=new ArrayList<List<Point>>();
				for(Double[] paramsValue:paramsRects){
					double[] params=new double[paramsValue.length];
					for(int i=0;i<paramsValue.length;i++)
						params[i]=paramsValue[i];
					List<Point> rect=getBoundsPoints(params,equationRect,leftX,rightX,topY,bottomY);
					rects.add(rect);
				}
				if(copy.getType()==ImageType.IMAGE_GRAY)
					copy=new ImageColorRGB((ImageGray)copy);
				BufferedImage bufImg=copy.showImage();
				GraphicLibrary graphics=new GraphicLibrary(bufImg.createGraphics());
				for(List<Point> rect:rects)
					graphics.drawLine(rect.get(0), rect.get(1));
				
				switch(copy.getType()){
				case IMAGE_GRAY:
					copy=new ImageGray(bufImg, false);
					break;
				case IMAGE_RGB:
					copy=new ImageColorRGB(bufImg);
					break;
				}
				
				return copy;
			}

			private List<Point> getBoundsPoints(double[] params, EquationRect equationRect,
					int leftX, int rightX, int topY, int bottomY) {
				Double leftY=equationRect.getY(params, leftX);
				Double rightY=equationRect.getY(params, rightX);
				Double topX=equationRect.getX(params, topY);
				Double bottomX=equationRect.getX(params, bottomY);
				
				List<Point> points=new ArrayList<Point>();
				
				if(leftY!=null && leftY>=topY && leftY<=bottomY)
					points.add(new Point(leftX,(int)Math.round((float)(double)leftY)));
				if(rightY!=null && rightY>=topY && rightY<=bottomY)
					points.add(new Point(rightX,(int)Math.round((float)(double)rightY)));
				if(topX!=null && topX>=leftX && topX<=rightX)
					points.add(new Point((int)Math.round((float)(double)topX),topY));
				if(bottomX!=null && bottomX>=leftX && bottomX<=rightX)
					points.add(new Point((int)Math.round((float)(double)bottomX),bottomY));

				return points;
			}
		});
		houghTransformCircle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Image img = controller.getImage();
				if (img == null)
					return;

				Image copy = img.copy();
				
				String[] inputs=UtilsDialogs.getInputs("Transformada Hough circulos", ""
						+ "Ingrese los rangos del centro del círculo (Xmin,Xmax,Ymin,Ymax)."
						+ "\n ej:99,101,99,101", ",");
				if(inputs==null || inputs.length!=4)
					return;

				double minX=Double.parseDouble(inputs[0]);
				double maxX=Double.parseDouble(inputs[1]);
				double minY=Double.parseDouble(inputs[2]);
				double maxY=Double.parseDouble(inputs[3]);

				double r1=Math.max(minX, copy.getWidth()-minX);
				double r2=Math.max(maxX, copy.getWidth()-maxX);
				double r3=Math.max(minY, copy.getHeight()-minY);
				double r4=Math.max(maxY, copy.getHeight()-maxY);
				
				double maxRadius=Math.max(Math.max(r1, r2), Math.max(r3, r4));
				double minRadius=0;
				String[] inputs2=UtilsDialogs.getInputs("Transformada Hough circulo", ""
						+ "Rangos:"
						+ "\n\t xCentro=["+minX+","+maxX+")"
						+ "\n\t yCentro=["+minY+","+maxY+")"
						+ "\n\t radio=["+minRadius+","+maxRadius+")"
	
						+ "\n Ingrese el paso de discretización (x,y,radio)"
						+ "\n ej:1,1,0.1", ",");
				
				if(inputs2==null || inputs2.length!=3)
					return;

				double xStep=Math.abs(Double.parseDouble(inputs2[0]));
				double yStep=Math.abs(Double.parseDouble(inputs2[1]));
				double radiusStep=Math.abs(Double.parseDouble(inputs2[2]));
				
				EquationCircle equationCircle=new EquationCircle();
				Param xCenterParam=new Param(minX, maxX, xStep); 
				Param yCenterParam=new Param(minY, maxY, yStep); 
				Param radiusParam=new Param(minRadius,maxRadius,radiusStep); 

				Param[] params=equationCircle.getParamArray(xCenterParam,yCenterParam,radiusParam);
				switch(copy.getType()){
				case IMAGE_GRAY:
					ImageGray binaryImg=binarizeImg((ImageGray)copy);
					HoughTransform houghTransform=new HoughTransform(binaryImg, equationCircle, params);
					houghTransform.apply();
					List<Double[]> paramsCircles=houghTransform.getResults();
					
					copy=drawCircles(paramsCircles, equationCircle, copy);
					break;
				case IMAGE_RGB:
					ImageColorRGB imgColor=(ImageColorRGB)copy;
					for(int i=0;i<Image.RGB_QTY;i++){
						ImageGray binaryImgBand=binarizeImg(imgColor.getBandOnlyGray(i));
						HoughTransform houghTransformBand=new HoughTransform(binaryImgBand, equationCircle, params);
						houghTransformBand.apply();
						List<Double[]> paramsCircleBand=houghTransformBand.getResults();
						
						copy=drawCircles(paramsCircleBand, equationCircle, copy);						
					}

					break;
				}
				
				controller.setSecondaryImage(copy);
				controller.refreshSecondaryImage();
				controller.refreshImage();
			}

			private Image drawCircles(List<Double[]> paramsCircles, EquationCircle equationCircle, Image copy) {
				if(copy.getType()==ImageType.IMAGE_GRAY)
					copy=new ImageColorRGB((ImageGray)copy);
				BufferedImage bufImg=copy.showImage();
				
				GraphicLibrary graphics=new GraphicLibrary(bufImg.createGraphics());

				for(Double[] paramsValue:paramsCircles){
					double[] params=new double[paramsValue.length];
					for(int i=0;i<paramsValue.length;i++)
						params[i]=paramsValue[i];
					Point center=new Point(Math.round((float)equationCircle.getXCenter(params)),Math.round((float)equationCircle.getYCenter(params)));
					double radius=equationCircle.getRadius(params);
					graphics.drawCircleLine(center, radius);
				}
				
				copy=new ImageColorRGB(bufImg);
				
				return copy;
			}
		});

		levelSetToImage.setOnAction(new EventHandler<ActionEvent>() {
			private ButtonLevelSetToImage buttonHandler=new ButtonLevelSetToImage(controller);
			@Override
			public void handle(ActionEvent event) {
				buttonHandler.call();
			}
		});
		levelSetToVideo.setOnAction(new EventHandler<ActionEvent>() {
			private ButtonLevelSetToVideo buttonHandler=new ButtonLevelSetToVideo(controller);
			@Override
			public void handle(ActionEvent event) {
				buttonHandler.call();
			}		
		});

	}

	private ImageGray binarizeImg(ImageGray copy) {
		PrewittOp prewittOp=new PrewittOp();
		ImageGray img=(ImageGray) prewittOp.apply(copy);
		img=img.applyThresholding(60);
		//OtsuThresholding otsuThresholding=new OtsuThresholding((ImageGray) img);
		//img=otsuThresholding.calculateOtsuThreshold();
		return img;
	}


}
