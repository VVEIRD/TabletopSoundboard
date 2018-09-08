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
import java.util.Objects;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class JCategoriesPage extends Page {
	
	Logger	logger = LogManager.getLogger(JCategoriesPage.class);
	
	private SoundBoard sb = null;
	private JLabel lblSoundboard;
	private JList<String> listCategories;
	DefaultListModel<String> dlm ;
	private JScrollPane spCat;

	public JCategoriesPage(SoundBoard sb) {
		this();
		this.sb = Objects.requireNonNull(sb);
		lblSoundboard.setText(lblSoundboard.getText() + " " + sb.name);
		for(String cat : this.sb.getCategories()) {
			dlm.addElement(cat);
		}
	}

	/**
	 * Create the panel.
	 */
	public JCategoriesPage() {
		setLayout(new BorderLayout(0, 0)); 
		setSize(new Dimension(420, 300));
		
		lblSoundboard = new JLabel("Soundboard: ");
		lblSoundboard.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblSoundboard, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_4 = new JPanel();
		add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JLabel lblCategories = new JLabel("Categories:");
		panel_4.add(lblCategories, BorderLayout.NORTH); 
		dlm = new DefaultListModel<>();
		listCategories = new JList<String>(dlm);
		
		spCat = new JScrollPane(listCategories);
		panel_4.add(spCat, BorderLayout.CENTER);
//		panel_4.add(listCategories, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_4.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.WEST);
		
		JButton btnBack = new JButton("Back");
		panel_2.add(btnBack);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.EAST);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(pageViewer, "Delete Category " + listCategories.getSelectedValue() + "?","Warning", JOptionPane.OK_CANCEL_OPTION);
				if(dialogResult == JOptionPane.OK_OPTION){
					sb.removeCategory(listCategories.getSelectedValue());
					DefaultListModel<String> model = (DefaultListModel<String>) listCategories.getModel();
					int selectedIndex = listCategories.getSelectedIndex();
					if (selectedIndex != -1) {
					    model.remove(selectedIndex);
					}
				}
			}
		});
		panel_3.add(btnDelete);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!listCategories.isSelectionEmpty()) {
					String cat = listCategories.getSelectedValue();
					JCategoryNamePage cnPage = new JCategoryNamePage(cat, sb);
					getPageViewer().viewPage(cnPage);
				}
			}
		});
		panel_3.add(btnEdit);
		
		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCategoryNamePage cnPage = new JCategoryNamePage(null, sb);
				getPageViewer().viewPage(cnPage);
			}
		});
		panel_3.add(btnNew);
		
		JPanel panel_5 = new JPanel();
		panel_3.add(panel_5);
		
		JButton btnFinish = new JButton("Finish");
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AudioApp.saveSoundBoard(sb);
					getPageViewer().close();
				} catch (IOException e1) {
					logger.error(e1);
					JOptionPane.showMessageDialog(null, "Could not save Soundboard: " + e1.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_3.add(btnFinish);

		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getPageViewer().back();
			}
		});

	}

}
