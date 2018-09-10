package vv3ird.ESDSoundboardApp.ngui.soundboard;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JPanel;

import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.ngui.ColorScheme;

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
import de.rcblum.stream.deck.util.IconPackage;
import de.rcblum.stream.deck.util.SDImage;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

public class JSoundBoardPanel extends JPanel {
	
	private SoundBoard soundBoard = null;

	private int height = 140;

	private JPanel pnThemes;

	public JSoundBoardPanel(SoundBoard soundBoard, boolean lightBackGround) {
		this.soundBoard = Objects.requireNonNull(soundBoard);
		this.setLayout(null);
		setBackground(lightBackGround ? ColorScheme.MAIN_BACKGROUND_COLOR.darker() : ColorScheme.SIDE_BAR_BACKGROUND_COLOR);
		setLayout(null);

		JLabel lblSoundboardname = new JLabel(soundBoard.name);
		lblSoundboardname.setForeground(lightBackGround ? Color.LIGHT_GRAY.brighter() : Color.LIGHT_GRAY);
		lblSoundboardname.setVerticalAlignment(SwingConstants.TOP);
		lblSoundboardname.setFont(new Font("Segoe UI", lblSoundboardname.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 14));
		lblSoundboardname.setHorizontalAlignment(SwingConstants.LEFT);
		lblSoundboardname.setBounds(10, 10, 430, 23);
		add(lblSoundboardname);
		pnThemes = new JPanel();
		pnThemes.setOpaque(false);
		JViewport viewport = new JViewport();
		viewport.setOpaque(false);
		viewport.setView(pnThemes);
		JScrollPane scrollPane = new JScrollPane();
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
			String catName = catNames.get(i);
			FolderItem fi = new FolderItem(catName, null, new StreamItem[0]);
			JLabel jl = new JLabel(new ImageIcon(fi.getIcon().image));
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
