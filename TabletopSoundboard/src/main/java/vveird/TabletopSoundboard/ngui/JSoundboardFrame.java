package vveird.TabletopSoundboard.ngui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import de.rcblum.stream.deck.device.general.SoftStreamDeck;
import vveird.TabletopSoundboard.AudioApp;
import vveird.TabletopSoundboard.ngui.components.JSelectablePanel;
import vveird.TabletopSoundboard.ngui.layout.UIColumnLayout;
import vveird.TabletopSoundboard.ngui.pages.JOptionsPage;
import vveird.TabletopSoundboard.ngui.pages.JSoundPage;
import vveird.TabletopSoundboard.ngui.pages.JSoundboardPage;
import vveird.TabletopSoundboard.ngui.pages.JSpotifySoundPage;
import vveird.TabletopSoundboard.ngui.pages.Page;
import vveird.TabletopSoundboard.ngui.pages.PageViewer;
import vveird.TabletopSoundboard.ngui.util.ColorScheme;

public class JSoundboardFrame extends JFrame {

	private JPanel contentPane;
	private JPanel pnSideBar;
	private JSelectablePanel pnSoundBoards;
	private JLabel lblSoundBoards;
	private JSelectablePanel pnSounds;
	private JLabel lblSounds;
	private JSelectablePanel pnOptions;
	private JLabel lblOptions;
	private JScrollPane scrollPane;
	private PageViewer pnContent;
	private JPanel pnStatus;
	private JLabel lblShowSoftdecks;
	private JSelectablePanel pnSpotifySounds;

