package model.levelset;

import java.awt.Point;

public class FirstCycleLevelSet implements CycleLevelSet {

	private Condition CONDITION_FOR_UPDATE_PIXELS_OUT=new Condition() {
		
		@Override
		public boolean evaluateCondition(double value) {
			return value>0;
		}
	};

	private Condition CONDITION_FOR_UPDATE_PIXELS_IN=new Condition() {
		
		@Override
		public boolean evaluateCondition(double value) {
			return value<0;
		}
	};

	private AlgorithmLevelSet algorithmLevelSet;
	public FirstCycleLevelSet(AlgorithmLevelSet algorithmLevelSet) {
		this.algorithmLevelSet=algorithmLevelSet;
	}

	@Override
	public double calculateValue(Point p) {
		return algorithmLevelSet.getLevelSetImage().calculateFd(p);
	}

	@Override
	public Condition getConditionForUpdatePixelsIn() {
		return CONDITION_FOR_UPDATE_PIXELS_IN;
	}

	@Override
	public Condition getConditionForUpdatePixelsOut() {
		return CONDITION_FOR_UPDATE_PIXELS_OUT;
	}
}
