package vv3ird.ESDSoundboardApp.ngui.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import vv3ird.ESDSoundboardApp.AudioApp;
import vv3ird.ESDSoundboardApp.Spotify.SpotifyFrontend;
import vv3ird.ESDSoundboardApp.config.AppConfiguration;
import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.ngui.ColorScheme;
import vv3ird.ESDSoundboardApp.ngui.components.JSoundBoardPanel;
import vv3ird.ESDSoundboardApp.ngui.layout.UIColumnLayout;
import vv3ird.ESDSoundboardApp.ngui.util.Helper;

import javax.swing.JCheckBox;
import javax.swing.border.EtchedBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class JOptionsPage extends Page{
	
	JPanel pnContent = null;
	private JTextField tfClientId;
	private JTextField tfSecret;
	private JTextField tfResponseUrl;
	private JPanel pnSpotify;
	JCheckBox chckbxEnableSpotify;
	private JLabel lblLoginSuccessful;
	private JLabel lblNotSucc;
	
	public JOptionsPage() {
		setSize(700, 460);
		setOpaque(false);
		setMaximumSize(new Dimension(700, 460));
		setPreferredSize(new Dimension(700, 460));
		setMinimumSize(new Dimension(700, 460));
		setSize(new Dimension(700, 460));
		setLayout(new BorderLayout());
		pnContent = new JPanel();
		pnContent.setOpaque(false);
		JViewport viewport = new JViewport();
		viewport.setOpaque(false);
		viewport.setView(pnContent);
		pnContent.setLayout(null);
		pnContent.setLayout(new UIColumnLayout(5, 5));
		
		chckbxEnableSpotify = new JCheckBox("Enable Spotify");
		chckbxEnableSpotify.setOpaque(false);
		chckbxEnableSpotify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnSpotify.setVisible(chckbxEnableSpotify.isSelected());
			}
		});
		chckbxEnableSpotify.setBounds(6, 7, 208, 23);
		pnContent.add(chckbxEnableSpotify);
		
		pnSpotify = new JPanel();
		pnSpotify.setOpaque(false);
		pnSpotify.setVisible(false);
		pnSpotify.setBorder(new EtchedBorder(EtchedBorder.LOWERED, ColorScheme.MAIN_BACKGROUND_COLOR.brighter(), ColorScheme.MAIN_BACKGROUND_COLOR.darker()));
		pnSpotify.setBounds(6, 37, 650, 198);
		pnSpotify.setSize(650, 198);
		pnSpotify.setPreferredSize(new Dimension(650, 198));
		pnSpotify.setMinimumSize(new Dimension(650, 198));
		pnSpotify.setMaximumSize(new Dimension(650, 198));
		pnContent.add(pnSpotify);
		pnSpotify.setLayout(null);
		
		JLabel lblClientId = new JLabel("Client ID:");
		lblClientId.setBounds(10, 18, 83, 14);
		pnSpotify.add(lblClientId);
		
		tfClientId = new JTextField();
		tfClientId.setBounds(103, 10, 554, 30);
		pnSpotify.add(tfClientId);
		tfClientId.setColumns(10);
		Helper.setTextfieldStyle(tfClientId);
		
		tfSecret = new JPasswordField();
		tfSecret.setColumns(10);
		tfSecret.setBounds(103, 50, 554, 30);
		Helper.setTextfieldStyle(tfSecret);
		pnSpotify.add(tfSecret);
		
		tfResponseUrl = new JTextField();
		tfResponseUrl.setText("http://localhost:5000/spotify-redirect");
		tfResponseUrl.setEditable(false);
		tfResponseUrl.setColumns(10);
		tfResponseUrl.setBounds(103, 90, 554, 30);
		Helper.setTextfieldStyle(tfResponseUrl);
		pnSpotify.add(tfResponseUrl);
		
		JLabel lblRedirectUrl = new JLabel("Redirect URL:");
		lblRedirectUrl.setBounds(10, 98, 83, 14);
		pnSpotify.add(lblRedirectUrl);
		
		JLabel lblClientSecret = new JLabel("Client Secret:");
		lblClientSecret.setBounds(10, 58, 83, 14);
		pnSpotify.add(lblClientSecret);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String clientId = null;
				String clientSecret = null;
				String responseUrl = null;
				if(chckbxEnableSpotify.isSelected()) {
					clientId = tfClientId.getText();
					clientSecret = tfSecret.getText();
					responseUrl = tfResponseUrl.getText();
					SpotifyFrontend sf = SpotifyFrontend.createInstance(clientId, clientSecret, responseUrl, true);
					if(sf.isLoggedIn()) {
						lblLoginSuccessful.setVisible(true);
						lblNotSucc.setVisible(false);
					}
					else {
						lblLoginSuccessful.setVisible(false);
						lblNotSucc.setVisible(true);
					}
				}
			}
		});
		btnLogin.setBounds(568, 164, 89, 23);
		pnSpotify.add(btnLogin);
		
		JTextPane txtpnToEnableSpotify = new JTextPane();
		txtpnToEnableSpotify.setEditable(false);
		txtpnToEnableSpotify.setText("To enable spotify support, goto \"https://developer.spotify.com/dashboard\" and create an App. From the dashboard goto the App and copy the Client ID and Secret. Goto \"Edit Settings\" and add the URL from \"Redirect URL\" to the whitelisted Redirect URIs. Scroll down to save. Then click on Login.");
		txtpnToEnableSpotify.setOpaque(false);
		txtpnToEnableSpotify.setForeground(ColorScheme.FOREGROUND_COLOR);
		txtpnToEnableSpotify.setBounds(10, 131, 392, 67);
		pnSpotify.add(txtpnToEnableSpotify);
		
		lblLoginSuccessful = new JLabel("Login successful");
		lblLoginSuccessful.setVisible(false);
		lblLoginSuccessful.setForeground(Color.GREEN.darker());
		lblLoginSuccessful.setBounds(469, 168, 89, 14);
		pnSpotify.add(lblLoginSuccessful);
		
		lblNotSucc = new JLabel("Login not successful");
		lblNotSucc.setForeground(Color.RED.darker());
		lblNotSucc.setVisible(false);
		lblNotSucc.setBounds(460, 168, 98, 14);
		pnSpotify.add(lblNotSucc);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setViewport(viewport);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 0, 700, 460);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		List<SoundBoard> sbs = AudioApp.getSoundboardLibrary();
		GridLayout gl = new GridLayout(sbs.size()+2, 1);
		gl.setVgap(0);
		updateOptions();
		add(scrollPane, BorderLayout.CENTER);
	}
	
	private void updateOptions() {
		AppConfiguration appConfig = AudioApp.getConfiguration();
		if (appConfig.spotifyClientId != null && appConfig.spotifyClientSecret != null && appConfig.spotifyResponseUrl != null) {
			tfClientId.setText(appConfig.spotifyClientId);
			tfSecret.setText(appConfig.spotifyClientSecret);
			tfResponseUrl.setText(appConfig.spotifyResponseUrl);
			chckbxEnableSpotify.setSelected(true);
			pnSpotify.setVisible(chckbxEnableSpotify.isSelected());
		}
	}

	@Override
	public void setPageView(PageViewer pageViewer) {
		super.setPageView(pageViewer);
		updateOptions();
	}

	@Override
	public JPanel getButtonBar() {
		JPanel bb = new JPanel();
		bb.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String clientId = null;
				String clientSecret = null;
				String responseUrl = null;
				if(chckbxEnableSpotify.isSelected()) {
					clientId = tfClientId.getText();
					clientSecret = tfSecret.getText();
					responseUrl = tfResponseUrl.getText();
				}
				AudioApp.setSpotifyConfiguration(clientId, clientSecret, responseUrl);
			}
		});
		btnSave.setBorderPainted(false);
		btnSave.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		btnSave.setPreferredSize(new Dimension(100, 23));
		bb.add(btnSave);
		return bb;
	}
	
	@Override
	public void setSize(Dimension d) {
	}
	
	
	@Override
	public void setSize(int width, int height) {
	}
	
	@Override
	public Dimension getSize() {
		// TODO Auto-generated method stub
		return new Dimension(700, 460);
	}
	
	@Override
	public Dimension getSize(Dimension rv) {
		rv.width = 700;
		rv.height = 460;
		return super.getSize(rv);
	}
	
	@Override
	protected void cancelAction() {
	}
	
	@Override
	protected void okAction() {
	}
}
