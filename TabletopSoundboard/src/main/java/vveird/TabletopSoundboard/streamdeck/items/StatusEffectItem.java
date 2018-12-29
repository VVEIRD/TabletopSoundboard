package vveird.TabletopSoundboard.streamdeck.items;

import java.nio.file.Paths;

import de.rcblum.stream.deck.event.KeyEvent;
import de.rcblum.stream.deck.items.AbstractStreamItem;
import de.rcblum.stream.deck.util.IconHelper;

public class StatusEffectItem extends AbstractStreamItem{

	public StatusEffectItem() {
		super(IconHelper.loadImageSafe(Paths.get("icons", "status_effect.png")));
	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		
	}

}
