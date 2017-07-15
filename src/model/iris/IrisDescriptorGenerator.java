package model.iris;

import java.awt.Point;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import model.image.ImageGray;

public class IrisDescriptorGenerator {

	
	private static final float SIZE_SAMPLE_RADIUS = 64;
	private InfoIris info;
	private ImageGray eye;
	private double[][] matrix;
	
	
	public IrisDescriptorGenerator(ImageGray eye, InfoIris info) {
		this.info=info;
		this.eye=eye;
	}
	
	
	public ImageGray process() {

		List<Point> minOut=info.getMinOut();
		List<Point> maxOut=info.getMaxOut();
		List<Point> minIn=info.getMinIn();
		List<Point> maxIn=info.getMaxIn();
		
		Comparator<Point> comparator=new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				return o1.y-o2.y;
			}
		};
		
		
		minOut.sort(comparator);
		maxOut.sort(comparator);
		minIn.sort(comparator);
		maxIn.sort(comparator);

		this.matrix=new double[minIn.size()+maxIn.size()][(int) SIZE_SAMPLE_RADIUS];

		fillMatrix(minOut, minIn,0);
		fillMatrix(maxOut, maxIn,minIn.size());
		
		
		return new ImageGray(matrix);
	}
	
	private void fillMatrix(List<Point> outList, List<Point> inList, int angleIndexInit) {
		
		
		double rate=((float)outList.size())/((float)inList.size());
		
		Iterator<Point> iterOut=outList.iterator();

		int angleIndex=angleIndexInit;

		for(Point inPoint:inList) {
			Point outPoint=iterOut.next();
			
			fillMatrix(angleIndex++, outPoint,inPoint);
			

			for(int i=0;i<(int)(rate-1) && iterOut.hasNext();i++)
				iterOut.next();
			
		}
		
	}


	private void fillMatrix(int angleIndex, Point outPoint, Point inPoint) {
		
		double dx=outPoint.x-inPoint.x;
		double dy=outPoint.y-inPoint.y;
		
		double m=dy/dx;
		double b=outPoint.y-outPoint.x*m;
		
		double sampleRate=dx/SIZE_SAMPLE_RADIUS;
		
		for(int radiusIndex=0;radiusIndex<SIZE_SAMPLE_RADIUS;radiusIndex++) {
			double xn=inPoint.x+sampleRate*radiusIndex;
			double yn=m*xn+b;
			
			matrix[angleIndex][radiusIndex]=eye.getPixel((int)Math.round(xn), (int)Math.round(yn));
		}
		
	}

}
