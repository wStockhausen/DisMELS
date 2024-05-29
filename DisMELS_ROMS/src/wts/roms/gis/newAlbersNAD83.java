/*
 * AlbersNAD83.java
 *
 * Created on June 27, 2006, 2:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.gis;

import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.cs.AxisInfo;
import org.geotools.cs.Ellipsoid;
import org.geotools.cs.Projection;
import org.geotools.ct.CannotCreateTransformException;
import org.geotools.units.Unit;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.NoninvertibleTransformException;

/**
 *
 * @author William Stockhausen
 */
final public class newAlbersNAD83 extends AbstractPCS {
    
    protected static final String pcsName = "Albers-NAD83";
    protected static final String pcsClassification = "Albers_Conic_Equal_Area";

    protected static final String PARAMNAMES_CENTRAL_MERIDIAN    = "central_meridian";
    protected static final String PARAMNAMES_LATITUDE_OF_ORIGIN  = "latitude_of_origin";
    protected static final String PARAMNAMES_STANDARD_PARALLEL_1 = "standard_parallel_1";
    protected static final String PARAMNAMES_STANDARD_PARALLEL_2 = "standard_parallel_2";
    protected static final String PARAMNAMES_FALSE_EASTING       = "false_easting";
    protected static final String PARAMNAMES_FALSE_NORTHING      = "false_northing";

    protected static final String[] paramNames = {PARAMNAMES_CENTRAL_MERIDIAN,
                                                  PARAMNAMES_LATITUDE_OF_ORIGIN,
                                                  PARAMNAMES_STANDARD_PARALLEL_1,
                                                  PARAMNAMES_STANDARD_PARALLEL_2,
                                                  PARAMNAMES_FALSE_EASTING,
                                                  PARAMNAMES_FALSE_NORTHING};

    protected static Unit linearUnit = Unit.METRE;

    public double getCentralMeridian() {
        return params.getDoubleParameter(PARAMNAMES_CENTRAL_MERIDIAN);
    }

    public void setCentralMeridian(double val) {
        params.setParameter(PARAMNAMES_CENTRAL_MERIDIAN, val);
    }
    
    public double getLatitudeOfOrigin() {
        return params.getDoubleParameter(PARAMNAMES_LATITUDE_OF_ORIGIN);
    }

    public void setLatitudeOfOrigin(double val) {
        params.setParameter(PARAMNAMES_LATITUDE_OF_ORIGIN, val);
    }

    public double getStandardParallel1() {
        return params.getDoubleParameter(PARAMNAMES_STANDARD_PARALLEL_1);
    }

    public void setStandardParallel1(double val) {
        params.setParameter(PARAMNAMES_STANDARD_PARALLEL_1, val);
    }

    public double getStandardParallel2() {
        return params.getDoubleParameter(PARAMNAMES_STANDARD_PARALLEL_2);
    }

    public void setStandardParallel2(double val) {
        params.setParameter(PARAMNAMES_STANDARD_PARALLEL_2, val);
    }

    public double getFalseEasting() {
        return params.getDoubleParameter(PARAMNAMES_FALSE_EASTING);
    }

    public void setFalseEasting(double val) {
        params.setParameter(PARAMNAMES_FALSE_EASTING, val);
    }
    
    public double getFalseNorthing() {
        return params.getDoubleParameter(PARAMNAMES_FALSE_NORTHING);
    }

    public void setFalseNorthing(double val) {
        params.setParameter(PARAMNAMES_FALSE_NORTHING, val);
    }

    /**
     * Creates a new instance of AlbersNAD83
     */
    public newAlbersNAD83() throws FactoryException, 
                                   CannotCreateTransformException, 
                                   NoninvertibleTransformException {
        super();
        gcs = CSCreator.createNAD_83();
        params = csf.createProjectionParameterList(pcsClassification);
        Ellipsoid ellipse = gcs.getHorizontalDatum().getEllipsoid();
        params.setParameter("semi_major",ellipse.getSemiMajorAxis());
        params.setParameter("semi_minor",ellipse.getSemiMinorAxis());
        params.setParameter("false_easting",0.0);
        params.setParameter("false_northing",0.0);
        //default values for EBS
        params.setParameter("central_meridian",-154.0);
        params.setParameter("latitude_of_origin",50.0);
        params.setParameter("standard_parallel_1",55.0);
        params.setParameter("standard_parallel_2",65.0);
        createObjects();
    }

    @Override
    public Object clone() {
        newAlbersNAD83 clone = null;
        try {
            clone = (newAlbersNAD83) super.clone();
            clone.setParameters(params);
            clone.propertySupport = new PropertyChangeSupport(clone);
        } catch (FactoryException ex) {
            Logger.getLogger(newAlbersNAD83.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CannotCreateTransformException ex) {
            Logger.getLogger(newAlbersNAD83.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoninvertibleTransformException ex) {
            Logger.getLogger(newAlbersNAD83.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return clone;
    }

    @Override
    protected void createObjects() throws FactoryException,
                                          CannotCreateTransformException,
                                          NoninvertibleTransformException {
            Projection projection = csf.createProjection(pcsName,pcsClassification,params);
            pcs = csf.createProjectedCoordinateSystem(
                    pcsName,
                    gcs,
                    projection,
                    linearUnit,
                    AxisInfo.X,
                    AxisInfo.Y);

            mt  = CSCreator.getTransformation(gcs,pcs).getMathTransform();
            mtinv = mt.inverse();
    }

    @Override
    /**
     * Return names of all parameter associated with the CS.
     * @return - string array of all parameter names for CS
     */
    public String[] getParameterNames() {
        return paramNames;
    }

    /**
     * Gets the classication of the Projected Coordinate System
     * @return - String containing the classification of the PCS
     */
    public String getClassification() {
        return pcsClassification;
    }

    /**
     * Gets the name of the Projected Coordinate System
     * @return - String containing the name of the PCS
     */
    public String getName() {
        return pcsName;
    }

}
