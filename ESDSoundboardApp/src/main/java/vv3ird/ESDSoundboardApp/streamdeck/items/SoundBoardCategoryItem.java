package vv3ird.ESDSoundboardApp.streamdeck.items;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rcblum.stream.deck.items.FolderItem;
import de.rcblum.stream.deck.items.StreamItem;
import de.rcblum.stream.deck.items.animation.AnimationStack;

public class SoundBoardCategoryItem extends FolderItem {

	private static Logger logger = LogManager.getLogger(SoundBoardCategoryItem.class);

	public SoundBoardCategoryItem(String folderName, StreamItem parent, SoundBoardCategoryItem previous, StreamItem[] children) {
		super(folderName, parent, new StreamItem[15]);
		children = children != null ? children : new StreamItem[15];
		int maxItems = 9;
		// Create Sub-folder for more then 9/10 children (with parent/without parent)
		int countTotal=0;
		for (StreamItem streamItem : children) {
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
		// AudioApp.addStatusBarItems(this, this.getChildren());
		if (previous != null) {
			this.getChildren()[11] = new PreviousItem(previous);
		}
		if (countTotal > maxItems && children.length-childIndex > 0) {
			logger.debug("Create next page for " + this.getText());
			// Create children for next page
			StreamItem[] nextPageChildren = Arrays.copyOfRange(children, childIndex, children.length);
			// Create next page
			SoundBoardCategoryItem next = new SoundBoardCategoryItem(folderName, parent, this, nextPageChildren);
			next.setTextPosition(TEXT_POS_CENTER);
			next.setText("Next");
			this.getChildren()[10] = new NextItem(next);
		}
	}
	
	public void setGlobalItem(int index, StreamItem item) {
		if(index < 0 || index >= 15)
			throw new IndexOutOfBoundsException("Index must be between 0 and 14 inclusive");
		this.getChildren()[index] = item;
		if (this.getChildren()[10] != null && this.getChildren()[10] instanceof NextItem) {
			((NextItem)this.getChildren()[10]).setGlobalItem(index, item);
		}
	}
	
	@Override
	public void setParent(StreamItem parent) {
		super.setParent(parent);
		if (this.getChild(10) != null)
			this.getChild(10).setParent(parent);
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
