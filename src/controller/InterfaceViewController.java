package controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import controller.utils.MouseSelectionController;
import controller.utils.MouseSelectionListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.file.ImageFileManager;
import model.image.Image;
import model.image.ImageColorRGB;
import model.image.ImageGray;

public class InterfaceViewController implements Initializable, MouseSelectionListener {

	@FXML
	private CustomMenuBar customMenuBar;

	@FXML
	private ImageView mainImage;

	@FXML
	private ImageView secondaryImage;
	
	@FXML
	private ImageView resultImage;

	private Image img;
	private Image secondaryImg;
	private Image resultImg;
	private BufferedImage bufImg;
	private BufferedImage bufSecondaryImg;
	private BufferedImage bufResultImg;
	private MouseSelectionController mouseSelectionController = new MouseSelectionController();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		customMenuBar.initialize(this);
		mainImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				mouseSelectionController.mouseClick(event);
			};
		});
		mainImage.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mouseSelectionController.mouseMoved(event);
			}
		});
		mouseSelectionController.registerListener(this); 
	}

	public void loadImage(File file) {
		try {
			ImageFileManager ifm = new ImageFileManager(file);
			String filename = file.getAbsolutePath();
			String extension = filename.substring(filename.lastIndexOf(".") + 1);
			switch (extension.toUpperCase()) {
			case "PGM":
				img = new ImageGray(ifm.readImagePGM(), false);
				break;
			case "RAW":
				Dialog<String> dialog = new TextInputDialog();
				dialog.setTitle("Input RAW");
				dialog.setHeaderText("Type width and height of " + file.getName() + ",for example: 200x300");
				Optional<String> result = dialog.showAndWait();
				if (!result.isPresent())
					return;
				String input = result.get();
				String[] inputs = input.split("x");
				int width = Integer.parseInt(inputs[0]);
				int height = Integer.parseInt(inputs[1]);
				img = new ImageGray(ifm.readImageRAW(width, height), false);
				break;
			case "PPM":
				img = new ImageColorRGB(ifm.readImagePPM());
				break;
			case "BMP":
				img = new ImageColorRGB(ifm.readImageBMP());
				break;
			default:
				img = new ImageColorRGB(ifm.readImage());
				break;
			}
			refreshImage();
			secondaryImage.setImage(null);
			resultImage.setImage(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Image getImage() {
		return img;
	}

	public Image getSecondaryImage() {
		return secondaryImg;
	}
	
	public Image getResultImage() {
		return resultImg;
	}

	public MouseSelectionController getMouseSelectionController() {
		return mouseSelectionController;
	}
	
	public void selectionMoved(Point firstClick, Point endPosition){
		drawRectangleSelection(firstClick,endPosition);
	}
	public void selectionReset(){
		cleanRectangleSelection();
	}
	public void selectionEnd(Point origin,Point end){
		
	}
	
	private void cleanRectangleSelection() {
		mainImage.setImage(SwingFXUtils.toFXImage(bufImg, null));
	}
	private void drawRectangleSelection(Point firstClick, Point endPosition) {
		BufferedImage bi = new BufferedImage(bufImg.getWidth(), bufImg.getHeight(), bufImg.getType());
		bi.setData(bufImg.getData());
		Graphics2D graphics = (Graphics2D) bi.getGraphics();
		int minX = Math.min(firstClick.x, endPosition.x);
		int maxX = Math.max(firstClick.x, endPosition.x);
		int minY = Math.min(firstClick.y, endPosition.y);
		int maxY = Math.max(firstClick.y, endPosition.y);
		Color colorMin=new Color(bi.getRGB(minX, minY));
		Color colorMax=new Color(bi.getRGB(maxX, maxY));

		Color newColor=new Color(255-(int)((colorMin.getRed()+colorMax.getRed())/2.0),
				255-(int)((colorMin.getGreen()+colorMax.getGreen())/2.0)
				,255-(int)((colorMin.getBlue()+colorMax.getBlue())/2.0));
		Color aux=graphics.getColor();
		graphics.setColor(newColor);
		graphics.drawRect(minX, minY, maxX - minX, maxY - minY);
		graphics.setColor(aux);
		mainImage.setImage(SwingFXUtils.toFXImage(bi, null));
	}




	public void refreshImage() {
		bufImg = img.showImage();
		mainImage.setImage(SwingFXUtils.toFXImage(bufImg, null));
	}

	public void refreshSecondaryImage() {
		bufSecondaryImg = secondaryImg.showImage();
		secondaryImage.setImage(SwingFXUtils.toFXImage(bufSecondaryImg, null));
	}
	
	public void refreshResultImage() {
		bufResultImg = resultImg.showImage();
		resultImage.setImage(SwingFXUtils.toFXImage(bufResultImg, null));
	}

	public void setMainImage(Image img) {
		this.img = img.copy();
	}
	
	public void setSecondaryImage(Image secondaryImg) {
		this.secondaryImg = secondaryImg.copy();
	}
	
	public void setResultImage(Image resultImg) {
		this.resultImg = resultImg.copy();
	}

	public void loadSecondaryImage(File file) {
		try {
			ImageFileManager ifm = new ImageFileManager(file);
			String filename = file.getAbsolutePath();
			String extension = filename.substring(filename.lastIndexOf(".") + 1);
			BufferedImage secondaryImg;
			switch (extension.toUpperCase()) {
			case "PGM":
				secondaryImg = ifm.readImagePGM();
				break;
			case "RAW":
				Dialog<String> dialog = new TextInputDialog();
				dialog.setTitle("Input RAW");
				dialog.setHeaderText("Type width and height of " + file.getName() + ",for example: 200x300");
				Optional<String> result = dialog.showAndWait();
				if (!result.isPresent())
					return;
				String input = result.get();
				String[] inputs = input.split("x");
				int width = Integer.parseInt(inputs[0]);
				int height = Integer.parseInt(inputs[1]);
				secondaryImg = ifm.readImageRAW(width, height);
				break;
			case "PPM":
				secondaryImg = ifm.readImagePPM();
				break;
			case "BMP":
				secondaryImg = ifm.readImageBMP();
				break;
			default:
				secondaryImg = ifm.readImage();
				break;
			}
			this.setSecondaryImage(new ImageGray(secondaryImg, false));
			secondaryImage.setImage(SwingFXUtils.toFXImage(secondaryImg, null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	


}
