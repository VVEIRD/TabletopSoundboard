package vv3ird.ESDSoundboardApp.gui.pages;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.ngui.pages.Page;

import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JCategoryNamePage extends Page {
	private JTextField tfCategoryName;
	private SoundBoard sb;

	/**
	 * Create the panel.
	 */
	public JCategoryNamePage() {
		setLayout(new BorderLayout(0, 0)); 
		setSize(new Dimension(300, 130));
		
		JLabel lblCategoryName = new JLabel("Category name:");
		add(lblCategoryName, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.WEST);
		
		JButton btnBack = new JButton("Back");
		panel_1.add(btnBack);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.EAST);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tfCategoryName.getText().trim().length() > 0) {
					JAmbienceSoundPage asPage = new JAmbienceSoundPage(tfCategoryName.getText().trim(), sb);
					getPageViewer().viewPage(asPage);
				}
			}
		});
		panel_2.add(btnNext);
		
		JPanel panel_3 = new JPanel();
		add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));
		
		tfCategoryName = new JTextField();
		tfCategoryName.setMaximumSize(new Dimension(6000, 20));
		tfCategoryName.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(tfCategoryName);
		tfCategoryName.setColumns(10);

		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getPageViewer().back();
			}
		});

	}

	public JCategoryNamePage(String category, SoundBoard sb) {
		this();
		this.sb = sb;
		if(category != null)
			tfCategoryName.setText(category);
		
	}

	@Override
	public JPanel getButtonBar() {
		// TODO Auto-generated method stub
		return null;
	}
}
