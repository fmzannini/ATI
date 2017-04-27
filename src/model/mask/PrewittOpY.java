package model.mask;

public class PrewittOpY extends Mask {

	private static double[][] WEIGHTS={
			{-1,-1,-1},
			{0,0,0},
			{1,1,1}
	};
	
	public PrewittOpY(ScrollableWindow scroll) {
		super(WEIGHTS, scroll);
	}

}
