package vv3ird.ESDSoundboardApp.ngui.pages;

import javax.swing.JPanel;

import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.ngui.ColorScheme;
import vv3ird.ESDSoundboardApp.ngui.components.PDControlScrollPane;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.JViewport;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JNewSoundboardPage extends Page {
	
	
	private JTextField tfSoundBoardName;
	
	private JPanel pnThemes = null;

	private SoundBoard sb = null;
	
	/**
	 * Create the panel.
	 */
	public JNewSoundboardPage(SoundBoard sb) {
		this.sb = sb;
		setSize(700, 460);
		setLayout(null);
		setBackground(ColorScheme.MAIN_BACKGROUND_COLOR);
		JLabel lblNewLabel = new JLabel(this.sb == null ? "New Soundboard" : "Edit Soundboard");
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel.setForeground(ColorScheme.FOREGROUND_COLOR);
		lblNewLabel.setFont(new Font("Segoe UI", lblNewLabel.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 12));
		lblNewLabel.setBounds(10, 11, 680, 30);
		add(lblNewLabel);
		
		tfSoundBoardName = new JTextField(this.sb != null ? this.sb.name : "");
		tfSoundBoardName.setForeground(ColorScheme.FOREGROUND_COLOR);
		tfSoundBoardName.setFont(new Font("Segoe UI", tfSoundBoardName.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 14));
		tfSoundBoardName.setBounds(10, 40, 680, 30);
		tfSoundBoardName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tfSoundBoardName.setBackground(ColorScheme.MAIN_BACKGROUND_COLOR.darker());
		add(tfSoundBoardName);
		tfSoundBoardName.setColumns(10);
		pnThemes = new JPanel();
		pnThemes.setBackground(getBackground().brighter());
		JViewport viewport = new JViewport();
		viewport.setOpaque(false);
		viewport.setView(pnThemes);
		JScrollPane scrollPane = new PDControlScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setViewport(viewport);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setBounds(10, 110, 610, 339);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(8);
		scrollPane.setBounds(10, 110, 680, 340);
		add(scrollPane);
		
		JLabel lblThemes = new JLabel("Themes");
		lblThemes.setVerticalAlignment(SwingConstants.BOTTOM);
		lblThemes.setForeground(Color.WHITE);
		lblThemes.setBounds(10, 80, 163, 30);
		add(lblThemes);
		
		JButton btnRemoveTheme = new JButton("-");
//		btnRemove.setOpaque(true);
		btnRemoveTheme.setBounds(667, 81, 23, 23);
		btnRemoveTheme.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		btnRemoveTheme.setBorderPainted(false);
		add(btnRemoveTheme);
		
		JButton btnAddTheme = new JButton("+");
		btnAddTheme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pageViewer.viewPage(new JNewThemePage(sb == null ? new SoundBoard(tfSoundBoardName.getText()) : sb));
			}
		});
		btnAddTheme.setBorderPainted(false);
//		btnAddTheme.setOpaque(false);
		btnAddTheme.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		btnAddTheme.setBounds(636, 81, 23, 23);
		add(btnAddTheme);
	}
	

	@Override
	public JPanel getButtonBar() {
		// TODO Auto-generated method stub
		return null;
	}
}
