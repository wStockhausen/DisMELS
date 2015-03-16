/*
 * ModelTypes.java
 *
 * Created on December 1, 2005, 1:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.model;

/**
 *
 * @author William Stockhausen
 */
public class ModelTypes {

    public static final int VERT_POSTYPE_NONE = 0;
    public static final int VERT_POSTYPE_RHO  = 1;
    public static final int VERT_POSTYPE_W    = 2;
    
    public static final int HORZ_POSTYPE_NONE = 0;
    public static final int HORZ_POSTYPE_RHO  = 1;
    public static final int HORZ_POSTYPE_PSI  = 2;
    public static final int HORZ_POSTYPE_U    = 3;
    public static final int HORZ_POSTYPE_V    = 4;
    
    public static final String MASKTYPE_NONE = "none";
    public static final String MASKTYPE_RHO  = "rho";
    public static final String MASKTYPE_PSI  = "psi";
    public static final String MASKTYPE_U    = "u";
    public static final String MASKTYPE_V    = "v";
    

//    private static ModelTypes mt = null;
//
//    /**
//     * Static method to return a singleton ModelTypes instance.
//     *
//     * @return - singleton instance of ModelTypes
//     */
//    public static ModelTypes getInstance() {
//        if (mt==null) mt = new ModelTypes();
//        return mt;
//    }
//
//    private HashMap<Integer, String> mTypes = null;
//    private HashMap<String, Integer> hTypes = null;
//    private HashMap<String, Integer> vTypes = null;
//
//    /** Creates a new instance of ModelTypes */
//    private ModelTypes() {
//        createHTypes();
//        createVTypes();
//    }
//
//    private void createHTypes() {
//        hTypes = new HashMap<String, Integer>(47);
//        mTypes = new HashMap<Integer, String>(4);
//
//        Integer iRHO = new Integer(HORZ_POSTYPE_RHO);
//        mTypes.put(iRHO,"mask_rho");//rho mask field
//        hTypes.put("eta_rho",iRHO);
//        hTypes.put("xi_rho",iRHO);
//        hTypes.put("temp",iRHO);
//        hTypes.put("salt",iRHO);
//        hTypes.put("rho",iRHO);
//        hTypes.put("omega",iRHO);
//        hTypes.put("AKs",iRHO);
//        hTypes.put("AKt",iRHO);
//        hTypes.put("AKv",iRHO);
//        hTypes.put("Hsbl",iRHO);
//        hTypes.put("zeta",iRHO);
//        hTypes.put("h",iRHO);
//        hTypes.put("f",iRHO);
//        hTypes.put("pm",iRHO);
//        hTypes.put("pn",iRHO);
//        hTypes.put("dndx",iRHO);
//        hTypes.put("dmde",iRHO);
//        hTypes.put("angle",iRHO);
//        hTypes.put("x_rho",iRHO);
//        hTypes.put("y_rho",iRHO);
//        hTypes.put("lat_rho",iRHO);
//        hTypes.put("lon_rho",iRHO);
//        hTypes.put("mask_rho",iRHO);
//
//        Integer iPSI = new Integer(HORZ_POSTYPE_PSI);
//        mTypes.put(iPSI,"mask_psi");//psi mask field
//        hTypes.put("eta_psi",iPSI);
//        hTypes.put("xi_psi",iPSI);
//        hTypes.put("x_psi",iPSI);
//        hTypes.put("y_psi",iPSI);
//        hTypes.put("lat_psi",iPSI);
//        hTypes.put("lon_psi",iPSI);
//        hTypes.put("mask_psi",iPSI);
//
//        Integer iU = new Integer(HORZ_POSTYPE_U);
//        mTypes.put(iU,"mask_u");//u mask field
//        hTypes.put("eta_u",iU);
//        hTypes.put("xi_u",iU);
//        hTypes.put("u",iU);
//        hTypes.put("x_u",iU);
//        hTypes.put("y_u",iU);
//        hTypes.put("lat_u",iU);
//        hTypes.put("lon_u",iU);
//        hTypes.put("mask_u",iU);
//
//        Integer iV = new Integer(HORZ_POSTYPE_V);
//        mTypes.put(iV,"mask_v");//v mask field
//        hTypes.put("eta_v",iV);
//        hTypes.put("xi_v",iV);
//        hTypes.put("v",iV);
//        hTypes.put("x_v",iV);
//        hTypes.put("y_v",iV);
//        hTypes.put("lat_v",iV);
//        hTypes.put("lon_v",iV);
//        hTypes.put("mask_v",iV);
//    }
//
//    private void createVTypes() {
//        vTypes = new HashMap<String, Integer>(11);
//
//        Integer iRHO = new Integer(VERT_POSTYPE_RHO);
//        vTypes.put("s_rho",iRHO);
//        vTypes.put("u",iRHO);
//        vTypes.put("v",iRHO);
//        vTypes.put("temp",iRHO);
//        vTypes.put("salt",iRHO);
//        vTypes.put("rho",iRHO);
//
//        Integer iW = new Integer(VERT_POSTYPE_W);
//        vTypes.put("s_w",iW);
//        vTypes.put("omega",iW);
//        vTypes.put("AKs",iW);
//        vTypes.put("AKt",iW);
//        vTypes.put("AKv",iW);
//    }
//    
    /**
     * Returns the horizontal grid type for the given dimName (e.g., "xi_rho").
     * 
     * @param dimName
     * @return 
     */
    public static int getHorzPosType(String dimName) {
        int i = HORZ_POSTYPE_NONE;//default value
        String[] strp = dimName.split("_");//assume dimName is like "x_rho" 
        int l = strp.length-1;
        if (strp[l].equalsIgnoreCase("rho")) i = HORZ_POSTYPE_RHO; else
        if (strp[l].equalsIgnoreCase("psi")) i = HORZ_POSTYPE_PSI; else
        if (strp[l].equalsIgnoreCase("u"))   i = HORZ_POSTYPE_U; else
        if (strp[l].equalsIgnoreCase("v"))   i = HORZ_POSTYPE_V; 
        return i;
    }
    
    /**
     * Returns the vertical grid type for the given dimName (e.g., "s_w").
     * 
     * @param dimName
     * @return 
     */
    public static int getVertPosType(String dimName) {
        int i = VERT_POSTYPE_NONE;
        String[] strp = dimName.split("_");//assume dimName is like "s_rho" 
        int l = strp.length-1;
        if (strp[l].equalsIgnoreCase("rho")) i = VERT_POSTYPE_RHO; else
        if (strp[l].equalsIgnoreCase("w"))   i = VERT_POSTYPE_W; 
        return i;
    }
    
//    public String getMaskType(String s) {
//        return mTypes.get(hTypes.get(s));
//    }
//
//    public String getMaskType(int i) {
//        return mTypes.get(new Integer(i));
//    }
}
