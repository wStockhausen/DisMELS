/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.topcomponents.MapViewer;

import java.awt.event.ActionEvent;
import javax.swing.*;
import org.openide.util.*;
import org.openide.util.actions.Presenter;

/**
 *
 * @author William.Stockhausen
 */
public class RemoveGISLayerAction  extends AbstractAction 
                                       implements Presenter.Menu, LookupListener, ContextAwareAction {

    private Lookup.Result<MapViewerTopComponent> lkpResult;
    
    private JMenu jmu = null;
    
//    private static final Logger logger = Logger.getLogger(RemoveGISLayerAction.class.getName());
    
    public RemoveGISLayerAction(){
        this(Utilities.actionsGlobalContext());
//        logger.info("===Creating RemoveGISLayerAction instance via default constructor");
    }
    
    public RemoveGISLayerAction(Lookup lookup){
        super();
        initComponents();//create visual components
        for (Object obj: lookup.lookupAll(Object.class)){
//            logger.log(Level.INFO, "lookup includes {0}", obj.getClass().getName());
        }
        lkpResult = lookup.lookupResult(MapViewerTopComponent.class);
        lkpResult.addLookupListener(this);
        resultChanged(new LookupEvent(lkpResult));
    }
    
    private void initComponents(){
//        logger.info("Creating JMenu component");
        jmu = new JMenu("Remove Non-selectable Map Layer");//TODO: use DynamicMenuContent ??
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
//        if ((!lkpResult.allInstances().isEmpty())){
//            logger.info("-----actionPerformed()!!");
            String ac = e.getActionCommand();
            MapViewerTopComponent tc = lkpResult.allInstances().iterator().next();
            tc.removeGISLayer(ac);
            jmu.remove((JMenuItem) e.getSource());
//            logger.info(e.paramString());
//        }
    }

    @Override
    public void resultChanged(LookupEvent ev) {
//        logger.info("===starting resultChanged()");
        boolean b = false;
        if (!lkpResult.allInstances().isEmpty()){
            MapViewerTopComponent tc = lkpResult.allInstances().iterator().next();
//            logger.info("===setting RemoveGISLayers menu in MapViewer!");
            tc.setRemoveGISLayersMenu(jmu);//this really only needs to happen once
            if (jmu.getMenuComponentCount()>0) b = true;
        }
//        logger.info("===setEnabled "+b);
        this.setEnabled(b);
        jmu.setEnabled(b);
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
//        logger.info("Creating ContextAwareInstance");
        return new RemoveGISLayerAction(actionContext);
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

