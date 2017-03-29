package model.mask;

import model.image.ImageGray;

public interface ScrollableWindow {
	
	public boolean hasNext();
	public double[][] nextRegion();
	public void updateCurrentCenter(double value);
	public ImageGray getResult();
}
