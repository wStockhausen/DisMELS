/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.netbeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.LHS_Factory;
import wts.models.DisMELS.framework.LHS_Types;

public class Installer extends ModuleInstall {

    /** key to retrieve user's home directory from system properties */
    public static final String userdir = "netbeans.user";
    /** DisMELS properties file name */
    public static final String props = "DisMELS.properties";
    /** extensions for copies of properties file */
    private static final Vector<String> exts = new Vector<String>();//String[]{".3",".2",".1"};
    /** file separator */
    public static final String ps = File.separator;
    private static final Logger logger = Logger.getLogger(Installer.class.getName());
    
    public Installer(){
        if (exts.isEmpty()){
            exts.add(".3");
            exts.add(".2");
            exts.add(".1");
        }
    }
    
    @Override
    public void restored() {
        logger.info("Restoring module.");
        super.restored();
        GlobalInfo g = GlobalInfo.getInstance();
        Properties p = new Properties();
        try {
            Properties sysProps = System.getProperties();
            for (Object key: sysProps.keySet()){
                String s = (String) key;
                logger.info(s+": "+System.getProperty(s));
            }
            String path = System.getProperty(userdir);
            logger.info("Using '"+path+"' as user directory");
            String pfn = path+ps+props;
            logger.info("Reading DisMELS properties from '"+pfn+"'");
            JOptionPane.showMessageDialog(null, pfn, "Reading properties from",JOptionPane.INFORMATION_MESSAGE);
            File f = new File(pfn);
            p.load(new FileInputStream(f));
            g.readProperties(p);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException | SecurityException ex) {
            Exceptions.printStackTrace(ex);
        }
        LHS_Types.getInstance();
        LHS_Factory.getInstance();
        logger.info("Restored module.");
    }
    
    @Override
    public boolean closing(){
        logger.info("Writing properties.");
        GlobalInfo g = GlobalInfo.getInstance();
        Properties p = new Properties();
        try {
            String path = System.getProperty(userdir);
            logger.info("Using '"+path+"' as user home directory");
            String pfn = path+ps+props;
            logger.info("Writing DisMELS properties to '"+pfn+"'");
            g.writeProperties(p);
            File f = new File(pfn);
            p.store(new FileOutputStream(pfn),null);
            JOptionPane.showMessageDialog(null, pfn, "Saving properties to",JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException | SecurityException ex) {
            Exceptions.printStackTrace(ex);
        }
        logger.info("Finished writing properties.");
        return true;
    }
}
