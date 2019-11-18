/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.IBMFunctions;

import javax.swing.JOptionPane;

/**
 *
 * @author William.Stockhausen
 */
public final class IBMParameterDouble extends IBMParameter {
    /** The value of the parameter */
    private double value = 0.0;

    public IBMParameterDouble(String name, String descr){
        super(name,descr);
    }

    public IBMParameterDouble(String name, String descr, double value){
        super(name,descr);
        setValue(value);
    }

    @Override
    public final IBMParameterDouble clone() {
        return new IBMParameterDouble(name,description,value);
    }

    @Override
    public final Double getValue() {
        return value;
    }

    @Override
    public final String getValueAsString() {
        return Double.toString(value);
    }

    @Override
    public final void setValue(Object value) {
        try {
            this.value = (double) value;
        } catch (java.lang.ClassCastException exc){
            String str = "Value '"+value.toString()+"' could  not be cast to double\n"+
                         "for IBMParameterDouble '"+this.name+"'.\n"+
                         "This probably results from an error in the parameters file. Please fix.";
            JOptionPane.showMessageDialog(null, str, "Error setting IBMParameterDouble value", JOptionPane.ERROR_MESSAGE);
            throw(exc);
        }
    }

    public final void setValue(double value) {
        this.value = value;
    }

    @Override
    public final void parseValue(String str) {
        value = java.lang.Double.parseDouble(str);
    } 
}
