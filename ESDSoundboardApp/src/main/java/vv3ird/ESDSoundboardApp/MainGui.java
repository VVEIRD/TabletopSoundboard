package vv3ird.ESDSoundboardApp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import vv3ird.ESDSoundboardApp.util.DynamicURLClassLoader;

public class MainGui {
	public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		Class<?> mainAppClass = Class.forName("vv3ird.ESDSoundboardApp.AudioApp", true, DynamicURLClassLoader.CLASS_LOADER);
		Method mainAudio = mainAppClass.getMethod("main", String[].class);
		mainAudio.invoke(null, new Object[] { args });
		
		Class<?> frameAppClass = Class.forName("vv3ird.ESDSoundboardApp.ngui.JSoundboardFrame", true, DynamicURLClassLoader.CLASS_LOADER);
		Method main = frameAppClass.getMethod("init", String[].class);
		main.invoke(null, new Object[] { args });
	}
}
