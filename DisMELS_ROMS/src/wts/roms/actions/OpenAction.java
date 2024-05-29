/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.actions;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.netbeans.api.actions.Openable;
import org.openide.util.*;

/**
 * ActionListener for objects implementing the Openable interface.
 * 
 * @author William.Stockhausen
 */
public class OpenAction extends AbstractAction implements ContextAwareAction, LookupListener {

    private Lookup.Result<Openable> lkpResult;
    
    public static final Logger logger = Logger.getLogger(OpenAction.class.getName());
    
    public OpenAction(){
        this(Utilities.actionsGlobalContext());
//        logger.info("===Created OpenAction instance via default constructor");
    }
    
    public OpenAction(Lookup lookup){
        super();
        lkpResult = lookup.lookupResult(Openable.class);
        lkpResult.addLookupListener(this);
        this.resultChanged(new LookupEvent(lkpResult));
        logger.info("===Created OpenAction instance via Lookup constructor");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if ((lkpResult!=null)&&(!lkpResult.allInstances().isEmpty())){
            logger.info("=====performing OpenAction");
            Openable ic = lkpResult.allInstances().iterator().next();
            ic.open();
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        logger.info("=====Creating ContextAwareInstance of OpenAction");
        return new OpenAction(actionContext);
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        logger.info("===starting OpenAction.resultChanged()");
        if (lkpResult.allInstances().isEmpty()){
            this.setEnabled(false);
            logger.info("===setEnabled false");
        } else {
            this.setEnabled(true);
            logger.info("===setEnabled true");
        }
    }
}