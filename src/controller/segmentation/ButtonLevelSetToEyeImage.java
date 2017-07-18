package controller.segmentation;

import java.awt.Point;
import java.util.List;

import controller.InterfaceViewController;
import controller.utils.MouseSelectionListener;
import controller.utils.UtilsDialogs;
import model.image.Image;
import model.image.Image.ImageType;
import model.image.ImageColorRGB;
import model.image.ImageGray;
import model.iris.InfoIris;
import model.iris.IrisExtractor;
import model.levelset.AlgorithmLevelSet;
import model.levelset.LevelSetImage;
import model.levelset.LevelSetImageGray;

public class ButtonLevelSetToEyeImage implements MouseSelectionListener{

	private static final String HEADER_FOR_IMAGE="Detectar iris";

	private static final String OUTER_IRIS_MESSAGE = "contorno EXTERIOR del iris. ";
	private static final String INNER_IRIS_MESSAGE = "contorno INTERIOR del iris. ";

	private StateIris currentIrisState = StateIris.OUTER_IRIS;
	private StateLevelSet currentLevelSetState = StateLevelSet.INIT;
	private Point inTopLeft;
	private Point inBottomRight;
	private Point outTopLeft;
	private Point outBottomRight;
	private InterfaceViewController controller;
	
	
	private List<Point> pixelsInOuterIris;
	private List<Point> pixelsOutOuterIris;
	private List<Point> pixelsInInnerIris;
	private List<Point> pixelsOutInnerIris;
	
	private String headerText;

	private int maxIterations;
	private IrisExtractor irisExtractor;

	public ButtonLevelSetToEyeImage(InterfaceViewController controller) {
		this(controller,HEADER_FOR_IMAGE);
	}
	public ButtonLevelSetToEyeImage(InterfaceViewController controller, String headerText) {
		this.controller = controller;
		this.headerText=headerText;
		controller.getMouseSelectionController().registerListener(this);
		irisExtractor=new IrisExtractor();
	}

	public enum StateIris{
		OUTER_IRIS,INNER_IRIS
	};
	public enum StateLevelSet {
		INIT, SELECTION_IN, SELECTION_OUT
	};

	public void reset(){
		currentIrisState=StateIris.OUTER_IRIS;
	}
	public void call() {
		Image img = controller.getImage();
		if (img == null)
			return;
		controller.getMouseSelectionController().resetSelection();
		
		String message=null;
		if(currentIrisState==StateIris.OUTER_IRIS)
			message=OUTER_IRIS_MESSAGE;
		else
			message=INNER_IRIS_MESSAGE;
		UtilsDialogs.showAlert(headerText, message + "Seleccione un área DENTRO del iris.");

		currentLevelSetState = StateLevelSet.SELECTION_IN;
	}

	protected void process() {
		Image img = controller.getImage();
		if (img == null)
			return;

		if(img.getType()!=ImageType.IMAGE_GRAY)
			img= new ImageGray(img.showImage(), true);
		
		controller.setMainImage(img);
		controller.refreshImage();

		ImageGray copy=(ImageGray)img.copy();
		
		AlgorithmLevelSet algorithmLevelSet=applyAlgorithmToSingleImage(copy);
		if(algorithmLevelSet==null)
			return;
		
		
		controller.setSecondaryImage(copy);
		controller.refreshSecondaryImage();
		controller.refreshImage();
		
		switch(currentIrisState){
		case OUTER_IRIS:
			pixelsInOuterIris=algorithmLevelSet.getPixelsIn();
			pixelsOutOuterIris=algorithmLevelSet.getPixelsOut();
			
			currentIrisState=StateIris.INNER_IRIS;
			call();
			break;
		case INNER_IRIS:
			pixelsInInnerIris=algorithmLevelSet.getPixelsIn();
			pixelsOutInnerIris=algorithmLevelSet.getPixelsOut();
			
			
			ImageColorRGB imgColor=irisExtractor.process(copy, pixelsInOuterIris, pixelsOutInnerIris, pixelsInInnerIris);
			
	//markIrisArea(copy, irisArea);
			
			
			controller.setSecondaryImage(imgColor);
			controller.refreshSecondaryImage();
			controller.refreshImage();

			currentIrisState=StateIris.OUTER_IRIS;
		}

	}


