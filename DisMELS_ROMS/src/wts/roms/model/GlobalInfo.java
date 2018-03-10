/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;
import wts.models.utilities.CalendarIF;
import wts.roms.gis.CSCreator;

/**
 * This class provides a repository for global variables for the DisMELS model.
 * 
 * @author William.Stockhausen
 */
public class GlobalInfo implements PropertyChangeListener {
    
    /*----STATIC VARIABLES and METHODS----*/
    /** version */
    public static final String version = "1.0";
    /** ROMS properties filename */
    public static final String propsFN = "ROMS.properties";
    /** String flag indicating property is not set */
    public static final String PROP_NotSet = "--not set--";
    /** working directory property key */
    public static final String PROP_WorkingDirFN = "wts.roms.model.GlobalInfo.WorkingDirectory";
    /** property id for changes to the reference date */
    public static final String PROP_RefDate = "wts.roms.model.GlobalInfo.ROMS_reference_date";
    /** property id for changes to the ROMS grid file */
    public static final String PROP_GridFile = "wts.roms.model.GlobalInfo.ROMS_grid_file";
    /** property id for changes to the ROMS canonical output file */
    public static final String PROP_CanonicalFile = "wts.roms.model.GlobalInfo.ROMS_canonical_output_file";
    /** property id for changes to the map projection */
    public static final String PROP_MapRegion = "wts.roms.model.GlobalInfo.Map_region";
    
    public static final String PROP_Grid2DCVI_RESET  = "wts.roms.model.GlobalInfo.Grid2DCVI_RESET";
    public static final String PROP_ModelCVI_RESET   = "wts.roms.model.GlobalInfo.ModelCVI_RESET";
    public static final String PROP_ModelOVI_RESET   = "wts.roms.model.GlobalInfo.ModelOVI_RESET";
    public static final String PROP_ModelOVI_ADDED   = "wts.roms.model.GlobalInfo.ModelOVI_ADD";
    public static final String PROP_ModelOVI_REMOVED = "wts.roms.model.GlobalInfo.ModelOVI_REMOVE";
    public static final String PROP_ModelOVI_RENAMED = "wts.roms.model.GlobalInfo.ModelOVI_RENAME";
    
    /** date format for use is 'yyyy-MM-dd HH:mm:ss' */
    public static final DateFormat dateFormat01 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /** date format for use is 'MM/dd/yyyy HH:mm' (Excel format) */
    public static final DateFormat dateFormat02 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    /** Class logger */
    private static final Logger logger = Logger.getLogger(GlobalInfo.class.getName());
    
    /** singleton instance*/
    private static GlobalInfo inst = null;
    
    /** static method to return GlobalInfo singleton
     * @return - singleton instance
     */
    public static GlobalInfo getInstance(){
        if (inst==null) inst = new GlobalInfo();
        return inst;
    }
    
    /*----INSTANCE VARIABLES and METHODS-------*/
    /** path to working directory */ 
    private String workingDirFN  = PROP_NotSet;
    
    /** 
     * ROMS reference date as a string. 
     * The default is "1900-01-01 00:00:00".
     */
    private String refDateString = "1900-01-01 00:00:00";
    /** ROMS reference date */
    private Date refDate = null;
    
    /** ROMS grid filename */
    private String gridFile = PROP_NotSet;
    /** ROMS model canonical filename */
    private String canonicalModelFile = PROP_NotSet;
    /** ROMS 2D grid critical variable info */
    private final CriticalGrid2DVariablesInfo cviGrid2D;
    /** ROMS model critical variables info */
    private final CriticalModelVariablesInfo cviModel;
    /** ROMS model optional variables info */
    private OptionalModelVariablesInfo oviModel;
    
    /** flag to enable PropertyChangeEvent processing internally */
    private boolean doEvents = true;
    
    /** global ROMS grid object */
    private ModelGrid3D grid3d = null;
    /** global ROMS 2d interpolator object */
    private Interpolator2D i2d = null;
    /** global calendar object */
    private CalendarIF calendar = null;
    
    /** 
     * Map projection region (must match a region from CSCreator).
     * CSCreator.REGION_ALASKA is the default.
     */
    private String mapRegion = CSCreator.REGION_ALASKA;
    
