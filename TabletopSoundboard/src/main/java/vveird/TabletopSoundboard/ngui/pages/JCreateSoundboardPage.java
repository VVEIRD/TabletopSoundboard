package vveird.TabletopSoundboard.ngui.pages;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JViewport;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.SwingConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rcblum.stream.deck.util.IconHelper;
import de.rcblum.stream.deck.util.SDImage;
import vveird.TabletopSoundboard.AudioApp;
import vveird.TabletopSoundboard.config.SoundBoard;
import vveird.TabletopSoundboard.ngui.components.JSoundPanel;
import vveird.TabletopSoundboard.ngui.components.PDControlScrollPane;
import vveird.TabletopSoundboard.ngui.layout.WrapLayout;
import vveird.TabletopSoundboard.ngui.util.ColorScheme;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Set;
import java.awt.event.ActionEvent;

/**
 * 
 * @author vveird
 * 
 * CHANGELOG:
 *  
 * DATE       USER    CHANGE
 * ---------- ------- -------------------------------------------------
 * 2019-03-21 vveird  Updated usage of IconHelper to be compatible with
 *                    StreamDeckCOre v 1.0.3
 *
 */
public class JCreateSoundboardPage extends Page {
	
	private static final long serialVersionUID = -998637210110413189L;

	private static Logger logger = LogManager.getLogger(JCreateSoundboardPage.class);

	private boolean newSoundBoard = true;
	
	private String oldSoundBoardName = null;
	
	private JTextField tfSoundBoardName;
	
	private JPanel pnThemes = null;
	
	private SoundBoard sb = null;

	/**
	 * Create the panel.
	 */
	public JCreateSoundboardPage(SoundBoard sb) {
		this.sb = sb;
		setSize(700, 460);
		setLayout(null);
		setBackground(ColorScheme.MAIN_BACKGROUND_COLOR);
		newSoundBoard = this.sb == null;
		if(newSoundBoard)
			this.sb = new SoundBoard(null);
		else
			this.sb = this.sb.clone();
		oldSoundBoardName = this.sb.name;
		JLabel lblNewLabel = new JLabel(newSoundBoard ? "New Soundboard" : "Edit Soundboard");
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel.setForeground(ColorScheme.FOREGROUND_COLOR);
		lblNewLabel.setFont(new Font("Segoe UI", lblNewLabel.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 12));
		lblNewLabel.setBounds(10, 11, 680, 30);
		add(lblNewLabel);
		
		tfSoundBoardName = new JTextField(this.sb != null ? this.sb.name : "");
		tfSoundBoardName.setForeground(ColorScheme.FOREGROUND_COLOR);
		tfSoundBoardName.setFont(new Font("Segoe UI", tfSoundBoardName.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 14));
		tfSoundBoardName.setBounds(10, 40, 680, 30);
		tfSoundBoardName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tfSoundBoardName.setBackground(ColorScheme.MAIN_BACKGROUND_COLOR.darker());
		add(tfSoundBoardName);
		tfSoundBoardName.setColumns(10);
		pnThemes = new JPanel();
		pnThemes.setBackground(ColorScheme.MAIN_BACKGROUND_COLOR.brighter());
		pnThemes.setLayout(new WrapLayout(FlowLayout.LEFT));
		JViewport viewport = new JViewport();
		viewport.setOpaque(false);
		viewport.setView(pnThemes);
		JScrollPane scrollPane = new PDControlScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setViewport(viewport);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setBounds(10, 110, 610, 339);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(8);
		scrollPane.setBounds(10, 110, 680, 330);
		add(scrollPane);
		
		JLabel lblThemes = new JLabel("Themes");
		lblThemes.setVerticalAlignment(SwingConstants.BOTTOM);
		lblThemes.setForeground(Color.WHITE);
		lblThemes.setBounds(10, 80, 163, 30);
		add(lblThemes);
		
		JButton btnAddTheme = new JButton("+");
		btnAddTheme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCreateSoundboardPage.this.sb.name = tfSoundBoardName.getText();
				pageViewer.viewPage(new JCreateThemePage(JCreateSoundboardPage.this.sb, null));
			}
		});
		btnAddTheme.setBorderPainted(false);
//		btnAddTheme.setOpaque(false);
		btnAddTheme.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		btnAddTheme.setBounds(667, 81, 23, 23);
		add(btnAddTheme);
		updateThemes();
	}
	
	protected void okAction() {
		try {
			sb.name = tfSoundBoardName.getText();
			if(oldSoundBoardName != null && !oldSoundBoardName.equalsIgnoreCase(sb.name))
				AudioApp.deleteSoundboard(new SoundBoard(oldSoundBoardName));
			AudioApp.saveSoundBoard(sb);
			pageViewer.back();
		} catch (IOException e1) {
			logger.error(e1);
			JOptionPane.showMessageDialog(null, "Could not save Soundboard: " + e1.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	protected void cancelAction() {
		pageViewer.back();
	}
	
	@Override
	public void setPageView(PageViewer pageViewer) {
		super.setPageView(pageViewer);
		updateThemes();
	}

	private void updateThemes() {
		pnThemes.removeAll();
		Set<String> categories = this.sb.getCategories();
		SDImage image = IconHelper.createFolderImage(ColorScheme.MAIN_BACKGROUND_COLOR,  true, ColorScheme.MAIN_BACKGROUND_COLOR.brighter());
		for (String cat : categories) {
			final String category = cat;
			JSoundPanel jsp = new JSoundPanel(image.image, cat, ColorScheme.MAIN_BACKGROUND_COLOR.brighter());
			jsp.addMouseListenerForDelete(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {}
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {
					JCreateSoundboardPage.this.sb.removeCategory(category);
					JCreateSoundboardPage.this.updateThemes();
				}
			});
			jsp.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {}
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {
					JCreateSoundboardPage.this.sb.name = tfSoundBoardName.getText();
					pageViewer.viewPage(new JCreateThemePage(JCreateSoundboardPage.this.sb, category));
					
				}
			});
			pnThemes.add(jsp);
		}
		pnThemes.revalidate();
		pnThemes.repaint();
	}
}
