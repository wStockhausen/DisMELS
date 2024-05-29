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
public class MouseConfigurationAction  extends AbstractAction 
                                       implements Presenter.Menu, LookupListener, ContextAwareAction {

    private Lookup.Result<LHSAttributesEditorTopComponent> lkpResult;
    
    private boolean doActions = true;
    
    private JMenu jmu = null;
    private ButtonGroup group = null;
    private JRadioButtonMenuItem jrb1 = null;
    private JRadioButtonMenuItem jrb2 = null;
    private JRadioButtonMenuItem jrb3 = null;
    
//    private static final Logger logger = Logger.getLogger(MouseConfigurationAction.class.getName());
    
    public MouseConfigurationAction(){
        this(Utilities.actionsGlobalContext());
//        logger.info("===Creating MouseConfigurationAction instance via default constructor");
    }
    
    public MouseConfigurationAction(Lookup lookup){
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
        jmu = new JMenu("Mouse configuration");//TODO: use DynamicMenuContent ??
        group = new ButtonGroup();
        //zoom
        jrb1 = new JRadioButtonMenuItem();
        jrb1.setText("left-click/drag to zoom");
        jmu.add(jrb1);
        group.add(jrb1);
        //add inidividuals
        jrb2 = new JRadioButtonMenuItem();
        jrb2.setText("left-click/drag to add individuals");
        jmu.add(jrb2);
        group.add(jrb2);
        //add individuals to polygon
        jrb3 = new JRadioButtonMenuItem();
        jrb3.setText("left-click to add individuals to a polygon");
        jmu.add(jrb3);
        group.add(jrb3);
        
        jrb1.setSelected(true);   
        
        jrb1.addActionListener(this);
        jrb2.addActionListener(this);
        jrb3.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
//        if ((lkpResult!=null)&&(!lkpResult.allInstances().isEmpty())){
        if ((!lkpResult.allInstances().isEmpty())&&doActions){
//            logger.info("-----MouseConfigurationAction.actionPerformed()!!");
            LHSAttributesEditorTopComponent tc = lkpResult.allInstances().iterator().next();
            if (jrb1.isSelected()) {tc.setMouseZooms();} else
            if (jrb2.isSelected()) {tc.setMouseAddsIndividuals();} else
            if (jrb3.isSelected()) {tc.setMouseAddsIndividualsToPolygon();}
//            logger.info(e.paramString());
        }
    }

    @Override
    public void resultChanged(LookupEvent ev) {
//        logger.info("===starting MouseConfigurationAction.resultChanged()");
        if (lkpResult.allInstances().isEmpty()){
            boolean b = false;
            this.setEnabled(b);
            jmu.setEnabled(b);
            jrb1.setEnabled(b);
            jrb2.setEnabled(b);
            jrb3.setEnabled(b);
//            logger.info("===setEnabled false");
        } else {
            doActions = false;
            LHSAttributesEditorTopComponent tc = lkpResult.allInstances().iterator().next();
            boolean b = true;
            this.setEnabled(b);
            jmu.setEnabled(b);
            jrb1.setEnabled(b);
            jrb1.setSelected(tc.getMouseZooms());
            jrb2.setEnabled(b);
            jrb2.setSelected(tc.getMouseAddsIndividuals());
            jrb3.setEnabled(b);
            jrb3.setSelected(tc.getMouseAddsIndividualsToPolygon());
            doActions = true;
//            logger.info("===setEnabled true");
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
//        logger.info("Creating ContextAwareInstance");
        return new MouseConfigurationAction(actionContext);
    }

    @Override
    public JMenuItem getMenuPresenter() {
//        logger.info("Returning MenuPresenter");
        return jmu;
    }
    
}

