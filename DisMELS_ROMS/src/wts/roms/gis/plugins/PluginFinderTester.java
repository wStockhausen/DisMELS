/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.roms.gis.plugins;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wstockhausen
 */
public class PluginFinderTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Starting tests");
        PluginFinderTester t = new PluginFinderTester();
        t.runTests();
        System.out.println("Done!");
    }

    private void testFinder(String dir) {
        try {
            PluginFinder_ProjCSs pf = new PluginFinder_ProjCSs();
            pf.search(dir);
        } catch (Exception ex) {
            Logger.getLogger(PluginFinderTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void runTests() {
        testFinder("D:/MyDocuments/NetBeansProjects/Models_ROMS_v1p0/dist");
    }

}
