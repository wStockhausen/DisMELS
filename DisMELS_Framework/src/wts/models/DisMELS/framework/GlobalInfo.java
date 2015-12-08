/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework;

import com.wtstockhausen.utils.RandomNumberGenerator;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import wts.roms.model.Interpolator3D;

/**
 * This class provides a repository for global variables for the DisMELS model.
 * This class encapsulates the wts.roms.model.GlobalInfo singleton and provides
 * access methods to it, as well as to DisMELS-specific information.
 * 
 * @author William.Stockhausen
 */
public class GlobalInfo implements LookupListener {
    
    /*----STATIC VARIABLES and METHODS----*/
    /** version */
    public static final String version = "1.0";
    /** String flag indicating property is not set */
    public static final String PROP_NotSet = "--not set--";
    /** DisMELS working directory property key */
    public static final String PROP_WorkingDirFN = "wts.models.DisMELS.framework.GlobalInfo.WorkingDirectory";
    
    
    /** date format for use is 'yyyy-MM-dd HH:mm:ss' */
    public static final DateFormat dateFormat01 = wts.roms.model.GlobalInfo.dateFormat01;
    /** date format for use is 'MM/dd/yyyy HH:mm' (Excel format) */
    public static final DateFormat dateFormat02 = wts.roms.model.GlobalInfo.dateFormat02;
    
    /** */
    private static final Logger logger = Logger.getLogger(GlobalInfo.class.getName());
    
    /** singleton instance*/
    private static GlobalInfo inst = null;
    
    /** static method to return wts.models.DisMELS.framework.GlobalInfo singleton.
     * @return - singleton instance
     */
    public static GlobalInfo getInstance(){
        if (inst==null) inst = new GlobalInfo();
        return inst;
    }
    
    /*----INSTANCE VARIABLES and METHODS-------*/
    /** path to working directory */ 
    private String workingDirFN  = PROP_NotSet;
    
    /** ROMS Global Info object */
    private final wts.roms.model.GlobalInfo romsGI;
    
    /** lookup result for "plugin" LHS classes */
    transient private Lookup.Result<LifeStageInterface> lkResLHSs = null;
    /** lookup result for "plugin" LHS parameter classes */
    transient private Lookup.Result<LifeStageParametersInterface> lkResLHSParams = null;
    /** lookup result for "plugin" LHS classes */
    transient private Lookup.Result<LifeStageAttributesInterface> lkResLHSAtts = null;
    
    /** info for LHS classes */
    private LHS_Classes lhsClasses = null;
    /** random number generator */
    final private RandomNumberGenerator rng;
    /** ROMS 3d interpolator */
    private Interpolator3D i3d = null;
    
    /** support for throwing property changes */
    private final transient PropertyChangeSupport propertySupport;
    
    /** class-private constructor */
    private GlobalInfo(){
        logger.info("--Instantiating singleton");
        /* look up LHS classes */
        lkResLHSs = Lookup.getDefault().lookupResult(LifeStageInterface.class);
        lkResLHSs.addLookupListener(this);//add info object as listener on LHS class changes
        for (Class c: lkResLHSs.allClasses() ){
            logger.info("\tfound LHS class: "+c.getName());
        }
        updateLHSClassesInfo();
        
        /* look up LHS parameter classes */
        lkResLHSParams = Lookup.getDefault().lookupResult(LifeStageParametersInterface.class);
        for (Class c: lkResLHSParams.allClasses() ){
            logger.info("\tfound LHS parameters class: "+c.getName());
        }
        /* look up LHS attribute classes */
        lkResLHSAtts = Lookup.getDefault().lookupResult(LifeStageAttributesInterface.class);
        for (Class c: lkResLHSAtts.allClasses() ){
            logger.info("\tfound LHS attributes class: "+c.getName());
        }
        
        rng = new RandomNumberGenerator();
        
        /* get ROMS global info object */
        romsGI = wts.roms.model.GlobalInfo.getInstance();
        
        /* add property support */
        propertySupport = new PropertyChangeSupport(this);
        logger.info("--Instantiated singleton");
    }
    