	protected AlgorithmLevelSet applyAlgorithmToSingleImage(ImageGray imgGray){

		LevelSetImage levelSetImage = null;
		
		levelSetImage = new LevelSetImageGray(imgGray, inTopLeft, inBottomRight,
				imgGray.getRegion(outTopLeft, outBottomRight));
		
		AlgorithmLevelSet algorithmLevelSet = new AlgorithmLevelSet(levelSetImage, inTopLeft, inBottomRight,
				maxIterations);

		algorithmLevelSet.apply();

		markResults(imgGray, algorithmLevelSet.getPixelsIn(), algorithmLevelSet.getPixelsOut());

		return algorithmLevelSet;
	}
	private static final double MARK_GRAY_IN = 0;
	private static final double MARK_GRAY_OUT = 255;
	private static final double[] MARK_RGB_IN = { 255, 128, 0 };
	private static final double[] MARK_RGB_OUT = { 0, 128, 255 };

	protected void markResults(Image copy, List<Point> pixelsIn, List<Point> pixelsOut) {
		switch (copy.getType()) {
		case IMAGE_GRAY:
			ImageGray imgGray = (ImageGray) copy;
			for (Point p : pixelsIn) {
//				imgGray.setPixel(p, MARK_GRAY_IN);
				imgGray.setPixel(p, 255-imgGray.getPixel(p));			
			}
			for (Point p : pixelsOut) {
//				imgGray.setPixel(p, MARK_GRAY_OUT);
				imgGray.setPixel(p, 255-imgGray.getPixel(p));			
			}
			break;
		case IMAGE_RGB:
			ImageColorRGB imgRGB = (ImageColorRGB) copy;
			for (Point p : pixelsIn) {
//				imgRGB.setPixel(p, MARK_RGB_IN);
				double[] pixel=imgRGB.getPixel(p);
				for(int i=0;i<Image.RGB_QTY;i++)
//					pixel[i]=255-pixel[i];
					pixel[i]=pixel[i]<128?255:0;

				imgRGB.setPixel(p, pixel);
			}
			for (Point p : pixelsOut) {
//				imgRGB.setPixel(p, MARK_RGB_OUT);
				double[] pixel=imgRGB.getPixel(p);
				for(int i=0;i<Image.RGB_QTY;i++)
//					pixel[i]=255-pixel[i];
					pixel[i]=pixel[i]<128?255:0;
				imgRGB.setPixel(p, pixel);
			}
			break;
		}

	}

	@Override
	public void selectionMoved(Point firstClick, Point endPosition) {
	}

	@Override
	public void selectionReset() {
	}

	@Override
	public void selectionEnd(Point origin, Point end) {
		switch(currentLevelSetState){
			case INIT:
				break;
			case SELECTION_IN:
				inTopLeft = origin;
				inBottomRight = end;
		
				String message=null;
				if(currentIrisState==StateIris.OUTER_IRIS)
					message=OUTER_IRIS_MESSAGE;
				else
					message=INNER_IRIS_MESSAGE;
				
				UtilsDialogs.showAlert(headerText, message + "Seleccione un área FUERA del iris.");
				currentLevelSetState = StateLevelSet.SELECTION_OUT;
				controller.getMouseSelectionController().resetSelection();
				break;
			case SELECTION_OUT:
				outTopLeft = origin;
				outBottomRight = end;

				controller.getMouseSelectionController().resetSelection();
				currentLevelSetState = StateLevelSet.INIT;
				
				String[] inputs = UtilsDialogs.getInputs(headerText,
						"Ingrese la máxima cantidad de iteraciones." + "\n Ej:15", ",");
				if (inputs == null || inputs.length != 1)
					return ;

				this.maxIterations = Integer.parseInt(inputs[0]);
				process();
				break;
		}
	}
	public InfoIris getInfoIris() {
		return irisExtractor.getInfoIris();
	}

}
