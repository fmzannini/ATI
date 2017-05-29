package controller.segmentation;

import java.io.File;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.ATIApplication;
import controller.InterfaceViewController;
import controller.utils.UtilsDialogs;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import model.image.Image;
import model.image.ImageColorRGB;
import model.image.ImageGray;
import model.levelset.AlgorithmLevelSet;
import model.levelset.LevelSetImage;
import model.levelset.LevelSetImageColorRGB;
import model.levelset.LevelSetImageGray;

public class ButtonLevelSetToVideo extends ButtonLevelSetToImage {

	private static final double SECONDS_TO_NANO = Math.pow(10, 9);
	private static final double NANO_TO_MS = Math.pow(10, 3)/Math.pow(10, 9);
	private static Pattern patternFile=Pattern.compile("(\\d+)([.][^.]*)?");
	private static String HEADER_FOR_VIDEO="LevelSet a un video";
	private long timeFrame;
	
	private List<File> files;
	private Iterator<File> iterFiles;
	
	private InterfaceViewController controller;
	
	public ButtonLevelSetToVideo(InterfaceViewController controller) {
		super(controller,HEADER_FOR_VIDEO);
		this.controller=controller;
	}
	
	@Override
	public void call() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		files = fileChooser.showOpenMultipleDialog(ATIApplication.primaryStage);
		
	/*	files.sort(new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				int number1=extractNumber(o1);
				int number2=extractNumber(o2);
				return number1-number2;
			}
			
			private int extractNumber(File f){
				String name=f.getName();
				Matcher matcher=patternFile.matcher(name);
				matcher.find();
				String group=matcher.group();
				int index=group.indexOf(".");
				if(index!=-1)
					group=group.substring(0, index);
				int number=Integer.parseInt(group);
				return number;
			}
		});*/
		iterFiles=files.iterator();
		
		if(!iterFiles.hasNext())
			return;
		
		controller.loadImage(iterFiles.next());
		
		String[] input=UtilsDialogs.getInputs(HEADER_FOR_VIDEO, "Ingrese frame rate (img/seg)."
				+ "\n Ej: 25"
				+ "\n PAL: 25"
				+ "\n NTSC: 29.7", ",");
		if(input==null || input[0]==null)
			return;
		double frameRate=Double.parseDouble(input[0]);
		
		timeFrame=(long)((1/frameRate)*SECONDS_TO_NANO);
		System.out.println("time between frames: "+timeFrame*NANO_TO_MS+"ms");
		super.call();
	}

	@Override
	protected void process() {
		Thread t1=new Thread(getMainTask());
		t1.start();
//		for(int i=0;i<files.size();i++){
//			PauseTransition pauseTransition=new PauseTransition(new Duration(i*timeFrame*NANO_TO_MS));
//			pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
//				
//				@Override
//				public void handle(ActionEvent event) {
//
//				}
//			});
//		}

	}
	
	private Runnable getMainTask() {

		return new Runnable() {
			
			@Override
			public void run() {
				int numFrame=1;
				AlgorithmLevelSet algorithmLevelSet=firstApplication();

				if(algorithmLevelSet==null)
					return;

			
				while(iterFiles.hasNext()){
					controller.loadImage(iterFiles.next());
					Image img = controller.getImage();
					if (img == null)
						return;

					Image copy = img.copy();

					markResults(img, algorithmLevelSet.getPixelsIn(), algorithmLevelSet.getPixelsOut());
					controller.refreshImage();		
					long timeInit=System.nanoTime();


					AlgorithmLevelSet updateAlgortihmLevelSet = nextApplication(copy, algorithmLevelSet);
					if(updateAlgortihmLevelSet==null)
						return;
					algorithmLevelSet=updateAlgortihmLevelSet;

					long timeEnd=System.nanoTime();
					long elapsedTime=timeEnd-timeInit;
					System.out.println("Time for process algorithm frame "+numFrame+": "+(elapsedTime*NANO_TO_MS)+"ms");
					long diffTimeFrame=timeFrame-elapsedTime;

					controller.setMainImage(copy);
					controller.refreshImage();

					if(diffTimeFrame>0)
						try {
							Thread.sleep((long) (diffTimeFrame*NANO_TO_MS));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					numFrame++;
				}
				
			}
		};
	}

	private AlgorithmLevelSet firstApplication(){
		Image img = controller.getImage();
		if (img == null)
			return null;

		Image copy = img.copy();

		AlgorithmLevelSet algorithmLevelSet=applyAlgorithmToSingleImage(copy);
		if(algorithmLevelSet==null)
			return null;

		controller.setMainImage(copy);
		controller.refreshImage();

		return algorithmLevelSet;
	}
	
	private AlgorithmLevelSet nextApplication(Image copy, AlgorithmLevelSet previousApplication){

		LevelSetImage levelSetImage = null;
		switch (copy.getType()) {
		case IMAGE_GRAY:
			ImageGray imgGray = (ImageGray) copy;
			LevelSetImageGray previousLevelSetImageGray=(LevelSetImageGray)previousApplication.getLevelSetImage();
			levelSetImage = new LevelSetImageGray(imgGray, previousLevelSetImageGray.getMeanColorObject(),previousLevelSetImageGray.getMeanColorBackground());
			break;
		case IMAGE_RGB:
			ImageColorRGB imgRGB=(ImageColorRGB) copy;
			LevelSetImageColorRGB previousLevelSetImageRGB=(LevelSetImageColorRGB)previousApplication.getLevelSetImage();
			levelSetImage=new LevelSetImageColorRGB(imgRGB, previousLevelSetImageRGB.getMeanColorObject(),previousLevelSetImageRGB.getMeanColorBackground());
			break;
		}
		AlgorithmLevelSet algorithmLevelSet = new AlgorithmLevelSet(levelSetImage, previousApplication.getPixelsIn(), previousApplication.getPixelsOut(), previousApplication.getMaxIterations());

		algorithmLevelSet.apply();

		markResults(copy, algorithmLevelSet.getPixelsIn(), algorithmLevelSet.getPixelsOut());
		
		

		return algorithmLevelSet;
	}
}
