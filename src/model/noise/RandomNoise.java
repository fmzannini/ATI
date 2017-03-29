package model.noise;

import model.image.ImageGray;
import model.random.RandomGenerator;
import utils.LinearTransformation;

public abstract class RandomNoise extends Noise {
	private RandomGenerator random;
	public RandomNoise(double density, RandomGenerator random){
		super(density);
		this.random=random;
	}
	

	@Override
	protected double applyNoise(double pixel) {
		
		return applyNoise(pixel,random.rand());
	}
	
	@Override
	protected ImageGray finalCorrection(ImageGray imgNoise) {
		return new ImageGray(LinearTransformation.grayImage(imgNoise),false);
	}
	
	protected abstract double applyNoise(double oldPixel, double noise);

}
