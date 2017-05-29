package controller.segmentation;

import java.awt.Point;
import java.util.Set;

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

	private State currentState = State.INIT;
	private Point inTopLeft;
	private Point inBottomRight;
	private Point outTopLeft;
	private Point outBottomRight;
	private InterfaceViewController controller;

	public ButtonLevelSetToImage(InterfaceViewController controller) {
		this.controller = controller;
		controller.getMouseSelectionController().registerListener(this);
	}

	public enum State {
		INIT, SELECTION_IN, SELECTION_OUT
	};

	public void call() {
		Image img = controller.getImage();
		if (img == null)
			return;

		UtilsDialogs.showAlert("LevelSet a la imagen ", "Seleccione un área DENTRO del objeto.");

		currentState = State.SELECTION_IN;
	}


	private void process() {
		Image img = controller.getImage();
		if (img == null)
			return;

		Image copy = img.copy();

		String[] inputs = UtilsDialogs.getInputs("LevelSet a la imagen ",
				"Ingrese la máxima cantidad de iteraciones." + "\n Ej:15", ",");
		if (inputs == null || inputs.length != 1)
			return;

		int maxIterations = Integer.parseInt(inputs[0]);

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

		controller.setSecondaryImage(copy);
		controller.refreshSecondaryImage();
		controller.refreshImage();

	}

	private static final double MARK_GRAY_IN = 0;
	private static final double MARK_GRAY_OUT = 255;
	private static final double[] MARK_RGB_IN = { 255, 128, 0 };
	private static final double[] MARK_RGB_OUT = { 0, 128, 255 };

	private void markResults(Image copy, Set<Point> pixelsIn, Set<Point> pixelsOut) {
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
		
				UtilsDialogs.showAlert("LevelSet a la imagen ", "Seleccione un área FUERA del objeto.");
				currentState = State.SELECTION_OUT;
				controller.getMouseSelectionController().resetSelection();
				break;
			case SELECTION_OUT:
				outTopLeft = origin;
				outBottomRight = end;

				controller.getMouseSelectionController().resetSelection();
				process();
				currentState = State.INIT;
				break;
		}
	}

}
