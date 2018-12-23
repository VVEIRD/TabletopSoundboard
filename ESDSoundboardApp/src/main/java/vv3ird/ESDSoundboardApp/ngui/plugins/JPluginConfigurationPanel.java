package vv3ird.ESDSoundboardApp.ngui.plugins;

import javax.swing.JPanel;

/**
 * Configuralion Panel for the plugin it comes with. All changes should only be
 * saved when the save function is called.
 * 
 * @author rcBlum
 *
 */
public abstract class JPluginConfigurationPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8954617829186923418L;

	/**
	 * returns if the plugin is properly configured
	 * @return <code>true</code> if the plugin is configured correctly, <code>false</code> if not.
	 */
	public abstract boolean isConfigured();
	
	/**
	 * Method is called by the gui if the save button of the Options Panel is pressed.<br>
	 * Changes should only be written down, if this method is called.<br>
	 * The activation/deactivation of the plugin should be done last.
	 */
	public abstract void save();

	/**
	 * Sets the plugin to be enabled.
	 */
	public abstract void enablePlugin();

	/**
	 * Sets the plugin to be disabled.
	 */
	public abstract void disablePlugin();

	/**
	 * Returns if the set to be enabled.
	 */
	public abstract boolean isEnabled();

	/**
	 * Initializes the plugin. Is called if the sdave button was pressed and the plugin was set to be enabled.<br>
	 * For already enabled plugins this method will also be called. 
	 * @return <code>true</code> if the plugin was properly initialized, <code>false</code> if not.
	 */
	public abstract boolean initPlugin();
	
	/**
	 * Returns the display name of the plugin.
	 * @return Display Name.
	 */
	public abstract String getPluginName();
}
