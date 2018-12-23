package vv3ird.ESDSoundboardApp.ngui.pages;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import vv3ird.ESDSoundboardApp.AudioApp;
import vv3ird.ESDSoundboardApp.config.AppConfiguration;
import vv3ird.ESDSoundboardApp.config.Sound;
import vv3ird.ESDSoundboardApp.config.Sound.Type;
import vv3ird.ESDSoundboardApp.ngui.components.IconSelectorPanel;
import vv3ird.ESDSoundboardApp.ngui.components.JSoundMetadataTemplatePanel;
import vv3ird.ESDSoundboardApp.ngui.util.ColorScheme;
import vv3ird.ESDSoundboardApp.player.AudioPlayer;
import vv3ird.ESDSoundboardApp.plugins.PluginManager;
import vv3ird.ESDSoundboardApp.plugins.data.SoundPluginMetadata;
import vv3ird.ESDSoundboardApp.plugins.data.SoundPluginMetadataTemplate;
import vv3ird.ESDSoundboardApp.plugins.data.exceptions.ValueNotInMetadataListException;
import vv3ird.ESDSoundboardApp.plugins.data.exceptions.WrongSoundPluginMetadataTypeException;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import java.awt.Font;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import java.awt.Color;
import javax.swing.JSpinner;

public class JCreateSoundPage extends Page {
	
	private static File lastDirectory = null;
	
	Logger	logger = LogManager.getLogger(JCreateSoundPage.class);
	
	static BufferedImage OK = null;
	static BufferedImage FALSE = null;
	static BufferedImage DEFAULT = null;
	
