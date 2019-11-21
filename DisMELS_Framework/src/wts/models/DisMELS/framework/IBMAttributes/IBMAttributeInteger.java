/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.IBMAttributes;

/**
 *
 * @author William.Stockhausen
 */
public class IBMAttributeInteger extends IBMAttribute {
    /** The value of the parameter */
    private int value = 0;

    public IBMAttributeInteger(String key, String shortName){
        super(key,shortName);
    }

    public IBMAttributeInteger(String key, String shortName, int value){
        super(key,shortName);
        this.value = value;
    }

    @Override
    public Class getValueClass(){
        return Integer.class;
    }

    @Override
    public IBMAttributeInteger clone() {
        return new IBMAttributeInteger(key,shortName,value);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getValueAsString() {
        return Integer.toString(value);
    }

    @Override
    public void setValue(Object value) {
        this.value = (int) value;
    }

    @Override
    public void parseValue(String str) {
        value = Integer.parseInt(str);
    }

}
