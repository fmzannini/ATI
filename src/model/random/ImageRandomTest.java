package model.random;

import model.image.ImageGray;
import plot.Histogram;
import utils.LinearTransformation;

public class ImageRandomTest {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 100;

	private static void test(RandomGenerator random, String filename) {
		ImageGray img = new ImageGray(WIDTH, HEIGHT);
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				img.setPixel(i, j, random.rand());
			}
		}
		img = new ImageGray(LinearTransformation.grayImage(img).getImage());

		Histogram histogram = new Histogram();
		histogram.grayScalePlot(img, filename);
	}

	public static void main(String[] args) {
		double psi = 10;
		test(new RayleighRandom(psi), System.getProperty("user.dir") + "/testRayleigh");

		double mu = 0;
		double sigma = 1;
		test(new NormalRandom(mu, sigma), System.getProperty("user.dir") + "/testNormal");

		double lambda = 10;
		test(new ExponentialRandom(lambda), System.getProperty("user.dir") + "/testExponential");

	}

}
