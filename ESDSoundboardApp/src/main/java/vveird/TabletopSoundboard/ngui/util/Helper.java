package vveird.TabletopSoundboard.ngui.util;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class Helper {
	
	public static Font defaultUiFont = new Font("Segoe UI", Font.PLAIN, 14);

	public static void setTextfieldStyle(JTextField c) {
		c.setForeground(ColorScheme.FOREGROUND_COLOR);
		c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		c.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		c.setBackground(ColorScheme.MAIN_BACKGROUND_COLOR.darker());
	}
}
