package vv3ird.ESDSoundboardApp.gui.copy;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

public class JSoundLibrary extends JFrame {

	private JPanel contentPane;
	private JPanel pnSoundList;
	private JTextField tfName;
	private JTextField tfSoundFile;
	private JButton btnSave;
	private JButton btnCancel;
	private JPanel pnIcon;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JSoundLibrary frame = new JSoundLibrary();
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
	public JSoundLibrary() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 430);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		pnSoundList = new JPanel();
		contentPane.add(pnSoundList, BorderLayout.WEST);
		
		JPanel pnContent = new JPanel();
		contentPane.add(pnContent, BorderLayout.CENTER);
		pnContent.setLayout(null);
		
		pnIcon = new JPanel();
		pnIcon.setBounds(10, 11, 72, 72);
		pnContent.add(pnIcon);
		
		JButton btnChoose = new JButton("Choose");
		btnChoose.setBounds(10, 94, 72, 23);
		pnContent.add(btnChoose);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(92, 11, 46, 14);
		pnContent.add(lblName);
		
		tfName = new JTextField();
		tfName.setBounds(92, 25, 170, 20);
		pnContent.add(tfName);
		tfName.setColumns(10);
		
		tfSoundFile = new JTextField();
		tfSoundFile.setEditable(false);
		tfSoundFile.setBounds(92, 61, 86, 20);
		pnContent.add(tfSoundFile);
		tfSoundFile.setColumns(10);
		
		JButton btnOpen = new JButton("Choose");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnOpen.setBounds(188, 60, 74, 23);
		pnContent.add(btnOpen);
		
		JLabel lblAudio = new JLabel("Audio");
		lblAudio.setBounds(92, 46, 46, 14);
		pnContent.add(lblAudio);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(188, 94, 74, 23);
		pnContent.add(btnCancel);
		
		btnSave = new JButton("Save");
		btnSave.setBounds(104, 94, 74, 23);
		pnContent.add(btnSave);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnNeu = new JButton("Neu");
		panel.add(btnNeu);
	}
}
