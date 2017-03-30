package model.mask;

public class MeanMask extends Mask {

	public MeanMask(ScrollableWindow scroll) {
		super(generateWeights(scroll), scroll);
	}

	private static double[][] generateWeights(ScrollableWindow scroll) {
		int widthWindow=scroll.getWidthWindow();
		int heightWindow=scroll.getHeightWindow();
		
		int total=widthWindow*heightWindow;
		double mean=1/(double)total;
		double[][] weights=new double[widthWindow][heightWindow];
		
		for(int i=0;i<widthWindow;i++){
			for(int j=0;j<heightWindow;j++)
				weights[i][j]=mean;
		}
		
		return weights;
	}
	
}
