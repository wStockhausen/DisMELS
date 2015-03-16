/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.IBMFunctions;

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
            this.value = (String) value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public void parseValue(String str) {
            value = str;
        }
    
}
