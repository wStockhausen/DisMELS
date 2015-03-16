/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework;

import com.vividsolutions.jts.geom.Coordinate;
import java.io.Serializable;
import java.util.List;
import wts.roms.model.LagrangianParticle;

/**
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
     */
    Object clone() throws CloneNotSupportedException;

    /**
     * Creates LHS instance using values in String[].  The resulting instance
     * should have the input typeName (String[0]).
     * @param strv
     * @return instance of new LHS
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    LifeStageInterface createInstance(String[] strv) throws InstantiationException, IllegalAccessException;

    /**
     * Creates LHS instance using the attributes in theAtts.  The resulting
     * instance should have the same typeName as theAtts.
     * @param strv
     * @return instance of new LHS
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    LifeStageInterface createInstance(LifeStageAttributesInterface theAtts) throws InstantiationException, IllegalAccessException;

    /**
     * Returns the associated attributes object.
     */
    LifeStageAttributesInterface getAttributes();

    /**
     * Returns the associated attributes class as a string.
     * @return 
     */
    String getAttributesClassName();

    /**
     * Returns the instance id.
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
     * @return - last proejcted position as Coordinate
     */
    Coordinate getLastPosition(int coordType);

    /**
     * Returns a list of new individuals created by this instance
     * due to transitions to the next life history stage within the last dt.
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
     */
    String getTypeName();
    
    /**
     * Gets the flag indicating whether or not to write the full track to output.
     * @return 
     */
    public boolean getWriteTracksFlag();
    
    /**
     * Sets the flag indicating whether or not to write the full track to output.
     * @return 
     */
    public void setWriteTracksFlag(boolean b);

    /**
     * Returns whether the individual is active (true) or not. 
     */
    boolean isActive();

    /**
     * Returns whether the individual is alive (true) or not. 
     */
    boolean isAlive();

    /**
     * Returns whether the instance represents a super individual (true) or not. 
     */
    boolean isSuperIndividual();

    /**
     * Sets the "active" status for the individual. 
     */
    void setActive(boolean b);

    /**
     * Sets the "alive" status for the individual. 
     */
    void setAlive(boolean b);

    /**
     * Sets the associated attributes object.  This should NOT change the
     * typeNameof the LHS instance (or the associated LHSAttributes instance)
     * on which the method is called.
     */
    void setAttributes(LifeStageAttributesInterface newAtts);

    /**
     * Sets the values of the associated attributes object.  This should NOT change the
     * typeNameof the LHS instance (or the associated LHSAttributes instance)
     * on which the method is called.
     */
    void setAttributes(String[] strv);

    /**
     * Sets the associated attributes object from the oldLHS instance.
     * Use this after creating an LHS instance as "output" from another LHS that
     * is functioning as an ordinary individual.
     * Note that the typeName for the instance on which the method is called
     * shoud NOT be changed.
     */
    void setInfoFromIndividual(LifeStageInterface oldLHS);

    /**
     * Sets the associated attributes object from the oldLHS instance.
     * Use this after creating an LHS instance as "output" from another LHS that
     * is functioning as a super-individual.
     * Note that the typeName for the instance on which the method is called
     * shoud NOT be changed.
     */
    void setInfoFromSuperIndividual(LifeStageInterface oldLHS, double numTrans);

    /**
     * Sets the lagrangian particle representation for the instance to a clone
     * of newLP.
     * @param newLP - LagrangianParticle instance to be cloned.
     */
    void setLagrangianParticle(LagrangianParticle newLP);

    /**
     * This method should be overriden by inheriting classes!
     */
    void setParameters(LifeStageParametersInterface newParams);

    /**
     * Sets the start time (in s from model reference date) for the individual
     * to be "activated" in the simulation.
     * @param newTime 
     */
    void setStartTime(double newTime);

    /**
     * Sets the tolerance (in grid coordinates) for individuals to the edge of 
     * the model grid. Individuals closer to the grid edge than the tolerance
     * should be removed from the simulation.
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
     * Steps the individual forward (dt>0) or backward (dt<0) in time.  This is 
     * the heart of the IBM for the individuals represented by the class that
     * implements this interface.
     * 
     * @param dt - time step duration (s)
     * @throws ArrayIndexOutOfBoundsException 
     */
    void step(double dt) throws ArrayIndexOutOfBoundsException;
    
}
