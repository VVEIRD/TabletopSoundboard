package vv3ird.ESDSoundboardApp.ngui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.rcblum.stream.deck.device.SoftStreamDeck;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.awt.CardLayout;
import net.miginfocom.swing.MigLayout;
import vv3ird.ESDSoundboardApp.AudioApp;
import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.ngui.components.JSelectablePanel;
import vv3ird.ESDSoundboardApp.ngui.components.JSoundBoardPanel;
import vv3ird.ESDSoundboardApp.ngui.layout.UIColumnLayout;
import vv3ird.ESDSoundboardApp.ngui.pages.JSoundPage;
import vv3ird.ESDSoundboardApp.ngui.pages.JSoundboardPage;
import vv3ird.ESDSoundboardApp.ngui.pages.Page;
import vv3ird.ESDSoundboardApp.ngui.pages.PageViewer;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		AudioApp.main(args);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JSoundboardFrame frame = new JSoundboardFrame();
					frame.setVisible(true);
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
		pnSideBar.setLayout(null);
		
		pnSoundBoards = new JSelectablePanel();
		pnSoundBoards.setBounds(0, 0, 200, 40);
		setSize(pnSoundBoards, 200, 40);
		pnSideBar.add(pnSoundBoards);
		pnSoundBoards.setLayout(null);
		
		lblSoundBoards = new JLabel("Soundboards");
		lblSoundBoards.setHorizontalAlignment(SwingConstants.CENTER);
		lblSoundBoards.setBounds(10, 0, 180, 40);
		lblSoundBoards.setOpaque(false);
		pnSoundBoards.add(lblSoundBoards);
		pnSoundBoards.setBackground(ColorScheme.SIDE_BAR_BACKGROUND_COLOR);
		
		pnSounds = new JSelectablePanel();
		pnSounds.setLayout(null);
		pnSounds.setBackground(new Color(0, 81, 108));
		pnSounds.setBounds(0, 40, 200, 40);
		pnSideBar.add(pnSounds);
		
		lblSounds = new JLabel("Sounds");
		lblSounds.setOpaque(false);
		lblSounds.setHorizontalAlignment(SwingConstants.CENTER);
		lblSounds.setBounds(10, 0, 180, 40);
		pnSounds.add(lblSounds);
		
		pnOptions = new JSelectablePanel();
		pnOptions.setLayout(null);
		pnOptions.setBackground(new Color(0, 81, 108));
		pnOptions.setBounds(0, 80, 200, 40);
		JSelectablePanel.JSelectablePanelGroup pnGroup = new JSelectablePanel.JSelectablePanelGroup();
		pnGroup.add(pnSoundBoards);
		pnGroup.add(pnSounds);
		pnSideBar.add(pnOptions);
		
		JSelectablePanel pnDecks = new JSelectablePanel();
		pnDecks.setSelected(true);
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
		this.pnSoundBoards.addSelectionListener(sl);
//		this.pnOptions.addSelectionListener(sl);
		JSoundboardFrame.this.pnSoundBoards.setSelected(true);
		lblSoundBoards.setForeground(ColorScheme.SIDE_BAR_FOREGROUND_COLOR);
		lblSounds.setForeground(ColorScheme.SIDE_BAR_FOREGROUND_COLOR);
		lblOptions.setForeground(ColorScheme.SIDE_BAR_FOREGROUND_COLOR);
		
		setResizable(false);
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
				}
			}

		});
	}

	private void setSize(Component comp, int width, int height) {
		comp.setPreferredSize(new Dimension(width, height));
		comp.setSize(new Dimension(width, height));
		comp.setMinimumSize(new Dimension(width, height));
		comp.setMaximumSize(new Dimension(width, height));
	}
}
