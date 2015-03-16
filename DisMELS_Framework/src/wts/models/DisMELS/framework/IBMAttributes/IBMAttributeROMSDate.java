/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.IBMAttributes;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import wts.models.DisMELS.framework.GlobalInfo;

/**
 *
 * @author William.Stockhausen
 */
public class IBMAttributeROMSDate extends IBMAttribute {
    public static Date refDate = null;
    protected static final DecimalFormat decFormat = new DecimalFormat("#.#####");
    
    /** The value of the parameter */
    private double value = 0.0;//negative values represent offset in days from start time, so need this as double

    public IBMAttributeROMSDate(String key, String shortName){
        super(key,shortName);
        if (refDate==null) refDate = GlobalInfo.getInstance().getRefDate();
    }

    public IBMAttributeROMSDate(String key, String shortName, double value){
        super(key,shortName);
        if (refDate==null) refDate = GlobalInfo.getInstance().getRefDate();
        this.value = value;
    }

    public IBMAttributeROMSDate(String key, String shortName, String value){
        super(key,shortName);
        if (refDate==null) refDate = GlobalInfo.getInstance().getRefDate();
        parseValue(value);
    }

    @Override
    public Class getValueClass(){
        return Double.class;
    }

    @Override
    public IBMAttributeROMSDate clone() {
        return new IBMAttributeROMSDate(key,shortName,value);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getValueAsString() {
        String dateStr = decFormat.format(value);
        if (value>0){
            long time = (long) value;//elapsed time in s from ref date
            Date startDate = new Date(1000L*time+refDate.getTime());//need to convert elapsed time to ms
            dateStr = GlobalInfo.dateFormat01.format(startDate);
        }
        return dateStr;
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
        Date dt;
        double refTime = refDate.getTime();//ref time in ms
        try {
            value = Double.parseDouble(str);
        } catch (java.lang.NumberFormatException ex) {
            try {
                //try default date format
                dt  = GlobalInfo.dateFormat01.parse(str);
                value = (double)(dt.getTime()-refTime)/1000L;//elapsed time in s
            } catch (ParseException ex1) {
                try {
                    //try Excel date format as backup
                    dt = GlobalInfo.dateFormat02.parse(str);
                    value = (double)(dt.getTime()-refTime)/1000L;//elapsed time in s
                } catch (ParseException ex2) {
                    Logger.getLogger(IBMAttributeROMSDate.class.getName()).log(Level.SEVERE, null, ex2);
                    throw new java.lang.NumberFormatException();
                }
            }
        }
    }
}
