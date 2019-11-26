/*
 * ModelControllerBean.java
 *
 * Created on December 15, 2005, 10:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.framework;

import com.wtstockhausen.utils.SwingWorker;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import wts.models.DisMELS.events.AnimationEventListener;
import wts.models.DisMELS.events.AnimationEventSupport;
import wts.roms.model.*;

/**
 *
 * @author William Stockhausen
 */
public class ModelControllerBean extends Object 
                                 implements Cloneable, Serializable {

    /* debug flag */
    public static boolean debug = true; //set for testing
    
    /** a logger for messages */
    private static final Logger logger = Logger.getLogger(ModelControllerBean.class.getName());
    
    
    //Fields req'd for Beans support
    public static final String PROP_CURRDATE = "current date/time";
    public static final String PROP_OCEANTIME = "ocean time";
    public static final String PROP_ILPFN = "Initial attributes file";
    public static final String PROP_LRFN  = "Results file";
    public static final String PROP_CNFN  = "Connectivity file";
    public static final String PROP_DFN   = "ROMS dataset file";
    public static final String PROP_PFN   = "LHS parameters file";
    public static final String PROP_TIME  = "Start time";
    public static final String PROP_NT    = "# of environmental model time steps";
    public static final String PROP_DT    = "Environmental model time step (s)";
    public static final String PROP_NLPT  = "# of LPT time steps per environmental time step";
    public static final String PROP_runForward    = "Flag to run model forward or backward";
    public static final String PROP_animationRate = "Rate for showing animation";
    public static final String PROP_resultsRate   = "Rate for writing results to disk";
    public static final String PROP_lhsStageTransRate = "Rate for evaluating LHS stage transitions";
    public static final String PROP_randomNumberSeed  = "Seed for random numbers";
    public static final String PROP_fixedEnvironment  = "Fix the environment";
    public static final String PROP_interpolateLikeROMS  = "Interpolate like ROMS";
    public static final String PROP_maskLikeROMS  = "Use rho mask for u,v like ROMS";
    public static final String PROP_noVerticalMotion  = "No vertical motion";
    public static final String PROP_noAdvection  = "No advection";
    public static final String PROP_updateLayerDepthsLikeROMS  = "Update layer depths like ROMS";
    public static final String PROP_showDeadIndivs  = "Show tracks for dead indivs";
    public static final String PROP_includeTracksInOutput = "Include tracks in output?";
    
    private transient PropertyChangeSupport propertySupport;

    private static final String cc = ",";

     /** GlobalInfo singleton */
    private GlobalInfo globalInfo = null;
    
   //Bean parameters
    /** ROMS dataset name */
    private String file_ROMSDataset = "";
    /** LHS parameters file name */
    private String file_Params = "";
    /** Initial attributes file for indivs */
    private String file_InitialAttributes = "";
    /** Results file */
    private String file_Results = "";
    /** Connectivity file */
    private String file_ConnResults = "";
    /** Time to start model integration */
    private double startTime = 0.0;
    /** # of times to step environmental model */
    private int ntEnvironModel = 1;
    /** time step for environmental model */
    private double timeStep = 1.0;
    /** # steps for LPT integration per environmental time step */
    private int ntBioModel = 1;
    /** Flag to write larval results at LPT integration timestep */
    private boolean writeFast = true;
    /** Rate at which to update animation */
    private int animationRate = 1;
    /** Rate at which to write results to disk */
    private int resultsRate = 1;
    /** Rate at which to evaluate LHS stage transitions */
    private int lhsStageTransRate = 1;
    /** Seed for random number generator */
    private long randomNumberSeed = -1;
    /** Flag to run forwards/backwards */
    private boolean runForward = true;
    /* flag to keep environment from updating */
    public boolean fixedEnvironment = false;
    /** flag to ignore vertical velocities */
    private boolean noVerticalMotion = LagrangianParticleTracker.noVerticalMotion;
    /** flag to ignore vertical velocities */
    private boolean noAdvection = LagrangianParticleTracker.noAdvection;
    /** flag to ignore use rho mask for u,v interpolation */
    private boolean maskLikeROMS = LagrangianParticleTracker.maskLikeROMS;
    /** flag to interpolate exactly like ROMS 2.1 */
    private boolean interpolateLikeROMS = Interpolator3D.interpolateLikeROMS;
    /** flag to update layer depths as in ROMS */
    private boolean updateLayerDepthsLikeROMS = PhysicalEnvironment.updateLayerDepthsLikeROMS;
    /** flag to show end points, tracks for dead indivs */
    private boolean showDeadIndivs = true;
    /** flag to include track info in output */
    private boolean includeTracksInOutput = true;
    
    //Transient fields
    /** flag for gui or batch mode */
    private transient boolean guiMode = true;
    /** ocean time */
    private transient double time;
    
    /** possible ROMS datasets */
    private transient String[] files_ROMSDatasets = null;
    /** current ROMS dataset */
    private transient String file_CurrROMSDataset = "";
    /** index to current ROMS dataset in files_ROMSDatasets */
    int indx_CurrROMSDataset = -1;
    
    private transient ModelGrid3D grid3D;
    private transient NetcdfReader netcdfReader;

    private transient PhysicalEnvironment pe1;
    private transient PhysicalEnvironment pe2;
    private transient PhysicalEnvironment pet;
    private transient Interpolator3D i3dt;
    
    private transient List<LifeStageInterface> indivs;
    private transient Iterator<LifeStageInterface> it;
    
    private transient FeatureCollection fcStartPoints = null;
    private transient FeatureCollection fcEndPoints   = null;
    private transient FeatureCollection fcTracks      = null;
    private transient FeatureCollection fcDeadPoints  = null;
    private transient FeatureCollection fcDeadTracks  = null;
    private transient final FeatureCollection tmpStrtPts  = FeatureCollections.newCollection();
    private transient final FeatureCollection tmpPts      = FeatureCollections.newCollection();
    private transient final FeatureCollection tmpTrks     = FeatureCollections.newCollection();
    private transient final FeatureCollection tmpDeadPts  = FeatureCollections.newCollection();
    private transient final FeatureCollection tmpDeadTrks = FeatureCollections.newCollection();

    private transient HashMap<String,PrintWriter> pwResultsMap;//map to PrintWriters for results
    private transient HashMap<String,PrintWriter> pwConnResultsMap;//map to PrintWriters for results
    
    private transient String statMessage;
    private transient int statTime;
    
    /** step counter for updating animation */ 
    private int animCtr;
    /** step counter for writing results */ 
    private int resCtr;
    /** step counter for updating life stage transitions */ 
    private int lhsStageTransCtr;
    /** counter for total bio model time steps */ 
    private long stpCtr;
    
    /** flag to update animation */
    private transient boolean updateAnimation  = false;
    /** flag to write results to file */
    private transient boolean updateResults    = false;
    /** flag to update life stage transitions */
    private transient boolean updateStageTrans = false;
    
    private transient final AnimationEventSupport animEventSupport;

    /**
     * Creates a new instance of ModelControllerBean
     */
    public ModelControllerBean() {
        globalInfo = GlobalInfo.getInstance();
        propertySupport = new PropertyChangeSupport(this);
        animEventSupport = new AnimationEventSupport();
    }
    
    public ModelControllerBean(String dfn, 
                               String ilpfn, 
                               String lrfn,
                               String pfn,
                               double startTime, int nTimes, 
                               double dt, int nLPTsteps) {
        globalInfo = GlobalInfo.getInstance();
        propertySupport = new PropertyChangeSupport(this);
        animEventSupport = new AnimationEventSupport();
        this.file_ROMSDataset       = dfn;
        this.file_InitialAttributes = ilpfn;
        this.file_Results           = lrfn;
        this.file_Params            = pfn;
        this.startTime      = startTime;
        this.ntEnvironModel = nTimes;
        this.timeStep       = dt;
        this.ntBioModel     = nLPTsteps;
    }
    
    @Override
    public Object clone() {
        try {
            ModelControllerBean clone = (ModelControllerBean) super.clone();
            clone.propertySupport        = new PropertyChangeSupport(clone);
            clone.file_ROMSDataset       = file_ROMSDataset;
            clone.file_InitialAttributes = file_InitialAttributes;
            clone.file_Results           = file_Results;
            clone.file_ConnResults       = file_ConnResults;
            clone.file_Params            = file_Params;
            clone.startTime         = startTime;
            clone.ntEnvironModel    = ntEnvironModel;
            clone.timeStep          = timeStep;
            clone.ntBioModel        = ntBioModel;
            clone.writeFast         = writeFast;
            clone.runForward        = runForward;
            clone.animationRate     = animationRate;
            clone.resultsRate       = resultsRate;            
            clone.lhsStageTransRate = lhsStageTransRate;
            clone.randomNumberSeed  = randomNumberSeed;
            return clone;
        } catch (java.lang.CloneNotSupportedException exc) {
            logger.info("ModelControllerBean: Problem cloning");
            logger.info(exc.toString());
        }
        return null;
    }
    
    public boolean isGUIMode() {
        return guiMode;
    }
    
    public void setGUIMode(boolean mode) {
        guiMode = mode;
    }
    
    public long getRandomNumberSeed() {
        return randomNumberSeed;
    }
    
    public void setRandomNumberSeed(long rns) {
        long oldValue = randomNumberSeed;
        randomNumberSeed = rns;
        propertySupport.firePropertyChange(PROP_randomNumberSeed, oldValue, randomNumberSeed);
    }

    public int getAnimationRate() {
        return animationRate;
    }
    
    public void setAnimationRate(int ar) {
        int oldValue = animationRate;
        animationRate = ar;
        propertySupport.firePropertyChange(PROP_animationRate, oldValue, animationRate);
    }

    public int getResultsRate() {
        return resultsRate;
    }
    
    public void setResultsRate(int rr) {
        int oldValue = resultsRate;
        resultsRate = rr;
        propertySupport.firePropertyChange(PROP_resultsRate, oldValue, resultsRate);
    }

    public int getStageTransitionRate() {
        return lhsStageTransRate;
    }
    
    public void setStageTransitionRate(int sr) {
        int oldValue = lhsStageTransRate;
        lhsStageTransRate = sr;
        propertySupport.firePropertyChange(PROP_lhsStageTransRate, oldValue, lhsStageTransRate);
    }

    public FeatureCollection getStartPointsFC() {
        return fcStartPoints;
    }
    
    public void setStartPointsFC(FeatureCollection newFC) {
        if (fcStartPoints!=null) fcStartPoints.clear();
        fcStartPoints = newFC;
    }

    public FeatureCollection getEndPointsFC() {
        return fcEndPoints;
    }
    
    public void setEndPointsFC(FeatureCollection newFC) {
        if (fcEndPoints!=null) fcEndPoints.clear();
        fcEndPoints = newFC;
    }

    public FeatureCollection getDeadPointsFC() {
        return fcDeadPoints;
    }
    
    public void setDeadPointsFC(FeatureCollection newFC) {
        if (fcDeadPoints!=null) fcDeadPoints.clear();
        fcDeadPoints = newFC;
    }

    public FeatureCollection getDeadTracksFC() {
        return fcDeadTracks;
    }
    
    public void setDeadTracksFC(FeatureCollection newFC) {
        if (fcDeadTracks!=null) fcDeadTracks.clear();
        fcDeadTracks = newFC;
    }

    public FeatureCollection getTracksFC() {
        return fcTracks;
    }
    
    public void setTracksFC(FeatureCollection newFC) {
        if (fcTracks!=null) fcTracks.clear();
        fcTracks = newFC;
    }
    
    public String getFile_InitialAttributes() {
        return file_InitialAttributes;
    }
    
    public void setFile_InitialAttributes(String fn) {
        String oldValue = file_InitialAttributes;
        file_InitialAttributes = fn;
        propertySupport.firePropertyChange(PROP_ILPFN, oldValue, file_InitialAttributes);
    }
    
    public String getFile_Params() {
        return file_Params;
    }
    
    public void setFile_Params(String fn) {
        String oldValue = file_Params;
        file_Params = fn;
        propertySupport.firePropertyChange(PROP_PFN, oldValue, file_Params);
    }
    
    public String getFile_Results() {
        return file_Results;
    }
    
    public void setFile_Results(String fn) {
        String oldValue = file_Results;
        file_Results = fn;
        propertySupport.firePropertyChange(PROP_LRFN, oldValue, file_Results);
    }
    
    public String getFile_ConnResults() {
        return file_ConnResults;
    }
    
    public void setFile_ConnResults(String fn) {
        String oldValue = file_ConnResults;
        file_ConnResults = fn;
        propertySupport.firePropertyChange(PROP_CNFN, oldValue, file_ConnResults);
    }
    
    public boolean getIncludeTracksInOutput(){
        return includeTracksInOutput;
    }
    
    public void setIncludeTracksInOutput(boolean b){
        boolean oldValue = includeTracksInOutput;
        AbstractLHS.setWriteTracksFlagForClass(b);
        includeTracksInOutput = b;
        propertySupport.firePropertyChange(PROP_includeTracksInOutput, oldValue, includeTracksInOutput);
    }
//    
//    public String getFile_ROMSGrid() {
//        return globalInfo.getGridFile();
//    }
//    
//    public String getFile_ROMSCanonicalDataset() {
//        return globalInfo.getCanonicalFile();
//    }
    
    public String getFile_ROMSDataset() {
        return file_ROMSDataset;
    }
    
    public void setFile_ROMSDataset(String fn) {
        String oldValue = file_ROMSDataset;
        file_ROMSDataset = fn;
        propertySupport.firePropertyChange(PROP_DFN, oldValue, file_ROMSDataset);
    }
    
    public double getTimeStep() {
        return timeStep;
    }
    
    public void setTimeStep(double val) {
        double oldValue = timeStep;
        timeStep = val;
        propertySupport.firePropertyChange(PROP_DT, oldValue, timeStep);
    }
    
    public int getNtBioModel() {
        return ntBioModel;
    }
    
    public void setNtBioModel(int val) {
        int oldValue = ntBioModel;
        ntBioModel = val;
        propertySupport.firePropertyChange(PROP_NLPT, oldValue, ntBioModel);
    }
    
    public int getNtEnvironModel() {
        return ntEnvironModel;
    }
    
    public void setNtEnvironModel(int val) {
        int oldValue = ntEnvironModel;
        ntEnvironModel = val;
        propertySupport.firePropertyChange(PROP_NT, oldValue, ntEnvironModel);
    }
    
    public double getStartTime() {
        return startTime;
    }
    
    public void setStartTime(double val) {
        double oldValue = startTime;
        startTime = val;
        propertySupport.firePropertyChange(PROP_TIME, oldValue, startTime);
    }
    
    public boolean isRunForward() {
        return runForward;
    }
    
    public void setRunForward(boolean val) {
        boolean oldValue = runForward;
        runForward = val;
        propertySupport.firePropertyChange(PROP_runForward, oldValue, runForward);
    }

    public boolean isFixedEnvironment() {
        return fixedEnvironment;
    }

    public void setFixedEnvironment(boolean val) {
        boolean oldValue = fixedEnvironment;
        fixedEnvironment = val;
        propertySupport.firePropertyChange(PROP_fixedEnvironment, oldValue, fixedEnvironment);
    }

    public boolean isInterpolateLikeROMS() {
        return interpolateLikeROMS;
    }

    public void setInterpolateLikeROMS(boolean val) {
        boolean oldValue = interpolateLikeROMS;
        interpolateLikeROMS = val;
        propertySupport.firePropertyChange(PROP_interpolateLikeROMS, oldValue, interpolateLikeROMS);
//        Interpolator3D.interpolateLikeROMS = interpolateLikeROMS;
    }

    public boolean isMaskLikeROMS() {
        return maskLikeROMS;
    }

    public void setMaskLikeROMS(boolean val) {
        boolean oldValue = maskLikeROMS;
        maskLikeROMS = val;
        propertySupport.firePropertyChange(PROP_maskLikeROMS, oldValue, maskLikeROMS);
//        LagrangianParticleTracker.maskLikeROMS = maskLikeROMS;
    }

    public boolean isNoVerticalMotion() {
        return noVerticalMotion;
    }

    public void setNoVerticalMotion(boolean val) {
        boolean oldValue = val;
        noVerticalMotion = val;
        propertySupport.firePropertyChange(PROP_noVerticalMotion, oldValue, noVerticalMotion);
    }

    public boolean isNoAdvection() {
        return noAdvection;
    }

    public void setNoAdvection(boolean val) {
        boolean oldValue = val;
        noAdvection = val;
        propertySupport.firePropertyChange(PROP_noAdvection, oldValue, noAdvection);
    }

    public boolean isUpdateLayerDepthsLikeROMS() {
        return updateLayerDepthsLikeROMS;
    }

    public void setUpdateLayerDepthsLikeROMS(boolean val) {
        boolean oldValue = updateLayerDepthsLikeROMS;
        updateLayerDepthsLikeROMS = val;
        propertySupport.firePropertyChange(PROP_updateLayerDepthsLikeROMS, oldValue, updateLayerDepthsLikeROMS);
//        PhysicalEnvironment.updateLayerDepthsLikeROMS = updateLayerDepthsLikeROMS;
    }
        
    public boolean getShowDeadIndivs() {
        return showDeadIndivs;
    }

    public void setShowDeadIndivs(boolean b) {
        boolean oldValue = showDeadIndivs;
        showDeadIndivs = b;
        propertySupport.firePropertyChange(PROP_showDeadIndivs, oldValue, showDeadIndivs);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /**
     * Method to clean up biggest memory hogs(?) prior to initializing/running a
     * new model.
     */
    public void cleanup(){
            if (fcStartPoints!=null) fcStartPoints.clear();
            if (fcEndPoints!=null)   fcEndPoints.clear();
            if (fcTracks!=null)      fcTracks.clear();
            if (fcDeadPoints!=null)  fcDeadPoints.clear();
            if (fcDeadTracks!=null)  fcDeadTracks.clear();
            tmpDeadPts.clear();
            tmpDeadTrks.clear();
            tmpPts.clear();
            tmpStrtPts.clear();
            tmpTrks.clear();
            
            it = null;
            if (indivs!=null) indivs.clear();
            indivs = null;
            
            LagrangianParticle.setTracker(null);
            
            globalInfo.setInterpolator3D(null);//set global instance to null (seems like really bad idea, but need to to clean up memory)
            AbstractLHS.i3d = null;//set to null to clean up memory
            i3dt   = null;
            pe1    = null;
            pe2    = null;
            pet    = null;
            grid3D = null;
            
            netcdfReader = null;
            
            System.gc();//call garbage control to clean up
    }
    
    //The following methods concern running the model   
    public void initialize() throws IOException, 
                                    FileNotFoundException {
        try {
            cleanup();
            
            if (randomNumberSeed>0) {
                globalInfo.setRandomNmberGeneratorSeed(randomNumberSeed);
            } else {
                globalInfo.setRandomNmberGeneratorSeed(System.currentTimeMillis());
            }
            logger.info("++Using random number seed "+globalInfo.getRandomNumberGenerator().getSeed());
            if (runForward) 
                timeStep = Math.abs(timeStep);
            else 
                timeStep = -Math.abs(timeStep);
            if (fixedEnvironment) {
                logger.info("ModelControllerBean: WARNING!! MCB.fixedEnvironment is true!!");
            }
            if (noVerticalMotion) {
                LagrangianParticleTracker.noVerticalMotion = noVerticalMotion;
                logger.info("ModelControllerBean: WARNING!! LPT.noVerticalMotion is true!!");
            }
            if (noAdvection) {
                LagrangianParticleTracker.noAdvection = noAdvection;
                logger.info("ModelControllerBean: WARNING!! LPT.noAdvection is true!!");
            }
            if (maskLikeROMS) {
                LagrangianParticleTracker.maskLikeROMS = maskLikeROMS;
                logger.info("ModelControllerBean: WARNING!! LPT.maskLikeROMS is true!!");
            }
            if (interpolateLikeROMS) {
                Interpolator3D.interpolateLikeROMS = interpolateLikeROMS;
                logger.info("ModelControllerBean: WARNING!! I3D.interpolateLikeROMS is true!!");
            }
            if (updateLayerDepthsLikeROMS) {
                PhysicalEnvironment.updateLayerDepthsLikeROMS = updateLayerDepthsLikeROMS;
                logger.info("ModelControllerBean: WARNING!! PE.updateLayerDepthsLikeROMS is true!!");
            }
            statMessage = "Initializing output files";
            logger.info(statMessage);
            initializeOutputFiles();
            statMessage = "Initializing environment";
            logger.info(statMessage);
            initializeEnvironment();
            statMessage = "Initializing biology";
            logger.info(statMessage);
            initializeBioModel();
            statMessage="Finished initialization";
        } catch (FileNotFoundException ex) {
            statMessage="Initialization halted: FileNotFoundException thrown";
            logger.info(statMessage);
            throw ex;
        } catch(IOException ex) {
            statMessage="Initialization halted: IOException thrown";
            logger.info(statMessage);
            throw ex;
        }
        //initialize counters and model time
        animCtr = mod(1,animationRate);             //animation step counter
        resCtr = mod(1,resultsRate);                //writing results step counter
        lhsStageTransCtr = mod(1,lhsStageTransRate);//evaluation of life stage transitions step counter
        stpCtr = 0;                                 //counter for total bio model timesteps
        statTime = 0;                               //??
        time = startTime;//set model time
        if (guiMode){
            //propertySupport.firePropertyChange(PROP_CURRDATE, null, globalInfo.getCalendar().getDate());
            propertySupport.firePropertyChange(PROP_OCEANTIME, null, startTime);
            animEventSupport.fireAnimationEvent();
        }
    }
    
    protected void initializeOutputFiles() {
        pwResultsMap     = new HashMap<>();
        pwConnResultsMap = new HashMap<>();
        LifeStageAttributesInterface atts = null;
        String file = null;
        LHS_Types info = LHS_Types.getInstance();
        Iterator<String> itr = info.getKeys().iterator();
        while (itr.hasNext()) {
            String lsType = itr.next();
            String lsiClass  = info.getType(lsType).getLHSClass();
            String attsClass = info.getType(lsType).getAttributesClass();
            ClassLoader syscl = Lookup.getDefault().lookup(ClassLoader.class);
            try{
                Class attsClazz = syscl.loadClass(attsClass);
                Constructor con = attsClazz.getDeclaredConstructor();
                atts = (LifeStageAttributesInterface) con.newInstance();
                if (!pwResultsMap.containsKey(attsClass)){
                    try {
                        file = globalInfo.getWorkingDir()+file_Results+"."+lsiClass+".csv";
                        logger.info("Writing model results to '"+file+"'");
                        FileWriter fwResults = new FileWriter(file);
                        PrintWriter pwResults = new PrintWriter(fwResults);
                        pwResults.println(atts.getCSVHeader());
                        pwResults.println(atts.getCSVHeaderShortNames());
                        pwResults.flush();
                        pwResultsMap.put(lsiClass, pwResults);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                        JOptionPane.showMessageDialog(null, "Could not create \n"+file,"ERROR",JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (!pwConnResultsMap.containsKey(attsClass)){
                    try {
                        file = globalInfo.getWorkingDir()+file_ConnResults+"."+lsiClass+".csv";
                        logger.info("Writing model connectivity results to '"+file+"'");
                        FileWriter fwConnResults = new FileWriter(file);
                        PrintWriter pwConnResults = new PrintWriter(fwConnResults);
                        pwConnResults.println(atts.getCSVHeader());
                        pwConnResults.println(atts.getCSVHeaderShortNames());
                        pwConnResults.flush();
                        pwConnResultsMap.put(lsiClass, pwConnResults);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                        JOptionPane.showMessageDialog(null, "Could not create \n"+file,"ERROR",JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex){
                Exceptions.printStackTrace(ex);
                JOptionPane.showMessageDialog(null, "Could not create 'test' object. Could not create \n"+file,"ERROR",JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException ex){
                Exceptions.printStackTrace(ex);
                JOptionPane.showMessageDialog(null, "Invalid class. Could not create \n"+file,"ERROR",JOptionPane.ERROR_MESSAGE);
            } catch (NoSuchMethodException ex){
                Exceptions.printStackTrace(ex);
            }
        }
    }
    
    protected void initializeEnvironment() throws IOException {
        //create ModelGrid3D instance
        if (!ModelGrid3D.isGrid(globalInfo.getGridFile())) {
            JOptionPane.showMessageDialog(null, "file "+globalInfo.getGridFile()+" is not a ROMS grid", 
                                         "Error message", JOptionPane.ERROR_MESSAGE);
            throw(new IOException("Error in ModelControllerBean.initializeEnvironemnt(): \nfile '"+globalInfo.getGridFile()+"' is not a ROMS grid!!"));
        }
        //read canonical ROMS dataset for ModelGrid3D constant fields
        grid3D = globalInfo.getGrid3D();
        
        //get array of ROMS dataset names in folder with "first" one
        try {
            files_ROMSDatasets = com.wtstockhausen.utils.FilesLister.findFilesWithSameExtension(file_ROMSDataset);
            String msg = "InitializeEnvironment:: available ROMS datasets:";
            for (String file_ROMSDataset : files_ROMSDatasets) msg = msg + "\n \t '" + file_ROMSDataset+"'";
            logger.info(msg);
            //set "current" ROMS dataset to "first" one
            file_CurrROMSDataset = file_ROMSDataset;
            logger.info("InitializeEnvironment:: current ROMS dataset: '"+file_CurrROMSDataset+"'");
            //identify location of "current" ROMS dataset in filenames array
            indx_CurrROMSDataset = Arrays.binarySearch(files_ROMSDatasets, file_CurrROMSDataset);
            logger.info("InitializeEnvironment:: current ROMS dataset has index: "+indx_CurrROMSDataset);
        } catch (IOException ex){
            JOptionPane.showMessageDialog(null, "file '"+file_ROMSDataset+"' was not found!", 
                                         "Error message", JOptionPane.ERROR_MESSAGE);
            throw(ex);
        }
        
        //set netcdfReader to read first ROMS dataset
        netcdfReader = new NetcdfReader(file_CurrROMSDataset);
        int nt = netcdfReader.getNumTimeSteps();
        //Identify PE1 & PE2 with which to start model
        if (runForward) {
            initializeEnvironmentForward(nt);
        } else {
            initializeEnvironmentBackwards(nt);
        }
        logger.info("Start time = "+startTime);
        logger.info("pe1 time   = "+pe1.getOceanTime());
        logger.info("pe2 time   = "+pe2.getOceanTime());
        if (!fixedEnvironment) {
            statMessage = "Interpolating environment";
            logger.info("Using varying environment!!");
            pet = PhysicalEnvironment.interpolate(startTime,pe1,pe2);
        } else {
            logger.info("Using constant environment!!");
            pet = pe1;
        }
//        statMessage = "Creating 3d interpolator";
//        i3dt = new Interpolator3D(pet);
        if (i3dt==null){
            statMessage = "Creating 3d interpolator";
            i3dt = new Interpolator3D(pet);
            LagrangianParticleTracker lpt = new LagrangianParticleTracker(i3dt);
            lpt.setTimeStep(timeStep/ntBioModel);
            LagrangianParticle.setTracker(lpt);
        } else {
            i3dt.setPhysicalEnvironment(pet);
        }
        globalInfo.setInterpolator3D(i3dt);//need this so AbstractLHS can get it
        
        globalInfo.getCalendar().setTimeOffset((long) startTime);
        propertySupport.firePropertyChange(PROP_OCEANTIME, null, startTime);
    }

    private void initializeEnvironmentForward(int nt) 
                        throws ArrayIndexOutOfBoundsException, IOException {        
        //Identify PE1 & PE2 with which to start model
        int it = -1;
        if (startTime<netcdfReader.getOceanTime(0)) {
            //set start time to time of first time slice in dataset
            setStartTime(netcdfReader.getOceanTime(0));
            logger.info("Start Time < initial Ocean Time ("+startTime+"), so resetting to initial Ocean Time.");
        }
        if (nt==1) {//only one time step per file
            while (it<0) {
                //Save time in the dataset.
                double lastT = netcdfReader.getOceanTime(0);
                //Assign pe1 to time slice in case we bracket startTime w/
                //the next dataset.
                logger.info("initializeEnvironment it = "+(nt-1));
                statMessage = "Reading environment 1 (as precaution) from "+file_CurrROMSDataset;
                pe1 =  new PhysicalEnvironment(0,netcdfReader);
                //Switch to next dataset.
                file_CurrROMSDataset = getNextFilename();
                logger.info("Switching to new netCDF file: "+file_CurrROMSDataset);
                netcdfReader.setNetcdfDataset(file_CurrROMSDataset);//open new dataset
                if ((startTime>=lastT)&&
                       (startTime<netcdfReader.getOceanTime(0))) {
                    //Success bracketing startTime!  Assign pe2.
                    logger.info("startTime bracketed between datasets!");
                    statMessage = "Reading environment 2";
                    pe2 = new PhysicalEnvironment(0,netcdfReader);
                    it = 0;//set to drop out of "while" condition
                } else {
                    pe1 = null; //reset pe1
                    //loop back to start of "while" and try with new dataset
                }
            }
        } else {//nt >1
            while (it<0) {
                //search through current dataset for match
                for (int i=1;i<nt;i++) {
                    logger.info("Testing for i: "+netcdfReader.getOceanTime(i-1)+"<="+startTime+"<"+netcdfReader.getOceanTime(i));
                   if ((startTime>=netcdfReader.getOceanTime(i-1))&&
                           (startTime<netcdfReader.getOceanTime(i)))
                       it = i-1;
                }
                if (it>-1) {
                    //Success bracketing startTime!
                    //Assign pe's.  Will drop out of "while" condition now.
                    logger.info("Success bracketing startTime!!");
                    logger.info("initializeEnvironment it = "+it);
                    statMessage = "Reading environment 1";
                    pe1 = new PhysicalEnvironment(it,netcdfReader);
                    statMessage = "Reading environment 2";
                    pe2 = new PhysicalEnvironment(it+1,netcdfReader);
                } else {
                    //startTime not bracketed within the dataset.
                    logger.info("startTime not bracketed w/in dataset!");
                    //Save last time in the dataset.
                    double lastT = netcdfReader.getOceanTime(nt-1);
                    //Assign pe1 to last time slice in case we bracket startTime w/
                    //the 1st time slice from the next dataset.
                    logger.info("initializeEnvironment it = "+(nt-1));
                    statMessage = "Reading environment 1 (as precaution)";
                    pe1 =  new PhysicalEnvironment(nt-1,netcdfReader);
                    //Switch to next dataset.
                    file_CurrROMSDataset = getNextFilename();
                    logger.info("Switching to new netCDF file: "+file_CurrROMSDataset);
                    netcdfReader.setNetcdfDataset(file_CurrROMSDataset);//open new dataset
                    nt = netcdfReader.getNumTimeSteps();//update number of time steps for new dataset
                    if ((startTime>=lastT)&&
                           (startTime<netcdfReader.getOceanTime(0))) {
                        //Success bracketing startTime!  Assign pe2.
                        logger.info("startTime brackedted between datasets!");
                        statMessage = "Reading environment 2";
                        pe2 = new PhysicalEnvironment(0,netcdfReader);
                        it = 0;//set to drop out of "while" condition
                    } else {
                        pe1 = null; //reset pe1
                        //loop back to start of "while" and try with new dataset
                    }
                }
            }//while loop
        }
    }
 
    //The following methods concern running the model
    protected void initializeEnvironmentBackwards(int nt) 
                        throws ArrayIndexOutOfBoundsException, IOException {
        int it = -1;
        if ((startTime==0)||(startTime>netcdfReader.getOceanTime(nt-1))) {
            //set start time to time of first time slice in dataset
            setStartTime(netcdfReader.getOceanTime(nt-1));
            logger.info("Start Time > initial Ocean Time ("+startTime+"), so resetting to initial Ocean Time.");
        }
        if (nt==1) {
            while (it<0) {
                //Save time in the dataset.
                double lastT = netcdfReader.getOceanTime(0);
                //Assign pe2 to time slice in case we bracket startTime w/
                //the next dataset.
                logger.info("initializeEnvironment it = "+(nt-1)+"; time = "+lastT);
                statMessage = "Reading environment 2 (as precaution) from "+file_CurrROMSDataset;
                pe2 =  new PhysicalEnvironment(0,netcdfReader);
                //Switch to next dataset.
                file_CurrROMSDataset = getPreviousFilename();
                logger.info("Switching to new netCDF file: "+file_CurrROMSDataset);
                netcdfReader.setNetcdfDataset(file_CurrROMSDataset);//open new dataset
                logger.info("new time  = "+netcdfReader.getOceanTime(0));
                if ((startTime<=lastT)&&
                       (startTime>netcdfReader.getOceanTime(0))) {
                    //Success bracketing startTime!  Assign pe1.
                    logger.info("startTime brackedted between datasets!");
                    statMessage = "Reading environment 1";
                    pe1 = new PhysicalEnvironment(0,netcdfReader);
                    it = 0;//set to drop out of "while" condition
                } else {
                    pe2 = null; //reset pe2
                    //loop back to start of "while" and try with new dataset
                }
            }
        } else {//nt>1
            it = nt-1;
            double ot = netcdfReader.getOceanTime(nt-1);
            logger.info("OceanTime["+(nt-1)+"] = "+ot);
            if (startTime>netcdfReader.getOceanTime(nt-1)) {
                setStartTime(netcdfReader.getOceanTime(nt-1));
            }
            for (int i=nt-1;i>0;i--) {
                ot = netcdfReader.getOceanTime(i-1);
                logger.info("OceanTime["+(i-1)+"] = "+ot);
                if ((startTime>=netcdfReader.getOceanTime(i-1))&&
                       (startTime<netcdfReader.getOceanTime(i)))
                   it = i;
            }
            logger.info("initializeEnvironment it = "+it);
            statMessage = "Reading environment 1";
            pe1 = new PhysicalEnvironment(it-1,netcdfReader);
            statMessage = "Reading environment 2";
            pe2 = new PhysicalEnvironment(it,netcdfReader);
            while (it<0) {
                //search through current dataset for match
                for (int i=(nt-1);i>0;i--) {
                    logger.info("Testing for i: "+netcdfReader.getOceanTime(i-1)+"<="+startTime+"<"+netcdfReader.getOceanTime(i));
                   if ((startTime>=netcdfReader.getOceanTime(i-1))&&
                           (startTime<netcdfReader.getOceanTime(i)))
                       it = i-1;
                }
                if (it>-1) {
                    //Success bracketing startTime!
                    //Assign pe's.  Will drop out of "while" condition now.
                    logger.info("Success bracketing startTime!!");
                    logger.info("initializeEnvironment it = "+it);
                    statMessage = "Reading environment 1";
                    pe1 = new PhysicalEnvironment(it,netcdfReader);
                    statMessage = "Reading environment 2";
                    pe2 = new PhysicalEnvironment(it+1,netcdfReader);
                } else {
                    //startTime not bracketed within the dataset.
                    logger.info("startTime not bracketed w/in dataset!");
                    //Save last time in the dataset.
                    double lastT = netcdfReader.getOceanTime(0);
                    //Assign pe2 to last time slice in case we bracket startTime w/
                    //the 1st time slice from the next dataset.
                    logger.info("initializeEnvironment it = "+0);
                    statMessage = "Reading environment 2 (as precaution)";
                    pe2 =  new PhysicalEnvironment(0,netcdfReader);
                    //Switch to next dataset.
                    file_CurrROMSDataset = getPreviousFilename();
                    logger.info("Switching to new netCDF file: "+file_CurrROMSDataset);
                    netcdfReader.setNetcdfDataset(file_CurrROMSDataset);//open new dataset
                    nt = netcdfReader.getNumTimeSteps();
                    if ((startTime<lastT)&&
                           (startTime>=netcdfReader.getOceanTime(nt-1))) {
                        //Success bracketing startTime!  Assign pe1.
                        logger.info("startTime bracketed between datasets!");
                        statMessage = "Reading environment 1";
                        pe1 = new PhysicalEnvironment(nt-1,netcdfReader);
                        it = 0;//set to drop out of "while" condition
                    } else {
                        pe2 = null; //reset pe2
                        //loop back to start of "while" and try with new dataset
                    }
                }
            }//while loop
        }
    }
 
    protected void timestepEnvironment() throws IOException {
        logger.info("Time-stepping environment...");
        if (pe2.getOceanTime()<=time) {
            pe1 = pe2;
            if (pe2.hasNext()) {
                logger.info("Getting next pe");
                pe2 = pe2.next();
                System.gc(); //garbage collection. TODO: is this necessary?
                logger.info("Got next pe "+pe2.getOceanTime());
            } else {
                logger.info("Trying next netcdf file");
                tryNext();
            }
        }
        pet = null;
        System.gc();//TODO: is this necessary?
        pet = PhysicalEnvironment.interpolate(time,pe1,pe2);
        i3dt.setPhysicalEnvironment(pet);
        logger.info("Environment interpolated to time: "+pet.getOceanTime());
    }
 
    protected void timestepEnvironmentBackwards() throws IOException {
        logger.info("Time-stepping environment backward...");
        if (pe1.getOceanTime()>time) {
            pe2 = pe1;
            if (pe1.hasPrevious()) {
                logger.info("Getting previous pe");
                pe1 = pe1.previous();
                logger.info("Got previous pe "+pe1.getOceanTime());
            } else {
                logger.info("Trying next netcdf file");
                tryPrevious();
            }
        }
        pet = null;
        pet = PhysicalEnvironment.interpolate(time,pe1,pe2);
        i3dt.setPhysicalEnvironment(pet);
        logger.info("Environment interpolated to time: "+pet.getOceanTime());
    }
 
    /**
     * Method is called internally when reading the "next" PhysicalEnvironment
     * from the netCDF file fails (because there aren't any more time steps in 
     * the file).  The method increments the netCDF file name and attempts to 
     * get the first PhysicalEnvironment from the new file.
     * 
     * @throws java.io.IOException 
     */
    protected void tryNext() throws IOException {
        file_CurrROMSDataset = getNextFilename();
        logger.info("Switching to new netCDF file: "+file_CurrROMSDataset);
        netcdfReader.setNetcdfDataset(file_CurrROMSDataset);
        pe2 = new PhysicalEnvironment(0,netcdfReader);
    }
 
    /**
     * Method is called internally to increment the netCDF file name for the
     * next ROMS dataset.
     * 
     * @return String - next ROMS dataset filename
     */
    protected String getNextFilename() {
//        int n = file_CurrROMSDataset.length();
//        //logger.info("file_CurrROMSDataset = "+file_CurrROMSDataset);
//        String idx = file_CurrROMSDataset.substring(n-7,n-3);
//        //logger.info("idx = "+idx);
//        int ndx = Integer.parseInt(idx);
//        char[] idxc = String.valueOf(ndx+1).toCharArray();
//        //logger.info("new index = "+String.valueOf(idxc));
//        char[] dfnc = file_CurrROMSDataset.toCharArray();
//        for (int i=idxc.length;i>0;i--) {
//            dfnc[n-(4+(idxc.length-i))] = idxc[i-1];
//        }
//        return String.valueOf(dfnc);
        logger.info("in getNextFilename()");
        if (indx_CurrROMSDataset<(files_ROMSDatasets.length-1)){
            indx_CurrROMSDataset++;
            logger.info("incrementing indx_CurrROMSDataset to: "+indx_CurrROMSDataset);
            logger.info("next ROMS dataset: "+files_ROMSDatasets[indx_CurrROMSDataset]);
            return files_ROMSDatasets[indx_CurrROMSDataset];
        }
        return "";
    }
    
    /**
     * Method is called internally to decrement the netCDF file name for the
     * previous ROMS dataset.
     * 
     * @return String - previous ROMS dataset filename
     */
    protected String getPreviousFilename() {
//        int n = file_CurrROMSDataset.length();
//        //logger.info("file_CurrROMSDataset = "+file_CurrROMSDataset);
//        String idx = file_CurrROMSDataset.substring(n-7,n-3);
//        //logger.info("idx = "+idx);
//        int ndx = Integer.parseInt(idx);
//        char[] idxc = String.valueOf(ndx-1).toCharArray();
//        //logger.info("new index = "+String.valueOf(idxc));
//        char[] dfnc = file_CurrROMSDataset.toCharArray();
//        for (int i=idxc.length;i>0;i--) {
//            dfnc[n-(4+(idxc.length-i))] = idxc[i-1];
//        }
//        return String.valueOf(dfnc);
        logger.info("in getPreviousFilename()");
        if (indx_CurrROMSDataset>0){
            indx_CurrROMSDataset--;
            logger.info("decrementing indx_CurrROMSDataset to: "+indx_CurrROMSDataset);
            logger.info("previous ROMS dataset: "+files_ROMSDatasets[indx_CurrROMSDataset]);
            return files_ROMSDatasets[indx_CurrROMSDataset];
        }
        return "";
    }

    /**
     * Method is called internally when reading the "previous" PhysicalEnvironment
     * from the netCDF file fails (because there aren't any more time steps in 
     * the file).  The method decrements the netCDF file name and attempts to 
     * get the last PhysicalEnvironment from the new file.
     * 
     * @throws java.io.IOException 
     */
    protected void tryPrevious() throws IOException {
        int n = file_CurrROMSDataset.length();
        //logger.info("file_CurrROMSDataset = "+file_CurrROMSDataset);
        String idx = file_CurrROMSDataset.substring(n-7,n-3);
        //logger.info("idx = "+idx);
        int ndx = Integer.parseInt(idx);
        char[] idxc = String.valueOf(ndx-1).toCharArray();
        //logger.info("new index = "+String.valueOf(idxc));
        char[] dfnc = file_CurrROMSDataset.toCharArray();
        for (int i=idxc.length;i>0;i--) {
            dfnc[n-(4+(idxc.length-i))] = idxc[i-1];
        }
        file_CurrROMSDataset = String.valueOf(dfnc);
        logger.info("Switching to new netCDF file: "+file_CurrROMSDataset);
        netcdfReader.setNetcdfDataset(file_CurrROMSDataset);
        int nt = netcdfReader.getNumTimeSteps();
        pe2 = new PhysicalEnvironment(nt-1,netcdfReader);
    }
  
    protected void initializeBioModel() throws FileNotFoundException, IOException {
//        LagrangianParticleTracker lpt = new LagrangianParticleTracker(i3dt);
//        lpt.setTimeStep(timeStep/ntBioModel);
//        LagrangianParticle.setTracker(lpt);
//        globalInfo.setInterpolator3D(i3dt);
        
        try {
            //create map of LHSParameters 
            Map<String,LifeStageParametersInterface> mapLSPs = null;
            String file;
            file = globalInfo.getWorkingDir()+file_Params;
            logger.info("reading parameters from '"+file+"'");
            if (file_Params.endsWith(".csv")||file_Params.endsWith(".CSV")){
                mapLSPs = LHS_Factory.createParametersFromCSV(file);
            } else
            if (file_Params.endsWith(".xml")||file_Params.endsWith(".XML")){
                mapLSPs = LHS_Factory.createParametersFromXML(file);
            }
            //assign LifeStageParameters to LifeStage types
            String[] lhsTypes = LHS_Factory.getTypeNames();
            for (int i=0; i<lhsTypes.length; i++){
                String lhsType = lhsTypes[i];
                LifeStageInterface lsi = LHS_Factory.createLHS(lhsType);
                LifeStageParametersInterface lspi = mapLSPs.get(lhsType);
                if (lspi !=null) lsi.setParameters(lspi);//lspi could be null if parameters not defined in file_Params for type
            }

            //create individuals
            LHS_Factory.resetID(1);//reset id counter in case of multiple runs
            file = globalInfo.getWorkingDir()+file_InitialAttributes;
            logger.info("reading initial attributes from '"+file+"'");
            indivs = LHS_Factory.createLHSsFromCSV(file);
            logger.info("Created "+indivs.size()+" individuals.");
            logger.info("report header: "+indivs.get(0).getReportHeader());
            //pwResults.println(indivs.get(0).getReportHeader());//write header to results file
            //pwConnResults.println(indivs.get(0).getReportHeader());//write header to results file
            LifeStageInterface lhs;
            it = indivs.listIterator();
            while (it.hasNext()){
                lhs = it.next();
                double st = lhs.getStartTime();//startTime in s if st>0; increment in days if st < 0
                if (st==0) {
                    lhs.setStartTime(startTime);//set to model start time}
                } else if (st<0) {
                    //use st as start increment in days relative to model start time
                    if (runForward) 
                        lhs.setStartTime(startTime-86400*st);
                    else
                        lhs.setStartTime(startTime+86400*st);
                }
            }
            if (guiMode) {
                it = indivs.listIterator();
                while (it.hasNext()) {
                    lhs = it.next();
                    tmpStrtPts.add(LHS_Factory.createPointFeatureNoAtts(lhs));
                }
                fcEndPoints.addAll(tmpStrtPts);
                tmpStrtPts.clear();
            }
            it = null;
            logger.info(indivs.size()+" individuals initially defined for model run.");
        } catch( InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    /**
     * Advance the biological model one time step.
     * 
     * @param dt - the time step
     */
    protected void timestepBioModel(double dt) {
//        logger.info("Time-stepping bio model! "+
//                globalInfo.getCalendar().getDateTimeString());
        Iterator<LifeStageInterface> itOutput;
        List<LifeStageInterface> tmpOutput1;
        List<LifeStageInterface> deadIndivs = new ArrayList<>();
        List<LifeStageInterface> newIndivs  = new ArrayList<>();
        List<LifeStageInterface> newIndivs1 = new ArrayList<>();
        LifeStageInterface lhs;
        LifeStageInterface lhs1;
        it = indivs.listIterator();
        try {
            while (it.hasNext()) {
                lhs  = it.next();
                if (lhs.isActive()) {
                    try {
                        if (lhs.isAlive()) {
    //                        if (debug||LagrangianParticleTracker.debug||Interpolator3D.debug)
    //                        logger.info("Stepping old lhs "+lhs.id);
                            lhs.step(dt);
                            if (lhs.isSuperIndividual()){
                                //update super individual stage transitions only when updateStageTrans is true
                                if (updateStageTrans) {
                                    tmpOutput1 = lhs.getMetamorphosedIndividuals(0.0);//don't need dt for super individuals
                                    if (!tmpOutput1.isEmpty()) newIndivs1.addAll(tmpOutput1);
                                }
                            } else {
                                //update individual stage transitions each time step
                                tmpOutput1 = lhs.getMetamorphosedIndividuals(dt);
                                if (!tmpOutput1.isEmpty()) {
                                    //flag object as having metamorphosed
                                    lhs.setActive(false);
                                    lhs.setAlive(false);
                                    //get just-created individual (only 1)
                                    itOutput = tmpOutput1.iterator();
                                    while (itOutput.hasNext()) {
                                        lhs1 = itOutput.next();//just-created individual
                                        lhs1.setActive(true);//make sure it's active
                                        //logger.info("new LHS "+lhs.getID()+" set active.");
                                        if (guiMode) tmpStrtPts.add(LHS_Factory.createPointFeatureNoAtts(lhs1));
                    //                    lhs1.setStartTime(time+dt);//new lhs regarded as created at end of time step
                    //                    lhs.step(0.0);//no dispersal, but make sure all internal var.s are set
                                        writeToReportFile(lhs1);//record initial conditions
                                        writeToConnectivityFile(lhs1);
                                        if (guiMode && updateAnimation) {
                                            tmpPts.add(LHS_Factory.createPointFeatureNoAtts(lhs1));
                                            //can't get track: length would be 0.
                                        }
                                    }
//                                    logger.info("Adding "+tmpOutput1.size()+" individuals");
                                    newIndivs.addAll(tmpOutput1);//need to add these to indivs after iteration is over
                                }
                            }
                            if (updateStageTrans) {
                                //evaluate spawning only when updateStageTrans is true
                                tmpOutput1 = lhs.getSpawnedIndividuals();
                                if (!tmpOutput1.isEmpty()) newIndivs1.addAll(tmpOutput1);
                            }
                        }
                        if (lhs.isAlive()) {
                            if (guiMode && updateAnimation) {
                                tmpPts.add(LHS_Factory.createPointFeatureNoAtts(lhs));
                                tmpTrks.add(LHS_Factory.createTrackFeature(lhs));
                            }
                            if (updateResults) writeToReportFile(lhs);
                        } else {
                            //lhs was alive but is now dead
                            //logger.info("lhs "+lhs.id+" id dead!");
                            if (guiMode&&showDeadIndivs) {
                                tmpDeadPts.add(LHS_Factory.createPointFeatureNoAtts(lhs));
                                tmpDeadTrks.add(LHS_Factory.createTrackFeature(lhs));
                            }
                            writeToReportFile(lhs);//write last report
                            writeToConnectivityFile(lhs);
                            deadIndivs.add(lhs);//add to deadIndivs collection
                        }
                    } catch (java.lang.ArrayIndexOutOfBoundsException exc) {
                        logger.info("lhs "+lhs.getID()+" exited model grid area.");
                        lhs.setActive(false);
                        lhs.setAlive(false);
                        if (guiMode&&showDeadIndivs) {
                            tmpDeadPts.add(LHS_Factory.createPointFeatureNoAtts(lhs));
                            tmpDeadTrks.add(LHS_Factory.createTrackFeature(lhs));
                        }
                        writeToReportFile(lhs);//write last report
                        writeToConnectivityFile(lhs);
                        deadIndivs.add(lhs);//add to deadIndivs collection
                    }
                } else if (!lhs.isActive()&&lhs.isAlive()) {
                    //lhs has not been "activated" yet
                    if (( runForward&&(lhs.getStartTime()<(time+dt)))||
                           (!runForward&&((lhs.getStartTime()<=0)||(lhs.getStartTime()>(time+dt))))) {
                        lhs.setActive(true);
                        //logger.info("LHS "+lhs.getID()+" set active.");
                        if (guiMode) tmpStrtPts.add(LHS_Factory.createPointFeatureNoAtts(lhs));
                        double dtp = (time+dt)-lhs.getStartTime();
                        //dtp could be > timeStep if startTime were set to (e.g.) 0.0
                        //If so, we'll set the StartTime for this gl to time and
                        //recompute the initial conditions prior to stepping the indivs.
                        if (Math.abs(dtp)>Math.abs(dt)) {
                            lhs.setStartTime(time);
                            dtp = dt;
                        }
                        writeToReportFile(lhs);//write initial conditions
                        writeToConnectivityFile(lhs);
                        lhs.step(dtp);//step the lhs to time+dt
                        if (updateResults) writeToReportFile(lhs);
                        if (guiMode && updateAnimation) {
                            tmpPts.add(LHS_Factory.createPointFeatureNoAtts(lhs));
                            tmpTrks.add(LHS_Factory.createTrackFeature(lhs));
                        }
                    }
                } else if (!lhs.isActive()&&!lhs.isAlive()) {
                    //should not get here
                    //logger.info("Something's wrong! Somebody's inactive and dead!!");
                }
            }//done with loop over indivs
            
            //remove dead indivs
            if (!deadIndivs.isEmpty()) {
//                logger.info("Removing "+deadIndivs.size()+" individuals.");
                indivs.removeAll(deadIndivs);
            }
            //add indivs after stage changes
            if (!newIndivs.isEmpty()){
                //logger.info("Adding "+newIndivs.size()+" individuals.");
                indivs.addAll(newIndivs);
            }
            
            //register newly-spawned indivs and new super indiv objects
            if (updateStageTrans) {
                if (!newIndivs1.isEmpty()) {
                    itOutput = newIndivs1.iterator();
                    while (itOutput.hasNext()) {
                        lhs = itOutput.next();
                        lhs.setActive(true);
                        //logger.info("new LHS "+lhs.getID()+" set active.");
                        if (guiMode) tmpStrtPts.add(LHS_Factory.createPointFeatureNoAtts(lhs));
                        lhs.setStartTime(time+dt);//output lhs's regarded as created at end of time step
    //                    lhs.step(0.0);//no dispersal, but make sure all internal var.s are set
                        writeToReportFile(lhs);//record initial conditions
                        writeToConnectivityFile(lhs);
                        if (guiMode && updateAnimation) {
                            tmpPts.add(LHS_Factory.createPointFeatureNoAtts(lhs));
                            //can't get track: length would be 0.
                        }
                    }
                    //logger.info("Adding "+newIndivs1.size()+" individuals");
                    indivs.addAll(newIndivs1);
                }
            }
//            logger.info("Current number of individuals: "+indivs.size());
            
            //update map
            if (guiMode && updateAnimation) {
//                logger.info("Updating animation");
                if (tmpStrtPts.size()>0) {
                    fcStartPoints.addAll(tmpStrtPts);
                    tmpStrtPts.clear();
                }
                
                fcEndPoints.clear();
                if (tmpPts.size()>0) {
                    fcEndPoints.addAll(tmpPts);
                    tmpPts.clear();
                }
                
                //fcTracks.clear();//TODO: enable thhis??
                if (tmpTrks.size()>0) {
                    fcTracks.addAll(tmpTrks);
                    tmpTrks.clear();
                }
                
                if (showDeadIndivs) {
                    if (tmpDeadPts.size()>0) {
                        fcDeadPoints.addAll(tmpDeadPts);
                        tmpDeadPts.clear();
                    }

                    if (tmpDeadTrks.size()>0) {
                        fcDeadTracks.addAll(tmpDeadTrks);
                        tmpDeadTrks.clear();
                    }
                }
                //propertySupport.firePropertyChange(PROP_CURRDATE, null, globalInfo.getCalendar().getDate());
                propertySupport.firePropertyChange(PROP_OCEANTIME, null, time+dt);//note that 'time' has not yet been advanced for the timestep
                animEventSupport.fireAnimationEvent();
            }
            it = null;
        } catch( InstantiationException | IllegalAccessException exc) {
            Exceptions.printStackTrace(exc);
        }
    }    
    
    protected void writeToConnectivityFile(LifeStageInterface lhs) {
        String className = LHS_Types.getInstance().getType(lhs.getTypeName()).getLHSClass();
        PrintWriter pwConnResults = pwConnResultsMap.get(className);
        pwConnResults.println(lhs.getReport());
    }
 
    protected void writeToConnectivityFile() {
        it = indivs.listIterator();
        LifeStageInterface lhs;
        while (it.hasNext()) {
            lhs = it.next();
            if (lhs.getAttributes().isActive()) writeToConnectivityFile(lhs);
        }
        it = null;
        Iterator<String> itr = pwConnResultsMap.keySet().iterator();
        while (itr.hasNext()) pwConnResultsMap.get(itr.next()).flush();
    }
 
    protected void writeToReportFile(LifeStageInterface lhs) {
        String className = LHS_Types.getInstance().getType(lhs.getTypeName()).getLHSClass();
        PrintWriter pwResults = pwResultsMap.get(className);
        pwResults.println(lhs.getReport());
        lhs.startTrack(lhs.getLastPosition(LifeStageInterface.COORDINATE_TYPE_GEOGRAPHIC),LifeStageInterface.COORDINATE_TYPE_GEOGRAPHIC);//restart track with current LL position as start
        lhs.startTrack(lhs.getLastPosition(LifeStageInterface.COORDINATE_TYPE_PROJECTED), LifeStageInterface.COORDINATE_TYPE_PROJECTED); //restart track with current XY position as start
    }
 
    protected void writeToReportFile() {
        it = indivs.listIterator();
        LifeStageInterface lhs;
        while (it.hasNext()) {
            lhs = it.next();
            if (lhs.getAttributes().isActive()) writeToReportFile(lhs);
        }
        it = null;
        Iterator<String> itr = pwResultsMap.keySet().iterator();
        while (itr.hasNext()) pwResultsMap.get(itr.next()).flush();
    }
    
    /**
     * Close all files associated with a map of output files
     * @param hm - map of output files
     */
    protected void closeOutputFiles(HashMap<String,PrintWriter> hm){
        Iterator<String> itr = hm.keySet().iterator();
        while (itr.hasNext())hm.get(itr.next()).close();
    }

    /**
     * Close all output files
     */
    protected void closeOutputFiles() {
        closeOutputFiles(pwResultsMap);
        closeOutputFiles(pwConnResultsMap);
    }
    
    private int mod(int x, int y) {
        int z = x-(x/y)*y;
        return z;
    }
    /**
     * Runs the environmental model for ntEnvironModel with time step of timeStep. 
     * The biological components are integrated forward with a potentially 
     * smaller time step of timeStep/ntBioModel.
     * 
     * @throws IOException
     */
    public void run() throws IOException {
        run(ntEnvironModel,timeStep,ntBioModel);
    }
    
    /**
     * Runs the environmental model for nTimes with time step of dt. 
     * The biological components are integrated forward with a potentially 
     * smaller time step of dt/nLPTsteps.
     * 
     * @param nTimes    -- number of time steps to propagate environmental model forward
     * @param dt        -- time step to use for environmental model propagation
     * @param nLPTsteps -- # of fast integration steps per environmental time step
     *                     used to propagate the biological model forward.
     * 
     * @throws IOException
     */
    public void run(int nTimes, double dt, int nLPTsteps) throws IOException {
        double systime = System.currentTimeMillis();
        double dtp = dt/nLPTsteps;
        for (int t=0;t<nTimes;t++) {//loop over environmental model timesteps
            //timestep indivs using current environment at fast timestep
            for (int j=0;j<nLPTsteps;j++) {//loop over biological model timesteps
                stpCtr++;
//                if (debug||LagrangianParticleTracker.debug||Interpolator3D.debug)
//                    logger.info("step "+stpCtr+", time = "+time+"; "+animCtr+"; "+resCtr+"; "+lhsStageTransCtr);
                updateAnimation  = (animCtr==0);
                updateResults    = (resCtr==0);
                updateStageTrans = (lhsStageTransCtr==0);
//                logger.info("animCtr = "+animCtr+" "+updateAnimation+" "+animationRate);
                timestepBioModel(dtp);
                time=time+dtp;
                globalInfo.getCalendar().setTimeOffset((long) time);
                animCtr          = mod(animCtr+1,animationRate);
                resCtr           = mod(resCtr+1,resultsRate);
                lhsStageTransCtr = mod(lhsStageTransCtr+1,lhsStageTransRate);
                statTime++;
                statMessage = "Env. model step "+t;
//                logger.info(" date is "+globalInfo.getCalendar().getDateTimeString());
//                logger.info(" doy = "+globalInfo.getCalendar().getYearDay()+
//                                   ". Is it daylight? "+DaylightFunctions.isDaylight(-163.0,55.0,globalInfo.getCalendar().getYearDay()));
            }
            if (!writeFast) writeToReportFile();
            logger.info("t = "+t+"; updated time is "+time+
                               " date is "+globalInfo.getCalendar().getDateTimeString()+
                               " constantEnv = "+fixedEnvironment);
            logger.info("Current number of individuals: "+indivs.size());
            if (!fixedEnvironment) {
                try {
                    if (runForward) timestepEnvironment();
                    else timestepEnvironmentBackwards();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                    throw(ex);
                }
            }
        }
        writeToConnectivityFile();//write final results to connectivity file
        closeOutputFiles();
        logger.info("Run time (min) = "+
                           (System.currentTimeMillis()-systime)/(60.0*1000));
    }
    
    public ModelTaskIF getInitModelTask() {
        final InitModelTask task = new InitModelTask();
        return task;
    }

    public ModelTaskIF getRunModelTask() {
        final RunModelTask task = new RunModelTask();
        return task;
    }
    
    public void addAnimationEventListener(AnimationEventListener el) {
        animEventSupport.addAnimationEventListener(el);
    }

    public void removeAnimationEventListener(AnimationEventListener el) {
        animEventSupport.removeAnimationEventListener(el);
    }
    
    private class InitModelTask implements ModelTaskIF {
        
        private boolean done=false;
        private boolean paused=false;
        private boolean canceled=false;
        
        private InitModelTask(){
        }
        
        @Override
        public void go() {
            final SwingWorker worker = new SwingWorker() {
                @Override
                public Object construct() {
                    done=false;
                    canceled=false;
                    statMessage="Initializing model...";
                    return new ActualInitModelTask();
                }
            };
           worker.start();
        }
        
        @Override
        public void stop() {
            canceled = true;
            statMessage = null;
        }
        
        @Override
        public void pause() {            
        }
        
        @Override
        public boolean isDone() {
            return done;
        }
        
        @Override
        public int getCurrentValue() {
            return statTime;
        }
        
        @Override
        public String getMessage() {
            return statMessage;
        }
        
        private class ActualInitModelTask {
            private ActualInitModelTask() {
                try {
                    initialize();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                    done = true;
                }
                done = true;
            }
        }
    }
    
    private class RunModelTask implements ModelTaskIF {
        
        private boolean done=false;
        private boolean paused=false;
        private boolean canceled=false;
        
        private RunModelTask(){
        }
        
        @Override
        public void go() {
            final SwingWorker worker = new SwingWorker() {
                @Override
                public Object construct() {
                    done=false;
                    canceled=false;
                    statMessage="Running model...";
                    return new ActualRunModelTask();
                }
            };
           worker.start();
        }
        
        @Override
        public void stop() {
            canceled = true;
            statMessage = null;
        }
        
        @Override
        public void pause() {            
        }
        
        @Override
        public boolean isDone() {
            return done;
        }
        
        @Override
        public int getCurrentValue() {
            return statTime;
        }
        
        @Override
        public String getMessage() {
            return statMessage;
        }
        
        private class ActualRunModelTask {
            private ActualRunModelTask() {
                try{
                    run(ntEnvironModel,timeStep,ntBioModel);                    
                }  catch (Exception ex){
                   Exceptions.printStackTrace(ex);
                   done = true;
                 }
                done = true;
            }
        }
    }
    
}
