package model.noise;

import model.random.ExponentialRandom;

public class ExponentialNoiseMultiplicative extends RandomNoise {
	
	public ExponentialNoiseMultiplicative(double density,double lambda) {
		super(density,new ExponentialRandom(lambda));

	
	}
	@Override
	protected double applyNoise(double oldPixel, double noise) {
		return oldPixel*noise;
	}


}
