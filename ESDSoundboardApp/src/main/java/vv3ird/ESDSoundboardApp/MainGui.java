package vv3ird.ESDSoundboardApp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import vv3ird.ESDSoundboardApp.ngui.util.DynamicURLClassLoader;

public class MainGui {
	public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Class<?> frameAppClass = Class.forName("vv3ird.ESDSoundboardApp.ngui.JSoundboardFrame", true,
				DynamicURLClassLoader.CLASS_LOADER);
		Method main = frameAppClass.getMethod("main", String[].class);
		main.invoke(null, new Object[] { args });
	}
}
