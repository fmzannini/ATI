package model.mask.edge_detector.directionals;

public class DirectionalMatrixSobel extends ApplyDirectionalOp{

	private static final double[][] OP={
			{1,2,1},
			{0,0,0},
			{-1,-2,-1}
	};
	
	public DirectionalMatrixSobel() {
		super(OP);
	}
}
