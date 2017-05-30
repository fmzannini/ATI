package model.hough;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.image.ImageGray;

public class HoughTransform {

	private static final double WHITE_VALUE = 255;
	private ImageGray binaryImg;
	private Equation2D equation;
	private Param[] params;
	
	private Map<Double[],Integer> votesMatrix;

	private List<Point> whitePoints;
	
	public HoughTransform(ImageGray binaryImg, Equation2D equation, Param[] params) {
		super();
		this.binaryImg = binaryImg;
		this.equation = equation;
		
		this.params=params;
		
		initializeVotesMatrix();
		initializeWhitePointsList();
	}
	
	private void initializeVotesMatrix(){
		votesMatrix=new HashMap<Double[],Integer>();
		int[] indexes=new int[params.length];
		for(int i=0;i<indexes.length;i++)
			indexes[i]=0;
		
		do{
			Double[] paramsValue=getParamsValues(indexes);
			votesMatrix.put(paramsValue, 0);
		}while((indexes=nextParamsIndexes(indexes))!=null);
	}
	
	private int[] nextParamsIndexes(int[] indexes){
		for(int i=params.length-1;i>=0;i--){
			indexes[i]++;
			if(i!=0 && indexes[i]>=params[i].getLength())
				indexes[i]=0;
			else if(i==0 && indexes[i]>=params[i].getLength())
				return null;
		}
		return indexes;
	}
	private Double[] getParamsValues(int[] indexes){
		Double[] ans=new Double[params.length];
		
		for(int i=0;i<params.length;i++){
			ans[i]=params[i].getValue(indexes[i]);
		}
		return ans;
	}
	
	private void initializeWhitePointsList(){
		whitePoints=new ArrayList<Point>();
		
		int width=binaryImg.getWidth();
		int height=binaryImg.getHeight();
		
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				Point p=new Point(i,j);
				
				if(binaryImg.getPixel(p)==WHITE_VALUE)
					whitePoints.add(p);
			}
		}
	}
	
	public void apply(){
		computateVotes();
	}
	
	private void computateVotes(){
		for(Double[] paramsValue: votesMatrix.keySet()){
			int qty=votesMatrix.get(paramsValue);
			double[] paramsValueUnBox=new double[paramsValue.length];
			for(int i=0;i<paramsValueUnBox.length;i++)
				paramsValueUnBox[i]=paramsValue[i];
			for(Point point:whitePoints){
				if(equation.isSolve(paramsValueUnBox, point)){
					qty++;
				}
			}
			votesMatrix.put(paramsValue, qty);
		}		
	}
	

	public List<Double[]> getResults(){
		int maxVotes=0;
		for(Integer votes:votesMatrix.values()){
			if(votes>maxVotes)
				maxVotes=votes;
		}
		
		List<Double[]> ans=new ArrayList<Double[]>();
		for(Entry<Double[],Integer> entry: votesMatrix.entrySet()){
			if(entry.getValue()>=0.8*maxVotes)
				ans.add(entry.getKey());
		}
		
		return ans;
	}
	

}
