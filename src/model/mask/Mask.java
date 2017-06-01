package model.mask;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
		Map<Boolean, List<Point>> map = new HashMap<Boolean, List<Point>>();
		map.put(true, new LinkedList<Point>()); //bordes
		map.put(false, new LinkedList<Point>()); //esquinas
		ImageColorRGB colorResult = scroll.getResult().toColor();
		while (scroll.hasNext()) {
			double[][] region = scroll.nextRegion();
			double newPixel = applySusanMask(region, weights, t, scroll.getMiddlePoint(), map, scroll.getCenter());
			scroll.updateCurrentCenter(newPixel);
		}
		double[] redPixel = {255.0, 0.0, 0.0};
		double[] bluePixel = {0.0, 0.0, 255.0};
		for (Point p: map.get(true)) {
			colorResult.setPixel(new Point((int) p.getX(), (int) p.getY()), redPixel);
		}
		for (Point p: map.get(false)) {
			colorResult.setPixel(new Point((int) p.getX(), (int) p.getY()), bluePixel);
		}
		
		return colorResult;
	}
	
	public double applySusanMask(double[][] region, double[][] weights, double t, Point center, Map<Boolean, List<Point>> map, Point p) {
		int k = 0;
		for (int i = 0; i < region.length; i++) {
			for (int j = 0; j < region.length; j++) {
				if (weights[i][j] != 0 && Math.abs(region[i][j] - region[center.x][center.y]) < t) {
					k++;
				}
			}
		}
		
		double s = 1 - ((double) k) / 37.0;
//		double[] originalPixel = new double[3];
//		originalPixel[0] = region[center.x][center.y];
//		originalPixel[1] = region[center.x][center.y];
//		originalPixel[2] = region[center.x][center.y];
		
		if (s >= 0.375 && s < 0.625) {
			map.get(true).add(new Point(p.x, p.y));
			return 255;
		} else if (s >= 0.625 && s < 0.875) {
			map.get(false).add(new Point(p.x, p.y));
			return 255;
		} else {
			return 0;
		}
	}
}
