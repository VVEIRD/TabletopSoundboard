package vv3ird.ESDSoundboardApp.gui.pages;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PageViewer extends JFrame {

	private JPanel contentPane;
	
	private Stack<Page> history = new Stack<>();
	
	private Page page = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PageViewer frame = new PageViewer();
					frame.viewPage(new JNewSoundPage());
					frame.setVisible(true);
					frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				} catch (Exception e) {
					e.printStackTrace(); 
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PageViewer() {
		setBounds(100, 100, 620, 456);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		page = new Page();
		contentPane.add(page, BorderLayout.CENTER);
	}
	
	public void viewPage(Page page) {
		contentPane.remove(this.page);
		this.history.push(this.page);
		this.page = page;
		page.setPageView(this);
		this.setSize(page.getPageDimension());
		this.setBounds(this.getX(), this.getY(), page.getWidth(), page.getHeight());
		contentPane.add(page, BorderLayout.CENTER);
		revalidate();
	}
	
	public void back() {
		if (!history.isEmpty()) {
			Page p = history.pop();
			contentPane.remove(this.page);
			this.page = p;
			page.setPageView(this);
			this.setSize(page.getPageDimension());
			contentPane.add(page, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
	}

	public void close() {
		history.clear();
		setVisible(false);
		dispose();
	}
	
}
