package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.file.ImageFileManager;
import model.image.ImageGray;
import utils.HammingDistance;

public class HammingDistanceTest {
	
	public static void main(String[] args) {
		HammingDistanceTest test = new HammingDistanceTest();
		try {
			double i = test.hammingDistance();
			System.out.println(i);
			System.out.println(i == 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double hammingDistance() throws IOException {
		File file1 = new File(System.getProperty("user.dir") + "/resources/image1.png");
		BufferedImage bi = (new ImageFileManager(file1)).readImage();
		ImageGray image1 = new ImageGray(bi, false);

		File file2 = new File(System.getProperty("user.dir") + "/resources/image1.png");
		BufferedImage bi2 = (new ImageFileManager(file2)).readImage();
		ImageGray image2 = new ImageGray(bi2, false); 
		
		return HammingDistance.compare(image1, image2);
	}
}
