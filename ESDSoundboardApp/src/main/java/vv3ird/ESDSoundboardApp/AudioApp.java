package vv3ird.ESDSoundboardApp;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.model_objects.specification.Image;
import com.wrapper.spotify.model_objects.specification.Playlist;

import vv3ird.ESDSoundboardApp.Spotify.SpotifyFrontend;
import vv3ird.ESDSoundboardApp.config.AppConfiguration;
import vv3ird.ESDSoundboardApp.config.AudioDevices;
import vv3ird.ESDSoundboardApp.config.Sound;
import vv3ird.ESDSoundboardApp.config.Sound.Type;
import vv3ird.ESDSoundboardApp.ngui.pages.JCreateSoundPage;
import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.player.AudioPlayer;
import vv3ird.ESDSoundboardApp.plugins.PluginManager;
import vv3ird.ESDSoundboardApp.plugins.data.MetadataStore;
import vv3ird.ESDSoundboardApp.plugins.data.SoundPluginMetadata;
import vv3ird.ESDSoundboardApp.plugins.listener.PlaybackListener;
import vv3ird.ESDSoundboardApp.streamdeck.items.SoundBoardItem;
import vv3ird.ESDSoundboardApp.streamdeck.items.StatusAmbienceItem;
import vv3ird.ESDSoundboardApp.streamdeck.items.StopItem;
import vv3ird.ESDSoundboardApp.streamdeck.items.configuration.ConfigItem;
import de.rcblum.stream.deck.StreamDeckController;
import de.rcblum.stream.deck.device.IStreamDeck;
import de.rcblum.stream.deck.device.SoftStreamDeck;
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

	public static Map<String, SoundBoard> soundboardLibrary = new HashMap<>();
	
	private static List<PlaybackListener> playbackListeners = new LinkedList<>();

	private static IStreamDeck streamDeck = null;
	
	private static JFrame splashscreen = null;

	static {
		try (InputStream in = AudioApp.class.getResourceAsStream("/resources/log4j.xml")) {
			Path tempConfig = File.createTempFile("audioapp", ".xml").toPath();
			Files.copy(in, tempConfig, StandardCopyOption.REPLACE_EXISTING);
			System.setProperty("log4j.configurationFile", tempConfig.toFile().getAbsolutePath());
			logger = LogManager.getLogger(AudioApp.class);
			logger.debug("Log4J config: " + tempConfig.toFile().getAbsolutePath());
			if(!GraphicsEnvironment.isHeadless()) {
				BufferedImage b = ImageIO.read(AudioApp.class.getResourceAsStream("/resources/splashscreen.png"));
				splashscreen = new JFrame();
				splashscreen.setSize(b.getWidth(), b.getHeight());
				splashscreen.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2 - b.getWidth()/2,
						Toolkit.getDefaultToolkit().getScreenSize().height/2 - b.getHeight()/2
				);
				splashscreen.setLayout(null);
				JLabel l = new JLabel(new ImageIcon(b));
				l.setBounds(0, 0, b.getWidth(), b.getHeight());
				splashscreen.add(l);
				splashscreen.setUndecorated(true);
				splashscreen.setAlwaysOnTop(true);
				splashscreen.setVisible(true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final static StopItem STOP_ITEM = new StopItem();

	public final static StatusAmbienceItem AMBIENCE_STATUS_ITEM = new StatusAmbienceItem();

	public static void playAmbience(Sound sound) {
		sound.resetCurrentFile();
		AudioPlayer player = new AudioPlayer(sound, configuration.getMixerInfo());
		addPlaybackListeners(player);
		STOP_ITEM.setRollingText(sound.getName());
		try {
			player.open();
		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
			logger.error("Error opening audio file: " + player.getAudioFile(), e);
		}
		float gain = linearToDecibel(configuration.masterGain) + 2;
		logger.debug("Setting gain for ambience player: " + gain);
		player.setGain(gain);
		player.loop(AudioPlayer.LOOP_FOREVER);
		if (AudioApp.crossfade())
			player.start();
		STOP_ITEM.setText("Stop");
		if (AudioApp.player != null && AudioApp.player.isActive()) {
			if (!AudioApp.crossfade()) {
				AudioApp.player.addLineListener(new LineListener() {
					@Override
					public void update(LineEvent event) {
						if (event.getType() == LineEvent.Type.STOP) {
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
		player.setGain(gain);
	}

	public static void playEffect(Sound sound) {
		AudioPlayer player = new AudioPlayer(sound, configuration.getMixerInfo());
		addPlaybackListeners(player);
		try {
			player.open();
			float gain = linearToDecibel(configuration.masterGain) + 2;
			
			logger.debug("Setting gain for effects player: " + gain);
			player.setGain(gain);
		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
			logger.error("Error opening audio file: " + player.getAudioFile(), e);
			e.printStackTrace();
		}
		player.loop(0);
		player.start();
	}

	public static void stop() {
		if (AudioApp.player != null) {
			AudioApp.player.stop(true);
			STOP_ITEM.setText("Start");
		}
	}

	public static void start() {
		if (AudioApp.player != null) {
			AudioApp.player.start();
			STOP_ITEM.setText("Stop");
		}
	}

	public static int getVolume() {
		return Math.round(configuration.masterGain * 100);
	}

	/**
	 * Set the volume of the app.
	 * 
	 * @param vol Volume from 0 .. 100 %
	 */
	public static void setVolume(int vol) {
		vol = vol < 0 ? 0 : vol > 100 ? 100 : vol;
		configuration.masterGain = vol / 100f;
		if (player != null) {
//			float min = player.getMinGain();
//			float max = player.getMaxGain();
			float gain = linearToDecibel(configuration.masterGain) ;
//			gain = gain > max ? max : gain < min ? min : gain;
//			float gain = min + ((max-min)*configuration.masterGain);
			logger.debug("Setting gain for player: " + gain);
			player.setGain(gain);
		}
		saveConfig();
	}

	public static void setSpotifyVolume(int vol) {
		if(isSpotifyEnabled()) {
			SpotifyFrontend sf = getSpotifyFrontend();
			sf.setVolumeForUsersPlayback(vol);
		}
	}
	

	public static boolean setSpotifyConfiguration(String clientId, String clientSecret, String responseUrl) {
		boolean loggedIn = false;
		configuration.spotifyClientId = clientId;
		configuration.spotifyClientSecret = clientSecret;
		configuration.spotifyResponseUrl = responseUrl;
		saveConfig();
		return loggedIn;
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
				logger.error("Cannot create config, using transient default configuration.", e1);
			}
		}
	}
	
	public static boolean isSpotifyEnabled() {
		if(configuration.spotifyClientId != null && configuration.spotifyClientSecret != null && configuration.spotifyResponseUrl != null) {
			SpotifyFrontend sf = SpotifyFrontend.createInstance(configuration.spotifyClientId, configuration.spotifyClientSecret, configuration.spotifyResponseUrl, true);
			return sf.isLoggedIn();
		}
		return false;
	}
	
	public static boolean isSpotifyPlaying() {
		if(isSpotifyEnabled()) {
			SpotifyFrontend sf = getSpotifyFrontend();
			CurrentlyPlayingContext cp = sf.getInformationAboutUsersCurrentPlayback();
			return cp != null && cp.getIs_playing();
		}
		return false;
	}
	
	private static String lastSpotifyId = null;

	public static void playSpotifyPlaylist(String spotifyOwner, String spotifyId) {
		if(isSpotifyEnabled()) {
			SpotifyFrontend sf = getSpotifyFrontend();
			sf.startResumeUsersPlaybackPlaylist(spotifyId);
			lastSpotifyId = spotifyId;
			sf.setVolumeForUsersPlayback((int)(100*configuration.masterGain));
		}
	}
	
	/**
	 * Returns the last played Spotify Id of the playlist.
	 * 
	 * @return The spotify id of the Playlist or null, if no playlist was played yet
	 */
	public static String getCurrentSpotifyId() {
		return lastSpotifyId;
	}

	/**
	 * Pauses the spotify playback
	 */
	public static void pauseSpotifyPlayback() {
		if(isSpotifyEnabled()) {
			SpotifyFrontend sf = getSpotifyFrontend();
			sf.pauseUsersPlayback();
		}
	}
	
	/**
	 * Returns the frontend for spotify
	 * @return {@link SpotifyFrontend} if spotify was configured, <code>null</code> if spotify has not been configured.
	 */
	public static SpotifyFrontend getSpotifyFrontend() {
		if(configuration.spotifyClientId != null && configuration.spotifyClientSecret != null && configuration.spotifyResponseUrl != null) {
			SpotifyFrontend sf = SpotifyFrontend.createInstance(configuration.spotifyClientId, configuration.spotifyClientSecret, configuration.spotifyResponseUrl, true);
			return sf;
		}
		return null;
	}

	/**
	 * @return Returns the configuration of the app.
	 */
	public static AppConfiguration getConfiguration() {
		if (configuration == null)
			loadConfiguration();
		return configuration;
	}

	/**
	 * Loads all soundboards from the filesystem
	 */
	private static void loadSoundBoards() {
		Path soundBoardRoot = configuration.getSoundBoardLibPath();
		logger.debug("SoundBoard root: " + soundBoardRoot.toString());
		File[] soundBoardDirs = soundBoardRoot.toFile().listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		if (soundBoardDirs != null) {
			for (File soundBoardDir : soundBoardDirs) {
				if (Files.isDirectory(soundBoardDir.toPath()) && soundBoardDir.list().length == 0) {
					soundBoardDir.delete();
					continue;
				}
				String name = soundBoardDir.getName();
				Path ambienceRoot = Paths.get(soundBoardDir.toString(), "ambience");
				Path effectsRoot = Paths.get(soundBoardDir.toString(), "effects");
				logger.debug("  SoundBoard: " + soundBoardDir.getName());
				logger.debug("    ambience root: " + ambienceRoot);
				logger.debug("    effects root: " + effectsRoot);
				Map<String, List<Sound>> ambience = loadSoundsForSoundBoard(ambienceRoot);
				Map<String, List<Sound>> effects = loadSoundsForSoundBoard(effectsRoot);
				for (String cat : effects.keySet()) {
					List<Sound> sounds = effects.get(cat);
					for (Sound sound : sounds) {
						logger.debug("Effect sound (" + cat + "): " + sound.getName());
					}
				}
				for (String cat : ambience.keySet()) {
					List<Sound> sounds = ambience.get(cat);
					for (Sound sound : sounds) {
						logger.debug("Ambience sound (" + cat + "): " + sound.getName());
					}
				}
				SoundBoard soundBoard = new SoundBoard(name, ambience, effects);
				soundboardLibrary.put(soundBoard.name, soundBoard);
			}
		}
	}

	/**
	 * Loads all Sounds for a certain soundboard.
	 * @param root Directory in which the sound configuration files are stored
	 * @return Map with the soundboard categories as key and a List of {@link Sound} Objects.
	 */
	private static Map<String, List<Sound>> loadSoundsForSoundBoard(Path root) {
		Map<String, List<Sound>> categories = new HashMap<>();
		try {
			Map<Path, List<Sound>> categoryP = Files.list(root).filter(p -> Files.isDirectory(p))
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

	/**
	 * Loads all Sounds that where added to the app.
	 */
	private static void loadSoundLibrary() {
		Path soundLibRoot = configuration.getSoundLibPath();
		logger.debug("SoundLib root: " + soundLibRoot.toString());
		soundLibrary.addAll(loadSounds(soundLibRoot));
	}

	/**
	 * load a list of {@link Sound} Objects stored as json files on disk. 
	 * @param root Folder where the json-files are stored.
	 * @return List of {@link Sound} Objects that were added previously to the app. 
	 */
	private static List<Sound> loadSounds(Path root) {
		List<Sound> soundLibrary = new LinkedList<>();
		logger.debug("Sound root: " + root.toString());
		if (Files.exists(root) && Files.isDirectory(root)) {
			File[] soundPaths = root.toFile().listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getAbsolutePath().endsWith(".json");
				}
			});
			for (File soundPath : soundPaths) {
				logger.debug("Loading sound: " + soundPath.toString());
				String soundString;
				try (FileInputStream fin = new FileInputStream(soundPath)) {
					byte[] soundBytes = fin.readAllBytes();
					fin.close();
					soundString = new String(soundBytes, "UTF-8");
					Gson gson = new Gson();
					Sound sound = gson.fromJson(soundString, Sound.class);
					soundLibrary.add(sound);
				} catch (Exception e) {
					logger.error("Failed to load sound: " + soundPath.toString(), e);
				}
			}
		} else {
			try {
				Files.createDirectories(root);
			} catch (IOException e) {
				logger.error("Couldn't create folder \"" + root.toString() + "\"", e);
			}
		}
		return soundLibrary;
	}

	/**
	 * Saves a soundboard to disk.<br>
	 * Creates a folderstructure that resembles the sounndboard:<br>
	 * SoundBoardname/<br>
	 * __ambience/<br>
	 * ____Ambience Sound 1.json<br>
	 * ____Ambience Sound 2.json<br>
	 * __effects/<br>
	 * ____Effect Sound 1.json<br>
	 * ____Effect Sound 2.json<br>
	 * @param sb
	 * @throws IOException
	 */
	public static void saveSoundBoard(SoundBoard sb) throws IOException {
		Path root = configuration.getSoundBoardLibPath();
		if (!Files.exists(root)) {
			Files.createDirectories(root);
		}
		Path soundBoardPath = Paths.get(root.toString(), sb.name);
		Path soundBoardPathBackup = Paths.get(root.toString(), "_" + sb.name);
		Path ambiencePath = Paths.get(soundBoardPath.toString(), "ambience");
		Path ambiencePathBackup = Paths.get(soundBoardPathBackup.toString(), "ambience");
		Path effectsPath = Paths.get(soundBoardPath.toString(), "effects");
		Path effectsPathBackup = Paths.get(soundBoardPathBackup.toString(), "effects");
		if (Files.exists(ambiencePath)) {
			Files.walk(ambiencePath).sorted(Comparator.reverseOrder()).filter(Files::exists).forEach(t -> {
				try {
					Path backupPath = ambiencePathBackup.resolve(ambiencePath.relativize(t));
					if (!Files.exists(backupPath.getParent()))
						Files.createDirectories(backupPath.getParent());
					if (!Files.isDirectory(t))
						Files.copy(t, backupPath, StandardCopyOption.REPLACE_EXISTING);
					// System.out.println(t);
					if (!t.equals(ambiencePath))
						Files.delete(t);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
		if (Files.exists(effectsPath)) {
			Files.walk(effectsPath).sorted(Comparator.reverseOrder()).filter(Files::exists).forEach(t -> {
				try {
					Path backupPath = effectsPathBackup.resolve(effectsPath.relativize(t));
					if (!Files.exists(backupPath.getParent()))
						Files.createDirectories(backupPath.getParent());
					if (!Files.isDirectory(t))
						Files.copy(t, backupPath, StandardCopyOption.REPLACE_EXISTING);
					// System.out.println(t);
					if (!t.equals(effectsPath))
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
		for (String cat : sb.getCategories()) {
			Path ambienceCatPath = Paths.get(ambiencePath.toString(), cat);
			Path effectsCatPath = Paths.get(effectsPath.toString(), cat);
			Files.createDirectories(ambienceCatPath);
			Files.createDirectories(effectsCatPath);
			saveSounds(ambienceCatPath, sb.getAmbienceSounds(cat));
			saveSounds(effectsCatPath, sb.getEffectSounds(cat));
		}
		logger.debug("Adding soundboard: " + sb.name);
		soundboardLibrary.put(sb.name, sb);
		if (Files.exists(soundBoardPathBackup)) {
			Files.walk(soundBoardPathBackup).sorted(Comparator.reverseOrder()).filter(Files::exists).forEach(t -> {
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

	/**
	 * Resets the root folder of the StreamController
	 */
	private static void resetStreamDeckController() {
		logger.debug("Resetting controller");
		if (controller != null) {
			// controller.stop(true, false);

			StreamItem[] sbItems = new StreamItem[soundboardLibrary.size()];
			int sbC = 0;
			for (SoundBoard sb : soundboardLibrary.values()) {
				sbItems[sbC++] = new SoundBoardItem(sb, null);
			}
			PagedFolderItem root = new PagedFolderItem("root", null, null, sbItems, streamDeck.getKeySize());
			AudioApp.addStatusBarItems(root, root.getChildren());
			controller.setRoot(root);
			// controller = new StreamDeckController(streamDeck, root);
			logger.debug("Done resetting controller");
		}
	}

	/**
	 * Deletes a Soundboard from the app and disk.
	 * @param sb SoundBoard to delete
	 * @throws IOException 
	 */
	public static void deleteSoundboard(SoundBoard sb) throws IOException {
		Path root = configuration.getSoundBoardLibPath();
		Path soundBoardPath = Paths.get(root.toString(), sb.name);

		if (Files.exists(soundBoardPath)) {
			deleteFolder(soundBoardPath.toFile());
		}
		soundboardLibrary.remove(sb.name);
		resetStreamDeckController();
	}

	/**
	 * Delets a folder from disk.
	 * @param folder
	 */
	public static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					boolean suc = f.delete();
					if (!suc)
						f.deleteOnExit();
				}
			}
		}
		boolean suc = folder.delete();
		if (!suc)
			folder.deleteOnExit();
	}

	/**
	 * Saves a sound to the sound library
	 * @param root Sound library root directory
	 * @param sounds Sound to be saved to the lib.
	 */
	private static void saveSounds(Path root, List<Sound> sounds) {
		if (sounds == null)
			return;
		logger.debug("Sound root: " + root.toString());
		try {
			if (!Files.exists(root) || !Files.isDirectory(root)) {
				Files.createDirectories(root);
			}
			for (Sound sound : sounds) {
				String name = sound.getName();
				name = name.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
				Path soundPath = Paths.get(root.toString(), name + ".json");
				logger.debug("Saving sound: " + soundPath.toString());
				String soundString;
				try {
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					soundString = gson.toJson(sound);
					byte[] soundBytes = soundString.getBytes("UTF-8");
					Files.write(soundPath, soundBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
				} catch (IOException e) {
					logger.error("Failed to save sound: " + soundPath.toString(), e);
				}
			}
		} catch (IOException e) {
			logger.error("Cannot create directory: " + root.toString() + ", not saving any files", e);
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
	 * Checks if old soud format is present and no new sound format, then converts
	 * it to the new.
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
				File[] soundFiles = file.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.endsWith(".mp3");
					}
				});
				for (File soundFile : soundFiles) {
					logger.debug("  Found sound: " + soundFile.getName());
					String name = soundFile.getName();
					name = name.substring(0, name.lastIndexOf('.'));
					Path source = soundFile.toPath();
					Path destFolder = soundLibPath.resolve(name);
					Path destAudio = soundLibPath.resolve(name).resolve(soundFile.getName());
					Path destSBSound = ambPath.resolve(name + ".json");
					Path destSound = soundLibPath.resolve(name + ".json");
					Path desticon = soundLibPath.resolve(name).resolve(name + ".png");
					try {
						Files.createDirectories(destFolder);
						Files.createDirectories(ambPath);
						Files.createDirectories(effPath);
						Files.copy(source, destAudio, StandardCopyOption.REPLACE_EXISTING);
						Files.copy(audioIcon, desticon, StandardCopyOption.REPLACE_EXISTING);
						Sound sound = new Sound(name, destAudio.toString(), desticon.toString(), type, null);
						String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(sound);
						try {
							byte[] utf8JsonString = jsonString.getBytes("UTF-8");
							Files.write(destSound, utf8JsonString, StandardOpenOption.CREATE,
									StandardOpenOption.TRUNCATE_EXISTING);
							Files.write(destSBSound, utf8JsonString, StandardOpenOption.CREATE,
									StandardOpenOption.TRUNCATE_EXISTING);
						} catch (UnsupportedEncodingException e) {
						}
					} catch (IOException e) {
						logger.error("Cannot copy sound file: " + soundFile.getPath(), e);
					}
				}
			}
		}
	}

	public static Sound saveSound(Sound s)
			throws IOException {
		if(!s.isSpotifySound()) {
			String audioFile = s.next();
			String name = s.getName();
			BufferedImage icon = s.getCover();
			Map<String, List<SoundPluginMetadata>> metadata = s.getMetadata();
			Path soundSource = Paths.get(audioFile);
			String extension = audioFile.substring(audioFile.lastIndexOf("."));
			Path soundLibPath = AudioApp.getConfiguration().getSoundLibPath();
			Path soundFolder = soundLibPath.resolve(name);
			Path soundJson = soundLibPath.resolve(name + ".json");
			Path soundFile = soundFolder.resolve(name + extension);
			Path soundIcon = soundFolder.resolve(name + ".png");
			Files.createDirectories(soundFolder);
			if(!soundSource.toAbsolutePath().equals(soundFile.toAbsolutePath()))
				Files.copy(soundSource, soundFile);
			ImageIO.write(icon, "PNG", soundIcon.toFile());
//			Sound s = new Sound(name, soundFile.toString(), soundIcon.toString(), type, tags);
			metadata.keySet().stream().forEach(c -> s.addMetadataFor(c, metadata.get(c)));
			s.setCoverPath(soundIcon.toString());
			s.setFilePaths(new String[] {soundFile.toString()});
			Sound.save(s, soundJson);
			Sound sls = soundLibrary.stream().filter(l -> s.getName().equals(l.getName())).findFirst().orElse(null);
			soundLibrary.remove(sls);
			soundLibrary.add(s);
			if(sls != null)
				updateSoundBoards(s);
			Collections.sort(soundLibrary);
			return s;
		} 
		else if (s.isSpotifySound()){
			AudioApp.storeMetadata("SPOTIFY_" + s.getSpotifyId(), s.getMetadata());
			Path cachFolder = Paths.get("cache", "spotify", s.getSpotifyOwner(), s.getSpotifyType());
			// Save cover image to cache folder
			try {
				if (!Files.exists(cachFolder))
					Files.createDirectories(cachFolder);
				Path imagePath = cachFolder.resolve(s.getSpotifyId() + ".png");
				// cache spotify cover
				SpotifyFrontend sf = SpotifyFrontend.createInstance(configuration.spotifyClientId, configuration.spotifyClientSecret, configuration.spotifyResponseUrl, true);
				Playlist pl = sf.getPlaylist(s.getSpotifyId());
				Image[] is = pl.getImages();
				if(is.length > 0) {
					BufferedImage img = ImageIO.read(new URL(is[0].getUrl()));
					ImageIO.write(img, "PNG", imagePath.toFile());
				}
			}
			catch (IOException e) {
				logger.error("Error saving cover to spotify cache", e);
			}
		}
		return null;
	}

	private static void updateSoundBoards(Sound s) {
		for (String soundboard : soundboardLibrary.keySet()) {
			SoundBoard sb = soundboardLibrary.get(soundboard);
			List<String> categoriesToUpdate = new LinkedList<>();
			for (String category : sb.getCategories()) {
				for (Sound sound : sb.getAmbienceSounds(category)) {
					if(sound.getName().equals(s.getName()))
						categoriesToUpdate.add(category);
				}
			}
			if(categoriesToUpdate.size() > 0) {
				for (String category : categoriesToUpdate) {
					if (s.isAmbience()) {
						sb.addAmbienceSound(category, s);
					}
					else {
						sb.addEffectSound(category, s);
					}
				}
				try {
					saveSoundBoard(sb);
				} catch (IOException e) {
					logger.error("Could not update soundboard " + sb.name, e);
				}
			}
		}
	}

	public static Sound saveNewSoundplaylist(String name, BufferedImage icon, String[] audioFiles, Sound.Type type,
			String[] tags) throws IOException {
		Path soundLibPath = AudioApp.getConfiguration().getSoundLibPath();
		Path soundFolder = soundLibPath.resolve(name);
		Path soundJson = soundLibPath.resolve(name + ".json");
		Path soundIcon = soundFolder.resolve(name + ".png");
		Files.createDirectories(soundFolder);
		for (int i = 0; i < audioFiles.length; i++) {
			String audioFile = audioFiles[i];
			Path soundSource = Paths.get(audioFile);
			String extension = audioFile.substring(audioFile.lastIndexOf("."));
			Path soundFile = soundFolder.resolve(name + extension);
			Files.copy(soundSource, soundFile);
		}
		ImageIO.write(icon, "PNG", soundIcon.toFile());
		Sound s = new Sound(name, audioFiles, soundIcon.toString(), type, tags);
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
		if (AudioDevices.getAudioDeviceByName(info.getName(), false) != null) {
			configuration.setAudioInterface(info);
			if (player != null)
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
		return soundboardLibrary.get(name);
	}

	public static List<SoundBoard> getSoundboardLibrary() {
		return new ArrayList<>(soundboardLibrary.values());
	}

	public static List<Sound> getSoundLibrary() {
		return soundLibrary;
	}

	public static List<Sound> getEffectSounds() {
		List<Sound> localSoundList = soundLibrary.stream().filter(s -> s.getType() == Type.EFFECT).collect(Collectors.toList());
//		if(isSpotifyEnabled()) {
//			List<Sound> spotifyPlaylists = getSpotifyPlaylistSounds(Type.EFFECT);
//			if(spotifyPlaylists != null)
//				localSoundList.addAll(spotifyPlaylists);
//		}
		return localSoundList;
	}

	public static List<Sound> getAmbienceSounds() {
		List<Sound> localSoundList = soundLibrary.stream().filter(s -> s.getType() == Type.AMBIENCE).collect(Collectors.toList());
		if(isSpotifyEnabled()) {
			List<Sound> spotifyPlaylists = getSpotifyPlaylistSounds(Type.AMBIENCE);
			if(spotifyPlaylists != null)
				localSoundList.addAll(spotifyPlaylists);
		}
		return localSoundList;
	}

	public static List<Sound> getSpotifyPlaylistSounds(Type type) {
		if(isSpotifyEnabled()) {
			SpotifyFrontend sf = getSpotifyFrontend();
			List<Sound> s = sf.getListOfCurrentUsersPlaylists().stream()
					.map(p -> new Sound(p.getName(), p.getOwner().getId(), p.getId(), p.getType().type, type))
					.collect(Collectors.toList());
			Collections.sort(s);
			List<Sound> finalSounds = new ArrayList<>(s.size());
			Sound lSound = null;
			for (Sound sound : s) {
				if(lSound == null || sound.compareTo(lSound) != 0) {
					MetadataStore ms = AudioApp.getMetadata("SPOTIFY_" + sound.getSpotifyId());
					if(ms != null) {
						sound.setMetadata(ms.getMetadata());
					}
					finalSounds.add(sound);
				}
				lSound = sound;
			}
			return finalSounds;
		}
		return null;
	}

	public static IStreamDeck getStreamDeck() {
		return streamDeck;
	}

	public static SDImage getSpotifyCover(Sound sound) {
		Path cachFolder = Paths.get("cache", "spotify", sound.getSpotifyOwner(), sound.getSpotifyType());
		try {
			if (!Files.exists(cachFolder))
				Files.createDirectories(cachFolder);
			Path imagePath = cachFolder.resolve(sound.getSpotifyId() + ".png");
			// cache spotify cover
			if(!Files.exists(imagePath)) {
				SpotifyFrontend sf = SpotifyFrontend.createInstance(configuration.spotifyClientId, configuration.spotifyClientSecret, configuration.spotifyResponseUrl, true);
				Playlist pl = sf.getPlaylist(sound.getSpotifyId());
				Image[] is = pl.getImages();
				if(is.length > 0) {
					BufferedImage img = ImageIO.read(new URL(is[0].getUrl()));
					ImageIO.write(img, "PNG", imagePath.toFile());
				}
				else if(Sound.DEFAULT_COVER != null)
					ImageIO.write(Sound.DEFAULT_COVER, "PNG", imagePath.toFile());
			}
//			BufferedImage img = ImageIO.read(imagePath.toFile());
			return IconHelper.loadImage(imagePath);
//			return IconHelper.convertImage(img);
		} catch (Exception e) {
			logger.error("Error loading spotify cover for " + sound.getName(), e);
		}
		return Sound.DEFAULT_COVER != null ? IconHelper.convertImage( Sound.DEFAULT_COVER) : null;
	}

	public static void main(String[] args) {
		loadConfiguration();
		checkOldLibFormate();
		loadSoundLibrary();
		loadSoundBoards();
		isSpotifyEnabled();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (streamDeck != null) {
					streamDeck.setBrightness(5);
					streamDeck.reset();
				}
				if (AudioApp.controller != null)
					AudioApp.controller.stop(true, false);
				AudioApp.stop();
			}
		});
		int sbC = 0;
		streamDeck = StreamDeckDevices.getStreamDeck();
		SoftStreamDeck.hideDecks();
		if (streamDeck == null)
			streamDeck = new SoftStreamDeck("Sound Board App", null);
		StreamItem[] sbItems = new StreamItem[soundboardLibrary.size()];
		for (SoundBoard sb : soundboardLibrary.values()) {
			sbItems[sbC++] = new SoundBoardItem(sb, null);
		}
		PagedFolderItem root = new PagedFolderItem("root", null, null, sbItems, streamDeck.getKeySize());
		AudioApp.addStatusBarItems(root, root.getChildren());
		streamDeck.reset();
		streamDeck.addKeyListener(new DelayedReduceBrightness(streamDeck));
		StreamDeckController.setKeyDeadzone(50);
		AudioApp.controller = new StreamDeckController(streamDeck, root);
		AudioApp.controller.setBrightness(75);
		PluginManager.init();
		if(splashscreen != null) {
			splashscreen.setVisible(false);
			splashscreen.dispose();
		}
	}

	public static void addConfig(String key, String value) {
		getConfiguration().addConfig(key, value);
		saveConfig();
	}

	public static void removeConfig(String key) {
		getConfiguration().removeConfig(key);
		saveConfig();
	}

	public static String getConfig(String key) {
		return getConfiguration().getConfig(key);
	}

	public static String[] getConfigKeys(String key) {
		return getConfiguration().getConfigKeys(key);
	}

	private static void addPlaybackListeners(AudioPlayer player) {
		for (PlaybackListener playbackListener : playbackListeners) {
			player.addPlaybackListener(playbackListener);
		}
	}

	public static void addPlaybackListener(PlaybackListener listener) {
		playbackListeners.add(listener);
		if (player != null)
			player.addPlaybackListener(listener);
	}

	public static void removePlaybackListener(PlaybackListener listener) {
		playbackListeners.remove(listener);
		if (player != null)
			player.removePlaybackListener(listener);
	}
	
	private static Map<String, MetadataStore> metadataStoreCache = new HashMap<>(); 

	public static void storeMetadata(String key, Map<String, List<SoundPluginMetadata>> metadata) {
		metadata = Objects.requireNonNull(metadata);
		key = Objects.requireNonNull(key);
		MetadataStore ms = new MetadataStore(key, metadata);
		Path metadataStore = AudioApp.getConfiguration().getMetadataStore();
		try {
			if (!Files.exists(metadataStore))
				Files.createDirectories(metadataStore);
			Path metadataFile = metadataStore.resolve(key + ".json");
			MetadataStore.save(ms, metadataFile);
			metadataStoreCache.put(key, ms);
		} catch (IOException e) {
			logger.error("Caonnot create folder to store additional metadata", e);
		}
	}
	
	public static MetadataStore getMetadata(String key) {
		key = Objects.requireNonNull(key);
		if(metadataStoreCache.containsKey(key))
			return metadataStoreCache.get(key);
		Path metadataStore = AudioApp.getConfiguration().getMetadataStore();
		try {
			if (!Files.exists(metadataStore))
				Files.createDirectories(metadataStore);
			Path metadataFile = metadataStore.resolve(key + ".json");
			if (Files.exists(metadataFile))
				return MetadataStore.load(metadataFile);
		} catch (IOException e) {
			logger.error("Caonnot create folder to store additional metadata", e);
		}
		return null;
	}
}
