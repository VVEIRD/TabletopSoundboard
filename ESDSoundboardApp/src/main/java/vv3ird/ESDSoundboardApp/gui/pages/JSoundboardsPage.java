package vv3ird.ESDSoundboardApp.gui.pages;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vv3ird.ESDSoundboardApp.AudioApp;
import vv3ird.ESDSoundboardApp.config.SoundBoard;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class JSoundboardsPage extends Page {
	
	Logger	logger = LogManager.getLogger(JSoundboardsPage.class);
	
	private List<SoundBoard> sbs = null;
	private JLabel lblSoundboard;
	private JList<String> listSoundboards;
	DefaultListModel<String> dlm ;
	private JScrollPane spBoards; 

	public JSoundboardsPage(List<SoundBoard> sbs) {
		this();
		this.sbs = Objects.requireNonNull(sbs);
		for(SoundBoard cat : this.sbs) {
			dlm.addElement(cat.name);
		}
	}

	/**
	 * Create the panel.
	 */
	public JSoundboardsPage() {
		setLayout(new BorderLayout(0, 0));
		setSize(new Dimension(420, 300));
		
		lblSoundboard = new JLabel("Soundboards");
		lblSoundboard.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblSoundboard, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_4 = new JPanel();
		add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JLabel lblCategories = new JLabel("");
		panel_4.add(lblCategories, BorderLayout.NORTH);
		dlm = new DefaultListModel<>();
		
		listSoundboards = new JList<String>(dlm);
		spBoards = new JScrollPane(listSoundboards);
		panel_4.add(spBoards, BorderLayout.CENTER);
//		panel_4.add(listSoundboards, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_4.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.WEST);
		
		JButton btnCancel = new JButton("Cancel");
		panel_2.add(btnCancel);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.EAST);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Delete Soundboard " + listSoundboards.getSelectedValue() + "?","Warning", JOptionPane.OK_CANCEL_OPTION);
				if(dialogResult == JOptionPane.OK_OPTION){
					try {
						AudioApp.deleteSoundboard(AudioApp.getSoundboard(listSoundboards.getSelectedValue()));
						DefaultListModel<String> model = (DefaultListModel<String>) listSoundboards.getModel();
						int selectedIndex = listSoundboards.getSelectedIndex();
						if (selectedIndex != -1) {
						    model.remove(selectedIndex);
						}
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Could not delete Soundboard " + listSoundboards.getSelectedValue(), "Error deleting Soundboard", JOptionPane.WARNING_MESSAGE);
						e1.printStackTrace();
					}
				} 
			}
		});
		panel_3.add(btnDelete);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!listSoundboards.isSelectionEmpty()) {
					String cat = listSoundboards.getSelectedValue();
					JCategoriesPage cnPage = new JCategoriesPage(sbs.stream().filter(s -> s.name.equals(cat)).findFirst().orElse(null));
					getPageViewer().viewPage(cnPage);
				}
			}
		});
		panel_3.add(btnEdit);
		
		JPanel panel_5 = new JPanel();
		panel_3.add(panel_5);
		
		JButton btnFinish = new JButton("Save");
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sbs = new ArrayList<>(sbs);
					for (SoundBoard sb : sbs) {
						AudioApp.saveSoundBoard(sb);
					}
					getPageViewer().close();
				} catch (IOException e1) {
					logger.error(e1);
					JOptionPane.showMessageDialog(null, "Could not save Soundboards: " + e1.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_3.add(btnFinish);

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getPageViewer().close();
			}
		});

	}

}
