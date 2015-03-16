/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.IBMFunctions;

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
        this.value = (boolean) value;
    }

    public final void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public final void parseValue(String str) {
        value = Boolean.parseBoolean(str);
    }
}
