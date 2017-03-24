package utils;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import model.image.ImageGray;

public final class LinearTransformation {
	
	private static final int GRAY_BAND = 0;

	public static BufferedImage grayImage(ImageGray originalImage) {
		double[][] image = originalImage.getImage();
		double max = 0.0;
		double min = 255.0;
		
		//finding max and min pixel in image
		for (int i = 0; i < originalImage.getWidth(); i++) {
			for (int j = 0; j < originalImage.getHeight(); j++) {
				max = image[i][j] > max ? image[i][j] : max;
				min = image[i][j] < min ? image[i][j] : min;
			}
		}
		
		//applying linear transformation
		BufferedImage bi = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster wr = bi.getRaster();
		for (int i = 0; i < originalImage.getWidth(); i++) {
			for (int j = 0; j < originalImage.getHeight(); j++) {
				wr.setSample(i, j, GRAY_BAND, 255 * (image[i][j] - min) / (max - min));
			}
		}
		return bi;
	}
	
}
