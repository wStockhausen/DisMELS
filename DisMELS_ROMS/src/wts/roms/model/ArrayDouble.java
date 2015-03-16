/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.roms.model;

/**
 *
 * @author wstockhausen
 */
public class ArrayDouble extends Array {

    public static ArrayDouble factory(double[][] ap){
        return new ArrayDouble(ap);
    }

    protected ArrayDouble(double[][] ap){
        super(Double.class, new int[]{ap.length,ap[0].length});
    }
}
