package vv3ird.ESDSoundboardApp.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import vv3ird.ESDSoundboardApp.AudioApp;
import vv3ird.ESDSoundboardApp.gui.pages.JCreateSoundBoard;
import vv3ird.ESDSoundboardApp.gui.pages.JNewSoundPage;
import vv3ird.ESDSoundboardApp.gui.pages.JSoundboardsPage;
import vv3ird.ESDSoundboardApp.ngui.pages.PageViewer;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JMain extends JFrame {

	private JPanel contentPane;

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
					JMain frame = new JMain();
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
	public JMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 265, 426);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JButton btnAddSondboard = new JButton("Add Sondboard");
		btnAddSondboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PageViewer pv = new PageViewer();
				JCreateSoundBoard csPage = new JCreateSoundBoard();
				pv.viewPage(csPage);
				pv.setVisible(true);
			}
		});
		panel.add(btnAddSondboard);
		
		JLabel lblNewLabel = new JLabel("");
		panel.add(lblNewLabel);
		
		JButton btnEditSB = new JButton("Edit SoundBoard");
		btnEditSB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PageViewer pv = new PageViewer();
				JSoundboardsPage csPage = new JSoundboardsPage(AudioApp.soundboardLibrary);
				pv.viewPage(csPage);
				pv.setVisible(true);
			}
		});
		panel.add(btnEditSB);
		
		JButton btnAddSound = new JButton("Add Sound");
		btnAddSound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PageViewer pv = new PageViewer();
				JNewSoundPage jnsp = new JNewSoundPage();
				pv.viewPage(jnsp);
				pv.setVisible(true);
			}
		});
		panel.add(btnAddSound);
		
		JLabel label = new JLabel("");
		panel.add(label);
	}

}
