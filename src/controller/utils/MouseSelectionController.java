package controller.utils;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.input.MouseEvent;

public class MouseSelectionController {

	private MouseSelection mouseSelection=new MouseSelection();
	private List<MouseSelectionListener> listeners=new LinkedList<MouseSelectionListener>();
	
	
	
	public void mouseClick(MouseEvent event) {
		Point click = new Point((int) Math.round(event.getX()), (int) Math.round(event.getY()));
		mouseSelection.click(click);
		if(isSelectionReset()){
			for(MouseSelectionListener listener:listeners)
				listener.selectionReset();
		}
		if(isSelectionEnd()){
			for(MouseSelectionListener listener:listeners)
				listener.selectionEnd(getSelectionOrigin(), getSelectionEnd());
		}
		event.consume();
	};

	public void mouseMoved(MouseEvent event) {
		Point mousePosition = new Point((int) Math.round(event.getX()), (int) Math.round(event.getY()));
		if(isSelectionMoved()){
			for(MouseSelectionListener listener:listeners)
				listener.selectionMoved(mouseSelection.getFirstClick(), mousePosition);
		}
		event.consume();
	}

	private boolean isSelectionMoved() {
		return mouseSelection.getFirstClick()!=null && mouseSelection.getSecondClick()==null;
	}

	private boolean isSelectionEnd() {
		return mouseSelection.getFirstClick()!=null && mouseSelection.getSecondClick()!=null;
	}

	private boolean isSelectionReset() {
		return mouseSelection.getFirstClick()==null && mouseSelection.getSecondClick()==null;
	}

	
	public Point getSelectionOrigin() {
		if (mouseSelection.getFirstClick() == null || mouseSelection.getSecondClick() == null)
			return null;

		int minX = Math.min(mouseSelection.getFirstClick().x, mouseSelection.getSecondClick().x);
		int minY = Math.min(mouseSelection.getFirstClick().y, mouseSelection.getSecondClick().y);

		return new Point(minX, minY);
	}

	public Point getSelectionEnd() {
		if (mouseSelection.getFirstClick() == null || mouseSelection.getSecondClick() == null)
			return null;

		int maxX = Math.max(mouseSelection.getFirstClick().x, mouseSelection.getSecondClick().x);
		int maxY = Math.max(mouseSelection.getFirstClick().y, mouseSelection.getSecondClick().y);

		return new Point(maxX, maxY);
	}

	public void resetSelection() {
		mouseSelection.resetSelection();
		if(isSelectionReset()){
			for(MouseSelectionListener listener:listeners)
				listener.selectionReset();
		}
	}

	public void registerListener(MouseSelectionListener listener) {
		listeners.add(listener);
	}

}
