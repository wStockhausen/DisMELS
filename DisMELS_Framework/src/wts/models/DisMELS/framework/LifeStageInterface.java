/*
 * LifeStageInterface.java
 */
package wts.models.DisMELS.framework;

import com.vividsolutions.jts.geom.Coordinate;
import java.io.Serializable;
import java.util.List;
import wts.roms.model.LagrangianParticle;

/**
 * Interface implemented by all life stage classes.
 * 
 * @author William.Stockhausen
 */
public interface LifeStageInterface extends Cloneable, Serializable {
    /** length of day in seconds */
    public static final double DAY_SECS = 86400.0;
    /** length of hour in seconds */
    public static final double HR_SECS = 3600.0;
    /** integer flag indicating position(s) in projected (x,y,-depth) coordinates */
    public static final int COORDINATE_TYPE_PROJECTED = 0;
    /** integer flag indicating position(s) in geographic (lat,lon,-depth) coordinates */
    public static final int COORDINATE_TYPE_GEOGRAPHIC = 1;

    /**
     * This method should be overridden by extending classes.
     * 
     * @return - a deep clone
     * @throws java.lang.CloneNotSupportedException
     */
    Object clone() throws CloneNotSupportedException;

    /**
     * Creates life stage instance using values in String[].  The resulting instance
     * should have the input typeName (String[0]).
     * 
     * @param strv
     * 
     * @return instance of new life stage
     * 
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    LifeStageInterface createInstance(String[] strv) throws InstantiationException, IllegalAccessException;

    /**
     * Creates LHS instance using the attributes in theAtts.  The resulting
     * instance should have the same typeName as theAtts.
     * @param theAtts
     * @return instance of new LHS
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    LifeStageInterface createInstance(LifeStageAttributesInterface theAtts) throws InstantiationException, IllegalAccessException;

    /**
     * Returns the associated attributes object.
     * @return 
     */
    LifeStageAttributesInterface getAttributes();

    /**
     * Returns the associated attributes class as a string.
     * @return 
     */
    String getAttributesClassName();

    /**
     * Returns the instance id.
     * @return 
     */
    long getID();

    /**
     * Returns the LagrangianParticle instance associated with the individual. 
     * @return 
     */
    LagrangianParticle getLagrangianParticle();

    /**
     * Gets the last projected position in the track of the instance.
     *
     * @param coordType - type of coordinates requested (e.g. grid or lat/lon)
     * 
     * @return - last proejcted position as Coordinate
     */
    Coordinate getLastPosition(int coordType);

    /**
     * Returns a list of new individuals created by this instance
     * due to transitions to the next life history stage within the last dt.
     * 
     * @param dt
     * 
     * @return List<LifeStageInterface>
     */
    List<LifeStageInterface> getMetamorphosedIndividuals(double dt);

    /**
     * Returns the class names (as strings) of the potential "next" life stage classes.
     * 
     * @return 
     */
    String[] getNextLHSClassNames();

    /**
     * This method should be overriden by inheriting classes!
     * 
     * @return the associated parameters instance
     */
    LifeStageParametersInterface getParameters();

    /**
     * Returns the class name (as string) of the associated 
     * life stage parameters class.
     * 
     * @return 
     */
    String getParametersClassName();

    /**
     * Returns the class name (as string) of the associated 
     * point feature type class.
     * 
     * @return 
     */
    String getPointFeatureTypeClassName();

    /**
     * This provides a default implementation of getReport() that returns
     * the attributes (via LifeStageInterfaceAttributes.getCSV()) and the track (in
     * geographic coordinates) as a CSV formatted string.
     *
     * @return - the attributes and track as a csv-formatted String
     */
    String getReport();

    /**
     * This provides a default implementation of getReportHeader() that returns
     * the attribute names (via LifeStageInterfaceAttributes.getCSVHeaderShortNames())
     * and "track" as a CSV formatted string.
     *
     * @return - the header names as a csv-formatted String
     */
    String getReportHeader();

    /**
     * Returns a list of new individuals created by this instance
     * through spawning.
     */
    List<LifeStageInterface> getSpawnedIndividuals();

    /**
     * Returns the class names (as strings) of the potential "spawned" life stage classes.
     * 
     * @return 
     */
    String[] getSpawnedLHSClassNames();

    double getStartTime();

    /**
     * Gets the instance's track.
     *
     * @param coordType - type of coordinates requested (e.g. grid or lat/lon)
     * @return - track as Coordinate[]
     */
    Coordinate[] getTrack(int coordType);

