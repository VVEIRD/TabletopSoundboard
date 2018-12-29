package vveird.TabletopSoundboard.ngui.pages;

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
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import vveird.TabletopSoundboard.AudioApp;
import vveird.TabletopSoundboard.config.SoundBoard;
import vveird.TabletopSoundboard.ngui.components.JSoundBoardPanel;
import vveird.TabletopSoundboard.ngui.layout.UIColumnLayout;
import vveird.TabletopSoundboard.ngui.util.ColorScheme;

public class JSoundboardPage extends Page{
	
	JPanel pnContent = null;
	
	public JSoundboardPage() {
		setSize(700, 460);
		setOpaque(false);
		setMaximumSize(new Dimension(700, 460));
		setPreferredSize(new Dimension(700, 460));
		setMinimumSize(new Dimension(700, 460));
		setSize(new Dimension(700, 460));
		setLayout(new BorderLayout());
		pnContent = new JPanel();
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
		updateList();
		add(scrollPane, BorderLayout.CENTER);
	}

	private void updateList() {
		pnContent.removeAll();
		List<SoundBoard> sbs = AudioApp.getSoundboardLibrary();
		boolean light = true;
		for (SoundBoard soundBoard : sbs) {
			JSoundBoardPanel jsbp = new JSoundBoardPanel(soundBoard, light);
			MouseListener mlDelete = new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
					removeSoundBoard();
				}
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {
					removeSoundBoard();
				}
				private void removeSoundBoard() {
					int dialogResult = JOptionPane.showConfirmDialog (null, "Delete Soundboard " + soundBoard.name,"Warning", JOptionPane.YES_NO_OPTION);
					if(dialogResult == JOptionPane.YES_OPTION){
						try {
							AudioApp.deleteSoundboard(soundBoard);
							updateList();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
			jsbp.addMouseListenerForDelete(mlDelete);
			MouseListener mlEdit = new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
					editSoundBoard();
				}
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {
					editSoundBoard();
				}
				private void editSoundBoard() {
					pageViewer.viewPage(new JCreateSoundboardPage(soundBoard));
				}
			};
			jsbp.addMouseListener(mlEdit);
			pnContent.add(jsbp);
			light = !light;
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
		JButton btnAddSoundboard = new JButton("+");
		btnAddSoundboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pageViewer.viewPage(new JCreateSoundboardPage(null));
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
	
	@Override
	protected void cancelAction() {
	}
	
	@Override
	protected void okAction() {
	}

}
