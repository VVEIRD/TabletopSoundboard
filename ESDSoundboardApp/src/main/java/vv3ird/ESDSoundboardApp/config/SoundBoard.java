package vv3ird.ESDSoundboardApp.config;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SoundBoard {
	
	public String name;

	// [Category][soundfile]
	public Map<String, List<Sound>> ambience;

	public Map<String, List<Sound>> effects;
	
	public SoundBoard(String name, Map<String, List<Sound>> ambience, Map<String, List<Sound>> effects) {
		this.name = name;
		this.ambience = ambience;
		this.effects = effects;
	}
	
	public SoundBoard(String name) {
		this.name = name;
		this.ambience = new HashMap<>();
		this.effects = new HashMap<>();
	}
	
	public boolean addCategory(String category) {
		if(!this.ambience.containsKey(category)) {
			this.ambience.put(category, new LinkedList<>());
			this.effects.put(category, new LinkedList<>());
			return true;
		}
		return false;
	}
	
	public boolean removeCategory(String category) {
		if(this.ambience.containsKey(category)) {
			this.ambience.remove(category);
			this.effects.remove(category);
			System.out.println("Category " + category + " removed");
			return true; 
		}
		return false;
	}
	
	public boolean addAmbienceSound(String category, Sound sound) {
		if(this.ambience.containsKey(category)) {
			if(this.ambience.get(category).contains(sound))
				this.ambience.get(category).remove(sound);
			return this.ambience.get(category).add(sound);
		}
		return false;
	}
	
	public boolean addEffectSound(String category, Sound sound) {
		if(this.effects.containsKey(category)) {
			if(this.effects.get(category).contains(sound))
				this.effects.get(category).remove(sound);
			return this.effects.get(category).add(sound);
		}
		return false;
	}
	
	public Set<String> getCategories() {
		return this.ambience.keySet();
	}

	public List<Sound> getAmbienceSounds(String category) {
		return this.ambience.get(category);
	}

	public List<Sound> getEffectSounds(String category) {
		return this.effects.get(category);
	}
	
	public void save(Path root) {
		
	}

	public boolean containsSound(String category, Sound s) {
		return this.ambience.get(category).stream().anyMatch(sq -> sq.compareTo(s) == 0);
	}
	
	public SoundBoard clone() {
		return new SoundBoard(name, new HashMap<>(ambience), new HashMap<>(effects));
	}
	
}
