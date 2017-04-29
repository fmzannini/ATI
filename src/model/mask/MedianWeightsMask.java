package model.mask;

import java.util.ArrayList;
import java.util.List;

public class MedianWeightsMask extends Mask {

	private static int[][] WEIGHTS_3={
			{1,2,1},
			{2,4,2},
			{1,2,1}
	};
	
	private int[][] weights;
	
	public MedianWeightsMask(ScrollableWindow scroll) {
		super(null, scroll);
		this.weights=WEIGHTS_3;
	}
	public MedianWeightsMask(int[][] weights, ScrollableWindow scroll) {
		super(null, scroll);
		this.weights=weights;
	}
	@Override
	protected double applyMask(double[][] region) {
		List<Double> list=new ArrayList<Double>(region.length*region[0].length);
		for(int i=0;i<region.length;i++){
			for(int j=0;j<region[0].length;j++){
				for(int k=0;k<this.weights[i][j];k++){
					list.add(region[i][j]);
				}
			}
		}
		
		list.sort(null);
		
		if(list.size()%2==0){
			double median1=list.get((list.size()/2-1));
			double median2=list.get((list.size()/2));
			return (median1+median2)/2.0;
		}else{
			return list.get((list.size()-1)/2);
		}
	}
}
