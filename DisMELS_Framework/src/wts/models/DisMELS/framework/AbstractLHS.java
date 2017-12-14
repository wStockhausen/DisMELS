/*
 * AbstractLHS.java
 */

package wts.models.DisMELS.framework;

import com.vividsolutions.jts.geom.Coordinate;
import com.wtstockhausen.utils.RandomNumberGenerator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import wts.roms.gis.AlbersNAD83;
import wts.roms.model.Interpolator3D;
import wts.roms.model.LagrangianParticle;

/**
 * An abstract base class that provides default implementations for many of the 
 * methods required to implement LifeStageInterface.
 * 
 * The implementations for the methods setAttributes(LifeStageAttributesInterface),
 * updateAttributes() and updateVariables() cover the attributes identified by keys
 * in LifeStageAttributesInterface. Subclasses overriding these methods may call
 * the overriden method on super to obtain this functionality without having to
 * rewrite it.
 */
public abstract class AbstractLHS implements LifeStageInterface {
    
    //Static fields
    /* ROMS 3d interpolator object */
    protected static Interpolator3D i3d;
    /* Random Number Generator */
    protected static RandomNumberGenerator rng = null;
    /** tolerance to edge of model grid */
    protected static double tolGridEdge = 0.5;
    
    /** String for "," */
    protected static final String cc = ",";
    /** the output format used for decimal numbers */
    protected static final DecimalFormat decFormat = new DecimalFormat("#.#####");

    /**flag to write track info to output file */
    protected static boolean writeTracksFlag = false;
    
    /**
     * Sets the flag to retain full track information (true) or only
     * beginning and ending locations (false) for track output.
     * @param b 
     */
    public static void setWriteTracksFlagForClass(boolean b){
        writeTracksFlag = b;
    }
    
    /**
     * Gets the flag to retain full track information (true) or only
     * beginning and ending locations (false) for track output.
     * @return 
     */
    public static boolean getWriteTracksFlagForClass(){
        return writeTracksFlag;
    }
    
    //Instance fields
    /* the LHS attributes */
    protected LifeStageAttributesInterface atts = null;
    /* the LHS parameters */
    protected LifeStageParametersInterface params = null;
    
    /* the LagrangianParticle */
    protected LagrangianParticle lp = new LagrangianParticle();;
    /* the spatial track in projected coordinates for the LHS */
    protected ArrayList<Coordinate> track = new ArrayList<>();
    /* the spatial track in geopgraphic coordinates for the LHS */
    protected ArrayList<Coordinate> trackLL = new ArrayList<>();
    /* array list for output LHS instances */
    protected ArrayList<LifeStageInterface> output = new ArrayList<>();
    /* the type of LHS instance */
    protected String typeName = null;
    /* id for this instance*/
    protected long id = 0;
    
    //fields that reflect attribute values
    protected boolean active=false;
    protected boolean alive=true;
    protected boolean attached=false;
    protected double  startTime=0;
    protected double  time=0;
    protected double  age=0;
    protected double  ageInStage=0;
    protected double  number=0;
    protected double  lat=0;
    protected double  lon=0;
    protected double  depth=0;
    protected String  gridCellID="";
    
    /**
     * Creates a new instance of AbstractLHS
     * @param typeName
     */
    protected AbstractLHS(String typeName) {
        this.typeName = typeName;
        id = LHS_Factory.getNewID();
        if (rng==null) rng = GlobalInfo.getInstance().getRandomNumberGenerator();
        if (i3d==null) i3d = GlobalInfo.getInstance().getInterpolator3D();
    }
    
    /**
     * Subclasses can use this method to make sure the attributes object from
     * this superclass refers to the one from their class.
     * 
     * @param subAtts 
     */
    protected void setAttributesFromSubClass(LifeStageAttributesInterface subAtts){
        atts = subAtts;
    }
    
    /**
     * Subclasses can use this method to make sure the parameters object from
     * this superclass refers to the one from their class.
     * 
     * @param subParams 
     */
    protected void setParametersFromSubClass(LifeStageParametersInterface subParams){
        params = subParams;
    }
    
    /**
     *  Returns the instance id.
     * @return 
     */
    @Override
    public final long getID() {
        return id;
    }
    
    @Override
    public LagrangianParticle getLagrangianParticle() {
        return lp;
    }
    
    /**
     * Sets the lagrangian particle representation for the instance to a clone  
     * of newLP.
     * @param newLP - LagrangianParticle instance to be cloned.
     */
    @Override
    public void setLagrangianParticle(LagrangianParticle newLP) {
        lp = (LagrangianParticle) newLP.clone();
    }
    
