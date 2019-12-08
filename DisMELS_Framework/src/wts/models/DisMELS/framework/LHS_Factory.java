/*
 * LHS_Factory.java
 *
 * Created on March 7, 2006, 11:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.framework;

import com.wtstockhausen.datasource.ParseCSV;
import java.awt.Color;
import java.beans.ExceptionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.XMLDecoder;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.geotools.feature.Feature;
import org.geotools.feature.IllegalAttributeException;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 * The singleton instance is the main avenue for creating individuals, attribute objects,
 * parameter objects, FeatureTypes associated with individuals.
 * 
 * The singleton listens to PropertyChanges thrown by the LHS_Types singleton and 
 * updates itself accordingly.
 * 
 * The singleton throws PropertyChanges to listeners whenever it is updated.  However,
 * classes accessing the static methods or singleton instance will obtain information
 * consistent with the current LHS_Types singleton.
 * 
 * @author William Stockhausen
 */
public class LHS_Factory implements PropertyChangeListener, ExceptionListener {
    
    /** the singleton instance of this class */
    private static LHS_Factory instance = null;
    
    /** a logger for messages */
    private static final Logger logger = Logger.getLogger(LHS_Factory.class.getName());
    
    /**
     * Gets the singleton instance of this class
     * @return 
     */
    public static LHS_Factory getInstance(){
        if (instance==null) instance = new LHS_Factory();
        return instance;
    }
    
    /**
        *  Clears the current LHS_Types and creates a new set. 
        * Note that this DOES NOT throw a PoprtyChangeEvent to listeners
        */
    public static void reset() {
        logger.info("reset() called.");
        if (instance==null) {
            instance = new LHS_Factory();
        } else {
            instance.resetInstance();
        }
    }
    
    /**
     * Resets the mappings for parameters of life stages.
     */
    public static void resetParameters(){
        logger.info("resetParameters() called.");
        if (instance==null) {
            instance = new LHS_Factory();
        } else {
            instance.paramsMap.clear();
        }
    }
    
    /**
     *  Returns a "default" instance of the LHSAttributes class 
     *  associated with the given key.
     * @param key - LHS type name to create attributes instance for
     * @return - "default" instance of attributes class
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     */
    public static LifeStageAttributesInterface createAttributes(String key) 
                        throws InstantiationException, IllegalAccessException {
        if (instance==null) instance = new LHS_Factory();
        /*
         * Each LHS instance has a unique LHSAttributes instance, so a new
         * instance is created for each call to createAttributes(String).  We
         * save some time (possibly) by caching the instance constructors for 
         * reqested types by key value in a HashMap (attributesConstructors), 
         * rather than recreating a new constructor each time. 
         */
        LifeStageAttributesInterface lhs = null;
        try {
            Constructor con = instance.attributesConstructors.get(key);
            if (con==null) {
                LHS_Type lhsType = instance.types.getType(key);
                if (lhsType==null) return null; //key not defined!
                ClassLoader syscl = Lookup.getDefault().lookup(ClassLoader.class);
                Class lhsClass = syscl.loadClass(lhsType.getAttributesClass());
                if (lhsClass==null) return null; //class not defined!
                con = lhsClass.getDeclaredConstructor(String.class);
                instance.attributesConstructors.put(key,con);
            }
            lhs = (LifeStageAttributesInterface) con.newInstance(key);
        } catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
        return lhs;
    }
    
    /**
     *  Returns an instance of the LHSAttributes class corresponding to the 
     * input String array.  The type name for the resulting instance should be
     * strv[0].
     * @param strv - String array defining attributes values
     * @return - instance of attributes class with values contained in strv
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     */
    public static LifeStageAttributesInterface createAttributes( String[] strv) 
                        throws InstantiationException, IllegalAccessException {
        //the type name (key) should be the first value in strv
        String key = strv[0];
        LifeStageAttributesInterface aLHS = createAttributes(key);//create vanilla instance
        aLHS.setValues(strv);//assign attribute values
        return aLHS;
    }
    
    public static List<LifeStageAttributesInterface> createAttributesFromCSV(String filename) 
                        throws FileNotFoundException, InstantiationException, 
                               IllegalAccessException, IOException {
        File file = new File(filename);
        return createAttributesFromCSV(file);        
    }
    
