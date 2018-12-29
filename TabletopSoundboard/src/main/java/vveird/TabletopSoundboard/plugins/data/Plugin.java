package vveird.TabletopSoundboard.plugins.data;

import java.util.List;

import vveird.TabletopSoundboard.ngui.plugins.JPluginConfigurationPanel;
import vveird.TabletopSoundboard.plugins.listener.PluginListener;

/**
 * Plugins are used to extend the functionality of the soundboard app:<br>
 * A) Providing additional audio sources. (Not implemented yet)<br>
 * B) if a new sound is played the plugin can recieve notification and the Sound Object<br>
 * C) if the playback of a sound is stopped, the plugin can recieve notification and the Sound Object<br>
 * @author Roland von Werden
 *
 */
public interface Plugin {
	
	/**
	 * Initializes the Plugin. If isEnabled() returns false, this method should not
	 * initialize the plugin.
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception;
	
	/**
	 * Returns the display name of the plugin. Must be unique. Instances need an unique Display name, too.
	 * @return Name of the plugin/instance
	 */
	public String getDisplayName();
	
	/**
	 * Indicates if the plugin can have more than one configured instance.<br>
	 * Configuration should be done through the JPluginConfigurationPanel from getConfigurationPanel();<br>
	 * A default instance will be loaded and has to deliver the instances.<br>
	 * The default instance will not be considered for metadata templates<br>
	 * Only the default instance will be notified if the plugin is a PlaybackListener.<br> 
	 * @return <code>true</code> if the plugin can have more than one instance.
	 */
	public boolean isMultiInstance();
	
	/**
	 * Returns the instances of the plugin, if it is an multi-instance plugin. 
	 * @return List with all instances or <code>null</code> if the plugin does not support multi-instancing.
	 */
	public List<Plugin> getInstances();
	
	/**
	 * Returns the Configuration Panel for the Options Page. multiple instances have to be configured in one panel.
	 * @return
	 */
	public JPluginConfigurationPanel getConfigurationPanel();
	
	/**
	 * Retrurns if the plugin is enabled
	 * @return
	 */
	public boolean isEnabled();

	/**
	 * Enables the plugin. Resources should be loaded.
	 */
	public void enable();

	/**
	 * Disables the plugin. Resources should be unloaded.
	 */
	public void disable();

	/**
	 * Returns if the Plugin is properly configured.
	 * @return <code>true</code> if the plugin is able to work, <code>false</code> if not.
	 */
	public boolean isConfigured();
	
	/**
	 * Determines if the plugin should be used to listen to playback changes.<br>
	 * If the plugin supports multiple instances, only the default instance will be notified.<br>
	 * Distribution to the instances has to be done by the default instance.
	 * @return <code>true</code> if it should listen, <code>false</code> if not.
	 */
	public boolean isPlaybackListener();
	
	/**
	 * Adds a PluginListener to the plugin. The listeners should be notified properly.
	 * @param pl Listener to add.
	 */
	public void addPluginListener(PluginListener pl);

	/**
	 * Removes a PluginListener from the plugin. 
	 * @param pl Listener to remove.
	 */
	public void removePluginListener(PluginListener pl);
	
	/**
	 * Returns true if the plugin provides Metadata templates for Sound objects.
	 * @return <code>true</code> if the plugin has metadata templates for sound objects, {@link FloatCondSetOp} if not.
	 */
	public boolean hasSoundPluginMetadataTemplates();
	
	/**
	 * Returns the metadata templates for the plugin.
	 * @return List with all templates.
	 */
	public List<SoundPluginMetadataTemplate> getSoundPluginMetadataTemplates();
	
	/**
	 * Returns a list with all Lists for Metadata with the list type.
	 * @param metadataName name of the metadata
	 * @return List of values for the metadata or <code>null</code> if either ther is no list or the plugin is not configured or enabled.
	 */
	public List<String> getListForMetadata(SoundPluginMetadata metadataName);
}