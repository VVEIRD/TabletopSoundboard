package vv3ird.ESDSoundboardApp.streamdeck.items;

import java.util.List;
import java.util.Map;
import java.util.Set;

import vv3ird.ESDSoundboardApp.config.Sound;
import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.AudioApp;
import de.rcblum.stream.deck.items.FolderItem;
import de.rcblum.stream.deck.items.PagedFolderItem;
import de.rcblum.stream.deck.items.StreamItem;

public class SoundBoardItem extends FolderItem {

	public SoundBoardItem(SoundBoard soundBoard, StreamItem parent) {
		super(soundBoard.name, parent, new StreamItem[15]);
		// Create Categories
		StreamItem[] ambienceCategories = createCategoryItems(soundBoard.ambience);
		StreamItem[] effectCategories = createCategoryItems(soundBoard.effects);
		// Create Folders for ambience and effect sounds
		PagedFolderItem ambience = new PagedFolderItem("Ambience", this, null, ambienceCategories, AudioApp.getStreamDeck().getKeySize());
		AudioApp.addStatusBarItems(ambience, ambience.getChildren());
		PagedFolderItem effects = new PagedFolderItem("Effects", this, null, effectCategories, AudioApp.getStreamDeck().getKeySize());
		AudioApp.addStatusBarItems(effects, effects.getChildren());
		// Add to Folder
		this.getChildren()[8] = ambience;
		this.getChildren()[6] = effects;
		// Add status bar items
		AudioApp.addStatusBarItems(this, this.getChildren());
	}

	private StreamItem[] createCategoryItems(Map<String, List<Sound>> ambience) {
		StreamItem[] result = null;
		Set<String> categories = ambience.keySet();
		result = new StreamItem[categories.size()];
		int catCount = 0;
		for (String category : categories) {
			List<Sound> sounds = ambience.get(category);
			StreamItem[] soundItems = new StreamItem[sounds.size()];
			for(int i=0;i<soundItems.length;i++) {
				soundItems[i] = new SoundItem(sounds.get(i));
			}
			PagedFolderItem categoryItem = new PagedFolderItem(category, this, null, soundItems, AudioApp.getStreamDeck().getKeySize());
			AudioApp.addStatusBarItems(categoryItem, categoryItem.getChildren());
			result[catCount++] = categoryItem;
		}
		return result;
	}
	
}
