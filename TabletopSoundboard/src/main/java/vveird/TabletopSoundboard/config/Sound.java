package vveird.TabletopSoundboard.config;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.GsonBuilder;

import de.rcblum.stream.deck.util.SDImage;
import vveird.TabletopSoundboard.AudioApp;
import vveird.TabletopSoundboard.plugins.data.SoundPluginMetadata;

public class Sound implements Comparable<Sound>, Iterator<String>{
	
	public static BufferedImage OK = null;
	public static BufferedImage FALSE = null;
	public static BufferedImage DEFAULT_COVER = null;

	static {
		try {
			OK = ImageIO.read(Sound.class.getClassLoader().getResource("resources/icons/ok.png"));
			FALSE = ImageIO.read(Sound.class.getClassLoader().getResource("resources/icons/false.png"));
			DEFAULT_COVER = ImageIO.read(Sound.class.getClassLoader().getResource("resources/icons/audio.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Logger logger = LogManager.getLogger(Sound.class);;

	private String name;
	
	private String[] filePaths;
	
	@Deprecated
	private String filePath;
	
	private transient int currentFile = 0;
	
	private String coverPath;

	private transient BufferedImage cover = null;
	
	private List<String> tags = null;
	
	private Type type = Type.AMBIENCE;
	
	private boolean isSpotifySound = false;
	
	private String spotifyOwner = null;
	
	private String spotifyId = null;
	
	private String spotifyType = "playlist";
	
	private Map<String, List<SoundPluginMetadata>> pluginMetadata = new HashMap<>();

	public Sound(String name, String filePath, String coverPath, Type type, String[] tags) {
		this(name, new String[] {filePath}, coverPath, type, tags);
	}

	public Sound(String name, String[] filePaths, String coverPath, Type type, String[] tags) {
		this(name, filePaths, coverPath, type, tags, null);
	}

	public Sound(String name, String[] filePaths, String coverPath, Type type, String[] tags,  Map<String, List<SoundPluginMetadata>> pluginMetadata) {
		this(name, filePaths, type, tags, pluginMetadata);
		this.coverPath = coverPath;
	}

	public Sound(String name, String[] filePaths, BufferedImage cover, Type type, String[] tags) {
		this(name, filePaths, cover, type, tags, null);
	}

	public Sound(String name, String[] filePaths, BufferedImage cover, Type type, String[] tags,  Map<String, List<SoundPluginMetadata>> pluginMetadata) {
		this(name, filePaths, type, tags, pluginMetadata);
		this.cover = cover;
	}

	public Sound(String name, String[] filePaths, Type type, String[] tags, Map<String, List<SoundPluginMetadata>> pluginMetadata) {
		super();
		this.name = name;
		this.filePaths = filePaths;
		this.type = type;
		this.tags = tags != null ? Arrays.asList(tags) : new LinkedList<>();
		this.pluginMetadata = pluginMetadata != null ? pluginMetadata : new HashMap<>();
	}

	public Sound(String name, String spotifyOwner, String spotifyId, String spotifyType, Type type) {
		super();
		this.name = name;
		this.type = type;
		this.isSpotifySound = true;
		this.spotifyOwner = spotifyOwner;
		this.spotifyId = spotifyId;
		this.spotifyType = spotifyType;
		this.tags = new ArrayList<>();
		this.tags.add("Spotify");
		this.filePaths = new String[0];
		
	}
	
	private void checkLegacy() {
		if(this.filePath != null) {
			this.filePaths = new String[] {this.filePath};
			this.filePath = null;
		}
	}
	
	public static void save(Sound s, Path file) {
		String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(s);
		try {
			byte[] utf8JsonString = jsonString.getBytes("UTF-8");
			Files.write(file, utf8JsonString, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static enum Type {
		AMBIENCE, EFFECT;
	}
	
	public List<String> getTags() {
		return tags;
	}

	@Override
	public int compareTo(Sound o) {
		if(o == null)
			return 1;
		int nameC = this.name.compareTo(o.name);
		int typeC = this.type.compareTo(o.type);
		return  nameC != 0 ? nameC : typeC;
	}
	
	@Override
	public String toString() {
		return this.name + (this.tags != null && this.tags.size() > 0 ? " (" + String.join(", ", this.tags) + ")" : "");
	}

	@Override
	public boolean hasNext() {
		checkLegacy();
		return this.filePaths.length > 0;
	}

	@Override
	public String next() {
		checkLegacy();
		if(this.filePaths.length == 0)
			return null;
		int currentIdx = this.currentFile;
		this.currentFile = this.currentFile+1 >= this.filePaths.length ? 0 : this.currentFile+1;
		return this.filePaths[currentIdx];
	}
	
	public boolean isPlaylist() {
		checkLegacy();
		return this.filePaths.length > 1;
	}
	
	public String getCoverPath() {
		return coverPath;
	}
	
	public String getCurrentFile() {
		checkLegacy();
		return this.filePaths.length > 0 ? this.filePaths[currentFile] : null;
	}
	
	public void resetCurrentFile() {
		this.currentFile = 0;
	}
	
	public String[] getFilePaths() {
		checkLegacy();
		return filePaths;
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setCoverPath(String coverPath) {
		this.coverPath = coverPath;
	}
	
	public void setCurrentFile(int currentFile) {
		this.currentFile = currentFile;
	}
	
	public void setFilePaths(String[] filePaths) {
		this.filePaths = filePaths;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public boolean isSpotifySound() {
		return isSpotifySound;
	}
	
	public String getSpotifyId() {
		return spotifyId;
	}
	
	public String getSpotifyOwner() {
		return spotifyOwner;
	}
	
	public String getSpotifyType() {
		return spotifyType;
	}
	
	public List<SoundPluginMetadata> getMetadataFor(String pluginClass) {
		if (pluginMetadata == null)
			pluginMetadata = new HashMap<>();
		return pluginMetadata.get(pluginClass);
	}
	
	public void addMetadataFor(String pluginClass, List<SoundPluginMetadata> metadata) {
		if (pluginMetadata == null)
			pluginMetadata = new HashMap<>();
		pluginMetadata.put(pluginClass, metadata);
	}
	
	public void removeMetadataFor(String pluginClass) {
		if (pluginMetadata == null)
			pluginMetadata = new HashMap<>();
		pluginMetadata.remove(pluginClass);
	}

	public Map<String, List<SoundPluginMetadata>> getMetadata() {
		return pluginMetadata;
	}

	public boolean isAmbience() {
		// TODO Auto-generated method stub
		return this.type == Type.AMBIENCE;
	}

	private void loadCover() {
		if(isSpotifySound()) {
			SDImage sCover = AudioApp.getSpotifyCover(this);
			if(sCover != null)
				this.cover = sCover.image;
			else 
				this.cover = DEFAULT_COVER;
		}
			
		if (getCoverPath() != null) {
			try {
				this.cover = ImageIO.read(new File(getCoverPath()));
			} catch (IOException e) {
				logger.error("Error loading cover: " + getCoverPath());
				logger.error(e);
			}
		}
	}

	public BufferedImage getCover() {
		if(cover == null)
			loadCover();
		return cover;
	}

	public void setMetadata(Map<String, List<SoundPluginMetadata>> metadata) {
		if(metadata != null)
			this.pluginMetadata = metadata;
	}

	public void setCover(BufferedImage cover) {
		this.cover = cover;
	}

	public boolean containedInTags(String search) {
		return tags != null && tags.stream().anyMatch(t -> t.toLowerCase().contains(search.toLowerCase()));
	}
}
