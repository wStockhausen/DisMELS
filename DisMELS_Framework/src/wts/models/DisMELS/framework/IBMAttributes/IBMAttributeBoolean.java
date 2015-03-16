/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.IBMAttributes;

/**
 *
 * @author William.Stockhausen
 */
public class IBMAttributeBoolean extends IBMAttribute {
    /** The value of the parameter */
    private boolean value = false;

    public IBMAttributeBoolean(String key, String shortName){
        super(key,shortName);
    }

    public IBMAttributeBoolean(String key, String shortName, boolean value){
        super(key,shortName);
        this.value = value;
    }

    @Override
    public Class getValueClass(){
        return Boolean.class;
    }

    @Override
    public IBMAttributeBoolean clone() {
        return new IBMAttributeBoolean(key,shortName,value);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getValueAsString() {
        return Boolean.toString(value);
    }

    @Override
    public void setValue(Object value) {
        this.value = (boolean) value;
    }

    @Override
    public void parseValue(String str) {
        value = Boolean.parseBoolean(str);
    }
}
