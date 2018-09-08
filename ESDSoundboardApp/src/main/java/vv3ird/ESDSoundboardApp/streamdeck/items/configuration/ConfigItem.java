package vv3ird.ESDSoundboardApp.streamdeck.items.configuration;

import vv3ird.ESDSoundboardApp.AudioApp;
import de.rcblum.stream.deck.items.FolderItem;
import de.rcblum.stream.deck.items.StreamItem;
import de.rcblum.stream.deck.util.IconHelper;
import de.rcblum.stream.deck.util.IconPackage;
import de.rcblum.stream.deck.util.SDImage;

public class ConfigItem extends FolderItem {

	public ConfigItem(StreamItem parent) {
		super(null, parent, new StreamItem[15]);
		SDImage ic = IconHelper.loadImageFromResource("/resources/icons/gear.png");
		if (ic == null)
			ic = IconHelper.getImage("temp://BLACK_ICON");
		this.setIcon(ic);
		this.getChildren()[3] = new CrossFadeItem();
		this.getChildren()[3] = new NewCrossFadeItem();
		this.getChildren()[1] = MasterGainControl.instance.getVolumeDownItem();
		this.getChildren()[0] = MasterGainControl.instance.getVolumeUpItem();
		this.getChildren()[2] = MasterGainControl.instance.getVolumeDisplayItem();
		this.getChildren()[7] = new AudioDevicesItem(this);
		this.getChildren()[10] = new QuitItem();
		
		AudioApp.addStatusBarItems(this, this.getChildren(), false);
	}

}
