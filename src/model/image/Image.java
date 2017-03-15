package model.image;

import java.awt.Point;
import java.awt.image.BufferedImage;

public interface Image {

	public static final int RGB_QTY=3;

	
	public int getWidth();
	public int getHeight();

	public Image getRegion(Point origin, Point end);
	public void overlapRegion(ImageColor region, Point origin);
	public void overlapRegion(ImageGray region, Point origin);
	
	public BufferedImage showImage();
	
}
