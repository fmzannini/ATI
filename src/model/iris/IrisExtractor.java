package model.iris;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.image.ImageColorRGB;
import model.image.ImageGray;

public class IrisExtractor {

	private InfoIris info;
	
	
	public ImageColorRGB process(ImageGray copy, List<Point> pixelsInOuterIris ,List<Point> pixelsOutInnerIris, List<Point> pixelsInInnerIris) {
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
		
		boolean firstTime=true;
		for(Integer y: list){
			List<Point> aux1=bins.get(y);
			Point minX1=aux1.get(0);
			Point maxX1=aux1.get(1);
	
			if(firstTime) {
				completeCircle(result,minX1.y,Math.min(minX1.x, maxX1.x),Math.max(minX1.x, maxX1.x));					
				firstTime=false;
			}
			List<Point> aux2=bins.get(y+1);
			if(aux2==null) {
				completeCircle(result,minX1.y,Math.min(minX1.x, maxX1.x),Math.max(minX1.x, maxX1.x));					
				continue;
			}
			Point minX2=aux2.get(0);
			Point maxX2=aux2.get(1);
	
			completeCircle(result,minX2.y,Math.min(minX1.x, minX2.x),Math.max(minX1.x, minX2.x));
			completeCircle(result,maxX2.y,Math.min(maxX1.x, maxX2.x),Math.max(maxX1.x, maxX2.x));
	
		}
		
		
		Point center=getCenter(pixelsOutInnerIris);
		
		Map<Directions,Point> interestPointsOut=new HashMap<Directions,Point>();
		Map<Directions,Point> interestPointsIn=new HashMap<Directions,Point>();
	
		for(Directions dir:Directions.values()){
			int x=center.x;
			int y=center.y;
			
			while(!pixelsOutInnerIris.contains(new Point(x,y)) && !pixelsInInnerIris.contains(new Point(x,y))){
				x+=dir.getX();
				y+=dir.getY();
			}
			
			interestPointsIn.put(dir, new Point(x,y));
	
			while(!result.contains(new Point(x,y))){
				x+=dir.getX();
				y+=dir.getY();
			}
			
			interestPointsOut.put(dir, new Point(x,y));
		}
		
		List<Point> minOut=new ArrayList<Point>();
		List<Point> maxOut=new ArrayList<Point>();
	
		List<Point> minIn=new ArrayList<Point>();
		List<Point> maxIn=new ArrayList<Point>();
	
		
		getPointsSeparate(result, interestPointsOut.get(Directions.D240), interestPointsOut.get(Directions.D300),minOut,maxOut);
		
		getPointsSeparate(pixelsOutInnerIris, interestPointsIn.get(Directions.D240), interestPointsIn.get(Directions.D300),minIn,maxIn);
		
	
	//	List<Point> irisArea=getIrisArea(copy,result,pixelsOutInnerIris,pixelsInInnerIris);
	
		ImageColorRGB imgColor=new ImageColorRGB(copy);
		
		for(Point p: pixelsOutInnerIris)
			imgColor.setPixel(p, new double[]{255,0,0});
		for(Point p: result)
			imgColor.setPixel(p, new double[]{0,0,255});
	
		imgColor.setPixel(center, new double[]{0,255,0});
	
		for(Point p: minOut)
			imgColor.setPixel(p, new double[]{255,255,0});
		for(Point p: maxOut)
			imgColor.setPixel(p, new double[]{0,255,255});
	
		for(Point p: minIn)
			imgColor.setPixel(p, new double[]{255,255,0});
		for(Point p: maxIn)
			imgColor.setPixel(p, new double[]{0,255,255});
		
		
		this.info=new InfoIris(minOut,maxOut,minIn,maxIn);
	
		return imgColor;
	}
	
	private void getPointsSeparate(List<Point> list, Point minY, Point maxY, List<Point> min, List<Point> max) {
		Map<Integer,List<Point>> aux=new HashMap<Integer,List<Point>>();
		for(Point p: list) {
			if(p.y<=maxY.y && p.y>=minY.y) {
				List<Point> points=aux.get(p.y);
				if(points==null)
					points=new ArrayList<Point>();
				points.add(p);
				aux.put(p.y, points);
			}
		}
		
		
		for(Entry<Integer,List<Point>> entry: aux.entrySet()) {
			List<Point> points=entry.getValue();
			Point minPoint=null;
			Point maxPoint=null;
			for(Point p:points) {
				if(minPoint==null || p.x<minPoint.x)
					minPoint=p;
				if(maxPoint==null || p.x>maxPoint.x)
					maxPoint=p;

			}

			min.add(minPoint);
			max.add(maxPoint);				
		}
		
	}
	private Point getCenter(List<Point> pixelsOutInnerIris) {
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
		for(int x=minX; x<=maxX; x++){
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

	public InfoIris getInfoIris() {
		return info;
	}


}