    /**
     * Returns the track information in string format.
     *
     * @param coordType - type of coordinates requested (e.g. grid or lat/lon)
     * @return
     */
    String getTrackAsString(int coordType);

    /**
     * Returns the life stage type for the instance.
     * @return the life stage type name
     */
    public String getTypeName();
    
    /**
     * Gets the flag indicating whether or not to write the full track to output.
     * @return true/false whether full track will be written to output
     */
    public boolean getWriteTracksFlag();
    
    /**
     * Sets the flag indicating whether or not to write the full track to output.
     * @param b - flag to write full track output 
     */
    public void setWriteTracksFlag(boolean b);

    /**
     * Returns whether the individual is active (true) or not. 
     * 
     * @return true/false whether individual is "active"
     */
    boolean isActive();

    /**
     * Returns whether the individual is alive (true) or not. 
     * 
     * @return true/false whether individual is "alive"
     */
    boolean isAlive();

    /**
     * Returns whether the instance represents a super individual (true) or not. 
     * 
     * @return true/false whether individual is a "superindividual"
     */
    boolean isSuperIndividual();

    /**
     * Sets the "active" status for the individual. 
     * 
     * @param b flag indicating whether or not individual is "active"
     */
    void setActive(boolean b);

    /**
     * Sets the "alive" status for the individual. 
     * 
     * @param b flag indicating whether or not individual is "alive"
     */
    void setAlive(boolean b);

    /**
     * Sets the associated attributes object.  This should NOT change the
     * typeNameof the LHS instance (or the associated LHSAttributes instance)
     * on which the method is called.
     * 
     * @param newAtts - the attributes object to set
     */
    void setAttributes(LifeStageAttributesInterface newAtts);

    /**
     * Sets the values of the associated attributes object.  This should NOT change the
     * typeNameof the LHS instance (or the associated LHSAttributes instance)
     * on which the method is called.
     * 
     * @param strv String array with attribute values as Strings
     */
    void setAttributes(String[] strv);

    /**
     * Sets the associated attributes object from the oldLHS instance.
     * Use this after creating an LHS instance as "output" from another LHS that
     * is functioning as an ordinary individual.
     * Note that the typeName for the instance on which the method is called
     * should NOT be changed.
     * 
     * @param oldLHS - the "old" individual from which information will be transferred
     */
    void setInfoFromIndividual(LifeStageInterface oldLHS);

    /**
     * Sets the associated attributes object from the oldLHS instance.
     * Use this after creating an LHS instance as "output" from another LHS that
     * is functioning as a super-individual.
     * Note that the typeName for the instance on which the method is called
     * shoud NOT be changed.
     * 
     * @param oldLHS - the "old" (super) individual from which information will be transferred
     * @param numTrans - the number of individuals making the stage transition 
     */
    void setInfoFromSuperIndividual(LifeStageInterface oldLHS, double numTrans);

    /**
     * Sets the lagrangian particle representation for the instance to a clone
     * of newLP.
     * 
     * @param newLP - LagrangianParticle instance to be cloned.
     */
    void setLagrangianParticle(LagrangianParticle newLP);

    /**
     * This method should be overriden by inheriting classes!
     * 
     * @param newParams - the new Parameters object to use
     */
    void setParameters(LifeStageParametersInterface newParams);

    /**
     * Sets the start time (in s from model reference date) for the individual
     * to be "activated" in the simulation.
     * 
     * @param newTime 
     */
    void setStartTime(double newTime);

    /**
     * Sets the tolerance (in grid coordinates) for individuals to the edge of 
     * the model grid. Individuals closer to the grid edge than the tolerance
     * should be removed from the simulation.
     * 
     * @param newTol 
     */
    void setGridEdgeTolerance(double newTol);

    /**
     * Clears the existing track and restarts it with the new initial position.
     *
     * @param initPos - new starting position.
     * @param coordType - type of coordinates requested (e.g. grid or lat/lon)
     */
    void startTrack(Coordinate initPos, int coordType);

    /**
     * Steps the individual forward (dt &gt 0) or backward (dt &lt 0) in time.  This is 
     * the heart of the IBM for the individuals represented by the class that
     * implements this interface.
     * 
     * @param dt - time step duration (s)
     * @throws ArrayIndexOutOfBoundsException 
     */
    void step(double dt) throws ArrayIndexOutOfBoundsException;
    
}
