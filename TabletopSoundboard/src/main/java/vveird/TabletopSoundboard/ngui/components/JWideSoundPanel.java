package vveird.TabletopSoundboard.ngui.components;

import javax.swing.JLabel;

import de.rcblum.stream.deck.device.StreamDeck;
import de.rcblum.stream.deck.device.StreamDeckConstants;
import de.rcblum.stream.deck.util.IconHelper;
import de.rcblum.stream.deck.util.SDImage;
import vveird.TabletopSoundboard.config.Sound;
import vveird.TabletopSoundboard.ngui.pages.JEditSoundPage;
import vveird.TabletopSoundboard.ngui.util.ColorScheme;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

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
public class JWideSoundPanel extends ImagePanel {

	private static final long serialVersionUID = 7112434291220556525L;
	
	Sound s = null;
	
	private JLabel btnX;
	private JLabel lblE;
	private JLabel lblTags;
	
	/**
	 * @wbp.parser.constructor
	 */
	public JWideSoundPanel(Sound s, Color background) {
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
	
	public JWideSoundPanel(BufferedImage img, String text, Color background) {
		init(img, text, background);
	}

	private void init(BufferedImage img, String text, Color background) {
		setLayout(null);
		int alpha = IconHelper.getTextBoxAlphaValue();
		IconHelper.setTextBoxAlphaValue(150);
		img = IconHelper.applyFrame(img, background.darker());
		SDImage bImg = IconHelper.addText(img, "", IconHelper.TEXT_CENTER);
		IconHelper.setTextBoxAlphaValue(alpha);
		setSize(StreamDeckConstants.ICON_SIZE);
		setPreferredSize(new Dimension(689, 72));
		setMinimumSize(new Dimension(689, (int)StreamDeckConstants.ICON_SIZE.getHeight()));
		setMaximumSize(new Dimension(689, (int)StreamDeckConstants.ICON_SIZE.getHeight()));
		setSize(new Dimension(689, (int)StreamDeckConstants.ICON_SIZE.getHeight()));
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
		String tags = "";
		if (this.s != null && this.s.getTags() != null)
			tags = String.join(" ", this.s.getTags());
		
		JPanel panel = new JPanel();
		panel.setBounds(72, 0, 612, 72);
		add(panel);
		panel.setLayout(null);
		panel.setBackground(background.darker());
		JLabel lblSoundName = new JLabel(text);
		lblSoundName.setSize(605, 40);
		lblSoundName.setLocation(5, 0);
		lblSoundName.setForeground(ColorScheme.FOREGROUND_COLOR);
		panel.add(lblSoundName);
		lblSoundName.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTags = new JLabel(tags);
		lblTags.setLocation(25, 50);
		lblTags.setSize(580, 20);
		lblTags.setForeground(ColorScheme.FOREGROUND_COLOR);
		panel.add(lblTags);
		lblTags.setFont(new Font("Segoe UI", Font.PLAIN, 12));
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
