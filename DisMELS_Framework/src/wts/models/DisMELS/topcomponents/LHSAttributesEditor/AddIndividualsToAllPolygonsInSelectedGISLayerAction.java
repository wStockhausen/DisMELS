/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.topcomponents.LHSAttributesEditor;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import org.openide.util.*;

/**
 * This class provides a context-sensitive JMenu to the LHSAttributesEditor to
 * add individuals to all polygons in the selected "selectable" GIS layer. The menu is
 * disabled if the editor is not in the current "context" or if no selectable layer
 * is selected. It is enabled if the reverse is true.
 * 
 * If clicked on, individuals will be added to all polygons in the currently-selected 
 * GIS layer.
 * 
 * @author William.Stockhausen
 */
public class AddIndividualsToAllPolygonsInSelectedGISLayerAction  extends AbstractAction 
                                       implements LookupListener, ContextAwareAction {

    private Lookup.Result<LHSAttributesEditorTopComponent> lkpResult;
    
    private JMenu jmu = null;
    
    private static final Logger logger = Logger.getLogger(AddIndividualsToAllPolygonsInSelectedGISLayerAction.class.getName());
    
    public AddIndividualsToAllPolygonsInSelectedGISLayerAction(){
        this(Utilities.actionsGlobalContext());
        logger.info("===Created instance via default constructor");
    }
    
    public AddIndividualsToAllPolygonsInSelectedGISLayerAction(Lookup lookup){
//        super("Add individuals to all polygons in selected GIS layer");
        super("Add individuals to ALL POLYGONS in selected GIS layer");
        logger.info("===Creating instance via lookup constructor");
        initComponents();//create visual components
        lkpResult = lookup.lookupResult(LHSAttributesEditorTopComponent.class);
        lkpResult.addLookupListener(this);
        resultChanged(new LookupEvent(lkpResult));
        logger.info("===Created instance via lookup constructor");
    }
    
    private void initComponents(){
//        logger.info("Creating JMenu component");
//        jmu = new JMenu("Add individuals to all polygons in selected GIS layer");//TODO: use DynamicMenuContent ??
        jmu = new JMenu(this);
    }
    
    /**
     * Invokes the <code>addIndividualsToAllPolygonsInSelectedLayer</code> method on the 
     * LHSAttributesEditor instance when the JMenu is enabled and clicked.
     * 
     * @param e the ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info("==starting actionPerformed()");
        if ((lkpResult!=null)&&(!lkpResult.allInstances().isEmpty())){
            logger.info("--adding Individuals To All Polygons In Selected Layer");
            LHSAttributesEditorTopComponent tc = lkpResult.allInstances().iterator().next();
            if (tc.getSelectedLayer()!=null) tc.addIndividualsToAllPolygonsInSelectedLayer();
        }
        logger.info("==finished actionPerformed()");
    }

    /**
     * Enables/disables the JMenu based on the GUI "context" and whether or not
     * a selectable GIS layer is currently selected.
     * 
     * @param ev the LookupEvent
     */
    @Override
    public void resultChanged(LookupEvent ev) {
        logger.info("===starting resultChanged()");
        boolean b = false;
        if (lkpResult.allInstances().isEmpty()){
            logger.info("===no LHSAttributesEditor in context: setEnabled false");
            b = false;//no LHSAttributesEditor in context
        } else {
            LHSAttributesEditorTopComponent tc = lkpResult.allInstances().iterator().next();
            tc.setJMenu_AddIndividualsToAllPolygonsInSelectedGISLayer(jmu);//this only needs to happen once
            if (tc.getSelectedLayer()==null) {
                logger.info("===no layer selected: setEnabled false");
                b = false;//no layer selected, so disable
            } else {
                logger.info("===a layer is selected: setEnabled true");
                b = true;//a layer is selected, so enable
            }
        }
        this.setEnabled(b);
        jmu.setEnabled(b);
        logger.info("===finished resultChanged()");
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        logger.info("===starting ContextAwareInstance");
        return new AddIndividualsToAllPolygonsInSelectedGISLayerAction(actionContext);
    }

}

