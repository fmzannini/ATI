package model.levelset;

import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AlgorithmLevelSet{
	
	private static final int PIXEL_OBJECT = -3;
	private static final int PIXEL_IN = -1;
	private static final int PIXEL_OUT = 1;
	private static final int PIXEL_BACKGROUND = 3;

	
	private LevelSetImage levelSetImage;
	

	private int matrix[][];

	private Set<Point> pixelsIn;
	private Set<Point> pixelsOut;

	
	private int maxIterations;
	private int iterations=0;

	protected int[][] getMatrix(){
		return matrix;
	}
	public LevelSetImage getLevelSetImage(){
		return levelSetImage;
	}
	
	
	public Set<Point> getPixelsIn() {
		return pixelsIn;
	}
	public Set<Point> getPixelsOut() {
		return pixelsOut;
	}

	public int getMaxIterations() {
		return maxIterations;
	}
	public AlgorithmLevelSet(LevelSetImage levelSetImage, Set<Point> pixelsIn, Set<Point> pixelsOut, int maxIterations) {
		this.pixelsIn=pixelsIn;
		this.pixelsOut=pixelsOut;
		this.maxIterations=maxIterations;
		this.levelSetImage=levelSetImage;
		
		generateMatrix();
	}

	
	public AlgorithmLevelSet(LevelSetImage levelSetImage, Point topLeft, Point bottomRight, int maxIterations) {
		this.pixelsIn=new HashSet<Point>();
		this.pixelsOut=new HashSet<Point>();
		this.maxIterations=maxIterations;
		this.levelSetImage=levelSetImage;
		
		generateSets(topLeft,bottomRight);
		generateMatrix();
	}

	
	private void generateSets(Point topLeft, Point bottomRight) {
		//Pixels In
		setPixelsRow(pixelsIn,topLeft.x,bottomRight.x,topLeft.y); //Borde superior
		setPixelsRow(pixelsIn,topLeft.x,bottomRight.x,bottomRight.y); //Borde inferior
		
		setPixelsCol(pixelsIn,topLeft.x,topLeft.y,bottomRight.y); //Borde izquierdo
		setPixelsCol(pixelsIn,bottomRight.x,topLeft.y,bottomRight.y); //Borde derecho

		
		//Pixels Out
		setPixelsRow(pixelsOut,topLeft.x-1,bottomRight.x+1,topLeft.y-1); //Borde superior
		setPixelsRow(pixelsOut,topLeft.x-1,bottomRight.x+1,bottomRight.y+1); //Borde inferior

		setPixelsCol(pixelsOut,topLeft.x-1,topLeft.y-1,bottomRight.y+1); //Borde izquierdo
		setPixelsCol(pixelsOut,bottomRight.x+1,topLeft.y-1,bottomRight.y+1); //Borde derecho

	}

	private void setPixelsRow(Collection<Point> collection,int xFrom, int xEnd, int y){
		if(y<0 || y>=levelSetImage.getHeight())
			return;
		for(int i=xFrom;i<=xEnd;i++){
			if(i<0 || i>=levelSetImage.getWidth())
				continue;
			collection.add(new Point(i,y));
		}
	}
	private void setPixelsCol(Collection<Point> collection,int x,int yFrom, int yEnd){
		if(x<0 || x>=levelSetImage.getWidth())
			return;
		for(int j=yFrom;j<=yEnd;j++){
			if(j<0 || j>=levelSetImage.getHeight())
				continue;
			collection.add(new Point(x,j));
		}
	}

	private void generateMatrix() {
		int imgWidth=levelSetImage.getWidth();
		int imgHeight=levelSetImage.getHeight();
		this.matrix=new int[imgWidth][imgHeight];
		
		for(Point p:this.pixelsIn){
			this.matrix[p.x][p.y]=PIXEL_IN;
		}
		for(Point p:this.pixelsOut){
			this.matrix[p.x][p.y]=PIXEL_OUT;
		}

		for(int i=0;i<imgWidth;i++){
			boolean isOut=true;
			int lastValue=0;
			for(int j=0;j<imgHeight;j++){
				int currentValue=this.matrix[i][j];
				if(currentValue==0){
					if(isOut){
						this.matrix[i][j]=PIXEL_BACKGROUND;
					}else{
						this.matrix[i][j]=PIXEL_OBJECT;
					}
				}else if((lastValue==PIXEL_OUT && currentValue==PIXEL_IN)
							||(lastValue==PIXEL_IN && currentValue==PIXEL_OUT)){
					isOut=!isOut;
				}
				lastValue=currentValue;
			}
		}
		
	}
	
/*	private void generateMatrix() {
		int imgWidth=levelSetImage.getWidth();
		int imgHeight=levelSetImage.getHeight();
		this.matrix=new int[imgWidth][imgHeight];
		for(int i=0;i<imgWidth;i++){
			for(int j=0;j<imgHeight;j++){
				double fdValue=levelSetImage.calculateFd(new Point(i, j));
				if(fdValue>0){
					this.matrix[i][j]=PIXEL_OBJECT;
				}else{
					this.matrix[i][j]=PIXEL_BACKGROUND;					
				}
			}
		}
		
		for(Point p:this.pixelsIn){
			this.matrix[p.x][p.y]=PIXEL_IN;
		}
		for(Point p:this.pixelsOut){
			this.matrix[p.x][p.y]=PIXEL_OUT;
		}
	}*/

	private void updatePixels(Set<Point> pointsOfBoundToUpdate, int[][] matrix, Set<Point> pointsOfBoundToAdd, int valueForReplace, int valueForMatrix, int valueForBound, CycleLevelSet cycleLevelSet, Condition condition){
		Iterator<Point> iterPixelsToUpdate=pointsOfBoundToUpdate.iterator();
		Set<Point> newsPixelsToAddToSetUpdate=new HashSet<Point>();
		while(iterPixelsToUpdate.hasNext()){
			Point p=iterPixelsToUpdate.next();
			double value=cycleLevelSet.calculateValue(p);
			if(condition.evaluateCondition(value)){
				iterPixelsToUpdate.remove();
				pointsOfBoundToAdd.add(p);
				matrix[p.x][p.y]=valueForReplace;
				List<Point> neighbors=getFourNeighbors(p);
				for(Point neighbor:neighbors){
					if(matrix[neighbor.x][neighbor.y]==valueForMatrix){
						newsPixelsToAddToSetUpdate.add(neighbor);
					}
				}
			}
		}
		for(Point p:newsPixelsToAddToSetUpdate){
			pointsOfBoundToUpdate.add(p);
			matrix[p.x][p.y]=valueForBound;
		}
		
		
	}
	
	private void cleanPixels(Set<Point> setToClean, int[][] matrix, int valueForMatrix,Condition condition){
		Iterator<Point> iterPixelsToClean=setToClean.iterator();
		while(iterPixelsToClean.hasNext()){
			Point p=iterPixelsToClean.next();
			List<Point> neighbors=getFourNeighbors(p);
			boolean isRemove=true;
			for(Point neighbor:neighbors){
				if(condition.evaluateCondition(matrix[neighbor.x][neighbor.y])){
					isRemove=false;
					break;
				}
			}
			if(isRemove){
				matrix[p.x][p.y]=valueForMatrix;
				iterPixelsToClean.remove();
			}
		}				
	}
	
	public void apply(){
		do{
			//first cycle
			//while(!isEnd() && iterations<maxIterations){
			int maxIterationsFirstCycle=Math.max(levelSetImage.getWidth(), levelSetImage.getHeight());
			for(int i=0;i<maxIterationsFirstCycle && !isEnd();i++){
				applyCycle(new CycleLevelSet() {
					
					@Override
					public double calculateValue(Point p) {
						return AlgorithmLevelSet.this.getLevelSetImage().calculateFd(p);
					}
				});
			}

			//second cycle
			for(int i=0;i<GaussianMaskForLevelSet.SIZE_WINDOW;i++){
				applyCycle(new CycleLevelSet() {
					private GaussianMaskForLevelSet gaussianMask=new GaussianMaskForLevelSet();
					
					
					@Override
					public double calculateValue(Point p) {
						return gaussianMask.applyMask(AlgorithmLevelSet.this.getMatrix(), p);
					}
				});				
			}
			iterations++;
		} while(!isEnd() && iterations<maxIterations);
		
	}
	
	private void applyCycle(CycleLevelSet cycleLevelSet) {
		updatePixelsOut(cycleLevelSet);
		cleanPixelsIn();
		updatePixelsIn(cycleLevelSet);
		cleanPixelsOut();
	}

	
	protected void updatePixelsOut(CycleLevelSet cycleLevelSet){
		updatePixels(pixelsOut, matrix, pixelsIn,PIXEL_IN, PIXEL_BACKGROUND, PIXEL_OUT, cycleLevelSet,
				new Condition() {
					
					@Override
					public boolean evaluateCondition(double value) {
						return value>0;
					}
				});
	}
	
	
	protected void cleanPixelsIn(){
		cleanPixels(pixelsIn, matrix, PIXEL_OBJECT,
				new Condition() {
					
					@Override
					public boolean evaluateCondition(double value) {
						return value>0;
					}
				});
	}

	protected void updatePixelsIn(CycleLevelSet cycleLevelSet) {
		updatePixels(pixelsIn, matrix, pixelsOut,PIXEL_OUT, PIXEL_OBJECT, PIXEL_IN, cycleLevelSet,
				new Condition() {
					
					@Override
					public boolean evaluateCondition(double value) {
						return value<0;
					}
				});
	}
	
	protected void cleanPixelsOut(){
		cleanPixels(pixelsOut, matrix, PIXEL_BACKGROUND, 
				new Condition() {
					
					@Override
					public boolean evaluateCondition(double value) {
						return value<0;
					}
				});
	}

	protected boolean isEnd() {
		for(Point p: pixelsIn){
			double fdValue=levelSetImage.calculateFd(p);
			if(fdValue<0)
				return false;
		}
		for(Point p: pixelsOut){
			double fdValue=levelSetImage.calculateFd(p);
			if(fdValue>0)
				return false;
		}

		return true;
	}



	private List<Point> getFourNeighbors(Point p) {
		List<Point> list=new LinkedList<Point>();
		if(p.x-1>=0)
			list.add(new Point(p.x-1,p.y));
		if(p.x+1<levelSetImage.getWidth())
			list.add(new Point(p.x+1,p.y));
		if(p.y-1>=0)
			list.add(new Point(p.x,p.y-1));
		if(p.y+1<levelSetImage.getHeight())
			list.add(new Point(p.x,p.y+1));
		
		return list;
	}
	
		
}
