/*
 * DateTimeFunctions.java
 *
 * Created on January 26, 2006, 3:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.utilities;

/**
 *  This class provides static functions to calculate information
 *  regarding sunrise, sunset, and daytime/nighttime.
 *
 * @author William Stockhausen
 */
public class DateTimeFunctions {
    
    private static double deg2rad = Math.toRadians(1.0);
   
    /**
     * Creates a new instance of DateTimeFunctions
     */
    private DateTimeFunctions() {
    }
    
    /**
     *  Returns true if lat,lon position experiences daylight at day/time
     *  given by doy.
     *@param lon - longitude of position (deg Greenwich, prime meridian)
     *@param lat - latitiude of position (deg)
     *@param doy - day-of-year in UTC (1-366, as double), with decimal portion 
     *             indicating time of day (0 = midnight, 0.5 = noon).
     */
    public static boolean isDaylight(double lon, double lat, double doy) {
        boolean b = false;
        double min = MathFunctions.mod(doy,1.0)*24*60; //time-of-day in minutes
        //calculate times of sunrise, sunset, solar noon i(in min, UTC),
        //          and solar angles of declination and zenith (in deg)
        //ss={sunrise,sunset,solar noon,declination,zenith}
        double[] ss = computeSunriseSunset(lon,lat,doy);
//        if (Double.isNaN(ss[1])) {
//            //no sunrise/sunset
//            if (lat*ss[2]>0) {
//                b = true;  //summer in hemisphere, so 24-hr day at this lat
//            } else {
//                b = false; //winter in hemisphere, so 24-hr night at this lat
//            }
//        } else {
//            if (min<ss[0]) {
//                b = false; //time before sunrise
//            } else if (min>ss[1]) {
//                b = false; //time after sunset
//            } else {
//                b = true; //time between sunrise and sunset
//            }            
//        }
        b = (ss[4]<90.833);
        return b;
    }
    
    /**
     * Computes time of local sunrise, sunset and solar noon (in minutes, UTC) 
     * for given lon, lat, and time (in Julian day-of-year).
     *@param lon : longitude of poisition (deg Greenwich, prime meridian)
     *@param lat : latitude of position (deg)
     *@param time : day-of-year (1-366, fractional part indicates time-of-day)
     *@return double[5] = [0] time of sunrise (min UTC from midnight)
     *                    [1] time of sunset (min UTC from midnight)
     *                    [2] time of solarnoon (min UTC from midnight)
     *                    [3] solar declination angle (deg)
     *                    [4] solar zenith angle (deg)
     * If sunrise/sunset=NaN then its either 24-hr day or night 
     * (if lat*declination>0, it's summer in the hemisphere, hence daytime). 
     * Alternatively, if the solar zenith angle > 90.833 deg, then it is night.
     */
    public static double[] computeSunriseSunset(double lon, double lat, double time) {
        double[] ss = new double[5];
        double doy = Math.floor(time);
        double hour = MathFunctions.mod(time,1.0)*24;
        //The fractional year, in radians, is
        double gm = ((2*Math.PI)/365)*(doy-1+(hour-12)/24);
        //the "equation of time" (in minutes)
        double eqtime = 229.18*(0.000075+0.001868*Math.cos(  gm)-0.032077*Math.sin(  gm)
                                        -0.014615*Math.cos(2*gm)-0.040849*Math.sin(2*gm));
        //the solar declination angle (in radians)
        double decl = 0.006918-0.399912*Math.cos(  gm)+0.070257*Math.sin(  gm)
                              -0.006758*Math.cos(2*gm)+0.000907*Math.sin(2*gm)
                              -0.002697*Math.cos(3*gm)+0.001480*Math.sin(3*gm);
        //the hour angle (in degress) at sunrise/sunset is
        double ha = Math.acos(Math.cos(90.833*deg2rad)/(Math.cos(lat*deg2rad)*Math.cos(decl))
                              -Math.tan(lat*deg2rad)*Math.tan(decl))/deg2rad;
        //adjust lon to -180 to +180.
        double lonp = lon>180 ? lon-360 : lon;
        //the UTC times (in minutes) of sunrise, sunset, and solar noon are
        double sunrise   = 720+4*(lonp-ha)-eqtime;
        double sunset    = 720+4*(lonp+ha)-eqtime;
        double solarnoon = 720+4*lonp-eqtime;
        
        //calculate the solar zenith corresponding to the given time
        double tmin = 24*60*(time-doy);      //time of day in minutes
        double tst  = tmin + eqtime - 4*lon; //true solar time in minutes
        ha = (tst/4-180)*deg2rad;            //hour angle in radians
        double cos_zen = Math.sin(lat*deg2rad)*Math.sin(decl)
                        +Math.cos(lat*deg2rad)*Math.cos(decl)*Math.cos(ha);
        double zen = Math.acos(cos_zen)/deg2rad; //solar zenith in degrees
        
        ss[0] = sunrise;      //time of sunrise, in elapsed minutes from midnight
        ss[1] = sunset;       //time of sunset, in elapsed minutes from midnight
        ss[2] = solarnoon;    //time of solar noon, in elapsed minutes from midnight
        ss[3] = decl/deg2rad; //solar declination angle (in deg) (>0, northern hemisphere summer)
        ss[4] = zen;          //solar zenith angle, in degrees
        return ss;
    }
    
    public static boolean isBetweenDOY(double doy, double startDOY, double endDOY) {
        boolean res = false;
        if (startDOY<=endDOY) {
            res = (doy>=startDOY)&&(doy<=endDOY);
        } else {
            res = (doy>=startDOY)||(doy<=endDOY);
        }
        return res;
    }
}
