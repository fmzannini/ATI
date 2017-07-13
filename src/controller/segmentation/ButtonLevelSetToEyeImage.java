package controller.segmentation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import controller.InterfaceViewController;
import controller.utils.MouseSelectionListener;
import controller.utils.UtilsDialogs;
import model.image.Image;
import model.image.Image.ImageType;
import model.image.ImageColorRGB;
import model.image.ImageGray;
import model.levelset.AlgorithmLevelSet;
import model.levelset.LevelSetImage;
import model.levelset.LevelSetImageGray;

public class ButtonLevelSetToEyeImage implements MouseSelectionListener{

	private static final String HEADER_FOR_IMAGE="Detectar iris";

	private static final String OUTER_IRIS_MESSAGE = "contorno EXTERIOR del iris. ";
	private static final String INNER_IRIS_MESSAGE = "contorno INTERIOR del iris. ";

	private StateIris currentIrisState = StateIris.OUTER_IRIS;
	private StateLevelSet currentLevelSetState = StateLevelSet.INIT;
	private Point inTopLeft;
	private Point inBottomRight;
	private Point outTopLeft;
	private Point outBottomRight;
	private InterfaceViewController controller;
	
	
	private List<Point> pixelsInOuterIris;
	private List<Point> pixelsOutOuterIris;
	private List<Point> pixelsInInnerIris;
	private List<Point> pixelsOutInnerIris;
	
	private String headerText;

	private int maxIterations;

	public ButtonLevelSetToEyeImage(InterfaceViewController controller) {
		this(controller,HEADER_FOR_IMAGE);
	}
	public ButtonLevelSetToEyeImage(InterfaceViewController controller, String headerText) {
		this.controller = controller;
		this.headerText=headerText;
		controller.getMouseSelectionController().registerListener(this);
	}

	public enum StateIris{
		OUTER_IRIS,INNER_IRIS
	};
	public enum StateLevelSet {
		INIT, SELECTION_IN, SELECTION_OUT
	};

	public void call() {
		Image img = controller.getImage();
		if (img == null)
			return;
		controller.getMouseSelectionController().resetSelection();
		
		String message=null;
		if(currentIrisState==StateIris.OUTER_IRIS)
			message=OUTER_IRIS_MESSAGE;
		else
			message=INNER_IRIS_MESSAGE;
		UtilsDialogs.showAlert(headerText, message + "Seleccione un área DENTRO del iris.");

		currentLevelSetState = StateLevelSet.SELECTION_IN;
	}

public enum Directions{
	D60(1,1),D110(1,-1),D240(-1,-1),D300(-1,1);
	
	int x;
	int y;
	Directions(int x, int y){
		this.x=x;
		this.y=y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
	
};
	protected void process() {
		Image img = controller.getImage();
		if (img == null)
			return;

		if(img.getType()!=ImageType.IMAGE_GRAY)
			img= new ImageGray(img.showImage(), true);
		
		controller.setMainImage(img);
		controller.refreshImage();

		ImageGray copy=(ImageGray)img.copy();
		
		AlgorithmLevelSet algorithmLevelSet=applyAlgorithmToSingleImage(copy);
		if(algorithmLevelSet==null)
			return;
		
		
		controller.setSecondaryImage(copy);
		controller.refreshSecondaryImage();
		controller.refreshImage();
		
		switch(currentIrisState){
		case OUTER_IRIS:
			pixelsInOuterIris=algorithmLevelSet.getPixelsIn();
			pixelsOutOuterIris=algorithmLevelSet.getPixelsOut();
			
			currentIrisState=StateIris.INNER_IRIS;
			call();
			break;
		case INNER_IRIS:
			pixelsInInnerIris=algorithmLevelSet.getPixelsIn();
			pixelsOutInnerIris=algorithmLevelSet.getPixelsOut();
			
			List<Point> result=new ArrayList<Point>();
			Map<Integer,List<Point>> bins=new HashMap<Integer,List<Point>>();
			for(Point p:pixelsInOuterIris){
				List<Point> list=bins.get(p.y);
				if(list==null)
					list=new ArrayList<Point>();
				list.add(p);
				bins.put(p.y, list);
			}
			
			
			for(Entry<Integer,List<Point>> entry: bins.entrySet() ){
				Point minX=null;
				Point maxX=null;
				for(Point p:entry.getValue()){
					if(minX==null || p.x<minX.x)
						minX=p;
					if(maxX==null || p.x>maxX.x)
						maxX=p;
				}
				result.add(minX);
				result.add(maxX);
				entry.getValue().clear();
				entry.getValue().add(minX);
				entry.getValue().add(maxX);

			}
			List<Integer> list=new ArrayList<Integer>(bins.keySet());
			list.sort(new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return o1-o2;
				}
			});
			for(Integer y: list){
				List<Point> aux1=bins.get(y);
				Point minX1=aux1.get(0);
				Point maxX1=aux1.get(1);

				List<Point> aux2=bins.get(y+1);
				if(aux2==null)
					continue;
				Point minX2=aux2.get(0);
				Point maxX2=aux2.get(1);

				completeCircle(result,minX2.y,Math.min(minX1.x, minX2.x),Math.max(minX1.x, minX2.x));
				completeCircle(result,maxX2.y,Math.min(maxX1.x, maxX2.x),Math.max(maxX1.x, maxX2.x));

			}
			
			
			Point center=getCenter(pixelsOutInnerIris);
			
