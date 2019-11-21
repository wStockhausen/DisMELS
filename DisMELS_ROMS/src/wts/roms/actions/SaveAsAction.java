/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.*;

/**
 * Provides a "Save As" capability for for objects implementing the {@link wts.models.DisMELS.actions.SaveAsable} interface.
 * 
 * @author William.Stockhausen
 */
public class SaveAsAction extends AbstractAction implements ContextAwareAction, LookupListener {

    private Lookup.Result<SaveAsable> lkpResult;
    
//    public static final Logger logger = Logger.getLogger(SaveAsAction.class.getName());
    
    public SaveAsAction(){
        this(Utilities.actionsGlobalContext());
//        logger.info("===Created SaveAsAction instance via default constructor");
    }
    
    public SaveAsAction(Lookup lookup){
        super();
        lkpResult = lookup.lookupResult(SaveAsable.class);
        lkpResult.addLookupListener(this);
        this.resultChanged(new LookupEvent(lkpResult));
//        logger.info("===Created SaveAsAction instance via Lookup constructor");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if ((lkpResult!=null)&&(!lkpResult.allInstances().isEmpty())){
            SaveAsable ic = lkpResult.allInstances().iterator().next();
            try {
                ic.saveAs();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
//        logger.info("=====Creating ContextAwareInstance of SaveAsAction");
        return new SaveAsAction(actionContext);
    }

    @Override
    public void resultChanged(LookupEvent ev) {
//        logger.info("===starting SaveAsAction.resultChanged()");
        if (lkpResult.allInstances().isEmpty()){
            this.setEnabled(false);
//            logger.info("===setEnabled false");
        } else {
            this.setEnabled(true);
//            logger.info("===setEnabled true");
        }
    }
}