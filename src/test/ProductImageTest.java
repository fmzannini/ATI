package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.file.ImageFileManager;
import model.image.ImageGray;

public class ProductImageTest {

	public static void main(String[] args) {
		ProductImageTest test = new ProductImageTest();
		try {
			ImageGray i = test.multiplyImages();
			File file = new File(System.getProperty("user.dir") + "/multiplyImages.ppm");
			new ImageFileManager(file).writeImagePPM(i.showImage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ImageGray multiplyImages() throws IOException {
		File file1 = new File(System.getProperty("user.dir") + "/resources/image1.png");
		BufferedImage bi = (new ImageFileManager(file1)).readImage();
		ImageGray image1 = new ImageGray(bi, false);

		File file2 = new File(System.getProperty("user.dir") + "/resources/image2.png");
		BufferedImage bi2 = (new ImageFileManager(file2)).readImage();
		ImageGray image2 = new ImageGray(bi2, false);
		
		return image1.multiply(image2);
	}
}
