package model.noise;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import model.image.ImageGray;
import model.random.UniformRandom;

public abstract class Noise {
	private double density;

	public Noise(double density){
		this.density=density;
	}
	/*
	protected List<Point> generatePoints(int width, int height, int noiseSize) {
		UniformRandom gen=new UniformRandom();
		Set<Point> points=new HashSet<Point>();
		while(points.size()<noiseSize){
			int x=(int)(width*gen.rand());
			int y=(int)(height*gen.rand());
			Point p=new Point(x,y);
			if(!points.contains(p)){
				points.add(p);
			}
		}
		List<Point> ans=new LinkedList<Point>();
		ans.addAll(points);
		return ans;
	}*/
	protected List<Point> generatePoints(int width, int height, int noiseSize) {
		UniformRandom gen=new UniformRandom();
		List<Point> points=new LinkedList<Point>();
		int imageSize=width*height;
		for(int i=0;i<noiseSize;i++){
			int rand=(int)((imageSize-i)*gen.rand());
			
			int x=rand%width;
			int y=(rand-rand%width)/width;
			
			for(Point p:points){
				if(p.y<y || (p.y==y && p.x<=x)){
					x++;
					if(x>=width){
						x=0;
						y++;
					}
				}
			}
			points.add(new Point(x,y));
		}
		return points;
	}
	
	protected double getDensity(){
		return density;
	}
	
	public ImageGray applyNoise(ImageGray image){
		int noiseSize=(int)(getDensity()*image.getWidth()*image.getHeight());
		List<Point> points=generatePoints(image.getWidth(),image.getHeight(), noiseSize);
		Iterator<Point> pointIter=points.iterator();
		
		ImageGray noiseImg=new ImageGray(image.showImage(),false);

		while(pointIter.hasNext()){
			Point point=pointIter.next();
			noiseImg.setPixel(point, applyNoise(image.getPixel(point)));
		}
		
		return finalCorrection(noiseImg);

	}
	
	protected abstract double applyNoise(double oldPixel);
	protected abstract ImageGray finalCorrection(ImageGray imgNoise);


}
