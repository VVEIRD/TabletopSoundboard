package vveird.TabletopSoundboard.plugins.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import vveird.TabletopSoundboard.plugins.PluginManager;
import vveird.TabletopSoundboard.plugins.data.SoundPluginMetadataTemplate.TYPE;

public class SoundPluginMetadata {
	
	public String pluginClass = null;
	
	/**
	 * Optional identifier if the plugin supports multiple instances.
	 */
	public String instanceId = null;
	
	public String pluginName = null;
	
	public String key = null;
	
	public String valueString = null;
	
	public int valueInt = -1;
	
	public TYPE type = TYPE.STRING;

	public final int lowerBounds;
	
	public final int upperBounds;

	public SoundPluginMetadata(String pluginClass, String pluginName, String key, String valueString, int valueInt, TYPE type) {
		this(pluginClass, null, pluginName, key, valueString, valueInt, type, 0, 0);
	}
	public SoundPluginMetadata(String pluginClass, String instanceId, String pluginName, String key, String valueString, int valueInt, TYPE type, int lowerBounds, int upperBounds) {
		super();
		this.pluginClass = pluginClass;
		this.instanceId = instanceId;
		this.pluginName = pluginName;
		this.key = key;
		this.valueString = valueString;
		this.valueInt = valueInt;
		this.type = type;
		this.lowerBounds = lowerBounds;
		this.upperBounds = upperBounds;
	}
	
	public SoundPluginMetadataTemplate createTemplate() {
		return new SoundPluginMetadataTemplate(pluginClass, instanceId, pluginName, type, key, PluginManager.getListForMetadata(this), lowerBounds, upperBounds, valueString, valueInt);
	}
	
}
