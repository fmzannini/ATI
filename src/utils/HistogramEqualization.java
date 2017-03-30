package utils;

import model.image.ImageGray;

public class HistogramEqualization {
	public static double[] equalizeGrayImageHistogram(ImageGray image) {
		// Get initial pixel value frequency into array
		int[] pixelFrequency = getPixelFrequency(image);

		// Calculate relative frequency
		double[] pixelRelativeFrequency = new double[256];
		int totalPixels = image.getHeight() * image.getWidth();
		for (int i = 0; i < pixelRelativeFrequency.length; i++) {
			pixelRelativeFrequency[i] = (double) pixelFrequency[i] / totalPixels;
		}
		for (int i = 1; i < pixelRelativeFrequency.length; i++) {
			pixelRelativeFrequency[i] = pixelRelativeFrequency[i] + pixelRelativeFrequency[i - 1];
		}

		// Transform relative frequency to fit in interval [0, 256]
		double minFrequency = pixelRelativeFrequency[0];
		for (int i = 0; i < pixelRelativeFrequency.length; i++) {
			double si = pixelRelativeFrequency[i];
			pixelRelativeFrequency[i] = Math.floor(((si - minFrequency) * 255 / (1 + minFrequency)) + 0.5);
		}

		return pixelRelativeFrequency;
	}

	private static int[] getPixelFrequency(ImageGray image) {
		int[] pixelFrequency = new int[256];
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				int pixelValue = (int) Math.floor(image.getImage()[i][j] + 0.5);
				pixelFrequency[pixelValue]++;
			}
		}
		return pixelFrequency;
	}

	public static ImageGray applyHistogramEqualization(ImageGray image, double[] equalizedHistogramDataset) {
		double[][] equalizedImage = image.getImage();
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				int pixelValue = (int) Math.floor(equalizedImage[i][j] + 0.5);
				equalizedImage[i][j] = equalizedHistogramDataset[pixelValue];
			}
		}

		return new ImageGray(equalizedImage);
	}
}
