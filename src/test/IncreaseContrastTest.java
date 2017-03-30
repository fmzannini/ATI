package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.file.ImageFileManager;
import model.image.ImageGray;

public class IncreaseContrastTest {
	
	public static void main(String[] args) {
		IncreaseContrastTest test = new IncreaseContrastTest();
		try {
			ImageGray i = test.increaseContrast(100, 200, 120, 180);
			File file = new File(System.getProperty("user.dir") + "/increaseContrast.ppm");
			new ImageFileManager(file).writeImagePPM(i.showImage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ImageGray increaseContrast(double r1, double r2, double s1, double s2) throws IOException {
		File file = new File(System.getProperty("user.dir") + "/resources/girl.png");
		BufferedImage bi = (new ImageFileManager(file)).readImage();
		ImageGray image = new ImageGray(bi, false);

		return image.increaseContrast(r1, r2, s1, s2);
	}
}
