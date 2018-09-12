package vv3ird.ESDSoundboardApp.ngui.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import vv3ird.ESDSoundboardApp.AudioApp;
import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.ngui.ColorScheme;
import vv3ird.ESDSoundboardApp.ngui.layout.UIColumnLayout;
import vv3ird.ESDSoundboardApp.ngui.soundboard.JSoundBoardPanel;

public class JSoundboardPage extends Page{
	
	public JSoundboardPage() {
		setSize(700, 460);
		setMaximumSize(new Dimension(700, 460));
		setPreferredSize(new Dimension(700, 460));
		setMinimumSize(new Dimension(700, 460));
		setSize(new Dimension(700, 460));
		setLayout(new BorderLayout());
		JPanel pnContent = new JPanel();
		pnContent.setLayout(new UIColumnLayout());
		pnContent.setOpaque(false);
		JViewport viewport = new JViewport();
		viewport.setOpaque(false);
		viewport.setView(pnContent);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setViewport(viewport);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(0, 0, 700, 460);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		pnContent.removeAll();
		List<SoundBoard> sbs = AudioApp.getSoundboardLibrary();
		GridLayout gl = new GridLayout(sbs.size()+2, 1);
		gl.setVgap(0);
		pnContent.setLayout(new UIColumnLayout());
		boolean light = true;
		for (SoundBoard soundBoard : sbs) {
			pnContent.add(new JSoundBoardPanel(soundBoard, light));
			light = !light;
		}
		add(scrollPane, BorderLayout.CENTER);
//		Component verticalGlue = Box.createVerticalGlue();
//		jp.add(verticalGlue);
//		pnContent.add(verticalGlue);
	}

	@Override
	public JPanel getButtonBar() {
		JPanel bb = new JPanel();
		bb.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton btnAddSoundboard = new JButton("+");
		btnAddSoundboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pageViewer.viewPage(new JNewSoundboardPage(null));
			}
		});
		btnAddSoundboard.setBorderPainted(false);
		btnAddSoundboard.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		btnAddSoundboard.setPreferredSize(new Dimension(23, 23));
		bb.add(btnAddSoundboard);
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

}
