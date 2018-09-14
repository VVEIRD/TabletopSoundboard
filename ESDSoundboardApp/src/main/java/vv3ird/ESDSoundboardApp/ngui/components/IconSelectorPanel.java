package vv3ird.ESDSoundboardApp.ngui.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;

public class IconSelectorPanel extends JPanel {
	
	BufferedImage image = null;
	
	IconPanel iPanel = new IconPanel();
	private JLabel lblZoom;
	private JSlider jsZoom;
	private JLabel lblIcon;
	
	public IconSelectorPanel() {
		this(null);
	}
		
	public IconSelectorPanel(BufferedImage image) {
		setOpaque(false);
		this.image = image;
		setPreferredSize(new Dimension(140, 140));
		setMaximumSize(new Dimension(180, 125));
		setMinimumSize(new Dimension(180, 125));
		setSize(new Dimension(180, 125));
		setLayout(null);
//		iPanel.setBorder(BorderFactory.createEtchedBorder());
		iPanel.setLocation(10, 26);
		iPanel.setImage(image);
		add(iPanel);
		
		JButton btnOpenImage = new JButton("Open");
		btnOpenImage.setBounds(10, 109, 72, 23);
		add(btnOpenImage);
		
		jsZoom = new JSlider();
		jsZoom.setOpaque(false);
		jsZoom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				BufferedImage b = getImage();
				if(b != null) {
					try {
						ImageIO.write(b, "PNG", new File("output.png"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		jsZoom.setValue(100);
		jsZoom.setMaximum(300);
		jsZoom.setMinimum(5);
		jsZoom.setOrientation(SwingConstants.VERTICAL);
		jsZoom.setBounds(92, 26, 35, 81);
		add(jsZoom);
		
		lblZoom = new JLabel("100 %");
		lblZoom.setBounds(92, 118, 42, 14);
		add(lblZoom);
		
		lblIcon = new JLabel("Icon");
		lblIcon.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblIcon.setBounds(10, 11, 46, 14);
		add(lblIcon);
		jsZoom.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				adjustImage();
			}
		});

		btnOpenImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openImage();
			}
		});
	}

	private void adjustImage() {
		lblZoom.setText(jsZoom.getValue() + "%");
		if(image != null) {
			BufferedImage resized = jsZoom.getValue() == 100 ? image : resizeImage(image, jsZoom.getValue(), false);
			this.iPanel.setImage(resized);
		}
	}
	
	public BufferedImage getImage() {
		if(image != null) {
			BufferedImage bimg = resizeImage(image, jsZoom.getValue(), true);
			BufferedImage nimg = new BufferedImage(72, 72, image.getType());
			Graphics2D g = nimg.createGraphics();
			g.drawImage(bimg, this.iPanel.getImagePosition().x, this.iPanel.getImagePosition().y, bimg.getWidth(), bimg.getHeight(), null);
			g.dispose();
			return nimg;
		}
		return null;
	}
	
	private BufferedImage resizeImage(BufferedImage image, int zoom, boolean antialising) {
		int newImageWidth = Math.round(image.getWidth() * (zoom/100f));
		int newImageHeight =  Math.round(image.getHeight() * (zoom/100f));
		BufferedImage resizedImage = new BufferedImage(newImageWidth , newImageHeight, image.getType());
		Graphics2D g = resizedImage.createGraphics();
		if(antialising) {
		    g.setComposite(AlphaComposite.Src);
		    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		    g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.drawImage(image, 0, 0, newImageWidth , newImageHeight , null);
		g.dispose();
		return resizedImage;
	}
	
	private void openImage() {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
		int returnValue = fc.showOpenDialog(this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();
			try {
				BufferedImage bImg = ImageIO.read(selectedFile);
				this.image = bImg;
				adjustImage();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setImage(BufferedImage img) {
		if (img != null) {
			this.image = img;
			adjustImage();
		}
	}
}