    /**
     * Gets the last projected position in the track of the instance.
     * 
     * @param coordType - type of coordinates requested (e.g. grid or lat/lon)
     * @return - last proejcted position as Coordinate
     */
    @Override
    public Coordinate getLastPosition(int coordType) {
        if (coordType==COORDINATE_TYPE_PROJECTED)  return track.get(track.size()-1); else
        if (coordType==COORDINATE_TYPE_GEOGRAPHIC) return trackLL.get(trackLL.size()-1);
        return null;
    }
    
    /**
     * Gets the instance's track.
     * 
     * @param coordType - type of coordinates requested (e.g. grid or lat/lon)
     * @return - track as Coordinate[]
     */
    @Override
    public Coordinate[] getTrack(int coordType) {
        Coordinate[] cv = new Coordinate[1];
        if (coordType==COORDINATE_TYPE_PROJECTED)  return track.toArray(cv); else
        if (coordType==COORDINATE_TYPE_GEOGRAPHIC) return trackLL.toArray(cv);
        return null;
    }

    /**
     * Clears the existing track and restarts it with the new initial position.
     * 
     * @param initPos - new starting position.
     * @param coordType - type of coordinates requested (e.g. grid or lat/lon)
     */
    @Override
    public void startTrack(Coordinate initPos, int coordType) {
        if (coordType==COORDINATE_TYPE_PROJECTED) {
            track.clear();
            track.add(initPos);
        } else
        if (coordType==COORDINATE_TYPE_GEOGRAPHIC) {
            trackLL.clear();
            trackLL.add(initPos);
        }    
    }

    /**
     * Returns the track information in string format.
     * 
     * @param coordType - type of coordinates requested (e.g. grid or lat/lon)
     * @return 
     */
    @Override
    public String getTrackAsString(int coordType){
        StringBuilder strb = new StringBuilder();
        if (coordType==COORDINATE_TYPE_PROJECTED) {
            for (Coordinate c : track){
                strb.append(decFormat.format(c.x));strb.append(":");
                strb.append(decFormat.format(c.y));strb.append(":");
                strb.append(decFormat.format(c.z));strb.append(";");
            }
        } else
        if (coordType==COORDINATE_TYPE_GEOGRAPHIC) {
            for (Coordinate c : trackLL){
                strb.append(decFormat.format(c.x));strb.append(":");
                strb.append(decFormat.format(c.y));strb.append(":");
                strb.append(decFormat.format(c.z));strb.append(";");
            }
        }
        return strb.toString();
    }

    /**
     *  Returns the LHS type for the instance.
     * @return 
     */
    @Override
    public String getTypeName() {return typeName;}
    
//  Methods inherited from LifeHistoryStageIF  
    /**
     *  This method should be overridden by extending classes.
     * 
     * @return - clone of the instance on which it was called
     * 
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Returns the report for the implementing class as a CSV formatted string.
     *
     * @return - the attributes and track as a csv-formatted String
     */
    @Override
    public abstract String getReport();

     /**
     * Returns the report header for the implementing class as a CSV formatted string.
     *
     * @return - the header names as a csv-formatted String
     */
    @Override
   public abstract String getReportHeader();
    
    @Override
    public void setGridEdgeTolerance(double newTol){
        tolGridEdge = newTol;
    }
    
    /** Gets value of flag to write complete track information (if true) */
    @Override
    public boolean getWriteTracksFlag(){
        return writeTracksFlag;
    }
    
    /** 
     * Sets flag to write complete track information (if true)
     * 
     * @param b - the value to set
     */
    @Override
    public void setWriteTracksFlag(boolean b){
        writeTracksFlag = b;
    }
    
