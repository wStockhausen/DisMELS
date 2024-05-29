/*
 *SimpleBenthicAdultLHS.java
 */

package wts.models.DisMELS.LHS.BenthicAdult;

import com.vividsolutions.jts.geom.Coordinate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import wts.models.DisMELS.LHS.BenthicJuvenile.SimpleBenthicJuvenileLHSAttributes;
import wts.models.DisMELS.LHS.PelagicStages.SimplePelagicLHSAttributes;
import wts.models.DisMELS.LHS.SimpleLHSs.AbstractSimpleLHS;
import wts.models.DisMELS.framework.*;
import wts.models.utilities.CalendarIF;
import wts.models.utilities.DateTimeFunctions;
import wts.roms.model.LagrangianParticle;


/**
 * DisMELS class representing benthic adults in the 
 * "simple" life stages (e.g., flatfish).
 * 
 * @author William Stockhausen
 */
@ServiceProvider(service=LifeStageInterface.class)
public class SimpleBenthicAdultLHS extends AbstractSimpleLHS {
    
        //Static fields    
            //  Static fields new to this class
    /* flag to do debug operations */
    public static boolean debug = false;
    /* Class for attributes */
    public static final String attributesClass = 
            "wts.models.DisMELS.LHS.BenthicAdult.SimpleBenthicAdultLHSAttributes";
    /* Class for parameters */
    public static final String parametersClass = 
            "wts.models.DisMELS.LHS.BenthicAdult.SimpleBenthicAdultLHSParameters";
    /* Class for feature type for point positions */
    public static final String pointFTClass = wts.models.DisMELS.framework.LHSPointFeatureType.class.getName();
//            "wts.models.DisMELS.LHS.BenthicAdult.SimpleBenthicAdultLHSPointFT";
    /* Classes for next LHS */
    public static final String[] nextLHSClasses = new String[]{ 
            "wts.models.DisMELS.LHS.BenthicAdult.SimpleBenthicAdultLHS"};
    /* Classes for spawned LHS */
    public static final String[] spawnedLHSClasses = new String[]{ 
            "wts.models.DisMELS.LHS.PelagicStages.SimplePelagicLHS"};
    /** a logger for messages */
    private static final Logger logger = Logger.getLogger(SimpleBenthicAdultLHS.class.getName());
    
        //Instance fields
            //  Fields hiding ones from superclass
    /* lhs attributes */
    protected SimpleBenthicAdultLHSAttributes atts = null;
    /* lhs parameters */
    protected SimpleBenthicAdultLHSParameters params = null;
            //  Fields new to class
            //fields that reflect parameter values
    protected boolean isSuperIndividual;
    protected double horizDiffusion;
    protected double minDepth;
    protected double maxDepth;
    protected double minStageDuration;
    protected double maxStageDuration;
    protected double minStageSize;
    protected double stageTransRate;
    protected double growthRate;
    protected double mortalityRate;
    protected double firstDayOfSpawning;
    protected double lengthOfSpawningSeason;
    protected double fecundity;
    protected boolean isBatchSpawner;
    protected double recoveryPeriod;
    protected double batchPeriod;
    protected boolean useRandomGrowth;
    protected boolean useRandomMortality;
    protected boolean useRandomTransitions;
    protected boolean useRandomSpawning;
            //other fields
    /** number of individuals transitioning to next stage */
    private double numTrans;  
     /** day of year */
    private double dayOfYear;
    /** spawning season flag */
    private boolean isSpawningSeason;
    private boolean doOnceAfterSpawningSeason = true;
    private double elapsedTimeToSpawn;
   
    /**
     * This constructor is provided only to facilitate the ServiceProvider functionality.
     * DO NOT USE IT!!
     * This creates a new instance of SimpleBenthicAdultLHS WITH type name="NULL".
     */
    public SimpleBenthicAdultLHS() {
        super("");
    }
    
