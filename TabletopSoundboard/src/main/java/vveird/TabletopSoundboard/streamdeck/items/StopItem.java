package vveird.TabletopSoundboard.streamdeck.items;

import de.rcblum.stream.deck.event.KeyEvent;
import de.rcblum.stream.deck.items.AbstractStreamItem;
import de.rcblum.stream.deck.items.animation.AnimationStack;
import de.rcblum.stream.deck.util.IconHelper;
import vveird.TabletopSoundboard.AudioApp;

public class StopItem extends AbstractStreamItem {

	public StopItem() {
		super(IconHelper.getImage("temp://BLACK_ICON"));
		this.setTextPosition(TEXT_POS_CENTER);
		this.setText("Stop");
	}
	
	public void setRollingText(String text) {
		if (text != null) {
			AnimationStack animation = IconHelper.createRollingTextAnimation(this.rawImg, text, TEXT_POS_BOTTOM);
			this.setAnimation(null);
			this.setAnimation(animation);
		}
		else {
			this.setAnimation(null);
		}
		
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
