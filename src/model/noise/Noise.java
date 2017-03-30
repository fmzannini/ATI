package model.noise;

import java.awt.Point;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import model.image.ImageGray;
import model.random.UniformRandom;

public abstract class Noise {
	private double density;

	public Noise(double density) {
		this.density = density;
	}

	/*
	 * protected List<Point> generatePoints(int width, int height, int
	 * noiseSize) { UniformRandom gen=new UniformRandom(); Set<Point> points=new
	 * HashSet<Point>(); while(points.size()<noiseSize){ int
	 * x=(int)(width*gen.rand()); int y=(int)(height*gen.rand()); Point p=new
	 * Point(x,y); if(!points.contains(p)){ points.add(p); } } List<Point>
	 * ans=new LinkedList<Point>(); ans.addAll(points); return ans; }
	 */

	protected List<Point> generatePoints(int width, int height, int noiseSize) {
		UniformRandom gen=new UniformRandom();
		Comparator<Point> comp=new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				return (o1.y==o2.y)?(o1.x-o2.x):(o1.y-o2.y);
			}
		};
		Map<Integer,Set<Point>> skippedMap=new HashMap<Integer,Set<Point>>();
		for(int i=0;i<height;i++)
			skippedMap.put(i, new TreeSet<Point>(comp));
		
		int imageSize=width*height;
		for(int i=0;i<noiseSize;i++){
			int rand=(int)((imageSize-i)*gen.rand());
			
			int x=rand%width;
			int y=(rand-rand%width)/width;
			
			for(int k=0;k<y;k++)
				x+=skippedMap.get(k).size();
			while(x>=width){
				x-=width;
				y++;
				x+=skippedMap.get(y-1).size();
			}
			for(Point p:skippedMap.get(y)){
				if(p.y<y || (p.y==y && p.x<=x)){
					x++;
					if(x>=width){
						x=0;
						y++;
					}
				}else
					break;
			}
			skippedMap.get(y).add(new Point(x,y));
		}
		List<Point> ans=new LinkedList<Point>();
		for(Entry<Integer,Set<Point>> entry:skippedMap.entrySet())
			ans.addAll(entry.getValue());
		return ans;
	}

	protected double getDensity() {
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
