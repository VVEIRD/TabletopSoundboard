package vv3ird.ESDSoundboardApp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import vv3ird.ESDSoundboardApp.config.AppConfiguration;
import vv3ird.ESDSoundboardApp.config.AudioDevices;
import vv3ird.ESDSoundboardApp.config.Sound;
import vv3ird.ESDSoundboardApp.config.Sound.Type;
import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.player.AudioPlayer;
import vv3ird.ESDSoundboardApp.streamdeck.items.SoundBoardItemNew;
import vv3ird.ESDSoundboardApp.streamdeck.items.StatusAmbienceItem;
import vv3ird.ESDSoundboardApp.streamdeck.items.StopItem;
import vv3ird.ESDSoundboardApp.streamdeck.items.configuration.ConfigItem;
import de.rcblum.stream.deck.StreamDeckController;
import de.rcblum.stream.deck.device.IStreamDeck;
import de.rcblum.stream.deck.device.StreamDeckDevices;
import de.rcblum.stream.deck.items.PagedFolderItem;
import de.rcblum.stream.deck.items.StreamItem;
import de.rcblum.stream.deck.util.IconHelper;
import de.rcblum.stream.deck.util.SDImage;

public class AudioApp {
	
	private static Logger logger;
	
	private static AudioPlayer player = null;
	
	private static StreamDeckController controller = null;
	
	private static AppConfiguration configuration = null;
	
	public static List<Sound> soundLibrary = new LinkedList<>();
	
	public static List<SoundBoard> soundboardLibrary = new LinkedList<>();
	
	private static IStreamDeck streamDeck = null;
	
