package vv3ird.ESDSoundboardApp.gui.pages;

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
import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.gui.elements.IconSelectorPanel;
import vv3ird.ESDSoundboardApp.ngui.pages.Page;
import vv3ird.ESDSoundboardApp.player.AudioPlayer;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Dimension;

import javax.swing.JLabel;
import java.awt.Font;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

public class JNewSoundPage extends Page {
	
	Logger	logger = LogManager.getLogger(JNewSoundPage.class);
	
	static BufferedImage OK = null;
	static BufferedImage FALSE = null;
	static BufferedImage DEFAULT = null;
	
	static {
		
		try {
			OK = ImageIO.read(JNewSoundPage.class.getClassLoader().getResource("resources/icons/ok.png"));
			FALSE = ImageIO.read(JNewSoundPage.class.getClassLoader().getResource("resources/icons/false.png"));
			DEFAULT = ImageIO.read(JNewSoundPage.class.getClassLoader().getResource("resources/icons/audio.png"));
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

	/**
	 * Create the panel.
	 */
	public JNewSoundPage() {
		setLayout(new BorderLayout(0, 0));
		setSize(new Dimension(480, 290));
		
		JPanel panel_4 = new JPanel();
		add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));
		dlm = new DefaultListModel<>();
		
		JPanel panel_1 = new JPanel();
		panel_4.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.WEST);
		
		JButton btnBack = new JButton("Back");
		panel_2.add(btnBack);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.EAST);
		
		btnFinish = new JButton("Finish");
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					BufferedImage icon = iPanel.getImage();
					icon = icon == null ? DEFAULT : icon;
					String name = tfName.getText().trim();
					String audio = tfAudio.getText();
					Sound.Type type = rdbtnAmbience.isSelected() ? Type.AMBIENCE : Type.EFFECT;
					AudioApp.saveNewSound(name, icon, audio,  type);
				} catch (IOException e1) {
					logger.error(e1);
					JOptionPane.showMessageDialog(null, "Could not save Soundboard: " + e1.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_3.add(btnFinish);
		
		JPanel panel = new JPanel();
		panel_4.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblName.setBounds(10, 11, 46, 11);
		panel.add(lblName);
		
		tfName = new JTextField();
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
		tfName.setBounds(10, 22, 400, 20);
		panel.add(tfName);
		tfName.setColumns(10);
		
		lblNameOk =  new JLabel(new ImageIcon(FALSE));
		lblNameOk.setBounds(420, 22, 20, 20);
		panel.add(lblNameOk);
		
		iPanel = new IconSelectorPanel(DEFAULT);
		iPanel.setBounds(10, 46, 160, 147);
		panel.add(iPanel);
		
		tfAudio = new JTextField();
		tfAudio.setEditable(false);
		tfAudio.setBounds(180, 53, 260, 20);
		panel.add(tfAudio);
		tfAudio.setColumns(10);
		
		JButton btnSelectAudio = new JButton("Select Audio");
		btnSelectAudio.setBounds(180, 84, 260, 23);
		panel.add(btnSelectAudio);

		btnSelectAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openSound();
			}
		});
		JButton btnPlay = new JButton("Play");
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
					player = new AudioPlayer(new Sound(tfName.getText(), file.toString(), null, rdbtnAmbience.isSelected() ? Type.AMBIENCE : Type.EFFECT), AudioApp.getConfiguration().getMixerInfo());
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
		btnPlay.setBounds(180, 118, 91, 45);
		panel.add(btnPlay);
		
		rdbtnAmbience = new JRadioButton("Ambience");
		rdbtnAmbience.setBounds(277, 117, 109, 23);
		panel.add(rdbtnAmbience);
		rdbtnAmbience.setSelected(true);
		
		JRadioButton rdbtnEffect = new JRadioButton("Effect");
		rdbtnEffect.setBounds(277, 140, 109, 23);
		panel.add(rdbtnEffect);
		ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnAmbience);
		bg.add(rdbtnEffect);
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getPageViewer().back();
			}
		});

	}

	private void openSound() {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new FileNameExtensionFilter("Audio files", new String[] {"mp3", "wav"}));
		int returnValue = fc.showOpenDialog(this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();
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
	public JPanel getButtonBar() {
		// TODO Auto-generated method stub
		return null;
	}
}
