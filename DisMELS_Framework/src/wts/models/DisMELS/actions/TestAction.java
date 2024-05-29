/*
 * TestAction.java
 */
package wts.models.DisMELS.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.*;

/**
 * Provides a "test" capability for for objects implementing the {@link wts.models.DisMELS.actions.Testable} interface.
 * 
 * @author William.Stockhausen
 */
public class TestAction extends AbstractAction implements ContextAwareAction, LookupListener {

    private Lookup.Result<Testable> lkpResult;
    
//    public static final Logger logger = Logger.getLogger(TestAction.class.getName());
    
    public TestAction(){
        this(Utilities.actionsGlobalContext());
//        logger.info("===Created TestAction instance via default constructor");
    }
    
    public TestAction(Lookup lookup){
        super();
        lkpResult = lookup.lookupResult(Testable.class);
        lkpResult.addLookupListener(this);
        this.resultChanged(new LookupEvent(lkpResult));
//        logger.info("===Created TestAction instance via Lookup constructor");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if ((lkpResult!=null)&&(!lkpResult.allInstances().isEmpty())){
            Testable ic = lkpResult.allInstances().iterator().next();
            ic.test();
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
//        logger.info("=====Creating ContextAwareInstance of TestAction");
        return new TestAction(actionContext);
    }

    @Override
    public void resultChanged(LookupEvent ev) {
//        logger.info("===starting TestAction.resultChanged()");
        if (lkpResult.allInstances().isEmpty()){
            this.setEnabled(false);
//            logger.info("===setEnabled false");
        } else {
            this.setEnabled(true);
//            logger.info("===setEnabled true");
        }
    }
}