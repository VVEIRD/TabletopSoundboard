package vv3ird.ESDSoundboardApp.player;

import java.io.File;

import vv3ird.ESDSoundboardApp.AudioApp;
import de.rcblum.stream.deck.event.KeyEvent;
import de.rcblum.stream.deck.items.AbstractStreamItem;
import de.rcblum.stream.deck.util.SDImage;

public class AAudioItem extends AbstractStreamItem {
	
	private String audioFile = null;

	public AAudioItem(SDImage img, String audioFile) {
		super(img);
		this.audioFile = audioFile;
		String[] sp = this.audioFile.split(File.separator + File.separator);
		String fileName = sp[sp.length-1];
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		this.setText(fileName);
	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		if (event.getType() == KeyEvent.Type.RELEASED_CLICKED) {
			System.out.println("Trigger: " + audioFile);
			AudioApp.playAmbience(this.audioFile);
		}
	}

}
