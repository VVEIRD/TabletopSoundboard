package vv3ird.ESDSoundboardApp.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import vv3ird.ESDSoundboardApp.AudioApp;

public class AppConfiguration {
	
	private static Logger logger = LogManager.getLogger(AppConfiguration.class);

	public String soundLib = "soundlib";
	
	public String soundBoardLib = "soundboards";
	
	public String audioInterface = null;
	
	public float masterGain = 1.0f;
	
	public int fadeOutMs = 1500;
	
	public boolean crossfade = true;
	
	public AppConfiguration() {
		this.setAudioInterface(AudioDevices.getDefaultAudioDevice());
	}
	
	public Path getSoundLibPath() {
		return Paths.get(this.soundLib);
	}
	
	public Path getSoundBoardLibPath() {
		return Paths.get(this.soundBoardLib);
	}
	
	public Mixer.Info getMixerInfo() {
		Mixer.Info audioInterface = null;
		if((audioInterface = AudioDevices.getAudioDeviceByName(this.audioInterface, false)) == null) {
			audioInterface = AudioDevices.getDefaultAudioDevice();
		}
		logger.debug("Selected Mixer: " + audioInterface.getName());
		return audioInterface;
	}
	
	public void setAudioInterface(Mixer.Info aInterface) {
		if(AudioDevices.getAudioDeviceByName(aInterface.getName(), false) != null)
			this.audioInterface = aInterface.getName();		
	}

	public static void save(AppConfiguration config, Path path) throws IOException {
		String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(config);
		try {
			byte[] utf8JsonString = jsonString.getBytes("UTF-8");
			Files.write(path, utf8JsonString, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (UnsupportedEncodingException e) {}
	}
	
	public static AppConfiguration load(Path path) throws IOException {
		AppConfiguration config = null;
		byte[] appConfigBytes = Files.readAllBytes(path);
		String appConfigString = new String(appConfigBytes, "UTF-8");
		logger.debug("Configuration:");
		logger.debug(appConfigString);
		Gson gson = new Gson();
		config = gson.fromJson(appConfigString, AppConfiguration.class);
		logger.debug(config);
		return config;
	}
	
	public static float linearToDecibel(float linear) {
		double dB;

		if (linear != 0)
			dB = 20.0f * Math.log10(linear);
		else
			dB = -144.0f;

		return (float) dB;
	}
	

}
