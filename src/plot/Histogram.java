package plot;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import model.image.ImageColor;
import model.image.ImageGray;

public class Histogram {

	private static final int RED_BAND = 0;
	private static final int GREEN_BAND = 1;
	private static final int BLUE_BAND = 2;

	public Histogram() {
	}

	public void grayScalePlot(ImageGray image) {
		double[] data = new double[image.getHeight() * image.getWidth()];

		double[][] matrix = image.getImage();
		int k = 0;
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				double key = Math.floor(matrix[i][j] + 0.5);
				data[k] = key;
				k++;
			}
		}

		final HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.FREQUENCY);
		dataset.addSeries("Color del pixel", data, 256);

		JFreeChart histogram = ChartFactory.createHistogram("Distribución de colores", "Color del pixel", "Cantidad",
				dataset, PlotOrientation.VERTICAL, false, true, false);

		int width = 640; /* Width of the image */
		int height = 480; /* Height of the image */
		File gray = new File("grayHistogram.jpeg");

		try {
			ChartUtilities.saveChartAsJPEG(gray, histogram, width, height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void colorScalePlot(ImageColor image) {
		double[] redBand = new double[image.getHeight() * image.getWidth()];
		double[] greenBand = new double[image.getHeight() * image.getWidth()];
		double[] blueBand = new double[image.getHeight() * image.getWidth()];

		double[][][] matrix = image.getImage();
		int k = 0;
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				redBand[k] = Math.floor(matrix[i][j][RED_BAND] + 0.5);
				greenBand[k] = Math.floor(matrix[i][j][GREEN_BAND] + 0.5);
				blueBand[k] = Math.floor(matrix[i][j][BLUE_BAND] + 0.5);
				k++;
			}
		}

		// Creating Histogram Dataset for each color band
		final HistogramDataset redDataset = new HistogramDataset();
		redDataset.setType(HistogramType.FREQUENCY);
		redDataset.addSeries("Tonos de rojo", redBand, 256);

		final HistogramDataset greenDataset = new HistogramDataset();
		greenDataset.setType(HistogramType.FREQUENCY);
		greenDataset.addSeries("Tonos de verde", greenBand, 256);

		final HistogramDataset blueDataset = new HistogramDataset();
		blueDataset.setType(HistogramType.FREQUENCY);
		blueDataset.addSeries("Tonos de azul", blueBand, 256);

		// Creating Histogram for each color band
		JFreeChart redHistogram = ChartFactory.createHistogram("Histograma de Rojos", "Tonos de rojo", "Cantidad",
				redDataset, PlotOrientation.VERTICAL, false, true, false);

		JFreeChart blueHistogram = ChartFactory.createHistogram("Histograma de azules", "Tonos de azul", "Cantidad",
				blueDataset, PlotOrientation.VERTICAL, false, true, false);

		JFreeChart greenHistogram = ChartFactory.createHistogram("Histograma de verdes", "Tonos de verde", "Cantidad",
				greenDataset, PlotOrientation.VERTICAL, false, true, false);

		int width = 640; /* Width of the image */
		int height = 480; /* Height of the image */
		File red = new File("redHistogram.jpeg");
		File green = new File("greenHistogram.jpeg");
		File blue = new File("blueHistogram.jpeg");

		try {
			ChartUtilities.saveChartAsJPEG(red, redHistogram, width, height);
			ChartUtilities.saveChartAsJPEG(green, greenHistogram, width, height);
			ChartUtilities.saveChartAsJPEG(blue, blueHistogram, width, height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double[] equalizeGrayImageHistogram(ImageGray image) {
		// Get initial pixel value frequency into array
		int[] pixelFrequency = getPixelFrequency(image);

		// Calculate relative frequency
		double[] pixelRelativeFrequency = new double[256];
		int totalPixels = image.getHeight() * image.getWidth();
		double minFrequency = 1.0;
		for (int i = 0; i < pixelRelativeFrequency.length; i++) {
			pixelRelativeFrequency[i] = pixelFrequency[i] / totalPixels;

			// saving minimum relative frequency for later
			if (pixelRelativeFrequency[i] < minFrequency) {
				minFrequency = pixelRelativeFrequency[i];
			}
		}

		// Transform relative frequency to fit in interval [0, 256]
		for (int i = 0; i < pixelRelativeFrequency.length; i++) {
			double si = pixelRelativeFrequency[i];
			pixelRelativeFrequency[i] = Math
					.floor(((si - minFrequency) * pixelRelativeFrequency.length / (1 - minFrequency)) + 0.5);
		}
		
		return pixelRelativeFrequency;
	}

	private int[] getPixelFrequency(ImageGray image) {
		int[] pixelFrequency = new int[256];
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				int pixelValue = (int) Math.floor(image.getImage()[i][j] + 0.5);
				pixelFrequency[pixelValue]++;
			}
		}
		return pixelFrequency;
	}

	public ImageGray applyHistogramEqualization(ImageGray image, double[] equalizedHistogramDataset) {
		double[][] equalizedImage = image.getImage();
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				int pixelValue = (int) Math.floor(equalizedImage[i][j] + 0.5);
				equalizedImage[i][j] = equalizedHistogramDataset[pixelValue];
			}
		}
		
		return new ImageGray(equalizedImage);
	}
}
