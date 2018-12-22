package vv3ird.ESDSoundboardApp.plugins.listener;

import java.util.List;

import vv3ird.ESDSoundboardApp.ngui.pages.Page;
import vv3ird.ESDSoundboardApp.ngui.plugins.JPluginConfigurationPanel;
import vv3ird.ESDSoundboardApp.plugins.data.SoundPluginMetadataTemplate;

public interface Plugin {
	
	public void init() throws Exception;
	
	public String getDisplayName();
	
	public JPluginConfigurationPanel getConfigurationPanel();
	
	public boolean isEnabled();

	public void enable();
	
	public void disable();

	public boolean isConfigured();
	
	public boolean isPlaybackListener();
	
	public void addPluginListener(PluginListener pl);
	
	public void removePluginListener(PluginListener pl);
	
	public boolean hasSoundPluginMetadataTemplates();
	
	public List<SoundPluginMetadataTemplate> getSoundPluginMetadataTemplates();
	
	public List<String> getListForMetadata(String metadataName);
}