    public static List<LifeStageAttributesInterface> createAttributesFromCSV(File file) 
                        throws FileNotFoundException, InstantiationException, 
                               IllegalAccessException, IOException {
        if (instance==null) instance = new LHS_Factory();
        List<LifeStageAttributesInterface> list = new ArrayList<>();
        LifeStageAttributesInterface aLHS;
        String lhsName  = null;
        String[] strv   = null;
        FileReader     fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        instance.pCSV = new ParseCSV(br);

        instance.pCSV.decodeLine(); //decode header line
        //now decode input data
        logger.info("Reading csv file for initial attributes.");
        long ctr = 1;
        try {
            while ((strv = instance.pCSV.decodeLine()) != null) {
                ctr++;
                lhsName = strv[0];
                aLHS = createAttributes(lhsName);
                aLHS.setValues(strv);//throws null pointer if createAttributes failed
                list.add(aLHS);
            }
            instance.pCSV.close();
            instance.pCSV = null;//can set to null because we're past eof
        } catch (java.lang.NullPointerException ex) {
            String str = file.getAbsolutePath()+"\nTried to create attributes for LHS type "+
                         lhsName+" from CSV.\nProblem CSV row was "+ctr+": \n'";
            for (int i=0;i<(strv.length-1);i++) str = str+strv[i]+", ";
            str = str+strv[strv.length-1]+"'";
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    str,
                    "Error setting attribute values using",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            Exceptions.printStackTrace(ex);
            throw ex;
       } catch (java.lang.NumberFormatException ex) {
            String str = file.getAbsolutePath()+"\nTried to create attributes for LHS type "+
                         lhsName+" from CSV.\nProblem CSV row was"+ctr+": \n'";
            for (int i=0;i<(strv.length-1);i++) str = str+strv[i]+", ";
            str = str+strv[strv.length-1]+"'";
            str = str+"\nNumber format error (check your values or last line!)";
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    str,
                    "Error setting parameter values using",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            Exceptions.printStackTrace(ex);
            throw ex;
        }

        return list;
    }
    
    public static List<LifeStageAttributesInterface> createAttributesFromCSV(String filename, long skip, long max, boolean first) 
                        throws FileNotFoundException, InstantiationException, 
                               IllegalAccessException, IOException {
        File file = null;
        if (first) file = new File(filename);
        return createAttributesFromCSV(file,skip,max,first);        
    }
    
    public static List<LifeStageAttributesInterface> createAttributesFromCSV(File file, long skip, long max, boolean first) 
                        throws FileNotFoundException, InstantiationException, 
                               IllegalAccessException, IOException {
        if (instance==null) instance = new LHS_Factory();
        List<LifeStageAttributesInterface> list = new ArrayList<>();
        LifeStageAttributesInterface aLHS;
        String lhsName;
        String[] strv = null;
        if (first) {
            logger.info("Opening csv file for initial attributes.");
            FileReader     fr   = new FileReader(file);
            BufferedReader br   = new BufferedReader(fr);
            instance.pCSV = new ParseCSV(br);

            //TODO?: Could put in check on header string--should be same as
            //String from call to getCSVHeader()
            instance.pCSV.decodeLine(); //decode header line
        }
        //now decode input data
        if (instance.pCSV==null){
            logger.info("--csv file for initial attributes closed, returning empty list.");
            return list;//return empty list
        } else {
            logger.info("--Reading csv file for initial attributes.");
            long ctr = 0;
            while (((strv = instance.pCSV.decodeLine()) != null)&&(ctr++<max)) {
                lhsName = strv[0];
                aLHS = createAttributes(lhsName);
                aLHS.setValues(strv);
                list.add(aLHS);
                for (long i=0;i<skip;i++) strv = instance.pCSV.decodeLine();//skip lines as requested
            }
            if (strv==null) {
                instance.pCSV.close();
                instance.pCSV = null;//can set to null because we're past eof
            }
        }
        return list;
    }
    
    /**
     *  Returns the defined LHS attributes class for the key
     * @param key 
     * @return 
     */
    public static String getAttributesClass(String key) {
        if (instance==null) instance = new LHS_Factory();
        LHS_Type lhs = instance.types.getType(key);
        String c = null;
        if (lhs!=null) c = lhs.getAttributesClass();
        return c;
    }
    
