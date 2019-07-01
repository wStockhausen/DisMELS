/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.HSMs;

import java.io.IOException;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Group;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;
import wts.roms.model.Array;
import wts.roms.model.ModelData;

/**
 *
 * @author William Stockhausen
 */
public class HSMap_NetCDF implements HSMapInterface {

    String conn = null;
    NetcdfDataset ds = null;
    
    public HSMap_NetCDF(){
        
    }
    
    @Override
    public String getConnectionString() {
        return(conn);
    }

    @Override
    public boolean setConnectionString(String conn) {
        try{
            ds = NetcdfDataset.openDataset(conn);
            return true;
        } catch (IOException ex) {
            //TOO: do something here!
        }
        return false;
    }

    @Override
    public Object calcValue(double[] pos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object calcValue(double[] pos, Object xtra) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public String[] getVariableNames() {
        Group g = ds.getRootGroup();
        java.util.List<Variable> vl = g.getVariables();
        java.util.ListIterator<Variable> li = vl.listIterator();
        String[] str= new String[vl.size()];
        Variable v = null;
        int i=0;
        while (li.hasNext()) {
            v = ((Variable) li.next());
            str[i++] = v.getName();
        }
        return str;
    }
    
    protected void extractHSM() throws IOException {
        String varname = null;
        try {
            Variable v = ds.findVariable(varname);
            if (v!=null) {
                //rank of underlying data array
                int r = v.getRank(); 
                //get the variable dimension names & determine indices
                String[] dimNames = new String[r];
                int timeIndex = -1;
                int sIndex    = -1;
                int etaIndex  = -1;
                int xiIndex   = -1;
                for (int j=0;j<r;j++) {
                    dimNames[j] = v.getDimension(j).getName();
                    if (dimNames[j].matches(".*time.*")) timeIndex = j;//has 'time' in the name
                    if (dimNames[j].startsWith("s"))    sIndex    = j;
                    if (dimNames[j].startsWith("eta"))  etaIndex  = j;
                    if (dimNames[j].startsWith("xi"))   xiIndex   = j;
                }
                //set the origin and shape arrays with which to extract data
                //from the variable.
                int[] shp = v.getShape();
                int[] origin = (int[]) shp.clone();
                for (int j=0;j<r;j++) {
                    origin[j] = 0;
//                    if (j==timeIndex) {
//                        shp[j] = 1;
//                        origin[j] = iTime;
//                    }
                }
                //Extract the time-sliced array from the variable.
                //Note that the array rank remains the same as the 
                //original.
                Array a = new Array(v.read(origin,shp));
                //Set the index names for the array to 
                //the dimension names from the variable
                for (int j=0;j<r;j++){
                    String dm = v.getDimension(j).getName();
                    a.setIndexName(j,dm);
                }
            }
        } catch (InvalidRangeException ex) {
            ex.printStackTrace();
        }
    }
}
