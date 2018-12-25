package vv3ird.ESDSoundboardApp.plugins.data;

import java.util.List;

import vv3ird.ESDSoundboardApp.plugins.data.exceptions.ValueNotInMetadataListException;
import vv3ird.ESDSoundboardApp.plugins.data.exceptions.WrongSoundPluginMetadataTypeException;

public class SoundPluginMetadataTemplate {
	
	public final TYPE type;

	public final String pluginClass;
	
	/**
	 * Optional identifier if the plugin supports multiple instances.
	 */
	public String instanceId = null;

	public final String pluginName;

	public final String key;
	
	public final List<String> list;

	public final int lowerBounds;
	
	public final int upperBounds;
	
	public int defaultValueInt;
	
	public String defaultValueString = null;

	public SoundPluginMetadataTemplate(String pluginClass, String pluginName, TYPE type, String key, List<String> list) {
		 this(pluginClass, null, pluginName, type, key, list, -999999, 999999, "", 0);
	}
	
	public SoundPluginMetadataTemplate(String pluginClass, String instanceId, String pluginName, TYPE type, String key,
			List<String> list, int lowerBounds, int upperBounds, String defaultValueString, int defaultValueInt) {
		super();
		this.type = type;
		this.instanceId = instanceId;
		this.pluginClass = pluginClass;
		this.pluginName = pluginName;
		System.out.println(pluginName);
		this.key = key;
		this.list = list;
		this.lowerBounds = lowerBounds;
		this.upperBounds = upperBounds;
		this.defaultValueString = defaultValueString;
		this.defaultValueInt = defaultValueInt;
	}
	
	public SoundPluginMetadata createMetadata(String value) throws WrongSoundPluginMetadataTypeException, ValueNotInMetadataListException {
		if(type == TYPE.INT)
			throw new WrongSoundPluginMetadataTypeException(pluginClass + ":" + key + " is of type int");
		if(type == TYPE.LIST && !list.contains(value))
			throw new ValueNotInMetadataListException(pluginClass + ":" + key + " is of type int");
		return new SoundPluginMetadata(pluginClass, instanceId, pluginName, key, value, -1, type, lowerBounds, upperBounds);
	}
	
	public SoundPluginMetadata createMetadata(int value) throws WrongSoundPluginMetadataTypeException {
		if(type != TYPE.INT)
			throw new WrongSoundPluginMetadataTypeException(pluginClass + ":" + key + " is not integer metadata");
		if(this.lowerBounds > value || this.upperBounds < value)
			throw new WrongSoundPluginMetadataTypeException(pluginClass + ":" + key + " value \"" + value + " \" is not in the allow range");
		return new SoundPluginMetadata(pluginClass, instanceId, pluginName, key, null, value, type, lowerBounds, upperBounds);
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
