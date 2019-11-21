/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.gis;

import org.geotools.feature.FeatureType;
import org.geotools.styling.Style;

/**
 *
 * @author William.Stockhausen
 */
public interface MapDataInterfaceScalarBase {

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
