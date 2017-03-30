package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.file.ImageFileManager;
import model.image.ImageGray;

public class ScalarProductImageTest {

	public static void main(String[] args) {
		ScalarProductImageTest test = new ScalarProductImageTest();
		try {
			ImageGray i = test.multiplyImage(5);
			File file = new File(System.getProperty("user.dir") + "/multiplyByScalarImage.ppm");
			new ImageFileManager(file).writeImagePPM(i.showImage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ImageGray multiplyImage(int n) throws IOException {
		File file = new File(System.getProperty("user.dir") + "/resources/image1.png");
		BufferedImage bi = (new ImageFileManager(file)).readImage();
		ImageGray image= new ImageGray(bi, false);
		
		return image.multiply(n);
	}
}
