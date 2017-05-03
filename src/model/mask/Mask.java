package model.mask;

import model.image.ImageGray;
import utils.LinearTransformation;

public class Mask {
	private double[][] weights;
	private ScrollableWindow scroll;

	public Mask(double[][] weights, ScrollableWindow scroll) {
		this.weights = weights;
		this.scroll = scroll;
	}

	protected double[][] getWeights() {
		return weights;
	}

	public ImageGray applyMask() {
		while (scroll.hasNext()) {
			double[][] region = scroll.nextRegion();
			double newPixel = applyMask(region);
			scroll.updateCurrentCenter(newPixel);
		}
		return finalTransformation(scroll.getResult());
	}

	protected ImageGray finalTransformation(ImageGray result) {
		return LinearTransformation.grayImage(result);
	}

	protected double applyMask(double[][] region, double[][] weights){
		double value = 0.0;
		for (int i = 0; i < region.length; i++) {
			for (int j = 0; j < region[0].length; j++) {
				value += region[i][j] * weights[i][j];
			}
		}
		return value;		
	}
	protected double applyMask(double[][] region) {
		return applyMask(region, weights);
	}
}