    /**
     * Creates a new LHS instance with parameters based on the typeName and
     * "default" attributes.
     */
    public SimpleBenthicAdultLHS(String typeName) 
                throws InstantiationException, IllegalAccessException {
        super(typeName);
        atts   = new SimpleBenthicAdultLHSAttributes(typeName);
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_id,id);
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_parentID,id);
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_origID,id);
        params = (SimpleBenthicAdultLHSParameters) LHS_Factory.createParameters(typeName);
        setAttributesFromSubClass(atts);  //set object in the superclass
        setParametersFromSubClass(params);//set object in the superclass
        setParameterValues();//copy the parameter values to local variables
    }

    /**
     * Creates a new instance of LHS with type name and
     * attribute values given by input String array.
     * 
     * Side effects:
     *  1. Calls createInstance(LifeStageAttributesInterface), with associated effects,
     *  based on creating an attributes instance from the string array.
     * /
     * @param strv - attributes as string array
     * @return - instance of LHS
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    @Override
    public SimpleBenthicAdultLHS createInstance(String[] strv) 
                        throws InstantiationException, IllegalAccessException {
        LifeStageAttributesInterface theAtts = LHS_Factory.createAttributes(strv);
        SimpleBenthicAdultLHS lhs = createInstance(theAtts);
        return lhs;
    }

    /**
     * Creates a new instance of this LHS with attributes (including type name) 
     * corresponding to the input attributes instance.
     * 
     * Side effects:
     *  1. If theAtts id attribute is "-1", then a new (unique) id value is created 
     *  for the new LHS instance.
     *  2. If theAtts parentID attribute is "-1", then it is set to the value for id.
     *  3. If theAtts origID attribute is "-1", then it is set to the value for id.
     *  4. initialize() is called to initialize variables and convert position
     *   attributes.
     * /
     * @param theAtts - attributes instance
     * @return - instance of LHS
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    @Override
    public SimpleBenthicAdultLHS createInstance(LifeStageAttributesInterface theAtts)
                        throws InstantiationException, IllegalAccessException {
        SimpleBenthicAdultLHS lhs = null;
        if (theAtts instanceof SimpleBenthicAdultLHSAttributes) {
            lhs = new SimpleBenthicAdultLHS(theAtts.getTypeName());
            long newID = lhs.id;//save id of new instance
            lhs.setAttributes(theAtts);
            if (lhs.atts.getID()==-1) {
                //constructing new individual, so reset id values to those of new
                lhs.id = newID;
                lhs.atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_id,newID);
            }
            newID = (Long) lhs.atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_parentID);
            if (newID==-1) {
                lhs.atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_parentID,newID);
            }
            newID = (Long) lhs.atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_origID);
            if (newID==-1) {
                lhs.atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_origID,newID);
            }
        }
        lhs.initialize();//initialize instance variables
        return lhs;
    }

    /**
     *  Returns the associated attributes.  
     */
    @Override
    public SimpleBenthicAdultLHSAttributes getAttributes() {
        return atts;
    }

    /**
     * Sets the values of the associated attributes object to those in the input
     * String[]. This does NOT change the typeNameof the LHS instance (or the 
     * associated LHSAttributes instance) on which the method is called.
     * Attribute values are set using SimpleBenthicLHSAttributes.setValues(String[]).
     * Side effects:
     *  1. If th new id attribute is not "-1", then its value for id replaces the 
     *      current value for the lhs.
     *  2. If the new parentID attribute is "-1", then it is set to the value for id.
     *  3. If the new origID attribute is "-1", then it is set to the value for id.
     *  4. initialize() is called to initialize variables and convert position
     *   attributes.
     * /
     * @param strv - attribute values as String[]
     */
    @Override
    public void setAttributes(String[] strv) {
        long aid;
        atts.setValues(strv);
        aid = atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_id, id);
        if (aid==-1) {
            //change atts id to lhs id
            atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_id, id);
        } else {
            //change lhs id to atts id
            id = aid;
        }
        aid = atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_parentID, id);
        if (aid==-1) {
            atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_parentID, id);
        }
        aid = atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_origID, id);
        if (aid==-1) {
            atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_origID, id);
        }
        initialize();//initialize instance variables
    }

    /**
     * Sets the attributes for the instance by copying values from the input.
     * This does NOT change the typeNameof the LHS instance (or the associated 
     * LHSAttributes instance) on which the method is called.
     * Note that ALL attributes are copied, so id, parentID, and origID are copied
     * as well. 
     *  As a side effect, updateVariables() is called to update instance variables.
     *  LHS instance variable "id" is changed to the id value of newAtts.
     * @param newAtts - should be instance of SimplePelagicLHSAttributes or
     *                  SimpleBenthicLHSAttributes.
     */
    @Override
    public void setAttributes(LifeStageAttributesInterface newAtts) {
        if (newAtts instanceof SimpleBenthicAdultLHSAttributes) {
            super.setAttributes(newAtts);
        } else if (newAtts instanceof SimpleBenthicJuvenileLHSAttributes) {
            super.setAttributes(newAtts);
        } else {
            //TODO: should throw an error here
            logger.info("SimpleBenthicAdultLHS.setAttributes(): no match for attributes type");
        }
    }
    
    /**
     *  Sets the associated attributes object. Use this after creating an LHS instance
     * as an "output" from another LHS that is functioning as an ordinary individual.
     */
    @Override
    public void setInfoFromIndividual(LifeStageInterface oldLHS){
        /** 
         * Since this is a single individual making a transition, we need to:
         *  1) copy the attributes from the old LHS (id's should remain as for old LHS)
         *  2) set age in stage = 0
         *  3) set active and alive to true
         *  4) set attached to true
         *  5) copy the Lagrangian Particle from the old LHS
         *  6) start a new track from the current position for the oldLHS
         *  7) update local variables
         */
        LifeStageAttributesInterface oldAtts = oldLHS.getAttributes();            
        setAttributes(oldAtts);
        
        //reset some attributes
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_ageInStage, 0.0);//reset age in stage
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_active,true);    //set active to true
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_alive,true);     //set alive to true
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_attached,true);  //set attached to true
        id = atts.getID(); //reset id for current LHS to one from old LHS

        //copy LagrangianParticle information
        this.setLagrangianParticle(oldLHS.getLagrangianParticle());
        //start track at last position of oldLHS track
        this.startTrack(oldLHS.getLastPosition(COORDINATE_TYPE_PROJECTED),COORDINATE_TYPE_PROJECTED);
        this.startTrack(oldLHS.getLastPosition(COORDINATE_TYPE_GEOGRAPHIC),COORDINATE_TYPE_GEOGRAPHIC);
        //update local variables to capture changes made here
        updateVariables();
    }
    
    /**
     *  Sets the associated attributes object. Use this after creating an LHS instance
     * as an "output" from another LHS that is functioning as a super individual.
     */
    @Override
    public void setInfoFromSuperIndividual(LifeStageInterface oldLHS, double numTrans) {
        /** 
         * Since the old LHS instance is a super individual, only a part 
         * (numTrans) of it transitioned to the current LHS. Thus, we need to:
         *          1) copy most attribute values from old stage
         *          2) make sure id for this LHS is retained, not changed
         *          3) assign old LHS id to this LHS as parentID
         *          4) copy old LHS origID to this LHS origID
         *          5) set number in this LHS to numTrans
         *          6) reset age in stage to 0
         *          7) set active and alive to true
         *          8) set attached to true
         *          9) copy the Lagrangian Particle from the old LHS
         *         10) start a new track from the current position for the oldLHS
         *         11) update local variables to match attributes
         */
        //copy some variables that should not change
        long idc = id;
        
        //copy the old attribute values
        LifeStageAttributesInterface oldAtts = oldLHS.getAttributes();            
        setAttributes(oldAtts);
        
        //reset some attributes and variables
        id = idc;
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_id,idc);//reset id to one for current LHS
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_parentID,
                oldAtts.getValue(LifeStageAttributesInterface.PROP_id));//copy old id to parentID
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_number, numTrans);//set number to numTrans
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_ageInStage, 0.0); //reset age in stage
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_active,true);     //set active to true
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_alive,true);      //set alive to true
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_attached,true);   //set attached to true
            
        //copy LagrangianParticle information
        this.setLagrangianParticle(oldLHS.getLagrangianParticle());
        //start track at last position of oldLHS track
        this.startTrack(oldLHS.getLastPosition(COORDINATE_TYPE_PROJECTED),COORDINATE_TYPE_PROJECTED);
        this.startTrack(oldLHS.getLastPosition(COORDINATE_TYPE_GEOGRAPHIC),COORDINATE_TYPE_GEOGRAPHIC);
        //update local variables to capture changes made here
        updateVariables();
    }

    /**
     *  Returns the associated parameters.  
     */
    @Override
    public SimpleBenthicAdultLHSParameters getParameters() {
        return params;
    }

    /**
     * Sets the parameters for the instance to a cloned version of the input.
     * @param newParams - should be instance of SimpleBenthicAdultLHSParameters
     */
    @Override
    public void setParameters(LifeStageParametersInterface newParams) {
        if (newParams instanceof SimpleBenthicAdultLHSParameters) {
            try {
                params = (SimpleBenthicAdultLHSParameters) newParams.clone();
                setParametersFromSubClass(params);
                setParameterValues();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(SimpleBenthicAdultLHS.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //throw an error
        }
    }
    
    /*
     * Copy the values from the params map to the param variables.
     */
    private void setParameterValues() {
        isSuperIndividual = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_isSuperIndividual,true);
        horizDiffusion = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_horizDiffusion,horizDiffusion);
        minDepth = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_minDepth,minDepth);
        maxDepth = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_maxDepth,maxDepth);
        minStageDuration = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_minStageDuration,minStageDuration);
        maxStageDuration = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_maxStageDuration,maxStageDuration);
        minStageSize = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_minStageSize,minStageSize);
        stageTransRate = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_stageTransRate,stageTransRate);
        growthRate = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_growthRate,growthRate);
        mortalityRate = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_mortalityRate,mortalityRate);
        useRandomGrowth = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_useRandomGrowth,true);
        useRandomMortality = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_useRandomMortality,true);
        useRandomTransitions = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_useRandomTransitions,true);
        firstDayOfSpawning = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_firstDaySpawning,firstDayOfSpawning);
        lengthOfSpawningSeason = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_lengthSpawningSeason,lengthOfSpawningSeason);
        fecundity = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_fecundity,fecundity);
        isBatchSpawner = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_isBatchSpawner,true);
        recoveryPeriod = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_recoveryPeriod,recoveryPeriod);
        batchPeriod = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_batchPeriod,batchPeriod);
        useRandomSpawning = 
                params.getValue(SimpleBenthicAdultLHSParameters.PROP_useRandomSpawning,true);
    }
    
    /**
     *  Provides a copy of the object.  The attributes and parameters
     *  are cloned in the process, so the clone is independent of the
     *  original.
     */
    @Override
    public Object clone() {
        SimpleBenthicAdultLHS clone = null;
        try {
            clone       = (SimpleBenthicAdultLHS) super.clone();
            clone.setAttributes((SimpleBenthicAdultLHSAttributes) atts.clone());
            clone.setParameters((SimpleBenthicAdultLHSParameters) params.clone());
            clone.lp    = (LagrangianParticle) lp.clone();
            clone.track   = (ArrayList<Coordinate>) track.clone();
            clone.trackLL = (ArrayList<Coordinate>) trackLL.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return clone;        
    }

    @Override
    public List<LifeStageInterface> getMetamorphosedIndividuals(double dt) {
        double dtp = 0.25*(dt/DAY_SECS);//use 1/4 timestep (converted from sec to d)
        output.clear();
        List<LifeStageInterface> nLHSs = null;
        if ((ageInStage+dtp>=minStageDuration)&&(size>=minStageSize)) {
            if ((numTrans>0)||!isSuperIndividual){
                nLHSs = createNextLHSs();
                if (nLHSs!=null) output.addAll(nLHSs);
            }
        }
        return output;
    }

    private List<LifeStageInterface> createNextLHSs() {
        List<LifeStageInterface> nLHSs = null;
        try {
            //create LHS with "output" stage
            if (isSuperIndividual) {
                /** 
                 * Since this LHS instance is a super individual, only a part 
                 * (numTrans) of it transitions to the next LHS. Thus, we need to:
                 *          1) create new LHS instance
                 *          2. assign new id to new instance
                 *          3) assign current LHS id to new LHS as parentID
                 *          4) copy current LHS origID to new LHS origID
                 *          5) set number in new LHS to numTrans for current LHS
                 *          6) reset numTrans in current LHS
                 */
                nLHSs = LHS_Factory.createNextLHSsFromSuperIndividual(typeName,this,numTrans);
                numTrans = 0.0;//reset numTrans to zero
            } else {
                /** 
                 * Since this is a single individual making a transition, we should
                 * "kill" the current LHS.  Also, the various IDs should remain
                 * the same in the new LHS since it's the same individual. Thus, 
                 * we need to:
                 *          1) create new LHS instance
                 *          2. assign current LHS id to new LHS id
                 *          3) assign current LHS parentID to new LHS parentID
                 *          4) copy current LHS origID to new LHS origID
                 *          5) kill current LHS
                 */
                nLHSs = LHS_Factory.createNextLHSsFromIndividual(typeName,this);
                alive  = false; //allow only 1 transition, so kill this stage
                active = false; //set stage inactive, also
            }
        } catch (IllegalAccessException | InstantiationException ex) {
            ex.printStackTrace();
        }
        return nLHSs;
    }

    @Override
    public List<LifeStageInterface> getSpawnedIndividuals() {
        output.clear();
        //logger.info("Adult "+id+": "+isSpawningSeason+", "+elapsedTimeToSpawn);
        if (isSpawningSeason && (elapsedTimeToSpawn<0)) doSpawning();
        return output;
    }
    
    private void doSpawning() {
        try {
            //create number of new individuals = fecundity
            //logger.info("Adult"+id+" spawning: fecundity = "+fecundity);
            LifeStageInterface nLHS = null;
            LifeStageAttributesInterface newAttsI = null;
            for (int i=0;i<fecundity;i++) {
                /** 
                 * For each individual, we need to:
                 *          1) create new LHS instance.
                 *          2. assign new id to new instance (gets done automatically).
                 *          3) assign current LHS id to new LHS as parentID
                 *          4) assign current LHS id to new LHS origID
                 *          5) set number in new LHS to 1.TODO: change this to a variable.
                 *          6) set age and ageInStage to 0 in new instance.
                 *          7) copy other attributes.
                 */
                nLHS = LHS_Factory.createSpawnedLHS(typeName);
                newAttsI = nLHS.getAttributes();
                if (newAttsI instanceof SimplePelagicLHSAttributes) {
                    SimplePelagicLHSAttributes newAtts = (SimplePelagicLHSAttributes) newAttsI;
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_active,     true);
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_alive,      true);
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_attached,   true);
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_age,        0.0);
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_ageInStage, 0.0);
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_gridCellID, 
                            atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_gridCellID));
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_horizPos1,  
                            atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_horizPos1));
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_horizPos2,  
                            atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_horizPos2));
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_horizType,  
                            atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_horizType));
                    //newAtts.setValue(newAtts.PROP_id,         -1);<-don't need to update this
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_parentID,   
                            atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_id));
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_origID,     
                            atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_id));
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_number,     1.0);//TODO:change this to fecundity/numSpawnPerIndiv
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_salinity,   
                            atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_salinity));
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_size,       0.0);//TODO: change this!!
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_startTime,  time);
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_temp,       
                            atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_temp));
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_time,       
                            atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_time));
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_vertPos,    
                            atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_vertPos));
                    newAtts.setValue(SimplePelagicLHSAttributes.PROP_vertType,   
                            atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_vertType));
                    //copy LagrangianParticle information
                    nLHS.setLagrangianParticle(lp);
                    //start track at last position of oldLHS track
                    nLHS.startTrack(track.get(track.size()-1),COORDINATE_TYPE_PROJECTED);
                    nLHS.startTrack(trackLL.get(trackLL.size()-1),COORDINATE_TYPE_GEOGRAPHIC);
                    //update local variables to capture changes made here
                    nLHS.setAttributes(newAtts);
                } else {
                    //should throw error
                    logger.info("SimpleBenthicAdultLHS.doSpawning(): no match for attributes type:"+newAttsI.toString());
                }
                output.add(nLHS);
                nLHS = null;
            }
            //reset elapsed time to spawn for next spawning
            if (isBatchSpawner) {
                if (useRandomSpawning) {
                    elapsedTimeToSpawn = recoveryPeriod+rng.computeUniformVariate(0.0, batchPeriod);
                } else {
                    elapsedTimeToSpawn = recoveryPeriod+batchPeriod;
                }
                elapsedTimeToSpawn = elapsedTimeToSpawn*DAY_SECS;//change from days to secs
            } else {
                //spawning only occurs once per season, so set to infinity
                elapsedTimeToSpawn = Double.POSITIVE_INFINITY;
            }
        } catch (IllegalAccessException | InstantiationException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Initializes time-dependent and time-independent variables
     * @param time
     */
    private void initializeTimedependentVariables() {
        //temporarily set calendar time to variable time
        CalendarIF cal = GlobalInfo.getInstance().getCalendar();
        long modTime = cal.getTimeOffset();
        cal.setTimeOffset((long) time);
        dayOfYear = cal.getYearDay();
        
        //set up spawning
        isSpawningSeason = DateTimeFunctions.isBetweenDOY(dayOfYear,
                                                          firstDayOfSpawning,
                                                          firstDayOfSpawning+lengthOfSpawningSeason);
        if (!isSpawningSeason) {
            //we're starting outside the spawning season, set up for next one
            setupSpawningSeason();
        } else {
            //we're starting in the middle of a spawning season
            doOnceAfterSpawningSeason = true;
            if (isBatchSpawner) {
                if (useRandomSpawning) {
                    elapsedTimeToSpawn = recoveryPeriod+rng.computeUniformVariate(0.0, batchPeriod);
                } else {
                    elapsedTimeToSpawn = recoveryPeriod;
                }
            } else {
                if (useRandomSpawning) {
                    elapsedTimeToSpawn = rng.computeUniformVariate(0.0, lengthOfSpawningSeason-(dayOfYear-firstDayOfSpawning));
                } else {
                    elapsedTimeToSpawn = (lengthOfSpawningSeason-(dayOfYear-firstDayOfSpawning))/2.0;
                }
            }
        }
        
        //reset calendar time to model time
        cal.setTimeOffset(modTime);
    }

    public void initialize() {
//        atts.setValue(atts.PROP_id,id);
        updateVariables();//set instance variables to attribute values
        int hType,vType;
        hType=vType=-1;
        double xPos, yPos, zPos;
        xPos=yPos=zPos=0;
        hType      = atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_horizType,hType);
        vType      = atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_vertType,vType);
        xPos       = atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_horizPos1,xPos);
        yPos       = atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_horizPos2,yPos);
        zPos       = atts.getValue(SimpleBenthicAdultLHSAttributes.PROP_vertPos,zPos);
        time       = startTime;
        numTrans   = 0.0; //set numTrans to zero
        if (debug) logger.info(hType+cc+vType+cc+startTime+cc+xPos+cc+yPos+cc+zPos);
        if (i3d!=null) {
            double[] IJ = new double[] {xPos,yPos};
            if (hType==Types.HORIZ_XY) {
                IJ = i3d.getGrid().computeIJfromXY(xPos,yPos);
            } else if (hType==Types.HORIZ_LL) {
//                if (xPos<0) xPos=xPos+360;
                IJ = i3d.getGrid().computeIJfromLL(yPos,xPos);
            }
            double K = 0;  //benthic adult starts out on bottom
            double z = i3d.interpolateBathymetricDepth(IJ);
            if (debug) logger.info("Bathymetric depth = "+z);
            lp.setIJK(IJ[0],IJ[1],K);
            //reset track array
            track.clear();
            trackLL.clear();
            //set horizType to lat/lon and vertType to depth
            atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_horizType,Types.HORIZ_LL);
            atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_vertType,Types.VERT_H);
            //interpolate initial position and environmental variables
            double[] pos = lp.getIJK();
            updatePosition(pos);
            interpolateEnvVars(pos);
            updateAttributes(); 
            initializeTimedependentVariables();
        }
        updateVariables();//set instance variables to attribute values
    }
    
    /**
     * Set up variables for start of next spawning season.
     */
    private void setupSpawningSeason() {
        doOnceAfterSpawningSeason = false;
        //Set up time of first spawning
        if (isBatchSpawner) {
            if (useRandomSpawning) {
                elapsedTimeToSpawn = rng.computeUniformVariate(0.0, batchPeriod)*DAY_SECS;
            } else {
                elapsedTimeToSpawn = 0.0;
            }
        } else {
            if (useRandomSpawning) {
                elapsedTimeToSpawn = rng.computeUniformVariate(0.0, lengthOfSpawningSeason)*DAY_SECS;
            } else {
                elapsedTimeToSpawn = (lengthOfSpawningSeason/2.0)*DAY_SECS;
            }
        }
    }
    
    @Override
    public void step(double dt) throws ArrayIndexOutOfBoundsException {
        //determine daytime/nighttime for vertical migration & calc indiv. W
        CalendarIF cal = GlobalInfo.getInstance().getCalendar();
        dayOfYear = cal.getYearDay();
        isSpawningSeason = DateTimeFunctions.isBetweenDOY(dayOfYear,firstDayOfSpawning,firstDayOfSpawning+lengthOfSpawningSeason);
        if (isSpawningSeason) {
            elapsedTimeToSpawn = elapsedTimeToSpawn-dt;
            doOnceAfterSpawningSeason = true;
        } else {
            if (doOnceAfterSpawningSeason) {
                setupSpawningSeason();
            }
        }
        //TODO: implement movement here
        double[] pos;
            double[] uv = calcUV(dt);
            lp.setU(uv[0],lp.getN());
            lp.setV(uv[1],lp.getN());
            lp.doPredictorStep();
            //assume same daytime status, but recalc depth and revise W 
//            pos = lp.getPredictedIJK();
//            depth = -i3d.calcZfromK(pos[0],pos[1],pos[2]);
//            if (debug) logger.info("Depth after predictor step = "+depth);
            //w = calcW(dt,lp.getNP1())+r; //set swimming rate for predicted position
            lp.setU(uv[0],lp.getNP1());
            lp.setV(uv[1],lp.getNP1());
            //now do corrector step
            lp.doCorrectorStep();
            pos = lp.getIJK();
        time = time+dt;
        updateSize(dt);
        updateNum(dt);
        updateAge(dt);
        updatePosition(pos);
        interpolateEnvVars(pos);
        //check for exiting grid
        if (i3d.isAtGridEdge(pos,tolGridEdge)){
            alive=false;
            active=false;
            gridCellID=i3d.getGridCellID(pos, tolGridEdge);
            logger.info("Indiv "+id+" exited grid at ["+pos[0]+","+pos[1]+"]: "+gridCellID);
        }
        updateAttributes(); //update the attributes object w/ nmodified values
    }
    
    /**
     * Function to calculate horizontal swimming speeds.
     * 
     * @param dt - time step
     * @return   - double[]{u,v}
     */
    public double[] calcUV(double dt) {
        double[] uv = {0.0,0.0};
        if (horizDiffusion>0) {
            double r = Math.sqrt(horizDiffusion/Math.abs(dt));
            uv[0] += r*rng.computeNormalVariate(); //stochastic swimming rate
            uv[1] += r*rng.computeNormalVariate(); //stochastic swimming rate
            if (debug) System.out.print("uv: "+r+"; "+uv[0]+", "+uv[1]+"\n");
        }
        uv[0] = Math.signum(dt)*uv[0];
        uv[1] = Math.signum(dt)*uv[1];
        return uv;
    }
    
    private void updateAge(double dt) {
        age        = age+dt/DAY_SECS;
        ageInStage = ageInStage+dt/DAY_SECS;
        if (ageInStage>maxStageDuration) {
            alive = false;
            active = false;
        }
    }

    private void updateSize(double dt) {
        size = size+dt*growthRate/DAY_SECS;
    }

    /**
     *
     * @param dt - time step in seconds
     */
    private void updateNum(double dt) {
        double totRate = mortalityRate;
        if ((ageInStage>=minStageDuration)&&(size>=minStageSize)) {
            totRate += stageTransRate;
            //apply mortality rate to previous number transitioning and
            //add in new transitioners
            numTrans = numTrans*Math.exp(-dt*mortalityRate/DAY_SECS)+
                    (stageTransRate/totRate)*number*(1-Math.exp(-dt*totRate/DAY_SECS));
        }
        number = number*Math.exp(-dt*totRate/DAY_SECS);
    }

    private void updatePosition(double[] pos) {
        bathym = i3d.interpolateBathymetricDepth(pos);
        depth = -i3d.calcZfromK(pos[0],pos[1],pos[2]);
        lat   = i3d.interpolateLat(pos);
        lon   = i3d.interpolateLon(pos);
        gridCellID = ""+Math.round(pos[0])+"_"+Math.round(pos[1]);
        updateTrack();
    }
    
    private void interpolateEnvVars(double[] pos) {
        temperature = i3d.interpolateTemperature(pos);
        salinity    = i3d.interpolateSalinity(pos);
    }

    @Override
    public double getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(double newTime) {
        startTime = newTime;
        time      = startTime;
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_startTime,startTime);
        atts.setValue(SimpleBenthicAdultLHSAttributes.PROP_time,time);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean b) {
        active = b;
        atts.setActive(b);
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setAlive(boolean b) {
        alive = b;
        atts.setAlive(b);
    }

    @Override
    public String getAttributesClassName() {
        return attributesClass;
    }

    @Override
    public String getParametersClassName() {
        return parametersClass;
    }

    @Override
    public String[] getNextLHSClassNames() {
        return nextLHSClasses;
    }

    @Override
    public String getPointFeatureTypeClassName() {
        return pointFTClass;
    }

    @Override
    public String[] getSpawnedLHSClassNames() {
        return spawnedLHSClasses;
    }

    @Override
    public boolean isSuperIndividual() {
        return isSuperIndividual;
    }

}
