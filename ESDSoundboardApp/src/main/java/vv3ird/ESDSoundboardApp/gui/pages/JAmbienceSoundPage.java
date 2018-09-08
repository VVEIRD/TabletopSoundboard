package vv3ird.ESDSoundboardApp.gui.pages;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JList;
import java.awt.Font;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import vv3ird.ESDSoundboardApp.config.Sound;
import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.config.Sound.Type;
import vv3ird.ESDSoundboardApp.AudioApp;

import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class JAmbienceSoundPage extends Page {
	private JTextField tfAvailableFilter;
	private JTextField tfSelectedFilter;
	private SoundBoard sb;
	List<Sound> availableSounds = new LinkedList<>();
	private JList<Sound> listAvailable;
	private JList<Sound> listSelected;
	List<Sound> selectedSounds = new LinkedList<>();
	private DefaultListModel<Sound> dlmAvailable = new DefaultListModel<>();
	private DefaultListModel<Sound> dlmSelected = new DefaultListModel<>();
	private JLabel lblCategoryaddAmbience;
	private String category;

	/**
	 * Create the panel.
	 */
	public JAmbienceSoundPage() {
		setLayout(new BorderLayout(0, 0));
		setSize(new Dimension(480, 350));
		
		lblCategoryaddAmbience = new JLabel("Category: , Add Effect Sounds");
		lblCategoryaddAmbience.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblCategoryaddAmbience, BorderLayout.NORTH);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		add(splitPane, BorderLayout.CENTER);
		
		JPanel panel_4 = new JPanel();
		splitPane.setLeftComponent(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_6 = new JPanel();
		panel_4.add(panel_6, BorderLayout.NORTH);
		panel_6.setLayout(new BoxLayout(panel_6, BoxLayout.X_AXIS));
		
		JLabel lblAvailable = new JLabel("Available"); 
		panel_6.add(lblAvailable);
		
		tfAvailableFilter = new JTextField();
		tfAvailableFilter.getDocument().addDocumentListener(new DocumentListener(){
            @Override public void insertUpdate(DocumentEvent e) { filter(); }
            @Override public void removeUpdate(DocumentEvent e) { filter(); }
            @Override public void changedUpdate(DocumentEvent e) {filter();}
        });
		panel_6.add(tfAvailableFilter);
		tfAvailableFilter.setColumns(10);

		listAvailable = new JList<Sound>(dlmAvailable);
		JScrollPane spAvail = new JScrollPane(listAvailable);
		spAvail.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spAvail.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel_4.add(spAvail, BorderLayout.CENTER);
		
//		panel_4.add(listAvailable, BorderLayout.CENTER);
		
		JPanel panel_5 = new JPanel();
		splitPane.setRightComponent(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		listSelected = new JList<Sound>(dlmSelected);
		JScrollPane spSel = new JScrollPane(listSelected);
		spSel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spSel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel_5.add(spSel, BorderLayout.CENTER);
		
//		panel_5.add(listSelected, BorderLayout.CENTER);
		
		JPanel panel_7 = new JPanel();
		panel_5.add(panel_7, BorderLayout.NORTH);
		panel_7.setLayout(new BoxLayout(panel_7, BoxLayout.X_AXIS));
		
		JLabel lblSelected = new JLabel("Selected");
		panel_7.add(lblSelected);
		
		tfSelectedFilter = new JTextField();
		tfSelectedFilter.getDocument().addDocumentListener(new DocumentListener(){
            @Override public void insertUpdate(DocumentEvent e) { filter(); }
            @Override public void removeUpdate(DocumentEvent e) { filter(); }
            @Override public void changedUpdate(DocumentEvent e) {filter();}
        });
		panel_7.add(tfSelectedFilter);
		tfSelectedFilter.setColumns(10);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.add(panel_1, BorderLayout.WEST);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getPageViewer().back();
			}
		});
		panel_1.add(btnBack);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.EAST);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JEffectSoundPage esPage = new JEffectSoundPage(category, selectedSounds, sb);
				getPageViewer().viewPage(esPage);
			}
		});
		panel_2.add(btnNext);
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.CENTER);
		
		JButton button_3 = new JButton("<");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Sound> sounds = listSelected.getSelectedValuesList();
				for (Sound sound : sounds) {
					dlmSelected.removeElement(sound);
					selectedSounds.remove(sound);
					dlmAvailable.addElement(sound);
					availableSounds.add(sound);
				}
				filter();
			}
		});
		panel_3.add(button_3);
		
		JButton button_2 = new JButton("<<");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i=dlmSelected.getSize()-1;i>=0;i--) {
					Sound sound = dlmSelected.get(i);
					dlmAvailable.addElement(sound);
					availableSounds.add(sound);
					dlmSelected.removeElement(sound);
					selectedSounds.remove(sound);
				}
				filter();
			}
		});
		panel_3.add(button_2);
		
		JButton button_1 = new JButton(">>");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i=dlmAvailable.getSize()-1;i>=0;i--) {
					Sound sound = dlmAvailable.get(i);
					dlmAvailable.removeElement(sound);
					availableSounds.remove(sound);
					dlmSelected.addElement(sound);
					selectedSounds.add(sound);
				}
				filter();
			}
		});
		panel_3.add(button_1);
		
		JButton button = new JButton(">");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Sound> sounds = listAvailable.getSelectedValuesList();
				for (Sound sound : sounds) {
					dlmAvailable.removeElement(sound);
					availableSounds.remove(sound);
					dlmSelected.addElement(sound);
					selectedSounds.add(sound);
				}
				filter();
			}
		});
		panel_3.add(button);

	}
	
	public void filterAvailableModel(String filter) {
		DefaultListModel<Sound> model = dlmAvailable;
	    for (Sound s : availableSounds) {
	        if (!s.name.contains(filter)) {
	            if (model.contains(s)) {
	                model.removeElement(s);
	            }
	        } else {
	            if (!model.contains(s)) {
	                model.addElement(s);
	            }
	        }
	    }
	}
	
	private void filter() {
	    String filter = tfAvailableFilter.getText();
	    filterAvailableModel(filter);
	    filter = tfSelectedFilter.getText();
	    filterSelectedModel(filter);
	}
	
	public void filterSelectedModel(String filter) {
		DefaultListModel<Sound> model = dlmSelected;
	    for (Sound s : selectedSounds) {
	        if (!s.name.contains(filter)) {
	            if (model.contains(s)) {
	                model.removeElement(s);
	            }
	        } else {
	            if (!model.contains(s)) {
	                model.addElement(s);
	            }
	        }
	    }
	}

	public JAmbienceSoundPage(String category, SoundBoard sb) {
		this();
		this.sb = sb;
		this.category = category;
		lblCategoryaddAmbience.setText("Category: " + category + ", Add Ambience Sounds");
		List<Sound> sounds = new ArrayList<>(AudioApp.getAmbienceSounds());
		availableSounds.addAll(sounds);
		List<Sound> preSelected = this.sb.getAmbienceSounds(category);
		List<Sound> selected = new LinkedList<>(); 
		if (preSelected != null)
			selected.addAll(preSelected);
		sounds.removeAll(selected);
		for (Sound sound : selected) {
			dlmSelected.addElement(sound);
		}
		for (Sound sound : sounds) {
			dlmAvailable.addElement(sound);
		}
	}

}
