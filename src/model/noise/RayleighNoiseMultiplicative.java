package model.noise;

import model.random.RayleighRandom;

public class RayleighNoiseMultiplicative extends RandomNoise{

	public RayleighNoiseMultiplicative(double density, double psi) {
		super(density, new RayleighRandom(psi));
	}

	@Override
	protected double applyNoise(double oldPixel, double noise) {
		return oldPixel*noise;
	}


}
