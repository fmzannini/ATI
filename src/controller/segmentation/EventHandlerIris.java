package controller.segmentation;


import controller.InterfaceViewController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.iris.InfoIris;

public class EventHandlerIris implements EventHandler<ActionEvent>{

	private ButtonLevelSetToEyeImage buttonHandler;

	public EventHandlerIris(InterfaceViewController controller) {
		buttonHandler=new ButtonLevelSetToEyeImage(controller);

	}
	
	@Override
	public void handle(ActionEvent event) {
		buttonHandler.call();
	}
	
	public InfoIris getInfoIris() {
		return buttonHandler.getInfoIris();
	}

}
