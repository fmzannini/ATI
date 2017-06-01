package model.levelset;

import java.awt.Point;

public class GaussianMaskForLevelSet{

	public static final int SIZE_WINDOW=5;

	private static final double SIGMA=1;
	private static final ScrollableWindowRepeatForLevelSet SCROLL=new ScrollableWindowRepeatForLevelSet(SIZE_WINDOW, SIZE_WINDOW);;
	private static final double[][] WEIGHTS=generateWeights(SCROLL, SIGMA);
	
	private int[][] matrix;
	
	public GaussianMaskForLevelSet(int[][] matrix) {
		this.matrix=matrix;
	}
	
	
	private static double[][] generateWeights(ScrollableWindowRepeatForLevelSet scroll, double sigma) {		
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
	
	

	public double applyMask(Point center){
		double[][] region=SCROLL.nextRegion(matrix, center);
		
		double value = 0.0;
		for (int i = 0; i < region.length; i++) {
			for (int j = 0; j < region[0].length; j++) {
				value += region[i][j] * WEIGHTS[i][j];
			}
		}
		return value;		
	}


}
