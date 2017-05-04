package model.mask.edge_detector.directionals;

public class DirectionalMatrixKirsh extends ApplyDirectionalOp{

	private static double[][] OP={
			{5,5,5},
			{-3,0,-3},
			{-3,-3,-3}
	};
	
	public DirectionalMatrixKirsh() {
		super(OP);
	}
}
