package vveird.TabletopSoundboard.plugins.listener;

import vveird.TabletopSoundboard.plugins.data.Plugin;

public interface PluginListener {
	
	public void onEnable(Plugin plugin);
	
	public void onDisable(Plugin plugin);
}
