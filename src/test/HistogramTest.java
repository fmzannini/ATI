package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.file.ImageFileManager;
import model.image.ImageColor;
import model.image.ImageColorRGB;
import model.image.ImageGray;
import plot.Histogram;

public class HistogramTest {

	public static void main(String[] args) {
		ImageGray image = equalizeHistogram();
		
		File file = new File(System.getProperty("user.dir") + "/equalizedImage.ppm");
		try {
			new ImageFileManager(file).writeImagePPM(image.showImage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void getGrayScaleHistogram() {
		File file = new File(System.getProperty("user.dir") + "/resources/prueba.png");
		BufferedImage bi;
		try {
			bi = (new ImageFileManager(file)).readImage();
			ImageGray image = new ImageGray(bi, true);
			
			Histogram histogram = new Histogram();
			histogram.grayScalePlot(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getColorScaleHistogram() {
		File file = new File(System.getProperty("user.dir") + "/resources/prueba.png");
		BufferedImage bi;
		try {
			bi = (new ImageFileManager(file)).readImage();
			ImageColorRGB colorImage = new ImageColorRGB(bi);
			
			Histogram histogram = new Histogram();
			histogram.colorScalePlot(colorImage);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ImageGray equalizeHistogram() {
		File file = new File(System.getProperty("user.dir") + "/resources/girl.png");
		BufferedImage bi;
		try {
			bi = (new ImageFileManager(file)).readImage();
			ImageGray image = new ImageGray(bi, false);
			
			Histogram histogram = new Histogram();
			return histogram.applyHistogramEqualization(image, histogram.equalizeGrayImageHistogram(image));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
