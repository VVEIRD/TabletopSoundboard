package vv3ird.ESDSoundboardApp.ngui.components;

import javax.swing.JPanel;

import vv3ird.ESDSoundboardApp.ngui.ColorScheme;

public class JSelectablePanel extends JPanel {

	private static final long serialVersionUID = -5709653658285821316L;
	boolean selected = false;
	
	public JSelectablePanel() {

	}
	
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		setBackground(this.selected ? ColorScheme.MAIN_BACKGROUND_COLOR : ColorScheme.SIDE_BAR_BACKGROUND_COLOR);
	}
}
