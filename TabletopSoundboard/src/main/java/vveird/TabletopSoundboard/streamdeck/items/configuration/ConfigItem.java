package vveird.TabletopSoundboard.streamdeck.items.configuration;

import de.rcblum.stream.deck.items.FolderItem;
import de.rcblum.stream.deck.items.StreamItem;
import de.rcblum.stream.deck.util.IconHelper;
import de.rcblum.stream.deck.util.IconPackage;
import de.rcblum.stream.deck.util.SDImage;
import vveird.TabletopSoundboard.AudioApp;

public class ConfigItem extends FolderItem {

	public ConfigItem(StreamItem parent) {
		super(null, parent, new StreamItem[parent.getChildCount()]);
		this.setButtonCount(parent.getButtonCount());
		this.setRowCount(parent.getRowCount());
		int columnCount = parent.getButtonCount()/parent.getRowCount();
		SDImage ic = IconHelper.loadImageFromResource("/icons/gear.png");
		if (ic == null)
			ic = IconHelper.getImage("temp://BLACK_ICON");
		this.setIcon(ic);
		this.getChildren()[columnCount-2] = new CrossFadeItem();
		this.getChildren()[columnCount-2] = new NewCrossFadeItem();
		this.getChildren()[0] = MasterGainControl.instance.getVolumeUpItem();
		this.getChildren()[1] = MasterGainControl.instance.getVolumeDownItem();
		this.getChildren()[2] = MasterGainControl.instance.getVolumeDisplayItem();
		this.getChildren()[columnCount+2] = new AudioDevicesItem(this);
		this.getChildren()[(rowCount-1)*columnCount] = new QuitItem();
		
		AudioApp.addStatusBarItems(this, this.getChildren(), false);
	}

}
