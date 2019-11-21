/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.gis;

import java.io.IOException;
import java.net.MalformedURLException;
import org.geotools.factory.FactoryConfigurationError;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author William.Stockhausen
 */
public interface MapDataInterfaceScalar3D extends MapDataInterfaceScalarBase {

    /**
     * Creates and returns a FeatureCollection for the 3D field sliced at
     * vertical index k.
     * @param k
     * @return FeatureCollection.
     * @throws org.geotools.feature.SchemaException
     * @throws org.geotools.feature.IllegalAttributeException
     * @throws org.opengis.referencing.operation.TransformException
     */
    FeatureCollection createFeatureCollection(int k) throws SchemaException, IllegalAttributeException, TransformException;

    /**
     * Returns a FeatureCollection for the field interpolated to depth z
     * @param z - depth (m; >0) at which to interpolate field
     * @return FeatureCollection.
     * @throws org.geotools.feature.SchemaException
     * @throws org.geotools.feature.IllegalAttributeException
     * @throws org.opengis.referencing.operation.TransformException
     */
    FeatureCollection createFeatureCollection(double z) throws SchemaException, IllegalAttributeException, TransformException;

    /**
     * Exports a constant k-sliced plane of the field to a shapefile.
     *
     * @param k       - integer; depth index at which to slice 3D field
     * @param shpFile - String;  name of shapefile to export to
     * @param add     - boolean; flag to create new (false) or add to existing (true) shapefile
     *
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     * @throws org.geotools.factory.FactoryConfigurationError
     * @throws org.geotools.feature.SchemaException
     * @throws org.geotools.feature.IllegalAttributeException
     * @throws org.opengis.referencing.operation.TransformException
     */
    void exportToShapefile(int k, String shpFile, boolean add) throws MalformedURLException, IOException, FactoryConfigurationError, SchemaException, IllegalAttributeException, TransformException;

    /**
     * Exports a constant depth-sliced plane of the vector field to a point shapefile.
     * Shapefile fields are date (as String), z (depth), speed, and angle.
     *
     * @param z       -  double; depth (<0 m) at which to slice 3D field
     * @param shpFile - String;  name of shapefile to export to
     * @param add     - boolean; flag to create new (false) or add to existing (true) shapefile
     *
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     * @throws org.geotools.factory.FactoryConfigurationError
     * @throws org.geotools.feature.SchemaException
     * @throws org.geotools.feature.IllegalAttributeException
     * @throws org.opengis.referencing.operation.TransformException
     */
    void exportToShapefile(double z, String shpFile, boolean add) throws MalformedURLException, IOException, FactoryConfigurationError, SchemaException, IllegalAttributeException, TransformException;
    
}
