package vv3ird.ESDSoundboardApp.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.gson.GsonBuilder;

public class Sound implements Comparable<Sound>, Iterator<String>{

	private String name;
	
	private String[] filePaths;
	
	@Deprecated
	private String filePath;
	
	private transient int currentFile = 0;
	
	private String coverPath;
	
	private List<String> tags = null;
	
	private Type type = Type.AMBIENCE;

	public Sound(String name, String filePath, String coverPath, Type type, String[] tags) {
		this(name, new String[] {filePath}, coverPath, type, tags);
	}

	public Sound(String name, String[] filePaths, String coverPath, Type type, String[] tags) {
		super();
		this.name = name;
		this.filePaths = filePaths;
		this.coverPath = coverPath;
		this.type = type;
		this.tags = tags != null ? Arrays.asList(tags) : null;
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
		return true;
	}

	@Override
	public String next() {
		checkLegacy();
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
		return this.filePaths[currentFile];
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
}
