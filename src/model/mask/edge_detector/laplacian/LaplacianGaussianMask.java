package model.mask.edge_detector.laplacian;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import model.image.ImageGray;
import model.mask.Mask;
import model.mask.ScrollableWindow;

public class LaplacianGaussianMask extends Mask{

	private static double[][] generateWeights(ScrollableWindow scroll, double sigma) {
		int widthWindow=scroll.getWidthWindow();
		int heightWindow=scroll.getHeightWindow();
		
		double[][] weights=new double[widthWindow][heightWindow];
		
		Point[] displacements=scroll.getDisplacements();
		Point middle=scroll.getMiddlePoint();
		
		Map<Point,Double> cache=new HashMap<>();
		
		for(Point displacement:displacements){
			int x=middle.x+displacement.x;
			int y=middle.y+displacement.y;
		
			Point absolutePoint=new Point(Math.abs(displacement.x),Math.abs(displacement.y));
			if(cache.containsKey(absolutePoint)){
				weights[x][y]=cache.get(absolutePoint);
			}else{
				weights[x][y]=getLaplacianGaussian(displacement.x,displacement.y,sigma);
				cache.put(absolutePoint, weights[x][y]);
			}
		}
		
		return weights;
	}

	private static double getLaplacianGaussian(int x, int y, double sigma) {
		double sqrX=x*x;
		double sqrY=y*y;
		double sqrSigma=sigma*sigma;
		double cubeSigma=sqrSigma*sigma;
		
		double constant=-1/(Math.sqrt(2*Math.PI)*cubeSigma);
		double aux=2-((sqrX+sqrY)/sqrSigma);
		double exp=Math.exp(-(sqrX+sqrY)/(2*sqrSigma));
		
		double ans=constant*aux*exp;
		return ans;
	}
	
	public LaplacianGaussianMask(ScrollableWindow scroll, double sigma) {
		super(generateWeights(scroll,sigma),scroll);
	}
	
	@Override
	protected ImageGray finalTransformation(ImageGray result) {
		return result;
	}
}
