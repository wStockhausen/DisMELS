/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.topcomponents.MapViewer;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.*;

/**
 * This is a context sensitive action to turn on the "Add GIS layer" menu item 
 * functionality associated with the MapViewerTopComponent mapGUI component.
 * 
 * @author William.Stockhausen
 */
public class AddGISLayerAction extends AbstractAction implements LookupListener, ContextAwareAction {

    private Lookup.Result<MapViewerTopComponent> lkpResult;
//    private static final Logger logger = Logger.getLogger(AddGISLayerAction.class.getName());
    
    public AddGISLayerAction(){
        this(Utilities.actionsGlobalContext());
//        logger.info("===Creating AddGISLayerAction instance via default constructor");
    }
    
    public AddGISLayerAction(Lookup lookup){
        super("Add GIS layer to map");
        lkpResult = lookup.lookupResult(MapViewerTopComponent.class);
        lkpResult.addLookupListener(this);
        this.resultChanged(new LookupEvent(lkpResult));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
//        logger.info("actionPerformed in AddGISLayerAction");
        if ((lkpResult!=null)&&(!lkpResult.allInstances().isEmpty())){
            MapViewerTopComponent tc = lkpResult.allInstances().iterator().next();
            tc.addGISLayer();
        }
    }

    @Override
    public final void resultChanged(LookupEvent ev) {
//        logger.info("===starting AddGISLayerAction.resultChanged()");
        if (lkpResult.allInstances().isEmpty()){
            this.setEnabled(false);
//            logger.info("===setEnabled false");
        } else {
            this.setEnabled(true);
//            logger.info("===setEnabled true");
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new AddGISLayerAction(actionContext);
    }
    
}
