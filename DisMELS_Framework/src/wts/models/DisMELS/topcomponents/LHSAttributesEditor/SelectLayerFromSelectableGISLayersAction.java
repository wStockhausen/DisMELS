/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.topcomponents.LHSAttributesEditor;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openide.util.*;
import org.openide.util.actions.Presenter;

/**
 *
 * @author William.Stockhausen
 */
public class SelectLayerFromSelectableGISLayersAction  extends AbstractAction 
                                       implements Presenter.Menu, LookupListener, ContextAwareAction {

    private Lookup.Result<LHSAttributesEditorTopComponent> lkpResult;
    
    private JMenu jmu = null;
    
//    private static final Logger logger = Logger.getLogger(SelectGISLayerAction.class.getName());
    
    public SelectLayerFromSelectableGISLayersAction(){
        this(Utilities.actionsGlobalContext());
//        logger.info("===Creating SelectGISLayerAction instance via default constructor");
    }
    
    public SelectLayerFromSelectableGISLayersAction(Lookup lookup){
        super();
        initComponents();//create visual components
        lkpResult = lookup.lookupResult(LHSAttributesEditorTopComponent.class);
        lkpResult.addLookupListener(this);
        resultChanged(new LookupEvent(lkpResult));
    }
    
    private void initComponents(){
        jmu = new JMenu("Select selectable GIS layer");//TODO: use DynamicMenuContent ??
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //nothing should happen here--write ActionListeners for the added radio button menu items
        //when they're created.
    }

    @Override
    public void resultChanged(LookupEvent ev) {
//        logger.info("===starting SelectGISLayerAction.resultChanged()");
        boolean b = false;
        if (lkpResult.allInstances().isEmpty()){
//            logger.info("===setEnabled false");
        } else {
            LHSAttributesEditorTopComponent tc = lkpResult.allInstances().iterator().next();
            tc.setJMenu_SelectLayerFromSelectableGISLayers(jmu);
//            logger.info("===set menu in MapViewer!");
            if (jmu.getMenuComponentCount()>0){
                b = true;
//                logger.info("===setEnabled true");
            }
        }
        this.setEnabled(b);
        jmu.setEnabled(b);
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
//        logger.info("Creating ContextAwareInstance");
        return new SelectLayerFromSelectableGISLayersAction(actionContext);
    }

    @Override
    public JMenuItem getMenuPresenter() {
//        logger.info("Returning MenuPresenter");
        return jmu;
    }
    
    public void addMenuItem(JMenuItem jmi){
        jmu.add(jmi);
    }
}

