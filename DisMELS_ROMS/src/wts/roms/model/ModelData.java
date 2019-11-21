/*
 * ModelData.java
 *
 * Created on November 30, 2005, 3:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *TODO: Make sure data is scaled correctly!
 */
package wts.roms.model;

import ucar.ma2.Index;
import ucar.ma2.IndexIterator;

/**
 *
 * @author William Stockhausen
 */
public class ModelData {
    private static boolean debug = false;
    
    private String name="";
    
    protected boolean timedependent = false;
    protected double ocean_time;
    
    protected Array a;
    protected Index ix;
    /** the rank of the Array a */
    protected int r;
    protected int[] shp;
    
    /** 
     * The rank of the field as encapsulated by the ModelData object.
     * This is returned by getRank().
     */
    protected int rank;
    
    protected double v;
    
    protected int vertPosType;
    protected int horzPosType;
    protected int sAdj   = 0;
    protected int etaAdj = 0;
    protected int xiAdj  = 0;
    
    protected int tIndex   = -1;
    protected int sIndex   = -1;
    protected int etaIndex = -1;
    protected int xiIndex  = -1;
    /**
     * Creates a new instance of ModelData
     */
    public ModelData() {
    }
    
    public ModelData(double[][] ap, String namep, int horzType, int vertType) {
        name = namep;
        a = ArrayDouble.factory(ap);
        ix = a.getIndex();
        r  = a.getRank();
        shp = a.getShape();
        horzPosType = horzType;
        vertPosType = vertType;
        setDimIndices(-1, -1, 0, 1);
        rank = 2;
    }
    
    public ModelData(Array ap, String namep) {
        a = ap;
        name = namep;
        ix = a.getIndex();
        r = a.getRank();
        shp = a.getShape();
        setPosTypes();
    }
    
    public ModelData(double t, Array ap, String namep) {
        a = ap;
        name = namep;
        ocean_time = t;
        timedependent = true;
        ix = a.getIndex();
        r = a.getRank();
        shp = a.getShape();
        setPosTypes();
    }
    
    /**
     * Creates a new ModelData object by linearly interpolating between 
     * two other ModelData objects (both must be time dependent, otherwise 
     * the operation doesn't make sense).
     * 
     * @param t time to interpolate to
     * @param md1 model data (at t0)
     * @param md2 model data (at t1)
     * @return interpolated data at t (or null if either md1 or md2 is 
     * not time-dependent)
     */
    public static ModelData timeInterpolate(double t,
                                              ModelData md1,
                                              ModelData md2) {
        //if input data is not time-dependent, don't interpolate
        if ((!md1.timedependent)||(!md2.timedependent)) {return null;}
        //if input data don't have the same shapes, don't interpolate
        if (md1.r!=md2.r) {return null;}
        
        //create new ModelData instance to hold interpolated values
        Array ap = md1.a.copy();
        for (int i=0;i<ap.getRank();i++) {
            ap.setIndexName(i,md1.a.getIndexName(i));
        }
        ModelData mdt = new ModelData(t,ap,md1.name);
        mdt.setDimIndices(md1.tIndex,md1.sIndex,md1.etaIndex,md1.xiIndex);
        
        //do simple linear interpolation between times 1 & 2 
        //for each array element
        float fac = (float)(md2.ocean_time - md1.ocean_time);
        float fac2 = ((float)(t-md1.ocean_time))/fac;
        float fac1 = ((float)(md2.ocean_time-t))/fac;
        IndexIterator imdt = mdt.a.getIndexIteratorFast();
        IndexIterator imd1 = md1.a.getIndexIteratorFast();
        IndexIterator imd2 = md2.a.getIndexIteratorFast();
        while (imdt.hasNext()) {
            imdt.setFloatNext(fac1*imd1.getFloatNext()+fac2*imd2.getFloatNext());
//            imdt.setDoubleNext(fac1*imd1.getDoubleNext()+fac2*imd2.getDoubleNext());
        }
        if (debug) {
            System.out.println("Interpolating ModeData "+md1.getName());
            System.out.println(t+", "+md1.ocean_time+", "+md2.ocean_time);
            System.out.println("fac,fac1,fac2 = "+fac+","+fac1+","+fac2);
            for (int s=3;s<=4;s++){
                System.out.println("s = "+s);
                for (int eta=20;eta<=30;eta++){
                    String str = eta+":  ";
                    for (int xi=20;xi<=30;xi++) {
                        str = str + mdt.getValue(xi,eta,s)+"= "+
                                    md1.getValue(xi,eta,s)+", "+
                                    md2.getValue(xi,eta,s)+";   ";
                    }
                System.out.println(str);
                }
            }
        }
        return mdt;
    }
    
    /**
     * tests whether underlying data is part of a time series.
     * @return True if data is part of time series. False otherwise.
     */
    public boolean isTimeDependent() {
        return timedependent;
    }
    
