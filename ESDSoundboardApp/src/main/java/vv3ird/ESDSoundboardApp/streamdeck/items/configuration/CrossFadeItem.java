package vv3ird.ESDSoundboardApp.streamdeck.items.configuration;

import vv3ird.ESDSoundboardApp.AudioApp;
import de.rcblum.stream.deck.event.KeyEvent;
import de.rcblum.stream.deck.items.AbstractStreamItem;
import de.rcblum.stream.deck.util.IconHelper;

public class CrossFadeItem extends AbstractStreamItem {
	
	boolean isOn = false;

	public CrossFadeItem() {
		super(IconHelper.getImage("temp://BLACK_ICON"));
		this.setTextPosition(TEXT_POS_CENTER);
		this.setText("Cross Fade");
		if (isOn != AudioApp.getConfiguration().crossfade) {
			this.img = IconHelper.applyImage(this.img, IconHelper.getImageFromResource("/resources/icons/selected.png"));
		}
	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		switch (event.getType()) {
		// Update selected status if config has changed
		case ON_DISPLAY:
			if (this.isOn != AudioApp.crossfade()) {
				this.isOn = AudioApp.crossfade();
				if (this.isOn)
					this.img = IconHelper.applyImage(this.img, IconHelper.getImageFromResource("/resources/icons/selected.png"));
				else
					this.img = this.text != null ? IconHelper.addText(this.rawImg, this.text, this.textPos) : this.rawImg;
				this.fireIconUpdate(false);
			}
			break;
		// Switch Crossfade on/off
		case RELEASED_CLICKED:
			this.isOn = !this.isOn;
			AudioApp.setCrossfade(this.isOn);
			if (this.isOn)
				this.img = IconHelper.applyImage(this.img, IconHelper.getImageFromResource("/resources/icons/selected.png"));
			else
				this.img = this.text != null ? IconHelper.addText(this.rawImg, this.text, this.textPos) : this.rawImg;
			this.fireIconUpdate(false);
			break;
		default:
			break;
		}
	}

}
