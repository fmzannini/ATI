package model.random;

import java.util.Random;

public class UniformRandom implements RandomGenerator {
	private Random gen;

	public UniformRandom() {
		this.gen = new Random();
	}

	public double rand() {
		return gen.nextDouble();
	}
}
