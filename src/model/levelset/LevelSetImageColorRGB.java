package model.levelset;

import java.awt.Point;

import model.image.Image;
import model.image.ImageColorRGB;

public class LevelSetImageColorRGB extends LevelSetImage {
	private ImageColorRGB img;
	
	private double[] meanColorObject;
	private double[] meanColorBackground;
	
	private double[][] cache;
	
	public double[] getMeanColorObject() {
		return meanColorObject;
	}
	public double[] getMeanColorBackground() {
		return meanColorBackground;
	}

	public LevelSetImageColorRGB(ImageColorRGB img, Point topLeft,Point bottomRight, ImageColorRGB regionOut) {
		super(img);
		this.img=img;
		
		ImageColorRGB regionIn=img.getRegion(topLeft, bottomRight);
		this.meanColorObject=regionIn.getMeanValuePixels();
		this.meanColorBackground=regionOut.getMeanValuePixels();
		
		this.cache=new double[img.getWidth()][img.getHeight()];
	}
	
	public LevelSetImageColorRGB(ImageColorRGB img,	double[] meanColorObject, double[] meanColorBackground){
		super(img);
		this.img=img;
		
		this.meanColorObject=meanColorObject;
		this.meanColorBackground=meanColorBackground;

		this.cache=new double[img.getWidth()][img.getHeight()];
	}
	

	@Override
	public double calculateFd(Point p) {
		if(cache[p.x][p.y]!=0)
			return cache[p.x][p.y];
		double[] pixel=this.img.getPixel(p);
		
		double aux1=norm2(this.meanColorBackground,pixel);
		double aux2=norm2(this.meanColorObject,pixel);
	
		double ans=Math.log(aux1/aux2);
		cache[p.x][p.y]=ans;
		return ans;
	}
	
	private double norm2(double[] pixel1,double[] pixel2){
		double acum=0;
		for(int i=0;i<pixel1.length;i++){
			acum+=Math.pow(pixel1[i]-pixel2[i],2);
		}
		return Math.sqrt(acum);
	}

}
