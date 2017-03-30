package controller;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

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

public class InterfaceViewController implements Initializable {

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
	private Selection selection = new Selection();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		customMenuBar.initialize(this);
		mainImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Point click = new Point((int) Math.round(event.getX()), (int) Math.round(event.getY()));
				selection.click(click);
				event.consume();
			};
		});
		mainImage.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Point mousePosition = new Point((int) Math.round(event.getX()), (int) Math.round(event.getY()));
				selection.mousePosition(mousePosition);
				event.consume();
			}
		});
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
	
	private class Selection {
		Point firstClick;
		Point secondClick;

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
			mainImage.setImage(SwingFXUtils.toFXImage(bufImg, null));
		}

		public void mousePosition(Point mousePosition) {
			if (firstClick != null && secondClick == null) {
				BufferedImage bi = new BufferedImage(bufImg.getWidth(), bufImg.getHeight(), bufImg.getType());
				bi.setData(bufImg.getData());
				Graphics2D graphics = (Graphics2D) bi.getGraphics();
				int minX = Math.min(firstClick.x, mousePosition.x);
				int maxX = Math.max(firstClick.x, mousePosition.x);
				int minY = Math.min(firstClick.y, mousePosition.y);
				int maxY = Math.max(firstClick.y, mousePosition.y);

				graphics.drawRect(minX, minY, maxX - minX, maxY - minY);
				mainImage.setImage(SwingFXUtils.toFXImage(bi, null));
			}
		}
	}

	public Point getSelectionOrigin() {
		if (selection.firstClick == null || selection.secondClick == null)
			return null;

		int minX = Math.min(selection.firstClick.x, selection.secondClick.x);
		int minY = Math.min(selection.firstClick.y, selection.secondClick.y);

		return new Point(minX, minY);
	}

	public Point getSelectionEnd() {
		if (selection.firstClick == null || selection.secondClick == null)
			return null;

		int maxX = Math.max(selection.firstClick.x, selection.secondClick.x);
		int maxY = Math.max(selection.firstClick.y, selection.secondClick.y);

		return new Point(maxX, maxY);
	}

	public void resetSelection() {
		selection.resetSelection();
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
		this.img = img;
	}
	
	public void setSecondaryImage(Image secondaryImg) {
		this.secondaryImg = secondaryImg;
	}
	
	public void setResultImage(Image resultImg) {
		this.resultImg = resultImg;
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
			secondaryImage.setImage(SwingFXUtils.toFXImage(secondaryImg, null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
