/*
 * NetcdfReader.java
 *
 * Created on December 1, 2005, 2:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.model;

import java.io.IOException;
import javax.swing.JOptionPane;
import ucar.ma2.Index;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Attribute;
import ucar.nc2.Group;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;
import wts.models.utilities.CalendarGregorian;
import wts.models.utilities.CalendarIF;
import wts.models.utilities.CalendarJulian;
import wts.models.utilities.DateTime;

/**
 *
 * @author William Stockhausen
 */
public class NetcdfReader implements INetcdfReader {
    
    private final CriticalModelVariablesInfo cvi;
    NetcdfDataset ds = null;
    Array ocean_time = null;
    Index ix = null;
    
    /** Creates a new instance of NetcdfReader */
    public NetcdfReader() {
        cvi = GlobalInfo.getInstance().getCriticalModelVariablesInfo();
    }

    public NetcdfReader(String fn) throws IOException {
        cvi = GlobalInfo.getInstance().getCriticalModelVariablesInfo();
        setNetcdfDataset(fn);
    }
    
    public NetcdfReader(NetcdfDataset dsp) throws IOException {
        cvi = GlobalInfo.getInstance().getCriticalModelVariablesInfo();
        setNetcdfDataset(dsp);
    }
    
    private void extractVariables() throws IOException {
        Variable v = ds.findVariable(cvi.getNameInROMSDataset("ocean_time"));
        if (v!=null) {
            ocean_time = new Array(v.read());
            ix = ocean_time.getIndex();
        } else {
            ocean_time = null;
            ix = null;
        }
    }
    
    /**
     * Extracts the variable associated with the given varname in the netcdf 
     * dataset.
     * @param varname - variable name in the dataset 
     * @return 
     */
    @Override
    public ucar.nc2.Variable findVariable(String varname) {
        Variable v = ds.findVariable(varname);
        return v;
    }
    
    /**
     * Returns the model field corresponding to the netcdf file variable varname.
     * It's internal name will be 'name'.
     * @param varname
     * @param name
     * @return
     * @throws IOException 
     */
    @Override
    public ModelData getModelData(String varname, String name) throws IOException {
        ModelData md = null;
        try {
            Variable v = ds.findVariable(varname);
            if (v==null) {
                System.out.println("WARNING: Attempted to read nonexistent netcdf var '"+
                                   " associated with '"+varname+"'.");
                return null;
            }
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
                if (dimNames[j].startsWith(".*time.*")) timeIndex = j;
                if (dimNames[j].startsWith("s"))    sIndex    = j;
                if (dimNames[j].startsWith("eta"))  etaIndex  = j;
                if (dimNames[j].startsWith("xi"))   xiIndex   = j;
            }
            //set the origin and shape arrays with which to extract data
            //from the variable.
            int[] shp = v.getShape();
            int[] origin = shp.clone();
            for (int j=0;j<r;j++) {
                origin[j] = 0;
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
            //Create the ModelData object using the "internal" name
            md = new ModelData(a, name);
            //Set the index types
            md.setDimIndices(timeIndex,sIndex,etaIndex,xiIndex);
        } catch (InvalidRangeException ex) {
            ex.printStackTrace();
        }
        return md;
    }
    
