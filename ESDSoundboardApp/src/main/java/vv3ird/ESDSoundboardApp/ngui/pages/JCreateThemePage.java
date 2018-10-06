package vv3ird.ESDSoundboardApp.ngui.pages;

import javax.swing.JPanel;

import vv3ird.ESDSoundboardApp.AudioApp;
import vv3ird.ESDSoundboardApp.config.Sound;
import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.ngui.components.FilterComboBox;
import vv3ird.ESDSoundboardApp.ngui.components.JSoundPanel;
import vv3ird.ESDSoundboardApp.ngui.components.PDControlScrollPane;
import vv3ird.ESDSoundboardApp.ngui.layout.WrapLayout;
import vv3ird.ESDSoundboardApp.ngui.util.ColorScheme;
import vv3ird.ESDSoundboardApp.ngui.util.Helper;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JViewport;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.SwingConstants;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;

public class JCreateThemePage extends Page {
	
	
	private static final long serialVersionUID = -6433885518054038664L;

	private JTextField tfThemeName;
	
	private JPanel pnAmbience = null;
	
	private JPanel pnEffect = null;
	private JLabel lblSoundBoardName;
	private JComboBox<Sound> cbAmbeince;
	private JComboBox<Sound> cbEffects;
	private JButton btnAddAmbience;
	
	private Map<Sound, JSoundPanel> ambienceSounds = new HashMap<>();
	
	private Map<Sound, JSoundPanel> effectSounds = new HashMap<>();
	
	private SoundBoard soundBoard = null;

	private String themeName = null;

	private JButton ok = new JButton("Ok");
	private JButton cancel = new JButton("Cancel");

