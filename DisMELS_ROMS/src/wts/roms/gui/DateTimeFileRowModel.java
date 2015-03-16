/*
 * DateTimeFileRowModel.java
 * 
 * Created on Jul 11, 2007, 5:16:44 AM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.gui;

import java.io.File;
import wts.models.utilities.DateTime;

/**
 *
 * @author wstockhausen
 */
public class DateTimeFileRowModel {
    
    public static final int dirCol  = 8;
    public static final int fileCol = 7;
    public static final int otCol  = 0;
    public static final int yrCol  = 1;
    public static final int monCol = 2;
    public static final int domCol = 3;
    public static final int hrCol  = 4;
    public static final int minCol = 5;
    public static final int secCol = 6;
    
    private String fileDir;
    private String fileName;
    private Long oceanTime;
    private DateTime dateTime;

    public DateTimeFileRowModel() {
    }
    
    public DateTimeFileRowModel(String fileDir, String fileName, Long oceanTime, DateTime dateTime) {
        this.fileDir   = fileDir;
        this.fileName  = fileName;
        this.oceanTime = oceanTime;
        this.dateTime  = dateTime;
    }
    
    public DateTimeFileRowModel(File file, Long oceanTime, DateTime dateTime) {
        this.fileDir   = file.getParent();
        this.fileName  = file.getName();
        this.oceanTime = oceanTime;
        this.dateTime  = dateTime;
    }
    
    public Object get(int i) {
        switch (i) {
            case otCol:   return oceanTime;
            case yrCol:   return new Integer(dateTime.year);
            case monCol:  return new Integer(dateTime.month);
            case domCol:  return new Integer(dateTime.dom);
            case hrCol:   return new Integer(dateTime.hour);
            case minCol:  return new Integer(dateTime.min);
            case secCol:  return new Integer(dateTime.sec);
            case fileCol: return fileName;
            case dirCol:  return fileDir;
        }
        return null;
    }
    
    public String getFileDir() {
        return fileDir;
    }
    
    public void SetFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getFileName() {
        return fileName;
    }
    
    public void SetFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getOceanTime() {
        return oceanTime;
    }
    
    public void setOceanTime(Long oceanTime) {
        this.oceanTime = oceanTime;
    }
    
    public DateTime getDateTime() {
        return dateTime;
    }
    public String toString() {
        String str = oceanTime.toString()+","+dateTime.getDateTimeString()+","+fileDir+File.separator+fileName;
        return str;
    }
}