    /**
     *iTime = 0-based index into time index
     * @param iTime   - time index
     * @param varname - netcdf file name
     * @param name    - internal name used to identify 
     * @return ModelData object
     * @throws java.io.IOException 
     */
    @Override
    public ModelData getModelData(int iTime, String varname, String name) throws IOException {
        ModelData md = null;
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
                    if (j==timeIndex) {
                        shp[j] = 1;
                        origin[j] = iTime;
                    }
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
                //Create the ModelData object
                md = new ModelData(ocean_time.getDouble(ix.set(iTime)), a, name);//'name' is the internal name
                //Set the index types
                md.setDimIndices(timeIndex,sIndex,etaIndex,xiIndex);
            }
        } catch (InvalidRangeException ex) {
            ex.printStackTrace();
        }
        return md;
    }

    /**
     * Gets the MaskData object associated with the netcdf file variable 'varname'.
     * 
     * @param varname - netcdf file variable name
     * @param name    - internal name used to identify object
     * @return
     * @throws IOException 
     */
    @Override
    public MaskData getMaskData(String varname, String name) throws IOException {
        MaskData md = null;
        try {
            Variable v = ds.findVariable(varname);
            if (v==null) {
                System.out.println("WARNING: Attempted to read nonexistent netcdf var "+varname);
                return null;
            }
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
                if (dimNames[j].startsWith(".*time.*")) timeIndex = j;
                if (dimNames[j].startsWith("s"))    sIndex    = j;
                if (dimNames[j].startsWith("eta"))  etaIndex  = j;
                if (dimNames[j].startsWith("xi"))   xiIndex   = j;
            }
            //set the origin and shape arrays with which to extract data
            //from the variable.
            int[] shp = v.getShape();
            int[] origin = shp.clone();
            for (int j=0;j<r;j++) {
                origin[j] = 0;
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
            //Create the ModelData object
            md = new MaskData(a, name);//using the internal name for future identification
            //Set the index types
            md.setDimIndices(timeIndex,sIndex,etaIndex,xiIndex);
        } catch (InvalidRangeException ex) {
            ex.printStackTrace();
        }
        return md;
    }

    @Override
    public int getNumTimeSteps() {
        return ocean_time.getShape()[0];
    }

    @Override
    public Array getOceanTime() {
        return ocean_time;
    }
    
    @Override
    public double getOceanTime(int iTime) throws java.lang.ArrayIndexOutOfBoundsException {
        double ot = -1;
        if (ocean_time!=null) ot = ocean_time.getDouble(ix.set(iTime));
        return ot;
    }
    
    @Override
    public CalendarIF getCalendar() {
        String grgCal = "gregorian";
        String julCal = "modified Julian day number";
        String stdCal = "standard";
        DateTime dt = null;
        CalendarIF mCal = null;
        Variable v = ds.findVariable(cvi.getNameInROMSDataset("ocean_time"));
        if (v!=null) {
            Attribute units = v.findAttribute("units");
            Attribute cal = v.findAttribute("calendar");
            if (units!=null) 
                dt = DateTime.parseDateTime(GlobalInfo.getInstance().getRefDateString());//use as default
            if ((cal!=null)) {
                if (cal.isString()){
                    if (cal.getStringValue().equalsIgnoreCase(julCal)) {
                        //calendar type is modified Julian day number
                        mCal = new CalendarJulian();
                    } else if (cal.getStringValue().equalsIgnoreCase(grgCal)) {
                        mCal = new CalendarGregorian();
                    } else if (cal.getStringValue().equalsIgnoreCase(stdCal)) {
                        //standard calendar type is ASSUMED to be modified Julian day number
                        //TODO: update this!!
                        mCal = new CalendarJulian();
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Calendar type "+cal.getStringValue()+" not recognized",
                                "Error reading netCDF file",JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                }
            } else {
                mCal = new CalendarJulian();//default
            }
            mCal.setRefDate(dt);
        }
        return mCal;
    }
    
    @Override
    public String[] getVariableNames() {
        Group g = ds.getRootGroup();
        java.util.List vl = g.getVariables();
        int size = vl.size();
        String[] str= new String[size];
        java.util.ListIterator li = vl.listIterator();
        Variable v = null;
        int i=0;
        while (li.hasNext()) {
            v = ((Variable) li.next());
            str[i++] = v.getName();
        }
        return str;
    }
    
    @Override
    public String getFilename() {
        return ds.getLocation();
    }
    
    @Override
    public ucar.nc2.dataset.NetcdfDataset getNetcdfDataset() {
        return ds;
    }
    
    /**
     * Reads the value of the scalar double variable identified by the netcdf name. 
     * 
     * @param name - name of variable in netcdf file
     * @return     - the value of variable as a double
     * @throws IOException 
     */
    @Override
    public double readScalarDouble(String name) throws IOException {
        double x = 0;
        Variable v = ds.findVariable(name);
        if (v!=null) x = v.readScalarDouble(); else throw new IOException();
        return x;
    }
    
    @Override
    public void setNetcdfDataset(NetcdfDataset nds) throws IOException {
        ds = nds;
        extractVariables();
    }
    
    @Override
    public void setNetcdfDataset(String fn) throws IOException {
        ds = NetcdfDataset.openDataset(fn);
        extractVariables();
    }
}
