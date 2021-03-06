/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework;

import com.wtstockhausen.utils.RandomNumberGenerator;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
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
    private wts.roms.model.GlobalInfo romsGI;
    
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
    transient private PropertyChangeSupport propertySupport;
    
    /** class-private constructor */
    private GlobalInfo(){
        logger.info("Instantiating singleton");
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
        
        /* set time zone and format reference date */
        romsGI = wts.roms.model.GlobalInfo.getInstance();
        propertySupport = new PropertyChangeSupport(this);
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
        return workingDirFN;
    }
    
    /**
     * Sets the working directory.
     *  Note that this calls LHS_Factory.reset(), 
     *  which call LHS_Types.readXML() to update the LHS_Types singleton.
     * @param dir - new plug-ins directory
     */
    public void setWorkingDir(String dir){
        String oldVal = workingDirFN;
        workingDirFN = dir;
        LHS_Types.getInstance().readXML();//read LHS_Types.xml file in dir
        LHS_Factory.reset();              //reset the lhs maps in LHS_Factory 
        logger.info("Changed working directory to "+workingDirFN);
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
     * @throws ParseException
     */
    public void setRefDateString(String strDate) throws ParseException {
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
     * Sets the ROMS grid file name.
     * 
     * @param file - the new filename
     */
    public void setGridFile(String file){
        romsGI.setGridFile(file);
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
     * Sets the ROMS model canonical file name.
     * 
     * @param file - the new filename
     */
    public void setCanonicalFile(String file){
        romsGI.setCanonicalFile(file);
    }
    
    /**
     * Gets the ROMS grid file name.
     * 
     * @return - the filename 
     */
    public String getMapRegion(){
        return romsGI.getMapRegion();
    }
    
    /**
     * Sets the ROMS mapValues region.
     * 
     * @param region - the new mapValues region
     */
    public void setMapRegion(String region){
        romsGI.setMapRegion(region);
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
     * @param see - the new rng sesed
     */
    public void setRandomNmberGeneratorSeed(long seed){
        rng.setSeed(seed);
    }

    /**
     * Write properties to property object. Should be called before application
     * is closed to save current property values.
     * 
     * @param p - the Properties object
     */
    public void writeProperties(java.util.Properties p){
        romsGI.writeProperties(p);
        p.setProperty(this.getClass().getName()+"_version", version);
        p.setProperty(PROP_WorkingDirFN, workingDirFN);
    }
    
    /**
     * Read properties from Properties object. Should be called at application startup
     * to read in previously-saved property values.
     * 
     * @param p - the Properties object
     */
    public void readProperties(java.util.Properties p){
        romsGI.readProperties(p);
        String version = p.getProperty(this.getClass().getName()+"_version");
        if (version.equals(GlobalInfo.version)){
            setWorkingDir(p.getProperty(PROP_WorkingDirFN, workingDirFN));
        }
        
    }
}
