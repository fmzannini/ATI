package model.hough;

import java.awt.Point;

public class EquationRect implements Equation2D {

	private static final double EPSILON = 1;

	
	private static final int PARAM_R = 0;
	private static final int PARAM_ANGLE = 1;


	private static final int PARAMS_LENGTH = 2;


	public Double getX(double[] paramsValue, double y){
		double r=paramsValue[PARAM_R];
		double angle=Math.toRadians(paramsValue[PARAM_ANGLE]);
		
		double cos=Math.cos(angle);
		double sin=Math.sin(angle);
		
		if(cos==0)
			return null;
		else if(sin==0)
			return r;
		else{
			double aux=(r-y*sin)/cos;
			return aux;
		}
	}
	public Double getY(double[] paramsValue, double x){
		double r=paramsValue[PARAM_R];
		double angle=Math.toRadians(paramsValue[PARAM_ANGLE]);
		
		double cos=Math.cos(angle);
		double sin=Math.sin(angle);
		
		if(cos==0)
			return r;
		else if(sin==0)
			return null;
		else{
			double aux=(r-x*cos)/sin;
			return aux;
		}
	}
	@Override
	public boolean isSolve(double[] paramsValue, Point point) {
		double x=point.x;
		double y=point.y;
		
		double r=paramsValue[PARAM_R];
		double angle=Math.toRadians(paramsValue[PARAM_ANGLE]);
		
		double aux=Math.abs(r-x*Math.cos(angle)-y*Math.sin(angle));
		
		return aux<EPSILON;
	}
	
	public Param[] getParamArray(Param rParam,Param angleParam){
		Param[] array=new Param[PARAMS_LENGTH];
		array[PARAM_R]=rParam;
		array[PARAM_ANGLE]=angleParam;
		
		return array;
	}

}
