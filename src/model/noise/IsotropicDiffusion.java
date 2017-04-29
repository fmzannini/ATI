package model.noise;

import model.image.ImageColorRGB;
import model.image.ImageGray;

public class IsotropicDiffusion {

	private ImageGray img;
	private ImageColorRGB imgRGB;
	double sigma;

	public IsotropicDiffusion(ImageGray img, double sigma) {
		this.img = img;
		this.sigma = sigma;
		this.imgRGB = null;
	}

	public IsotropicDiffusion(ImageColorRGB img, double sigma) {
		this.img = null;
		this.sigma = sigma;
		this.imgRGB = img;
	}

	public ImageGray diffuseGrayImage() {
		ImageGray result = new ImageGray(img.getWidth(), img.getHeight());
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				double dn = 0;
				double ds = 0;
				double de = 0;
				double dw = 0;
				double pixel = img.getPixel(i, j);
				if (i - 1 >= 0) {
					dn = img.getPixel(i - 1, j) - pixel;
				}
				if (i + 1 <= img.getWidth() - 1) {
					ds = img.getPixel(i + 1, j) - pixel;
				}
				if (j + 1 <= img.getHeight() - 1) {
					de = img.getPixel(i, j + 1) - pixel;
				}
				if (j - 1 >= 0) {
					dw = img.getPixel(i, j - 1) - pixel;
				}

				double pixelValue = applyDiffusion(img.getPixel(i, j), dn, ds, de, dw);
				result.setPixel(i, j, pixelValue);
			}
		}

		return result;
	}

	public ImageColorRGB diffuseColorImage() {
		ImageColorRGB result = new ImageColorRGB(imgRGB.getWidth(), imgRGB.getHeight());
		for (int i = 0; i < imgRGB.getWidth(); i++) {
			for (int j = 0; j < imgRGB.getHeight(); j++) {
				double[] dn = { 0.0, 0.0, 0.0 };
				double[] ds = { 0.0, 0.0, 0.0 };
				double[] de = { 0.0, 0.0, 0.0 };
				double[] dw = { 0.0, 0.0, 0.0 };
				double[] pixel = imgRGB.getPixel(i, j);
				if (i - 1 >= 0) {
					dn[0] = imgRGB.getPixel(i - 1, j)[0] - pixel[0];
					dn[1] = imgRGB.getPixel(i - 1, j)[1] - pixel[1];
					dn[2] = imgRGB.getPixel(i - 1, j)[2] - pixel[2];
				}
				if (i + 1 <= img.getWidth() - 1) {
					ds[0] = imgRGB.getPixel(i + 1, j)[0] - pixel[0];
					ds[1] = imgRGB.getPixel(i + 1, j)[1] - pixel[1];
					ds[2] = imgRGB.getPixel(i + 1, j)[2] - pixel[2];
				}
				if (j - 1 >= 0) {
					de[0] = imgRGB.getPixel(i, j - 1)[0] - pixel[0];
					de[1] = imgRGB.getPixel(i, j - 1)[1] - pixel[1];
					de[2] = imgRGB.getPixel(i, j - 1)[2] - pixel[2];
				}
				if (j + 1 <= img.getHeight() - 1) {
					dw[0] = imgRGB.getPixel(i, j + 1)[0] - pixel[0];
					dw[1] = imgRGB.getPixel(i, j + 1)[1] - pixel[1];
					dw[2] = imgRGB.getPixel(i, j + 1)[2] - pixel[2];
				}

				double[] pixelValue = { 0.0, 0.0, 0.0 };
				pixelValue[0] = applyDiffusion(img.getPixel(i, j), dn[0], ds[0], de[0], dw[0]);
				pixelValue[1] = applyDiffusion(img.getPixel(i, j), dn[1], ds[1], de[1], dw[1]);
				pixelValue[2] = applyDiffusion(img.getPixel(i, j), dn[2], ds[2], de[2], dw[2]);
				result.setPixel(i, j, pixelValue);
			}
		}

		return result;
	}

	private double applyDiffusion(double pixel, double dn, double ds, double de, double dw) {
		return pixel + (dn + ds + de + dw) / 4;
	}
}
