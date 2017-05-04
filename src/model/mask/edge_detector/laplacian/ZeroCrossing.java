package model.mask.edge_detector.laplacian;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import model.image.ImageGray;

public class ZeroCrossing {


	private static final double MAX_PIXEL_VALUE = 255;

	private enum State{
		INITIAL, WITHOUT_ZERO, IN_ZERO
	}

	private boolean useSlopeEvaluation;
	private State state;
	
	public ZeroCrossing(boolean useSlopeEvaluation) {
		this.useSlopeEvaluation=useSlopeEvaluation;
	}
	
	
	public ImageGray zeroCrossing(ImageGray result){
		ImageGray ans=new ImageGray(result.getWidth(), result.getHeight());

		double[] line;
		Map<Integer,Double> values;
		for(int x=0;x<result.getWidth();x++){
			line=getLineY(result,x);
			values=zeroCrossingLine(line);
			updateImageLineY(ans,x,values);
		}
		for(int y=0;y<result.getHeight();y++){
			line=getLineX(result,y);
			values=zeroCrossingLine(line);
			updateImageX(ans,y,values);
		}

		return ans;
	}


	private Map<Integer, Double> zeroCrossingLine(double[] line) {
		Map<Integer,Double> map=new HashMap<>();
		state=State.INITIAL;
		double lastValue=0;
		int lastSign=0;
		int lastIndex=0;
		for(int i=0;i<line.length;i++){
			double currentValue=line[i];
			int signCurrent=(int) Math.signum(currentValue);
			switch(state){
			case INITIAL:
				if(signCurrent!=0){
					lastIndex=i;
					lastValue=currentValue;
					lastSign=signCurrent;
					state=State.WITHOUT_ZERO;
				}
				break;
			case WITHOUT_ZERO:
				if(signCurrent==0){
					state=State.IN_ZERO;
				}else{
					if(lastSign!=signCurrent){
						markCross(map,lastIndex,lastValue,currentValue);
					}
				}
					
				lastIndex=i;
				lastValue=currentValue;
				lastSign=signCurrent;
				
				break;
				
			case IN_ZERO:
				if(signCurrent!=0){
					if(lastSign!=signCurrent)
						markCross(map,lastIndex,lastValue,currentValue);

					state=State.WITHOUT_ZERO;
					lastIndex=i;
					lastValue=currentValue;
					lastSign=signCurrent;
				}
			}
		}
		return map;
	}


	private void markCross(Map<Integer, Double> map, int lastIndex, double lastValue, double currentValue) {
		double markValue;
		if(useSlopeEvaluation)
			markValue=Math.abs(lastValue)+Math.abs(currentValue);
		else
			markValue=MAX_PIXEL_VALUE;
		map.put(lastIndex, markValue);
	}


	private void updateImageX(ImageGray ans, int y, Map<Integer, Double> values) {
		for(Entry<Integer,Double> entry:values.entrySet()){
			int x=entry.getKey();
			ans.setPixel(x, y,entry.getValue());
		}		
	}


	private void updateImageLineY(ImageGray ans, int x, Map<Integer, Double> values) {
		for(Entry<Integer,Double> entry:values.entrySet()){
			int y=entry.getKey();
			ans.setPixel(x, y,entry.getValue());
		}
	}


	private double[] getLineX(ImageGray result, int y) {
		double[] line=new double[result.getWidth()];
		for(int x=0;x<result.getWidth();x++)
			line[x]=result.getPixel(x, y);
		return line;
	}

	private double[] getLineY(ImageGray result, int x) {
		double[] line=new double[result.getHeight()];
		for(int y=0;y<result.getHeight();y++)
			line[y]=result.getPixel(x, y);
		return line;
	}

}