    /**
     * Returns the current Calendar instance.
     * @return 
     */
    public wts.models.utilities.CalendarIF getCalendar(){
        return romsGI.getCalendar();
    }
    
    /**
     * Returns the current Interpolator3D instance.
     * @return 
     */
    public Interpolator3D getInterpolator3D(){
        return i3d;
    }
    
    /**
     * Sets a new Interpolator3D instance.
     * @param newI3D 
     */
    public void setInterpolator3D(Interpolator3D newI3D){
        i3d = newI3D;
    }
    
    /**
     * Gets info object for all LHS classes
     * @return - the info object
     */
    public LHS_Classes getLHSClassesInfo(){
        return lhsClasses;
    }
    
    /**
     * Gets the list of available LHS classes as a Set
     * @return - the set
     */
    public Set<Class<? extends LifeStageInterface>> getLHSClasses(){
        return lkResLHSs.allClasses();
    }
    
    /**
     * Gets the list of available LHS parameters classes as a Set
     * @return - the set
     */
    public Set<Class<? extends LifeStageAttributesInterface>> getLHSAttributesClasses(){
        return lkResLHSAtts.allClasses();
    }
    
    /**
     * Gets the list of available LHS parameters classes as a Set
     * @return - the set
     */
    public Set<Class<? extends LifeStageParametersInterface>> getLHSParametersClasses(){
        return lkResLHSParams.allClasses();
    }
    
    /**
     * Gets the working directory
     * @return
     */
    public String getWorkingDir(){
        return romsGI.getWorkingDir();
    }
    
    /**
     * Sets the working directory (WD).
     * Effects:
     *   1. Reads ROMS.properties file in new WD, if the file exists, otherwise
     *      it uses the current ROMS properties and writes a new ROMS.properties file in the new WD.
     *   2. Calls LHS_Factory.reset(), which call LHS_Types.readXML(), which reads LHSTypes.xml in the new WD 
     *      to update the LHS_Types singleton.
     * 
     * @param dir - path to new working directory directory
     */
    public void setWorkingDir(String dir){
        logger.info("setWorkingDir('"+dir+")");
        String oldVal = workingDirFN;
        workingDirFN = dir;
        romsGI.setWorkingDir(dir);
        LHS_Types.getInstance().readXML();//read LHS_Types.xml file in dir
        LHS_Factory.reset();              //reset the lhs maps in LHS_Factory 
        logger.info("Changed working directory to '"+workingDirFN+"'");
        propertySupport.firePropertyChange(PROP_WorkingDirFN, oldVal, workingDirFN);//fire property change to listeners
    }
    
    /**
     * Gets the reference date as a String
     * @return
     */
    public String getRefDateString(){
        return romsGI.getRefDateString();
    }
    
    /**
     * Sets the reference date using the parsed value of the input string.
     * @param strDate - reference date in string format
     */
    public void setRefDateString(String strDate) {
        romsGI.setRefDateString(strDate);
        AbstractLHSAttributes.refDate = romsGI.getRefDate();
    }
    
    /**
     * Gets the reference date.
     * @return
     */
    public Date getRefDate(){
        return romsGI.getRefDate();
    }
    
    public void addLookupListener(LookupListener listener){
        lkResLHSs.addLookupListener(listener);
    }
    
    public void removeLookupListener(LookupListener listener){
        lkResLHSs.removeLookupListener(listener);
    }
    
