package vv3ird.ESDSoundboardApp.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.google.gson.GsonBuilder;

public class Sound {

	public String name;
	
	public String filePath;
	
	public String coverPath;
	
	public Type type = Type.AMBIENCE;

	public Sound(String name, String filePath, String coverPath, Type type) {
		super();
		this.name = name;
		this.filePath = filePath;
		this.coverPath = coverPath;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return this.name;
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

}
