package vveird.TabletopSoundboard.ngui.components;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import vveird.TabletopSoundboard.AudioApp;

public class IconPanel extends JPanel implements MouseMotionListener, MouseListener {

	private BufferedImage icon = null;
	
	private JLabel iconLabel = null;
	private Point initialClick = null;

	public IconPanel() {
		this(null);
	}

	public IconPanel(BufferedImage icon) {
		setLayout(null);
		setSize(72, 72);
		setPreferredSize(new Dimension(72, 72));
		setMaximumSize(new Dimension(72, 72));
		setMinimumSize(new Dimension(72, 72));
		if (icon != null) {
			this.icon = icon;
			iconLabel = new JLabel(new ImageIcon(icon));
			iconLabel.setSize(icon.getWidth(), icon.getHeight());
			int x = icon.getWidth() / 2 - 36;
			int y = icon.getHeight() / 2 - 36;
			iconLabel.setLocation(-x, -y);
			this.add(iconLabel);
			iconLabel.addMouseMotionListener(this);
			iconLabel.addMouseListener(this);
			this.revalidate();
		}
	}
	
	public void setImage(BufferedImage icon) {
		this.icon = icon;
		if (iconLabel != null) {
			iconLabel.removeMouseListener(this);
			iconLabel.removeMouseMotionListener(this);
			this.remove(iconLabel);
			validate();
			repaint();
		}
		if (icon != null) {
			iconLabel = new JLabel(new ImageIcon(icon));
			iconLabel.setSize(icon.getWidth(), icon.getHeight());
			int x = icon.getWidth() / 2 - 36;
			int y = icon.getHeight() / 2 - 36;
			iconLabel.setLocation(-x, -y);
			this.add(iconLabel);
			iconLabel.addMouseMotionListener(this);
			iconLabel.addMouseListener(this);
			validate();
			repaint();
		}
	}
	
	public Point getImagePosition() {
		return this.iconLabel.getLocation();
	}

	private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {
		initialClick = evt.getPoint();
	}

	private void jLabel1MouseDragged(java.awt.event.MouseEvent evt) {
		if(initialClick == null)
			jLabel1MousePressed(evt);
		if (iconLabel != null) {
			int thisX = iconLabel.getLocation().x;
			int thisY = iconLabel.getLocation().y;

			// Determine how much the mouse moved since the initial click
			int xMoved = (thisX + evt.getX()) - (thisX + 
					initialClick.x);
			int yMoved = (thisY + evt.getY()) - (thisY + 
					initialClick.y);

			// Move picture to this position
			int X = thisX + xMoved;
			int Y = thisY + yMoved;
			
			X = X + (this.icon.getWidth()-72) < 0 ? -(this.icon.getWidth()-72) : X;
			X = X > 0 ? 0 : X;
			
			Y = Y + (this.icon.getHeight()-72) < 0 ? -(this.icon.getHeight()-72) : Y;
			Y = Y > 0 ? 0 : Y;

			iconLabel.setLocation(X, Y);
			iconLabel.repaint();
		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		jLabel1MouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		jLabel1MousePressed(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		initialClick = null;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BufferedImage bImg = ImageIO.read(Paths.get("images", "600x400.jpg").toFile());
					JFrame frame = new JFrame();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setLayout(null);
					frame.setSize(600, 400);
					IconPanel iP = new IconPanel();
					iP.setImage(bImg);
					iP.setBounds(0, 0, 72, 72);
					iP.setBorder(BorderFactory.createEtchedBorder());
					frame.add(iP);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
