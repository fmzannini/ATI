package model.hough;

import java.awt.Point;
import java.util.List;

public interface Equation2D {
	
	public boolean isSolve(double[] paramsValue, Point point);
}