	static {
		try (InputStream in = AudioApp.class.getResourceAsStream("/resources/log4j.xml")) {
			Path tempConfig = File.createTempFile("audioapp", ".xml").toPath();
			Files.copy(in, tempConfig, StandardCopyOption.REPLACE_EXISTING);
			System.setProperty("log4j.configurationFile", tempConfig.toFile().getAbsolutePath());
			logger = LogManager.getLogger(AudioApp.class);
			logger.debug("Log4J config: " + tempConfig.toFile().getAbsolutePath());
			loadConfiguration();
			checkOldLibFormate();
			loadSoundLibrary();
			loadSoundBoards();
			Runtime.getRuntime().addShutdownHook(new Thread() {
			    public void run() {
			    	if(streamDeck != null) {
				    	streamDeck.reset();
				    	streamDeck.setBrightness(5);
			    	}
			    	if(AudioApp.controller != null)
			    		AudioApp.controller.stop(true, true); 
				}
			 });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static SDImage FOLDER_ICON = IconHelper.loadImageSafe("icons" + File.separator + "folder.png");
	public static SDImage AUDIO_ICON = IconHelper.loadImageSafe("icons" + File.separator + "audio.png");
	public static SDImage BACK_ICON = IconHelper.loadImageSafe("icons" + File.separator + "back.png");

	public final static StopItem STOP_ITEM = new StopItem();
	
	public final static StatusAmbienceItem AMBIENCE_STATUS_ITEM =  new StatusAmbienceItem();
		
	public static void playAmbience(Sound sound) {
		AudioPlayer player = new AudioPlayer(sound, configuration.getMixerInfo());
		STOP_ITEM.setRollingText(sound.name);
		try {
			player.open();
			float gain = linearToDecibel(configuration.masterGain) + 2;
			logger.debug("Setting gain for ambience player: " + gain);
			player.setGain(gain);
		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
			logger.error("Error opening audio file: " + player.getAudioFile());
			logger.error(e);
			e.printStackTrace();
		}
		player.loop(AudioPlayer.LOOP_FOREVER);
		if (AudioApp.crossfade())
			player.start();
		STOP_ITEM.setText("Stop");
		if (AudioApp.player != null && AudioApp.player.isActive()) {
			if (!AudioApp.crossfade()) {
				AudioApp.player.addLineListener(new LineListener() {
					@Override
					public void update(LineEvent event) {
						if  (event.getType() == LineEvent.Type.STOP) {
							player.start();
						}
					}
				});
			}
			AudioApp.player.stop(true);
		}
//		if (!configuration.crossfade)
//			player.start();
		AudioApp.player = player;
	}
	
	public static void playEffect(Sound sound) {
		AudioPlayer player = new AudioPlayer(sound, configuration.getMixerInfo());
		try {
			player.open();
			float gain = linearToDecibel(configuration.masterGain) + 2;
			logger.debug("Setting gain for effects player: " + gain);
			player.setGain(gain);
		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
			logger.error("Error opening audio file: " + player.getAudioFile());
			logger.error(e);
			e.printStackTrace();
		}
		player.loop(0);
		player.start();
	}

	public static void stop() {
		if(AudioApp.player != null) {
			AudioApp.player.stop(true);
			STOP_ITEM.setText("Start");
		}
	}

	public static void start() {
		if(AudioApp.player != null) {
			AudioApp.player.start();
			STOP_ITEM.setText("Stop");
		}
	}
	
	public static int getVolume() {
		return Math.round(configuration.masterGain * 100);
	}
	
	/**
	 * Set the volume of the app.
	 * @param vol Volume from 0 .. 100 %
	 */
	public static void setVolume(int vol) {
		vol = vol < 0 ? 0 : vol > 100 ? 100 : vol;
		configuration.masterGain = vol/100f;
		if(player != null) {
			float min = player.getMinGain();
			float max = player.getMaxGain();
			float gain = linearToDecibel(configuration.masterGain) + 2;
			gain = gain > max ? max : gain < min ? min : gain;
//			float gain = min + ((max-min)*configuration.masterGain);
			logger.debug("Setting gain for player: " + gain);
			player.setGain(gain);
		}
		saveConfig();
	}
	
	private static float linearToDecibel(float linear) {
		double dB;

		if (linear != 0)
			dB = 20.0f * Math.log10(linear);
		else
			dB = -144.0f;

		return (float) dB;
	}
	
	private static void loadConfiguration() {
		try {
			configuration = AppConfiguration.load(Paths.get("config.json"));
		} catch (IOException e) {
			logger.info("Create default configuration");
			configuration = new AppConfiguration();
			try {
				AppConfiguration.save(configuration, Paths.get("config.json"));
			} catch (IOException e1) {
				logger.error("Cannot create config, using transient default configuration.");
				e1.printStackTrace();
			}
		}
	}

	public static AppConfiguration getConfiguration() {
		if (configuration == null)
			loadConfiguration();
		return configuration;
	}

	private static void loadSoundBoards() {
		Path soundBoardRoot = configuration.getSoundBoardLibPath();
		logger.debug("SoundBoard root: " + soundBoardRoot.toString());
		File[] soundBoardDirs = soundBoardRoot.toFile().listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		if(soundBoardDirs != null) {
			for (File soundBoardDir : soundBoardDirs) {
				String name = soundBoardDir.getName();
				Path ambienceRoot = Paths.get(soundBoardDir.toString(), "ambience");
				Path effectsRoot = Paths.get(soundBoardDir.toString(), "effects");
				logger.debug("  SoundBoard: " + soundBoardDir.getName());
				logger.debug("    ambience root: " + ambienceRoot);
				logger.debug("    effects root: " + effectsRoot);
				Map<String, List<Sound>> ambience = loadSoundsForSoundBoard(ambienceRoot);
				Map<String, List<Sound>> effects = loadSoundsForSoundBoard(effectsRoot);
				for(String cat : effects.keySet()) {
					List<Sound> sounds = effects.get(cat);
					for (Sound sound : sounds) {
						logger.debug("Effect sound ("+ cat + "): " + sound.name);
					}
				}
				for(String cat : ambience.keySet()) {
					List<Sound> sounds = ambience.get(cat);
					for (Sound sound : sounds) {
						logger.debug("Ambience sound ("+ cat + "): " + sound.name);
					}
				}
				SoundBoard soundBoard = new SoundBoard(name, ambience, effects);
				soundboardLibrary.add(soundBoard);
			}
		}
	}
	
	private static Map<String, List<Sound>> loadSoundsForSoundBoard(Path root) {
		Map<String, List<Sound>> categories = new HashMap<>();
		try {
			Map<Path, List<Sound>> categoryP = Files.list(root)
					.filter(p -> Files.isDirectory(p))
					.collect(Collectors.toMap(Path::getFileName, AudioApp::loadSounds));
			Set<Path> ks = categoryP.keySet();
			for (Path path : ks) {
				categories.put(path.toString(), categoryP.get(path));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categories;
	}

	private static void loadSoundLibrary() {
		Path soundLibRoot = configuration.getSoundLibPath();
		logger.debug("SoundLib root: " + soundLibRoot.toString());
		soundLibrary.addAll(loadSounds(soundLibRoot));
	}

	private static List<Sound> loadSounds(Path root) {
		List<Sound> soundLibrary = new LinkedList<>();
		logger.debug("Sound root: " + root.toString());
		if(Files.exists(root) && Files.isDirectory(root)) {
			File[] soundPaths = root.toFile().listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getAbsolutePath().endsWith(".json");
				}
			});
			for (File soundPath : soundPaths) {
				logger.debug("Loading sound: " + soundPath.toString());
				String soundString;
				try {
					byte[] soundBytes = Files.readAllBytes(Paths.get(soundPath.toString()));
					soundString = new String(soundBytes, "UTF-8");
					Gson gson = new Gson();
					Sound sound = gson.fromJson(soundString, Sound.class);
					soundLibrary.add(sound);
				} catch (IOException e) {
					logger.error("Failed to load sound: " + soundPath.toString());
					e.printStackTrace();
				}
			}
		}
		else {
			try {
				Files.createDirectories(root);
			} catch (IOException e) {
				logger.error("Couldn't create folder \"" + root.toString() + "\"");
				logger.error(e);
			}
		}
		return soundLibrary;
	}
	
	public static void saveSoundBoard(SoundBoard sb) throws IOException {
		Path root = configuration.getSoundBoardLibPath();
		if (!Files.exists(root)) {
			Files.createDirectories(root);
		}
		Path soundBoardPath = Paths.get(root.toString(), sb.name);
		Path soundBoardPathBackup = Paths.get(root.toString(), "_"+sb.name);
		Path ambiencePath = Paths.get(soundBoardPath.toString(), "ambience");
		Path ambiencePathBackup = Paths.get(soundBoardPathBackup.toString(), "ambience");
		Path effectsPath = Paths.get(soundBoardPath.toString(), "effects");
		Path effectsPathBackup = Paths.get(soundBoardPathBackup.toString(), "effects");
		if(Files.exists(ambiencePath)) {
			Files.walk(ambiencePath)
		    .sorted(Comparator.reverseOrder())
		    .filter(Files::exists)
		    .forEach(t -> {
				try {
					Path backupPath = ambiencePathBackup.resolve(ambiencePath.relativize(t));
					if(!Files.exists(backupPath.getParent()))
						Files.createDirectories(backupPath.getParent());
					if(!Files.isDirectory(t))
						Files.copy(t, backupPath, StandardCopyOption.REPLACE_EXISTING);
					System.out.println(t);
					if(!t.equals(ambiencePath))
						Files.delete(t);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
		if(Files.exists(effectsPath)) {
			Files.walk(effectsPath)
		    .sorted(Comparator.reverseOrder())
		    .filter(Files::exists)
		    .forEach(t -> {
				try {
					Path backupPath = effectsPathBackup.resolve(effectsPath.relativize(t));
					if(!Files.exists(backupPath.getParent()))
						Files.createDirectories(backupPath.getParent());
					if(!Files.isDirectory(t))
						Files.copy(t, backupPath, StandardCopyOption.REPLACE_EXISTING);
					System.out.println(t);
					if(!t.equals(effectsPath))
						Files.delete(t);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
		Files.createDirectories(soundBoardPath);
		Files.createDirectories(ambiencePath);
		Files.createDirectories(effectsPath);
		for(String cat : sb.getCategories()) {
			Path ambienceCatPath = Paths.get(ambiencePath.toString(), cat);
			Path effectsCatPath = Paths.get(effectsPath.toString(), cat);
			Files.createDirectories(ambienceCatPath);
			Files.createDirectories(effectsCatPath);
			saveSounds(ambienceCatPath, sb.getAmbienceSounds(cat));
			saveSounds(effectsCatPath, sb.getEffectSounds(cat));
		}
		if (!soundboardLibrary.contains(sb)) {
			logger.debug("Adding soundboard: " + sb.name);
			soundboardLibrary.add(sb);
		}
		if(Files.exists(soundBoardPathBackup)) {
			Files.walk(soundBoardPathBackup)
		    .sorted(Comparator.reverseOrder())
		    .filter(Files::exists)
		    .forEach(t -> {
				try {
					Files.delete(t);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
		resetStreamDeckController();
	}
	
	private static void resetStreamDeckController() {
		logger.debug("Resetting controller");
		if(controller != null) {
			controller.stop(true, false);
		}
		StreamItem[] sbItems = new StreamItem[soundboardLibrary.size()];
		int sbC = 0;
		for (SoundBoard sb : soundboardLibrary) {
			sbItems[sbC++] = new SoundBoardItemNew(sb, null);
		}
		PagedFolderItem root = new PagedFolderItem("root", null, null, sbItems);
		AudioApp.addStatusBarItems(root, root.getChildren());
		controller = new StreamDeckController(streamDeck, root);
		logger.debug("Done resetting controller");
	}

	public static void deleteSoundboard(SoundBoard sb) throws IOException {
		Path root = configuration.getSoundBoardLibPath();
		Path soundBoardPath = Paths.get(root.toString(), sb.name);
		if(Files.exists(soundBoardPath)) {
			Files.walk(soundBoardPath)
		    .sorted(Comparator.reverseOrder())
		    .filter(Files::exists)
		    .forEach(t -> {
				try {
					Files.delete(t);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
		soundboardLibrary.remove(sb);
		resetStreamDeckController();
	}

	private static void saveSounds(Path root, List<Sound> sounds) {
		if(sounds == null)
			return;
		logger.debug("Sound root: " + root.toString());
		try {
			if(!Files.exists(root) || !Files.isDirectory(root)) {
					Files.createDirectories(root);
			}
			for (Sound sound : sounds) {
				Path soundPath = Paths.get(root.toString(), sound.name + ".json");
				logger.debug("Saving sound: " + soundPath.toString());
				String soundString;
				try {
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					soundString = gson.toJson(sound);
					byte[] soundBytes = soundString.getBytes("UTF-8");
					Files.write(soundPath, soundBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
				} catch (IOException e) {
					logger.error("Failed to save sound: " + soundPath.toString());
					logger.error(e);
				}
			}
		} catch (IOException e) {
			logger.error("Cannot create directory: " + root.toString() + ", not saving any files");
			logger.error(e);
		}
	}

	public static void addStatusBarItems(StreamItem parent, StreamItem[] items) {
		addStatusBarItems(parent, items, true);
	}

	public static void addStatusBarItems(StreamItem parent, StreamItem[] items, boolean withConfig) {
		Objects.requireNonNull(items);
		if (items.length < 15)
			throw new IndexOutOfBoundsException("Cannot add status bar to array, array must be >= 15");

		items[14] = AudioApp.STOP_ITEM;
//		items[13] = AudioApp.AMBIENCE_STATUS_ITEM; 
		if (withConfig)
			items[12] = new ConfigItem(parent);
	}

	/**
	 * Checks if old soud format is present and no new sound format, then converts it to the new.
	 */
	private static void checkOldLibFormate() {
		if (!Files.exists(configuration.getSoundBoardLibPath()) && Files.exists(Paths.get("sounds"))) {
			convertOldLib();
		}
	}
	
	/**
	 * Converts the old sound library format to the new format.
	 */
	public static void convertOldLib() {
		AppConfiguration appConfig = new AppConfiguration();
		Path soundLibPath = appConfig.getSoundLibPath();
		Path soundBoardLibPath = appConfig.getSoundBoardLibPath();
		Path audioIcon = Paths.get("icons", "audio.png");
		Path oldSounds = Paths.get("sounds");
		File[] categories = oldSounds.toFile().listFiles();
		for (File file : categories) {
			Type type = Type.AMBIENCE;
			if (file.isDirectory()) {
				String category = file.getName();
				// Create Category & subfolders
				Path ambPath = soundBoardLibPath.resolve("Default").resolve("ambience").resolve(category);
				Path effPath = soundBoardLibPath.resolve("Default").resolve("effects");
				logger.debug("Found category: " + category);
				File[] soundFiles = file.listFiles(new FilenameFilter() {public boolean accept(File dir, String name) {return name.endsWith(".mp3");}});
				for (File soundFile : soundFiles) {
					logger.debug("  Found sound: " + soundFile.getName());
					String name = soundFile.getName();
					name = name.substring(0, name.lastIndexOf('.'));
					Path source = soundFile.toPath();
					Path destFolder = soundLibPath.resolve(name);
					Path destAudio = soundLibPath.resolve(name).resolve(soundFile.getName());
					Path destSBSound = ambPath.resolve(name+".json");
					Path destSound = soundLibPath.resolve(name+".json");
					Path desticon= soundLibPath.resolve(name).resolve(name+".png");
					try {
						Files.createDirectories(destFolder);
						Files.createDirectories(ambPath);
						Files.createDirectories(effPath);
						Files.copy(source, destAudio, StandardCopyOption.REPLACE_EXISTING);
						Files.copy(audioIcon, desticon, StandardCopyOption.REPLACE_EXISTING);
						Sound sound = new Sound(name, destAudio.toString(), desticon.toString(), type);
						String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(sound);
						try {
							byte[] utf8JsonString = jsonString.getBytes("UTF-8");
							Files.write(destSound, utf8JsonString, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
							Files.write(destSBSound, utf8JsonString, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
						} catch (UnsupportedEncodingException e) {}
					} catch (IOException e) {
						logger.error("Cannot copy sound file: " + soundFile.getPath());
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static Sound saveNewSound(String name, BufferedImage icon, String audioFile, Sound.Type type) throws IOException {
		Path soundSource = Paths.get(audioFile);
		String extension = audioFile.substring(audioFile.lastIndexOf("."));
		Path soundLibPath = AudioApp.getConfiguration().getSoundLibPath();
		Path soundFolder = soundLibPath.resolve(name);
		Path soundJson = soundLibPath.resolve(name + ".json");
		Path soundFile = soundFolder.resolve(name + extension );
		Path soundIcon = soundFolder.resolve(name + ".png" );
		Files.createDirectories(soundFolder);
		Files.copy(soundSource, soundFile);
		ImageIO.write(icon, "PNG", soundIcon.toFile());
		Sound s = new Sound(name, soundFile.toString(), soundIcon.toString(), type);
		Sound.save(s, soundJson);
		soundLibrary.add(s);
		return s;
	}

	public static boolean crossfade() {
		return configuration.crossfade;
	}
	
	public static void setCrossfade(boolean cf) {
		configuration.crossfade = cf;
		saveConfig();
	}
	
	public static void setAudioInterface(Mixer.Info info) {
		if(AudioDevices.getAudioDeviceByName(info.getName(), false) != null) {
			configuration.setAudioInterface(info);
			if(player != null)
				playAmbience(player.getSound());
			saveConfig();
		}
	}

	private synchronized static void saveConfig() {
		try {
			AppConfiguration.save(configuration, Paths.get("config.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getFadeOutMs() {
		return configuration.fadeOutMs;
	}
	
	public static SoundBoard getSoundboard(String name) {
		return soundboardLibrary.stream().filter(s -> s.name.equalsIgnoreCase(name)).findFirst().orElse(null);
	}
	
	public static List<Sound> getSoundLibrary() {
		return soundLibrary;
	}
	
	public static List<Sound> getEffectSounds() {
		return soundLibrary.stream().filter(s -> s.type == Type.EFFECT).collect(Collectors.toList());
	}
	
	public static List<Sound> getAmbienceSounds() {
		return soundLibrary.stream().filter(s -> s.type == Type.AMBIENCE).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		StreamItem[] sbItems = new StreamItem[soundboardLibrary.size()];
		int sbC = 0;
		for (SoundBoard sb : soundboardLibrary) {
			sbItems[sbC++] = new SoundBoardItemNew(sb, null);
		}
		PagedFolderItem root = new PagedFolderItem("root", null, null, sbItems);
		AudioApp.addStatusBarItems(root, root.getChildren());
//		File lib = new File("sounds");
//		StreamItem[] categories = new StreamItem[15];
//		int catCount = 0;
//		File[] categorieFiles = lib.listFiles();
//		if (categorieFiles == null || categorieFiles.length == 0)
//			System.exit(4);
//		for (File catFile : categorieFiles) {
//			if (catFile.isDirectory()) {
//				File[] audios = catFile.listFiles();
//				AudioItem[] audioItems = new AudioItem[15];
//				int count = 0;
//				for (File file : audios) {
//					if (count >= 14)
//						break;
//					if (!file.getAbsolutePath().endsWith(".mp3"))
//						continue;
//					AudioItem audioItem = new AudioItem(AUDIO_ICON, file.getAbsolutePath());
//					audioItem.setTextPosition(StreamItem.TEXT_POS_CENTER);
//					audioItems[count++] = audioItem;
//					if(count == 4)
//						count++;
//				}
//				
//				FolderItem categoryitem = new FolderItem(catFile.getName(), null, audioItems);
//				categories[catCount++] = categoryitem;
//			}
//		}
//		categories[14] = STOP_ITEM ;
//		FolderItem root = new FolderItem("root", null, categories);
		streamDeck = StreamDeckDevices.getStreamDeck();
		if (streamDeck != null) {
			streamDeck.reset();
			streamDeck.addKeyListener(new DelayedReduceBrightness(streamDeck));
			StreamDeckController.setKeyDeadzone(50);
			AudioApp.controller = new StreamDeckController(streamDeck, root);
			AudioApp.controller.setBrightness(75);
		}
		else {
			logger.error("No Stream Deck Device found. Please check your devices connection");
			logger.error("Shutting down");
			System.exit(5);
		}
	}
}
