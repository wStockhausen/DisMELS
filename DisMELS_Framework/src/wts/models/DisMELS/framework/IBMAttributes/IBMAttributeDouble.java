/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.IBMAttributes;

import java.text.DecimalFormat;

/**
 *
 * @author William.Stockhausen
 */
public class IBMAttributeDouble extends IBMAttribute {
    
    protected static final DecimalFormat decFormat = new DecimalFormat("#.#####");
    
    /** The value of the parameter */
    private double value = 0.0;

    public IBMAttributeDouble(String key, String shortName){
        super(key,shortName);
    }

    public IBMAttributeDouble(String key, String shortName, double value){
        super(key,shortName);
        this.value = value;
    }

    @Override
    public Class getValueClass(){
        return Double.class;
    }

    @Override
    public IBMAttributeDouble clone() {
        return new IBMAttributeDouble(key,shortName,value);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getValueAsString() {
        return decFormat.format(value);
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof String) {
            parseValue((String) value);
        } else {
            this.value = (double) value;
        }
    }

    @Override
    public void parseValue(String str) {
        value = Double.parseDouble(str);
    }
}
