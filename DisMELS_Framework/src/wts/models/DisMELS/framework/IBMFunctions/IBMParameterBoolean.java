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
public final class IBMParameterBoolean extends IBMParameter {
    /** The value of the parameter */
    private boolean value = false;
    
    public IBMParameterBoolean(String name, String descr){
        super(name,descr);
    }

    public IBMParameterBoolean(String name, String descr, boolean value){
        super(name,descr);
        setValue(value);
    }

    @Override
    public final IBMParameterBoolean clone() {
        return new IBMParameterBoolean(name,description,value);
    }

    @Override
    public final Boolean getValue() {
        return value;
    }

    @Override
    public final String getValueAsString() {
        return Boolean.toString(value);
    }

    @Override
    public final void setValue(Object value) {
        try {
            this.value = (boolean) value;
        } catch (java.lang.ClassCastException exc){
            String str = "Value '"+value.toString()+"' could  not be cast to boolean\n"+
                         "for IBMParameterBoolean '"+this.name+"'.\n"+
                         "This probably results from an error in the parameters file. Please fix.";
            JOptionPane.showMessageDialog(null, str, "Error setting IBMParameterBoolean value", JOptionPane.ERROR_MESSAGE);
            throw(exc);
        }
    }

    public final void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public final void parseValue(String str) {
        value = Boolean.parseBoolean(str);
    }
}
