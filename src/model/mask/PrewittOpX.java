package model.mask;

public class PrewittOpX extends Mask {

	private static double[][] WEIGHTS={
			{-1,0,1},
			{-1,0,1},
			{-1,0,1}
	};
	
	public PrewittOpX(ScrollableWindow scroll) {
		super(WEIGHTS, scroll);
	}

}
