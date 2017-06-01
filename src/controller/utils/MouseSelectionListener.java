package controller.utils;

import java.awt.Point;

public interface MouseSelectionListener {

	public void selectionMoved(Point firstClick, Point endPosition);
	public void selectionReset();
	public void selectionEnd(Point origin,Point end);
}
