/*
 * TableCellRendererTextArea.java
 *
 * Created on November 2, 2004, 12:09 PM
 */

package com.wtstockhausen.table;

/**
 *
 * @author  William Stockhausen
 */

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class TableCellRendererTextArea implements TableCellRenderer {
    
    private JTextArea jta;
    
    /** Creates a new instance of TableCellRendererTextArea */
    public TableCellRendererTextArea() {
        super();
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, 
                                                   boolean isSelected, boolean hasFocus, 
                                                   int row, int column) {
        jta = new JTextArea((String) value);  
        jta.setLineWrap(true);
        jta.setWrapStyleWord(true);
        jta.setFont(table.getFont());
        return jta;
    }
}
