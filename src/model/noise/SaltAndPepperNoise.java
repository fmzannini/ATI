package model.noise;

import model.image.ImageGray;
import model.random.UniformRandom;

public class SaltAndPepperNoise extends RandomNoise{

	private double p0;
	private double p1;
	
	public SaltAndPepperNoise(double density,double p0,double p1) {
		super(density,new UniformRandom());
		this.p0=p0;
		this.p1=p1;
	}

	@Override
	protected double applyNoise(double oldPixel, double x) {
		if(x<=p0)
			return 0;
		else if(x>=p1)
			return 255;
		else
			return oldPixel;
	}

	@Override
	protected ImageGray finalCorrection(ImageGray imgNoise) {
		return imgNoise;
	}



}
