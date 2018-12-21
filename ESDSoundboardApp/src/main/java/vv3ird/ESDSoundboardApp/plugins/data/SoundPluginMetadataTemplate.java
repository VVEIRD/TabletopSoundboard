package vv3ird.ESDSoundboardApp.plugins.data;

import java.util.List;

import vv3ird.ESDSoundboardApp.plugins.data.exceptions.ValueNotInMetadataListException;
import vv3ird.ESDSoundboardApp.plugins.data.exceptions.WrongSoundPluginMetadataTypeException;

public class SoundPluginMetadataTemplate {
	
	public final TYPE type;

	public final String pluginClass;

	public final String pluginName;

	public final String key;
	
	public final List<String> list;
	
	public SoundPluginMetadataTemplate(String pluginClass, String pluginName, TYPE type, String key, List<String> list) {
		super();
		this.type = type;
		this.pluginClass = pluginClass;
		this.pluginName = pluginName;
		this.key = key;
		this.list = list;
	}
	
	public SoundPluginMetadata createMetadata(String value) throws WrongSoundPluginMetadataTypeException, ValueNotInMetadataListException {
		if(type == TYPE.INT)
			throw new WrongSoundPluginMetadataTypeException(pluginClass + ":" + key + " is of type int");
		if(type == TYPE.LIST && !list.contains(value))
			throw new ValueNotInMetadataListException(pluginClass + ":" + key + " is of type int");
		return new SoundPluginMetadata(pluginClass, key, value, -1, type);
	}
	
	public SoundPluginMetadata createMetadata(int value) throws WrongSoundPluginMetadataTypeException {
		if(type != TYPE.INT)
			throw new WrongSoundPluginMetadataTypeException(pluginClass + ":" + key + " value \"" + value + " \" is not in the metadata list");
		return new SoundPluginMetadata(pluginClass, key, null, value, type);
	}

	/**
	 * There are 3 types of  metadata: String, int and a list of strings.
	 * @author rcBlum
	 *
	 */
	public static enum TYPE {
		STRING, INT, LIST;
	}
}
