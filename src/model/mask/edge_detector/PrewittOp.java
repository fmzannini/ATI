package model.mask.edge_detector;

import model.image.Image;
import model.image.ImageColorRGB;
import model.image.ImageGray;
import model.mask.ScrollableWindowRepeat;

public class PrewittOp {

	private static double[][] OP_X={
			{-1,0,1},
			{-1,0,1},
			{-1,0,1}
	};
	private static double[][] OP_Y={
			{-1,-1,-1},
			{0,0,0},
			{1,1,1}
	};

	private static final int WINDOW_SIZE=3;
	
	
	private GradientOp  gradientMask;
	
	public PrewittOp(){
	}
	
	
	public Image apply(Image img){
		Image result=img;
		switch(img.getType()){
		case IMAGE_GRAY:
			result=apply((ImageGray)img);
			break;
		case IMAGE_RGB:
			ImageColorRGB imgColor=(ImageColorRGB)img;
			for(int i=0;i<ImageColorRGB.RGB_QTY;i++){
				ImageGray band=imgColor.getBandOnlyGray(i);
				band=apply(band);
				imgColor.setBand(band,i);
			}
			result=imgColor;
			break;
		}
		return result;
	}
	
	private ImageGray apply(ImageGray img){
		this.gradientMask=new GradientOp(OP_X, OP_Y, new ScrollableWindowRepeat(img, WINDOW_SIZE, WINDOW_SIZE));
		return gradientMask.applyMask();
	}
	
}
