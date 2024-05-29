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
 * This is a context sensitive action to turn on the "Clear all individuals" menu item 
 * functionality associated with the LHSAttributesEditorTopComponent.
 * 
 * @author William.Stockhausen
 */
public class ClearAllIndividualsAction extends AbstractAction implements LookupListener, ContextAwareAction {

    private Lookup.Result<LHSAttributesEditorTopComponent> lkpResult;
//    private static final Logger logger = Logger.getLogger(ClearAllIndividualsAction.class.getName());
    
    public ClearAllIndividualsAction(){
        this(Utilities.actionsGlobalContext());
//        logger.info("===Creating ClearAllIndividualsAction instance via default constructor");
    }
    
    public ClearAllIndividualsAction(Lookup lookup){
        super("Add GIS layer to map");
        lkpResult = lookup.lookupResult(LHSAttributesEditorTopComponent.class);
        lkpResult.addLookupListener(this);
        this.resultChanged(new LookupEvent(lkpResult));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
//        logger.info("actionPerformed in ClearAllIndividualsAction");
        if ((lkpResult!=null)&&(!lkpResult.allInstances().isEmpty())){
            LHSAttributesEditorTopComponent tc = lkpResult.allInstances().iterator().next();
            tc.clearAllIndividuals();
        }
    }

    @Override
    public final void resultChanged(LookupEvent ev) {
//        logger.info("===starting ClearAllIndividualsAction.resultChanged()");
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
        return new ClearAllIndividualsAction(actionContext);
    }
    
}
