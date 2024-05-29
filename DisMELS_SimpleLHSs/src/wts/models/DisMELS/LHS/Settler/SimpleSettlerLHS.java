/*
 * SimpleSettlerLHS.java
 *
 * Created on January 24, 2006, 11:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.LHS.Settler;


import com.vividsolutions.jts.geom.Coordinate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import wts.models.DisMELS.LHS.PelagicStages.SimplePelagicLHSAttributes;
import wts.models.DisMELS.LHS.SimpleLHSs.AbstractSimpleLHS;
import wts.models.DisMELS.framework.*;
import wts.models.utilities.DateTimeFunctions;
import wts.roms.model.LagrangianParticle;

/**
 *
 * @author William Stockhausen
 */
@ServiceProvider(service=LifeStageInterface.class)
public class SimpleSettlerLHS extends AbstractSimpleLHS {
    
        //Static fields    
            //  Static fields new to this class
    /* flag to do debug operations */
    public static boolean debug = false;
    /* Class for attributes */
    public static final String attributesClass = 
            "wts.models.DisMELS.LHS.Settler.SimpleSettlerLHSAttributes";
    /* Class for parameters */
    public static final String parametersClass = 
            "wts.models.DisMELS.LHS.Settler.SimpleSettlerLHSParameters";
    /* Class for feature type for point positions */
    public static final String pointFTClass = wts.models.DisMELS.framework.LHSPointFeatureType.class.getName();
//            "wts.models.DisMELS.LHS.Settler.SimpleSettlerLHSPointFT";
    /* Classes for next LHS */
    public static final String[] nextLHSClasses = new String[]{ 
            "wts.models.DisMELS.LHS.BenthicJuvenile.SimpleBenthicJuvenileLHS"};
    /* Classes for spawned LHS */
    public static final String[] spawnedLHSClasses = new String[]{};
    /** a logger for messages */
    private static final Logger logger = Logger.getLogger(SimpleSettlerLHS.class.getName());
    
        //Instance fields
            //  Fields hiding ones from superclass
    /* attributes */
    protected SimpleSettlerLHSAttributes atts = null;
    /* parameters */
    protected SimpleSettlerLHSParameters params = null;
            //  Fields new to class
            //fields that reflect parameter values
    protected boolean isSuperIndividual;
    protected boolean hasPreferredDepthDay;
    protected boolean hasPreferredDepthNight;
    protected boolean willAttachDay;
    protected boolean willAttachNight;
    protected double minDepthDay;
    protected double maxDepthDay;
    protected double minDepthNight;
    protected double maxDepthNight;
    protected double vertSwimmingSpeed;
    protected double vertDiffusion;
    protected double horizDiffusion;
    protected double maxStageDuration;
    protected double minSettlementDepth;
    protected double maxSettlementDepth;
    protected double stageTransRate;
    protected double growthRate;
    protected double mortalityRate;
    protected boolean useRandomGrowth;
    protected boolean useRandomMortality;
    protected boolean useRandomTransitions;
            //other fields
    /** flag for daytime(true)/nighttime(false) */
    private boolean isDaytime;
    /** number of individuals transitioning to next stage */
    private double numTrans;  
    
    /**
     * This constructor is provided only to facilitate the ServiceProvider functionality.
     * DO NOT USE IT!!
     * This creates a new instance of SimpleSettlerLHS WITH type name="NULL".
     */
    public SimpleSettlerLHS() {
        super("NULL");
    }
    
    /**
     * Creates a new instance of SimplePelagicLHS with the given typeName.
     * A new id number is calculated in the superclass and assigned to
     * the new instance's id, parentID, and origID. 
     */
    public SimpleSettlerLHS(String typeName) 
                throws InstantiationException, IllegalAccessException {
        super(typeName);
        atts   = new SimpleSettlerLHSAttributes(typeName);
        atts.setValue(SimpleSettlerLHSAttributes.PROP_id,id);
        atts.setValue(SimpleSettlerLHSAttributes.PROP_parentID,id);
        atts.setValue(SimpleSettlerLHSAttributes.PROP_origID,id);
        atts.setValue(SimpleSettlerLHSAttributes.PROP_origID,id);
        params = (SimpleSettlerLHSParameters) LHS_Factory.createParameters(typeName);
        setAttributesFromSubClass(atts);  //set object in the superclass
        setParametersFromSubClass(params);//set object in the superclass
        setParameterValues();//copy the parameter values to local variables
    }

