/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.gis;

import com.vividsolutions.jts.geom.GeometryFactory;
import org.geotools.ct.MathTransform;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import wts.models.utilities.DateTime;
import wts.roms.model.GlobalInfo;
import wts.roms.model.Interpolator3D;
import wts.roms.model.ModelData;

/**
 *
 * @author William.Stockhausen
 */
public abstract class AbstractMapDataScalar3D implements MapDataInterfaceScalar3D {
    
    protected GlobalInfo romsGI;
    
    protected Interpolator3D i3d;
    protected ModelData mask = null;
    
    protected DateTime date = null;
    protected FeatureCollection fc = null;
    protected FeatureType ft = null;
    protected GeometryFactory gf = null;
    protected double max = Double.NEGATIVE_INFINITY;
    protected double min = Double.POSITIVE_INFINITY;
    protected MathTransform mt = null; //gt2.1-

    protected AbstractMapDataScalar3D() {
        romsGI = GlobalInfo.getInstance();
    }

    /**
     * Returns the FeatureType.
     * @return 
     */
    @Override
    public FeatureType getFeatureType() {
        return ft;
    }

    /**
     * Returns the maximum value associated with scaling the FeatureCollection.
     * @return 
     */
    @Override
    public double getMax() {
        return max;
    }

    /**
     * Returns the minimum value associated with scaling the FeatureCollection.
     * @return 
     */
    @Override
    public double getMin() {
        return min;
    }
    
}
