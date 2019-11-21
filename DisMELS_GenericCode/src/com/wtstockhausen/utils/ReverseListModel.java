/*
 * ReverseListModel.java
 *
 * Created on February 10, 2006, 12:49 PM
 */

package com.wtstockhausen.utils;

import java.beans.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * @author William Stockhausen
 */
public class ReverseListModel extends Object 
                                  implements Serializable, ListModel {
    
    public static final String PROP_SIZE = "size";
    
    private LinkedList<String> data = new LinkedList<String>();
    private int size = 5;
    
    transient private ArrayList<ListDataListener> dataListeners 
                                            = new ArrayList<ListDataListener>();
    transient private PropertyChangeSupport propertySupport;
    
    public ReverseListModel() {
        propertySupport = new PropertyChangeSupport(this);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    public LinkedList getData() {
        return data;
    }
    
    public void setData(LinkedList ll) {
        LinkedList oldVal = data;
        data = ll;
        propertySupport.firePropertyChange("data list",oldVal,data);
    }
    
    public int getSize() {
        return data.size();
    }

    public void setSize(int sz) {
        int oldVal = size;
        size = sz;
        propertySupport.firePropertyChange(PROP_SIZE, oldVal, size);
    }

    public void addElement(String s) {
        data.remove(s);  //removes s if present in list
        data.addFirst(s);//add it to first position
        if (data.size()>size) data.removeLast();
        fireListDataEvent();
    }
    
    public Object getElementAt(int index) {
        String s = "";
        if (index<data.size()) {
            s = data.get(index);
        }
        return s;
    }

    public void setElement(Object o) {
        if (o instanceof String) {
            addElement((String)o);
        }
    }
    
    private void fireListDataEvent() {
        ListDataEvent evt = new ListDataEvent(this,ListDataEvent.CONTENTS_CHANGED,0,0);
        Iterator<ListDataListener> it = dataListeners.iterator();
        while(it.hasNext()) {
            it.next().contentsChanged(evt);
        }
    }
    
    public void addListDataListener(ListDataListener l) {
        dataListeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        dataListeners.remove(l);
    }
    
}
