/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.gis;

import com.vividsolutions.jts.geom.GeometryFactory;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import org.geotools.ct.MathTransform;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import wts.GIS.shapefile.ShapefileCreator;
import wts.models.utilities.DateTime;
import wts.roms.model.GlobalInfo;
import wts.roms.model.ModelData;

/**
 *
 * @author William.Stockhausen
 */
public abstract class AbstractMapDataScalar2D implements MapDataInterfaceScalar2D {
    
    protected GlobalInfo romsGI;
    protected ModelData mask = null;
    
    protected DateTime date = null;
    protected FeatureCollection fc = null;
    protected FeatureType ft = null;
    protected GeometryFactory gf = null;
    protected double max = Double.NEGATIVE_INFINITY;
    protected double min = Double.POSITIVE_INFINITY;
    protected MathTransform mt = null; //gt2.1-

    protected AbstractMapDataScalar2D() {
        romsGI = GlobalInfo.getInstance();
    }

    /**
     * Exports the FeatureCollection to the given shapefile.
     * @param shpFile
     * @throws MalformedURLException
     * @throws IOException 
     */
    public void exportFeatureCollection(String shpFile) throws MalformedURLException, IOException {
        if (fc!=null) {
            ShapefileCreator sc = new ShapefileCreator();
            URI uri = (new File(shpFile)).toURI();
            sc.setShapefileURL(uri.toURL());
            sc.createShapefile(fc);
        }
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
