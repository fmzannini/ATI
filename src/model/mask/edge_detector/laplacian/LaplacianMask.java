package model.mask.edge_detector.laplacian;

import model.image.ImageGray;
import model.mask.Mask;
import model.mask.ScrollableWindowRepeat;

public class LaplacianMask extends Mask{

	private static final double[][] WEIGHTS={
			{0,-1,0},
			{-1,4,-1},
			{0,-1,0}
	};
	private static final int WINDOWS_SIZE=3;
	
	public LaplacianMask(ImageGray img) {
		super(WEIGHTS,new ScrollableWindowRepeat(img, WINDOWS_SIZE, WINDOWS_SIZE));
	}
	
	@Override
	protected ImageGray finalTransformation(ImageGray result) {
		return result;
	}
}
