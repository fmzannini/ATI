package model.harris;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import model.image.Image;
import model.image.ImageColorRGB;
import model.image.ImageGray;
import model.mask.GaussianMask;
import model.mask.Mask;
import model.mask.ScrollableWindowRepeat;
import model.mask.edge_detector.gradient.SobelOpX;
import model.mask.edge_detector.gradient.SobelOpY;

public class HarrisDetector {

	private static final int GAUSSIAN_WINDOW_SIZE = 7;
	private static final double GAUSSIAN_SIGMA = 2;
	private static final double K_CONST = 0.04;
	
	private Image img;
	private List<Point> detectedPoints=new ArrayList<Point>();
	private double threshold; //porcentaje del máximo valor de matriz R
	public HarrisDetector(Image img, double threshold){
		this.img=img;
		this.threshold=threshold;

	}
	
	public void apply(){
		switch(img.getType()){
		case IMAGE_GRAY:
			ImageGray imgGray=(ImageGray) img;
			apply(imgGray);
			break;
		case IMAGE_RGB:
			ImageColorRGB imgColor=(ImageColorRGB)img;
			apply(imgColor);
			break;
		}
		
	}

	private void apply(ImageColorRGB imgColor) {
		for(int i=0;i<Image.RGB_QTY;i++){
			ImageGray imgGray=imgColor.getBandOnlyGray(i);
			apply(imgGray);			
		}
	}

	private void apply(ImageGray imgGray) {
//		Mask opX=new PrewittOpX(new ScrollableWindowRepeat(imgGray, 3, 3));
//		Mask opY=new PrewittOpY(new ScrollableWindowRepeat(imgGray, 3, 3));

		Mask opX=new SobelOpX(new ScrollableWindowRepeat(imgGray, 3, 3));
		Mask opY=new SobelOpY(new ScrollableWindowRepeat(imgGray, 3, 3));
//		Mask opX=new SobelOpX(new ScrollableWindowFillWithZero(imgGray, 3, 3));
//		Mask opY=new SobelOpY(new ScrollableWindowFillWithZero(imgGray, 3, 3));

		double[][] matrixIx=opX.applyMaskWithoutTransformation();
		double[][] matrixIy=opY.applyMaskWithoutTransformation();
//		double[][] matrixIx=opX.applyMask().getImage();
//		double[][] matrixIy=opY.applyMask().getImage();
		
		double[][] matrixIx2=new double[matrixIx.length][matrixIx[0].length];
		double[][] matrixIy2=new double[matrixIy.length][matrixIy[0].length];
		double[][] matrixIxy=new double[matrixIx.length][matrixIx[0].length];
		
		for(int i=0;i<matrixIx.length;i++){
			for(int j=0;j<matrixIx[0].length;j++){
				double ix=matrixIx[i][j];
				double iy=matrixIy[i][j];
				matrixIx2[i][j]=ix*ix;
				matrixIy2[i][j]=iy*iy;
				matrixIxy[i][j]=ix*iy;
			}
		}
		
		double[][] Ix2gauss=applyGaussianMask(matrixIx2);
		double[][] Iy2gauss=applyGaussianMask(matrixIy2);
		double[][] Ixygauss=applyGaussianMask(matrixIxy);

		double[][] matrixR=new double[Ix2gauss.length][Ix2gauss[0].length];
		for(int i=0;i<Ix2gauss.length;i++){
			for(int j=0;j<Ix2gauss[0].length;j++){
				double ix2=Ix2gauss[i][j];
				double iy2=Iy2gauss[i][j];
				double ixy=Ixygauss[i][j];
				
				matrixR[i][j]=getRValue(ix2,iy2,ixy);
			}
		}

		detectPoints(matrixR);
	}
	
	private void detectPoints(double[][] matrixR) {
		double max=0;
		for(int i=0;i<matrixR.length;i++){
			for(int j=0;j<matrixR[0].length;j++){
				double rValue=matrixR[i][j];
				if(max<rValue)
					max=rValue;
			}
		}
		for(int i=0;i<matrixR.length;i++){
			for(int j=0;j<matrixR[0].length;j++){
				double rValue=matrixR[i][j];
				if(rValue>threshold*max)
					detectedPoints.add(new Point(i,j));
			}
		}		
		
	}
	
	public List<Point> getDetectedPoints(){
		return detectedPoints;
	}

	private double getRValue(double ix2, double iy2, double ixy) {
		double det=ix2*iy2-ixy*ixy;
		double trace=ix2+iy2;
		
		double ans=det-K_CONST*trace*trace;
		return ans;
	}

	private double[][] applyGaussianMask(double[][] matrix){
		GaussianMask gm=new GaussianMask(new ScrollableWindowRepeat(new ImageGray(matrix), GAUSSIAN_WINDOW_SIZE, GAUSSIAN_WINDOW_SIZE), GAUSSIAN_SIGMA);
//		GaussianMask gm=new GaussianMask(new ScrollableWindowFillWithZero(new ImageGray(matrix), GAUSSIAN_WINDOW_SIZE, GAUSSIAN_WINDOW_SIZE), GAUSSIAN_SIGMA);

		double[][] matrixGauss=gm.applyMaskWithoutTransformation();
//		double[][] matrixGauss=gm.applyMask().getImage();
		return matrixGauss;
	}
}
