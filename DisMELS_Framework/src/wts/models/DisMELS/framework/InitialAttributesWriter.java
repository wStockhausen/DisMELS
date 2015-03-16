/*
 * InitialAttributesWriter.java
 *
 * Created on January 10, 2006, 8:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.framework;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;

/**
 *
 * @author William Stockhausen
 */
public class InitialAttributesWriter {
    
    private static LHSPointFeatureType ft;
    
    /**
     * Creates a new instance of InitialAttributesWriter
     */
    public InitialAttributesWriter() {
    }
    
    public LHSPointFeatureType getFeatureType() {
        return ft;
    }
    
    public void setFeatureType(LHSPointFeatureType ftip) {
        ft = ftip;
    }
    
    public void writePositionFile(String fn,FeatureCollection fc) throws IOException {
        File file = new File(fn);
        writePositionFile(file,fc);
    }
    
    public void writePositionFile(File file,FeatureCollection fc) throws IOException {
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);
        //write header info
        String str = ft.getFeatureHeader();
        pw.println(str);
        //get features and write position info
        Feature f;
        Iterator it = fc.iterator();
        while(it.hasNext()) {
            f = (Feature) it.next();
            str = ft.FeatureToCSV(f);
            pw.println(str);
        }
        pw.close();
    }
}
