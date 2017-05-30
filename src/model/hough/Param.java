package model.hough;

public class Param {

	private double minValue;
	private double maxValue;
	private double step;
	
	private double[] values;

	public Param(double minValue, double maxValue, double step) {
		super();
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.step = step;
		
		values=generateValues();
	}

	private double[] generateValues() {
		int length=(int) ((maxValue-minValue)/step)+1;
		double[] values=new double[length];
		for(int i=0;i<length;i++){
			values[i]=minValue+i*step;
		}
		return values;
	}
	
	public double getValue(int i){
		return values[i];
	}
	public int getLength(){
		return values.length;
	}
	
}
