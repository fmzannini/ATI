package model.noise;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.file.ImageFileManager;
import model.image.ImageGray;

public class ImageNoiseTest {

	public void test(){
		ExponentialNoiseMultiplicative enm=new ExponentialNoiseMultiplicative(0.5, 20);
		test(enm,"exponentialNoise");
		
		GaussianNoiseAdditive gna=new GaussianNoiseAdditive(0.5, 0, 500);
		test(gna,"gaussianNoise");
		
		RayleighNoiseMultiplicative rnm=new RayleighNoiseMultiplicative(0.5, 5);
		test(rnm,"rayleighNoise");
		
		SaltAndPepperNoise spn=new SaltAndPepperNoise(0.5, 0.3, 0.7);
		test(spn,"saltAndPepperNoise");		
	}
	private void test(Noise rn, String filenameOutput){
		try {
			ImageFileManager ifm=new ImageFileManager(new File(System.getProperty("user.dir")+"/resources/imgTest/bell.pgm"));
			BufferedImage img=ifm.readImagePGM();
			ImageGray enImg=rn.applyNoise(new ImageGray(img,false));
			ImageFileManager ifm2=new ImageFileManager(new File(System.getProperty("user.dir")+"/"+filenameOutput+".pgm"));
			ifm2.writeImagePGM(enImg.showImage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		ImageNoiseTest imgNoiseTest=new ImageNoiseTest();
		imgNoiseTest.test();
	}
}
