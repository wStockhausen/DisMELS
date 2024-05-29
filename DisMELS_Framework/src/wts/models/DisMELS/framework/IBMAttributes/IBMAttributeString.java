/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.IBMAttributes;

/**
 *
 * @author William.Stockhausen
 */
public class IBMAttributeString extends IBMAttribute {
    /** The value of the parameter */
    private String value = "";

    public IBMAttributeString(String key, String shortName){
        super(key,shortName);
    }

    public IBMAttributeString(String key, String shortName, String value){
        super(key,shortName);
        this.value = value;
    }

    @Override
    public Class getValueClass(){
        return String.class;
    }

    @Override
    public IBMAttributeString clone() {
        return new IBMAttributeString(key,shortName,value);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getValueAsString() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (String) value;
    }

    @Override
    public void parseValue(String str) {
        value = str;
    }

}
