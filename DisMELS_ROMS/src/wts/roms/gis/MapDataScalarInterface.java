/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.gis;

import java.io.IOException;
import java.net.MalformedURLException;
import org.geotools.factory.FactoryConfigurationError;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.styling.Style;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author William.Stockhausen
 */
public interface MapDataScalarInterface {

    /**
     * Creates and returns a FeatureCollection for a 2D field.
     * @return FeatureCollection.
     */
    FeatureCollection createFeatureCollection() throws SchemaException, IllegalAttributeException, TransformException;

    /**
     * Creates and returns a FeatureCollection for the 3D field sliced at
     * vertical index k.
     * @return FeatureCollection.
     */
    FeatureCollection createFeatureCollection(int k) throws SchemaException, IllegalAttributeException, TransformException;

    /**
     * Returns a FeatureCollection for the field interpolated to depth z
     * @param z - depth (m; >0) at which to interpolate field
     * @return FeatureCollection.
     */
    FeatureCollection createFeatureCollection(double z) throws SchemaException, IllegalAttributeException, TransformException;

    /**
     * Exports the FeatureCollection to the given shapefile.
     * @param shpFile
     * @throws MalformedURLException
     * @throws IOException
     */
    void exportFeatureCollection(String shpFile) throws MalformedURLException, IOException;

    /**
     * Exports the 2D scalar field to a polygon shapefile.
     * Shapefile fields are ID, xi, eta, date (as String), value.
     *
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
    void exportToShapefile(String shpFile, boolean add) throws MalformedURLException, IOException, FactoryConfigurationError, SchemaException, IllegalAttributeException, TransformException;

    /**
     * Exports a constant k-sliced plane of the field to ashapefile.
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

    /**
     * Returns the FeatureType.
     * @return
     */
    FeatureType getFeatureType();

    /**
     * Returns the maximum value associated with scaling the FeatureCollection.
     * @return
     */
    double getMax();

    /**
     * Returns the minimum value associated with scaling the FeatureCollection.
     * @return
     */
    double getMin();

    /**
     * Returns the user-friendly name of the MapData.
     * @return
     */
    String getName();

    /**
     * Returns the geotools.styling.Style associated with the MapData.
     * @return
     */
    Style getStyle();

    /**
     * Sets the Style.
     * @param s
     */
    void setStyle(Style s);
    
}
