package vv3ird.ESDSoundboardApp.streamdeck.items;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rcblum.stream.deck.items.FolderItem;
import de.rcblum.stream.deck.items.StreamItem;
import de.rcblum.stream.deck.items.animation.AnimationStack;
import de.rcblum.stream.deck.util.IconHelper;
import vv3ird.ESDSoundboardApp.AudioApp;
import vv3ird.ESDSoundboardApp.config.Sound;

public class SoundBoardCategoryItem extends FolderItem {

	private static Logger logger = LogManager.getLogger(SoundBoardCategoryItem.class);

	public SoundBoardCategoryItem(String folderName, StreamItem parent, SoundBoardCategoryItem previous, SoundItem[] children, int buttonCount) {
		super(folderName, parent, new StreamItem[buttonCount]);
		children = children != null ? children : new SoundItem[buttonCount];
		int maxItems = buttonCount - 6;
		// Create Sub-folder for more then 9/buttonCount-5 children (with parent/without parent)
		int countTotal = previous == null ? 1 : 0;
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
			this.getChildren()[buttonCount-3] = previous.getChild(buttonCount-3);
		}
		for (SoundItem streamItem : children) {
			if (streamItem != null)
				countTotal++;
		}
		
		// Fill current page
		int childIndex = 0;
		int countSI = 0;
		for(;childIndex<children.length&&countSI<maxItems;childIndex++) {
			if(children[childIndex] != null) {
				this.getChildren()[countSI<4? countSI : countSI+1] = children[childIndex];
				children[childIndex].setParent(this);
				countSI++;
			}
		}
		// Create status bar
//		 AudioApp.addStatusBarItems(this, this.getChildren());
		if (previous != null) {
			this.getChildren()[11] = new PreviousItem(previous);
		}
		if (countTotal > maxItems && children.length-childIndex > 0) {
			logger.debug("Create next page for " + this.getText());
			// Create children for next page
			SoundItem[] nextPageChildren = Arrays.copyOfRange(children, childIndex, children.length);
			// Create next page
			SoundBoardCategoryItem next = new SoundBoardCategoryItem(folderName, parent, this, nextPageChildren, buttonCount);
			next.setTextPosition(TEXT_POS_CENTER);
			next.setText("Next");
			this.getChildren()[buttonCount-5] = new NextItem(next);
		}
	}
	
	public void setGlobalItem(int index, StreamItem item) {
		if(index < 0 || index >= 15)
			throw new IndexOutOfBoundsException("Index must be between 0 and 14 inclusive");
		this.getChildren()[index] = item;
		if (this.getChildren()[buttonCount-5] != null && this.getChildren()[buttonCount-5] instanceof NextItem) {
			((NextItem)this.getChildren()[buttonCount-5]).setGlobalItem(index, item);
		}
	}
	
	@Override
	public void setParent(StreamItem parent) {
		super.setParent(parent);
		if (this.getChild(buttonCount-5) != null)
			this.getChild(buttonCount-5).setParent(parent);
	}
	
	public static class NextItem extends FolderItem{

		SoundBoardCategoryItem next = null;
		
		public NextItem(SoundBoardCategoryItem next) {
			super("Next", null, new StreamItem[15]);
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
			if(index < 0 || index >= 15)
				throw new IndexOutOfBoundsException("Index must be between 0 and 14 inclusive");
			next.getChildren()[index] = item;
		}
	}
	
	public static class PreviousItem extends FolderItem{

		SoundBoardCategoryItem next = null;
		
		public PreviousItem(SoundBoardCategoryItem next) {
			super("Previous", null, new StreamItem[15]);
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
