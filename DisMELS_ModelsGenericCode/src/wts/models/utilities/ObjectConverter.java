/*
 * ObjectConverter.java
 */
package wts.models.utilities;

/**
 *
 * @author william.stockhausen
 */
public class ObjectConverter {
    private static ObjectConverter instance = null;
    public static ObjectConverter getInstance(){
        if (instance==null) instance = new ObjectConverter();
        return instance;
    }
    
    protected ObjectConverter(){};
    
    public int to_int(Object o) throws NumberFormatException {
        int x = 0;
        if (o instanceof String){
            x = Integer.parseInt((String) o);
        } else if (o instanceof Double){
            x = ((Double) o).intValue();
        } else if (o instanceof Long){
            x = ((Long) o).intValue();
        } else if (o instanceof Integer){
            x = ((Integer) o).intValue();
        } else if (o instanceof Boolean){
            if ((Boolean) o) x = 1;
        }
        return x;
    }
    
    public long to_long(Object o) throws NumberFormatException {
        long x = 0;
        if (o instanceof String){
            x = Long.parseLong((String) o);
        } else if (o instanceof Double){
            x = ((Double) o).longValue();
        } else if (o instanceof Long){
            x = ((Long) o).longValue();
        } else if (o instanceof Integer){
            x = ((Integer) o).longValue();
        } else if (o instanceof Boolean){
            if ((Boolean) o) x = 1;
        }
        return x;
    }
    
    public double to_double(Object o) throws NumberFormatException {
        double x = 0;
        if (o instanceof String){
            x = Double.parseDouble((String) o);
        } else if (o instanceof Double){
            x = ((Double) o).doubleValue();
        } else if (o instanceof Long){
            x = ((Long) o).doubleValue();
        } else if (o instanceof Integer){
            x = ((Integer) o).doubleValue();
        } else if (o instanceof Boolean){
            if ((Boolean) o) x = 1.0D;
        }
        return x;
    }
    
}
