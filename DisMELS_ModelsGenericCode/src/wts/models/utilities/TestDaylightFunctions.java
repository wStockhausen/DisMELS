/*
 * TestDaylightFunctions.java
 *
 * Created on August 2, 2006, 5:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.utilities;

/**
 *
 * @author William Stockhausen
 */
public class TestDaylightFunctions {
    
    /**
     * Creates a new instance of TestDaylightFunctions
     */
    public TestDaylightFunctions() {
    }
    
    private void computeSS(double lon,double lat,double yd) {
        double[] ss = null;
        ss = DateTimeFunctions.computeSunriseSunset(lon,lat,yd);
        System.out.println("lon = "+lon+" lat = "+lat+" yrday = "+Math.floor(yd));
        System.out.println("sunrise "+ss[0]/60+", sunset "+ss[1]/60+", daylight :"+(ss[1]-ss[0])/60);
        System.out.println("solar noon "+ss[2]/60+", declination "+ss[3]);
    }
    
    private void isDaytime(double lon, double lat, double yd) {
        double[] ss = null;
        ss = DateTimeFunctions.computeSunriseSunset(lon,lat,yd);
        boolean b = DateTimeFunctions.isDaylight(lon,lat,yd);
        System.out.println("lon = "+lon+" lat = "+lat+" yrday = "+Math.floor(yd)+" hr = "+MathFunctions.mod(yd,1.0)*24);
        System.out.println("zenith "+ss[4]+", is daylight? "+(ss[4]<90.833));
    }
    private void runTests(){
        double lon = -163.0;
        double lat = 55.0;
        double yd  = 92;
        computeSS(lon,lat,yd);
        for (int h=0;h<24;h++) {
            isDaytime(lon,lat,yd+h/24.0);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestDaylightFunctions test = new TestDaylightFunctions();
        test.runTests();
    }
    
}
