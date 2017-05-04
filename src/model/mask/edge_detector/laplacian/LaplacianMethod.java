package model.mask.edge_detector.laplacian;

import model.image.ImageGray;
import model.mask.edge_detector.ApplyOperator;

public class LaplacianMethod extends ApplyOperator{

	private static final double THRESHOLD_CONST = 0.8;
	private boolean useSlopeEvaluation;
	
	public enum ThresholdType{
		MAX,MEAN,MEDIAN,CUSTOM
	}
	private ThresholdType thresholdType;
	private double thresholdValue;

	public LaplacianMethod() {
		this.useSlopeEvaluation=false;
	}

	public LaplacianMethod(double thresholdValue) {
		this.useSlopeEvaluation=true;
		this.thresholdType=ThresholdType.CUSTOM;
		this.thresholdValue=thresholdValue;
	}

	public LaplacianMethod(ThresholdType thresholdType) {
		this.useSlopeEvaluation=true;
		this.thresholdType=thresholdType;
	}

	@Override
	protected ImageGray apply(ImageGray img) {
		ImageGray result=applyLaplacianMask(img);
		
		ImageGray ans;
		ans=zeroCrossing(result);			

		if(useSlopeEvaluation){
			ans=applyThreshold(ans);
		}
		return ans;
	}

	protected ImageGray applyLaplacianMask(ImageGray img){
		LaplacianMask mask=new LaplacianMask(img);
		return mask.applyMask();
	}
	private ImageGray applyThreshold(ImageGray ans) {
		
		double threshold=0;
		switch(thresholdType){
		case MAX:
			double max=ans.getMaxValue();
			threshold=THRESHOLD_CONST*max;
			break;
		case MEAN:
			double mean=ans.getMeanValuePixels();
			threshold=mean;
			break;
		case MEDIAN:
			double median=ans.getMedianValue();
			threshold=median;
			break;
		case CUSTOM:
			threshold=thresholdValue;
			break;
		}
		return ans.applyThresholding(threshold);
	}

	private ImageGray zeroCrossing(ImageGray result) {
		ZeroCrossing zeroCrossing=new ZeroCrossing(useSlopeEvaluation);
		return zeroCrossing.zeroCrossing(result);
	}
	
	
}
