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
 * This is a context sensitive action to turn on the "Clear last individuals added" menu item 
 * functionality associated with the LHSAttributesEditorTopComponent.
 * 
 * @author William.Stockhausen
 */
public class ClearLastIndividualsAddedAction extends AbstractAction implements LookupListener, ContextAwareAction {

    private Lookup.Result<LHSAttributesEditorTopComponent> lkpResult;
//    private static final Logger logger = Logger.getLogger(ClearLastIndividualsAddedAction.class.getName());
    
    public ClearLastIndividualsAddedAction(){
        this(Utilities.actionsGlobalContext());
//        logger.info("===Creating ClearLastIndividualsAddedAction instance via default constructor");
    }
    
    public ClearLastIndividualsAddedAction(Lookup lookup){
        super("Add GIS layer to map");
        lkpResult = lookup.lookupResult(LHSAttributesEditorTopComponent.class);
        lkpResult.addLookupListener(this);
        this.resultChanged(new LookupEvent(lkpResult));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
//        logger.info("actionPerformed in ClearLastIndividualsAddedAction");
        if ((lkpResult!=null)&&(!lkpResult.allInstances().isEmpty())){
            LHSAttributesEditorTopComponent tc = lkpResult.allInstances().iterator().next();
            tc.clearLastAddedIndividuals();
        }
    }

    @Override
    public final void resultChanged(LookupEvent ev) {
//        logger.info("===starting ClearLastIndividualsAddedAction.resultChanged()");
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
        return new ClearLastIndividualsAddedAction(actionContext);
    }
    
}
