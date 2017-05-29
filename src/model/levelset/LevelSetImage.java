package model.levelset;

import java.awt.Point;

import model.image.Image;

public abstract class LevelSetImage {
	private Image img;
	
	
	protected LevelSetImage(Image img){
		this.img=img;
	}

	public abstract double calculateFd(Point p);



	public int getWidth() {
		return img.getWidth();
	}

	public int getHeight() {
		return img.getHeight();
	}
}
