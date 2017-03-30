package model.mask;

import java.util.LinkedList;
import java.util.List;

public class MedianMask extends Mask {

	public MedianMask(ScrollableWindow scroll) {
		super(null, scroll);
	}

	@Override
	protected double applyMask(double[][] region) {
		List<Double> list=new LinkedList<Double>();
		for(int i=0;i<region.length;i++){
			for(int j=0;j<region[0].length;j++){
				list.add(region[i][j]);
			}
		}
		list.sort(null);
		double median=list.get((list.size()-1)/2);
		return median;
	}
}
