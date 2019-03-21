package vveird.TabletopSoundboard.ngui.components;

import javax.swing.JLabel;

import de.rcblum.stream.deck.device.StreamDeck;
import de.rcblum.stream.deck.util.IconHelper;
import de.rcblum.stream.deck.util.SDImage;
import vveird.TabletopSoundboard.AudioApp;
import vveird.TabletopSoundboard.config.Sound;
import vveird.TabletopSoundboard.ngui.pages.JEditSoundPage;
import vveird.TabletopSoundboard.ngui.util.ColorScheme;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * JPanel to display the cover of an Sound object.
 * 
 * @author vveird
 * 
 * Changelog:
 *   DATE       USER    CHANGE
 *   ---------- ------- -------------------------------------------------
 *   2019-03-21 vveird  The cover of the sound will now not be loaded in
 *                      this class, only retrieved from the Sound object
 *
 */
public class JSoundPanel extends ImagePanel {

	private static final long serialVersionUID = 7112434291220556525L;
	
	Sound s = null;
	
	private JLabel btnX;
	private JLabel lblE;
	
	/**
	 * @wbp.parser.constructor
	 */
	public JSoundPanel(Sound s, Color background) {
		this.s = s;
		BufferedImage bImg = null;
		try {
			if(this.s.getCover() != null)
				bImg = this.s.getCover();
			else
				bImg = JEditSoundPage.DEFAULT;
		}
		catch (Exception e) {
		}
		init(bImg, s.getName(), background);
	}
	
	public JSoundPanel(BufferedImage img, String text, Color background) {
		init(img, text, background);
	}

	private void init(BufferedImage img, String text, Color background) {
		setLayout(null);
		int alpha = IconHelper.getTextBoxAlphaValue();
		IconHelper.setTextBoxAlphaValue(150);
		img = IconHelper.applyFrame(img, background);
		SDImage bImg = IconHelper.addText(img, text, IconHelper.TEXT_CENTER);
		IconHelper.setTextBoxAlphaValue(alpha);
		setSize(StreamDeck.ICON_SIZE, StreamDeck.ICON_SIZE);
		setPreferredSize(new Dimension(StreamDeck.ICON_SIZE, StreamDeck.ICON_SIZE));
		setMinimumSize(new Dimension(StreamDeck.ICON_SIZE, StreamDeck.ICON_SIZE));
		setMaximumSize(new Dimension(StreamDeck.ICON_SIZE, StreamDeck.ICON_SIZE));
		setSize(new Dimension(StreamDeck.ICON_SIZE, StreamDeck.ICON_SIZE));
		setImage(bImg.image);
		btnX = new JLabel("X");
		btnX.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnX.setForeground(ColorScheme.FOREGROUND_COLOR);
		btnX.setBounds(59, 0, 23, 23);
		btnX.setOpaque(false);
		add(btnX);
		
		lblE = new JLabel("E");
		lblE.setOpaque(false);
		lblE.setVisible(false);
		lblE.setForeground(Color.WHITE);
		lblE.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblE.setBounds(5, 49, 23, 23);
		add(lblE);
	}
	
	public synchronized void addMouseListenerForDelete(MouseListener l) {
		btnX.addMouseListener(l);
	}
	
	public synchronized void addMouseListenerForEdit(MouseListener l) {
		lblE.addMouseListener(l);
		lblE.setVisible(true);
		lblE.revalidate();
	}
}
