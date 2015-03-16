/*
 * InitialAttributesReader.java
 *
 * Created on January 24, 2006, 9:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.framework;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;

/**
 *
 * @author William Stockhausen
 */
public class InitialAttributesReader {
    
    /**
     * Creates a new instance of InitialAttributesReader
     */
    public InitialAttributesReader() {
    }
    
     public FeatureCollection readAttributesFile(String fn) 
            throws IOException, InstantiationException, IllegalAccessException {
        File file = new File(fn);
        return readAttributesFile(file);
    }
    
   public FeatureCollection readAttributesFile(File file) 
            throws IOException, InstantiationException, IllegalAccessException {
        FeatureCollection fc  = FeatureCollections.newCollection();
        List<LifeStageAttributesInterface> lhs = LHS_Factory.createAttributesFromCSV(file);
        Iterator<LifeStageAttributesInterface> it = lhs.iterator();
        while (it.hasNext()) {
            Feature f = LHS_Factory.createPointFeature(it.next());
            fc.add(f);
        }
        return fc;
    }
}
