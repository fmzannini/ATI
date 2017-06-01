package model.mask.edge_detector.gradient;

import model.mask.Mask;
import model.mask.ScrollableWindow;

public class SobelOpX extends Mask {

	private static double[][] OP_X={
			{-1,0,1},
			{-2,0,2},
			{-1,0,1}
	};
	
	
	public SobelOpX(ScrollableWindow scroll) {
		super(OP_X, scroll);
	}
	
}
