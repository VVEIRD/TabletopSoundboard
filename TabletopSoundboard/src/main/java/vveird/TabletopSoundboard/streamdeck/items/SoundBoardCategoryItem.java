package vveird.TabletopSoundboard.streamdeck.items;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rcblum.stream.deck.items.FolderItem;
import de.rcblum.stream.deck.items.StreamItem;
import de.rcblum.stream.deck.items.animation.AnimationStack;
import de.rcblum.stream.deck.util.IconHelper;
import vveird.TabletopSoundboard.config.Sound;

public class SoundBoardCategoryItem extends FolderItem {

	private static Logger logger = LogManager.getLogger(SoundBoardCategoryItem.class);

	public SoundBoardCategoryItem(String folderName, StreamItem parent, SoundBoardCategoryItem previous, SoundItem[] children, int buttonCount, int rowCount) {
		super(folderName, parent, new StreamItem[buttonCount]);
		this.setButtonCount(buttonCount);
		this.setRowCount(rowCount);
		children = children != null ? children : new SoundItem[buttonCount];
		int maxItems = buttonCount - (this.getColumnCount()+1);
		// Create Sub-folder for more then 9/buttonCount-this.getColumnCount() children (with parent/without parent)
		int countTotal = previous == null ? 0 : 0;
		// Generate play all sounds button
		if(previous == null) {
			List<String> files = new LinkedList<>();
			Arrays.asList(children).stream().filter(s -> !s.getSound().isSpotifySound()).map(s -> Arrays.asList(s.getSound().getFilePaths())).forEach(files::addAll);
			Collections.sort(files);
//			String coverPath = Arrays.asList(children).stream().map(s -> s.getSound().getCoverPath()).findFirst().orElse("BLACK");
			Sound.Type type = Arrays.asList(children).stream().map(s -> s.getSound().getType()).findFirst().orElse(Sound.Type.AMBIENCE);
			Sound playAllSound = new Sound(folderName +  " - Play all", files.toArray(new String[0]), IconHelper.getImage("temp://BLACK_ICON").image, type, null);
			SoundItem playAll = new SoundItem(playAllSound);
			playAll.setText( "Play all");
			playAll.setParent(this);
			this.getChildren()[buttonCount-3] = playAll;
			playAllSound.resetCurrentFile();
		}
		else {
			// Take playAllSound from previous page
			this.getChildren()[buttonCount-3] = previous.getChild(buttonCount-3);
		}
		// Count all child sound items
		for (SoundItem streamItem : children) {
			if (streamItem != null)
				countTotal++;
		}
		
		// Fill current page
//		int insertIndex = 0;
		int sourceIndex = 0;
		int countSoundItemAdded = 0;
//		for (SoundItem soundItem : children) {
//			sourceIndex++;
//			if (countSoundItemAdded >= maxItems)
//				break;
//			else if (soundItem == null) {
//				continue;
//			}
//			else if (insertIndex == this.getColumnCount()-1) {
//				insertIndex++;
//				continue;
//			}
//			this.getChildren()[insertIndex++] = soundItem;
//			soundItem.setParent(this);
//			countSoundItemAdded++;
//		}
		for(;sourceIndex<children.length&&countSoundItemAdded<maxItems;sourceIndex++) {
			if(children[sourceIndex] != null) {
				this.getChildren()[countSoundItemAdded<this.getColumnCount()-1? countSoundItemAdded : countSoundItemAdded+1] = children[sourceIndex];
				children[sourceIndex].setParent(this);
				countSoundItemAdded++;
			}
		}
		if (previous != null) {
			this.getChildren()[this.getButtonCount() - (this.getColumnCount()-1)] = new PreviousItem(previous);
		}
		if (countTotal > maxItems && children.length-sourceIndex > 0) {
			logger.debug("Create next page for " + this.getText());
			// Create children for next page
			SoundItem[] nextPageChildren = Arrays.copyOfRange(children, sourceIndex, children.length);
			// Create next page
			SoundBoardCategoryItem next = new SoundBoardCategoryItem(folderName, parent, this, nextPageChildren, buttonCount, rowCount);
			next.setTextPosition(TEXT_POS_CENTER);
			next.setText("Next");
			this.getChildren()[this.getButtonCount() - this.getColumnCount()] = new NextItem(next);
		}
	}
	
	public void setGlobalItem(int index, StreamItem item) {
		if(index < 0 || index >= this.getButtonCount())
			throw new IndexOutOfBoundsException("Index must be between 0 and 14 inclusive");
		this.getChildren()[index] = item;
		// Set global item for all other pages
		if (this.getChildren()[this.getButtonCount() - this.getColumnCount()] != null && this.getChildren()[this.getButtonCount() - this.getColumnCount()] instanceof NextItem) {
			((NextItem)this.getChildren()[this.getButtonCount() - this.getColumnCount()]).setGlobalItem(index, item);
		}
	}
	
	@Override
	public void setParent(StreamItem parent) {
		super.setParent(parent);
		if (this.getChild(buttonCount-this.getColumnCount()) != null)
			this.getChild(buttonCount-this.getColumnCount()).setParent(parent);
	}
	
	public static class NextItem extends FolderItem{

		SoundBoardCategoryItem next = null;
		
		public NextItem(SoundBoardCategoryItem next) {
			super("Next", null, new StreamItem[next.getButtonCount()]);
			this.next = next;
			this.setTextPosition(TEXT_POS_CENTER);
			this.setText("Next");
		}
		
		@Override
		public StreamItem getChild(int i) {
			return next.getChild(i);
		}
		
		@Override
		public AnimationStack getAnimation() {
			return next.getAnimation();
		}
		
		@Override
		public int getChildCount() {
			return next.getChildCount();
		}
		
		@Override
		public int getChildId(StreamItem item) {
			return next.getChildId(item);
		}
		
		@Override
		public StreamItem[] getChildren() {
			return next.getChildren();
		}
		
		@Override
		public StreamItem getParent() {
			return next.getParent();
		}
		
		@Override
		public void setParent(StreamItem parent) {
			next.setParent(parent);
		}
		
		public void setGlobalItem(int index, StreamItem item) {
			if(index < 0 || index >= this.getButtonCount())
				throw new IndexOutOfBoundsException("Index must be between 0 and 14 inclusive");
			next.getChildren()[index] = item;
		}
	}
	
	public static class PreviousItem extends FolderItem{

		SoundBoardCategoryItem next = null;
		
		public PreviousItem(SoundBoardCategoryItem next) {
			super("Previous", null, new StreamItem[next.getButtonCount()]);
			this.next = next;
			this.setTextPosition(TEXT_POS_CENTER);
			this.setText("Previous");
		}
		
		@Override
		public StreamItem getChild(int i) {
			return next.getChild(i);
		}
		
		@Override
		public AnimationStack getAnimation() {
			return next.getAnimation();
		}
		
		@Override
		public int getChildId(StreamItem item) {
			return next.getChildId(item);
		}
		
		@Override
		public StreamItem[] getChildren() {
			return next.getChildren();
		}
		
		@Override
		public StreamItem getParent() {
			return next.getParent();
		}
		
		@Override
		public void setParent(StreamItem parent) {
			next.setParent(parent);
		}
	}

}
