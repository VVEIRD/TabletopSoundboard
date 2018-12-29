package vveird.TabletopSoundboard.ngui.pages;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;

import vveird.TabletopSoundboard.AudioApp;
import vveird.TabletopSoundboard.config.Sound;
import vveird.TabletopSoundboard.config.SoundBoard;
import vveird.TabletopSoundboard.config.Sound.Type;
import vveird.TabletopSoundboard.ngui.components.JSoundPanel;
import vveird.TabletopSoundboard.ngui.layout.WrapLayout;
import vveird.TabletopSoundboard.ngui.util.ColorScheme;

public class JSpotifySoundPage extends Page{
	
	JPanel pnContent = null;
	
	String filter = "";
	
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
		sbs = sbs.stream().filter(s -> filter.length() == 0 || s.getName().toLowerCase().contains(filter.toLowerCase())).collect(Collectors.toList());
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
		JTextField filter = new JTextField(JSpotifySoundPage.this.filter);
		filter.setToolTipText("Press Enter to apply the filter");
		filter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JSpotifySoundPage.this.filter = filter.getText();
				updateList();
			}
		});
		filter.setPreferredSize(new Dimension(180, 23));
//		JLabel lblFilter = new JLabel("Filter: ");
		JButton btnFilter = new JButton("Filter");
		btnFilter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		btnFilter.setOpaque(false);
		btnFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JSpotifySoundPage.this.filter = filter.getText();
				updateList();
			}
		});
//		lblFilter.setForeground(ColorScheme.FOREGROUND_COLOR);
		bb.add(filter);
		bb.add(btnFilter);
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
