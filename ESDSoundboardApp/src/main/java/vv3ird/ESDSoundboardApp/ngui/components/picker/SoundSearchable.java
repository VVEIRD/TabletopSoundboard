package vv3ird.ESDSoundboardApp.ngui.components.picker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import vv3ird.ESDSoundboardApp.config.Sound;

/**

 * Implementation of the Searchable interface that searches a List of String objects. 

 * This implementation searches only the beginning of the words, and is not be optimized

 * for very large Lists. 

 * @author G. Cope

 *

 */

public class SoundSearchable implements Searchable<Sound,String>{



	private Map<String, List<Sound>> index = new HashMap<>();
	
	private List<Sound> source = null;

	

	/**

	 * Constructs a new object based upon the parameter terms. 

	 * @param terms The inventory of terms to search.

	 */

	public SoundSearchable(List<Sound> items){
		Collections.sort(items);
		this.source = new ArrayList<>(items);
		for (Sound sound : items) {
			if(sound != null) {
				String[] ncs = sound.name.split(" ");
				for (String string : ncs) {
					addIndex(string, sound);
				}
				if(sound.tags != null) {
					for (int i = 0; i < sound.tags.length; i++) {
						if(sound.tags[i] != null)
							addIndex(sound.tags[i], sound);
					}
				}
			}
		}
	}

	private void addIndex(String string, Sound sound) {
		if(sound == null || string == null)
			return;
		if(!index.containsKey(sound.name))
			index.put(sound.name, new LinkedList<>());
		index.get(sound.name).add(sound);	
		
	}

	@Override
	public List<Sound> search(String value) {
		for (String key : index.keySet()) {
			if (key.contains(value))
				return index.get(key);
		}
		return new ArrayList<>();
	}
	
	@Override
	public List<Sound> getFullList() {
		return source;
	}
}