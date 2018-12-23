package vv3ird.ESDSoundboardApp.util;

import java.net.URL;
import java.net.URLClassLoader;

public class DynamicURLClassLoader extends URLClassLoader {
	
	public static final DynamicURLClassLoader CLASS_LOADER = new DynamicURLClassLoader(ClassLoader.getSystemClassLoader());

    public DynamicURLClassLoader(ClassLoader classLoader) {
        super(new URL[0], classLoader);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
}