			for(Directions dir:Directions.values()){
				int x=center.x;
				int y=center.y;
				while(!pixelsOutInnerIris.contains(new Point(x,y))){
					x+=dir.getX();
					y+=dir.getY();
				}
				
				
				
			}
			
			
			
			
			

//			List<Point> irisArea=getIrisArea(copy,result,pixelsOutInnerIris,pixelsInInnerIris);

			ImageColorRGB imgColor=new ImageColorRGB(copy);
			for(Point p: pixelsOutInnerIris)
				imgColor.setPixel(p, new double[]{255,0,0});
			for(Point p: result)
				imgColor.setPixel(p, new double[]{0,0,255});

			imgColor.setPixel(center, new double[]{0,255,0});

	//markIrisArea(copy, irisArea);

			controller.setSecondaryImage(imgColor);
			controller.refreshSecondaryImage();
			controller.refreshImage();

			currentIrisState=StateIris.OUTER_IRIS;
		}

	}

	private Point getCenter(List<Point> pixelsOutInnerIris2) {
		float x=0;
		float y=0;
		int count=0;
		for(Point p:pixelsOutInnerIris){
			x+=p.x;
			y+=p.y;
			count++;
		}
		return new Point(Math.round(x/count),Math.round(y/count));
	}
	private void completeCircle(List<Point> result, int y, int minX, int maxX) {
		for(int x=minX; x<maxX; x++){
			result.add(new Point(x,y));
		}
		
	}
	private void markIrisArea(ImageGray copy, List<Point> irisArea) {
		for (Point p : irisArea) {
			copy.setPixel(p, 128);			
		}
	}
	private List<Point> getIrisArea(ImageGray copy,List<Point> pixelsOuter, List<Point> pixelsInner , List<Point> pixelsOutInner) {
		Set<Point> allPoints=new HashSet<Point>();
		
		Set<Point> currentStep=new HashSet<Point>();
		Set<Point> newStep=null;

		Set<Point> firstTime=new HashSet<>(pixelsOutInner);
		
		
		allPoints.addAll(pixelsOuter);
		for(Point p: pixelsInner){
			allPoints.add(p);
			currentStep.add(p);
		}
		
		do{
			newStep=new HashSet<Point>();
			for(Point p:currentStep){
				List<Point> neighbours=getFourNeighbours(p);
				for(Point neighbour: neighbours){
					if(p.x<0 || p.y<0 || neighbour.x<0 ||neighbour.y<0)
						System.out.println(" ");
					if(firstTime==null || (firstTime!=null && !firstTime.contains(neighbour))){
						if(!allPoints.contains(neighbour)){
							allPoints.add(neighbour);
							newStep.add(neighbour);
						}
					}
					
				}
			}
			currentStep=newStep;

			markIrisArea(copy, new ArrayList<Point>(allPoints));

			controller.setSecondaryImage(copy);
			controller.refreshSecondaryImage();
			controller.refreshImage();

		//	if(firstTime!=null)
			//	firstTime=null;
		}while(newStep.size()>0);
		
		
		return new ArrayList<Point>(allPoints);
	}
	private List<Point> getFourNeighbours(Point p) {
		List<Point> neighbours=new ArrayList<Point>();
		
		neighbours.add(new Point(p.x+1,p.y));
		neighbours.add(new Point(p.x-1,p.y));
		neighbours.add(new Point(p.x,p.y+1));
		neighbours.add(new Point(p.x,p.y-1));
		
		return neighbours;
	}
	protected AlgorithmLevelSet applyAlgorithmToSingleImage(ImageGray imgGray){

		LevelSetImage levelSetImage = null;
		
		levelSetImage = new LevelSetImageGray(imgGray, inTopLeft, inBottomRight,
				imgGray.getRegion(outTopLeft, outBottomRight));
		
		AlgorithmLevelSet algorithmLevelSet = new AlgorithmLevelSet(levelSetImage, inTopLeft, inBottomRight,
				maxIterations);

		algorithmLevelSet.apply();

		markResults(imgGray, algorithmLevelSet.getPixelsIn(), algorithmLevelSet.getPixelsOut());

		return algorithmLevelSet;
	}
	private static final double MARK_GRAY_IN = 0;
	private static final double MARK_GRAY_OUT = 255;
	private static final double[] MARK_RGB_IN = { 255, 128, 0 };
	private static final double[] MARK_RGB_OUT = { 0, 128, 255 };

	protected void markResults(Image copy, List<Point> pixelsIn, List<Point> pixelsOut) {
		switch (copy.getType()) {
		case IMAGE_GRAY:
			ImageGray imgGray = (ImageGray) copy;
			for (Point p : pixelsIn) {
//				imgGray.setPixel(p, MARK_GRAY_IN);
				imgGray.setPixel(p, 255-imgGray.getPixel(p));			
			}
			for (Point p : pixelsOut) {
//				imgGray.setPixel(p, MARK_GRAY_OUT);
				imgGray.setPixel(p, 255-imgGray.getPixel(p));			
			}
			break;
		case IMAGE_RGB:
			ImageColorRGB imgRGB = (ImageColorRGB) copy;
			for (Point p : pixelsIn) {
//				imgRGB.setPixel(p, MARK_RGB_IN);
				double[] pixel=imgRGB.getPixel(p);
				for(int i=0;i<Image.RGB_QTY;i++)
//					pixel[i]=255-pixel[i];
					pixel[i]=pixel[i]<128?255:0;

				imgRGB.setPixel(p, pixel);
			}
			for (Point p : pixelsOut) {
//				imgRGB.setPixel(p, MARK_RGB_OUT);
				double[] pixel=imgRGB.getPixel(p);
				for(int i=0;i<Image.RGB_QTY;i++)
//					pixel[i]=255-pixel[i];
					pixel[i]=pixel[i]<128?255:0;
				imgRGB.setPixel(p, pixel);
			}
			break;
		}

	}

	@Override
	public void selectionMoved(Point firstClick, Point endPosition) {
	}

	@Override
	public void selectionReset() {
	}

	@Override
	public void selectionEnd(Point origin, Point end) {
		switch(currentLevelSetState){
			case INIT:
				break;
			case SELECTION_IN:
				inTopLeft = origin;
				inBottomRight = end;
		
				String message=null;
				if(currentIrisState==StateIris.OUTER_IRIS)
					message=OUTER_IRIS_MESSAGE;
				else
					message=INNER_IRIS_MESSAGE;
				
				UtilsDialogs.showAlert(headerText, message + "Seleccione un área FUERA del iris.");
				currentLevelSetState = StateLevelSet.SELECTION_OUT;
				controller.getMouseSelectionController().resetSelection();
				break;
			case SELECTION_OUT:
				outTopLeft = origin;
				outBottomRight = end;

				controller.getMouseSelectionController().resetSelection();
				currentLevelSetState = StateLevelSet.INIT;
				
				String[] inputs = UtilsDialogs.getInputs(headerText,
						"Ingrese la máxima cantidad de iteraciones." + "\n Ej:15", ",");
				if (inputs == null || inputs.length != 1)
					return ;

				this.maxIterations = Integer.parseInt(inputs[0]);
				process();
				break;
		}
	}

}
