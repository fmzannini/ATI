package model.mask;

public class HighPassMask extends Mask {
	
	private static double[][] WEIGHTS_3 = new double[][]{
		{-1,-1,-1},
		{-1, 8,-1},
		{-1,-1,-1}
	};
	
	private static double[][] WEIGHTS_5 = new double[][]{
		{-1,-1,-1,-1,-1},
		{-1, 0, 1, 0,-1},
		{-1, 1,12, 1,-1},
		{-1, 0, 1, 0,-1},
		{-1,-1,-1,-1,-1},
	};
	
	
	public HighPassMask(ScrollableWindow scroll){
		super(getWeights(3), scroll);
	}
	public HighPassMask(int n, ScrollableWindow scroll) {
		super(getWeights(n), scroll);
	}

	private static double[][] getWeights(int n){
		if(n==3){
			return WEIGHTS_3;
		}else if(n==5){
			return WEIGHTS_5;
		}
		return null;
	}
}
