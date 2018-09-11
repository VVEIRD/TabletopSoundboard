package vv3ird.ESDSoundboardApp.ngui.pages;

import javax.swing.JPanel;

import vv3ird.ESDSoundboardApp.gui.pages.Page;
import vv3ird.ESDSoundboardApp.ngui.ColorScheme;
import vv3ird.ESDSoundboardApp.ngui.internal.PDControlScrollPane;

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

public class JNewSoundboardPage extends Page {
	
	
	private JTextField textField;
	
	private JPanel pnThemes = null;

	/**
	 * Create the panel.
	 */
	public JNewSoundboardPage() {
		setSize(700, 460);
		setLayout(null);
		setBackground(ColorScheme.MAIN_BACKGROUND_COLOR);
		JLabel lblNewLabel = new JLabel("New Soundboard");
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel.setForeground(ColorScheme.FOREGROUND_COLOR);
		lblNewLabel.setFont(new Font("Segoe UI", lblNewLabel.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 12));
		lblNewLabel.setBounds(10, 11, 680, 30);
		add(lblNewLabel);
		
		textField = new JTextField();
		textField.setForeground(ColorScheme.FOREGROUND_COLOR);
		textField.setFont(new Font("Segoe UI", textField.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 14));
		textField.setBounds(10, 40, 680, 30);
		textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		textField.setBackground(ColorScheme.MAIN_BACKGROUND_COLOR.darker());
		add(textField);
		textField.setColumns(10);
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
		btnRemoveTheme.setForeground(ColorScheme.FOREGROUND_COLOR);
		btnRemoveTheme.setBackground(ColorScheme.MAIN_BACKGROUND_COLOR.darker());
//		btnRemove.setOpaque(true);
		btnRemoveTheme.setBounds(667, 81, 23, 23);
		btnRemoveTheme.setBorder(BorderFactory.createLineBorder(ColorScheme.MAIN_BACKGROUND_COLOR.darker(), 2));
		btnRemoveTheme.setBorderPainted(false);
		add(btnRemoveTheme);
		
		JButton btnAddTheme = new JButton("+");
		btnAddTheme.setForeground(Color.WHITE);
		btnAddTheme.setBorderPainted(false);
		btnAddTheme.setBorder(BorderFactory.createLineBorder(ColorScheme.MAIN_BACKGROUND_COLOR.darker(), 2));
		btnAddTheme.setBackground(new Color(51, 94, 116));
		btnAddTheme.setBounds(636, 81, 23, 23);
		add(btnAddTheme);
	}

	@Override
	public void okAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelAction() {
		// TODO Auto-generated method stub
		
	}
}
