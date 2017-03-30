package model.mask;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.file.ImageFileManager;
import model.image.ImageGray;
import utils.LinearTransformation;

public class TestMask {

	public TestMask() {
	}
	
	public ImageGray testMeanMask(ImageGray img, int n){
		MeanMask mm=new MeanMask(new ScrollableWindowRepeat(img, n, n));
		ImageGray result=mm.applyMask();

		return new ImageGray(LinearTransformation.grayImage(result),false);
	}
	
	public ImageGray testMedianMask(ImageGray img, int n){
		MedianMask mm=new MedianMask(new ScrollableWindowRepeat(img, n, n));
		ImageGray result=mm.applyMask();

		return new ImageGray(LinearTransformation.grayImage(result),false);
	}
	public ImageGray testMedianWeightsMask(ImageGray img){
		MedianWeightsMask mm=new MedianWeightsMask(new ScrollableWindowRepeat(img, 3, 3));
		ImageGray result=mm.applyMask();
	
		return new ImageGray(LinearTransformation.grayImage(result),false);
		
	}
	public ImageGray testGaussianMask(ImageGray img, int n,double sigma){
		GaussianMask gm=new GaussianMask(new ScrollableWindowRepeat(img, n,n),sigma);
		ImageGray result=gm.applyMask();
	
		return new ImageGray(LinearTransformation.grayImage(result),false);
	}
	
	public ImageGray testHighPassMask(ImageGray img){
		HighPassMask hpm=new HighPassMask(new ScrollableWindowRepeat(img, 3,3));
		ImageGray result=hpm.applyMask();
		
		return new ImageGray(LinearTransformation.grayImage(result),false);
	}
	
	public static void main(String[] args) throws IOException {
		ImageFileManager ifm=new ImageFileManager(new File(System.getProperty("user.dir")+"/resources/imgTest/bell.pgm"));
		BufferedImage img=ifm.readImagePGM();
		ImageGray imgGray=new ImageGray(img,false);
		
		TestMask test=new TestMask();
		
		ImageGray mean=test.testMeanMask(imgGray, 3);
		ImageFileManager ifm1=new ImageFileManager(new File(System.getProperty("user.dir")+"/testMean.pgm"));
		ifm1.writeImagePGM(mean.showImage());
		
		ImageGray median=test.testMedianMask(imgGray, 3);
		ImageFileManager ifm2=new ImageFileManager(new File(System.getProperty("user.dir")+"/testMedian.pgm"));
		ifm2.writeImagePGM(median.showImage());

		ImageGray gaussian=test.testGaussianMask(imgGray, 3, 1);
		ImageFileManager ifm3=new ImageFileManager(new File(System.getProperty("user.dir")+"/testGaussian.pgm"));
		ifm3.writeImagePGM(gaussian.showImage());

		ImageGray highPass=test.testHighPassMask(imgGray);
		ImageFileManager ifm4=new ImageFileManager(new File(System.getProperty("user.dir")+"/testHighPass.pgm"));
		ifm4.writeImagePGM(highPass.showImage());

		ImageGray medianWeights=test.testMedianWeightsMask(imgGray);
		ImageFileManager ifm5=new ImageFileManager(new File(System.getProperty("user.dir")+"/testMedianWeights.pgm"));
		ifm5.writeImagePGM(medianWeights.showImage());

	}

}
