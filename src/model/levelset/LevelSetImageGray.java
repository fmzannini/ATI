package model.levelset;

import java.awt.Point;

import model.image.ImageGray;

public class LevelSetImageGray extends LevelSetImage {



	private ImageGray img;
	
	private double meanColorObject;
	private double meanColorBackground;
	
	public LevelSetImageGray(ImageGray img, Point topLeft,Point bottomRight, ImageGray regionOut) {
		super(img);
		this.img=img;
		
		ImageGray regionIn=img.getRegion(topLeft, bottomRight);
		this.meanColorObject=regionIn.getMeanValuePixels();
		this.meanColorBackground=regionOut.getMeanValuePixels();
	}
	
	

	@Override
	public double calculateFd(Point p) {
		double pixel=this.img.getPixel(p);
		
		double aux1=Math.abs(this.meanColorBackground-pixel);
		double aux2=Math.abs(this.meanColorObject-pixel);
	
		double ans=Math.log(aux1/aux2);
		return ans;
	}

}
