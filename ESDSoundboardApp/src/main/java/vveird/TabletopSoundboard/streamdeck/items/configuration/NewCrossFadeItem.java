package vveird.TabletopSoundboard.streamdeck.items.configuration;

import de.rcblum.stream.deck.items.ToggleItem;
import vveird.TabletopSoundboard.AudioApp;

public class NewCrossFadeItem extends ToggleItem {
	
	public NewCrossFadeItem() {
		super(AudioApp.crossfade());
		this.setTextPosition(TEXT_POS_CENTER);
		this.setText("Crossfade");
	}

	@Override
	protected void onDisplay() {
		if (this.isSelected() != AudioApp.crossfade())
			this.setSelected(AudioApp.crossfade());
	}

	@Override
	protected void onEnable(boolean byEvent) {
		if (!AudioApp.crossfade())
			AudioApp.setCrossfade(true);
	}

	@Override
	protected void onDisable(boolean byEvent) {
		if (AudioApp.crossfade())
			AudioApp.setCrossfade(false);
	}

}
