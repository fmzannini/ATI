package model.image;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class ImageColorRGB extends ImageColor {

	private static final int RED_BAND = 0;
	private static final int GREEN_BAND = 1;
	private static final int BLUE_BAND = 2;

	public ImageColorRGB(BufferedImage image) {
		this(image.getWidth(), image.getHeight());
		int width = this.getWidth();
		int height = this.getHeight();
		Raster raster = image.getData();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				double[] pixelValue = new double[RGB_QTY];
				for (int k = 0; k < RGB_QTY; k++) {
					pixelValue[k] = raster.getSampleDouble(i, j, k);
				}
				this.setPixel(i, j, pixelValue);
			}
		}

	}

	public ImageColorRGB(int width, int height) {
		super(width, height, RGB_QTY);
	}

	public ImageColorRGB(double[][][] image) {
		super(image, RGB_QTY);
	}

	@Override
	public ImageType getType() {
		return ImageType.IMAGE_RGB;
	}

	@Override
	public Image getRegion(Point origin, Point end) {
		return new ImageColorRGB(super.getRegionMatrix(origin, end));
	}

	public BufferedImage showImage() {
		int width = this.getWidth();
		int height = this.getHeight();
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster wr = bi.getRaster();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				wr.setSample(i, j, RED_BAND, this.getPixel(i, j)[RED_BAND]);
				wr.setSample(i, j, GREEN_BAND, this.getPixel(i, j)[GREEN_BAND]);
				wr.setSample(i, j, BLUE_BAND, this.getPixel(i, j)[BLUE_BAND]);
			}
		}
		return bi;
	}

	public ImageColorRGB getBand(int band) {
		int width = this.getWidth();
		int height = this.getHeight();
		ImageColorRGB imgColor = new ImageColorRGB(width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				double[] pixelValue = new double[] { 0, 0, 0 };
				pixelValue[band] = this.getPixel(i, j)[band];
				imgColor.setPixel(i, j, pixelValue);
			}
		}
		return imgColor;
	}

	public ImageColorHSV passToHSV() {
		int width = this.getWidth();
		int height = this.getHeight();
		ImageColorHSV imgColor = new ImageColorHSV(width, height);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				double[] pixelRGB = this.getPixel(i, j);
				float[] pixelHSV = new float[HSV_QTY];
				pixelHSV = Color.RGBtoHSB((int) pixelRGB[RED_BAND], (int) pixelRGB[GREEN_BAND],
						(int) pixelRGB[BLUE_BAND], pixelHSV);

				double[] pixelHSV_double = new double[HSV_QTY];
				for (int k = 0; k < HSV_QTY; k++) {
					pixelHSV_double[k] = (double) pixelHSV[k];
				}
				imgColor.setPixel(i, j, pixelHSV_double);
			}
		}
		return imgColor;
	}

	public ImageColorRGB getNegative() {
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				this.getImage()[i][j][RED_BAND] = -this.getImage()[i][j][RED_BAND] + 255 - 1;
				this.getImage()[i][j][GREEN_BAND] = -this.getImage()[i][j][GREEN_BAND] + 255 - 1;
				this.getImage()[i][j][BLUE_BAND] = -this.getImage()[i][j][BLUE_BAND] + 255 - 1;
			}
		}
		return this;
	}

	public ImageColorRGB applyThresholding(int t) {
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				this.getImage()[i][j][RED_BAND] = this.getImage()[i][j][RED_BAND] >= t ? 255.0 : 0.0;
				this.getImage()[i][j][GREEN_BAND] = this.getImage()[i][j][GREEN_BAND] >= t ? 255.0 : 0.0;
				this.getImage()[i][j][BLUE_BAND] = this.getImage()[i][j][BLUE_BAND] >= t ? 255.0 : 0.0;
			}
		}
		return this;
	}

	public ImageColorRGB applyThresholding(int tRed, int tGreen, int tBlue) {
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				this.getImage()[i][j][RED_BAND] = this.getImage()[i][j][RED_BAND] >= tRed ? 255.0 : 0.0;
				this.getImage()[i][j][GREEN_BAND] = this.getImage()[i][j][GREEN_BAND] >= tGreen ? 255.0 : 0.0;
				this.getImage()[i][j][BLUE_BAND] = this.getImage()[i][j][BLUE_BAND] >= tBlue ? 255.0 : 0.0;
			}
		}
		return this;
	}

	public Image copy() {
		double[][][] matrix = new double[this.getWidth()][this.getHeight()][3];
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				matrix[i][j][RED_BAND] = this.getImage()[i][j][RED_BAND];
				matrix[i][j][GREEN_BAND] = this.getImage()[i][j][GREEN_BAND];
				matrix[i][j][BLUE_BAND] = this.getImage()[i][j][BLUE_BAND];
			}
		}
		return new ImageColorRGB(matrix);
	}

	public void setBand(ImageGray band, int k) {
		double[][] pixels = band.getImage();
		for (int i = 0; i < band.getWidth(); i++) {
			for (int j = 0; j < band.getHeight(); j++) {
				double[] currentValue = this.getPixel(i, j);
				currentValue[k] = pixels[i][j];
				this.setPixel(i, j, currentValue);
			}
		}
	}

}
