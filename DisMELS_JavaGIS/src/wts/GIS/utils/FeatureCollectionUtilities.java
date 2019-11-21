/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.GIS.utils;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;

/**
 *
 * @author wstockhausen
 */
public class FeatureCollectionUtilities {
    /**
     * Find min, max of attribute "attr" in feature collection "fc"
     * @param attr - name of attribute in feature collection to search
     * @param fc   - feature collection to search
     * @return - double[2] with {min, max}
     */
    static public double[] findMinMax(String attr, FeatureCollection fc) {
        double[] mm = {Double.POSITIVE_INFINITY,Double.NEGATIVE_INFINITY};//min, max
        double val;
        FeatureIterator fi = fc.features();
        Feature f;
        while (fi.hasNext()) {
            f = fi.next();
            val = ((Double) f.getAttribute(attr)).doubleValue();
            if (val<mm[0]) mm[0] = val;
            if (mm[1]<val) mm[1] = val;
        }
        fi.close();
        return mm;
    }
}