	/**
	 * Launch the application.
	 * @throws ClassNotFoundException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public static void init(String[] args) {
		try {
			// Set system-platform Java L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
//		AudioApp.main(args);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JSoundboardFrame frame = new JSoundboardFrame();
					frame.setVisible(true);
					frame.setAlwaysOnTop(true);
					frame.setAlwaysOnTop(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JSoundboardFrame() {
		super("Soundboard App");
		setTitle("Soundboard App" + (AudioApp.isSpotifyEnabled() ? " (Spotify enabled)" : ""));
		pnContent = new PageViewer();
		pnContent.setOpaque(false);
		setBackground(ColorScheme.MAIN_BACKGROUND_COLOR);
		getContentPane().setBackground(ColorScheme.MAIN_BACKGROUND_COLOR);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 910, 520);
		
		((JPanel) getContentPane()).setBorder(new EmptyBorder(0, 0, 0, 0));
		getContentPane().setLayout(new BorderLayout(0, 0));

		getContentPane().add(pnContent, BorderLayout.CENTER);
		pnSideBar = new JPanel();
		pnSideBar.setBackground(ColorScheme.SIDE_BAR_BACKGROUND_COLOR);
		setSize(pnSideBar, 200, 450);
		pnContent.add(pnSideBar, BorderLayout.WEST);
		pnSideBar.setLayout(new UIColumnLayout());
		
		pnSoundBoards = new JSelectablePanel(false);
		pnSoundBoards.setBounds(0, 0, 200, 40);
		pnSoundBoards.setPreferredSize(new Dimension(200, 40));
		setSize(pnSoundBoards, 200, 40);
		pnSideBar.add(pnSoundBoards);
		pnSoundBoards.setLayout(null);
		
		lblSoundBoards = new JLabel("Soundboards");
		lblSoundBoards.setHorizontalAlignment(SwingConstants.CENTER);
		lblSoundBoards.setBounds(10, 0, 180, 40);
		lblSoundBoards.setOpaque(false);
		pnSoundBoards.add(lblSoundBoards);
		pnSoundBoards.setBackground(ColorScheme.SIDE_BAR_BACKGROUND_COLOR);
		
		pnSounds = new JSelectablePanel(false);
		pnSounds.setLayout(null);
		pnSounds.setBackground(ColorScheme.SIDE_BAR_BACKGROUND_COLOR);
		pnSounds.setBounds(0, 40, 200, 40);
		pnSounds.setPreferredSize(new Dimension(200, 40));
		pnSideBar.add(pnSounds);
		
		lblSounds = new JLabel("Sounds");
		lblSounds.setOpaque(false);
		lblSounds.setHorizontalAlignment(SwingConstants.CENTER);
		lblSounds.setBounds(10, 0, 180, 40);
		pnSounds.add(lblSounds);
		
		
		pnSpotifySounds = new JSelectablePanel("Spotify Playlists", false);
		if (AudioApp.isSpotifyEnabled()) {
			pnSpotifySounds.setBounds(0, 80, 200, 40);
			pnSpotifySounds.setPreferredSize(new Dimension(200, 40));
			pnSideBar.add(pnSpotifySounds);
		}
		
		pnOptions = new JSelectablePanel(false);
		pnOptions.setLayout(null);
		pnOptions.setBackground(new Color(0, 81, 108));
		pnOptions.setPreferredSize(new Dimension(200, 40));
		pnOptions.setBounds(0, 120, 200, 40);
		JSelectablePanel.JSelectablePanelGroup pnGroup = new JSelectablePanel.JSelectablePanelGroup();
		pnGroup.add(pnSoundBoards);
		pnGroup.add(pnSounds);
		pnGroup.add(pnSpotifySounds);
		pnGroup.add(pnOptions);
		pnSideBar.add(pnOptions);
		
		JSelectablePanel pnDecks = new JSelectablePanel();
		pnDecks.setSelected(false);
		pnDecks.addSelectionListener(new JSelectablePanel.SelectionListener() {
			
			@Override
			public void selectionChanged(JSelectablePanel source, boolean newValue) {
				if(newValue)
					SoftStreamDeck.showDecks();
				else
					SoftStreamDeck.hideDecks();
			}
		});
		pnDecks.setLayout(null);
		pnDecks.setBounds(0, 410, 200, 40);
		pnDecks.setPreferredSize(new Dimension(200, 40));
		JPanel filler = new JPanel();
		filler.setPreferredSize(new Dimension(40, 250));
		filler.setOpaque(false);
		pnSideBar.add(filler);
		pnSideBar.add(pnDecks);
		
		lblShowSoftdecks = new JLabel("Show SoftDecks");
		lblShowSoftdecks.setOpaque(false);
		lblShowSoftdecks.setHorizontalAlignment(SwingConstants.CENTER);
		lblShowSoftdecks.setForeground(Color.LIGHT_GRAY);
		lblShowSoftdecks.setBounds(10, 0, 180, 40);
		pnDecks.add(lblShowSoftdecks);
		
		lblOptions = new JLabel("Options");
		lblOptions.setOpaque(false);
		lblOptions.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptions.setBounds(10, 0, 180, 40);
		JSelectablePanel.SelectionListener sl = new JSelectablePanel.SelectionListener() {
			@Override
			public void selectionChanged(JSelectablePanel source, boolean newValue) {
				if(newValue) {
					switchContent();
				}
			}
		};
		pnOptions.add(lblOptions);
		this.pnSounds.addSelectionListener(sl);
		this.pnSpotifySounds.addSelectionListener(sl);
		this.pnSoundBoards.addSelectionListener(sl);
		this.pnOptions.addSelectionListener(sl);
		JSoundboardFrame.this.pnSoundBoards.setSelected(true);
		lblSoundBoards.setForeground(ColorScheme.SIDE_BAR_FOREGROUND_COLOR);
		lblSounds.setForeground(ColorScheme.SIDE_BAR_FOREGROUND_COLOR);
		lblOptions.setForeground(ColorScheme.SIDE_BAR_FOREGROUND_COLOR);
		
		//setResizable(false);
		switchContent();
	}

	private void switchContent() {
		displaySoundBoards();
	}

	private void displaySoundBoards() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (pnSoundBoards.isSelected()) {
					Page p = new JSoundboardPage();
					pnContent.viewPage(p);
				} else if (pnSounds.isSelected()) {
					Page p = new JSoundPage();
					pnContent.viewPage(p);
				}else if (pnOptions.isSelected()) {
					Page p = new JOptionsPage();
					pnContent.viewPage(p);
				}else if (pnSpotifySounds.isSelected()) {
					Page p = new JSpotifySoundPage();
					pnContent.viewPage(p);
				}
			}

		});
	}

	private void setSize(Component comp, int width, int height) {
		comp.setPreferredSize(new Dimension(width, height));
		comp.setSize(new Dimension(width, height));
		comp.setMinimumSize(new Dimension(width, height));
		//comp.setMaximumSize(new Dimension(width, height));
	}
}
