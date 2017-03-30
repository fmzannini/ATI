package model.mask;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.file.ImageFileManager;
import model.image.ImageGray;
import model.noise.GaussianNoiseAdditive;
import model.noise.RayleighNoiseMultiplicative;
import model.noise.SaltAndPepperNoise;

public class NoiseAndMaskTest {

	private static final String PATH_TEST = "test/noiseAndMasks";
	
	private double[] sigmaValues=new double[]{
		0.25,0.5,0.75,1,1.5,2,5,10,15,20,25,50	
	};
	private double[] psiValues=new double[]{
			0.25,0.5,0.75,1,1.5,2,5,10,15,20,25,50	
		};
	private double[] densityValues=new double[]{
			0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1
		};
	
	public void testGaussianNoiseAdditiveAndMasks(ImageGray imgGray) throws IOException{
		for(double sigma:sigmaValues){
			GaussianNoiseAdditive gna=new GaussianNoiseAdditive(1, 0, sigma);
			ImageGray imgGaussian=gna.applyNoise(imgGray);
			passMaskAndSave(imgGaussian,"GNA"+String.format("%.2f", (float)sigma));
		}
	}

	public void testRayleighNoiseMultiplicativeAndMasks(ImageGray imgGray) throws IOException{
		for(double psi:psiValues){
			RayleighNoiseMultiplicative rnm=new RayleighNoiseMultiplicative(1, psi);
			ImageGray imgRayleigh=rnm.applyNoise(imgGray);
			passMaskAndSave(imgRayleigh,"RNM"+String.format("%.2f", (float)psi));
		}
	}
	
	public void testSaltAndPepperNoiseAndMask(ImageGray imgGray) throws IOException{
		for(double density:densityValues){
			SaltAndPepperNoise spn=new SaltAndPepperNoise(density, 0.3, 0.7);
			ImageGray imgSaltAndPepper=spn.applyNoise(imgGray);
			passMaskAndSave(imgSaltAndPepper,"SPN"+String.format("%.2f%%", (float)density*100));
		}
	}
	
	private void passMaskAndSave(ImageGray imgGray,String filename) throws IOException{
		TestMask test=new TestMask();
		
		ImageGray mean=test.testMeanMask(imgGray, 3);
		ImageFileManager ifm1=new ImageFileManager(new File(System.getProperty("user.dir")+"/"+PATH_TEST+"/"+filename+"-MaskMean.pgm"));
		ifm1.writeImagePGM(mean.showImage());
		
		ImageGray median=test.testMedianMask(imgGray, 3);
		ImageFileManager ifm2=new ImageFileManager(new File(System.getProperty("user.dir")+"/"+PATH_TEST+"/"+filename+"-MaskMedian.pgm"));
		ifm2.writeImagePGM(median.showImage());

		ImageGray gaussian=test.testGaussianMask(imgGray, 3, 1);
		ImageFileManager ifm3=new ImageFileManager(new File(System.getProperty("user.dir")+"/"+PATH_TEST+"/"+filename+"-MaskGaussian.pgm"));
		ifm3.writeImagePGM(gaussian.showImage());

		ImageGray highPass=test.testHighPassMask(imgGray);
		ImageFileManager ifm4=new ImageFileManager(new File(System.getProperty("user.dir")+"/"+PATH_TEST+"/"+filename+"-MaskHighPass.pgm"));
		ifm4.writeImagePGM(highPass.showImage());

		ImageGray medianWeights=test.testMedianWeightsMask(imgGray);
		ImageFileManager ifm5=new ImageFileManager(new File(System.getProperty("user.dir")+"/"+PATH_TEST+"/"+filename+"-MaskMedianWeights.pgm"));
		ifm5.writeImagePGM(medianWeights.showImage());
	}
	
	public static void main(String[] args) throws IOException {
		ImageFileManager ifm=new ImageFileManager(new File(System.getProperty("user.dir")+"/resources/imgTest/bell.pgm"));
		BufferedImage img=ifm.readImagePGM();
		ImageGray imgGray=new ImageGray(img,false);

		File file=new File(System.getProperty("user.dir")+"/"+PATH_TEST);
		file.mkdirs();
		
		NoiseAndMaskTest test=new NoiseAndMaskTest();
		
		test.testGaussianNoiseAdditiveAndMasks(imgGray);
		test.testRayleighNoiseMultiplicativeAndMasks(imgGray);
		test.testSaltAndPepperNoiseAndMask(imgGray);
		
	}
}
