/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.topcomponents.LHSAttributesEditor;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.*;

/**
 *
 * @author William.Stockhausen
 */
public class AddIndividualsBySelectedGISLayerAction  extends AbstractAction 
                                       implements LookupListener, ContextAwareAction {

    private Lookup.Result<LHSAttributesEditorTopComponent> lkpResult;
    
    private static final Logger logger = Logger.getLogger(AddIndividualsBySelectedGISLayerAction.class.getName());
    
    public AddIndividualsBySelectedGISLayerAction(){
        this(Utilities.actionsGlobalContext());
        logger.info("===Created AddIndividualsBySelectedGISLayerAction instance via default constructor");
    }
    
    public AddIndividualsBySelectedGISLayerAction(Lookup lookup){
        super("Add individuals to all polygons in selected GIS layer");
        logger.info("===Creating AddIndividualsBySelectedGISLayerAction instance via lookup constructor");
        lkpResult = lookup.lookupResult(LHSAttributesEditorTopComponent.class);
        lkpResult.addLookupListener(this);
        resultChanged(new LookupEvent(lkpResult));
        logger.info("===Created AddIndividualsBySelectedGISLayerAction instance via lookup constructor");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info("starting AddIndividualsBySelectedGISLayerAction.actionPerformed");
        if ((lkpResult!=null)&&(!lkpResult.allInstances().isEmpty())){
            logger.info("--adding Individuals To All Polygons In Selected Layer");
            LHSAttributesEditorTopComponent tc = lkpResult.allInstances().iterator().next();
            tc.addIndividualsToAllPolygonsInSelectedLayer();
        }
        logger.info("finished AddIndividualsBySelectedGISLayerAction.actionPerformed");
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        logger.info("===starting AddIndividualsBySelectedGISLayerAction.resultChanged()");
        if (lkpResult.allInstances().isEmpty()){
            logger.info("===no LHSAttributesEditor in context: setEnabled false");
            this.setEnabled(false);//no LHSAttributesEditor in context
        } else {
            LHSAttributesEditorTopComponent tc = lkpResult.allInstances().iterator().next();
            if (tc.getSelectedLayer()==null) {
                logger.info("===no layer selected: setEnabled false");
                this.setEnabled(false);//no layer selected, so disable
            } else {
                logger.info("===a layer is selected: setEnabled true");
                this.setEnabled(true);//a layer is selected, so enable
            }
        }
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        logger.info("starting AddIndividualsBySelectedGISLayerAction.ContextAwareInstance");
        return new AddIndividualsBySelectedGISLayerAction(actionContext);
    }
}

