package vveird.TabletopSoundboard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import vveird.TabletopSoundboard.util.DynamicURLClassLoader;

public class MainGui {
	public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		Class<?> mainAppClass = Class.forName("vveird.TabletopSoundboard.AudioApp", true, DynamicURLClassLoader.CLASS_LOADER);
		Method mainAudio = mainAppClass.getMethod("main", String[].class);
		mainAudio.invoke(null, new Object[] { args });
		
		Class<?> frameAppClass = Class.forName("vveird.TabletopSoundboard.ngui.JSoundboardFrame", true, DynamicURLClassLoader.CLASS_LOADER);
		Method main = frameAppClass.getMethod("init", String[].class);
		main.invoke(null, new Object[] { args });
	}
}