    /**
     * Updates both types of tracks (projected and geographic).
     */    
    protected void updateTrack() {
        double[] dest = AlbersNAD83.transformGtoP(new double[]{lon,lat});
        Coordinate c = new Coordinate(dest[0],dest[1]);
        if (writeTracksFlag){
            //add current location to end of tracks
            track.add(c);
            trackLL.add(new Coordinate(lon,lat,-depth));
        } else {
            if (track.isEmpty()) {
                //add current location to tracks as 1st coordinate
                track.add(c);
                trackLL.add(new Coordinate(lon,lat,-depth));
            } else if (track.size()==1) {
                //add current location to tracks as 2nd coordinate
                track.add(c);
                trackLL.add(new Coordinate(lon,lat,-depth));
            } else {
                //replace 2nd coordinate with current location
                track.set(1, c);
                trackLL.set(1,new Coordinate(lon,lat,-depth));
            }
        }
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
        String key;
        key = LifeStageAttributesInterface.PROP_active;     atts.setValue(key,newAtts.getValue(key));
        key = LifeStageAttributesInterface.PROP_alive;      atts.setValue(key,newAtts.getValue(key));
        key = LifeStageAttributesInterface.PROP_age;        atts.setValue(key,newAtts.getValue(key));
        key = LifeStageAttributesInterface.PROP_ageInStage; atts.setValue(key,newAtts.getValue(key));
        key = LifeStageAttributesInterface.PROP_gridCellID; atts.setValue(key,newAtts.getValue(key));
        key = LifeStageAttributesInterface.PROP_horizPos1;  atts.setValue(key,newAtts.getValue(key));
        key = LifeStageAttributesInterface.PROP_horizPos2;  atts.setValue(key,newAtts.getValue(key));
        key = LifeStageAttributesInterface.PROP_horizType;  atts.setValue(key,newAtts.getValue(key));
        key = LifeStageAttributesInterface.PROP_id;         atts.setValue(key,newAtts.getValue(key));
        key = LifeStageAttributesInterface.PROP_parentID;   atts.setValue(key,newAtts.getValue(key));
        key = LifeStageAttributesInterface.PROP_origID;     atts.setValue(key,newAtts.getValue(key));
        key = LifeStageAttributesInterface.PROP_number;     atts.setValue(key,newAtts.getValue(key));
        key = LifeStageAttributesInterface.PROP_startTime;  atts.setValue(key,newAtts.getValue(key));
        key = LifeStageAttributesInterface.PROP_time;       atts.setValue(key,newAtts.getValue(key));
        key = LifeStageAttributesInterface.PROP_vertPos;    atts.setValue(key,newAtts.getValue(key));
        key = LifeStageAttributesInterface.PROP_vertType;   atts.setValue(key,newAtts.getValue(key));
        id = atts.getValue(LifeStageAttributesInterface.PROP_id, id);
        updateVariables();
    }
    
    /**
     * Updates attribute values defined for this abstract class. Subclasses should
     * override this method, possibly calling super.updateAttributes().
     */
    protected void updateAttributes() {
        //note that the following do not need to be updated
        //  id, parentID, origID, startTime, horizType, vertType
        atts.setValue(LifeStageAttributesInterface.PROP_active,active);
        atts.setValue(LifeStageAttributesInterface.PROP_alive,alive);
        atts.setValue(LifeStageAttributesInterface.PROP_time,time);
        atts.setValue(LifeStageAttributesInterface.PROP_horizPos1,lon);
        atts.setValue(LifeStageAttributesInterface.PROP_horizPos2,lat);
        atts.setValue(LifeStageAttributesInterface.PROP_vertPos,depth);
        atts.setValue(LifeStageAttributesInterface.PROP_age,age);
        atts.setValue(LifeStageAttributesInterface.PROP_ageInStage,ageInStage);
        atts.setValue(LifeStageAttributesInterface.PROP_number,number);
        atts.setValue(LifeStageAttributesInterface.PROP_gridCellID,gridCellID);
    }

    /**
     * Updates local variables from the attributes.  
     * The following are NOT updated here:
     *  id, parentID, origID, hType, vType
     */
    protected void updateVariables() {
        active     = atts.getValue(LifeStageAttributesInterface.PROP_active,active);
        alive      = atts.getValue(LifeStageAttributesInterface.PROP_alive,alive);
        startTime  = atts.getValue(LifeStageAttributesInterface.PROP_startTime,startTime);
        time       = atts.getValue(LifeStageAttributesInterface.PROP_time,time);
        lon        = atts.getValue(LifeStageAttributesInterface.PROP_horizPos1,lon);
        lat        = atts.getValue(LifeStageAttributesInterface.PROP_horizPos2,lat);
        depth      = atts.getValue(LifeStageAttributesInterface.PROP_vertPos,depth);
        age        = atts.getValue(LifeStageAttributesInterface.PROP_age,age);
        ageInStage = atts.getValue(LifeStageAttributesInterface.PROP_ageInStage,ageInStage);
        number     = atts.getValue(LifeStageAttributesInterface.PROP_number,number);
        gridCellID = atts.getValue(LifeStageAttributesInterface.PROP_gridCellID,gridCellID);
    }
    
}
