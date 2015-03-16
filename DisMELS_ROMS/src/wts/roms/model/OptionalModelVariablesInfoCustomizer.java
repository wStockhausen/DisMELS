/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Customizer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.openide.util.Exceptions;

/**
 * Customizer for OptionalModelVariablesInfo objects.
 * 
 * Should be added as a PropertyChangeListener to the GlobalInfo instance by
 * the encompassing TopComponent.  The customizer will listen for changes to
 * the canonical model file and update the model variables accordingly. If added
 * as a PropertyChangeListener, should be removed as well at the componentClosed()
 * method of the TopComponent.
 * 
 * @author William.Stockhausen
 */
public class OptionalModelVariablesInfoCustomizer extends javax.swing.JPanel
                                             implements Customizer, PropertyChangeListener {

    private static final Logger logger = Logger.getLogger(OptionalModelVariablesInfoCustomizer.class.getName());
    
    /** the object being customized */
    private OptionalModelVariablesInfo obj = null;
    /** map from variable name to customizer */
    private final Map<String,OptionalVariableInfoCustomizer> mapOVIC;
    /**
     * Creates new form OptionalModelVariablesInfoCustomizer
     */
    public OptionalModelVariablesInfoCustomizer() {
        mapOVIC = new HashMap<>(20); 
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpPanel3 = new javax.swing.JPanel();
        jspVariables = new javax.swing.JScrollPane();
        jpVariables = new javax.swing.JPanel();
        oviCustomizer = new wts.roms.model.OptionalVariableInfoCustomizer();
        jPanel1 = new javax.swing.JPanel();
        jcbROMSvariables = new javax.swing.JComboBox();
        jbAdd = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jpPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(OptionalModelVariablesInfoCustomizer.class, "OptionalModelVariablesInfoCustomizer.jpPanel3.border.title"))); // NOI18N

        jpVariables.setLayout(new java.awt.GridLayout(0, 1));

        oviCustomizer.setEnabled(false);
        jpVariables.add(oviCustomizer);

        jspVariables.setViewportView(jpVariables);

        javax.swing.GroupLayout jpPanel3Layout = new javax.swing.GroupLayout(jpPanel3);
        jpPanel3.setLayout(jpPanel3Layout);
        jpPanel3Layout.setHorizontalGroup(
            jpPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jspVariables, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
        );
        jpPanel3Layout.setVerticalGroup(
            jpPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jspVariables, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
        );

        add(jpPanel3, java.awt.BorderLayout.CENTER);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(OptionalModelVariablesInfoCustomizer.class, "OptionalModelVariablesInfoCustomizer.jPanel1.border.title"))); // NOI18N

        jcbROMSvariables.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jbAdd.setText(org.openide.util.NbBundle.getMessage(OptionalModelVariablesInfoCustomizer.class, "OptionalModelVariablesInfoCustomizer.jbAdd.text")); // NOI18N
        jbAdd.setToolTipText(org.openide.util.NbBundle.getMessage(OptionalModelVariablesInfoCustomizer.class, "OptionalModelVariablesInfoCustomizer.jbAdd.toolTipText")); // NOI18N
        jbAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jbAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbROMSvariables, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(323, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jcbROMSvariables, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jbAdd))
        );

        add(jPanel1, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jbAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddActionPerformed
        String alias = (String) jcbROMSvariables.getSelectedItem();
        OptionalVariableInfo ovi = new OptionalVariableInfo();
        ovi.setNameInROMSDataset(alias);
        ovi.setName(alias);//internal name defaults to name in ROMS dataset
        addOVI(ovi);
    }//GEN-LAST:event_jbAddActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JPanel jPanel1;
    protected javax.swing.JButton jbAdd;
    protected javax.swing.JComboBox jcbROMSvariables;
    protected javax.swing.JPanel jpPanel3;
    protected javax.swing.JPanel jpVariables;
    protected javax.swing.JScrollPane jspVariables;
    protected wts.roms.model.OptionalVariableInfoCustomizer oviCustomizer;
    // End of variables declaration//GEN-END:variables

    
    /**
     * Sets the object to be customized in the customizer. Must be a 
     * OptionalModelVariablesInfo instance.
     * 
     * @param bean 
     */
    @Override
    public void setObject(Object bean) {
        if (bean instanceof OptionalModelVariablesInfo){
            logger.info("Setting object "+bean.toString());
            obj = (OptionalModelVariablesInfo) bean;
            setObject();
        }
    }

    private void setObject(){
        mapOVIC.clear();
        jpVariables.removeAll();
        jpVariables.add(oviCustomizer);//this functions as a header
        Set<String> names = obj.getNames();
        for (String name: names){
            logger.info("Found optional variable "+name);
            OptionalVariableInfo ovi = obj.getVariableInfo(name);
            addOVI(ovi);
        }
        setROMSVariables();
        validate();
        repaint();
    }

    /**
     * Adds a variable to the obj using it's name as the key. This creates a 
     * customizer for the variable and adds it to the variables customizer.
     * 
     * @param ovi 
     */
    private void addOVI(OptionalVariableInfo ovi){
        String name = ovi.getName();
        if (name==null) mapOVIC.remove(null);//remove previous customizer associted w/ null
        obj.addVariable(ovi);
        OptionalVariableInfoCustomizer ovic = mapOVIC.get(name);
        if (ovic==null){
            //add new customizer
            ovic = new OptionalVariableInfoCustomizer();
            ovic.setObject(ovi);
            ContextMenu cm = new ContextMenu(name);
            ovic.setComponentPopupMenu(cm.getPopup());
            jpVariables.add(ovic);
            mapOVIC.put(name,ovic);
        } else {
            //update old customizer for variable
            ovic.setObject(ovi);
        }
        validate();
    }
    
    /**
     * Removes a variable from the obj and associated customizer.
     * 
     * @param ovi 
     */
    private void removeOVI(String name){
        logger.info("Removing ovi "+name);
        obj.removeVariable(name);
        OptionalVariableInfoCustomizer ovic = mapOVIC.remove(name);
        jpVariables.remove(ovic);
        validate();
    }
    
    /**
     * Sets the variables listed in jcbROMSvariables by reading them from the
     * canonical file given in the GlobalInfo singleton.
     */
    private void setROMSVariables(){
        jcbROMSvariables.removeAllItems();
        GlobalInfo globalInfo = GlobalInfo.getInstance();
        if (!globalInfo.getCanonicalFile().equals(GlobalInfo.PROP_NotSet)){
            try {
                NetcdfReader nR = new NetcdfReader(globalInfo.getCanonicalFile());
                String[] romsNames = nR.getVariableNames();
                Set<String> names = new TreeSet<>();
                names.addAll(Arrays.asList(romsNames));
                Iterator<String> it = names.iterator();
                while (it.hasNext()) jcbROMSvariables.addItem(it.next());
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
    
    /**
     * Method reacts to changes in the ROMS grid file property of the GlobalInfo singleton
     * by forcing an update of the ROMS variables listed in jcbROMSvariables.
     * 
     * @param evt - a PropertyChange event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GlobalInfo.PROP_CanonicalFile)){
            setROMSVariables();
            validate();
        } else 
        if (evt.getPropertyName().equals(GlobalInfo.PROP_ModelOVI_ADDED)){
            logger.info("PROP_ModelOVI_ADDED detected");
        } else 
        if (evt.getPropertyName().equals(GlobalInfo.PROP_ModelOVI_REMOVED)){
            logger.info("PROP_ModelOVI_REMOVED detected");
        } else 
        if (evt.getPropertyName().equals(GlobalInfo.PROP_ModelOVI_RENAMED)){
            logger.info("PROP_ModelOVI_RENAMED detected");
            String oldName = (String)evt.getOldValue();
            String newName = (String)evt.getNewValue();
            //remove customizer associated with old name
            OptionalVariableInfoCustomizer ovic = mapOVIC.remove(oldName);
            //associate customizer with new name
            mapOVIC.put(newName,ovic);
        } else 
        if (evt.getPropertyName().equals(GlobalInfo.PROP_ModelOVI_RESET)){
            logger.info("PROP_ModelOVI_RESET detected");
            setObject(evt.getNewValue());
        } 
    }
    
    private class ContextMenu implements ActionListener {

        private final JPopupMenu cm;
        private final String name;
        
        private ContextMenu(String name){
            this.name = name;
            cm = new JPopupMenu();
            JMenuItem item = new JMenuItem("delete variable");
            item.addActionListener(this);    
            cm.add(item);
        }
        
        private JPopupMenu getPopup(){
            return cm;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("ContextMenu.ActionPerformed: removing ovi");
            removeOVI(name);
        }
        
    }
}