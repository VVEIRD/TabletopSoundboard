package vv3ird.ESDSoundboardApp.ngui.plugins;

import java.util.Map;

import javax.swing.JPanel;

public abstract class JPluginConfigurationPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8954617829186923418L;

	public abstract boolean isConfigured();
	
	public abstract void save();

	public abstract void enablePlugin();

	public abstract boolean isEnabled();

	public abstract void disablePlugin();
	
	public abstract boolean initPlugin();
	
	public abstract String getPluginName();
}
