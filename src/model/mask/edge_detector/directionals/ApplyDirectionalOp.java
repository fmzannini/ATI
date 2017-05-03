package model.mask.edge_detector.directionals;

import model.image.ImageGray;
import model.mask.ScrollableWindowRepeat;
import model.mask.edge_detector.ApplyOperator;

public class ApplyDirectionalOp extends ApplyOperator {

	private double[][] op;
	
	public ApplyDirectionalOp(double[][] op) {
		this.op=op;
	}
	
	@Override
	protected ImageGray apply(ImageGray img){
		DirectionalOp directionalMask=new DirectionalOp(op, new ScrollableWindowRepeat(img, op.length,op[0].length));
		return directionalMask.applyMask();
	}

}
