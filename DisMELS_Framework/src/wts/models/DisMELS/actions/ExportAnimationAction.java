/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.actions;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import org.openide.util.*;
import org.openide.util.actions.Presenter;
import wts.models.DisMELS.events.AnimationEventListener;
import wts.models.DisMELS.topcomponents.ModelReviewer.ModelReviewerTopComponent;
import wts.models.DisMELS.topcomponents.ModelRunner.ModelRunnerTopComponent;

/**
 * This is a context sensitive action to turn on the "Export map as image" menu item 
 * functionality associated with the ModelRunnerTopComponent mapGUI component.
 * 
 * @author William.Stockhausen
 */
public class ExportAnimationAction extends AbstractAction 
                            implements LookupListener, ContextAwareAction, Presenter.Menu {

//    private static final Logger logger = Logger.getLogger(ExportAnimationAction.class.getName());
    
    private Lookup.Result<AnimationEventListener> lkpResult;
    private final JCheckBoxMenuItem jmi;
    
    public ExportAnimationAction(){
        this(Utilities.actionsGlobalContext());
    }
    
    public ExportAnimationAction(Lookup lookup){
        super("Export map as image");
        jmi = new JCheckBoxMenuItem();
        initComponents();
        lkpResult = lookup.lookupResult(AnimationEventListener.class);
        lkpResult.addLookupListener(this);
        this.resultChanged(new LookupEvent(lkpResult));
    }
    
    private void initComponents(){
        jmi.setText("Save animation");
        jmi.setSelected(false);
        jmi.addActionListener(this);//will call actionPerformed below?!
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
//        logger.info("Action performed called!");
        if ((lkpResult!=null)&&(!lkpResult.allInstances().isEmpty())){
            AnimationEventListener lis = lkpResult.allInstances().iterator().next();
            if (lis instanceof ModelRunnerTopComponent){
                ModelRunnerTopComponent tc = (ModelRunnerTopComponent) lis;
                tc.saveAnimation(jmi.isSelected());
            } else if (lis instanceof ModelReviewerTopComponent){
                ModelReviewerTopComponent tc = (ModelReviewerTopComponent) lis;
                tc.saveAnimation(jmi.isSelected());
            }
        }
    }

    @Override
    public final void resultChanged(LookupEvent ev) {
        boolean b = false;
        if (!lkpResult.allInstances().isEmpty()) b = true;
        this.setEnabled(b);
        jmi.setEnabled(b);
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new ExportAnimationAction(actionContext);
    }

    @Override
    public JMenuItem getMenuPresenter() {
        return jmi;
    }
    
}
