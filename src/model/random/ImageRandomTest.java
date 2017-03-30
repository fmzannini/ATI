package model.random;

import java.io.File;
import java.io.IOException;

import model.file.ImageFileManager;
import model.image.ImageGray;
import plot.Histogram;
import utils.LinearTransformation;

public class ImageRandomTest {

	private static final int WIDTH=100;
	private static final int HEIGHT=100;
	
	public void test(){
		double psi=10;
		test(new RayleighRandom(psi),System.getProperty("user.dir")+"/testRayleigh");
		
		double mu=0;
		double sigma=1;
		test(new NormalRandom(mu,sigma),System.getProperty("user.dir")+"/testNormal");

		double lambda=10;
		test(new ExponentialRandom(lambda),System.getProperty("user.dir")+"/testExponential");

	}
	private static void test(RandomGenerator random, String filename){
		ImageGray img=new ImageGray(WIDTH,HEIGHT);
		for(int i=0;i<WIDTH;i++){
			for(int j=0;j<HEIGHT;j++){
				img.setPixel(i, j, random.rand());
			}
		}
		img = new ImageGray(LinearTransformation.grayImage(img).getImage());
		
		ImageFileManager ifm=new ImageFileManager(new File(filename+".pgm"));
		try {
			ifm.writeImagePGM(img.showImage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Histogram histogram=new Histogram();
		histogram.grayScalePlot(img, filename+"-histogram");
	}
	
	public static void main(String[] args) {
		ImageRandomTest irt=new ImageRandomTest();
		irt.test();
	}

}
