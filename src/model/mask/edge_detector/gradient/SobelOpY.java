package model.mask.edge_detector.gradient;

import model.mask.Mask;
import model.mask.ScrollableWindow;

public class SobelOpY extends Mask {

	private static double[][] OP_Y={
			{-1,0,1},
			{-2,0,2},
			{-1,0,1}
	};

	private static final int WINDOW_SIZE=3;
	
	
	
	public SobelOpY(ScrollableWindow scroll) {
		super(OP_Y, scroll);
	}
	
}

