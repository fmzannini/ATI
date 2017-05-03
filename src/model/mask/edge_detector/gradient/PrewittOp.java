package model.mask.edge_detector.gradient;

import model.image.ImageGray;
import model.mask.ScrollableWindowRepeat;
import model.mask.edge_detector.ApplyOperator;

public class PrewittOp extends ApplyOperator{

	private static double[][] OP_X={
			{-1,0,1},
			{-1,0,1},
			{-1,0,1}
	};
	private static double[][] OP_Y={
			{-1,-1,-1},
			{0,0,0},
			{1,1,1}
	};

	private static final int WINDOW_SIZE=3;
	
	
	
	public PrewittOp(){
	}
	
	@Override
	protected ImageGray apply(ImageGray img){
		GradientOp gradientMask=new GradientOp(OP_X, OP_Y, new ScrollableWindowRepeat(img, WINDOW_SIZE, WINDOW_SIZE));
		return gradientMask.applyMask();
	}
	
}
