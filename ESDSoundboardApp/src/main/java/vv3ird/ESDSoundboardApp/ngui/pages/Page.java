package vv3ird.ESDSoundboardApp.ngui.pages;

import java.awt.Dimension;

import javax.swing.JPanel;

public abstract class Page extends JPanel {
	
	protected Dimension d = new Dimension(0, 0);

	protected PageViewer pageViewer = null;
	
	/**
	 * Create the panel.
	 */
	public Page() { 

	}
	
	public void setPageView(PageViewer pageViewer) {
		this.pageViewer = pageViewer; 
	}
	
	public PageViewer getPageViewer() {
		return pageViewer;
	}
	
	@Override
	public void setSize(Dimension d) {
		d = new Dimension(d);
		super.setSize(d);
	}
	
	@Override
	public void setSize(int width, int height) {
		d = new Dimension(width, height);
		super.setSize(width, height);
	}
	
	public Dimension getPageDimension() {
		return d;
	}
	
	public abstract JPanel getButtonBar();
}
