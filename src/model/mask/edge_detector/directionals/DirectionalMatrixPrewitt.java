package model.mask.edge_detector.directionals;

public class DirectionalMatrixPrewitt extends ApplyDirectionalOp{

	private static final double[][] OP={
			{1,1,1},
			{0,0,0},
			{-1,-1,-1}
	};
	
	public DirectionalMatrixPrewitt() {
		super(OP);
	}
}
