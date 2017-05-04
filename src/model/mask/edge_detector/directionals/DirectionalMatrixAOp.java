package model.mask.edge_detector.directionals;

public class DirectionalMatrixAOp extends ApplyDirectionalOp{
	private static double[][] OP={
			{1,1,1},
			{1,-2,1},
			{-1,-1,-1}
	};
	
	public DirectionalMatrixAOp() {
		super(OP);
	}
	
}
