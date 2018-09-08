package vv3ird.ESDSoundboardApp.streamdeck.items;

import vv3ird.ESDSoundboardApp.AudioApp;
import de.rcblum.stream.deck.event.KeyEvent;
import de.rcblum.stream.deck.items.AbstractStreamItem;
import de.rcblum.stream.deck.util.IconHelper;

public class StopItem extends AbstractStreamItem {

	public StopItem() {
		super(IconHelper.getImage("temp://BLACK_ICON"));
		this.setTextPosition(TEXT_POS_CENTER);
		this.setText("Stop");
	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		if (event.getType() == KeyEvent.Type.RELEASED_CLICKED) {
			if (this.getText().equals("Stop")) {
				AudioApp.stop();
				this.setText("Start");
			}
			else {
				AudioApp.start();
				this.setText("Stop");
			}
		}
	}

}
