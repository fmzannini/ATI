package model.mask;

import java.awt.Point;

import model.image.ImageColorRGB;
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
	
	public double[][] applyMaskWithoutTransformation() {
		while (scroll.hasNext()) {
			double[][] region = scroll.nextRegion();
			double newPixel = applyMask(region);
			scroll.updateCurrentCenter(newPixel);
		}
		return scroll.getResult().getImage();
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
	
	public ImageColorRGB applySusanMask(double t, int width, int height) {
		ImageColorRGB result = new ImageColorRGB(width, height);
		while (scroll.hasNext()) {
			double[][] region = scroll.nextRegion();
			double[] newPixel = applySusanMask(region, weights, t, scroll.getMiddlePoint());
			System.out.println(scroll.getMiddlePoint());
			result.setPixel(scroll.getMiddlePoint(), newPixel);
		}
		return result;
	}
	
	public double[] applySusanMask(double[][] region, double[][] weights, double t, Point center) {
		int k = 0;
		for (int i = 0; i < region.length; i++) {
			for (int j = 0; j < region.length; j++) {
				if (weights[i][j] != 0 && Math.abs(region[i][j] - region[center.x][center.y]) < t) {
					k++;
				}
			}
		}
		
		double s = 1 - ((double) k) / 37.0;
		double[] redPixel = {255.0, 0.0, 0.0};
		double[] bluePixel = {0.0, 0.0, 255.0};
		double[] originalPixel = new double[3];
		originalPixel[0] = region[center.x][center.y];
		originalPixel[1] = region[center.x][center.y];
		originalPixel[2] = region[center.x][center.y];
		
		if (s >= 0.375 && s < 0.625) {
			return redPixel;
		} else if (s >= 0.625 && s < 0.875) {
			return bluePixel;
		} else {
			return originalPixel;
		}
	}
}
