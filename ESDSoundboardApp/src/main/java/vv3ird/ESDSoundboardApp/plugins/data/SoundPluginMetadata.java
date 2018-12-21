package vv3ird.ESDSoundboardApp.plugins.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import vv3ird.ESDSoundboardApp.plugins.data.SoundPluginMetadataTemplate.TYPE;

public class SoundPluginMetadata {
	
	public String pluginClass = null;
	
	public String key = null;
	
	public String valueString = null;
	
	public int valueInt = -1;
	
	public TYPE type = TYPE.STRING;

	public SoundPluginMetadata(String pluginClass, String key, String valueString, int valueInt, TYPE type) {
		super();
		this.pluginClass = pluginClass;
		this.key = key;
		this.valueString = valueString;
		this.valueInt = valueInt;
		this.type = type;
	}
	public static void main(String[] args) {
		SoundPluginMetadata effect = new SoundPluginMetadata("vv3ird.ESDSoundboardApp.plugins.NanoleafLightPanel.NanoleafLightPanelPlugin", "Effect", "Arlarm on Board", -1, TYPE.LIST);
		SoundPluginMetadata brightness = new SoundPluginMetadata("vv3ird.ESDSoundboardApp.plugins.NanoleafLightPanel.NanoleafLightPanelPlugin", "Brightness", null, 70, TYPE.INT);
		SoundPluginMetadata switchOnOf = new SoundPluginMetadata("vv3ird.ESDSoundboardApp.plugins.NanoleafLightPanel.NanoleafLightPanelPlugin", "Switch On/Off", null, 1, TYPE.INT);
		List<SoundPluginMetadata> metadata = new LinkedList<>();
		metadata.add(effect);
		metadata.add(brightness);
		metadata.add(switchOnOf);
		Map<String, List<SoundPluginMetadata>> pluginMetadata = new HashMap<>();
		pluginMetadata.put(effect.pluginClass, metadata);
		Gson json = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(json.toJson(pluginMetadata));
	}
	
}
