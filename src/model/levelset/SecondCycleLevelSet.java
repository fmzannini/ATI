package model.levelset;

import java.awt.Point;

public class SecondCycleLevelSet implements CycleLevelSet {

	private static final Condition CONDITION_FOR_UPDATE_PIXELS_OUT=new Condition() {
		
		@Override
		public boolean evaluateCondition(double value) {
			return value<0;
		}
	};

	private static final Condition CONDITION_FOR_UPDATE_PIXELS_IN=new Condition() {
		
		@Override
		public boolean evaluateCondition(double value) {
			return value>0;
		}
	};

	
	private GaussianMaskForLevelSet gaussianMask;	
	private AlgorithmLevelSet algorithmLevelSet;
	
	public SecondCycleLevelSet(AlgorithmLevelSet algorithmLevelSet) {
		this.algorithmLevelSet=algorithmLevelSet;
		this.gaussianMask=new GaussianMaskForLevelSet(matrixCopy(this.algorithmLevelSet.getMatrix()));		
	}

	private int[][] matrixCopy(int[][] matrix){
		int[][] newMatrix=new int[matrix.length][];
		for(int i=0;i<matrix.length;i++){
			newMatrix[i]=new int[matrix[i].length];
			System.arraycopy(matrix[i], 0, newMatrix[i], 0, newMatrix[i].length);
		}
		return newMatrix;
	}
	
	@Override
	public double calculateValue(Point p) {
		return gaussianMask.applyMask(p);
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
