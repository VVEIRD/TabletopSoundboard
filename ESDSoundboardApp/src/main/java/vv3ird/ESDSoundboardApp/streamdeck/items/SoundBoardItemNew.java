package vv3ird.ESDSoundboardApp.streamdeck.items;

import java.util.List;
import java.util.Map;
import java.util.Set;

import vv3ird.ESDSoundboardApp.config.Sound;
import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.AudioApp;
import de.rcblum.stream.deck.items.FolderItem;
import de.rcblum.stream.deck.items.PagedFolderItem;
import de.rcblum.stream.deck.items.ProxyItem;
import de.rcblum.stream.deck.items.StreamItem;
import de.rcblum.stream.deck.util.IconHelper;

public class SoundBoardItemNew extends FolderItem {

	public SoundBoardItemNew(SoundBoard soundBoard, StreamItem parent) {
		super(soundBoard.name, parent, new StreamItem[15]);
		// Create Categories
		StreamItem[] categories = createCategoryItems(soundBoard.ambience, soundBoard.effects);
		int i_d = 0;
		for (int i = 0; i < categories.length; i++) {
			if(i_d == 4) i_d++;
			this.getChildren()[i_d++] = categories[i];
		}
		AudioApp.addStatusBarItems(this, this.getChildren());
		//StreamItem[] effectCategories = createCategoryItems(soundBoard.effects);
		// Create Folders for ambience and effect sounds
//		SoundBoardCategoryItem ambience = new SoundBoardCategoryItem("Ambience", this, null, ambienceCategories);
//		AudioApp.addStatusBarItems(ambience, ambience.getChildren());
//		SoundBoardCategoryItem effects = new SoundBoardCategoryItem("Effects", this, null, effectCategories);
//		AudioApp.addStatusBarItems(effects, effects.getChildren());
//		// Add to Folder
//		this.getChildren()[8] = ambience;
//		this.getChildren()[6] = effects;
//		// Add status bar items
//		AudioApp.addStatusBarItems(this, this.getChildren());
	}

	private StreamItem[] createCategoryItems(Map<String, List<Sound>> ambience, Map<String, List<Sound>> effects) {
		StreamItem[] result = null;
		Set<String> categories = ambience.keySet();
		result = new StreamItem[categories.size()];
		int catCount = 0;
		for (String category : categories) {
			List<Sound> ambienceSounds = ambience.get(category);
			SoundItem[] ambienceItems = null;
			List<Sound> effectsSounds = effects.get(category);
			SoundItem[] effectsItems = null;
			if(ambienceSounds != null && ambienceSounds.size() > 0) {
				ambienceItems = new SoundItem[ambienceSounds.size()];
				for(int i=0;i<ambienceItems.length;i++) {
					ambienceItems[i] = new SoundItem(ambienceSounds.get(i));
				}
			}
			if(effectsSounds != null && effectsSounds.size() > 0) {
				effectsItems = new SoundItem[effectsSounds.size()];
				for(int i=0;i<effectsItems.length;i++) {
					effectsItems[i] = new SoundItem(effectsSounds.get(i));
				}
			}
			SoundBoardCategoryItem effectsCategoryItem = null;
			SoundBoardCategoryItem ambienceCategoryItem = null;
			if (ambienceItems != null) {
				ambienceCategoryItem = new SoundBoardCategoryItem(category, this, null, ambienceItems, AudioApp.getStreamDeck().getKeySize());
				AudioApp.addStatusBarItems(ambienceCategoryItem, ambienceCategoryItem.getChildren());
			}
			if (effectsItems != null) {
				effectsCategoryItem = new SoundBoardCategoryItem(category, this, null, effectsItems, AudioApp.getStreamDeck().getKeySize());
				AudioApp.addStatusBarItems(effectsCategoryItem, effectsCategoryItem.getChildren());
				ProxyItem p1 = new ProxyItem(ambienceCategoryItem, null);
				p1.setIcon(IconHelper.loadImageFromResourceSafe("/resources/icons/change.png"));
				p1.setTextLine1("Effects", 16);
				p1.setTextLine2("Ambience", 12);
				effectsCategoryItem.setGlobalItem(13, p1);
				if(ambienceCategoryItem != null) {
					ProxyItem p2 = new ProxyItem(effectsCategoryItem, null);
					p2.setIcon(IconHelper.loadImageFromResourceSafe("/resources/icons/change.png"));
					p2.setTextLine1("Ambience", 16);
					p2.setTextLine2("Effects", 12);
					ambienceCategoryItem.setGlobalItem(13, p2);
				}
			}
			result[catCount++] = ambienceCategoryItem != null ? ambienceCategoryItem : effectsCategoryItem;
		}
		return result;
	}
	
}
