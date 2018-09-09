package test.vv3ird.BetterSoundboardApp.player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vv3ird.ESDSoundboardApp.config.AppConfiguration;
import vv3ird.ESDSoundboardApp.player.AudioPlayer;
import vv3ird.ESDSoundboardApp.AudioApp;

public class TestAudioPlayer {
	
	static {
		try (InputStream in = AudioApp.class.getResourceAsStream("/resources/log4j.xml")) {
			Path tempConfig = File.createTempFile("audioapp", ".xml").toPath();
			Files.copy(in, tempConfig, StandardCopyOption.REPLACE_EXISTING);
			System.setProperty("log4j.configurationFile", tempConfig.toFile().getAbsolutePath());
			Logger logger = LogManager.getLogger(AudioApp.class);
			logger.debug("Log4J config: " + tempConfig.toFile().getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws InterruptedException, IOException, LineUnavailableException, UnsupportedAudioFileException {
//		System.out.println(1);
//		AudioPlayer player = new AudioPlayer("C:\\\\Users\\\\vv3ird\\\\eclipse-workspace\\\\BetterSoundboardApp\\\\soundlib\\\\Bloodstarved Beast\\\\Bloodstarved Beast.mp3", new AppConfiguration().getMixerInfo());
//		System.out.println(2);
//		player.loop(AudioPlayer.LOOP_FOREVER);
//		System.out.println(3);
//		player.setGain(0);
//		System.out.println(4);
//		player.start();
//		float gain = 0;
////		for (int i = 0; i < 40 ; i++) {
////			player.setGain(--gain);
////			Thread.sleep(500);
////		}
//		Thread.sleep(5_000);
//		player.stop(true);
		
	}
}
