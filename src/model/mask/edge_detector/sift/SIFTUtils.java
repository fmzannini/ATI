package model.mask.edge_detector.sift;

import java.util.Vector;

import model.image.Image;
import model.image.ImageColorRGB;
import mpi.cbg.fly.Feature;
import mpi.cbg.fly.PointMatch;
import mpi.cbg.fly.SIFT;

public class SIFTUtils {
	public static Image sift(ImageColorRGB image) {
		Vector<Feature> f1 = SIFT.getFeatures(image.showImage());
		paintFeatures(image, f1);
		return image;
	}

	public static Image[] sift(ImageColorRGB imageA, ImageColorRGB imageB) {
		Vector<Feature> fs1 = SIFT.getFeatures(imageA.showImage());
		Vector<Feature> fs2 = SIFT.getFeatures(imageB.showImage());
		Image image1 = imageA.copy();
		Image image2 = imageB.copy();

		Vector<PointMatch> matches = SIFT.createMatches(fs1, fs2, 1.5f, null, 1);
		for (PointMatch match : matches) {
			double[] color = { (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255) };
			paintNeighbours(image1, (int) match.getP1().getL()[0], (int) match.getP1().getL()[1], color);
			paintNeighbours(image2, (int) match.getP2().getL()[0], (int) match.getP2().getL()[1], color);
		}
		// System.out.println((double) matches.size()
		// / Math.min(fs1.size(), fs2.size()) * 100 + "%");
//		paintFeatures(image1, fs1);
//		paintFeatures(image2, fs2);
		Image[] pair = { image1, image2 };
		return pair;
	}

	private static void paintFeatures(Image image, Vector<Feature> features) {
		double[] green = { 0.0, 255.0, 0, 0 };
		for (Feature feature : features) {
			paintNeighbours(image, (int) feature.location[0], (int) feature.location[1], green);
		}

	}

	public static void paintNeighbours(Image image, int x, int y, double[] color) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				((ImageColorRGB) image).setPixel(x + i, y + j, color);
			}
		}
	}
}
