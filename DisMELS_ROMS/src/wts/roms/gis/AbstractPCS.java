/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.roms.gis;

import java.beans.PropertyChangeSupport;
import javax.media.jai.ParameterList;
import javax.media.jai.ParameterListDescriptor;
import org.geotools.cs.CoordinateSystemFactory;
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
public abstract class AbstractPCS implements IFProjCS {

    public static String PROP_PARAMS = "parameters";
    protected static final CoordinateSystemFactory csf = CoordinateSystemFactory.getDefault();

    protected ParameterList params = null;

    protected GeographicCoordinateSystem gcs = null;
    protected ProjectedCoordinateSystem  pcs = null;
    protected MathTransform mt = null;
    protected MathTransform mtinv = null;

    /**
     * Utility field used by bound properties.
     */
    protected PropertyChangeSupport propertySupport = null;

    /**
     * Protected constructor should be called by all constructors of subclasses.
     */
    protected AbstractPCS() {
        propertySupport = new PropertyChangeSupport(this);
    }

    /**
     * A concrete implementation of this abstract function must be provided
     * in concrete subclasses.  Implementing functions should create gcs, pcs,
     * mt and mtinv.
     */
    protected abstract void createObjects() throws FactoryException,
                                                   CannotCreateTransformException,
                                                   NoninvertibleTransformException;

    /**
     *  This method should be overridden by extending classes.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Object getParameterValue(String name) {
        return params.getObjectParameter(name);
    }

    public void setParameterValue(String name,Object val) {
        params.setParameter(name, val);
    }

    public Double getParameterDoubleValue(String name) {
        return params.getDoubleParameter(name);
    }

    public void setParameterValue(String name, double val) {
        params.setParameter(name, val);
    }

    /**
     * Returns a COPY of the CS parameter list.
     * @return - copy of the parameter list for the CS.
     */
    public javax.media.jai.ParameterList getParameters(){
        ParameterListDescriptor pld = params.getParameterListDescriptor();
        javax.media.jai.ParameterList clone = new javax.media.jai.ParameterListImpl(pld);
        String[] names = pld.getParamNames();
        int np = names.length;
        for (int n=0;n<np;n++) {
            Object val = params.getObjectParameter(names[n]);
            clone.setParameter(names[n], val);
        }
        return clone;
    }

    /**
     * Implementing functions should update the values corresponding to the
     * input parameters.
     * @param pl - javax.media.jai.ParameterList of values to be changed
     */
    public void setParameters(ParameterList pl) throws FactoryException, 
                                                       CannotCreateTransformException,
                                                       NoninvertibleTransformException {
        ParameterListDescriptor pld = pl.getParameterListDescriptor();
        String[] names = pld.getParamNames();
        int np = names.length;
        for (int n=0;n<np;n++) {
            try {
                Object val = pl.getObjectParameter(names[n]);
                if (pld.isParameterValueValid(names[n], val)) {
                    params.setParameter(names[n], val);
                }
            } catch(IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
        createObjects();
        propertySupport.firePropertyChange(PROP_PARAMS,null,null);
    }

    /**
     * Return names of all set-able parameter associated with the CS.
     * @return - string array of all set-able parameter names for CS
     */
    public abstract String[] getParameterNames();

    /**
     * Returns the underlying geographic coordinate system object.
     * @return org.geotools.cs.GeographicCoordinateSystem object
     */
    public GeographicCoordinateSystem getGeoCS() {
        return gcs;
    }

    /**
     * Returns the underlying projected coordinate system object.
     * @return org.geotools.cs.ProjectedCoordinateSystem object
     */
    public ProjectedCoordinateSystem getProjCS() {
        return pcs;
    }

    /**
     * Returns the underlying math transform object for the transformation
     * from geographic to projected coordinates.
     * @return org.geotools.cs.MathTransform object
     */
    public MathTransform getGtoPtransform() {
        return mt;
    }

    /**
     * Returns the underlying math transform object for the transformation
     * from projected to geographic coordinates.
     * @return org.geotools.cs.MathTransform object
     */
    public MathTransform getPtoGtransform() {
        return mtinv;
    }


    /**
     *  Transform a point from geographic (NAD83) to projected (Albers).  If the
     *  source point is 3d, the x,y coordinates are transformed and the z coordinate
     *  is simply copied.
     *  @param src - double array of source coordinates (2d or 3d)
     *  @return    - transformed (NAD83) point
     */
    public double[] transformGtoP(double[] src){
        double[] dest = new double[src.length];
        transformGtoP(src,0,dest,0,1);
        if (src.length>2) dest[2] = src[2]; //copy z dimension
        return dest;
    }

    /**
     *  Transform a point from projected (Albers) to geographic (NAD83).  If the
     *  source point is 3d, the x,y coordinates are transformed and the z coordinate
     *  is simply copied.
     *  @param src - double array of source coordinates (2d or 3d)
     *  @return    - transformed (NAD83) point
     */
    public double[] transformPtoG(double[] src){
        double[] dest = new double[src.length];
        transformPtoG(src,0,dest,0,1);
        if (src.length>2) dest[2] = src[2]; //copy z dimension
        return dest;
    }

    /**
     *  Transform points from geographic (NAD83) to projected (Albers).  Note
     *  that srcPts and dstPts can be the same array.  Dimension of points is
     *  determined by the coordinate systems on which the transform is based.
     *  I THINK the NAD83<->Albers CS's defined herein have dimension 2.  If you
     *  have 3d coordinates, the transform "works" if you only transform 1 point,
     *  but the destination z value is 0.
     *
     *  @param srcPts - double array of source (geo) points, with coordinates
     *                  packed: {x1,y1,z1,x2,y2,z2}
     *  @param i      - offset into srcPts array at which to start
     *  @param dstPts - double array of destination (projected) points
     *  @param i0     - offset into dstPts array at which to start recording results
     *  @param i1     - no. of points to transform
     */
    public void transformGtoP(double[] srcPts, int i, double[] dstPts, int i0, int i1) {
        try {
            mt.transform(srcPts,i,dstPts,i0,i1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *  Transform points from projected (Albers) to geographic (NAD83).  Note
     *  that srcPts and dstPts can be the same array.  Dimension of points is
     *  determined by the coordinate systems on which the transform is based.
     *  I THINK the NAD83<->Albers CS's defined herein have dimension 2.  If you
     *  have 3d coordinates, the transform "works" if you only transform 1 point,
     *  but the destination z value is 0.
     *
     *  @param srcPts - double array of source (geo) points, with coordinates
     *                  packed: {x1,y1,z1,x2,y2,z2}
     *  @param i      - offset into srcPts array at which to start
     *  @param dstPts - double array of destination (projected) points
     *  @param i0     - offset into dstPts array at which to start recording results
     *  @param i1     - no. of points to transform
     */
    public void transformPtoG(double[] srcPts, int i, double[] dstPts, int i0, int i1) {
        try {
            mtinv.transform(srcPts,i,dstPts,i0,i1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertySupport.addPropertyChangeListener(l);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertySupport.removePropertyChangeListener(l);
    }
}