    /**
     * Creates a new instance of LHS with type name and
     * attribute values given by input String array.
     * 
     * Side effects:
     *  1. Calls createInstance(AbstractLHSAttributes), with associated effects,
     *  based on creating an attributes instance from the string array.
     * /
     * @param strv - attributes as string array
     * @return - instance of LHS
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    @Override
    public SimpleSettlerLHS createInstance(String[] strv) 
                        throws InstantiationException, IllegalAccessException {
        LifeStageAttributesInterface theAtts = LHS_Factory.createAttributes(strv);
        SimpleSettlerLHS lhs = createInstance(theAtts);
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
    public SimpleSettlerLHS createInstance(LifeStageAttributesInterface theAtts)
                        throws InstantiationException, IllegalAccessException {
        SimpleSettlerLHS lhs = null;
        if (theAtts instanceof SimpleSettlerLHSAttributes) {
            lhs = new SimpleSettlerLHS(theAtts.getTypeName());
            long newID = lhs.id;//save id of new instance
            lhs.setAttributes(theAtts);
            if (lhs.atts.getID()==-1) {
                //constructing new individual, so reset id values to those of new
                lhs.id = newID;
                lhs.atts.setValue(SimpleSettlerLHSAttributes.PROP_id,newID);
            }
            newID = (Long) lhs.atts.getValue(SimpleSettlerLHSAttributes.PROP_parentID);
            if (newID==-1) {
                lhs.atts.setValue(SimpleSettlerLHSAttributes.PROP_parentID,newID);
            }
            newID = (Long) lhs.atts.getValue(SimpleSettlerLHSAttributes.PROP_origID);
            if (newID==-1) {
                lhs.atts.setValue(SimpleSettlerLHSAttributes.PROP_origID,newID);
            }
        }
        lhs.initialize();//initialize instance variables
        return lhs;
    }

    /**
     *  Returns the associated attributes.  
     */
    @Override
    public SimpleSettlerLHSAttributes getAttributes() {
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
        aid = atts.getValue(SimpleSettlerLHSAttributes.PROP_id, id);
        if (aid==-1) {
            //change atts id to lhs id
            atts.setValue(SimpleSettlerLHSAttributes.PROP_id, id);
        } else {
            //change lhs id to atts id
            id = aid;
        }
        aid = atts.getValue(SimpleSettlerLHSAttributes.PROP_parentID, id);
        if (aid==-1) {
            atts.setValue(SimpleSettlerLHSAttributes.PROP_parentID, id);
        }
        aid = atts.getValue(SimpleSettlerLHSAttributes.PROP_origID, id);
        if (aid==-1) {
            atts.setValue(SimpleSettlerLHSAttributes.PROP_origID, id);
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
     *  Instance field "id" is also updated.
     * @param newAtts - should be instance of SimplePelagicLHSAttributes
     */
    @Override
    public void setAttributes(LifeStageAttributesInterface newAtts) {
        if (newAtts instanceof SimplePelagicLHSAttributes) {
            super.setAttributes(newAtts);
        } else {
            //TODO: should throw an error here
            logger.info("SimpleSettlerLHS.setAttributes(): no match for attributes type");
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
         *  5) copy the Lagrangian Particle from the old LHS
         *  6) start a new track from the current position for the oldLHS
         *  7) update local variables
         */
        LifeStageAttributesInterface oldAtts = oldLHS.getAttributes();            
        setAttributes(oldAtts);
        
        //reset some attributes
        atts.setValue(LifeStageAttributesInterface.PROP_ageInStage, 0.0);//reset age in stage
        atts.setValue(LifeStageAttributesInterface.PROP_active,true);    //set active to true
        atts.setValue(LifeStageAttributesInterface.PROP_alive,true);     //set alive to true
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
        atts.setValue(LifeStageAttributesInterface.PROP_id,idc);//reset id to one for current LHS
        atts.setValue(LifeStageAttributesInterface.PROP_parentID,
                oldAtts.getValue(LifeStageAttributesInterface.PROP_id));//copy old id to parentID
        atts.setValue(LifeStageAttributesInterface.PROP_number, numTrans);//set number to numTrans
        atts.setValue(LifeStageAttributesInterface.PROP_ageInStage, 0.0); //reset age in stage
        atts.setValue(LifeStageAttributesInterface.PROP_active,true);     //set active to true
        atts.setValue(LifeStageAttributesInterface.PROP_alive,true);      //set alive to true
            
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
    public SimpleSettlerLHSParameters getParameters() {
        return params;
    }

    /**
     * Sets the parameters for the instance to a cloned version of the input.
     * @param newParams - should be instance of SimpleSettlerLHSParameters
     */
    @Override
    public void setParameters(LifeStageParametersInterface newParams) {
        if (newParams instanceof SimpleSettlerLHSParameters) {
            try {
                params = (SimpleSettlerLHSParameters) newParams.clone();
                setParametersFromSubClass(params);
                setParameterValues();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(SimpleSettlerLHS.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //TODO: throw some error
        }
    }
    
    /*
     * Copy the values from the params map to the param variables.
     */
    private void setParameterValues() {
        isSuperIndividual = 
                params.getValue(SimpleSettlerLHSParameters.PROP_isSuperIndividual,true);
        hasPreferredDepthDay = 
                params.getValue(SimpleSettlerLHSParameters.PROP_hasPreferredDepthDay,true);
        hasPreferredDepthNight = 
                params.getValue(SimpleSettlerLHSParameters.PROP_hasPreferredDepthNight,true);
        willAttachDay = 
                params.getValue(SimpleSettlerLHSParameters.PROP_willAttachDay,true);
        willAttachNight = 
                params.getValue(SimpleSettlerLHSParameters.PROP_willAttachNight,true);
        minDepthDay = 
                params.getValue(SimpleSettlerLHSParameters.PROP_minDepthDay,minDepthDay);
        maxDepthDay = 
                params.getValue(SimpleSettlerLHSParameters.PROP_maxDepthDay,maxDepthDay);
        minDepthNight = 
                params.getValue(SimpleSettlerLHSParameters.PROP_minDepthNight,minDepthNight);
        maxDepthNight = 
                params.getValue(SimpleSettlerLHSParameters.PROP_maxDepthNight,maxDepthNight);
        vertSwimmingSpeed = 
                params.getValue(SimpleSettlerLHSParameters.PROP_vertSwimmingSpeed,vertSwimmingSpeed);
        vertDiffusion = 
                params.getValue(SimpleSettlerLHSParameters.PROP_vertDiffusion,vertDiffusion);
        horizDiffusion = 
                params.getValue(SimpleSettlerLHSParameters.PROP_horizDiffusion,horizDiffusion);
        maxStageDuration = 
                params.getValue(SimpleSettlerLHSParameters.PROP_maxStageDuration,maxStageDuration);
        minSettlementDepth = 
                params.getValue(SimpleSettlerLHSParameters.PROP_minSettlementDepth,minSettlementDepth);
        maxSettlementDepth = 
                params.getValue(SimpleSettlerLHSParameters.PROP_maxSettlementDepth,minSettlementDepth);
        stageTransRate = 
                params.getValue(SimpleSettlerLHSParameters.PROP_stageTransRate,stageTransRate);
        growthRate = 
                params.getValue(SimpleSettlerLHSParameters.PROP_growthRate,growthRate);
        mortalityRate = 
                params.getValue(SimpleSettlerLHSParameters.PROP_mortalityRate,mortalityRate);
        useRandomGrowth = 
                params.getValue(SimpleSettlerLHSParameters.PROP_useRandomGrowth,true);
        useRandomMortality = 
                params.getValue(SimpleSettlerLHSParameters.PROP_useRandomMortality,true);
        useRandomTransitions = 
                params.getValue(SimpleSettlerLHSParameters.PROP_useRandomTransitions,true);
    }
    
    /**
     *  Provides a copy of the object.  The attributes and parameters
     *  are cloned in the process, so the clone is independent of the
     *  original.
     */
    @Override
    public Object clone() {
        SimpleSettlerLHS clone = null;
        try {
            clone  = (SimpleSettlerLHS) super.clone();
            clone.setAttributes(atts);//this clones atts
            clone.setParameters(params);//this clones params
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
        output.clear();
        List<LifeStageInterface> nLHSs = null;
        //if total depth is appropriate for settlement and 
        //indiv is near the bottom, then settle and transform to next stage.
        if ((bathym>=minSettlementDepth)&&
                (bathym<=maxSettlementDepth)&&
                (depth>(bathym-5))) {
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
                 * Since this is LHS instance is a super individual, only a part 
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

    public void initialize() {
//        atts.setValue(SimpleSettlerLHSAttributes.PROP_id,id);
        updateVariables();//set instance variables to attribute values
        int hType,vType;
        hType=vType=-1;
        double xPos, yPos, zPos;
        xPos=yPos=zPos=0;
        hType      = atts.getValue(SimpleSettlerLHSAttributes.PROP_horizType,hType);
        vType      = atts.getValue(SimpleSettlerLHSAttributes.PROP_vertType,vType);
        xPos       = atts.getValue(SimpleSettlerLHSAttributes.PROP_horizPos1,xPos);
        yPos       = atts.getValue(SimpleSettlerLHSAttributes.PROP_horizPos2,yPos);
        zPos       = atts.getValue(SimpleSettlerLHSAttributes.PROP_vertPos,zPos);
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
            double z = i3d.interpolateBathymetricDepth(IJ);
            if (debug) logger.info("Bathymetric depth = "+z);

            double K = 0;  //set K = 0 (at bottom) as default
            if (vType==Types.VERT_K) {
                if (zPos<0) {K = 0;} else
                if (zPos>i3d.getGrid().getN()) {K = i3d.getGrid().getN();} else
                K = zPos;
            } else if (vType==Types.VERT_Z) {//depths negative
                if (zPos<-z) {K = 0;} else                    //at bottom
                if (zPos>0) {K = i3d.getGrid().getN();} else //at surface
                K = i3d.calcKfromZ(IJ[0],IJ[1],zPos);        //at requested depth
            } else if (vType==Types.VERT_H) {//depths positive
                if (zPos>z) {K = 0;} else                    //at bottom
                if (zPos<0) {K = i3d.getGrid().getN();} else //at surface
                K = i3d.calcKfromZ(IJ[0],IJ[1],-zPos);      //at requested depth
            } else if (vType==Types.VERT_DH) {//distance off bottom
                if (zPos<0) {K = 0;} else                    //at bottom
                if (zPos>z) {K = i3d.getGrid().getN();} else //at surface
                K = i3d.calcKfromZ(IJ[0],IJ[1],-(z-zPos));   //at requested distance off bottom
            }
            lp.setIJK(IJ[0],IJ[1],K);
            //reset track array
            track.clear();
            trackLL.clear();
            //set horizType to lat/lon and vertType to depth
            atts.setValue(SimpleSettlerLHSAttributes.PROP_horizType,Types.HORIZ_LL);
            atts.setValue(SimpleSettlerLHSAttributes.PROP_vertType,Types.VERT_H);
            //interpolate initial position and environmental variables
            double[] pos = lp.getIJK();
            updatePosition(pos);
            interpolateEnvVars(pos);
            updateAttributes(); 
        }
        updateVariables();//set instance variables to attribute values
    }
    
    @Override
    public void step(double dt) throws ArrayIndexOutOfBoundsException {
        double[] pos = null;
        //determine daytime/nighttime for vertical migration & calc indiv. W
        isDaytime = DateTimeFunctions.isDaylight(lon,lat,GlobalInfo.getInstance().getCalendar().getYearDay());
        if (isDaytime&&willAttachDay&&(depth>(bathym-1))) {
            //set indiv on bottom and don't let it move
            attached = true;
            pos = lp.getIJK();
            pos[2] = 0;
        } else 
        if (!isDaytime&&willAttachNight&&(depth>(bathym-1))) {
            //set indiv on bottom and don't let it move
            attached = true;
            pos = lp.getIJK();
            pos[2] = 0;
        } else {
            attached = false;
            //calculate deterministic swimming rate for current position.
            //note that the sign is reversed if timestep (dt) is < 0
            //to maintain correct depth range behavior when running model backwards
            double w = calcW(dt);
            lp.setW(w,lp.getN());
            double[] uv = calcUV(dt);
            lp.setU(uv[0],lp.getN());
            lp.setV(uv[1],lp.getN());
            //now do predictor step
            lp.doPredictorStep();
            //assume same daytime status, but recalc depth and revise W 
//            pos = lp.getPredictedIJK();
//            depth = -i3d.calcZfromK(pos[0],pos[1],pos[2]);
//            if (debug) logger.info("Depth after predictor step = "+depth);
            //w = calcW(dt,lp.getNP1())+r; //set swimming rate for predicted position
            lp.setW(w,lp.getNP1());
            lp.setU(uv[0],lp.getNP1());
            lp.setV(uv[1],lp.getNP1());
            //now do corrector step
            lp.doCorrectorStep();
            pos = lp.getIJK();
        }
//        logger.info("id: "+id+cc+isDaytime+cc+depth+cc+bathym+cc+attached);
        time += dt;
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
        updateAttributes(); //update the attributes object w/ modified values
    }
        
    /**
     * Function to calculate vertical swimming speed.
     * 
     * @param dt - time step
     * @return 
     */
    protected double calcW(double dt) {
//        String str = "";
        double w = 0;
        if ((minSettlementDepth<=bathym)&&(bathym<=maxSettlementDepth)){
            w =  -vertSwimmingSpeed*(1.0-Math.exp(-(bathym-depth)/5.0));//swim towards bottom
        } else
        if (isDaytime) {
            if (depth<minDepthDay) {
                w = -vertSwimmingSpeed*(1.0-Math.exp(-(minDepthDay-depth)/10.0));
            } else 
            if (depth>maxDepthDay) {
                w =  vertSwimmingSpeed*(1.0-Math.exp(-(depth-maxDepthDay)/10.0));
            }
//            str = id+" Daytime "+depth+" "+minDepthDay+" "+maxDepthDay+" "+w;
        } else {
            if (depth<minDepthNight) {
                w = -vertSwimmingSpeed*(1.0-Math.exp(-(minDepthNight-depth)/10.0));
            } else 
            if (depth>maxDepthNight) {
                w =  vertSwimmingSpeed*(1-Math.exp(-(depth-maxDepthNight)/10.0));
            }
//            str = id+" Nighttime "+depth+" "+minDepthNight+" "+maxDepthNight+" "+w;
        }
        //System.out.print(str);
        if (vertDiffusion>0) {
            w += rng.computeNormalVariate()*Math.sqrt(vertDiffusion/Math.abs(dt));
        }
        w = Math.signum(dt)*w;
        return w;
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

    private void updateNum(double dt) {
        double totRate = mortalityRate;
        //if total depth is appropriate for settlement and 
        //indiv is near the bottom, then settle and transform to next stage.
        if ((bathym>=minSettlementDepth)&&
                (bathym<=maxSettlementDepth)&&
                (depth>(bathym-5))) {
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
        depth      = -i3d.calcZfromK(pos[0],pos[1],pos[2]);
        lat        = i3d.interpolateLat(pos);
        lon        = i3d.interpolateLon(pos);
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
        atts.setValue(SimpleSettlerLHSAttributes.PROP_startTime,startTime);
        atts.setValue(SimpleSettlerLHSAttributes.PROP_time,time);
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
    public List<LifeStageInterface> getSpawnedIndividuals() {
        output.clear();
        return output;
    }

    @Override
    public boolean isSuperIndividual() {
        return isSuperIndividual;
    }

}
