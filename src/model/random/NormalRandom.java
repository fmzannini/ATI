package model.random;

public class NormalRandom implements RandomGenerator {
	private UniformRandom gen1;
	private UniformRandom gen2;

	private double mu;
	private double sigma;

	public NormalRandom(double mu, double sigma) {
		this.gen1 = new UniformRandom();
		this.gen2 = new UniformRandom();

		this.mu = mu;
		this.sigma = sigma;
	}

	public double rand() {
		double x1 = gen1.rand();
		double x2 = gen2.rand();
		double y = Math.sqrt(-2 * Math.log(x1)) * Math.cos(2 * Math.PI * x2);

		return y * Math.sqrt(sigma) + mu;
	}
}
