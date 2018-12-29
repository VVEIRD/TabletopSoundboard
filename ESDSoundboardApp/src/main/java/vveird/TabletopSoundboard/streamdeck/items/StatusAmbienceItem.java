package vveird.TabletopSoundboard.streamdeck.items;

import java.nio.file.Paths;

import de.rcblum.stream.deck.event.KeyEvent;
import de.rcblum.stream.deck.items.AbstractStreamItem;
import de.rcblum.stream.deck.util.IconHelper;

public class StatusAmbienceItem extends AbstractStreamItem{

	public StatusAmbienceItem() {
		super(IconHelper.loadImageSafe(Paths.get("icons", "status_ambience.png")));
	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		
	}

}
