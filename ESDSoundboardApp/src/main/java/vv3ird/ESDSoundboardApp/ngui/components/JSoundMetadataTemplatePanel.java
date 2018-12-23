package vv3ird.ESDSoundboardApp.ngui.components;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import vv3ird.ESDSoundboardApp.plugins.data.SoundPluginMetadata;
import vv3ird.ESDSoundboardApp.plugins.data.SoundPluginMetadataTemplate;
import vv3ird.ESDSoundboardApp.plugins.data.SoundPluginMetadataTemplate.TYPE;
import vv3ird.ESDSoundboardApp.plugins.data.exceptions.ValueNotInMetadataListException;
import vv3ird.ESDSoundboardApp.plugins.data.exceptions.WrongSoundPluginMetadataTypeException;

import java.awt.Color;
import javax.swing.BorderFactory;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JSpinner;

public class JSoundMetadataTemplatePanel extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4154666663753566232L;
	private JTextField tfValue;
	private JSpinner spValue;
	private JComboBox cbValue;
	
	private SoundPluginMetadataTemplate template;

	/**
	 * Create the panel.
	 */
	public JSoundMetadataTemplatePanel(SoundPluginMetadataTemplate template) {
		setLayout(null);
		setBounds(0, 0, 430, 30);
		this.template = template;
		JLabel lblKey = new JLabel(template.key);
		lblKey.setBounds(10, 8, 140, 14);
		add(lblKey);
		if (this.template.type == TYPE.LIST) {
			cbValue = new JComboBox(template.list.toArray(new Object[0]));
			cbValue.setBounds(160, 4, 260, 22);
			add(cbValue);
		}
		else if (this.template.type == TYPE.STRING) {
			tfValue = new JTextField(template.defaultValueString);
			tfValue.setForeground(Color.WHITE);
			tfValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			tfValue.setColumns(10);
			tfValue.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			tfValue.setBackground(new Color(51, 94, 116));
			tfValue.setBounds(160, 4, 260, 22);
			add(tfValue);
		}
		else if (this.template.type == TYPE.INT) {
			spValue = new JSpinner(new SpinnerNumberModel(template.defaultValueInt, template.lowerBounds, template.upperBounds, 1));
			spValue.setBounds(160, 4, 260, 22);
			add(spValue);
		}
	}
	
	public SoundPluginMetadata createMetadata() throws WrongSoundPluginMetadataTypeException, ValueNotInMetadataListException {
		if (this.template.type == TYPE.LIST) {
			String value = (String)cbValue.getSelectedItem();
			return template.createMetadata(value);
		}
		else if (this.template.type == TYPE.STRING) {
			String value = tfValue.getText();
			return template.createMetadata(value);
		}
		else if (this.template.type == TYPE.INT) {
			int value = (Integer) spValue.getValue();
			return template.createMetadata(value);
		}
		return null;
	}

}
