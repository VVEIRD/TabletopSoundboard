package vv3ird.ESDSoundboardApp.ngui.pages;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import vv3ird.ESDSoundboardApp.AudioApp;
import vv3ird.ESDSoundboardApp.config.Sound;
import vv3ird.ESDSoundboardApp.config.Sound.Type;
import vv3ird.ESDSoundboardApp.config.SoundBoard;
import vv3ird.ESDSoundboardApp.ngui.components.JSoundPanel;
import vv3ird.ESDSoundboardApp.ngui.layout.WrapLayout;
import vv3ird.ESDSoundboardApp.ngui.util.ColorScheme;

public class JSpotifySoundPage extends Page{
	
	JPanel pnContent = null;
	
	public JSpotifySoundPage() {
		setSize(700, 460);
		setOpaque(false);
		setMaximumSize(new Dimension(700, 460));
		setPreferredSize(new Dimension(700, 460));
		setMinimumSize(new Dimension(700, 460));
		setSize(new Dimension(700, 460));
		setLayout(new BorderLayout());
		pnContent = new JPanel();
		pnContent.setLayout(new WrapLayout(FlowLayout.LEFT));
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
		updateList();
		add(scrollPane, BorderLayout.CENTER);
	}

	private void updateList() {
		pnContent.removeAll();
		List<Sound> sbs = AudioApp.getSpotifyPlaylistSounds(Type.AMBIENCE);
		if(sbs == null)
			sbs = new LinkedList<>();
		List<JPanel> toAdd = new ArrayList<>(sbs.size());
		for (Sound sound : sbs) {
			JSoundPanel jsbp = new JSoundPanel(sound, ColorScheme.MAIN_BACKGROUND_COLOR);
			MouseListener mlDelete = new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
					removeSound();
				}
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {
					removeSound();
				}
				private void removeSound() {
//					int dialogResult = JOptionPane.showConfirmDialog (null, "Delete Sound " + sound.getName(),"Warning", JOptionPane.YES_NO_OPTION);
//					if(dialogResult == JOptionPane.YES_OPTION){
//						updateList();
//					}
				}
			};
			jsbp.addMouseListenerForDelete(mlDelete);
			MouseListener mlEdit = new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
					editSound();
				}
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {
					editSound();
				}
				private void editSound() {
					pageViewer.viewPage(new JEditSoundPage(sound));
				}
			};
			jsbp.addMouseListenerForEdit(mlEdit);
			toAdd.add(jsbp);
		}
		for (JPanel jPanel : toAdd) {
			pnContent.add(jPanel);
		}
		pnContent.revalidate();
		pnContent.repaint();
	}
	
	@Override
	public void setPageView(PageViewer pageViewer) {
		super.setPageView(pageViewer);
		updateList();
	}

	@Override
	public JPanel getButtonBar() {
		JPanel bb = new JPanel();
		bb.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JLabel filler = new JLabel("");
		filler.setPreferredSize(new Dimension(23, 23));
		bb.add(filler);
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
