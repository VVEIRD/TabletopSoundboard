package vv3ird.ESDSoundboardApp.ngui.components.picker;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import vv3ird.ESDSoundboardApp.config.Sound;

/**
 * 
 * JComboBox with an autocomplete drop down menu. This class is hard-coded for
 * String objects, but can be
 * 
 * altered into a generic form to allow for any searchable item.
 * 
 * @author G. Cope
 *
 * 
 * 
 */

public class AutocompleteJComboBox<T extends Comparable<? super T>> extends JComboBox<T> {

	static final long serialVersionUID = 4321421L;

	private final Searchable<T, String> searchable;

	/**
	 * 
	 * Constructs a new object based upon the parameter searchable
	 * 
	 * @param s
	 * 
	 */

	public AutocompleteJComboBox(Searchable<T, String> s) {
		
		

		super();

		this.searchable = s;
		for(T item : this.searchable.getFullList())
			addItem(item);

		setEditable(true);
		
		Component c = getEditor().getEditorComponent();

		if (c instanceof JTextComponent) {
			final JTextComponent tc = (JTextComponent) c;
			tc.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void changedUpdate(DocumentEvent arg0) {
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					update();
				}

				@Override
				public void removeUpdate(DocumentEvent arg0) {
					showAll();
				}

				public void update() {

					// perform separately, as listener conflicts between the editing component
					// and JComboBox will result in an IllegalStateException due to editing
					// the component when it is locked.

					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							String search = tc.getText();
							List<T> founds = search.isEmpty() ? searchable.getFullList() : new ArrayList<T>(searchable.search(search));

							Collections.sort(founds);// sort alphabetically

							setEditable(false);

							removeAllItems();
							for (T s : founds) {
								addItem(s);
							}
							tc.setText(search);
							setEditable(true);
							setPopupVisible(true);
						}
					});
				}

				public void showAll() {

					// perform separately, as listener conflicts between the editing component
					// and JComboBox will result in an IllegalStateException due to editing
					// the component when it is locked.

					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {

							List<T> founds = searchable.getFullList() ;

							Collections.sort(founds);// sort alphabetically

							setEditable(false);

							removeAllItems();
							setSelectedIndex(-1);
							for (T s : founds) {
								addItem(s);
							}
							setEditable(true);
							setPopupVisible(true);
						}
					});
				}
			});

			// When the text component changes, focus is gained
			// and the menu disappears. To account for this, whenever the focus
			// is gained by the JTextComponent and it has searchable values, we show the
			// popup.
			tc.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) {
					if (tc.getText().length() > 0) {
						setPopupVisible(true);
					}
				}

				@Override
				public void focusLost(FocusEvent arg0) {
				}
			});
		} else {
			throw new IllegalStateException("Editing component is not a JTextComponent!");
		}
	}
}
