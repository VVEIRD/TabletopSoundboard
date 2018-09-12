package vv3ird.ESDSoundboardApp.ngui.pages;

import javax.swing.JPanel;

import vv3ird.ESDSoundboardApp.AudioApp;
import vv3ird.ESDSoundboardApp.config.Sound;
import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.ngui.ColorScheme;
import vv3ird.ESDSoundboardApp.ngui.components.PDControlScrollPane;
import vv3ird.ESDSoundboardApp.ngui.components.picker.AutocompleteJComboBox;
import vv3ird.ESDSoundboardApp.ngui.components.picker.SoundSearchable;
import vv3ird.ESDSoundboardApp.ngui.layout.WrapLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.JViewport;

import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JNewThemePage extends Page {
	
	
	private static final long serialVersionUID = -6433885518054038664L;

	private JTextField textField;
	
	private JPanel pnAmbience = null;
	
	private JPanel pnEffect = null;
	private JLabel lblSoundBoardName;
	private JComboBox<Sound> cbAmbeince;
	private JButton btnAddAmbience;
	private JButton btnRemoveAMbience;
	
	private SoundBoard soundBoard = null;

	/**
	 * Create the panel.
	 */
	public JNewThemePage(SoundBoard soundBoard) {
		this.soundBoard = soundBoard;
		setSize(700, 450);
		setLayout(null);
		setBackground(ColorScheme.MAIN_BACKGROUND_COLOR);
		JLabel lblNewLabel = new JLabel("New Theme");
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel.setForeground(ColorScheme.FOREGROUND_COLOR);
		lblNewLabel.setFont(new Font("Segoe UI", lblNewLabel.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 12));
		lblNewLabel.setBounds(10, 11, 83, 30);
		add(lblNewLabel);
		
		textField = new JTextField();
		textField.setForeground(ColorScheme.FOREGROUND_COLOR);
		textField.setFont(new Font("Segoe UI", textField.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 14));
		textField.setBounds(10, 40, 680, 30);
		textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		textField.setBackground(ColorScheme.MAIN_BACKGROUND_COLOR.darker());
		add(textField);
		textField.setColumns(10);
		pnAmbience = new JPanel();
		pnAmbience.setBackground(getBackground().brighter());
		pnAmbience.setLayout(new WrapLayout());
		JViewport viewport = new JViewport();
		viewport.setOpaque(false);
		viewport.setView(pnAmbience);
		JScrollPane ambienceScrollPane = new PDControlScrollPane();
		ambienceScrollPane.setViewportBorder(null);
		ambienceScrollPane.setViewport(viewport);
		ambienceScrollPane.getViewport().setOpaque(false);
		ambienceScrollPane.setOpaque(false);
		ambienceScrollPane.setBorder(BorderFactory.createEmptyBorder());
		ambienceScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		ambienceScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		ambienceScrollPane.getVerticalScrollBar().setUnitIncrement(8);
		ambienceScrollPane.setBounds(10, 110, 330, 300);
		add(ambienceScrollPane);
		
		pnEffect = new JPanel();
		pnEffect.setBackground(getBackground().brighter());
		pnEffect.setLayout(new WrapLayout());
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
//		btnRemove.setOpaque(true);
		btnRemoveEffect.setBounds(667, 421, 23, 23);
		btnRemoveEffect.setBorder(BorderFactory.createLineBorder(ColorScheme.MAIN_BACKGROUND_COLOR.darker(), 2));
		btnRemoveEffect.setBorderPainted(false);
		add(btnRemoveEffect);
		
		JButton btnAddEffect = new JButton("+");
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
		btnAddAmbience.setBorderPainted(false);
		btnAddAmbience.setBorder(BorderFactory.createLineBorder(ColorScheme.MAIN_BACKGROUND_COLOR.darker(), 2));
		btnAddAmbience.setBounds(284, 421, 23, 23);
		add(btnAddAmbience);
		
		btnRemoveAMbience = new JButton("-");
		btnRemoveAMbience.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnRemoveAMbience.setBorderPainted(false);
		btnRemoveAMbience.setBorder(BorderFactory.createLineBorder(ColorScheme.MAIN_BACKGROUND_COLOR.darker(), 2));
		btnRemoveAMbience.setBounds(317, 421, 23, 23);
		add(btnRemoveAMbience);

		cbAmbeince = new JComboBox<Sound>(AudioApp.getAmbienceSounds().toArray(new Sound[0]));
		cbAmbeince.setBounds(10, 421, 265, 22);
		add(cbAmbeince);
		
		
		JComboBox<Sound> cbEffects = new JComboBox<Sound>(AudioApp.getEffectSounds().toArray(new Sound[0]));
		cbEffects.setBounds(359, 421, 265, 22);
		add(cbEffects);
	}

	@Override
	public JPanel getButtonBar() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void okAction() {
		
	}
	
	public void cancelAction() {
		pageViewer.back();
	}
}
