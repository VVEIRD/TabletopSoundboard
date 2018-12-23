package vv3ird.ESDSoundboardApp.plugins.listener;

import vv3ird.ESDSoundboardApp.plugins.data.Plugin;

public interface PluginListener {
	
	public void onEnable(Plugin plugin);
	
	public void onDisable(Plugin plugin);
}
