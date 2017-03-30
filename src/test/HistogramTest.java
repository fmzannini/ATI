package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import model.file.ImageFileManager;
import model.image.ImageColor;
import model.image.ImageColorRGB;
import model.image.ImageGray;
import plot.Histogram;
import utils.HistogramEqualization;

public class HistogramTest {

	public static void main(String[] args) {
		getGrayScaleHistogram();
		File file = new File(System.getProperty("user.dir") + "/resources/girl.png");
		BufferedImage bi;
		try {
			bi = (new ImageFileManager(file)).readImage();
			ImageGray image = new ImageGray(bi, false);
			ImageGray equalizedImage = getEqualizedHistogram(image);
			equalizedImage = getEqualizedHistogram(equalizedImage);
			File file2 = new File(System.getProperty("user.dir") + "/equalizedImage.ppm");
			new ImageFileManager(file2).writeImagePPM(equalizedImage.showImage());
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
			histogram.grayScalePlot(image, "test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ImageGray getEqualizedHistogram(ImageGray image) {

		try {
			Histogram h = new Histogram();

			double[] data = HistogramEqualization.equalizeGrayImageHistogram(image);
			image = HistogramEqualization.applyHistogramEqualization(image, data);

			final HistogramDataset dataset = new HistogramDataset();
			dataset.setType(HistogramType.FREQUENCY);
			double[] imageDataSet = h.getImageDataSet(image);
			dataset.addSeries("Color del pixel", imageDataSet, 256);

			JFreeChart histogram = ChartFactory.createHistogram("Distribución de colores", "Color del pixel",
					"Cantidad", dataset, PlotOrientation.VERTICAL, false, true, false);

			int width = 640; /* Width of the image */
			int height = 480; /* Height of the image */
			File gray = new File("equalizedHistogram.jpeg");

			ChartUtilities.saveChartAsJPEG(gray, histogram, width, height);

			return image;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
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
		File file = new File(System.getProperty("user.dir") + "/resources/image2.png");
		BufferedImage bi;
		try {
			bi = (new ImageFileManager(file)).readImage();
			ImageGray image = new ImageGray(bi, false);

			Histogram histogram = new Histogram();
			return HistogramEqualization.applyHistogramEqualization(image, HistogramEqualization.equalizeGrayImageHistogram(image));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
