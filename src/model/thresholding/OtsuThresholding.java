package model.thresholding;

import model.image.ImageColorRGB;
import model.image.ImageGray;

public class OtsuThresholding {

	private ImageGray img;
	private ImageColorRGB imgRGB;
	private static final int RED_BAND = 0;
	private static final int GREEN_BAND = 1;
	private static final int BLUE_BAND = 2;

	public OtsuThresholding(ImageGray img) {
		this.img = img;
		this.imgRGB = null;
	}

	public OtsuThresholding(ImageColorRGB imgRGB) {
		this.imgRGB = imgRGB;
		this.img = null;
	}

	public ImageGray calculateOtsuThreshold() {
		double[] pi = new double[256];
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				pi[(int) img.getPixel(i, j)]++;
			}
		}

		int pixelAmount = img.getWidth() * img.getHeight();
		for (int i = 0; i < pi.length; i++) {
			pi[i] = pi[i] / pixelAmount;
		}

		double[] cumulativeSum = new double[256];
		for (int i = 1; i < cumulativeSum.length; i++) {
			cumulativeSum[i] = pi[i] + cumulativeSum[i - 1];
		}

		double[] cumulativeMean = new double[256];
		for (int i = 1; i < cumulativeMean.length; i++) {
			cumulativeMean[i] = cumulativeMean[i - 1] + pi[i] * i;
		}

		double globalMean = 0.0;
		for (int i = 0; i < pi.length; i++) {
			globalMean += pi[i] * i;
		}

		double[] deviation = new double[256];
		double maxDeviation = 0.0;
		for (int i = 0; i < pi.length; i++) {
			deviation[i] = Math.pow(globalMean * cumulativeSum[i] - cumulativeMean[i], 2)
					/ (cumulativeSum[i] * (1 - cumulativeSum[i]));
			maxDeviation = maxDeviation < deviation[i] ? deviation[i] : maxDeviation;
		}
		
		int otsuThreshold = 0;
		int count = 0;
		for (int i = 0; i < deviation.length; i++) {
			if (deviation[i] == maxDeviation) {
				otsuThreshold += i;
				count++;
			}
		}
		otsuThreshold = (int) (otsuThreshold / count + 0.5);
		
		System.out.println(otsuThreshold);
		return img.applyThresholding((int) (otsuThreshold));
	}
	
	public ImageColorRGB calculateOtsuThresholdColor() {
		double[][] pi = new double[256][3];
		for (int i = 0; i < imgRGB.getWidth(); i++) {
			for (int j = 0; j < imgRGB.getHeight(); j++) {
				pi[(int) imgRGB.getPixel(i, j)[RED_BAND]][RED_BAND]++;
				pi[(int) imgRGB.getPixel(i, j)[GREEN_BAND]][GREEN_BAND]++;
				pi[(int) imgRGB.getPixel(i, j)[BLUE_BAND]][BLUE_BAND]++;
			}
		}

		int pixelAmount = imgRGB.getWidth() * imgRGB.getHeight();
		for (int i = 0; i < 256; i++) {
			pi[i][RED_BAND] = pi[i][RED_BAND] / pixelAmount;
			pi[i][GREEN_BAND] = pi[i][GREEN_BAND] / pixelAmount;
			pi[i][BLUE_BAND] = pi[i][BLUE_BAND] / pixelAmount;
		}

		double[][] cumulativeSum = new double[256][3];
		for (int i = 1; i < 256; i++) {
			cumulativeSum[i][RED_BAND] = pi[i][RED_BAND] + cumulativeSum[i - 1][RED_BAND];
			cumulativeSum[i][GREEN_BAND] = pi[i][GREEN_BAND] + cumulativeSum[i - 1][GREEN_BAND];
			cumulativeSum[i][BLUE_BAND] = pi[i][BLUE_BAND] + cumulativeSum[i - 1][BLUE_BAND];
		}

		double[][] cumulativeMean = new double[256][3];
		for (int i = 1; i < 256; i++) {
			cumulativeMean[i][RED_BAND] = cumulativeMean[i - 1][RED_BAND] + pi[i][RED_BAND] * i;
		}

		double[] globalMean = new double[3];
		for (int i = 0; i < 256; i++) {
			globalMean[RED_BAND] += pi[i][RED_BAND] * i;
			globalMean[GREEN_BAND] += pi[i][GREEN_BAND] * i;
			globalMean[BLUE_BAND] += pi[i][BLUE_BAND] * i;
		}

		double[][] deviation = new double[256][3];
		double[] maxDeviation = new double[3];
		for (int i = 0; i < pi.length; i++) {
			deviation[i][RED_BAND] = Math.pow(globalMean[RED_BAND] * cumulativeSum[i][RED_BAND] - cumulativeMean[i][RED_BAND], 2)
					/ (cumulativeSum[i][RED_BAND] * (1 - cumulativeSum[i][RED_BAND]));
			maxDeviation[RED_BAND] = maxDeviation[RED_BAND] < deviation[i][RED_BAND] ? deviation[i][RED_BAND] : maxDeviation[RED_BAND];
			
			deviation[i][GREEN_BAND] = Math.pow(globalMean[GREEN_BAND] * cumulativeSum[i][GREEN_BAND] - cumulativeMean[i][GREEN_BAND], 2)
					/ (cumulativeSum[i][GREEN_BAND] * (1 - cumulativeSum[i][GREEN_BAND]));
			maxDeviation[GREEN_BAND] = maxDeviation[GREEN_BAND] < deviation[i][GREEN_BAND] ? deviation[i][GREEN_BAND] : maxDeviation[GREEN_BAND];
			
			deviation[i][BLUE_BAND] = Math.pow(globalMean[BLUE_BAND] * cumulativeSum[i][BLUE_BAND] - cumulativeMean[i][BLUE_BAND], 2)
					/ (cumulativeSum[i][BLUE_BAND] * (1 - cumulativeSum[i][BLUE_BAND]));
			maxDeviation[BLUE_BAND] = maxDeviation[BLUE_BAND] < deviation[i][BLUE_BAND] ? deviation[i][BLUE_BAND] : maxDeviation[BLUE_BAND];
		}
		
		int[] otsuThreshold = new int[3];
		int[] count = new int[3];
		for (int i = 0; i < 256; i++) {
			if (deviation[i][RED_BAND] == maxDeviation[RED_BAND]) {
				otsuThreshold[RED_BAND] += i;
				count[RED_BAND]++;
			}
			if (deviation[i][GREEN_BAND] == maxDeviation[GREEN_BAND]) {
				otsuThreshold[GREEN_BAND] += i;
				count[GREEN_BAND]++;
			}
			if (deviation[i][BLUE_BAND] == maxDeviation[BLUE_BAND]) {
				otsuThreshold[BLUE_BAND] += i;
				count[BLUE_BAND]++;
			}
		}
		otsuThreshold[RED_BAND] = (int) (otsuThreshold[RED_BAND] / count[RED_BAND] + 0.5);
		otsuThreshold[GREEN_BAND] = (int) (otsuThreshold[GREEN_BAND] / count[GREEN_BAND] + 0.5);
		otsuThreshold[BLUE_BAND] = (int) (otsuThreshold[BLUE_BAND] / count[BLUE_BAND] + 0.5);
		
		System.out.println(otsuThreshold);
		return imgRGB.applyThresholding(otsuThreshold[RED_BAND], otsuThreshold[GREEN_BAND], otsuThreshold[BLUE_BAND]);
	}
}
