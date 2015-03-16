/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.topcomponents.MapViewer;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.*;

/**
 * This is a context sensitive action to turn on the "Export map as image" menu item 
 * functionality associated with the MapViewerTopComponent mapGUI component.
 * 
 * @author William.Stockhausen
 */
public class ExportMapAsImageAction extends AbstractAction implements LookupListener, ContextAwareAction {

    private Lookup.Result<MapViewerTopComponent> lkpResult;
//    private static final Logger logger = Logger.getLogger(ExportMapAsImageAction.class.getName());
    
    public ExportMapAsImageAction(){
        this(Utilities.actionsGlobalContext());
    }
    
    public ExportMapAsImageAction(Lookup lookup){
        super("Export map as image");
        lkpResult = lookup.lookupResult(MapViewerTopComponent.class);
        lkpResult.addLookupListener(this);
        this.resultChanged(new LookupEvent(lkpResult));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if ((lkpResult!=null)&&(!lkpResult.allInstances().isEmpty())){
            try {
                MapViewerTopComponent tc = lkpResult.allInstances().iterator().next();
                tc.saveMapAsImage();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    @Override
    public final void resultChanged(LookupEvent ev) {
        if (lkpResult.allInstances().isEmpty()){
            this.setEnabled(false);
        } else {
            this.setEnabled(true);
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new ExportMapAsImageAction(actionContext);
    }
    
}
