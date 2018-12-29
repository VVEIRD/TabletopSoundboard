package vveird.TabletopSoundboard.streamdeck.items.configuration;

import de.rcblum.stream.deck.event.KeyEvent;
import de.rcblum.stream.deck.event.KeyEvent.Type;
import de.rcblum.stream.deck.items.AbstractStreamItem;
import de.rcblum.stream.deck.util.IconHelper;
import vveird.TabletopSoundboard.AudioApp;

public class QuitItem extends AbstractStreamItem {

	public QuitItem() {
		super(IconHelper.BLACK_ICON);
		setTextPosition(TEXT_POS_CENTER);
		setText("Quit");
	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		if(event.getType() == Type.RELEASED_CLICKED) {
			AudioApp.stop();
			System.exit(0);
		}
	}

}
