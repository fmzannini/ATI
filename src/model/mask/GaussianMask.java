package model.mask;

import java.awt.Point;

public class GaussianMask extends Mask {

	public GaussianMask(ScrollableWindow scroll, double sigma) {
		super(generateWeights(scroll,sigma), scroll);
	}

	private static double[][] generateWeights(ScrollableWindow scroll, double sigma) {
		int widthWindow=scroll.getWidthWindow();
		int heightWindow=scroll.getHeightWindow();
		
		double[][] weights=new double[widthWindow][heightWindow];
		
		Point[] displacements=scroll.getDisplacements();
		Point middle=scroll.getMiddlePoint();
		for(Point displacement:displacements){
			int x=middle.x+displacement.x;
			int y=middle.y+displacement.y;
			
			weights[x][y]=getGaussian(displacement.x,displacement.y,sigma);
		}
		
		return weights;
	}

	private static double getGaussian(int x, int y, double sigma) {
		return 1/(2.0*Math.PI*Math.pow(sigma, 2))*Math.exp(-(Math.pow(x, 2)+Math.pow(y, 2)/Math.pow(sigma, 2)));
	}

}
