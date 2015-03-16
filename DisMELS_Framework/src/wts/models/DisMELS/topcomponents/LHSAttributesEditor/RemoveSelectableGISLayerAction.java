/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.topcomponents.LHSAttributesEditor;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.openide.util.*;
import org.openide.util.actions.Presenter;

/**
 *
 * @author William.Stockhausen
 */
public class RemoveSelectableGISLayerAction  extends AbstractAction 
                                       implements Presenter.Menu, LookupListener, ContextAwareAction {

    private Lookup.Result<LHSAttributesEditorTopComponent> lkpResult;
    
    private JMenu jmu = null;
    
//    private static final Logger logger = Logger.getLogger(RemoveSelectableGISLayerAction.class.getName());
    
    public RemoveSelectableGISLayerAction(){
        this(Utilities.actionsGlobalContext());
//        logger.info("===Creating RemoveSelectableGISLayerAction instance via default constructor");
    }
    
    public RemoveSelectableGISLayerAction(Lookup lookup){
        super();
        initComponents();//create visual components
        for (Object obj: lookup.lookupAll(Object.class)){
//            logger.log(Level.INFO, "lookup includes {0}", obj.getClass().getName());
        }
        lkpResult = lookup.lookupResult(LHSAttributesEditorTopComponent.class);
        lkpResult.addLookupListener(this);
        resultChanged(new LookupEvent(lkpResult));
    }
    
    private void initComponents(){
//        logger.info("Creating JMenu component");
        jmu = new JMenu("Remove Selectable GIS Layer");//TODO: use DynamicMenuContent ??
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //nothing should happen here--write ActionListeners for the added menu items
        //when they're created.
    }

    @Override
    public void resultChanged(LookupEvent ev) {
//        logger.info("===starting resultChanged()");
        boolean b = false;
        if (!lkpResult.allInstances().isEmpty()){
            LHSAttributesEditorTopComponent tc = lkpResult.allInstances().iterator().next();
//            logger.info("===setting RemoveSelectableGISLayers menu in LHSAttributesEditor!");
            tc.setRemoveSelectableGISLayersMenu(jmu);//this really only needs to happen once
            if (jmu.getMenuComponentCount()>0) b = true;
        }
//        logger.info("===setEnabled "+b);
        this.setEnabled(b);
        jmu.setEnabled(b);
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
//        logger.info("Creating ContextAwareInstance");
        return new RemoveSelectableGISLayerAction(actionContext);
    }

    @Override
    public JMenuItem getMenuPresenter() {
//        logger.info("Returning MenuPresenter");
        return jmu;
    }
    
    public void addMenuItem(JMenuItem jmi){
//        logger.info("Adding JMenuItem "+jmi.getText());
        jmu.add(jmi);
    }
}

