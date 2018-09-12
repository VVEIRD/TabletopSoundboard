package vv3ird.ESDSoundboardApp.gui.pages;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTextField;

import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.ngui.pages.Page;
import vv3ird.ESDSoundboardApp.AudioApp;

import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JCreateSoundBoard extends Page {
	private JTextField tfSoundBoard;

	/**
	 * Create the panel.
	 */
	public JCreateSoundBoard() {
		setLayout(new BorderLayout(0, 0));
		setSize(new Dimension(300, 130));
		
		JLabel lblCreateANew = new JLabel("Create a new SoundBoard");
		lblCreateANew.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblCreateANew, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.WEST);
		
		JButton btnCancel = new JButton("Cancel");
		panel_2.add(btnCancel);
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.EAST);
		
		JButton btnNext = new JButton("Next");
		panel_3.add(btnNext);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tfSoundBoard.getText().trim().length() > 0 && AudioApp.getSoundboard(tfSoundBoard.getText().trim()) == null) {
					SoundBoard sb = new SoundBoard(tfSoundBoard.getText().trim());
					JCategoriesPage cPage = new JCategoriesPage(sb);
					getPageViewer().viewPage(cPage);
				}
			}
		});
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		tfSoundBoard = new JTextField();
		tfSoundBoard.setMaximumSize(new Dimension(6000, 20));
		panel_1.add(tfSoundBoard);
		tfSoundBoard.setColumns(10);

	}

	@Override
	public JPanel getButtonBar() {
		// TODO Auto-generated method stub
		return null;
	}
}
