package model.graphics;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.file.ImageFileManager;
import model.image.ImageGray;

public class ImageTest {

	private static final int WIDTH = 200;
	private static final int HEIGHT = 200;

	private static final double LENGTH_SQUARE = 100;
	private static final double RADIUS_CIRCLE = 50;

	public static void main(String[] args) {
		ImageTest test = new ImageTest();
		BufferedImage square = test.generateSquare();
		BufferedImage circle = test.generateCircle();

		File file = new File(System.getProperty("user.dir") + "/whiteCircle.ppm");
		try {
			new ImageFileManager(file).writeImagePPM(square);
			file = new File(System.getProperty("user.dir") + "/whiteSquare.ppm");
			new ImageFileManager(file).writeImagePPM(circle);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BufferedImage generateSquare() {
		BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_BINARY);
		GraphicLibrary graphics = new GraphicLibrary(bi.createGraphics());

		graphics.drawSquare(new Point(WIDTH / 2, HEIGHT / 2), LENGTH_SQUARE);
		return bi;
	}

	public BufferedImage generateCircle() {
		BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_BINARY);
		GraphicLibrary graphics = new GraphicLibrary(bi.createGraphics());

		graphics.drawCircle(new Point(WIDTH / 2, HEIGHT / 2), RADIUS_CIRCLE);
		return bi;
	}


}
