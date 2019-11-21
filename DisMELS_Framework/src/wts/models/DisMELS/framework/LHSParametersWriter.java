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
import java.util.Map;

/**
 *
 * @author William Stockhausen
 */
public class LHSParametersWriter {
    
    /**
     * Creates a new instance of InitialAttributesWriter
     */
    private LHSParametersWriter() {
    }
    
    public static void writeParametersFile(
            String fn,Map<String,LifeStageParametersInterface> map) throws IOException {
        File file = new File(fn);
        writeParametersFile(file,map);
    }
    
    public static void writeParametersFile(
            File file,Map<String,LifeStageParametersInterface> map) throws IOException {
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);
        //write parameter info for all defined parameter types
        LifeStageParametersInterface pLHS;
        Iterator<String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            pLHS = map.get(keys.next());
            if (pLHS!=null) {
                pw.println(pLHS.getCSVHeader());
                pw.println(pLHS.getCSV());
            }
        }
        pw.close();
    }
}
