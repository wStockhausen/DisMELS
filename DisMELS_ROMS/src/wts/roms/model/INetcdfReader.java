/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.roms.model;

import java.io.IOException;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;
import wts.models.utilities.CalendarIF;

/**
 *
 * @author wstockhausen
 */
public interface INetcdfReader {

    Variable findVariable(String varname);

    CalendarIF getCalendar();

    String getFilename();

    /**
     * @param varname - netcdf name
     * @param name    - internal name
     * @return ModelData object
     * @throws java.io.IOException
     */
    ModelData getModelData(String varname, String name) throws IOException;

    /**
     *iTime = 0-based index into time index
     * @param iTime   - time index
     * @param iL - vertical layer index
     * @param varname - netcdf file name
     * @param name    - internal name used to identify 
     * @return ModelData object
     * @throws java.io.IOException 
     */
    public ModelData getModelData(int iTime, int iL, String varname, String name) throws IOException;
    
    /**
     * @param varname - netcdf name
     * @param name    - internal name
     * @return ModelData object
     * @throws java.io.IOException
     */
    MaskData getMaskData(String varname, String name) throws IOException;

    /**
     * iTime = 0-based index into time index
     * @param iTime   - time index into field
     * @param varname - netcdf name
     * @param name    - internal name
     * @return ModelData object
     * @throws java.io.IOException
     */
    ModelData getModelData(int iTime, String varname, String name) throws IOException;

    NetcdfDataset getNetcdfDataset();

    int getNumTimeSteps();

    Array getOceanTime();

    double getOceanTime(int iTime) throws ArrayIndexOutOfBoundsException;

    /**
     * Gets a String[] of variable names in the netcdf file being read.
     * 
     * @return 
     */
    String[] getVariableNames();

    /**
     * Reads the value of a scalar double associated with the netcdf file variable
     * identified by 'varname'.
     * @param name
     * @return
     * @throws IOException 
     */
    double readScalarDouble(String name) throws IOException;

    void setNetcdfDataset(NetcdfDataset nds) throws IOException;

    void setNetcdfDataset(String fn) throws IOException;

}
