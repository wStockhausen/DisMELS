/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.topcomponents.LHSAttributesEditor;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.*;

/**
 * This is a context sensitive action to turn on the "Add GIS layer" menu item 
 * functionality associated with the LHSAttributesEditorTopComponent mapGUI component.
 * 
 * @author William.Stockhausen
 */
public class AddSelectableGISLayerAction extends AbstractAction implements LookupListener, ContextAwareAction {

    private Lookup.Result<LHSAttributesEditorTopComponent> lkpResult;
//    private static final Logger logger = Logger.getLogger(AddSelectableGISLayerAction.class.getName());
    
    public AddSelectableGISLayerAction(){
        this(Utilities.actionsGlobalContext());
//        logger.info("===Creating instance via default constructor");
    }
    
    public AddSelectableGISLayerAction(Lookup lookup){
        super("Add GIS layer to map");
        lkpResult = lookup.lookupResult(LHSAttributesEditorTopComponent.class);
        lkpResult.addLookupListener(this);
        this.resultChanged(new LookupEvent(lkpResult));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
//        logger.info("actionPerformed");
        if ((lkpResult!=null)&&(!lkpResult.allInstances().isEmpty())){
            LHSAttributesEditorTopComponent tc = lkpResult.allInstances().iterator().next();
            tc.addSelectableGISLayer();
        }
    }

    @Override
    public final void resultChanged(LookupEvent ev) {
//        logger.info("===starting resultChanged()");
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
        return new AddSelectableGISLayerAction(actionContext);
    }
    
}
