/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.roms.model;

import ucar.ma2.Index;
import ucar.ma2.IndexIterator;

/**
 * This class encapsulates the new version (as of ToolsUI_4.2) of
 * ucar.ma2.Array to reinstate the deleted methods
 * <pre>
 *      getIndexName(int i)
 *      setIndexName(int i, String name)
 * </pre>
 * @author William Stockhausen
 */
public class Array {
    
    public static Array factory(Class clazz, int[] dims){
        return new Array(clazz,dims);
    }

    protected ucar.ma2.Array a;
    protected String[] names;

    protected Array(Class clazz, int[] dims){
        a = ucar.ma2.Array.factory(clazz, dims);
        initNames();
    }

    public Array(ucar.ma2.Array ap){
        a = ap;
        initNames();
    }

    private void initNames(){
        names = new String[a.getRank()];
        for (int i=0;i<a.getRank();i++){
            names[i] = null;
        }
    }

    public Array copy(){
        Array res = new Array(a.copy());
        for (int i=0;i<names.length;i++){
            if (names[i]!=null){
                res.names[i] = names[i];
            }
        }
        return res;
    }

    public Object copyToNDJavaArray(){
        return a.copyToNDJavaArray();
    }
    
    public IndexIterator getIndexIteratorFast(){
        return a.getIndexIteratorFast();
    }
    
    public String getIndexName(int i) {
        return names[i];
    }

    public void setIndexName(int i, String name) {
        names[i] = name;
    }

    public Index getIndex(){
        return a.getIndex();
    }

    public Class getElementType() {
        return a.getElementType();
    }

    public int getRank(){
        return a.getRank();
    }

    public int[] getShape(){
        return a.getShape();
    }

    public Object getStorage() {
        return a.getStorage();
    }

    public double getDouble(Index index) {
        return a.getDouble(index);
    }

    public void setDouble(Index index, double d) {
        a.setDouble(index, d);
    }

    public float getFloat(Index index) {
        return a.getFloat(index);
    }

    public void setFloat(Index index, float f) {
        a.setFloat(index, f);
    }

    public long getLong(Index index) {
        return a.getLong(index);
    }

    public void setLong(Index index, long l) {
        a.setLong(index, l);
    }

    public int getInt(Index index) {
        return a.getInt(index);
    }

    public void setInt(Index index, int i) {
        a.setInt(index, i);
    }

    public short getShort(Index index) {
        return a.getShort(index);
    }

    public void setShort(Index index, short s) {
        a.setShort(index, s);
    }

    public byte getByte(Index index) {
        return a.getByte(index);
    }

    public void setByte(Index index, byte b) {
        a.setByte(index, b);
    }

    public char getChar(Index index) {
        return a.getChar(index);
    }

    public void setChar(Index index, char c) {
        a.setChar(index,c);
    }

    public boolean getBoolean(Index index) {
        return a.getBoolean(index);
    }

    public void setBoolean(Index index, boolean bln) {
        a.setBoolean(index,bln);
    }

    public Object getObject(Index index) {
        return a.getObject(index);
    }

    public void setObject(Index index, Object o) {
        a.setObject(index,o);
    }

    public double getDouble(int i) {
        return a.getDouble(i);
    }

    public void setDouble(int i, double d) {
        a.setDouble(i,d);
    }

    public float getFloat(int i) {
        return a.getFloat(i);
    }

    public void setFloat(int i, float f) {
        a.setFloat(i,f);
    }

    public long getLong(int i) {
        return a.getLong(i);
    }

    public void setLong(int i, long l) {
        a.setLong(i,l);
    }

    public int getInt(int i) {
        return a.getInt(i);
    }

    public void setInt(int i, int i1) {
        a.setInt(i,i1);
    }

    public short getShort(int i) {
        return a.getShort(i);
    }

    public void setShort(int i, short s) {
        a.setShort(i,s);
    }

    public byte getByte(int i) {
        return a.getByte(i);
    }

    public void setByte(int i, byte b) {
        a.setByte(i,b);
    }

    public char getChar(int i) {
        return a.getChar(i);
    }

    public void setChar(int i, char c) {
        a.setChar(i,c);
    }

    public boolean getBoolean(int i) {
        return a.getBoolean(i);
    }

    public void setBoolean(int i, boolean bln) {
        a.setBoolean(i, bln);
    }

    public Object getObject(int i) {
        return a.getObject(i);
    }

    public void setObject(int i, Object o) {
        a.setObject(i, o);
    }

}
