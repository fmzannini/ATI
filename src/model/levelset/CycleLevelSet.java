package model.levelset;

import java.awt.Point;

public interface CycleLevelSet {

	public double calculateValue(Point p);

	public Condition getConditionForUpdatePixelsIn();

	public Condition getConditionForUpdatePixelsOut();
}
