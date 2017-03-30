package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.file.ImageFileManager;
import model.image.ImageGray;

public class ThresholdingTest {

	public static void main(String[] args) {
		ThresholdingTest test = new ThresholdingTest();
		try {
			ImageGray i = test.applyThreshold(120);
			File file = new File(System.getProperty("user.dir") + "/threshold.ppm");
			new ImageFileManager(file).writeImagePPM(i.showImage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ImageGray applyThreshold(int t) throws IOException {
		File file = new File(System.getProperty("user.dir") + "/resources/prueba.png");
		BufferedImage bi = (new ImageFileManager(file)).readImage();
		ImageGray image = new ImageGray(bi, true);
		return image.applyThresholding(t);
	}
}
