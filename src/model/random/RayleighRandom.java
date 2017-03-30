package model.random;

public class RayleighRandom implements RandomGenerator {
	private UniformRandom gen;
	private double psi;

	public RayleighRandom(double psi) {
		this.gen = new UniformRandom();
		this.psi = psi;
	}

	public double rand() {
		double x = gen.rand();
		double y = psi * Math.sqrt(-2 * Math.log(1 - x));
		return y;
	}
}
