package model.image;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import utils.DynamicRangeCompression;
import utils.LinearTransformation;

public class ImageGray implements Image {

	private static final int GRAY_BAND = 0;

	private double[][] image;
	private int width;
	private int height;

	public double[][] getImage() {
		return image;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ImageGray(BufferedImage image, boolean isColor) {
		this(image.getWidth(), image.getHeight());
		Raster raster = image.getData();

		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if (isColor) {
					double avg = 0;
					for (int k = 0; k < RGB_QTY; k++) {
						avg += raster.getSampleDouble(i, j, k);
					}
					avg /= RGB_QTY;

					this.image[i][j] = avg;
				} else {
					this.image[i][j] = raster.getSampleDouble(i, j, GRAY_BAND);
				}
			}
		}
	}

	public ImageGray(int width, int height) {
		this.image = new double[width][height];
		this.width = width;
		this.height = height;
	}

	public ImageGray(double[][] image) {
		this.image = image;
		this.width = image.length;
		this.height = image[0].length;
	}

	public double getPixel(Point p) {
		return this.getPixel(p.x, p.y);
	}

	public double getPixel(int x, int y) {
		return this.image[x][y];
	}

	public void setPixel(Point p, double pixel) {
		this.setPixel(p.x, p.y, pixel);
	}

	public void setPixel(int x, int y, double pixel) {
		this.image[x][y] = pixel;
	}

	public ImageGray getRegion(Point origin, Point end) {
		int width = Math.abs(end.x - origin.x);
		int height = Math.abs(end.y - origin.y);
		double[][] region = new double[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				region[i][j] = this.image[origin.x + i][origin.y + j];
			}
		}
		return new ImageGray(region);
	}

	public void setRegion(ImageGray region, Point origin, Point end) {
		int width = region.getWidth();
		int height = region.getHeight();
		double[][] pixels = region.getImage();

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				this.image[origin.x + i][origin.y + j] = pixels[i][j];
			}
		}
	}

	public void overlapRegion(ImageColor region, Point origin) {
		int width = region.getWidth();
		int height = region.getHeight();
		double[][][] pixels = region.getImage();

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				double avg = 0;
				for (int k = 0; k < RGB_QTY; k++) {
					avg += pixels[i][j][k];
				}
				avg /= ((double) RGB_QTY);

				this.image[origin.x + i][origin.y + j] += avg;
			}
		}
	}

	public void overlapRegion(ImageGray region, Point origin) {
		int width = region.getWidth();
		int height = region.getHeight();
		double[][] pixels = region.getImage();

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				this.image[origin.x + i][origin.y + j] += pixels[i][j];
			}
		}
	}

	public BufferedImage showImage() {
		BufferedImage bi = new BufferedImage(this.width, this.height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster wr = bi.getRaster();
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				wr.setSample(i, j, GRAY_BAND, this.image[i][j]);
			}
		}
		return bi;
	}

	public int getQtyPixels() {
		return width * height;
	}

	/* Devuelve el valor promedio de los pixeles */
	public double getMeanValuePixels() {
		double avg = 0;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				avg += this.image[i][j];
			}
		}
		avg /= this.getQtyPixels();

		return avg;
	}

	public BufferedImage getNegative() {
		BufferedImage bi = new BufferedImage(this.width, this.height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster wr = bi.getRaster();
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				wr.setSample(i, j, GRAY_BAND, -this.image[i][j] + 255 - 1);
			}
		}
		return bi;
	}

	public BufferedImage applyThresholding(int t) {
		BufferedImage bi = new BufferedImage(this.width, this.height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster wr = bi.getRaster();
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				wr.setSample(i, j, GRAY_BAND, this.image[i][j] >= t ? 255.0 : 0.0);
			}
		}
		return bi;
	}

	public BufferedImage sum(ImageGray image) {
		if (image.getHeight() != this.height || image.getWidth() != this.width) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				this.image[i][j] = this.image[i][j] + image.getImage()[i][j];
			}
		}
		return LinearTransformation.grayImage(this);
	}

	public BufferedImage multiply(int n) {
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				this.image[i][j] = this.image[i][j] * n;
			}
		}
		return DynamicRangeCompression.grayImage(this);
	}

	public BufferedImage multiply(ImageGray image) {
		if (image.getHeight() != this.height || image.getWidth() != this.width) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				this.image[i][j] = this.image[i][j] * image.getImage()[i][j];
			}
		}
		return LinearTransformation.grayImage(this);
	}

	public BufferedImage substract(ImageGray image) {
		if (image.getHeight() != this.height || image.getWidth() != this.width) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				this.image[i][j] = this.image[i][j] - image.getImage()[i][j];
			}
		}
		return LinearTransformation.grayImage(this);
	}

	public BufferedImage increaseContrast(double r1, double r2, double s1, double s2) {
		BufferedImage bi = new BufferedImage(this.width, this.height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster wr = bi.getRaster();
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if (this.image[i][j] < r1) {
					wr.setSample(i, j, GRAY_BAND, calculateContrast(this.image[i][j], 0, r1, 0, s1));	
				} else if (this.image[i][j] >= r1 || this.image[i][j] <= r2) {
					wr.setSample(i, j, GRAY_BAND, calculateContrast(this.image[i][j], r1, r2, s1, s2));
				} else {
					wr.setSample(i, j, GRAY_BAND, calculateContrast(this.image[i][j], r2, 255, s2, 255));
				}
				
			}
		}
		return bi;
	}

	private double calculateContrast(double pixel, double r1, double r2, double s1, double s2) {
		return ((s2 - s2) / (r2 - r1)) * pixel + s1 - ((s2 - s2) / (r2 - r1)) * r1;
	}

}
