package model.mask;

import java.awt.Point;

import model.image.ImageGray;

public interface ScrollableWindow {
	public int getWidthWindow();
	public int getHeightWindow();
	public Point getMiddlePoint();
	public Point[] getDisplacements();
	public Point getCenter();
	
	public boolean hasNext();
	public double[][] nextRegion();
	public void updateCurrentCenter(double value);
	public ImageGray getResult();
}