    /** support for throwing property changes */
    transient private PropertyChangeSupport propertySupport;
    
    /** class-private constructor */
    private GlobalInfo(){
        logger.info("Instantiating singleton");
        
        cviGrid2D = CriticalGrid2DVariablesInfo.getInstance();
        cviGrid2D.addPropertyChangeListener(this);
        cviModel = CriticalModelVariablesInfo.getInstance();
        cviModel.addPropertyChangeListener(this);
        oviModel = OptionalModelVariablesInfo.getInstance();
        oviModel.addPropertyChangeListener(this);
        
        /* set time zone and format reference date */
        TimeZone tz = TimeZone.getTimeZone("GMT");//set time zone to GMT
        dateFormat01.setTimeZone(tz);
        try {
            refDate = dateFormat01.parse(refDateString);//parse default ref date string
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);//need to catch this exception
        }
        propertySupport = new PropertyChangeSupport(this);
        logger.info("Instantiated singleton");
    }
    
    /**
     * Gets the ROMS global calendar.
     * 
     * If the calendar object has not been created previously, then
     * the canonical model file is read.If the canonical model file
     * is invalid, then setCanonicalFile(PROP_NotSet) is called.
     * 
     * Note: the calendar cannot be created unless the 
     * canonicalModelFile points to a valid ROMS dataset.
     * 
     * @return 
     */
    public CalendarIF getCalendar(){
        if (calendar==null){
            try {
                NetcdfReader nr = new NetcdfReader(canonicalModelFile);
                calendar = nr.getCalendar();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error", 
                                              "Calendar information not available."+
                                                "\nROMS canonical file not set or invalid."+
                                                "\nMust set ROMS canonical file using the ROMS Info Editor.", 
                                              JOptionPane.ERROR_MESSAGE);
                setCanonicalFile(PROP_NotSet);
            }
        }
        return calendar;
    }
    
    /**
     * Get the ROMS model 2D grid object.
     * 
     * @return 
     */
    public ModelGrid2D getGrid2D(){
        if (grid3d==null){
            if (!gridFile.equalsIgnoreCase(PROP_NotSet)){
                grid3d = new ModelGrid3D(gridFile);
            }
        }
        return grid3d;
    }
    
    /**
     * Get the ROMS model 3D grid object. If the canonicalModelFile has been
     * set, then the vertical grid information is read from this file if it hasn't
     * been done previously.
     * 
     * Make sure to check that the vertical grid
     * information has been read from the canonical model file (i.e., call 
     * hasVerticalGridInfo() after obtaining the ModelGrid3D object).
     * 
     * Side effects:
     *  calendar (see getCalendar()) is created if the canonical file is read. 
     * 
     * @return 
     */
    public ModelGrid3D getGrid3D(){
        if (grid3d==null) getGrid2D();
        if ((grid3d!=null)&&!grid3d.hasVerticalGridInfo()){
            try {
                NetcdfReader nr = new NetcdfReader(canonicalModelFile);
                grid3d.readConstantFields(nr);
                calendar = nr.getCalendar();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error", 
                                              "Vertical grid information not available."+
                                                "\nROMS canonical file not set or invalid."+
                                                "\nMust set ROMS canconical file using the ROMS Info Editor.", 
                                              JOptionPane.ERROR_MESSAGE);
                setCanonicalFile(PROP_NotSet);
            }
        }
        return grid3d;
    }
    
    /**
     * Get the ROMS model 2D interpolator.
     * 
     * @return 
     */
    public Interpolator2D getInterpolator(){
        if (i2d==null){
            i2d = new Interpolator2D();
        }
        return i2d;
    }
    
    /**
     * Resets the ROMS info properties to their default values.
     */
    public void reset(){
        setGridFile(PROP_NotSet);//this should take care of all(?!)
    }
    
    /**
     * Gets the reference date as a String
     * @return
     */
    public String getRefDateString(){
        return refDateString;
    }
    
    /**
     * Sets the reference date using the parsed value of the input string.
     * @param strDate - reference date in string format
     */
    public void setRefDateString(String strDate) {
        if (!refDateString.equals(strDate)){
            String oldVal = refDateString;
            try {
                refDateString = strDate;
                Date newRefDate = dateFormat01.parse(refDateString);//throws ParseException if format is incorrect
                refDate = newRefDate;
                propertySupport.firePropertyChange(PROP_RefDate, oldVal, refDateString);//fire property change to listeners
                logger.info("ROMS reference date set");
            } catch (ParseException ex) {
                logger.warning("Invalid ROMS ref date: '"+refDate+"'. Using old value.");
                refDateString = oldVal;//reset to original value (refDate has not yet changed) 
            }
        }
    }
    
    /**
     * Gets the reference date.
     * 
     * @return
     */
    public Date getRefDate(){
        return refDate;
    }
    
    /**
     * Gets the ROMS grid file name.
     * 
     * @return - the filename 
     */
    public String getGridFile(){
        return gridFile;
    }
    
    /**
     * Sets (but does not read) the ROMS grid file to the new name. 
     * 
     * Effects include: 
     *  1. the ModelGrid3D (grid3d) object is set to null
     *  2. the Interpolator2D (i2d) object is set to null
     *  3. the CriticalGrid2DVariablesInfo (cviGrid2D) is reset
     *  4. setCanonicalFile(PROP_NotSet) is called (generating further side effects)
     *  5. a PropertyChangeEvent with property PROP_GridFile is fired
     * 
     * Note that the grid object (ModelGrid3D) is not actually created
     * until it is accessed via getGrid2D() or getGrid3D().
     * 
     * @param file - the new filename
     */
    public void setGridFile(String file){
        if (!gridFile.equals(file)){
            logger.info("--setting ROMS grid file to '"+file+"'");
            String oldval = gridFile;
            gridFile = file;
            doEvents = false;//turn off PropertyChangeEvent processing
            cviGrid2D.reset();//throws PROP_RESET
            grid3d = null;
            i2d = null;
            doEvents = true;//turn on PropertyChangeEvent processing
            setCanonicalFile(PROP_NotSet);
            propertySupport.firePropertyChange(PROP_GridFile,oldval,gridFile);
//            propertySupport.firePropertyChange(PROP_Grid2DCVI_RESET, null, cviGrid2D);
            logger.info("--ROMS grid file set to '"+file+"'");
        }
    }
    
    /**
     * Gets the ROMS model canonical file name.
     * 
     * @return - the filename 
     */
    public String getCanonicalFile(){
        return canonicalModelFile;
    }
    
    /**
     * Sets (but does not read) the ROMS model canonical file to the new name.
     * 
     * Effects include:
     *  1. the vertical grid info is reset on the ModelGrid3D (grid3d) object
     *  2. the CriticalModelVariablesInfo and OptionalModelVariablesInfo objects are reset
     *  3. a PropertyChangeEvent with property PROP_CanonicalFile is fired
     * 
     * @param file - the new filename
     */
    public void setCanonicalFile(String file){
        if (!canonicalModelFile.equals(file)){
            logger.info("---setting ROMS canonical file to '"+file+"'");
            String oldval = canonicalModelFile;
            if (grid3d!=null) grid3d.resetVerticalGridInfo();
            calendar = null;
            canonicalModelFile = file;
            doEvents = false;//turn off PropertyChangeEvent processing
            cviModel.reset();//throws PROP_RESET
            oviModel.reset();//throws PROP_RESET
            doEvents = true;//turn on PropertyChangeEvent processing
            propertySupport.firePropertyChange(PROP_CanonicalFile,oldval,canonicalModelFile);
//            propertySupport.firePropertyChange(PROP_ModelCVI_RESET, null, cviModel);
//            propertySupport.firePropertyChange(PROP_ModelOVI_RESET, null, oviModel);
            logger.info("---ROMS canonical file set to '"+file+"'");
        }
    }
    
    /**
     * Gets the critical variables info for the ROMS 2D grid.
     * 
     * @return - the CVI 
     */
    public final CriticalGrid2DVariablesInfo getCriticalGrid2DVariablesInfo(){
        return cviGrid2D;
    }
    
    /**
     * Gets the critical variables info for the ROMS model.
     * 
     * @return - the CVI 
     */
    public final CriticalModelVariablesInfo getCriticalModelVariablesInfo(){
        return cviModel;
    }
    
    /**
     * Gets the optional variables info for the ROMS model.
     * 
     * @return - the OVI 
     */
    public final OptionalModelVariablesInfo getOptionalModelVariablesInfo(){
        return oviModel;
    }
    
    /**
     * Sets the optional variables info for the ROMS model.
     * 
     * @param ovi - the optional model variables information object 
     */
    public void setOptionalModelVariablesInfo(OptionalModelVariablesInfo ovi){
        OptionalModelVariablesInfo oldOVI = oviModel;
        oviModel = ovi;
        propertySupport.firePropertyChange(PROP_ModelOVI_RESET, oldOVI, oviModel);
    }
    
    /**
     * Gets the map region
     * 
     * @return - the map region 
     */
    public String getMapRegion(){
        return mapRegion;
    }
    
    /**
     * Sets the ROMS map region.
     * 
     * @param region - the new map region
     */
    public void setMapRegion(String region){
        logger.info("setMapRegion(region)");
        if (CSCreator.isValidRegion(region)){
            logger.info("Map region '"+region+"' is valid!");
            String oldval = mapRegion;
            mapRegion = region;
            CSCreator.setRegion(mapRegion);
            propertySupport.firePropertyChange(PROP_MapRegion,oldval,mapRegion);
        } else {
            logger.info("Map region '"+region+"' is NOT valid!!!");            
        }
    }
    
    /**
     * Returns the name of the internal (DisMELS) mask field associated with the 
     * internal (DisMELS) field name, or null if no mask field is associated with the name.
     * 
     * @param fieldName - DisMELS (not ROMS file) field name
     * @return 
     */
    public String getMaskForField(String fieldName){
        AbstractVariableInfo avi = oviModel.getVariableInfo(fieldName);
        if (avi==null) {
            avi = cviModel.getVariableInfo(fieldName);
            if (avi==null){
                avi = cviGrid2D.getVariableInfo(fieldName);
                if (avi==null) {
                    avi = oviModel.getVariableInfo(fieldName);
                    if (avi==null) {
                        JOptionPane.showMessageDialog(
                                null,
                                "No variable associated with internal name '"+fieldName+"'.",
                                "AbstractVariablesInfo.GetDescription: Error!",
                                JOptionPane.ERROR_MESSAGE);
                        logger.warning("No variable associated with internal name '"+fieldName+"'.");
                        return null;
                    }
                }
            }
        }
        String type = avi.getMaskType();
        logger.info("ModelData "+fieldName+" has mask type "+type);
        if (type.equals(ModelTypes.MASKTYPE_NONE)) return null;
        return "mask_"+type;
    }
    
    /**
     * Gets the working directory
     * 
     * @return - the working directory
     */
    public String getWorkingDir(){
        return workingDirFN;
    }
    
    /**
     * Sets the working directory (WD).
     *  Reads ROMS.properties file in new WD, if the file exists, otherwise
     *  use the current ROMS properties.
     * 
     * @param dir - path to new working directory directory
     */
    public void setWorkingDir(String dir){
        logger.info("---setWorkingDirectory("+dir+")");
        if (!workingDirFN.equals(dir)){
            workingDirFN = dir;
            try {
                String romsPropsPath = workingDirFN+File.separator+propsFN;
                File f = new File(romsPropsPath);
                if (f.exists()){
                    readProperties(f);
                } else {
                    logger.info(romsPropsPath+" does not exist.");
//                    JOptionPane.showMessageDialog(null, 
//                                                  new String("ROMS.properties file not found.\nUsing previously-defined or default ROMS info.\nPlease define ROMS info and save file."), 
//                                                  "File not found.", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (FileNotFoundException ex) {
                logger.info(ex.toString());
//                JOptionPane.showMessageDialog(null, 
//                                              new String("ROMS.properties file not found.\nPlease define ROMS Info and save file."), 
//                                              "File not found.", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | SecurityException ex) {
                logger.info(ex.toString());
//                JOptionPane.showMessageDialog(null, 
//                                              new String("Problem reading ROMS.properties file.\nPlease define ROMS Info and save file."), 
//                                              "Problem reading file.", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex){
                logger.info(ex.toString());
            }
            logger.info("---Changed working directory to "+workingDirFN);
        }
    }

    /**
     * Write properties to file represented by fn. Should be called before application
     * is closed to save current property values.
     * 
     * @param fn - the file name
     * @throws java.io.IOException
     */
    public void writeProperties(String fn) throws IOException{
        logger.info("Writing properties to "+fn);
        File f = new File(fn);
        writeProperties(f);
        logger.info("Done writing properties to "+fn);
    }

    /**
     * Write properties to file f. Should be called before application
     * is closed to save current property values.
     * 
     * @param f - the file name
     * @throws java.io.IOException
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
        logger.info("Writing properties");
        p.setProperty(this.getClass().getName()+"_version", version);
        p.setProperty(PROP_RefDate, refDateString);
        p.setProperty(PROP_GridFile, gridFile);
        p.setProperty(PROP_CanonicalFile, canonicalModelFile);
        p.setProperty(PROP_MapRegion, mapRegion);
        cviGrid2D.writeProperties(p);
        cviModel.writeProperties(p);
        oviModel.writeProperties(p);
        logger.info("Done writing properties");
    }
    
    /**
     * Read properties from file represented by fn. Should be called at application startup
     * to read in previously-saved property values.
     * 
     * @param fn - the Properties file name
     */
    public void readProperties(String fn) throws IOException{
        logger.info("\nreading properties from "+fn);
        File f = new File(fn);
        readProperties(f);
        logger.info("done reading properties from "+fn+"\n");
    }
    
    /**
     * Read properties from File f. Should be called at application startup
     * to read in previously-saved property values.
     * 
     * @param f - the File object
     * @throws java.io.IOException
     */
    public void readProperties(java.io.File f) throws IOException{
        logger.info("reading properties from "+f.getAbsolutePath());
        Properties p = new Properties();
        FileInputStream fis = new FileInputStream(f);
        p.load(fis);
        readProperties(p);
        fis.close();
        logger.info("done reading properties from "+f.getAbsolutePath());
    }
    
    /**
     * Read properties from Properties object. Should be called at application startup
     * to read in previously-saved property values.
     * 
     * @param p - the Properties object
     */
    public void readProperties(java.util.Properties p){
        logger.info("reading properties");
        String version = p.getProperty(this.getClass().getName()+"_version");
        if (version.equals("1.0")) {
            setRefDateString(p.getProperty(PROP_RefDate, refDateString));
            setMapRegion(p.getProperty(PROP_MapRegion, mapRegion));
            logger.info("reading grid file property");
            setGridFile(p.getProperty(PROP_GridFile, gridFile));
            logger.info("done reading grid file property");
            logger.info("reading canonical file property");
            setCanonicalFile(p.getProperty(PROP_CanonicalFile, canonicalModelFile));
            logger.info("done reading caonical file property");
        }
        logger.info("reading cviGrid2D properties");
        cviGrid2D.readProperties(p);
//        propertySupport.firePropertyChange(PROP_Grid2DCVI_RESET, null, cviGrid2D);
        logger.info("reading cviModel properties");
        cviModel.readProperties(p);
//        propertySupport.firePropertyChange(PROP_ModelCVI_RESET, null, cviModel);
        logger.info("reading oviModel properties");
        oviModel.readProperties(p);
//        propertySupport.firePropertyChange(PROP_ModelOVI_RESET, null, oviModel);
        logger.info("done reading properties");
    }
    
    /**
     * Adds a PropertyChangeListener instance to list of objects to be notified of 
     * PropertyChanges.
     * @param listener - object to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    /**
     * Removes a PropertyChangeListener instance from list of objects to be notified of 
     * PropertyChanges.
     * 
     * @param listener - object to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    /**
     * Called when an object the GlobalInfo instance listens to fires a PropertyChange.
     * 
     * Currently, this does NOTHING (i.e., doesn't respond to any property changes. This
     * is probably how it should stay. Objects should listen for and react to property changes 
     * fired BY the GlobalInfo object, not the other way round.
     * 
     * @param evt 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }
}
