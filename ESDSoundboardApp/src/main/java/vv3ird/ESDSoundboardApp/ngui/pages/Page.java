package vv3ird.ESDSoundboardApp.ngui.pages;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class Page extends JPanel {
	
	protected Dimension d = new Dimension(0, 0);

	protected PageViewer pageViewer = null;

	protected JButton ok = new JButton("Ok");
	
	protected JButton cancel = new JButton("Cancel");
	
	/**
	 * Create the panel.
	 */
	public Page() { 
		ok.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okAction();
			}
		});
		ok.setBorderPainted(false);
		ok.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		
		// Cancel Button
		cancel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelAction();
			}
		});
		cancel.setBorderPainted(false);
		cancel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

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
	
	protected abstract void okAction();
	
	protected abstract void cancelAction();

	public JPanel getButtonBar() {
		JPanel bb = new JPanel();
		bb.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bb.add(cancel);
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(5, 5));
		p.setOpaque(false);
		bb.add(p);
		bb.add(ok);
		return bb;
	}
}