	/**
	 * Create the panel.
	 */
	public JCreateThemePage(SoundBoard soundBoard, String themeName) {
		setSize(700, 450);
		setLayout(null);
		setBackground(ColorScheme.MAIN_BACKGROUND_COLOR);
		
		this.soundBoard = soundBoard;
		this.themeName  = themeName;
		
		JLabel lblNewLabel = new JLabel("New Theme");
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel.setForeground(ColorScheme.FOREGROUND_COLOR);
		lblNewLabel.setFont(new Font("Segoe UI", lblNewLabel.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 12));
		lblNewLabel.setBounds(10, 11, 83, 30);
		add(lblNewLabel);
		
		tfThemeName = new JTextField(themeName);
//		tfThemeName.setForeground(ColorScheme.FOREGROUND_COLOR);
//		tfThemeName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//		tfThemeName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//		tfThemeName.setBackground(ColorScheme.MAIN_BACKGROUND_COLOR.darker());
		Helper.setTextfieldStyle(tfThemeName);
		tfThemeName.setBounds(10, 40, 680, 30);
		add(tfThemeName);
		tfThemeName.setColumns(10);
		
		//
		// AMBIENCE FIELD
		//
		pnAmbience = new JPanel();
		pnAmbience.setBackground(getBackground().brighter());
		pnAmbience.setLayout(new WrapLayout(FlowLayout.LEFT));
		JViewport viewport = new JViewport();
		viewport.setOpaque(false);
		viewport.setView(pnAmbience);
		JScrollPane ambienceScrollPane = new PDControlScrollPane();
		ambienceScrollPane.setViewportBorder(null);
		ambienceScrollPane.setViewport(viewport);
		ambienceScrollPane.getViewport().setOpaque(false);
		ambienceScrollPane.setOpaque(false);
		ambienceScrollPane.setBorder(BorderFactory.createEmptyBorder());
		ambienceScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		ambienceScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		ambienceScrollPane.getVerticalScrollBar().setUnitIncrement(8);
		ambienceScrollPane.setBounds(10, 110, 330, 300);
		add(ambienceScrollPane);
		
		pnEffect = new JPanel();
		pnEffect.setBackground(getBackground().brighter());
		pnEffect.setLayout(new WrapLayout(FlowLayout.LEFT));
		viewport = new JViewport();
		viewport.setOpaque(false);
		viewport.setView(pnEffect);
		PDControlScrollPane effectScrollPane = new PDControlScrollPane();
		effectScrollPane.setViewportBorder(null);
		effectScrollPane.setViewport(viewport);
		effectScrollPane.getViewport().setOpaque(false);
		effectScrollPane.setOpaque(false);
		effectScrollPane.setBounds(360, 110, 330, 300);
		effectScrollPane.setBorder(BorderFactory.createEmptyBorder());
		effectScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		effectScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		effectScrollPane.getVerticalScrollBar().setUnitIncrement(8);
		add(effectScrollPane);
		
		JLabel lblAmbience = new JLabel("Ambience");
		lblAmbience.setVerticalAlignment(SwingConstants.BOTTOM);
		lblAmbience.setForeground(Color.WHITE);
		lblAmbience.setBounds(10, 80, 163, 30);
		add(lblAmbience);
		
		JButton btnAddEffect = new JButton("+");
		btnAddEffect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cbAmbeince.getSelectedItem() != null && cbAmbeince.getSelectedItem() instanceof Sound) {
					final Sound s = (Sound)cbAmbeince.getSelectedItem();
					if(!effectSounds.containsKey(s)) {
						System.out.println("Adding sound: " + s);
						final JSoundPanel sp = createSoundPanel(s, pnEffect, effectSounds);
						effectSounds.put(s, sp);
						pnEffect.add(sp);
						pnEffect.revalidate();
						pnEffect.repaint();
					}
				}
			}
		});
		btnAddEffect.setBorderPainted(false);
		btnAddEffect.setBorder(BorderFactory.createLineBorder(ColorScheme.MAIN_BACKGROUND_COLOR.darker(), 2));
		btnAddEffect.setBounds(667, 421, 23, 23);
		add(btnAddEffect);
		
		lblSoundBoardName = new JLabel(this.soundBoard.name);
		lblSoundBoardName.setFont(new Font("Segoe UI", lblSoundBoardName.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 14));
		lblSoundBoardName.setHorizontalAlignment(SwingConstants.CENTER);
		lblSoundBoardName.setForeground(Color.WHITE);
		lblSoundBoardName.setBounds(108, 11, 500, 30);
		add(lblSoundBoardName);
		
		JLabel lblEffects = new JLabel("Effects");
		lblEffects.setVerticalAlignment(SwingConstants.BOTTOM);
		lblEffects.setForeground(Color.WHITE);
		lblEffects.setBounds(360, 80, 163, 30);
		add(lblEffects);
		
		btnAddAmbience = new JButton("+");
		btnAddAmbience.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cbAmbeince.getSelectedItem() != null && cbAmbeince.getSelectedItem() instanceof Sound) {
					final Sound s = (Sound)cbAmbeince.getSelectedItem();
					if(!ambienceSounds.containsKey(s)) {
						System.out.println("Adding sound: " + s);
						final JSoundPanel sp = createSoundPanel(s, pnAmbience, ambienceSounds);
						ambienceSounds.put(s, sp);
						pnAmbience.add(sp);
						pnAmbience.revalidate();
						pnAmbience.repaint();
					}
				}
			}
		});
		btnAddAmbience.setBorderPainted(false);
		btnAddAmbience.setBorder(BorderFactory.createLineBorder(ColorScheme.MAIN_BACKGROUND_COLOR.darker(), 2));
		btnAddAmbience.setBounds(317, 421, 23, 23);
		add(btnAddAmbience);

		cbAmbeince = new FilterComboBox(AudioApp.getAmbienceSounds());
		cbAmbeince.setBounds(10, 421, 297, 22);
		add(cbAmbeince);
		
		
		cbEffects = new FilterComboBox(AudioApp.getEffectSounds());
		cbEffects.setBounds(359, 421, 298, 22);
		add(cbEffects);

		ok.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okAction();
			}
		});
		ok.setBorderPainted(false);
		ok.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		
		// Cancel Button
		cancel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelAction();
			}
		});
		cancel.setBorderPainted(false);
		cancel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		//
		// DATA
		//
		if(this.themeName != null && this.soundBoard.getAmbienceSounds(this.themeName) != null) {
			List<Sound> amb = this.soundBoard.getAmbienceSounds(this.themeName);
			for (Sound sound : amb) {
				final JSoundPanel sp = createSoundPanel(sound, pnAmbience, ambienceSounds);
				ambienceSounds.put(sound, sp);
				pnAmbience.add(sp);
				pnAmbience.revalidate();
				pnAmbience.repaint();
			}
		}
		if(this.themeName != null && this.soundBoard.getEffectSounds(this.themeName) != null) {
			List<Sound> amb = this.soundBoard.getEffectSounds(this.themeName);
			for (Sound sound : amb) {
				final JSoundPanel sp = createSoundPanel(sound, pnEffect, effectSounds);
				effectSounds.put(sound, sp);
				pnEffect.add(sp);
				pnEffect.revalidate();
				pnEffect.repaint();
			}
		}
	}

	@Override
	public JPanel getButtonBar() {
		JPanel bb = new JPanel();
		bb.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bb.add(cancel);
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(5, 5));
		p.setOpaque(false);
		bb.add(p);
		bb.add(ok);
		return bb;
	}

	private JSoundPanel createSoundPanel(final Sound s, JPanel panel, Map<Sound, JSoundPanel> sounds) {
		final JSoundPanel sp = new JSoundPanel(s, panel.getBackground());
		sp.addMouseListenerForDelete(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				sounds.remove(s);
				panel.remove(sp);
				panel.revalidate();
				panel.repaint();
			}
		});
		return sp;
	}
	
	public void okAction() {
		if(!tfThemeName.getText().trim().isEmpty()) {
			String newName = tfThemeName.getText();
			if(!newName.trim().isEmpty()) {
				if(themeName != null)
					soundBoard.removeCategory(themeName);
				soundBoard.addCategory(newName);
				for (Sound s : ambienceSounds.keySet()) {
					soundBoard.addAmbienceSound(newName, s);
				}
				for (Sound s : effectSounds.keySet()) {
					soundBoard.addEffectSound(newName, s);
				}
				pageViewer.back();
			}
		}
	}
	
	public void cancelAction() {
		pageViewer.back();
	}
}
