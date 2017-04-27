package model.noise;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import model.image.ImageGray;
import model.random.UniformRandom;

public abstract class Noise {
	private double density;

	public Noise(double density) {
		this.density = density;
	}

	protected List<Point> generatePoints(int width, int height, int noiseSize) {		
		UniformRandom gen=new UniformRandom();
		List<Point> ans=new LinkedList<Point>();
		
		int imageSize=width*height;
		WrapBuckets buckets=new WrapBuckets(imageSize);
		for(int i=0;i<noiseSize;i++){
			int rand=(int)((imageSize-i)*gen.rand());

			rand=buckets.skipNumbers(rand);
			
			int x=rand%width;
			int y=(rand-rand%width)/width;
			
			buckets.addNumber(rand);
			
			ans.add(new Point(x,y));
		}
		buckets.getAll();
		return ans;
	}


	public static class WrapBuckets{
		private int size=0;
		private List<Buckets> buckets=new ArrayList<Buckets>();
		private int bucketSize;

		public WrapBuckets(int totalSize){
			int qty=(int)Math.sqrt((double)totalSize/Buckets.SIZE_BUCKET);
			bucketSize=(int) Math.ceil((double)totalSize/qty);
			for(int i=0;i<qty;i++)
				buckets.add(new Buckets(bucketSize));
		}
		
		public Collection<? extends Integer> getAll() {
			List<Integer> all=new LinkedList<Integer>();
			for(Buckets bucket:buckets)
				all.addAll(bucket.getAll());
			return all;
		}
	
		public int getSize(){
			return size;
		}
		public void addNumber(int x){
			int index=x/bucketSize;
			buckets.get(index).addNumber(x%bucketSize);
			size++;
		}
		public int skipNumbers(int x){
			int index=0;

			while(index<x/bucketSize){
				x+=buckets.get(index).getSize();
				index++;
			}
			
			while(bucketSize-buckets.get(x/bucketSize).getSize()<=x%bucketSize){
				x+=buckets.get(x/bucketSize).getSize();
			}
			
			return (x-x%bucketSize)+buckets.get(x/bucketSize).skipNumbers(x%bucketSize);
		}
	}

	public static class Buckets{
		private int size=0;
		private List<Set<Integer>> buckets=new ArrayList<Set<Integer>>();
		
		public static final int SIZE_BUCKET=10;
		private static final Comparator<Integer> comp=new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1-o2;
			}
		};
	
		public Buckets(int totalSize){
			int qty=(int)Math.ceil((double)totalSize/SIZE_BUCKET);
			for(int i=0;i<qty;i++)
				buckets.add(new TreeSet<Integer>(comp));
		}
		
		public Collection<? extends Integer> getAll() {
			List<Integer> all=new LinkedList<Integer>();
			for(Set<Integer> bucket:buckets)
				all.addAll(bucket);
			return all;
		}
	
		public int getSize(){
			return size;
		}
		public void addNumber(int x){
			int index=x/SIZE_BUCKET;
			buckets.get(index).add(x);
			size++;
		}
		public int skipNumbers(int x){
			int index=0;
	
			while(index<x/SIZE_BUCKET){
				x+=buckets.get(index).size();
				index++;
			}
			
			while(SIZE_BUCKET-buckets.get(x/SIZE_BUCKET).size()<=x%SIZE_BUCKET){
				x+=buckets.get(x/SIZE_BUCKET).size();
			}
			
			for(Integer x2:buckets.get(x/SIZE_BUCKET)){
				if(x2<=x)
					x++;
				else
					break;
			}
			return x;
		}
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
