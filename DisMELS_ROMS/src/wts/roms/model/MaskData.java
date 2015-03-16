/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.roms.model;

/**
 *
 * @author wstockhausen
 */
public class MaskData extends ModelData {

    public MaskData(Array ap, String namep) {
        super(ap,namep);
    }
    
    @Override
    public double getValue(int xi, int eta) {
        double v = super.getValue(xi, eta);
        return (Double.isNaN(v) ? 1.0 : v);//if NaN used to represent water cells, return 1.0
    }

}
