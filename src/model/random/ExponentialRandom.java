package model.random;

public class ExponentialRandom implements RandomGenerator{
	private UniformRandom gen;
	private double lambda;
	
	public ExponentialRandom(double lambda){
		this.gen=new UniformRandom();
		this.lambda=lambda;
	}
	
	public double rand(){
		double x=gen.rand();
		double y=-1/lambda*Math.log(1-x);
		return y;
	}
}
