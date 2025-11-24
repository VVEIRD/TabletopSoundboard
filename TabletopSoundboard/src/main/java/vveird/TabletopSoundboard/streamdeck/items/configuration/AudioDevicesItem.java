package vveird.TabletopSoundboard.streamdeck.items.configuration;

import java.util.Objects;

import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rcblum.stream.deck.event.KeyEvent;
import de.rcblum.stream.deck.event.KeyEvent.Type;
import de.rcblum.stream.deck.items.FolderItem;
import de.rcblum.stream.deck.items.StreamItem;
import de.rcblum.stream.deck.items.ToggleItem;
import de.rcblum.stream.deck.util.IconHelper;
import de.rcblum.stream.deck.util.SDImage;
import vveird.TabletopSoundboard.AudioApp;
import vveird.TabletopSoundboard.config.AudioDevices;

public class AudioDevicesItem extends FolderItem {
	
	Logger logger = LogManager.getLogger(AudioDevicesItem.class);
	
	private DeviceItem selectedItem = null;

	public AudioDevicesItem(StreamItem parent) {
		super(null, parent, new StreamItem[parent.getButtonCount()]);
		this.setButtonCount(parent.getButtonCount());
		this.setRowCount(parent.getRowCount());
		SDImage ic = IconHelper.loadImageFromResource("/icons/AudioDevice.png");
		if (ic == null)
			ic = IconHelper.getImage("temp://BLACK_ICON");
		this.setIcon(ic);
		this.setTextPosition(TEXT_POS_CENTER);
		this.setText("Audio Interface");
		setupChildren();
		AudioApp.addStatusBarItems(this, this.getChildren(), false);
	}

	private void setupChildren() {
		for(int i=0; i<this.getChildren().length; i++)
			this.getChildren()[i] = null;
		Mixer.Info[] devices = AudioDevices.getAvailableAudioDevices();
		Mixer.Info selectedDevice = AudioApp.getConfiguration().getMixerInfo();
		logger.debug("Selected audiointerface: " + selectedDevice.getName());
		int c = 0;
		for (Info info : devices) {
			DeviceItem di = new DeviceItem(info, info == selectedDevice ? true : false);
			if (di.isSelected())
				this.selectedItem = di;
			this.getChildren()[c++] = di;
			if (c == this.getColumnCount()-1) c++;
			// No more than X Speakers supported for now
			if (c > ((this.rowCount-1)*this.getColumnCount())-1) {
				if (selectedItem == null) {
					DeviceItem sdi = new DeviceItem(selectedDevice, true);
					this.getChildren()[0] = sdi;
				}
				break;
			}
		}
	}
	
	@Override
	public void onKeyEvent(KeyEvent event) {
		super.onKeyEvent(event);
		if(event.getType() == Type.PRESSED)
			setupChildren();
	}
	
	private void setSelected(DeviceItem item) {
		item = Objects.requireNonNull(item);
		if (this.selectedItem != null && this.selectedItem != item) {
			this.selectedItem.setSelected(false);
			this.selectedItem = item;
			AudioApp.setAudioInterface(item.device);
		}
	}
	
	public class DeviceItem extends ToggleItem {
		
		private Info device = null;

		public DeviceItem(Mixer.Info device, boolean selected) {
			super(IconHelper.loadImageFromResource("/icons/Speaker.png"), selected);
			setTextPosition(TEXT_POS_CENTER);
			setText(device.getName());
			this.device  = device;
		}

		@Override
		protected void onDisplay() {
			
		}

		@Override
		protected void onEnable(boolean byEvent) {
			AudioDevicesItem.this.setSelected(this);
		}

		@Override
		protected void onDisable(boolean byEvent) {
			if(byEvent)
				setSelected(true);
		}

	}

}
