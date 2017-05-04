package model.thresholding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.image.Image;
import model.image.ImageColorRGB;
import model.image.ImageGray;

public class GlobalThresholding {

	private ImageGray img;
	private ImageColorRGB imgRGB;
	private static final int RED_BAND = 0;
	private static final int GREEN_BAND = 1;
	private static final int BLUE_BAND = 2;

	public GlobalThresholding(ImageGray img) {
		this.img = img;
		this.imgRGB = null;
	}

	public GlobalThresholding(ImageColorRGB imgRGB) {
		this.imgRGB = imgRGB;
		this.img = null;
	}

	public ImageGray calculateGlobalThreshold(int delta) {
		int t = 0;
		while (t != 0 && t != 255) {
			t = (int) (Math.random() * 255);
		}

		int taux = -delta - 1;
		boolean notfirst = false;
		do {
			List<Point> m1 = new ArrayList<Point>();
			List<Point> m2 = new ArrayList<Point>();

			for (int i = 0; i < img.getWidth(); i++) {
				for (int j = 0; j < img.getHeight(); j++) {
					if (img.getPixel(i, j) > t) {
						m1.add(new Point(i, j));
					} else {
						m2.add(new Point(i, j));
					}
				}
			}

			int t1 = 0;
			for (Point p : m1) {
				t1 += img.getPixel(p);
			}
			t1 = t1 / m1.size();

			int t2 = 0;
			for (Point p : m2) {
				t2 += img.getPixel(p);
			}
			t2 = t2 / m2.size();

			if (notfirst) {
				taux = t;
				t = (t1 + t2) / 2;
			} else {
				t = (t1 + t2) / 2;
				notfirst = true;
			}
		} while ((t - taux) < delta);
		System.out.println(t);
		return img.applyThresholding(t);
	}
	
	public ImageColorRGB calculateGlobalThresholdColor(int delta) {
		int aux = 0;
		while (aux != 0 && aux != 255) {
			aux = (int) Math.random() * 255;
		}
		int[] t = {aux, aux, aux};

		int[] taux = {-delta - 1,-delta - 1,-delta - 1};
		boolean notfirst = false;
		do {
			HashMap<Integer, List<Point>> m1 = new HashMap<Integer, List<Point>>();
			m1.put(RED_BAND, new ArrayList<Point>());
			m1.put(GREEN_BAND, new ArrayList<Point>());
			m1.put(BLUE_BAND, new ArrayList<Point>());
			HashMap<Integer, List<Point>> m2 = new HashMap<Integer, List<Point>>();
			m2.put(RED_BAND, new ArrayList<Point>());
			m2.put(GREEN_BAND, new ArrayList<Point>());
			m2.put(BLUE_BAND, new ArrayList<Point>());

			for (int i = 0; i < imgRGB.getWidth(); i++) {
				for (int j = 0; j < imgRGB.getHeight(); j++) {
					for (int k = 0; k <= BLUE_BAND; k++) {
						if (imgRGB.getPixel(i, j)[k] > t[k]) {
							(m1.get(k)).add(new Point(i, j));
						} else {
							(m2.get(k)).add(new Point(i, j));
						}
					}
				}
			}

			int[] t1 = {0,0,0};
			int[] t2 = {0,0,0};
			for (int k = 0; k <= BLUE_BAND; k++) {
				for (Point p : m1.get(k)) {
					t1[k] += img.getPixel(p);
				}
				t1[k] = t1[k] / (m1.get(k)).size();
				
				for (Point p : m2.get(k)) {
					t2[k] += img.getPixel(p);
				}
				t2[k] = t2[k] / (m2.get(k)).size();
			}

			if (notfirst) {
				taux = t;
				for (int k = 0; k <= BLUE_BAND; k++) {
					t[k] = (t1[k] + t2[k]) / 2;
				}
			} else {
				for (int k = 0; k <= BLUE_BAND; k++) {
					t[k] = (t1[k] + t2[k]) / 2;
				}
				notfirst = true;
			}
		} while ((t[RED_BAND] - taux[RED_BAND]) < delta && (t[GREEN_BAND] - taux[GREEN_BAND]) < delta && (t[BLUE_BAND] - taux[BLUE_BAND]) < delta);
		
		return imgRGB.applyThresholding(t[0], t[1], t[2]);
	}
}
