package vv3ird.ESDSoundboardApp.ngui.components;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private BufferedImage image;

	public ImagePanel(BufferedImage image) {
		this.image = image;
		this.setOpaque(false);
	}
	public ImagePanel() {
		this.image = image;
		this.setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if(this.image != null)
			g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
		super.paintComponent(g);
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}

}