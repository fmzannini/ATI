package model.mask.edge_detector;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import model.image.ImageGray;
import model.mask.CardinalDirections;
import model.mask.GaussianMask;
import model.mask.ScrollableWindowRepeat;
import model.mask.edge_detector.gradient.SobelOp;
import model.mask.edge_detector.gradient.SobelOpX;
import model.mask.edge_detector.gradient.SobelOpY;
import utils.LinearTransformation;

public class CannyDetector {

	public ImageGray apply(ImageGray img, int n, double sigma) {
		// filtro gausiano
		GaussianMask gm = new GaussianMask(new ScrollableWindowRepeat((ImageGray) img.copy(), n, n), sigma);
		ImageGray imgWithMask = gm.applyMask();
		double[][] imgWithMaskMatrix = imgWithMask.getImage();

		// sobel con normalización de imagen
		SobelOp sobel = new SobelOp();
		ImageGray sobelImage = (ImageGray) sobel.apply((ImageGray) imgWithMask.copy());
		sobelImage = LinearTransformation.grayImage(sobelImage);
		double[][] sobelMatrix = sobelImage.getImage();

		// derivadas direccionales por cada pixel en x y en y con la imagen pre
		// sobel
		SobelOpX sobelX = new SobelOpX(new ScrollableWindowRepeat(imgWithMask, 3, 3));
		SobelOpY sobelY = new SobelOpY(new ScrollableWindowRepeat(imgWithMask, 3, 3));

		double[][] dx = sobelX.applyMaskWithoutTransformation();
		double[][] dy = sobelY.applyMaskWithoutTransformation();

		// supresión de no máximos
		for (int i = 1; i < imgWithMaskMatrix.length - 1; i++) {
			for (int j = 1; j < imgWithMaskMatrix[0].length - 1; j++) {
				Point p = getBorderDirection(i, j, dx, dy);
				nonMaximumSupresion(i, j, sobelMatrix, p);
			}
		}

		// umbralización con histéresis
		double t1 = calculateT1(imgWithMaskMatrix);
		double t2 = calculateT2(imgWithMaskMatrix);
		
		System.out.println(t1 + "   " + t2);
		List<Point> pendingPoints = new LinkedList<Point>();
		for (int i = 0; i < sobelMatrix.length; i++) {
			for (int j = 0; j < sobelMatrix[0].length; j++) {
				if (sobelMatrix[i][j] < t1) {
					sobelMatrix[i][j] = 0;
				} else if (sobelMatrix[i][j] > t2) {
					sobelMatrix[i][j] = 255;
				} else {
					pendingPoints.add(new Point(i, j));
				}
			}
		}
		boolean wasModified = true;
		while (wasModified && !pendingPoints.isEmpty()) {
			wasModified = false;
			Iterator<Point> it = pendingPoints.iterator();
			System.out.println(pendingPoints.size());
			while (it.hasNext()) {
				Point p = it.next();
				if (hasToBeChanged(sobelMatrix, (int) p.getX(), (int) p.getY())) {
					it.remove();
					wasModified = true;
					sobelMatrix[(int)p.getX()][(int)p.getY()] = 255;
				}
			}
		}
		System.out.println("----\n" + pendingPoints.size());
		for (Point p: pendingPoints) {
			sobelMatrix[(int)p.getX()][(int)p.getY()] = 0;
		}

		return new ImageGray(sobelMatrix);
	}

	private Point getBorderDirection(int i, int j, double[][] dx, double[][] dy) {
		double angle = Math.atan((dy[i][j] + 0.01) / (dx[i][j] + 0.01));
		angle = (angle * 180 / Math.PI);
		if (angle < 0) {
			angle += 180;
		}
		if (angle < 22.5 || angle > 157.5) {
			return new Point(1, 0);
		} else if (angle < 67.5) {
			return new Point(1, 1);
		} else if (angle < 112.5) {
			return new Point(0, 1);
		} else {
			return new Point(1, -1);
		}
	}

	private void nonMaximumSupresion(int i, int j, double[][] sobel, Point p) {
		if (sobel[i][j] <= sobel[i + (int) p.getX()][j + (int) p.getY()]) {
			sobel[i][j] = 0;
			return;
		}
		if (sobel[i][j] <= sobel[i - (int) p.getX()][j - (int) p.getY()]) {
			sobel[i][j] = 0;
			return;
		}
	}

	private double[] getValues(double[][] matrix) {
		double[] result = new double[matrix.length * matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				result[i + j * matrix.length] = matrix[i][j];
			}
		}
		
		return result;
	}

	private double getMean(double[] values) {
		double sum = 0;
		for (double a : values) {
			sum += a;
		}
		return sum / values.length;
	}

	private double getStdrDeviation(double[] values) {
		double mean = getMean(values);
		double aux = 0;
		for (double a : values) {
			aux += ((mean - a) * (mean - a));
		}
		return Math.sqrt(aux / values.length);
	}

	private double calculateT1(double[][] matrix) {
		double[] values = getValues(matrix);
		double mean = getMean(values);
		System.out.println("mean: " + mean);
		double deviation = getStdrDeviation(values);
		System.out.println("deviation: " + deviation);
		return (int) ((mean - deviation) > 0 ? (mean - deviation) : 0);
	}

	private double calculateT2(double[][] matrix) {
		double[] values = getValues(matrix);
		double mean = getMean(values);
		double deviation = getStdrDeviation(values);
		return (int) ((mean + deviation) < 255 ? (mean + deviation) : 255);
	}

	private boolean hasToBeChanged(double[][] matrix, int x, int y) {
		int[][] mask = new int[][] { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
		if (x == 0) {
			for (int i = 0; i < 2; i++) {
				mask[0][i] = 0;
			}
		}
		if (x == matrix.length - 1) {
			for (int i = 0; i < 2; i++) {
				mask[2][i] = 0;
			}
		}
		if (y == 0) {
			for (int i = 0; i < 2; i++) {
				mask[i][0] = 0;
			}
		}
		if (y == matrix[0].length - 1) {
			for (int i = 0; i < 2; i++) {
				mask[i][2] = 0;
			}
		}

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				if (mask[i][j] == 1 && matrix[x + i - 1][y + j - 1] == 255) {
					return true;
				}
			}
		}
		return false;
	}

}
