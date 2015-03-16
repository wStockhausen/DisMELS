/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.roms.gis.plugins;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author wstockhausen
 */
public class JarFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        return name.endsWith(".jar");
    }

}
