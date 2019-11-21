/*
 * ObjectTransformer.java
 *
 * Created on March 11, 2003, 10:53 AM
 */

package com.wtstockhausen.utils;

/**
 *
 * @author  William Stockhausen
 */
public class ObjectTransformer {
    private static Double d = new Double(0.0);
    private static String s = new String();
    private static Object obj;
    
    /** Creates a new instance of ObjectTransform */
    public ObjectTransformer() {
    }
    
    public static Object transform(Object o, Class c) {
        obj = null;
        if (o instanceof Double) {
            //System.out.println("Transform Double to "+c.getName());
            d = (Double) o;
            if (c.getName().equals("java.lang.String")) {
                obj = (Object) d.toString();
            } else if (c.getName().equals("java.lang.Boolean")) {
                obj = (Object) new Boolean((d.doubleValue()>0));
            } else if (c.getName().equals("java.lang.Integer")) {
                obj = (Object) new Integer(d.intValue());
            } else if (c.getName().equals("java.lang.Float")) {
                obj = (Object) new Float(d.floatValue());
            } else if (c.getName().equals("java.lang.Double")) {
                obj = (Object) new Double(d.doubleValue());
            }
        } else if (o instanceof String) {
            //System.out.println("Tranform String to "+c.getName());
            s = (String) o;
            if (c.getName().equals("java.lang.String")) {
                obj = (Object) s.toString();
            } else if (c.getName().equals("java.lang.Boolean")) {
                obj = (Object) Boolean.valueOf(s);
            } else if (c.getName().equals("java.lang.Integer")) {
                try {
                    obj = (Object) Integer.valueOf(s);
                } catch (NumberFormatException exc) {
                    obj = (Object) new Integer(Integer.MIN_VALUE);
                }
            } else if (c.getName().equals("java.lang.Float")) {
                try {
                    obj = (Object) Float.valueOf(s);
                } catch (NumberFormatException exc) {
                    obj = (Object) new Float(Float.MIN_VALUE);
                }
            } else if (c.getName().equals("java.lang.Double")) {
                try {
                    obj = (Object) Double.valueOf(s);
                } catch (NumberFormatException exc) {
                    obj = (Object) new Double(Double.MIN_VALUE);
                }
            }
        }
        return obj;
    }
    
}
