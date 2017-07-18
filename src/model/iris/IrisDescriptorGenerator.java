package model.iris;

import java.awt.Point;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.jtransforms.fft.DoubleFFT_2D;

import model.image.ImageGray;
import utils.LinearTransformation;

public class IrisDescriptorGenerator {

	
	private static final float SIZE_SAMPLE_RADIUS = 64;
	private InfoIris info;
	private ImageGray eye;
	private double[][] matrix;
	private double f0;
	private double logSigmaOnF0Sqr2;
	
	public IrisDescriptorGenerator(ImageGray eye, InfoIris info, double f0, double sigma) {
		this.info=info;
		this.eye=eye;
		
		this.f0=f0;
		this.logSigmaOnF0Sqr2=2*Math.pow(Math.log(sigma/f0),2);
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
		
		double[][] matrixFft=fft();
		filterLogGabor(matrixFft);
		matrixFft=ifft(matrixFft);
		return LinearTransformation.grayImage(new ImageGray(matrixFft));
		//return new ImageGray(matrixFft);
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

	private double[][] fft() {
		 DoubleFFT_2D fft=new DoubleFFT_2D(matrix.length, matrix[0].length);
		 
		 double[][] matrixFft=new double[matrix.length][matrix[0].length*2];
		 for(int i=0;i<matrix.length;i++) {
			 for(int j=0;j<matrix[0].length;j++) {
				 matrixFft[i][2*j]=matrix[i][j];
			 }
		 }
		 
		 fft.complexForward(matrixFft);
		 
		 return matrixFft;
		 
	}
	
	private double[][] ifft(double[][] matrixFft){
		 DoubleFFT_2D fft=new DoubleFFT_2D(matrixFft.length, matrixFft[0].length/2);

		 fft.complexInverse(matrixFft, false);
		 double[][] ans=new double[matrixFft.length][matrixFft[0].length/2];
		 
		 for(int i=0;i<ans.length;i++){
			 for(int j=0;j<ans[0].length;j++){
				 ans[i][j]=matrixFft[i][2*j];
					
//				 ans[i][j]=Math.hypot(matrixFft[i][2*j],matrixFft[i][2*j+1]);
			 }
		 }
		 
		 
		 return ans;
	}
	
	private void filterLogGabor(double[][] matrixFft) {
		for(int i=0;i<matrixFft.length;i++) {
			for(int j=0;j<matrixFft[0].length;j++) {
				matrixFft[i][j]=logGabor(Math.abs(matrixFft[i][j]));
			}
		}
	}
	
	private double logGabor(double frecuency) {
		if(frecuency==0)
			return 0;
		double logFOnF0= Math.log(frecuency/f0);
		
		double aux1=-Math.pow(logFOnF0,2);
		double aux2=aux1/logSigmaOnF0Sqr2;
		
		return Math.exp(aux2);
	}
}
