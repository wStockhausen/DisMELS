/*
 * ColorMapEntryImpl.java
 *
 * Created on September 30, 2003, 4:39 PM
 */

package wts.GIS.styling;

/**
 *
 * @author  wstockha
 */

import org.geotools.filter.Expression;
import org.geotools.styling.ColorMapEntry;

public class ColorMapEntryImpl implements ColorMapEntry {
    
    private Expression color;
    private String label;
    private Expression opacity;
    private Expression quantity;
    
    /** Creates a new instance of ColorMapEntryImpl */
    public ColorMapEntryImpl() {
    }
    
    public Expression getColor() {
        return color;
    }
    
    public String getLabel() {
        return label;        
    }
    
    public Expression getOpacity() {
        return opacity;        
    }
    
    public Expression getQuantity() {
        return quantity;        
    }
    
    public void setColor(java.awt.Color color) {
    }
    
    public void setColor(org.geotools.filter.Expression color) {
        this.color = color;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public void setOpacity(Expression opacity) {
        this.opacity = opacity;
    }
    
    public void setQuantity(Expression quantity) {
        this.quantity = quantity;
    }
    
}
