package vv3ird.ESDSoundboardApp.plugins.listener;

import vv3ird.ESDSoundboardApp.config.Sound;

public interface PlaybackListener {

	public void onStart(Sound s);
	
	public void onStop(Sound s);
	
}
