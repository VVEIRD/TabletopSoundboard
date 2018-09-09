package vv3ird.ESDSoundboardApp.player;

import java.io.File;

import vv3ird.ESDSoundboardApp.AudioApp;
import vv3ird.ESDSoundboardApp.config.Sound;
import de.rcblum.stream.deck.event.KeyEvent;
import de.rcblum.stream.deck.items.AbstractStreamItem;
import de.rcblum.stream.deck.util.SDImage;

public class AAudioItem extends AbstractStreamItem {
	
	private Sound audio = null;

	public AAudioItem(SDImage img, Sound audio) {
		super(img);
		this.audio = audio;
		String[] sp = this.audio.filePath.split(File.separator + File.separator);
		String fileName = sp[sp.length-1];
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		this.setText(fileName);
	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		if (event.getType() == KeyEvent.Type.RELEASED_CLICKED) {
			System.out.println("Trigger: " + audio.name + "(" + audio.filePath + ")");
			AudioApp.playAmbience(this.audio);
		}
	}

}