    public String[] getDimNames() {
        String[] str = new String[r];
        for (int i=0;i<r;i++) str[i] = a.getIndexName(i);
        return str;
    }
    
    public int getHorzPosType() {
        return horzPosType;
    }
   
    /**
     * Returns the max ROMS xi index, which
     * may be different from the java max index.
     *
     * @return--max ROMS xi index.
     */
    public int getL() {
        int N = -1;
        if (xiIndex < 0) {
            return N;  //return -1 indicating no xi dimension
        } else {
            shp = a.getShape();
            N = shp[xiIndex]-xiAdj-1;
        }
        return N;
    }
    
    /**
     * Returns the number of "interior grid points" along the xi axis 
     * (-1 if no xi axis).  This is 1 less than the max ROMS index, which
     * may be different from the java max index.
     * @return the number of "interior grid points" along the xi axis 
     *         (-1 if no xi axis).
     */
    public int getLm() {
        int N = -1;
        if (xiIndex < 0) {
            return N;  //return -1 indicating no xi dimension
        } else {
            shp = a.getShape();
            N = shp[xiIndex]-xiAdj-2;//it's -2 here because we want Lm=L-1
        }
        return N;
    }

    /**
     * Returns the max ROMS eta index, which
     * may be different from the java max index.
     *
     * @return--max ROMS eta index.
     */
    public int getM() {
        int N = -1;
        if (etaIndex < 0) {
            return N;  //return -1 indicating no eta dimension
        } else {
            shp = a.getShape();
            N = shp[etaIndex]-etaAdj-1;
        }
        return N;
    }
    
    /**
     * Returns the number of "interior grid points" along the eta axis 
     * (-1 if no eta axis).  This is 1 less than the max ROMS index, which
     * may be different from the java max index.
     * @return the number of "interior grid points" along the eta axis 
     *         (-1 if no eta axis).
     */
    public int getMm() {
        int N = -1;
        if (etaIndex < 0) {
            return N;  //return -1 indicating no eta dimension
        } else {
            shp = a.getShape();
            N = shp[etaIndex]-etaAdj-2;//it's -2 here because we want Mm=M-1
        }
        return N;
    }
    
    /**
     * Returns the number of grid cells in the s direction, which is also the
     * maximum ROMS index along the s axis (-1 if no s axis).
     * This may be different from the java max index.
     *
     * @return--number of grid cells in the s direction (-1 if no s axis).
     */
    public int getN() {
        int N = -1;
        if (sIndex < 0) {
            return N;  //return -1 indicating no s dimension
        } else {
            shp = a.getShape();
            N = shp[sIndex]-sAdj-1;//it's -1 here because we want N
        }
        return N;
    }
    
    public String getName() {
        return name;
    }

    public double getOceanTime() {
        return ocean_time;
    }
    
    /**
     * Gets the (real) rank of the model data.  This is NOT necessarily the same
     * as the rank of the underlying Array object.
     * 
     * @return array rank
     */
    public int getRank() {
        return rank;
    }
    
    /**
     * Gets the shape array for the model data.
     * @return shape array (int[])
     */
    public int[] getShape() {
        return a.getShape();
    }
    
    /**
     * Gets field value at specified indices.  Indices should be specified 
     * as ROMS FORTRAN indices, not as java indices. The ROMS indices are 
     * converted to the correct java indices internally based on the vertPosType
     * and the horzPosType of the model data array.
     * 
     * @param s ROMS index along the s direction (k or s/z)
     * @return field value at specified indices
     */
    public double getValue(int s) 
            throws java.lang.ArrayIndexOutOfBoundsException {
        try {
            //Adjust input ROMS FORTRAN indices to java 0-based indices
            s   = s+sAdj;
//            if ((s<0)) {
//                //String strp = name+": s = "+s;
//                //System.out.println(strp);
//                return -999.0;
//            }
            for (int i=0;i<r;i++) shp[i] = 0;
            shp[sIndex] = s;
            v = a.getDouble(ix.set(shp));
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            String str = "IndexOutOfBounds for "+name+": s = "+s;
            System.out.println(str);
            //ex.printStackTrace();
            throw ex;
        }
        return v;
    }
    
    /**
     * Gets field value at specified indices.  Indices should be specified 
     * as ROMS FORTRAN indices, not as java indices. The ROMS indices are 
     * converted to the correct java indices internally based on the vertPosType
     * and the horzPosType of the model data array.
     * @param xi ROMS index along the xi direction (i or x)
     * @param eta ROMS index along the eta direction (j or y)
     * @return field value at specified indices
     */
    public double getValue(int xi,int eta) 
            throws java.lang.ArrayIndexOutOfBoundsException {
        try {
            //Adjust input ROMS FORTRAN indices to java 0-based indices
            eta = eta+etaAdj;
            xi  = xi+xiAdj;
//            if ((xi<0)||(eta<0)) {
//                //String strp = name+": xi = "+xi+" or eta = "+eta;
//                //System.out.println(strp);
//                return -999.0;
//            }
            for (int i=0;i<r;i++) shp[i] = 0;
            shp[xiIndex] = xi;
            shp[etaIndex] = eta;
            v = a.getDouble(ix.set(shp));
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            String str = "IndexOutOfBounds for "+name+": xi = "+xi+", eta = "+eta;
            System.out.println(str);
            //ex.printStackTrace();
            throw ex;
        }
        return v;
    }
    
