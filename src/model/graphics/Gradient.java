package model.graphics;

import java.io.File;
import java.io.IOException;

import model.file.ImageFileManager;
import model.image.ImageColor;
import model.image.ImageColorRGB;
import model.image.ImageGray;

public class Gradient {
	private static final int MAX_VALUE = 256;
	private static final int RGB_QTY = 3;

	private static final double[] BLACK = new double[] { 0, 0, 0 };
	private static final double[] RED = new double[] { 255, 0, 0 };
	private static final double[] YELLOW = new double[] { 255, 255, 0 };
	private static final double[] GREEN = new double[] { 0, 255, 0 };
	private static final double[] CYAN = new double[] { 0, 255, 255 };
	private static final double[] BLUE = new double[] { 0, 0, 255 };
	private static final double[] MAGENTA = new double[] { 255, 0, 255 };
	private static final double[] WHITE = new double[] { 255, 255, 255 };

	public ImageGray grayLinearGradient(int width, int height) {
		ImageGray imgGray = new ImageGray(width, height);

		for (int i = 0; i < width; i++) {
			double pixelValue = MAX_VALUE * i / ((double) width);
			for (int j = 0; j < height; j++) {
				imgGray.setPixel(i, j, pixelValue);
			}
		}

		return imgGray;
	}

	public ImageColorRGB colorLinearGradient(int width, int height) {
		ImageColorRGB imgColor = new ImageColorRGB(width, height);

		/*
		 * for(int i=0;i<width;i++){ for(int j=0;j<height;j++){ double[]
		 * pixelValue=new double[RGB_QTY];
		 * pixelValue[0]=MAX_VALUE*((double)i)/((double)width);
		 * pixelValue[1]=MAX_VALUE*((double)(width-i-1))/((double)width);
		 * pixelValue[2]=MAX_VALUE*((double)j)/((double)height);
		 * 
		 * imgColor.setPixel(i, j, pixelValue); } }
		 */
		double[][] points = { BLACK, RED, YELLOW, GREEN, CYAN, BLUE, MAGENTA, WHITE };

		int segmentWidth = width / (points.length - 1);
		for (int i = 0; i < points.length - 1; i++) {
			for (int j = 0; j < segmentWidth; j++) {
				double[] pixelValue = gradientColor(points[i], points[i + 1], j / ((double) segmentWidth));
				setColorCol(imgColor, pixelValue, segmentWidth * i + j);
			}

		}
		return imgColor;
	}

	public double[] gradientColor(double[] origin, double[] dest, double step) {
		double[] ans = new double[RGB_QTY];
		for (int k = 0; k < RGB_QTY; k++) {
			ans[k] = origin[k] + (dest[k] - origin[k]) * step;
		}
		return ans;
	}

	public void setColorCol(ImageColor imgColor, double[] pixelValue, int col) {
		int height = imgColor.getHeight();
		for (int i = 0; i < height; i++) {
			imgColor.setPixel(col, i, pixelValue);
		}
	}

	public static void main(String[] args) throws IOException {
		int width, height;

		width = 1024;
		height = 256;
		Gradient gradient = new Gradient();
		ImageGray gradientGray = gradient.grayLinearGradient(width, height);
		File file = new File(System.getProperty("user.dir") + "/gradientGray.pgm");
		new ImageFileManager(file).writeImagePGM(gradientGray.showImage());

		width = 1024;
		height = 1024;
		ImageColorRGB gradientColor = gradient.colorLinearGradient(width, height);
		file = new File(System.getProperty("user.dir") + "/gradientColor.ppm");
		new ImageFileManager(file).writeImagePPM(gradientColor.showImage());
	}
}
