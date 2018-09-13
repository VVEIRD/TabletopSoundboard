package vv3ird.ESDSoundboardApp.ngui.components;

import javax.swing.JPanel;

import de.rcblum.stream.deck.device.StreamDeck;
import de.rcblum.stream.deck.util.IconHelper;
import de.rcblum.stream.deck.util.SDImage;
import vv3ird.ESDSoundboardApp.config.Sound;
import vv3ird.ESDSoundboardApp.ngui.ColorScheme;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class JSoundPanel extends ImagePanel {

	Sound s = null;
	private JLabel btnX;
	
	public JSoundPanel(Sound s, Color background) {
		setLayout(null);
		setSize(StreamDeck.ICON_SIZE, StreamDeck.ICON_SIZE);
		setPreferredSize(new Dimension(StreamDeck.ICON_SIZE, StreamDeck.ICON_SIZE));
		setMinimumSize(new Dimension(StreamDeck.ICON_SIZE, StreamDeck.ICON_SIZE));
		setMaximumSize(new Dimension(StreamDeck.ICON_SIZE, StreamDeck.ICON_SIZE));
		setSize(new Dimension(StreamDeck.ICON_SIZE, StreamDeck.ICON_SIZE));
		setBackground(Color.RED);
		this.s = s;
		Color oldC = IconHelper.FRAME_COLOR;
		try {
			IconHelper.FRAME_COLOR = background;
			SDImage bImg = IconHelper.loadImage(this.s.getCoverPath(), new FileInputStream(new File(this.s.getCoverPath())), true);
			bImg = IconHelper.addText(bImg, s.getName(), IconHelper.TEXT_BOTTOM);
			setImage(bImg.image);
		}
		catch (IOException e) {
		}
		IconHelper.FRAME_COLOR = oldC;
		btnX = new JLabel("X");
		btnX.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnX.setForeground(ColorScheme.FOREGROUND_COLOR);
		btnX.setBounds(59, 0, 23, 23);
		btnX.setOpaque(false);
		add(btnX);
	}
	
	public JSoundPanel(BufferedImage img, Color background) {
		setLayout(null);
		setSize(StreamDeck.ICON_SIZE, StreamDeck.ICON_SIZE);
		setPreferredSize(new Dimension(StreamDeck.ICON_SIZE, StreamDeck.ICON_SIZE));
		setMinimumSize(new Dimension(StreamDeck.ICON_SIZE, StreamDeck.ICON_SIZE));
		setMaximumSize(new Dimension(StreamDeck.ICON_SIZE, StreamDeck.ICON_SIZE));
		setSize(new Dimension(StreamDeck.ICON_SIZE, StreamDeck.ICON_SIZE));
		setImage(img);
		btnX = new JLabel("X");
		btnX.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnX.setForeground(ColorScheme.FOREGROUND_COLOR);
		btnX.setBounds(59, 0, 23, 23);
		btnX.setOpaque(false);
		add(btnX);
	}
	
	@Override
	public synchronized void addMouseListener(MouseListener l) {
		btnX.addMouseListener(l);
	}
	
}
