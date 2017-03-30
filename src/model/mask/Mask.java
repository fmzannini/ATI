package model.mask;

import model.image.ImageGray;

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
		return scroll.getResult();
	}

	protected double applyMask(double[][] region) {
		double value = 0.0;
		for (int i = 0; i < region.length; i++) {
			for (int j = 0; j < region[0].length; j++) {
				value += region[i][j] * weights[i][j];
			}
		}
		return value;
	}
}
