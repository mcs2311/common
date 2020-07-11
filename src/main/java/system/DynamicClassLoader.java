//-------------------------------------------------------------------------------------
package codex.system;
//-------------------------------------------------------------------------------------
import java.lang.reflect.Method;
import java.net.*;

//-------------------------------------------------------------------------------------
/**
 * Simple extension from ClassLoader overiding the loadClass(String name,
 * boolean resolve) method and allowing to register new classes
 * 
 * @author uriel
 * 
 */
public class DynamicClassLoader extends ClassLoader {
/*	DynamicClassLoader() {
		this(ClassLoader.getSystemClassLoader());
	}*/

	private ClassLoader parent;

//-------------------------------------------------------------------------------------
	public DynamicClassLoader(ClassLoader _parent) {
		//super(parent);
		super(_parent);
		parent = _parent;
	}


//-------------------------------------------------------------------------------------
//	private final static String BEAN_AC = BeansAccess.class.getName();
	/**
	 * Predefined define defineClass method signature (name, bytes, offset,
	 * length)
	 */
	private final static Class<?>[] DEF_CLASS_SIG = new Class[] { String.class, byte[].class, int.class, int.class };

	/**
	 * 
	 * @param parent used to choose the ClassLoader
	 * @param clsName C
	 * @param clsData
	 * @return
	 */

//-------------------------------------------------------------------------------------
	public static <T> Class<T> directLoad(Class<? extends T> parent, String clsName, byte[] clsData) {
		DynamicClassLoader loader = new DynamicClassLoader(parent.getClassLoader());
		@SuppressWarnings("unchecked")
		Class<T> clzz = (Class<T>) loader.defineClass(clsName, clsData);
		return clzz;
	}

//-------------------------------------------------------------------------------------
	@SuppressWarnings("deprecation")
	public static <T> T directInstance(Class<? extends T> parent, String clsName, byte[] clsData) throws InstantiationException, IllegalAccessException {
		Class<T> clzz = directLoad(parent, clsName, clsData);
		return clzz.newInstance();
	}

//-------------------------------------------------------------------------------------
	@Override
	protected synchronized java.lang.Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		/*
		 * Use default class loader
		 */
		return super.loadClass(name, resolve);
	}

//-------------------------------------------------------------------------------------
	/**
	 * Call defineClass into the parent classLoader using the
	 * method.setAccessible(boolean) hack
	 * 
	 * @see ClassLoader#defineClass(String, byte[], int, int)
	 */
	Class<?> defineClass(String name, byte[] bytes) throws ClassFormatError {
		try {
			// Attempt to load the access class in the same loader, which makes
			// protected and default access members accessible.
			Method method = ClassLoader.class.getDeclaredMethod("defineClass", DEF_CLASS_SIG);
			method.setAccessible(true);
			return (Class<?>) method.invoke(getParent(), new Object[] { name, bytes, Integer.valueOf(0), Integer.valueOf(bytes.length) });
		} catch (Exception ignored) {
		}
		return defineClass(name, bytes, 0, bytes.length);
	}

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
