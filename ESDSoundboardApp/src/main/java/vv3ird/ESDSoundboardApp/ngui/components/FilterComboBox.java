package vv3ird.ESDSoundboardApp.ngui.components;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import vv3ird.ESDSoundboardApp.config.Sound;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class FilterComboBox extends JComboBox<Sound> {
    private List<Sound> array = null;

    public FilterComboBox(List<Sound> array) {
        this.array = array;
        for (Sound t : array) {
			addItem(t);
		}
        this.setEditable(true);
        final JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        comboFilter(textfield.getText());
                    }
                });
            }
        });

    }

    public void comboFilter(String enteredText) {
		if (!this.isPopupVisible()) {
			this.showPopup();
		}

		List<Sound> filterArray= new ArrayList<Sound>();
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).getName().toLowerCase().contains(enteredText.toLowerCase()) 
					|| array.get(i).getTags() != null && array.get(i).getTags().stream().anyMatch(s -> s.toLowerCase().contains(enteredText.toLowerCase())) ) {
				filterArray.add(array.get(i));
			}
		}
		if (filterArray.size() > 0) {
			DefaultComboBoxModel<Sound> model = (DefaultComboBoxModel<Sound>) this.getModel();
			model.removeAllElements();
			for (Sound s: filterArray)
				model.addElement(s);

			JTextField textfield = (JTextField) this.getEditor().getEditorComponent();
			textfield.setText(enteredText);
		}
	}
}