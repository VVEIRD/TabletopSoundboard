package vveird.TabletopSoundboard.streamdeck.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.rcblum.stream.deck.device.StreamDeck;
import de.rcblum.stream.deck.device.StreamDeckConstants;
import de.rcblum.stream.deck.items.FolderItem;
import de.rcblum.stream.deck.items.PagedFolderItem;
import de.rcblum.stream.deck.items.ProxyItem;
import de.rcblum.stream.deck.items.StreamItem;
import de.rcblum.stream.deck.util.IconHelper;
import vveird.TabletopSoundboard.AudioApp;
import vveird.TabletopSoundboard.config.Sound;
import vveird.TabletopSoundboard.config.SoundBoard;
import vveird.TabletopSoundboard.streamdeck.items.SoundBoardCategoryItem.NextItem;

public class SoundBoardItem extends FolderItem {

	public SoundBoardItem(SoundBoard soundBoard) {
		this(soundBoard, null, StreamDeckConstants.BUTTON_COUNT, StreamDeckConstants.ROW_COUNT);
	}

	public SoundBoardItem(SoundBoard soundBoard, StreamItem parent) {
		this(soundBoard, null, parent.getButtonCount(), parent.getRowCount());
	}

	public SoundBoardItem(SoundBoard soundBoard, StreamItem parent, int buttonCount, int rowCount) {
		super(soundBoard.name, parent, new StreamItem[buttonCount]);
		this.setButtonCount(buttonCount);
		this.setRowCount(rowCount);
		// Create Categories
		StreamItem[] categories = createCategoryItems(soundBoard.ambience, soundBoard.effects);
		int i_d = 0;
		for (int i = 0; i < categories.length; i++) {
			if(i_d == this.getColumnCount()-1) i_d++;
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
		Set<String> categories = ambience.keySet().stream().sorted().collect(Collectors.toSet());
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
				ambienceCategoryItem = new SoundBoardCategoryItem(category, this, null, ambienceItems, this.getButtonCount(), this.getRowCount());
				StreamItem page = ambienceCategoryItem;
				while (page != null && page instanceof FolderItem) {
					AudioApp.addStatusBarItems(page, page.getChildren(), false);
					page = page.getChildren()[page.getButtonCount() - page.getColumnCount()];
				}
			}
			if (effectsItems != null) {
				effectsCategoryItem = new SoundBoardCategoryItem(category, this, null, effectsItems, this.getButtonCount(), this.getRowCount());
				StreamItem page = effectsCategoryItem;
				while (page != null && page instanceof FolderItem) {
					AudioApp.addStatusBarItems(page, page.getChildren(), false);
					page = page.getChildren()[page.getButtonCount() - page.getColumnCount()];
				}
				ProxyItem p1 = new ProxyItem(ambienceCategoryItem, null);
				p1.setIcon(IconHelper.loadImageFromResourceSafe("/icons/change.png"));
				p1.setTextLine1("Effects", 16);
				p1.setTextLine2("Ambience", 12);
				effectsCategoryItem.setGlobalItem(13, p1);
				if(ambienceCategoryItem != null) {
					ProxyItem p2 = new ProxyItem(effectsCategoryItem, null);
					p2.setIcon(IconHelper.loadImageFromResourceSafe("/icons/change.png"));
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
