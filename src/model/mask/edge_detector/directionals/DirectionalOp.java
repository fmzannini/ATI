package model.mask.edge_detector.directionals;

import model.mask.Mask;
import model.mask.ScrollableWindow;

public class DirectionalOp extends Mask {
	
	private double[][][] dirs;
	

	public DirectionalOp(double[][] weights, ScrollableWindow scroll) {
		super(null, scroll);
		generateDirections(weights);
	}

	private void generateDirections(double[][] weights) {
		dirs=new double[4][][];
		dirs[0]=weights;
		dirs[1]=rotateMatrix3x3(dirs[0]);
		dirs[2]=rotateMatrix3x3(dirs[1]);
		dirs[3]=rotateMatrix3x3(dirs[2]);

	}

	private double[][] rotateMatrix3x3(double[][] matrix) {
		double[][] ans=new double[3][3];
		ans[1][1]=matrix[1][1];
		ans[0][1]=matrix[0][2];
		ans[0][2]=matrix[1][2];
		ans[1][2]=matrix[2][2];
		ans[2][2]=matrix[2][1];
		ans[2][1]=matrix[2][0];
		ans[2][0]=matrix[1][0];
		ans[1][0]=matrix[0][0];
		ans[0][0]=matrix[0][1];

		return ans;
	}

	
	@Override
	protected double applyMask(double[][] region) {
		double[] values=new double[dirs.length];
		for(int i=0;i<dirs.length;i++){
			values[i]=applyMask(region, dirs[i]);
		}
		double pixelValue=max(values);
		return pixelValue;
	}

	private double max(double[] values) {
		double max=values[0];
		for(int i=0;i<values.length;i++)
			if(max<Math.abs(values[i]))
				max=Math.abs(values[i]);
		return max;
	}
	
}
