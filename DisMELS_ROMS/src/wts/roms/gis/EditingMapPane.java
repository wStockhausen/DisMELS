/*
 * EditingMapPane.java
 *
 * Created on December 31, 2005, 9:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.gis;

import org.geotools.cs.CoordinateSystem;
import org.geotools.gui.swing.StyledMapPane;

/**
 *
 * @author William Stockhausen
 */
public class EditingMapPane extends StyledMapPane {
    
    private boolean mouseDragAdds = false;
    private boolean mouseDragZooms = true;
    
    /** Creates a new instance of EditingMapPane */
    public EditingMapPane() {
        super();
    }
    
    /** Creates a new instance of EditingMapPane */
    public EditingMapPane(CoordinateSystem cs) {
        super(cs);
    }
    
    public boolean getMouseDragZooms() {
        return mouseDragZooms;
    }
    
    public void setMouseDragZooms(boolean b) {
        mouseDragAdds = !b;
        mouseDragZooms = b;
    }
    
    protected void mouseSelectionPerformed(java.awt.Shape area) {
        if (mouseDragZooms) super.mouseSelectionPerformed(area);
        if (mouseDragAdds) {
            
        }
    }
}
