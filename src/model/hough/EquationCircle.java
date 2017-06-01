package model.hough;

import java.awt.Point;

public class EquationCircle implements Equation2D {

	private static final double EPSILON = 1;

	
	private static final int PARAM_X_CENTER = 0;
	private static final int PARAM_Y_CENTER = 1;
	private static final int PARAM_RADIUS = 2;

	private static final int PARAMS_LENGTH = 3;
		

	@Override
	public boolean isSolve(double[] paramsValue, Point point) {
		double xCenter=paramsValue[PARAM_X_CENTER];
		double yCenter=paramsValue[PARAM_Y_CENTER];
		double radius=paramsValue[PARAM_RADIUS];
		
		double aux=Math.abs(Math.pow(radius,2)-Math.pow(point.x-xCenter, 2)-Math.pow(point.y-yCenter, 2));
		
		return aux<EPSILON;
	}

	public Param[] getParamArray(Param xCenterParam, Param yCenterParam, Param radiusParam){
		Param[] array=new Param[PARAMS_LENGTH];
		array[PARAM_X_CENTER]=xCenterParam;
		array[PARAM_Y_CENTER]=yCenterParam;
		array[PARAM_RADIUS]=radiusParam;
		
		return array;
	}

	public double getXCenter(double[] params) {
		return params[PARAM_X_CENTER];
	}

	public double getYCenter(double[] params) {
		return params[PARAM_Y_CENTER];
	}

	public double getRadius(double[] params) {
		return params[PARAM_RADIUS];
	}
}
