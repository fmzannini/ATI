package model.iris;

import java.awt.Point;
import java.util.List;

public class InfoIris {

	private List<Point> minOut;
	private List<Point> maxOut;
	private List<Point> minIn;
	private List<Point> maxIn;
	
	public InfoIris(List<Point> minOut, List<Point> maxOut, List<Point> minIn, List<Point> maxIn) {
		this.minOut=minOut;
		this.maxOut=maxOut;
		this.minIn=minIn;
		this.maxIn=maxIn;
	}

	public List<Point> getMinOut() {
		return minOut;
	}

	public List<Point> getMaxOut() {
		return maxOut;
	}

	public List<Point> getMinIn() {
		return minIn;
	}

	public List<Point> getMaxIn() {
		return maxIn;
	}

	
	
}
