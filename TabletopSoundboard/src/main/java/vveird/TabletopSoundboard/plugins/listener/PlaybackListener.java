package vveird.TabletopSoundboard.plugins.listener;

import vveird.TabletopSoundboard.config.Sound;

public interface PlaybackListener {

	public void onStart(Sound s);
	
	public void onStop(Sound s);
	
}
