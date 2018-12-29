package vveird.TabletopSoundboard.plugins.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MetadataStore {
	
	private static Logger logger = LogManager.getLogger(MetadataStore.class);
	
	private String id = null;
	
	private Map<String, List<SoundPluginMetadata>> metadata = null;
	
	public MetadataStore(String id, Map<String, List<SoundPluginMetadata>> metadata) {
		super();
		this.id = id;
		this.metadata = metadata;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, List<SoundPluginMetadata>> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, List<SoundPluginMetadata>> metadata) {
		this.metadata = metadata;
	}
	
	public static void save(MetadataStore ms, Path file) {
		String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(ms);
		try {
			byte[] utf8JsonString = jsonString.getBytes("UTF-8");
			Files.write(file, utf8JsonString, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch(IOException e) {
			logger.error("Error writing metadata: " + file.toString(), e);
		}
	}
	
	public static MetadataStore load(Path path) {
		try {
			MetadataStore ms = null;
			byte[] metadataStoreBytes = Files.readAllBytes(path);
			String metadataStoreString = new String(metadataStoreBytes, "UTF-8");
			logger.debug("MetadataStore:");
			logger.debug(ms);
			Gson gson = new Gson();
			ms = gson.fromJson(metadataStoreString, MetadataStore.class);
			logger.debug(ms);
			return ms;
		} catch(IOException e) {
			logger.error("Error reading metadata: " + path.toString(), e);
		}
		return null;
	}
}
