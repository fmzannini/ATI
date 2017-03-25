package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.file.ImageFileManager;
import model.image.ImageGray;
import plot.Histogram;

public class HistogramTest {

	public static void main(String[] args) {
		
		try {
			File file = new File(System.getProperty("user.dir") + "/resources/girl.png");
			BufferedImage bi;
			bi = (new ImageFileManager(file)).readImage();
			ImageGray image = new ImageGray(bi, false);
			
			Histogram histogram = new Histogram();
			histogram.scaleGrayPlot(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
