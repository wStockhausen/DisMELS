/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.roms.gis.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 *
 * @author wstockhausen
 */
public class PluginFinder_ProjCSs {
    
    public static String superclassName = "wts.roms.gis.AbstractPCS";
    private static final Class[] parameters = new Class[]{URL.class};

    private List<Class> pluginClasses;

    public PluginFinder_ProjCSs() {
        pluginClasses = new ArrayList<Class>(5);
    }

    /**
     * This method
     *  1. searches for all jar files under the folder represented by "directory",
     *  2. retrieves all classes in each jar,
     *  3. check if each class extends the class represented by superclassName
     *  4. if so, the corresponding "plugin" class is added to the List of 
     *      plugin classes (retrieved by getPluginClasses())
     * 
     * @param directory
     * @throws java.lang.Exception
     */
    public void search(String directory) throws Exception {
        File dir = new File(directory);
        if (dir.isFile()) {
            return;
        }
        File[] files = dir.listFiles(new JarFilter());
        for (File f : files) {
            List<String> classNames = getClassNames(f.getAbsolutePath());
            for (String className : classNames) {
                // Remove the ".class" at the back
                String name = className.substring(0, className.length() - 6);
                Class clazz = getClass(f, name);
                Class sc = clazz.getSuperclass();
                if (sc!=null){
//                    System.out.println("Checking class "+clazz.getCanonicalName()+", superclass is "+sc.getCanonicalName());
                    if (sc.getCanonicalName().equals(superclassName)) {
                        System.out.println("Found AbstracPCS class "+clazz.getCanonicalName());
                        pluginClasses.add(clazz);
                    }
                }
            }
        }
    }

    /**
     * Gets all the class names from a jar file (specified by jarName) as a List.
     * @param jarName - name of jar file
     * @return - List<Class> object of class names in jar
     * @throws java.io.IOException
     */
    public List<String> getClassNames(String jarName) throws IOException {
        ArrayList<String> classes = new ArrayList<String>(10);
        JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
        JarEntry jarEntry;
        while (true) {
            jarEntry = jarFile.getNextJarEntry();
            if (jarEntry == null) {
                break;
            }
            if (jarEntry.getName().endsWith(".class")) {
                classes.add(jarEntry.getName().replaceAll("/", "\\."));
            }
        }

        return classes;
    }

    /**
     * Gets the class with the given name in the jar file identified by file, 
     * loads it into the JVM, and returns the class.
     * @param file - jar file name
     * @param name - class name
     * @return - the Class object
     * @throws java.lang.Exception
     */
    public Class getClass(File file, String name) throws Exception {
        addURL(file.toURI().toURL());
        URLClassLoader clazzLoader;
        Class clazz;
        String filePath = file.getAbsolutePath();
        filePath = "jar:file://" + filePath + "!/";
        URL url = new File(filePath).toURI().toURL();
        clazzLoader = new URLClassLoader(new URL[]{url});
        clazz = clazzLoader.loadClass(name);
        return clazz;
    }

    /**
     * Adds jar file with URL u to the existing classpath. 
     * @param u - URL to jar file
     * @throws java.io.IOException
     */
    public void addURL(URL u) throws IOException {
        URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        URL urls[] = sysLoader.getURLs();
        for (int i = 0; i < urls.length; i++) {
            if (urls[i].toString().equalsIgnoreCase(u.toString())) {
                return;
            }
        }
        Class sysclass = URLClassLoader.class;
        try {
        Method method = sysclass.getDeclaredMethod("addURL", parameters);
        method.setAccessible(true);
        method.invoke(sysLoader, new Object[]{u});
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }

    /**
     * Returns a List of the discovered plugin classes
     * @return - a List<Class> object
     */
    public List<Class> getPluginClasses() {
        return pluginClasses;
    }

    /**
     * Sets the list of plugin classes to the input List object.
     * @param pluginClasses
     */
    public void setPluginClasses(List<Class> pluginClasses) {
        this.pluginClasses = pluginClasses;
    }

}
