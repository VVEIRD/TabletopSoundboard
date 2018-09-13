package vv3ird.ESDSoundboardApp.ngui.pages;

import javax.swing.JPanel;

import vv3ird.ESDSoundboardApp.AudioApp;
import vv3ird.ESDSoundboardApp.config.Sound;
import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.ngui.ColorScheme;
import vv3ird.ESDSoundboardApp.ngui.components.FilterComboBox;
import vv3ird.ESDSoundboardApp.ngui.components.JSoundPanel;
import vv3ird.ESDSoundboardApp.ngui.components.PDControlScrollPane;
import vv3ird.ESDSoundboardApp.ngui.layout.WrapLayout;

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

public class JNewThemePage extends Page {
	
	
	private static final long serialVersionUID = -6433885518054038664L;

	private JTextField textField;
	
	private JPanel pnAmbience = null;
	
	private JPanel pnEffect = null;
	private JLabel lblSoundBoardName;
	private JComboBox<Sound> cbAmbeince;
	private JComboBox<Sound> cbEffects;
	private JButton btnAddAmbience;
	private JButton btnRemoveAMbience;
	
	private Map<Sound, JSoundPanel> ambienceSounds = new HashMap<>();
	
	private Map<Sound, JSoundPanel> effectSounds = new HashMap<>();
	
	private SoundBoard soundBoard = null;

	private String themeName = null;

	private JButton ok = new JButton("Ok");
	private JButton cancel = new JButton("Cancel");

	/**
	 * Create the panel.
	 */
	public JNewThemePage(SoundBoard soundBoard, String themeName) {
		this.soundBoard = soundBoard;
		this.themeName  = themeName;
		setSize(700, 450);
		setLayout(null);
		setBackground(ColorScheme.MAIN_BACKGROUND_COLOR);
		JLabel lblNewLabel = new JLabel("New Theme");
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel.setForeground(ColorScheme.FOREGROUND_COLOR);
		lblNewLabel.setFont(new Font("Segoe UI", lblNewLabel.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 12));
		lblNewLabel.setBounds(10, 11, 83, 30);
		add(lblNewLabel);
		
		textField = new JTextField(themeName);
		textField.setForeground(ColorScheme.FOREGROUND_COLOR);
		textField.setFont(new Font("Segoe UI", textField.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 14));
		textField.setBounds(10, 40, 680, 30);
		textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		textField.setBackground(ColorScheme.MAIN_BACKGROUND_COLOR.darker());
		add(textField);
		textField.setColumns(10);
		
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
		
		JButton btnRemoveEffect = new JButton("-");
		btnRemoveEffect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cbEffects.getSelectedItem() != null && cbEffects.getSelectedItem() instanceof Sound) {
					final Sound s = (Sound)cbEffects.getSelectedItem();
					if(effectSounds.containsKey(s)) {
						pnEffect.add(effectSounds.get(s));
						effectSounds.remove(s);
						pnEffect.revalidate();
						pnEffect.repaint();
					}
				}
			}
		});
//		btnRemove.setOpaque(true);
		btnRemoveEffect.setBounds(667, 421, 23, 23);
		btnRemoveEffect.setBorder(BorderFactory.createLineBorder(ColorScheme.MAIN_BACKGROUND_COLOR.darker(), 2));
		btnRemoveEffect.setBorderPainted(false);
		add(btnRemoveEffect);
		
		JButton btnAddEffect = new JButton("+");
		btnAddEffect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cbAmbeince.getSelectedItem() != null && cbAmbeince.getSelectedItem() instanceof Sound) {
					final Sound s = (Sound)cbAmbeince.getSelectedItem();
					if(!effectSounds.containsKey(s)) {
						System.out.println("Adding sound: " + s);
						final JSoundPanel sp = new JSoundPanel(s, pnEffect.getBackground());
						effectSounds.put(s, sp);
						sp.addMouseListener(new MouseListener() {
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
								effectSounds.remove(s);
								pnEffect.remove(sp);
								pnEffect.revalidate();
								pnEffect.repaint();
							}
						});
						pnEffect.add(sp);
						pnEffect.revalidate();
						pnEffect.repaint();
					}
				}
			}
		});
		btnAddEffect.setBorderPainted(false);
		btnAddEffect.setBorder(BorderFactory.createLineBorder(ColorScheme.MAIN_BACKGROUND_COLOR.darker(), 2));
		btnAddEffect.setBounds(634, 421, 23, 23);
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
						final JSoundPanel sp = new JSoundPanel(s, pnAmbience.getBackground());
						ambienceSounds.put(s, sp);
						sp.addMouseListener(new MouseListener() {
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
								ambienceSounds.remove(s);
								pnAmbience.remove(sp);
								pnAmbience.revalidate();
								pnAmbience.repaint();
							}
						});
						pnAmbience.add(sp);
						pnAmbience.revalidate();
						pnAmbience.repaint();
					}
				}
			}
		});
		btnAddAmbience.setBorderPainted(false);
		btnAddAmbience.setBorder(BorderFactory.createLineBorder(ColorScheme.MAIN_BACKGROUND_COLOR.darker(), 2));
		btnAddAmbience.setBounds(284, 421, 23, 23);
		add(btnAddAmbience);
		
		btnRemoveAMbience = new JButton("-");
		btnRemoveAMbience.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cbAmbeince.getSelectedItem() != null && cbAmbeince.getSelectedItem() instanceof Sound) {
					final Sound s = (Sound)cbAmbeince.getSelectedItem();
					if(ambienceSounds.containsKey(s)) {
						pnAmbience.add(ambienceSounds.get(s));
						ambienceSounds.remove(s);
						pnAmbience.revalidate();
						pnAmbience.repaint();
					}
				}
			}
		});
		btnRemoveAMbience.setBorderPainted(false);
		btnRemoveAMbience.setBorder(BorderFactory.createLineBorder(ColorScheme.MAIN_BACKGROUND_COLOR.darker(), 2));
		btnRemoveAMbience.setBounds(317, 421, 23, 23);
		add(btnRemoveAMbience);

		cbAmbeince = new FilterComboBox(AudioApp.getAmbienceSounds());
		cbAmbeince.setBounds(10, 421, 265, 22);
		add(cbAmbeince);
		
		
		cbEffects = new FilterComboBox(AudioApp.getEffectSounds());
		cbEffects.setBounds(359, 421, 265, 22);
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
	
	public void okAction() {
		if(!textField.getText().trim().isEmpty()) {
			String newName = textField.getText();
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
