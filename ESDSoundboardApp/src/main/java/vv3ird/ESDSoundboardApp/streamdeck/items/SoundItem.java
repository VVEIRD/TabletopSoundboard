package vv3ird.ESDSoundboardApp.streamdeck.items;

import java.io.File;

import vv3ird.ESDSoundboardApp.config.Sound;
import vv3ird.ESDSoundboardApp.config.Sound.Type;
import vv3ird.ESDSoundboardApp.AudioApp;
import de.rcblum.stream.deck.event.KeyEvent;
import de.rcblum.stream.deck.items.AbstractStreamItem;
import de.rcblum.stream.deck.util.IconHelper;

public class SoundItem extends AbstractStreamItem {
	
	private Sound sound = null;

	public SoundItem(Sound sound) {
		super(IconHelper.loadImageSafe(sound.coverPath));
		this.sound = sound;
		String[] sp = this.sound.filePath.split(File.separator + File.separator);
		String fileName = sp[sp.length-1];
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		this.setTextPosition(TEXT_POS_CENTER);
		this.setText(sound.name);
	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		if (event.getType() == KeyEvent.Type.RELEASED_CLICKED) {
			System.out.println("Trigger: " + sound.name);
			System.out.println("Trigger: " + sound.filePath);
			if (this.sound.type == null || this.sound.type == Type.AMBIENCE)
				AudioApp.playAmbience(this.sound);
			else
				AudioApp.playEffect(this.sound);
		}
	}

}
