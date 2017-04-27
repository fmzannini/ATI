package model.noise;

import model.random.NormalRandom;

public class GaussianNoiseAdditive extends RandomNoise{

	public GaussianNoiseAdditive(double density, double mu, double sigma){
		super(density,new NormalRandom(mu,sigma));
	}

	@Override
	protected double applyNoise(double oldPixel, double noise) {
		return oldPixel+noise;
	}

	

	
}
