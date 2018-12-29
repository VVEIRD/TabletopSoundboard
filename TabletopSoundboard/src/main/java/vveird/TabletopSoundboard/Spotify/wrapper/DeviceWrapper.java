package vveird.TabletopSoundboard.Spotify.wrapper;

import com.wrapper.spotify.model_objects.miscellaneous.Device;

public class DeviceWrapper {
	
	private Device device = null;

	public DeviceWrapper(Device device) {
		super();
		this.device = device;
	}
	
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	@Override
	public String toString() {
		return device.getName();
	}

}
