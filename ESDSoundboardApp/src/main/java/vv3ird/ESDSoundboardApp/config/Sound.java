package vv3ird.ESDSoundboardApp.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.google.gson.GsonBuilder;

public class Sound implements Comparable<Sound>{

	public String name;
	
	public String filePath;
	
	public String coverPath;
	
	public List<String> tags = null;
	
	public Type type = Type.AMBIENCE;

	public Sound(String name, String filePath, String coverPath, Type type) {
		super();
		this.name = name;
		this.filePath = filePath;
		this.coverPath = coverPath;
		this.type = type;
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
		return this.name.compareTo(o != null ? o.name : null);
	}
	
	@Override
	public String toString() {
		return this.name + (this.tags != null && this.tags.size() > 0 ? " (" + String.join(", ", this.tags) + ")" : "");
	}
	

}