    /**
     *  Returns a "default" instance of the LHSParameters class 
     *  associated with the given key.
     * @param key 
     * @return 
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     */
    public static LifeStageParametersInterface createParameters(String key) 
                        throws InstantiationException, IllegalAccessException {
        if (instance==null) instance = new LHS_Factory();
        /*
         * LHSParameters instances are identical for each lhs type, so a new
         * instance is created only when "key" is a new lhs type name.  Otherwise,
         * a previous instance is retrieved from instance.paramsMap and returned.
         */
        //see if there's a version already in the instance.paramsMap
        LifeStageParametersInterface lhs = instance.paramsMap.get(key);
        if (lhs!=null) return lhs;
        //If not, create an instance with default parameters
        LHS_Type lhsType = instance.types.getType(key);
        if (lhsType==null) {
            logger.info("createParameters(key): LHSType for '"+key+"' not defined!!");
            return null;
        } //key not defined!
        try {
            ClassLoader syscl = Lookup.getDefault().lookup(ClassLoader.class);
            Class lhsClass = syscl.loadClass(lhsType.getParametersClass());
            if (lhsClass==null) {
                logger.info("createParameters(key): Class for '"+key+"' not defined!!");
                return null;
            } //class not defined!
            Constructor con = lhsClass.getDeclaredConstructor(String.class);
            lhs = (LifeStageParametersInterface) con.newInstance(key);
            instance.paramsMap.put(key,lhs);//update the instance.paramsMap
        } catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) {
//            Exceptions.printStackTrace(ex);
        }
        return lhs;
    }
    
    public static Map<String,LifeStageParametersInterface> createParametersFromCSV(String filename) 
                        throws FileNotFoundException, InstantiationException, 
                               IllegalAccessException, IOException {
        File file = new File(filename);
        return createParametersFromCSV(file);        
    }
    
    public static Map<String,LifeStageParametersInterface> createParametersFromCSV(File file) 
                        throws FileNotFoundException, InstantiationException, 
                               IllegalAccessException, IOException {
        if (instance==null) instance = new LHS_Factory();
        FileReader     fr   = new FileReader(file);
        BufferedReader br   = new BufferedReader(fr);
        instance.pCSV = new ParseCSV(br);
        String   lhsName = null;
        String[] strv    = null;
        
        LifeStageParametersInterface pLHS, pLHSi;

        logger.info("Reading csv file for parameters.");
        try {
            while (instance.pCSV.decodeLine() != null) {//the header line
                strv = instance.pCSV.decodeLine();//the values line
                lhsName = strv[0];
                //create parameters for lhsName and save in map
                pLHS  = createParameters(lhsName);
                pLHSi = pLHS.createInstance(strv);
                instance.paramsMap.put(lhsName,pLHSi);
                //load parameters instance into LHS
                //(this sets parameters for LHS's that use "static" parameters)
//                lhs = createLHS(lhsName);//TODO: why do this now?  Should do this in ModelRunner during initialization
//                lhs.setParameters(pLHSi);
            }
            instance.pCSV.close();
            instance.pCSV = null;//can set to null because we're past eof
        } catch (java.lang.NullPointerException ex) {
            String str = file.getAbsolutePath()+"\nTried to create parameters for LHS type "+
                         lhsName+" from CSV.\nProblem CSV row was: \n'";
            for (int i=0;i<(strv.length-1);i++) str = str+strv[i]+", ";
            str = str+strv[strv.length-1]+"'";
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    str,
                    "Error setting parameter values using",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            Exceptions.printStackTrace(ex);
            throw ex;
        } catch (java.lang.NumberFormatException ex) {
            String str = file.getAbsolutePath()+"\nTried to create parameters for LHS type "+
                         lhsName+" from CSV.\nProblem CSV row was: \n'";
            for (int i=0;i<(strv.length-1);i++) str = str+strv[i]+", ";
            str = str+strv[strv.length-1]+"'";
            str = str+"\nNumber format error (check your values!";
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    str,
                    "Error setting parameter values using",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            Exceptions.printStackTrace(ex);
            throw ex;
        }
        return new LinkedHashMap<String,LifeStageParametersInterface>(instance.paramsMap);//return shallow copy of paramsMap
    }
    
    public static Map<String,LifeStageParametersInterface> createParametersFromXML(String filename) 
                        throws FileNotFoundException, InstantiationException, 
                               IllegalAccessException, IOException {
        File file = new File(filename);
        return createParametersFromXML(file);        
    }
    
    public static Map<String,LifeStageParametersInterface> createParametersFromXML(File file) 
                        throws FileNotFoundException, InstantiationException, 
                               IllegalAccessException, IOException {
        if (instance==null) instance = new LHS_Factory();
            logger.info("starting createParametersFromXML("+file.getName()+")");
            FileInputStream fis     = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            XMLDecoder xd = new XMLDecoder(bis);
            xd.setOwner(instance);
            xd.setExceptionListener(instance);
            Map<String,LifeStageParametersInterface> map = null;
            logger.info("--reading paramsMap object from XML");
            try {
                map = (Map<String,LifeStageParametersInterface>) xd.readObject();
            } catch (Exception ex){
                logger.warning(ex.getMessage());
            }
            logger.info("--done reading paramsMap object from XML");
            xd.close();
            
            //update instance.paramsMap with new info
            logger.info("--updating LHS_Factory.paramsMap");
            for (String key: map.keySet()) {
                logger.info("----updating '"+key+"'.");
                instance.paramsMap.put(key,map.get(key));
            }
            logger.info("--finished updating LHS_Factory.paramsMap");

            logger.info("--finished createParametersFromXML("+file.getName()+")");
            //return COPY of instance.paramsMap (same parameters objects, but different map object)
            map = new LinkedHashMap<>(instance.paramsMap);
        return map;
    }
    
    /**
     *  Returns the defined LHS parameters class for the key
     * @param key - LHS stage name
     * @return - parameters class name
     */
    public static String getParametersClass(String key) {
        if (instance==null) instance = new LHS_Factory();
        LHS_Type lhs = instance.types.getType(key);
        String c = null;
        if (lhs!=null) c = lhs.getParametersClass();
        return c;
    }
    
    /**
     *  Read values for defined parameter types
     * @param file 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     * @throws java.io.IOException 
     */
    public static void readParametersFile(File file) 
                        throws FileNotFoundException, InstantiationException, 
                               IllegalAccessException, IOException {
        LHS_Factory.createParametersFromCSV(file);
    }
    
    /**
     *  Read values for defined parameter types
     * @param filename 
     * @throws java.io.FileNotFoundException 
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     * @throws java.io.IOException 
     */
    public static void readParametersFile(String filename) 
                        throws FileNotFoundException, InstantiationException, 
                               IllegalAccessException, IOException {
        LHS_Factory.createParametersFromCSV(filename);
    }
    
    /**
     *  Returns a point feature representing the LHS attributes object.
     * @param aLHS 
     * @return 
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     */
    public static Feature createPointFeature(LifeStageAttributesInterface aLHS) 
                        throws InstantiationException, IllegalAccessException {
        LHSPointFeatureType ftLHS = createPointFeatureType(aLHS.getTypeName());
        ftLHS.setAttributes(aLHS);
        ftLHS.setGeometryFromAttributes();
        Feature feature = null;
        try {
            feature = ftLHS.createFeature();
        } catch (IllegalAttributeException ex) {
            Exceptions.printStackTrace(ex);
        }
        return feature;
    }
    
    /**
     *  Returns a point feature representing the LHS object.
     * @param lhs 
     * @return 
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     */
    public static Feature createPointFeature(LifeStageInterface lhs) 
                        throws InstantiationException, IllegalAccessException {
        LHSPointFeatureType ftLHS = createPointFeatureType(lhs.getTypeName());
        ftLHS.setAttributes(lhs.getAttributes());
        ftLHS.setGeometryFromAttributes();
        Feature feature = null;
        try {
            feature = ftLHS.createFeature();
        } catch (IllegalAttributeException ex) {
            Exceptions.printStackTrace(ex);
        }
        return feature;
    }
    
    /**
     *  Returns a point feature representing the LHS object location
     * and typeName, but with no LHSAttributes.
     * @param lhs 
     * @return 
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     */
    public static Feature createPointFeatureNoAtts(LifeStageInterface lhs) 
                        throws InstantiationException, IllegalAccessException {
        LHSPointFeatureTypeNoAtts ftLHS = createPointFeatureTypeNoAtts(lhs.getTypeName());
        LifeStageAttributesInterface atts = lhs.getAttributes();
        ftLHS.setGeometryFromAttributes(atts);
        Feature feature = null;
        try {
            feature = ftLHS.createFeature();
        } catch (IllegalAttributeException | NullPointerException ex) {
            Exceptions.printStackTrace(ex);
        }
        return feature;
    }
    
    /**
     *  Returns a "default" instance of the LHSPointFeatureType class 
     *  associated with the given key.
     * @param key 
     * @return 
     * @throws java.lang.IllegalAccessException 
     * @throws java.lang.InstantiationException 
     */
    public static LHSPointFeatureType createPointFeatureType(String key) 
                        throws InstantiationException, IllegalAccessException {
        if (instance==null) instance = new LHS_Factory();
        LHSPointFeatureType lhs = instance.pointFTs.get(key);
        if (lhs!=null) return lhs; //found it, so return.
        LHS_Type lhsType = instance.types.getType(key);
        if (lhsType==null) return null;
        try {
            ClassLoader syscl = Lookup.getDefault().lookup(ClassLoader.class);
            Class lhsClass = syscl.loadClass(lhsType.getPointFeatureTypeClass());
            if (lhsClass==null) return null; //key not defined!
            Constructor con = lhsClass.getDeclaredConstructor(String.class);
            lhs = (LHSPointFeatureType) con.newInstance(key);
            instance.pointFTs.put(key,lhs);//update pointsFT
        } catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
        return lhs;
    }
    
    /**
     *  Returns a "default" instance of the LHSPointFeatureTypeNoAtts class 
     *  associated with the given key.
     * @param key 
     * @return 
     * @throws java.lang.IllegalAccessException 
     * @throws java.lang.InstantiationException 
     */
    public static LHSPointFeatureTypeNoAtts createPointFeatureTypeNoAtts(String key) 
                        throws InstantiationException, IllegalAccessException {
        LHSPointFeatureTypeNoAtts lhs = null;
        try {
            if (instance==null) instance = new LHS_Factory();
            lhs = instance.pointFTsNoAtts.get(key);
            if (lhs!=null) return lhs; //found it, so return.
            lhs = new LHSPointFeatureTypeNoAtts(key);//create a new instance
            instance.pointFTsNoAtts.put(key,lhs);//update map pointFTsNoAtts
        } catch (NullPointerException ex){
            ex.printStackTrace();
            throw ex;
        }
        return lhs;
    }
    
    /**
     *  Returns the defined LHS point feature type class for the key
     * @param key - LHS stage name
     * @return - name of class
     */
    public static String getPointFeatureTypeClass(String key) {
        if (instance==null) instance = new LHS_Factory();
        LHS_Type lhs = instance.types.getType(key);
        String c = null;
        if (lhs!=null) c = lhs.getPointFeatureTypeClass();
        return c;
    }
        
    /**
     *  Returns a "default" instance of the LHS class associated with the given key
     * @param key - name of LHS_Type
     * @return instance of LHS class associated with LHS_Type with name key
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     */
    public static LifeStageInterface createLHS(String key) 
                        throws InstantiationException, IllegalAccessException {
        if (instance==null) instance = new LHS_Factory();
        LifeStageInterface lhs = null;
        try {
            Constructor con = instance.lhsConstructors.get(key);
            if (con==null) {
                LHS_Type lhsType = instance.types.getType(key);
                if (lhsType==null) return null; //key not defined!
                ClassLoader syscl = Lookup.getDefault().lookup(ClassLoader.class);
                Class lhsClass = syscl.loadClass(lhsType.getLHSClass());
                if (lhsClass==null) return null; //class not defined!
                con = lhsClass.getDeclaredConstructor(String.class);
                instance.lhsConstructors.put(key, con);//add constructor
            }
            lhs = (LifeStageInterface) con.newInstance(key);
        } catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
        return lhs;
    }
    
    public static List<LifeStageInterface> createLHSsFromCSV(String filename) 
                        throws FileNotFoundException, InstantiationException, 
                               IllegalAccessException, IOException {
        File file = new File(filename);
        return createLHSsFromCSV(file);        
    }
    
    public static List<LifeStageInterface> createLHSsFromCSV(File file) 
                        throws FileNotFoundException, InstantiationException, 
                               IllegalAccessException, IOException {
        if (instance==null) instance = new LHS_Factory();
        List<LifeStageInterface> list = new ArrayList<>();
        FileReader     fr   = new FileReader(file);
        BufferedReader br   = new BufferedReader(fr);
        instance.pCSV = new ParseCSV(br);
        String[]       strv = null;

        //TODO?: Could put in check on header string--should be same as
        //String from call to getCSVHeader() or getCSVHeaderShortNames()
        instance.pCSV.decodeLine(); //decode header line
        //now decode input data
        logger.info("Reading csv file to create LHSs from initial attributes.");
        long ctr = 1;
        String lhsName;
        LifeStageInterface lhs;
        while ((strv = instance.pCSV.decodeLine()) != null) {
            ctr++;
            try {
                lhsName = strv[0];
                lhs = createLHS(lhsName);
                lhs.setAttributes(strv);
                list.add(lhs);
            } catch(java.lang.NullPointerException ex) {
                logger.info("NullPointerException in createLHSsFromCSV on line "+ctr);
                throw(ex);
            } catch (java.lang.ArrayIndexOutOfBoundsException ex){
                String msg = "java.lang.ArrayIndexOutOfBoundsException thrown reading initial "+
                             "attributes file line "+ctr+".";
                logger.info(msg);
                logger.info(ex.getMessage());
            } catch(java.lang.UnknownError ex) {
                logger.info("UnknownError in createLHSsFromCSV on line "+ctr);
                throw(ex);
            }
        }
        instance.pCSV.close();
        instance.pCSV = null;//can set to null because we're past eof
        logger.info("Finished reading csv file to create LHSs from initial attributes.");
        return list;
    }
    
    /**
     *  Returns the defined LHS class for the key 
     * @param key 
     * @return 
     */
    public static String getLHSClass(String key) {
        if (instance==null) instance = new LHS_Factory();
        LHS_Type lhs = instance.types.getType(key);
        String c = null;
        if (lhs!=null) c = lhs.getLHSClass();
        return c;
    }
        
    /**
     *  Returns a "default" instance of the output LHS class 
     *  associated with the given key.
     * @param key - type name of the LHS for which to create an output LHS
     * @return    - instance of output class with output type name
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     */
    public static List<LifeStageInterface> createNextLHSs(String key) 
                        throws InstantiationException, IllegalAccessException {
        if (instance==null) instance = new LHS_Factory();
        LHS_Type lhsType = instance.types.getType(key);
        if (lhsType==null) return null; //key not defined!
        List<LifeStageInterface> nLHSs = new ArrayList<LifeStageInterface>();
        Set<String> nKeys = lhsType.getNextLHSNames();//key for next LHS
        for (String nKey : nKeys){
            LHS_Type nlhsType = instance.types.getType(nKey);
            if (nlhsType==null) {
                logger.info("No LHS type '"+nKey+"' found as next stage for LHS '"+key+"'.");
                return null;//no object for key!
            } 
            LifeStageInterface nlhs = null;
            try {
                ClassLoader syscl = Lookup.getDefault().lookup(ClassLoader.class);
                Class nlhsClass = syscl.loadClass(nlhsType.getLHSClass());
                if (nlhsClass==null) {
                    logger.info("Could not find class "+nlhsType.getLHSClass());
                } else {
                    Constructor con = nlhsClass.getDeclaredConstructor(String.class);
                    nlhs = (LifeStageInterface) con.newInstance(nKey);
                    nLHSs.add(nlhs);
                }
            } catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return nLHSs;
    }
    
    /**
     *  Returns instance of the next LHS class initialized with attributes oldAtts
     *  associated with the given key.
     * @param key     - type name of the LHS for which to create the next LHS
     * @param oldAtts - the attributes with which to initialize the new instance
     * @return - instance of next LHS class with associated type name
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     */
    public static List<LifeStageInterface> createNextLHSsFromIndividual(String key, LifeStageInterface oldLHS) 
                        throws InstantiationException, IllegalAccessException {
        List<LifeStageInterface> nLHSs = null;
        try {
            nLHSs = createNextLHSs(key);
            if (nLHSs!=null) {
                for (LifeStageInterface nLHS : nLHSs){
                    nLHS.setInfoFromIndividual(oldLHS);
                }
            }
        } catch (SecurityException | IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        }
        return nLHSs;
    }
    
    /**
     *  Returns instance of the next LHS class initialized with attributes oldAtts
     *  associated with the given key.
     * @param key     - type name of the LHS for which to create the next LHS
     * @param oldAtts - the attributes with which to initialize the new instance
     * @return - instance of next LHS class with associated type name
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     */
    public static List<LifeStageInterface> createNextLHSsFromSuperIndividual(String key, LifeStageInterface oldLHS,double numTrans) 
                        throws InstantiationException, IllegalAccessException {
        List<LifeStageInterface> nLHSs = null;
        try {
            nLHSs = createNextLHSs(key);
            if (nLHSs!=null) {
                for (LifeStageInterface nlhs : nLHSs){
                    nlhs.setInfoFromSuperIndividual(oldLHS,numTrans);
                }
            }
        } catch (SecurityException | IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        }
        return nLHSs;
    }
    
    /**
     *  Returns the defined next stage LHS classes for the key 
     * @param key - name of LHS type
     * @return - class of output LHS associated with LHS type identified by key
     */
    public static Set<String> getNextLHSClasses(String key) {
        if (instance==null) instance = new LHS_Factory();
        LHS_Type lhs = instance.types.getType(key);
        Set<String> c = null;
        if (lhs!=null) {
            for (String name : lhs.getNextLHSNames()){
                c.add(lhs.getNextLHSClass(name));
            }
        }
        return c;
    }    
        
    /**
     *  Returns a "default" instance of the output LHS class 
     *  associated with the given key.
     * @param key - type name of the LHS for which to create a spawned LHS
     * @return    - instance of spawned class with spawned type name
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     */
    public static LifeStageInterface createSpawnedLHS(String key) 
                        throws InstantiationException, IllegalAccessException {
        if (instance==null) instance = new LHS_Factory();
        LHS_Type lhsType = instance.types.getType(key);
        if (lhsType==null) return null; //key not defined!
        String sKey = lhsType.getSpawnedLHSName();//key for spawned LHS
        LHS_Type slhsType = instance.types.getType(sKey);
        if (slhsType==null) return null; //no object for key!
        LifeStageInterface slhs = null;
        try {
            ClassLoader syscl = Lookup.getDefault().lookup(ClassLoader.class);
            Class slhsClass = syscl.loadClass(slhsType.getLHSClass());
            if (slhsClass==null) return null; //class not defined!
            Constructor con = slhsClass.getDeclaredConstructor(String.class);
            slhs = (LifeStageInterface) con.newInstance(sKey);
        } catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
        return slhs;
    }
    
    /**
     *  Returns the defined spawned stage LHS class for the key 
     * @param key - name of LHS type
     * @return - class of output LHS associated with LHS type identified by key
     */
    public static String getSpawnedLHSClass(String key) {
        if (instance==null) instance = new LHS_Factory();
        LHS_Type lhs = instance.types.getType(key);
        String c = null;
        if (lhs!=null) c = lhs.getSpawnedLHSClass();
        return c;
    }
    
    /**
     *  Returns a "default" instance of the LHSTrackFeatureType class 
     *  associated with the given key.
     * @param key 
     * @return 
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     */
    public static LHSTrackFeatureType createTrackFeatureType(String key) 
                        throws InstantiationException, IllegalAccessException {
        if (instance==null) instance = new LHS_Factory();
        LHSTrackFeatureType tft = instance.trackFTs.get(key);
        if (tft!=null) return tft; //found it, so return.
        LHS_Type lhsType = instance.types.getType(key);
        if (lhsType==null) {
            logger.warning("in createTrackFeatureType(String key): Could not find lhsType for key: "+key);
            return null;
        }
        try {
            tft = new LHSTrackFeatureType(key);
            instance.trackFTs.put(key,tft);//update trackFTs
        } catch (SecurityException | IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        }
        return tft;
    }
    
    /**
     *  Returns a track feature representing the LHS object track
     * @param lhs 
     * @return 
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     */
    public static Feature createTrackFeature(LifeStageInterface lhs) 
                        throws InstantiationException, IllegalAccessException {
        if (instance==null) instance = new LHS_Factory();
        Feature feature = null;
        if (lhs.getTrack(LifeStageInterface.COORDINATE_TYPE_PROJECTED).length>1){
            LHSTrackFeatureType ftLHS = createTrackFeatureType(lhs.getTypeName());
            ftLHS.setGeometry(lhs.getTrack(LifeStageInterface.COORDINATE_TYPE_PROJECTED));
            try {
                feature = ftLHS.createFeature();
            } catch (IllegalAttributeException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return feature;
    }
    
    /**
     *  Returns an array of the defined type names 
     * @return - String array of names of defined types 
     */
    public static String[] getTypeNames() {
        if (instance==null) instance = new LHS_Factory();
        String[] strv = new String[1];
        strv = instance.types.getKeys().toArray(strv);
        return strv;
    }
    
    /**
     * 
     * @return - new id number
     */
    public static long getNewID() {
        if (instance==null) instance = new LHS_Factory();
        return instance.id++;
    }
    
    /**
     * reset the ID counter to n.
     * @param n - integer value to which to reset id.
     */
    public static void resetID(int n) {
        if (instance==null) instance = new LHS_Factory();
        instance.id=n;
    }

    /**
     *  Returns color associated with the defined LHS type name
     *  @param key - defined LHS type name
     *  @return    - associated color as java.awt.Color 
     */
    public static Color getLHScolor(String key) {
        if (instance==null) instance = new LHS_Factory();
        LHS_Type lhs = instance.types.getType(key);
        return lhs.getColor();
    }
    
    /**
     * Adds a PopertyChangeListener to the instance.
     * 
     * @param l 
     */
    public static synchronized void addPropertyChangeListener(PropertyChangeListener l){
        if (instance==null) instance = new LHS_Factory();
        instance.propertySupport.addPropertyChangeListener(l);
    }
    
    /**
     * Removes a PopertyChangeListener to the instance.
     * 
     * @param l 
     */
    public static synchronized void removePropertyChangeListener(PropertyChangeListener l){
        if (instance==null) instance = new LHS_Factory();
        instance.propertySupport.removePropertyChangeListener(l);
    }
    
    /** reset property: indicates that LHS_Factory.reset() has been called. */
    public static final String PROP_RESET = "reset";
    /** support for throwing property changes */
    transient private PropertyChangeSupport propertySupport;

    /** the singleton LHS_Types instance */
    private LHS_Types types;
    /** lookup map by LH stage name for parameters objects associated with different LHS types */
    private Map<String,LifeStageParametersInterface> paramsMap;
    /** lookup map by LH stage name for point feature type objects associated with different LHS types */
    private Map<String,LHSPointFeatureType> pointFTs;
    /** lookup map by LH stage name for point feature type objects without LifeStageAttributes associated with different LHS types */
    private Map<String,LHSPointFeatureTypeNoAtts> pointFTsNoAtts;
    /** lookup map by LH stage name for track feature type objects associated with different LHS types */
    private Map<String,LHSTrackFeatureType> trackFTs;
    /** lookup map by LH stage name for attributes instance constructors associated with different LHS types */
    private Map<String,Constructor> attributesConstructors;
    /** lookup map by LH stage name for LHS instance constructors associated with different LHS types */
    private Map<String,Constructor> lhsConstructors;
    /** parser object for csv files */
    private ParseCSV pCSV = null;

//    private static LHSTrackFeatureType t = new LHSTrackFeatureType();
    private long id = 0;
    
    /** Can't instantiate LHS_Factory. Get the singleton instance using LHS_Factory.getInstance()  */
    protected LHS_Factory() {
        logger.info("Instantiating LHS_Factory");
        types = LHS_Types.getInstance();//get the singleton once!
        
        paramsMap              = new LinkedHashMap<>();
        pointFTs               = new LinkedHashMap<>();
        pointFTsNoAtts         = new LinkedHashMap<>();
        trackFTs               = new LinkedHashMap<>();
        attributesConstructors = new LinkedHashMap<>();
        lhsConstructors        = new LinkedHashMap<>();
        
        propertySupport = new PropertyChangeSupport(this);
        
        types.addPropertyChangeListener(this);
    }
    
    /**
     *  Clears the local LHS_Types object currently held and creates a new set
     */
    public void resetInstance() {
//        types.removePropertyChangeListener(this);//don't need to do this for singleton
        
        paramsMap.clear();
        pointFTs.clear();
        trackFTs.clear();
        attributesConstructors.clear();
        lhsConstructors.clear();
        
        id = 0;
//        types = LHS_Types.getInstance();       //don't need to do tis for singleton
//        types.addPropertyChangeListener(this);
    }
    
    /**
     * Removes the objects associated with the LHS name "key" from all local maps.
     * 
     * @param key 
     */
    private void removeType(String key){
        paramsMap.remove(key);
        pointFTs.remove(key);
        trackFTs.remove(key);
        attributesConstructors.remove(key);
        lhsConstructors.remove(key);
    }

    /**
     * Responds to PropertyChange events fired by the LHS_Types singleton. 
     * 
     * Currently, resetInstance() is called for events of types
     * LHS_Types.PROP_addType, PROP_removeType, and PROP_renameType.
     * 
     * Events of type LHS_Types.PROP_types are ignored.
     *      
     * 
     * @param evt 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(LHS_Types.PROP_types)){
            resetInstance();
            logger.info("propertyChange(): detected PROP_Types change");
        } else 
        if (evt.getPropertyName().equals(LHS_Types.PROP_addType)){
            //don't really need to do anything here
//            LHS_Type type = (LHS_Type)evt.getNewValue();
//            addType(type.getLHSName());
            logger.info("propertyChange(): detected PROP_addType change");
//            resetInstance();
        } else 
        if (evt.getPropertyName().equals(LHS_Types.PROP_removeType)){
            LHS_Type type = (LHS_Type)evt.getOldValue();
            removeType(type.getLHSName());
            logger.info("propertyChange(): detected PROP_removeType change");
//            resetInstance();
        } else 
        if (evt.getPropertyName().equals(LHS_Types.PROP_typeNameChange)){
            String oldKey = (String)evt.getOldValue();
            String newKey = (String)evt.getNewValue();
            LifeStageParametersInterface oldP = paramsMap.remove(oldKey);
            if (oldP!=null) {
                oldP.setTypeName(newKey);//just change the name
                paramsMap.put(newKey,oldP);//file under new name
            }
            if (pointFTs.remove(oldKey)!=null) {
                try {
                    createPointFeatureType(newKey);
                } catch (InstantiationException | IllegalAccessException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (trackFTs.remove(oldKey)!=null) {
                try {
                    createTrackFeatureType(newKey);
                } catch (InstantiationException | IllegalAccessException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (attributesConstructors.remove(oldKey)!=null) {
                try {
                    createAttributes(newKey);
                } catch (InstantiationException | IllegalAccessException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (lhsConstructors.remove(oldKey)!=null) {
                try {
                    createLHS(newKey);
                } catch (InstantiationException | IllegalAccessException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            logger.info("propertyChange(): detected PROP_typeNameChange");
//            resetInstance();
        } else {
            return;
        }
        //fire PropertyChange indicating changes.
        propertySupport.firePropertyChange(PROP_RESET,null,null);
    }

    @Override
    public void exceptionThrown(Exception e) {
        StackTraceElement[] stes = e.getStackTrace();
        String txt = "";
        for (StackTraceElement ste : stes) {
            txt = txt + ste.toString() + "\n";
        }
        JOptionPane.showMessageDialog(null, txt,"Error in LHS_Factory",JOptionPane.ERROR_MESSAGE);
        logger.warning(txt);
    }
}
