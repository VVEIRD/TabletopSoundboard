package vv3ird.ESDSoundboardApp.ngui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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
import vv3ird.ESDSoundboardApp.ngui.soundboard.JSoundBoardPanel;

import javax.swing.SwingConstants;
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
	private JSelectablePanel pn;
	private JLabel lblOptions;
	private JScrollPane scrollPane;
	private JPanel pnContent;
	private JPanel pnStatus;

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
		setBackground(ColorScheme.MAIN_BACKGROUND_COLOR);
		getContentPane().setBackground(ColorScheme.MAIN_BACKGROUND_COLOR);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 500);
		((JPanel) getContentPane()).setBorder(new EmptyBorder(0, 0, 0, 0));
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		pnSideBar = new JPanel();
		pnSideBar.setBackground(ColorScheme.SIDE_BAR_BACKGROUND_COLOR);
		setSize(pnSideBar, 200, 450);
		getContentPane().add(pnSideBar, BorderLayout.WEST);
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
		
		pn = new JSelectablePanel();
		pn.setLayout(null);
		pn.setBackground(new Color(0, 81, 108));
		pn.setBounds(0, 80, 200, 40);
		pnSideBar.add(pn);
		
		lblOptions = new JLabel("Options");
		lblOptions.setOpaque(false);
		lblOptions.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptions.setBounds(10, 0, 180, 40);
		MouseListener ml = new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				mouseClicked(e);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getSource() == JSoundboardFrame.this.pnSoundBoards) {
					JSoundboardFrame.this.pnSounds.setSelected(false);
					JSoundboardFrame.this.pn.setSelected(false);
					JSoundboardFrame.this.pnSoundBoards.setSelected(true);
				} 
				else if (e.getSource() == JSoundboardFrame.this.pn) {
					JSoundboardFrame.this.pnSounds.setSelected(false);
					JSoundboardFrame.this.pnSoundBoards.setSelected(false);
					JSoundboardFrame.this.pn.setSelected(true);
				}
				else if (e.getSource() == JSoundboardFrame.this.pnSounds) {
					JSoundboardFrame.this.pn.setSelected(false);
					JSoundboardFrame.this.pnSoundBoards.setSelected(false);
					JSoundboardFrame.this.pnSounds.setSelected(true);
				}
				switchContent();
			}
		};
		pn.add(lblOptions);
		this.pnSounds.addMouseListener(ml);
		this.pnSoundBoards.addMouseListener(ml);
		this.pn.addMouseListener(ml);
		JSoundboardFrame.this.pnSoundBoards.setSelected(true);
		lblSoundBoards.setForeground(ColorScheme.SIDE_BAR_FOREGROUND_COLOR);
		lblSounds.setForeground(ColorScheme.SIDE_BAR_FOREGROUND_COLOR);
		lblOptions.setForeground(ColorScheme.SIDE_BAR_FOREGROUND_COLOR);

		pnContent = new JPanel();
		pnContent.setOpaque(false);
		JViewport viewport = new JViewport();
		viewport.setOpaque(false);
		viewport.setView(pnContent);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setViewport(viewport);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setBounds(0, 40, 450, 150);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		pnStatus = new JPanel();
		getContentPane().add(pnStatus, BorderLayout.SOUTH);
		pnStatus.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		pnStatus.setBackground(ColorScheme.SIDE_BAR_BACKGROUND_COLOR);
		setSize(pnStatus, 900, 40);
		setResizable(false);
		switchContent();
	}

	private void switchContent() {
		displaySoundBoards();
	}

	private void displaySoundBoards() {
		pnContent.removeAll();
		List<SoundBoard> sbs = AudioApp.getSoundboardLibrary();
		GridLayout gl = new GridLayout(sbs.size()+2, 1);
		gl.setVgap(0);
		pnContent.setLayout(gl);
		boolean light = true;
		for (SoundBoard soundBoard : sbs) {
			pnContent.add(new JSoundBoardPanel(soundBoard, light));
			light = !light;
		}
		JPanel jp = new JPanel();
		jp.setOpaque(false);
		jp.setMaximumSize(new Dimension(2000, 2000));
		BoxLayout bx = new BoxLayout(jp, BoxLayout.Y_AXIS);
		jp.setLayout(bx);
		Component verticalGlue = Box.createVerticalGlue();
//		jp.add(verticalGlue);
		pnContent.add(verticalGlue);
	}

	private void setSize(Component comp, int width, int height) {
		comp.setPreferredSize(new Dimension(width, height));
		comp.setSize(new Dimension(width, height));
		comp.setMinimumSize(new Dimension(width, height));
		comp.setMaximumSize(new Dimension(width, height));
	}
}
