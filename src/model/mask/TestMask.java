package model.mask;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.file.ImageFileManager;
import model.image.ImageGray;

public class TestMask {

	public TestMask() {
	}
	
	public ImageGray testMeanMask(ImageGray img, int n){
		MeanMask mm=new MeanMask(new ScrollableWindowRepeat(img, n, n));
		ImageGray result=mm.applyMask();
		return result;
	}
	
	public ImageGray testMedianMask(ImageGray img, int n){
		MedianMask mm=new MedianMask(new ScrollableWindowRepeat(img, n, n));
		ImageGray result=mm.applyMask();
		return result;
	}
	public ImageGray testMedianWeightsMask(ImageGray img){
		MedianWeightsMask mm=new MedianWeightsMask(new ScrollableWindowRepeat(img, 3, 3));
		ImageGray result=mm.applyMask();
		return result;
	}
	public ImageGray testGaussianMask(ImageGray img,int n,double sigma){
		GaussianMask gm=new GaussianMask(new ScrollableWindowRepeat(img, n,n),sigma);
		ImageGray result=gm.applyMask();
		return result;
	}
	public ImageGray testHighPassMask(ImageGray img){
		HighPassMask gm=new HighPassMask(new ScrollableWindowRepeat(img, 3,3));
		ImageGray result=gm.applyMask();
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		ImageFileManager ifm=new ImageFileManager(new File(System.getProperty("user.dir")+"/resources/imgTest/bell.pgm"));
		BufferedImage img=ifm.readImagePGM();
		ImageGray imgGray=new ImageGray(img,false);
		
		TestMask test=new TestMask();
		
		ImageGray mean=test.testMeanMask(imgGray, 3);
		ImageFileManager ifm1=new ImageFileManager(new File(System.getProperty("user.dir")+"/testMean.pgm"));
		ifm1.writeImagePGM(mean.showImage());
	}

}
