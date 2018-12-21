package vv3ird.ESDSoundboardApp.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vv3ird.ESDSoundboardApp.AudioApp;
import vv3ird.ESDSoundboardApp.plugins.data.SoundPluginMetadata;
import vv3ird.ESDSoundboardApp.plugins.data.SoundPluginMetadataTemplate;
import vv3ird.ESDSoundboardApp.plugins.listener.PlaybackListener;
import vv3ird.ESDSoundboardApp.plugins.listener.Plugin;
import vv3ird.ESDSoundboardApp.plugins.listener.PluginListener;

public class PluginManager {
	
	private static Logger logger;
	
	private static Map<String, Plugin> plugins = new HashMap<>();
	
	public static void init() {
		logger = LogManager.getLogger(PluginManager.class);
		loadPlugins();
		initPlugins();
	}

	private static void loadPlugins() {
		logger.debug("Loading Plugins");
		try {
			Files.list(Paths.get("plugins")).forEach(p -> {
				Map.Entry<String, Plugin> e = load(p);
				if(e != null && e.getValue() != null) {
					plugins.put(e.getKey(), e.getValue());
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void initPlugins() {
		for (String pluginClazz: plugins.keySet()) {
			Plugin p = plugins.get(pluginClazz);
			p.addPluginListener(new PlListener());
			initPlugin(p);
		}
	}

	private static void initPlugin(Plugin plugin) {
		logger.debug("Initializing plugin \"" + plugin.getDisplayName() + "\"");
		if(plugin.isEnabled() && plugin.isConfigured()) {
			logger.debug("Plugin is enabled and configured");
			try {
				plugin.init();
				if(plugin.isPlaybackListener() && plugin instanceof PlaybackListener)
					AudioApp.addPlaybackListener((PlaybackListener)plugin);
			} catch (Exception e) {
				logger.error("Error initializing plugin " + plugin.getDisplayName() + "(" + plugin.getClass().getCanonicalName() + ")");
				logger.error(e);
			}
		}
		else if(!plugin.isEnabled()) 
			logger.debug("Plugin is not enabled");
		else if(!plugin.isConfigured()) 
			logger.debug("Plugin is not configured yet");
	}

	private static void disablePlugin(Plugin plugin) {
		if(plugin.isConfigured()) {
			if(plugin.isPlaybackListener() && plugin instanceof PlaybackListener)
				AudioApp.addPlaybackListener((PlaybackListener)plugin);
		}
	}
	
	public static List<SoundPluginMetadataTemplate> getSoundPluginMetadataTemplate() {
		List<SoundPluginMetadataTemplate> templates = new LinkedList<>();
		for (String key : plugins.keySet()) {
			Plugin p = plugins.get(key);
			if(p.isEnabled() && p.isConfigured()) {
				templates.addAll(p.getSoundPluginMetadataTemplates());
			}
		}
		return templates;
	}
	
	public static List<String> getListForMetadata(SoundPluginMetadata metadata) {
		Plugin p = plugins.get(metadata.pluginClass);
		if (p != null) {
			return p.getListForMetadata(metadata.key);
		}
		return null;
	}
	
	private static Map.Entry<String, Plugin> load(Path p) {
		logger.debug("Trying to load plugin " + p.getFileName().toString());
		try {
			if (p != null && Files.isRegularFile(p)) {
				ZipFile lib = new ZipFile(p.toFile());
				ZipEntry pluginFile = enumerationAsStream(lib.entries()).filter(z -> z.getName().equals("plugin.txt"))
						.findFirst().orElse(null);
				String clazzName = null;
				if (pluginFile != null) {
					InputStream stream = lib.getInputStream(pluginFile);
					clazzName = new String(stream.readAllBytes());
					stream.close();
				}
				lib.close();
				if (clazzName != null) {
					logger.debug("Plugin class found: " + clazzName);
					URLClassLoader child = new URLClassLoader(new URL[] { p.toFile().toURI().toURL() },
							ClassLoader.getSystemClassLoader());
					Class<?> classToLoad = Class.forName(clazzName, true, child);
					Object o = classToLoad.newInstance();
					logger.debug("Plugin loaded");
					if (o instanceof Plugin)
						return new AbstractMap.SimpleEntry<String, Plugin>(clazzName, (Plugin) o);
				}
				else {
					logger.debug("Found no plugin class");
				}
			}
		} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			logger.error("Error occured loading plugin " + p.getFileName().toString() + ":");
			logger.error(e);
		}
		return null;
	}
	
	public static <T> Stream<T> enumerationAsStream(Enumeration<T> e) {
	     return StreamSupport.stream(
	         Spliterators.spliteratorUnknownSize(
	             new Iterator() {
	                 public T next() {
	                     return e.nextElement();
	                 }
	                 public boolean hasNext() {
	                     return e.hasMoreElements();
	                 }
	             },
	             Spliterator.ORDERED), false);
	 }
	private static class PlListener implements PluginListener {

		@Override
		public void onEnable(Plugin plugin) {
			initPlugin(plugin);
		}

		@Override
		public void onDisable(Plugin plugin) {
			disablePlugin(plugin);
		}
		
	}
}
