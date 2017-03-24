package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.file.ImageFileManager;
import model.image.ImageColorRGB;
import model.image.ImageGray;

public class NegativeTest {

	public static void main(String[] args) {
		NegativeTest test = new NegativeTest();
		try {
			BufferedImage i = test.getNegativeGray();
			File file = new File(System.getProperty("user.dir") + "/negativeGray.ppm");
			new ImageFileManager(file).writeImagePPM(i);
			
			i = test.getNegativeColor();
			file = new File(System.getProperty("user.dir") + "/negativeColor.ppm");
			new ImageFileManager(file).writeImagePPM(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BufferedImage getNegativeGray() throws IOException {
		File file = new File(System.getProperty("user.dir") + "/resources/girl.png");
		BufferedImage bi = (new ImageFileManager(file)).readImage();
		ImageGray image = new ImageGray(bi, false);

		return image.getNegative();
	}
	
	public BufferedImage getNegativeColor() throws IOException {
		File file = new File(System.getProperty("user.dir") + "/resources/prueba.png");
		BufferedImage bi = (new ImageFileManager(file)).readImage();
		ImageColorRGB image = new ImageColorRGB(bi);

		return image.getNegative();
	}
}
