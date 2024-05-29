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
public interface MapDataInterfaceScalar2D extends MapDataInterfaceScalarBase {

    /**
     * Creates and returns a FeatureCollection for a 2D field.
     * @return FeatureCollection.
     * @throws org.geotools.feature.SchemaException
     * @throws org.geotools.feature.IllegalAttributeException
     * @throws org.opengis.referencing.operation.TransformException
     */
    FeatureCollection createFeatureCollection() throws SchemaException, IllegalAttributeException, TransformException;

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
    
}
