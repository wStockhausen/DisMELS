/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.topcomponents.ModelGridViewer;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.*;

/**
 * This is a context sensitive action to turn on the "Export ROMS grid to shapefile" menu item 
 * functionality associated with the MapViewerTopComponent mapGUI component.
 * 
 * @author William.Stockhausen
 */
public class ExportGridToShapefileAction extends AbstractAction implements LookupListener, ContextAwareAction {

    private Lookup.Result<ModelGridViewerTopComponent> lkpResult;
//    private static final Logger logger = Logger.getLogger(ExportGridToShapefileAction.class.getName());
    
    public ExportGridToShapefileAction(){
        this(Utilities.actionsGlobalContext());
//        logger.info("===Creating AddGISLayerAction instance via default constructor");
    }
    
    public ExportGridToShapefileAction(Lookup lookup){
        super("Export ROMS grid to shapefile");
        lkpResult = lookup.lookupResult(ModelGridViewerTopComponent.class);
        lkpResult.addLookupListener(this);
        this.resultChanged(new LookupEvent(lkpResult));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
//        logger.info("actionPerformed in AddGISLayerAction");
        if ((lkpResult!=null)&&(!lkpResult.allInstances().isEmpty())){
            ModelGridViewerTopComponent tc = lkpResult.allInstances().iterator().next();
            tc.exportGridToShapefile();
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
        return new ExportGridToShapefileAction(actionContext);
    }
    
}
