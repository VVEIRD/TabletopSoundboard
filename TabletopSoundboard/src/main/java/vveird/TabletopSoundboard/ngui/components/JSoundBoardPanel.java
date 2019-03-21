package vveird.TabletopSoundboard.ngui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import de.rcblum.stream.deck.event.KeyEvent;
import de.rcblum.stream.deck.items.FolderItem;
import de.rcblum.stream.deck.items.StreamItem;
import de.rcblum.stream.deck.items.animation.AnimationStack;
import de.rcblum.stream.deck.items.listeners.IconUpdateListener;
import de.rcblum.stream.deck.util.IconHelper;
import de.rcblum.stream.deck.util.IconPackage;
import de.rcblum.stream.deck.util.SDImage;
import vveird.TabletopSoundboard.config.SoundBoard;
import vveird.TabletopSoundboard.ngui.util.ColorScheme;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

/**
 * JPanel to display the soundboards.
 * 
 * @author vveird
 * 
 * CHANGELOG:
 *  
 * DATE       USER    CHANGE
 * ---------- ------- -------------------------------------------------
 * 2019-03-21 vveird  Updated usage of IconHelper to be compatible with
 *                    StreamDeckCOre v 1.0.3
 *
 */
public class JSoundBoardPanel extends JPanel {
	
	private SoundBoard soundBoard = null;

	private int height = 140;
	
	JLabel lblSoundboardname;

	private JPanel pnThemes;
	
	private JLabel btnX;
	
	private MouseListener ml = null;

	public JSoundBoardPanel(SoundBoard soundBoard, boolean lightBackGround) {
		this.soundBoard = Objects.requireNonNull(soundBoard);
		this.setLayout(null);
		Color backgroundColor = lightBackGround ? ColorScheme.MAIN_BACKGROUND_COLOR : ColorScheme.MAIN_BACKGROUND_COLOR.darker() ;
		Color iconColor = lightBackGround ? backgroundColor.darker() : backgroundColor.brighter(); 
		setBackground(backgroundColor);
		setLayout(null);

		lblSoundboardname = new JLabel(soundBoard.name);
		lblSoundboardname.setForeground(Color.LIGHT_GRAY.brighter() );
		lblSoundboardname.setVerticalAlignment(SwingConstants.TOP);
		lblSoundboardname.setFont(new Font("Segoe UI", lblSoundboardname.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 14));
		lblSoundboardname.setHorizontalAlignment(SwingConstants.LEFT);
		lblSoundboardname.setBounds(10, 10, 300, 23);
		lblSoundboardname.setOpaque(false);
		add(lblSoundboardname);
		pnThemes = new JPanel();
		pnThemes.setOpaque(false);
		JViewport viewport = new JViewport();
		viewport.setOpaque(false);
		viewport.setView(pnThemes);
		JScrollPane scrollPane = new PDControlScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setViewport(viewport);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setBounds(0, 40, 450, 100);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		List<String> catNames = new ArrayList<>(this.soundBoard.getCategories());
		for(int i=0; i<catNames.size(); i++) {
			IconHelper.setTextBoxAlphaValue(170);
			String catName = catNames.get(i);
			FolderItem fi = new FolderItem(catName, null, new StreamItem[0]);
			fi.setIcon(IconHelper.createFolderImage(iconColor, true, backgroundColor));
			JLabel jl = new JLabel(new ImageIcon(fi.getIcon().image));
			IconHelper.setTextBoxAlphaValue(200);
			jl.setSize(new Dimension(72, 72));
			pnThemes.add(jl);
		}
		add(scrollPane);
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				scrollPane.setBounds(0, 40, e.getComponent().getWidth(), 100);
			}
		});
		setSize(new Dimension((int)super.getSize().getWidth(), height));
		setPreferredSize(new Dimension((int)super.getSize().getWidth(), height));
		setMaximumSize(new Dimension((int)super.getSize().getWidth(), height));
		setMinimumSize(new Dimension((int)super.getSize().getWidth(), height));
		btnX = new JLabel("X");
		btnX.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnX.setForeground(ColorScheme.FOREGROUND_COLOR);
		btnX.setBounds(getWidth()-25, 0, 23, 23);
		btnX.setOpaque(false);
		add(btnX);
		addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentResized(ComponentEvent e) {
				btnX.setBounds((int)JSoundBoardPanel.super.getSize().getWidth()-23, 0, 23, 23);
			}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
	}
	
	public synchronized void addMouseListenerForDelete(MouseListener l) {
		btnX.addMouseListener(l);
	}
	
	@Override
	public synchronized void addMouseListener(MouseListener l) {
		super.addMouseListener(l);
		ml = l;
		pnThemes.addMouseListener(ml);
		lblSoundboardname.addMouseListener(ml);
		for(int i=0; i<pnThemes.getComponentCount();i++)
			pnThemes.getComponent(i).addMouseListener(ml);
	}
	
	@Override
	public synchronized void removeMouseListener(MouseListener l) {
		super.removeMouseListener(l);
		pnThemes.removeMouseListener(ml);
		lblSoundboardname.removeMouseListener(ml);
		for(int i=0; i<pnThemes.getComponentCount();i++)
			pnThemes.getComponent(i).removeMouseListener(ml);
		ml = null;
	}
	
	@Override
	public Dimension getSize() {
		return new Dimension((int)super.getSize().getWidth(), height);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int)super.getPreferredSize().getWidth(), height);
	}
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension((int)super.getMinimumSize().getWidth(), height);
	}
	
	@Override
	public Dimension getMaximumSize() {
		return new Dimension((int)super.getMaximumSize().getWidth(), height);
	}
	
	@Override
	public Rectangle getBounds(Rectangle rv) {
		rv = super.getBounds(rv);
		rv.height = height;
		return rv;
	}
}
