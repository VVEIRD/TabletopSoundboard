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
		super(IconHelper.loadImageSafe(sound.getCoverPath()));
		this.sound = sound;
		this.setTextPosition(TEXT_POS_CENTER);
		this.setText(sound.getName());
	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		if (event.getType() == KeyEvent.Type.RELEASED_CLICKED) {
			System.out.println("Trigger: " + sound.getName());
			System.out.println("Trigger: " + sound.getCurrentFile());
			if (this.sound.getType() == null || this.sound.getType() == Type.AMBIENCE)
				AudioApp.playAmbience(this.sound);
			else
				AudioApp.playEffect(this.sound);
		}
	}

}
