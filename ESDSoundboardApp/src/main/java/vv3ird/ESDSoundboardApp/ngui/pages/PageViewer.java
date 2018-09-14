package vv3ird.ESDSoundboardApp.ngui.pages;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import vv3ird.ESDSoundboardApp.ngui.ColorScheme;

public class PageViewer extends JPanel {

	private static final long serialVersionUID = 2872963373388003141L;

	
	private Stack<Page> history = new Stack<>();
	
	private Page page = null;
	
	private JPanel statusBar = null;

	/**
	 * Create the frame.
	 */
	public PageViewer() {
		setLayout(new BorderLayout(0, 0));
		page = new Page() {
			@Override
			public JPanel getButtonBar() {return null;}
			@Override
			protected void cancelAction() {}
			@Override
			protected void okAction() {}
		};
		add(page, BorderLayout.CENTER);
	}
	
	public void viewPage(Page page) {
		remove(this.page);
		this.history.push(this.page);
		this.page = page;
		page.setPageView(this);
		add(page, BorderLayout.CENTER);
		JPanel bb = page.getButtonBar();
		bb = bb != null ? bb : new JPanel();
		bb.setBackground(ColorScheme.SIDE_BAR_BACKGROUND_COLOR);
		if(statusBar != null) {
			remove(statusBar);
		}
		statusBar = bb;
		add(bb, BorderLayout.SOUTH);
		revalidate();
	}
	
	public void back() {
		if (!history.isEmpty()) {
			Page p = history.pop();
			remove(this.page);
			this.page = p;
			page.setPageView(this);
			add(page, BorderLayout.CENTER);
			JPanel bb = page.getButtonBar();
			bb = bb != null ? bb : new JPanel();
			bb.setBackground(ColorScheme.SIDE_BAR_BACKGROUND_COLOR);
			if(statusBar != null) {
				remove(statusBar);
			}
			statusBar = bb;
			add(bb, BorderLayout.SOUTH);
			revalidate();
			repaint();
		}
	}
	
}
