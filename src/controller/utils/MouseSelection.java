package controller.utils;

import java.awt.Point;

public class MouseSelection {

	private Point firstClick;
	private Point secondClick;

	public Point getFirstClick() {
		return firstClick;
	}

	public void setFirstClick(Point firstClick) {
		this.firstClick = firstClick;
	}

	public Point getSecondClick() {
		return secondClick;
	}

	public void setSecondClick(Point secondClick) {
		this.secondClick = secondClick;
	}

	public void click(Point p) {
		if (firstClick == null) {
			firstClick = p;
		} else if (secondClick == null) {
			secondClick = p;
		} else {
			resetSelection();
		}
	}

	public void resetSelection() {
		firstClick = null;
		secondClick = null;
	}

}