	static {
		
		try {
			OK = ImageIO.read(JCreateSoundPage.class.getClassLoader().getResource("resources/icons/ok.png"));
			FALSE = ImageIO.read(JCreateSoundPage.class.getClassLoader().getResource("resources/icons/false.png"));
			DEFAULT = ImageIO.read(JCreateSoundPage.class.getClassLoader().getResource("resources/icons/audio.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	DefaultListModel<String> dlm ;
	private JTextField tfName;
	private JLabel lblNameOk;
	private JButton btnFinish ;
	private JTextField tfAudio;
	private AudioPlayer player = null;
	private IconSelectorPanel iPanel;
	private JRadioButton rdbtnAmbience;
	private JPanel pnStatus;
	private JTextField tfTags;
	private JComboBox cbPlugins;
	private JButton btnAddPluginMetadata;

	private Map<String, List<SoundPluginMetadataTemplate>> availableTemplates = null;
	
	private Map<String, List<SoundPluginMetadataTemplate>> configuredTemplates = null;
	
	private Map<String, JPanel> configuredTemplatePanels = null;
	private JPanel panel;

	/**
	 * Create the panel.
	 */
	public JCreateSoundPage() {
		setLayout(new BorderLayout(0, 0));
		setSize(new Dimension(656, 435));
		setOpaque(false);

		configuredTemplates = new HashMap<>();
		
		configuredTemplatePanels = new HashMap<>();

		dlm = new DefaultListModel<>();
		
		pnStatus = new JPanel();
//		panel_4.add(pnStatus, BorderLayout.SOUTH);
		pnStatus.setLayout(new BorderLayout(0, 0));
		
		JButton btnBack = new JButton("Back");
		pnStatus.add(btnBack, BorderLayout.WEST);
		
		btnFinish = new JButton("Finish");
		pnStatus.add(btnFinish, BorderLayout.EAST);
		pnStatus.setOpaque(false);
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.setOpaque(false);
		add(panel, BorderLayout.CENTER);
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblName.setBounds(10, 11, 46, 11);
		panel.add(lblName);
		
		tfName = new JTextField();
		tfName.setForeground(ColorScheme.FOREGROUND_COLOR);
		tfName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tfName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tfName.setBackground(ColorScheme.MAIN_BACKGROUND_COLOR.darker());
		tfName.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				assertName();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				assertName();				
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				assertName();				
			}
		});
		tfName.setBounds(10, 22, 400, 25);
		panel.add(tfName);
		tfName.setColumns(10);
		
		lblNameOk =  new JLabel(new ImageIcon(FALSE));
		lblNameOk.setBounds(420, 22, 20, 20);
		panel.add(lblNameOk);
		
		iPanel = new IconSelectorPanel(DEFAULT);
		iPanel.setBounds(10, 58, 160, 151);
		panel.add(iPanel);
		
		tfAudio = new JTextField();
		tfAudio.setForeground(ColorScheme.FOREGROUND_COLOR);
		tfAudio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tfAudio.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tfAudio.setBackground(ColorScheme.MAIN_BACKGROUND_COLOR.darker());
		tfAudio.setEditable(false);
		tfAudio.setBounds(180, 58, 260, 25);
		panel.add(tfAudio);
		tfAudio.setColumns(10);
		
		JButton btnSelectAudio = new JButton("Select Audio");
		btnSelectAudio.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		btnSelectAudio.setOpaque(false);
		btnSelectAudio.setBounds(180, 94, 260, 23);
		panel.add(btnSelectAudio);

		btnSelectAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openSound();
			}
		});
		JButton btnPlay = new JButton("Play");
		btnPlay.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(player != null && player.isActive())
					stopAudio();
				else
					playAudio();
			}

			private void stopAudio() {
				player.stop(false);
				player.close();
//				player = null;
				btnPlay.setText("Play");
			}

			private void playAudio() {
				if(player != null && player.isActive())
					player.close();
				File file = new File(tfAudio.getText());
				if(file.exists()) {
					player = new AudioPlayer(new Sound(tfName.getText(), file.toString(), null, rdbtnAmbience.isSelected() ? Type.AMBIENCE : Type.EFFECT, null), AudioApp.getConfiguration().getMixerInfo());
					try {
						player.open();
						float gain = AppConfiguration.linearToDecibel(AudioApp.getConfiguration().masterGain) + 2;
						logger.debug("Setting gain for ambience player: " + gain);
						player.setGain(gain);
						player.start();
						btnPlay.setText("Playing");
					} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
						logger.error("Error opening audio file: " + player.getAudioFile());
						logger.error(e);
						e.printStackTrace();
					}
				}
			}
		});
		btnPlay.setBounds(180, 128, 91, 45);
		panel.add(btnPlay);
		
		rdbtnAmbience = new JRadioButton("Ambience");
		rdbtnAmbience.setOpaque(false);
		rdbtnAmbience.setBounds(277, 124, 109, 23);
		panel.add(rdbtnAmbience);
		rdbtnAmbience.setSelected(true);
		
		JRadioButton rdbtnEffect = new JRadioButton("Effect");
		rdbtnEffect.setOpaque(false);
		rdbtnEffect.setBounds(277, 147, 109, 23);
		panel.add(rdbtnEffect);
		ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnAmbience);
		bg.add(rdbtnEffect);
		
		tfTags = new JTextField();
		tfTags.setForeground(ColorScheme.FOREGROUND_COLOR);
		tfTags.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tfTags.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tfTags.setBackground(ColorScheme.MAIN_BACKGROUND_COLOR.darker());
		tfTags.setBounds(180, 184, 260, 25);
		panel.add(tfTags);
		tfTags.setColumns(10);
		
		JLabel lblTags = new JLabel("Tags");
		lblTags.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTags.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblTags.setBounds(394, 171, 46, 11);
		panel.add(lblTags);
		
		JLabel lblPlugins = new JLabel("Plugin Metadata");
		lblPlugins.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblPlugins.setBounds(10, 220, 206, 11);
		panel.add(lblPlugins);
		availableTemplates = PluginManager.getSoundPluginMetadataTemplates();
		cbPlugins = new JComboBox(availableTemplates.keySet().toArray(new Object[0]));
		cbPlugins.setBounds(10, 234, 376, 22);
		panel.add(cbPlugins);
		
		btnAddPluginMetadata = new JButton("Add");
		btnAddPluginMetadata.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				addPluginMetadata((String)cbPlugins.getSelectedItem(), availableTemplates.get(cbPlugins.getSelectedItem()));
			}
		});
		btnAddPluginMetadata.setOpaque(false);
		btnAddPluginMetadata.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		btnAddPluginMetadata.setBounds(394, 234, 46, 23);
		panel.add(btnAddPluginMetadata);
		
	}
	
	
	private void addPluginMetadata(String plugin, List<SoundPluginMetadataTemplate> templates) {
		logger.debug("Adding Metadata for " + plugin);
		int panelStartY = 270;
		int templateStartY = 30;
		int templateIncrease = 35;
		if(configuredTemplates.containsKey(plugin))
			return;
		configuredTemplates.put(plugin, templates);
		JPanel pnPlugin = new JPanel();
		pnPlugin.setLayout(null);
		pnPlugin.setOpaque(false);
		

		JLabel lblPluginName = new JLabel(plugin);
		lblPluginName.setBounds(10, 11, 410, 14);
		lblPluginName.setFont(lblPluginName.getFont().deriveFont(Font.BOLD));
		pnPlugin.add(lblPluginName);
		
		int tIndex = 0;
		for (SoundPluginMetadataTemplate template : templates) {
			JSoundMetadataTemplatePanel pn = new JSoundMetadataTemplatePanel(template);
			pn.setBounds(10, templateStartY + (templateIncrease*tIndex++), 430, 30);
			pn.setOpaque(false);
			pnPlugin.add(pn);
		}
		pnPlugin.setBounds(10, (configuredTemplates.size()*panelStartY) + ((configuredTemplates.size()-1)*10), 430, templateStartY + (templates.size() * templateIncrease));
		panel.add(pnPlugin);
		panel.revalidate();
		panel.repaint();
		configuredTemplatePanels.put(plugin, pnPlugin);
	}

	private void openSound() {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new FileNameExtensionFilter("Audio files", new String[] {"mp3", "wav"}));
		if(lastDirectory != null)
			fc.setCurrentDirectory(lastDirectory);
		int returnValue = fc.showOpenDialog(this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();
			lastDirectory = selectedFile.getParentFile();
			Mp3File song;
			if(selectedFile.toString().toLowerCase().endsWith(".mp3"))
				try {
					song = new Mp3File(selectedFile.getAbsolutePath());
					if (song.hasId3v2Tag()){
					     ID3v2 id3v2tag = song.getId3v2Tag();
					     byte[] imageData = id3v2tag.getAlbumImage();
					     if (imageData != null)
						     try {
								BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
								iPanel.setImage(img);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
				} catch (UnsupportedTagException e1) {
					e1.printStackTrace();
				} catch (InvalidDataException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			tfAudio.setText(selectedFile.getAbsoluteFile().toString());
		}
	}
	
	private void assertName() {
		if(AudioApp.getSoundLibrary().stream().anyMatch(s -> s.getName().equalsIgnoreCase(tfName.getText().trim())) || tfName.getText().isEmpty()) {
			lblNameOk.setIcon(new ImageIcon(FALSE));
			btnFinish.setEnabled(false);
		}
		else {
			lblNameOk.setIcon(new ImageIcon(OK));
			btnFinish.setEnabled(true);
		}
		lblNameOk.validate();
		lblNameOk.repaint();
	}
	
	@Override
	protected void okAction() {
		try {
			BufferedImage icon = iPanel.getImage();
			icon = icon == null ? DEFAULT : icon;
			String name = tfName.getText().trim();
			String audio = tfAudio.getText();
			String[] tags =  tfTags.getText().split(" ");
			Sound.Type type = rdbtnAmbience.isSelected() ? Type.AMBIENCE : Type.EFFECT;
			Map<String, List<SoundPluginMetadata>> metadata = new HashMap<>();
			for (String meta : configuredTemplates.keySet()) {
				List<SoundPluginMetadata> data = new LinkedList<>();
				JPanel p = configuredTemplatePanels.get(meta);
				for(int i=0;i<p.getComponentCount();i++) {
					if (p.getComponent(i) instanceof JSoundMetadataTemplatePanel) {
						JSoundMetadataTemplatePanel tp = (JSoundMetadataTemplatePanel) p.getComponent(i);
						SoundPluginMetadata md = tp.createMetadata();
						data.add(md);
					}
				}
				if(data.size() > 0)
					metadata.put(data.get(0).pluginClass, data);
			}
			AudioApp.saveNewSound(name, icon, audio,  type, tags, metadata);
			pageViewer.back();
		} catch (IOException e1) {
			logger.error(e1);
			JOptionPane.showMessageDialog(null, "Could not save Soundboard: " + e1.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
		} catch (ValueNotInMetadataListException | WrongSoundPluginMetadataTypeException e) {
			logger.error(e);
			JOptionPane.showMessageDialog(null, "Metadata are not correct: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	@Override
	protected void cancelAction() {
		getPageViewer().back();
	}
}
