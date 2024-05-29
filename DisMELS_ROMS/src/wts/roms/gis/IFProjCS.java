/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.roms.gis;

import java.io.Serializable;
import javax.media.jai.ParameterList;
import org.geotools.cs.GeographicCoordinateSystem;
import org.geotools.cs.ProjectedCoordinateSystem;
import org.geotools.ct.CannotCreateTransformException;
import org.geotools.ct.MathTransform;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.NoninvertibleTransformException;

/**
 *
 * @author wstockhausen
 */
public interface IFProjCS extends Serializable, Cloneable{
    
    /**
     * Gets the classication of the Projected Coordinate System
     * @return - String containing the classification of the PCS
     */
    public String getClassification();

    /**
     * Gets the name of the Projected Coordinate System
     * @return - String containing the name of the PCS
     */
    public String getName();

    /**
     * Returns the underlying geographic coordinate system object.
     * @return org.geotools.cs.GeographicCoordinateSystem object
     */
    public GeographicCoordinateSystem getGeoCS();

    /**
     * Returns the underlying math transform object for the transformation
     * from geographic to projected coordinates.
     * @return org.geotools.cs.MathTransform object
     */
    public MathTransform getGtoPtransform();

    /**
     * Returns the underlying projected coordinate system object.
     * @return org.geotools.cs.ProjectedCoordinateSystem object
     */
    public ProjectedCoordinateSystem getProjCS();

    /**
     * Return names of all parameter associated with the CS.
     * @return - string array of all parameter names for CS
     */
    public String[] getParameterNames();

    /**
     * Returns a COPY of the CS parameter list.
     * @return - copy of the parameter list for the CS.
     */
    public javax.media.jai.ParameterList getParameters();

    /**
     * Implementing functions should update the values
     * coresponding to the input parameters.
     * @param pl - javax.media.jai.ParameterList of values to be changed
     */
    public void setParameters(ParameterList pl) throws FactoryException,
                                                       CannotCreateTransformException,
                                                       NoninvertibleTransformException;

    public Object getParameterValue(String name);

    public void setParameterValue(String name,Object val);

    public Double getParameterDoubleValue(String name);

    public void setParameterValue(String name, double val);

    /**
     * Transform a point from geographic to projected CS.
     * @param src - double array of source coordinates (2d or 3d)
     * @return    - transformed (NAD83) point
     */
    public double[] transformGtoP(double[] src);

    /**
     * Transform points from geographic to projected CS.
     *
     * @param srcPts - double array of source (geo) points, with coordinates
     * packed: {x1,y1,z1,x2,y2,z2}
     * @param i      - offset into srcPts array at which to start
     * @param dstPts - double array of destination (projected) points
     * @param i0     - offset into dstPts array at which to start recording results
     * @param i1     - no. of points to transform
     */
    public void transformGtoP(double[] srcPts, int i, double[] dstPts, int i0, int i1);

    /**
     * Returns the underlying math transform object for the transformation
     * from projected to geographic coordinates.
     * @return org.geotools.cs.MathTransform object
     */
    public MathTransform getPtoGtransform();


    /**
     * Transform a point from projected to geographic CS.
     * @param src - double array of source coordinates (2d or 3d)
     * @return    - transformed (NAD83) point
     */
    public double[] transformPtoG(double[] src);

    /**
     * Transform points from projected to geographic CS.
     *
     * @param srcPts - double array of source (geo) points, with coordinates
     * packed: {x1,y1,z1,x2,y2,z2}
     * @param i      - offset into srcPts array at which to start
     * @param dstPts - double array of destination (projected) points
     * @param i0     - offset into dstPts array at which to start recording results
     * @param i1     - no. of points to transform
     */
    public void transformPtoG(double[] srcPts, int i, double[] dstPts, int i0, int i1);

}
