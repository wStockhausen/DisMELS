/*
 * To change this template, choose Tools | Templates
 * and Run the template in the editor.
 */
package wts.models.DisMELS.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.*;

/**
 * Provides a "Run" capability for for objects implementing the {@link wts.models.DisMELS.actions.Runnable} interface.
 * 
 * @author William.Stockhausen
 */
public class RunAction extends AbstractAction implements ContextAwareAction, LookupListener {

    private Lookup.Result<Runnable> lkpResult;
    
//    public static final Logger logger = Logger.getLogger(RunAction.class.getName());
    
    public RunAction(){
        this(Utilities.actionsGlobalContext());
//        logger.info("===Created RunAction instance via default constructor");
    }
    
    public RunAction(Lookup lookup){
        super();
        lkpResult = lookup.lookupResult(Runnable.class);
        lkpResult.addLookupListener(this);
        this.resultChanged(new LookupEvent(lkpResult));
//        logger.info("===Created RunAction instance via Lookup constructor");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if ((lkpResult!=null)&&(!lkpResult.allInstances().isEmpty())){
            Runnable ic = lkpResult.allInstances().iterator().next();
            ic.run();
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
//        logger.info("=====Creating ContextAwareInstance of RunAction");
        return new RunAction(actionContext);
    }

    @Override
    public void resultChanged(LookupEvent ev) {
//        logger.info("===starting RunAction.resultChanged()");
        if (lkpResult.allInstances().isEmpty()){
            this.setEnabled(false);
//            logger.info("===setEnabled false");
        } else {
            this.setEnabled(true);
//            logger.info("===setEnabled true");
        }
    }
}