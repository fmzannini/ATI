package model.mask.edge_detector.gradient;

import model.image.ImageGray;
import model.mask.Mask;
import model.mask.ScrollableWindow;

public class GradientOp extends Mask{

	private double[][] op_x;
	private double[][] op_y;
	
	public GradientOp(double[][] op_x, double[][] op_y, ScrollableWindow scroll) {
		super(null, scroll);
		this.op_x=op_x;
		this.op_y=op_y;
	}
	
	@Override
	protected double applyMask(double[][] region) {
		double valueX=applyMask(region, op_x);
		double valueY=applyMask(region, op_y);
		
		double gradientMagnitude=Math.hypot(valueX, valueY);
		return gradientMagnitude;
	}
	
	@Override
	protected ImageGray finalTransformation(ImageGray result) {
		//Truncando los valores a 255
		/*
		for(int i=0;i<result.getWidth();i++){
			for(int j=0;j<result.getHeight();j++){
				double val=result.getPixel(i, j);
				if(val>255)
					result.setPixel(i, j, 255);
			}
		}*/
		
		//Ajustando con transformacion lineal
		result=super.finalTransformation(result);
		return result;
	}
}
