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
public final class IBMParameterString extends IBMParameter {
        /** The value of the parameter */
        private String value = "";
        
        public IBMParameterString(String name, String descr){
            super(name,descr);
        }

        public IBMParameterString(String name, String descr, String value){
            super(name,descr);
            setValue(value);
        }

        @Override
        public IBMParameterString clone() {
            return new IBMParameterString(name,description,value);
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String getValueAsString() {
            return value;
        }

        @Override
        public void setValue(Object value) {
            try {
                this.value = (String) value;
            } catch (java.lang.ClassCastException exc){
                String str = "Value '"+value.toString()+"' could  not be cast to String\n"+
                             "for IBMParameterString '"+this.name+"'.\n"+
                         "This probably results from an error in the parameters file. Please fix.";
                JOptionPane.showMessageDialog(null, str, "Error setting IBMParameterString value", JOptionPane.ERROR_MESSAGE);
                throw(exc);
            }
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public void parseValue(String str) {
            value = str;
        }
    
}
