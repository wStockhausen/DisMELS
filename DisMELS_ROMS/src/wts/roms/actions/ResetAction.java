/*
 * To change this template, choose Tools | Templates
 * and Reset the template in the editor.
 */
package wts.roms.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.*;

/**
 * Provides a "Reset" capability for for objects implementing the {@link wts.models.DisMELS.actions.Resetable} interface.
 * 
 * @author William.Stockhausen
 */
public class ResetAction extends AbstractAction implements ContextAwareAction, LookupListener {

    private Lookup.Result<Resetable> lkpResult;
    
//    public static final Logger logger = Logger.getLogger(ResetAction.class.getName());
    
    public ResetAction(){
        this(Utilities.actionsGlobalContext());
//        logger.info("===Created ResetAction instance via default constructor");
    }
    
    public ResetAction(Lookup lookup){
        super();
        lkpResult = lookup.lookupResult(Resetable.class);
        lkpResult.addLookupListener(this);
        this.resultChanged(new LookupEvent(lkpResult));
//        logger.info("===Created ResetAction instance via Lookup constructor");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if ((lkpResult!=null)&&(!lkpResult.allInstances().isEmpty())){
            Resetable ic = lkpResult.allInstances().iterator().next();
            ic.reset();
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
 //       logger.info("=====Creating ContextAwareInstance of ResetAction");
        return new ResetAction(actionContext);
    }

    @Override
    public void resultChanged(LookupEvent ev) {
//        logger.info("===starting ResetAction.resultChanged()");
        if (lkpResult.allInstances().isEmpty()){
            this.setEnabled(false);
//            logger.info("===setEnabled false");
        } else {
            this.setEnabled(true);
//            logger.info("===setEnabled true");
        }
    }
}