    /**
     * Adds a PropertyChangeListener instance to list of objects to be notified of 
     * PropertyChanges.
     * 
     * @param listener - object to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        romsGI.addPropertyChangeListener(listener);
        propertySupport.addPropertyChangeListener(listener);
    }
    
    /**
     * Removes a PropertyChangeListener instance from list of objects to be notified of 
     * PropertyChanges.
     * 
     * @param listener - object to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        romsGI.removePropertyChangeListener(listener);
        propertySupport.removePropertyChangeListener(listener);
    }

    private void updateLHSClassesInfo(){
        /** construct LHS classes info object */
        lhsClasses = new LHS_Classes(lkResLHSs.allClasses());
    }
    
    @Override
    public void resultChanged(LookupEvent ev) {
        updateLHSClassesInfo();
    }
    
    /**
     * Gets the ROMS grid file name.
     * 
     * @return - the filename 
     */
    public String getGridFile(){
        return romsGI.getGridFile();
    }
    
    /**
     * Gets the current ModelGrid3D object.
     * 
     * @return - the ModelGrid3D object
     */
    public wts.roms.model.ModelGrid3D getGrid3D(){
        return romsGI.getGrid3D();
    }
    
    /**
     * Gets the ROMS model canonical file name.
     * 
     * @return - the filename 
     */
    public String getCanonicalFile(){
        return romsGI.getCanonicalFile();
    }
    
    /**
     * Gets the random number generator
     * 
     * @return - the random number generator
     */
    public final RandomNumberGenerator getRandomNumberGenerator(){
        return rng;
    }
    
    /**
     * Sets the seed for the random number generator.
     * 
     * @param seed - the new rng sesed
     */
    public void setRandomNmberGeneratorSeed(long seed){
        rng.setSeed(seed);
    }

    /**
     * Write properties to file represented by fn. Should be called before application
     * is closed to save current property values.
     * 
     * @param fn - the file name
     */
    public void writeProperties(String fn) throws IOException{
        logger.info("Writing properties to "+fn);
//        String rfn = workingDirFN+File.separator+romsGI.propsFN; 
//        romsGI.writeProperties(rfn);
        File f = new File(fn);
        writeProperties(f);
        logger.info("Done writing properties to "+fn);
    }

    /**
     * Write properties to file f. Should be called before application
     * is closed to save current property values.
     * 
     * @param f - the file name
     */
    public void writeProperties(File f) throws IOException{
        logger.info("Writing properties to "+f.getPath());
        Properties p = new Properties();
        writeProperties(p);
        FileOutputStream fos = new FileOutputStream(f);
        p.store(fos,null);
        fos.close();
        logger.info("Done writing properties to "+f.getPath());
    }

    /**
     * Write properties to property object. Should be called before application
     * is closed to save current property values.
     * 
     * @param p - the Properties object
     */
    public void writeProperties(java.util.Properties p){
        p.setProperty(this.getClass().getName()+"_version", version);
        p.setProperty(PROP_WorkingDirFN, workingDirFN);
//        romsGI.writeProperties(p);
    }
    
    /**
     * Read properties from file represented by fn. Should be called at application startup
     * to read in previously-saved property values.
     * 
     * @param fn - the Properties file name
     */
    public void readProperties(String fn) throws IOException{
        logger.info("--readProperties(String fn): reading properties from "+fn);
        File f = new File(fn);
        readProperties(f);
        logger.info("--readProperties(String fn): done reading properties from "+fn);
    }
    
    /**
     * Read properties from File f. Should be called at application startup
     * to read in previously-saved property values.
     * 
     * @param f - the File object
     */
    public void readProperties(java.io.File f) throws IOException{
        logger.info("----readProperties(java.io.File f): reading properties from "+f.getAbsolutePath());
        Properties p = new Properties();
        FileInputStream fis = new FileInputStream(f);
        p.load(fis);
        readProperties(p);
        fis.close();
        logger.info("----readProperties(java.io.File f): done reading properties from "+f.getAbsolutePath());
    }
    
    /**
     * Read properties from Properties object. Should be called at application startup
     * to read in previously-saved property values.
     * 
     * @param p - the Properties object
     */
    public void readProperties(java.util.Properties p){
        logger.info("------readProperties(java.util.Properties p): reading properties");
        String version = p.getProperty(this.getClass().getName()+"_version");
        if (version.equals(GlobalInfo.version)){
            setWorkingDir(p.getProperty(PROP_WorkingDirFN, workingDirFN));
        }
        logger.info("------readProperties(java.util.Properties p): done reading properties");        
    }
}
