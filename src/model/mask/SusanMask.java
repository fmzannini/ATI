package model.mask;

public class SusanMask extends Mask {

	private static double[][] WEIGHTS_7 = new double[][]{
		{0,0,1,1,1,0,0},
		{0,1,1,1,1,1,0},
		{1,1,1,1,1,1,1},
		{1,1,1,1,1,1,1},
		{1,1,1,1,1,1,1},
		{0,1,1,1,1,1,0},
		{0,0,1,1,1,0,0}
	};
	
	public SusanMask(ScrollableWindow scroll) {
		super(getWeights(7), scroll);
		// TODO Auto-generated constructor stub
	}
	
	private static double[][] getWeights(int n) {
		if (n == 7) {
			return WEIGHTS_7;
		} else {
			return null;
		}
	}
}
