package vv3ird.ESDSoundboardApp.ngui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.LinkedList;
import java.util.List;

public class UIColumnLayout implements LayoutManager2 {

	 private List<Component> components = new LinkedList<Component>();

	 private int elementSeperatingSpaceY = 0 ;

	 private int elementSeperatingSpaceX = 0 ;
	 
	 public UIColumnLayout() {
		// TODO Auto-generated constructor stub
	}
	 
	public UIColumnLayout(int elementSeperatingSpaceX,
			int elementSeperatingSpaceY) {
		super();
		this.elementSeperatingSpaceY = elementSeperatingSpaceY;
		this.elementSeperatingSpaceX = elementSeperatingSpaceX;
	}



	@Override
	public void addLayoutComponent(String name, Component comp) {
		this.components.add(comp);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		this.components.remove(comp);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		int width = parent.getWidth();
		if (width == 0)
			width = Integer.MAX_VALUE;
		int height = this.elementSeperatingSpaceY;
		synchronized (parent.getTreeLock()) {
			width = parent.getWidth();
			for (Component comp : this.components) {
				height += comp.getPreferredSize().getHeight() + this.elementSeperatingSpaceY;
				width = Math.max(width, (int) comp.getPreferredSize().getWidth());
			}
		}
		return new Dimension(width, height);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		int width = parent.getWidth();
		if (width == 0)
			width = Integer.MAX_VALUE;
		int height = this.elementSeperatingSpaceY;
		synchronized (parent.getTreeLock()) {
			for (Component comp : this.components) {
				height += comp.getMinimumSize().getHeight() + this.elementSeperatingSpaceY;
				width = Math.max(width, (int) comp.getMinimumSize().getWidth());
			}
		}
		return new Dimension(width, height);
	}

	@Override
	public void layoutContainer(Container parent) {
		int width = 0;
		int yPos = this.elementSeperatingSpaceY;
		synchronized (parent.getTreeLock()) {
			width = (int) parent.getWidth();
			if (width == 0)
				width = Integer.MAX_VALUE;
			for (Component comp : this.components) {
				comp.setBounds(this.elementSeperatingSpaceX, yPos, width - (this.elementSeperatingSpaceX*2) , (int) comp.getPreferredSize().getHeight());
				yPos += comp.getPreferredSize().getHeight() + this.elementSeperatingSpaceY;
			}
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		this.components.add(comp);
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
		// TODO Auto-generated method stub

	}

}
