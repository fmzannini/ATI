package model.mask.edge_detector.gradient;

import model.mask.Mask;
import model.mask.ScrollableWindow;

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
