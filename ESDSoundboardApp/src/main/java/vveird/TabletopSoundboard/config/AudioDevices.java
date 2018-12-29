package vveird.TabletopSoundboard.config;

import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;

public class AudioDevices {


	public static Mixer.Info[] getAvailableAudioDevices() {
		Mixer.Info[] infos = AudioSystem.getMixerInfo();
		List<Mixer.Info> availableDevicesList = new LinkedList<>();
		for (Info info : infos) {
			if (AudioSystem.getMixer(info).getSourceLineInfo().length > 0) {
				availableDevicesList.add(info);
			}
		}
		Mixer.Info[] availableDevices = new Mixer.Info[availableDevicesList.size()];
		for (int i = 0; i < availableDevices.length; i++) {
			availableDevices[i] = availableDevicesList.get(i);
		}
		return availableDevices;
	}
	

	public static Mixer.Info getAudioDeviceByName(String name, boolean caseSensitive) {
		Mixer.Info[] infos = AudioSystem.getMixerInfo();
		for (Info info : infos) {
			String devName = info.getName();
			if (AudioSystem.getMixer(info).getSourceLineInfo().length > 0 && (caseSensitive ? devName.equals(name) : devName .equalsIgnoreCase(name))) {
				return info;
			}
		}
		return null;
	}

	public static Mixer.Info getDefaultAudioDevice() {
		Mixer.Info[] infos = AudioSystem.getMixerInfo();
		for (Info info : infos) {
			if (AudioSystem.getMixer(info).getSourceLineInfo().length > 0 && info.getName().toLowerCase().contains("primary") && !info.getName().toLowerCase().contains("port")) {
				return info;
			}
		}
		for (Info info : infos) {
			if (AudioSystem.getMixer(info).getSourceLineInfo().length > 0) {
				return info;
			}
		}
		return null;
	}
	
}
