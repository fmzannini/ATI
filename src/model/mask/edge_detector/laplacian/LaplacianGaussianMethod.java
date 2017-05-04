package model.mask.edge_detector.laplacian;

import model.image.ImageGray;
import model.mask.ScrollableWindowRepeat;

public class LaplacianGaussianMethod extends LaplacianMethod{

	private int windowSize;
	private double sigma;
	
	public LaplacianGaussianMethod(int windowize, double sigma) {
		super();
		this.windowSize=windowize;
		this.sigma=sigma;
	}
	public LaplacianGaussianMethod(int windowize, double sigma, double thresholdValue) {
		super(thresholdValue);
		this.windowSize=windowize;
		this.sigma=sigma;
	}
	public LaplacianGaussianMethod(int windowize, double sigma, ThresholdType thresholdType) {
		super(thresholdType);
		this.windowSize=windowize;
		this.sigma=sigma;
	}

	@Override
	protected ImageGray applyLaplacianMask(ImageGray img) {
		LaplacianGaussianMask maskLoG=new LaplacianGaussianMask(new ScrollableWindowRepeat(img, windowSize, windowSize), sigma);
		return maskLoG.applyMask();
	}
}
