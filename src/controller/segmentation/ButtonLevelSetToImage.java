package controller.segmentation;

import java.awt.Point;
import java.util.List;

import controller.InterfaceViewController;
import controller.utils.MouseSelectionListener;
import controller.utils.UtilsDialogs;
import model.image.Image;
import model.image.ImageColorRGB;
import model.image.ImageGray;
import model.levelset.AlgorithmLevelSet;
import model.levelset.LevelSetImage;
import model.levelset.LevelSetImageColorRGB;
import model.levelset.LevelSetImageGray;

public class ButtonLevelSetToImage implements MouseSelectionListener{

	private static final String HEADER_FOR_IMAGE="LevelSet a la imagen";
	
	private State currentState = State.INIT;
	private Point inTopLeft;
	private Point inBottomRight;
	private Point outTopLeft;
	private Point outBottomRight;
	private InterfaceViewController controller;
	
	private String headerText;

	private int maxIterations;

	public ButtonLevelSetToImage(InterfaceViewController controller) {
		this(controller,HEADER_FOR_IMAGE);
	}
	public ButtonLevelSetToImage(InterfaceViewController controller, String headerText) {
		this.controller = controller;
		this.headerText=headerText;
		controller.getMouseSelectionController().registerListener(this);
	}

	
	public enum State {
		INIT, SELECTION_IN, SELECTION_OUT
	};

	public void call() {
		Image img = controller.getImage();
		if (img == null)
			return;

		UtilsDialogs.showAlert(headerText, "Seleccione un área DENTRO del objeto.");

		currentState = State.SELECTION_IN;
	}


	protected void process() {
		Image img = controller.getImage();
		if (img == null)
			return;

		Image copy = img.copy();

		AlgorithmLevelSet algorithmLevelSet=applyAlgorithmToSingleImage(copy);
		if(algorithmLevelSet==null)
			return;
		
		controller.setSecondaryImage(copy);
		controller.refreshSecondaryImage();
		controller.refreshImage();
		

	}

	protected AlgorithmLevelSet applyAlgorithmToSingleImage(Image copy){


		LevelSetImage levelSetImage = null;
		switch (copy.getType()) {
		case IMAGE_GRAY:
			ImageGray imgGray = (ImageGray) copy;
			levelSetImage = new LevelSetImageGray(imgGray, inTopLeft, inBottomRight,
					imgGray.getRegion(outTopLeft, outBottomRight));
			break;
		case IMAGE_RGB:
			ImageColorRGB imgRGB=(ImageColorRGB) copy;
			levelSetImage=new LevelSetImageColorRGB(imgRGB, inTopLeft, inBottomRight,
					imgRGB.getRegion(outTopLeft, outBottomRight));
			break;
		}
		AlgorithmLevelSet algorithmLevelSet = new AlgorithmLevelSet(levelSetImage, inTopLeft, inBottomRight,
				maxIterations);

		algorithmLevelSet.apply();

		markResults(copy, algorithmLevelSet.getPixelsIn(), algorithmLevelSet.getPixelsOut());

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
				imgGray.setPixel(p, MARK_GRAY_IN);
			}
			for (Point p : pixelsOut) {
				imgGray.setPixel(p, MARK_GRAY_OUT);
			}
			break;
		case IMAGE_RGB:
			ImageColorRGB imgRGB = (ImageColorRGB) copy;
			for (Point p : pixelsIn) {
				imgRGB.setPixel(p, MARK_RGB_IN);
			}
			for (Point p : pixelsOut) {
				imgRGB.setPixel(p, MARK_RGB_OUT);
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
		switch(currentState){
			case INIT:
				break;
			case SELECTION_IN:
				inTopLeft = origin;
				inBottomRight = end;
		
				UtilsDialogs.showAlert(headerText, "Seleccione un área FUERA del objeto.");
				currentState = State.SELECTION_OUT;
				controller.getMouseSelectionController().resetSelection();
				break;
			case SELECTION_OUT:
				outTopLeft = origin;
				outBottomRight = end;

				controller.getMouseSelectionController().resetSelection();
				currentState = State.INIT;
				
				String[] inputs = UtilsDialogs.getInputs(headerText,
						"Ingrese la máxima cantidad de iteraciones." + "\n Ej:15", ",");
				if (inputs == null || inputs.length != 1)
					return ;

				this.maxIterations = Integer.parseInt(inputs[0]);
				process();
				break;
		}
	}

}
