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
}
