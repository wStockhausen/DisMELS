/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.netbeans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    public static final String userdirProp = "netbeans.user";
    /** DisMELS properties file name */
    public static final String propsFN = "DisMELS.properties";
    /** full path to userdir */
    private static String userdir = null;
    /** full path to DisMELS properties file */
    private static String propsPath = null;
    /** extensions for copies of properties file */
    private static final Vector<String> exts = new Vector<>();//String[]{".3",".2",".1"};
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
        logger.info("--Restoring DisMELS modules.");
        super.restored();
        //get system properties and identify userdirProp
        Properties sysProps = System.getProperties();
        for (Object key: sysProps.keySet()){
            String s = (String) key;
            logger.info(s+": "+System.getProperty(s));
        }
        userdir = System.getProperty(userdirProp);
        logger.info("Using '"+userdir+"' as user directory");
        propsPath = userdir+File.separator+propsFN;
        logger.info("Reading DisMELS properties from '"+propsPath+"'");
//        JOptionPane.showMessageDialog(null, propsPath, "Reading properties from",JOptionPane.INFORMATION_MESSAGE);
        GlobalInfo g = GlobalInfo.getInstance();
        try {            
            g.readProperties(propsPath);//losd DisMELS global properties
        } catch (FileNotFoundException ex) {
            logger.info("---\nNote: DisMELS.properties file not found at "+propsPath+"\n---");
            g.setWorkingDir(userdir);
        } catch (IOException | SecurityException ex) {
            Exceptions.printStackTrace(ex);
            g.setWorkingDir(userdir);
        }
        LHS_Types.getInstance();
        LHS_Factory.getInstance();
        logger.info("--Restored DisMELS modules.");
    }
    
    @Override
    public boolean closing(){
        logger.info("Writing DisMELS properties.");
        try {
            GlobalInfo g = GlobalInfo.getInstance();
            logger.info("Using '"+userdir+"' as user home directory");
            logger.info("Writing DisMELS properties to '"+propsPath+"'");
            g.writeProperties(propsPath);
            JOptionPane.showMessageDialog(null, propsPath, "Saving properties to",JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | SecurityException ex) {
            Exceptions.printStackTrace(ex);
        }
        logger.info("Finished writing DisMELS properties.");
        return true;
    }
}