    /**
     * Gets field value at specified indices.  Indices should be specified 
     * as ROMS FORTRAN indices, not as java indices. The ROMS indices are 
     * converted to the correct java indices internally based on the vertPosType
     * and the horzPosType of the model data array.
     * 
     * @param xi--ROMS index along the xi direction (i or x)
     * @param eta--ROMS index along the eta direction (j or y)
     * @param s--ROMS index along the s direction (k or s/z)
     * @return--field value at specified indices
     * @throws java.lang.ArrayIndexOutOfBoundsException
     */
    public double getValue(int xi,int eta,int s) 
                                throws java.lang.ArrayIndexOutOfBoundsException{
        try {
            //Adjust input ROMS FORTRAN indices to java 0-based indices
            s   = s+sAdj;
            eta = eta+etaAdj;
            xi  = xi+xiAdj;
//            if ((xi<0)||(eta<0)||(s<0)) {
//                //String strp = name+": xi = "+xi+" or eta = "+eta+" or s = "+s;
//                //System.out.println(strp);
//                return -999.0;
//            }
            for (int i=0;i<r;i++) shp[i] = 0;
            shp[xiIndex]  = xi;
            shp[etaIndex] = eta;
            shp[sIndex]   = s;
            v = a.getDouble(ix.set(shp));
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            String str = "IndexOutOfBounds for "+name+": xi = "+xi+", eta = "+eta+", s = "+s;
            System.out.println(str);
            //ex.printStackTrace();
            throw ex;
        }
        return  v;
    }
    
    public Object getArray() {
        Object v = a.copyToNDJavaArray();
        return v;
    }
    
    public int getVertPosType() {
        return vertPosType;
    }
    
    public int[] getDimIndices() {
        int[] i = new int[4];
        i[0] = tIndex;
        i[1] = sIndex;
        i[2] = etaIndex;
        i[3] = xiIndex;
        return i;
    }
    
    public void setDimIndices(int tIndex, int sIndex, 
                              int etaIndex, int xiIndex) {
        rank = 0;
        this.tIndex   = tIndex;   //don't worry about tIndex
        this.sIndex   = sIndex;   if (sIndex  >=0) rank++;
        this.etaIndex = etaIndex; if (etaIndex>=0) rank++;
        this.xiIndex  = xiIndex;  if (xiIndex >=0) rank++;
    }
    
    private void setPosTypes() {
        String ht = "";
        String it = "";
        
        //test for horizontal dimension
        for (int i=0;i<r;i++) {
            it = a.getIndexName(i);
            if (it.startsWith("eta")) ht = it;
        }
        horzPosType = ModelTypes.getHorzPosType(ht);
        if (horzPosType==ModelTypes.HORZ_POSTYPE_RHO){
            etaAdj = 0;
            xiAdj  = 0;
        }
        if (horzPosType==ModelTypes.HORZ_POSTYPE_PSI){
            etaAdj = -1;
            xiAdj  = -1;
        }
        if (horzPosType==ModelTypes.HORZ_POSTYPE_U){
            etaAdj = 0;
            xiAdj  = -1;
        }
        if (horzPosType==ModelTypes.HORZ_POSTYPE_V){
            etaAdj = -1;
            xiAdj  = 0;
        }
        
        //test for vertical dimension
        ht = "";
        for (int i=0;i<r;i++) {
            it = a.getIndexName(i);
            if (it.startsWith("s")) ht = it;
        }
        vertPosType = ModelTypes.getVertPosType(ht);
        if (vertPosType==ModelTypes.VERT_POSTYPE_RHO) sAdj = -1;
        if (vertPosType==ModelTypes.VERT_POSTYPE_W)   sAdj = 0;        
    }
    
    public void debug() {
        if (debug) {
            String cc = ",";
            System.out.println("\n<<<<<<<<<<<<<<ModelData>>>>>>>>>>>>>>>>");
            System.out.println("Name = "+name);
            String str = "";
            shp = a.getShape();
            for (int i=0;i<shp.length;i++) str = str+shp[i]+cc;
            System.out.println("Shape = "+str);
            System.out.println("HorzPosType = "+horzPosType);
            System.out.println("VertPosType = "+vertPosType);
            System.out.println("Adjustments : "+xiAdj+cc+etaAdj+cc+sAdj);
            System.out.println("Lm, Mm, N = "+getLm()+cc+getMm()+cc+getN());
        }
    }
}
