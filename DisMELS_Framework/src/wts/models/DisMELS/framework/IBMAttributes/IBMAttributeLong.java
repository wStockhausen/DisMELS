/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.IBMAttributes;

/**
 *
 * @author William.Stockhausen
 */
public class IBMAttributeLong extends IBMAttribute {
    /** The value of the parameter */
    private long value = 0L;

    public IBMAttributeLong(String key, String shortName){
        super(key,shortName);
    }

    public IBMAttributeLong(String key, String shortName, long value){
        super(key,shortName);
        this.value = value;
    }

    @Override
    public Class getValueClass(){
        return Long.class;
    }

    @Override
    public IBMAttributeLong clone() {
        return new IBMAttributeLong(key,shortName,value);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getValueAsString() {
        return Long.toString(value);
    }

    @Override
    public void setValue(Object value) {
        this.value = (long) value;
    }

    @Override
    public void parseValue(String str) {
        value = Long.parseLong(str);
    }

}
