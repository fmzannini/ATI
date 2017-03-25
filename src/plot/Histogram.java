package plot;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.stream.Stream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import model.image.ImageGray;

public class Histogram {

	public Histogram() {
	}

	public void scaleGrayPlot(ImageGray image) {
		double[] data = new double[image.getHeight() * image.getWidth()];

		double[][] matrix = image.getImage();
		int k = 0;
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				double key = Math.floor(matrix[i][j] + 0.5);
				data[k++] = key;
			}
		}
		
		final HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.FREQUENCY);
		dataset.addSeries("Color del pixel", data, 256);


		JFreeChart histogram = ChartFactory.createHistogram("Distribución de colores", "Color del pixel", "Cantidad",
				dataset, PlotOrientation.VERTICAL, false, true, false);

		int width = 640; /* Width of the image */
		int height = 480; /* Height of the image */
		File BarChart = new File("BarChart.jpeg");

		try {
			ChartUtilities.saveChartAsJPEG(BarChart, histogram, width, height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
