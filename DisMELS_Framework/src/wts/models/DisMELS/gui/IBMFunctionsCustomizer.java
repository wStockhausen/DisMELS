/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.gui;

import java.awt.BorderLayout;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionCustomizer;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.LifeStageParametersInterface;

/**
 *
 * @author William.Stockhausen
 */
public class IBMFunctionsCustomizer extends javax.swing.JPanel {

    private LifeStageParametersInterface obj = null;
    private String cat = null;
    private Map<String,IBMFunctionCustomizer> mapOfCustomizers = null;
    private boolean doEvents = false;
    
    private static final Logger logger = Logger.getLogger(IBMFunctionsCustomizer.class.getName());
    
    /**
     * Creates new form IBMFunctionsCustomizer
     */
    public IBMFunctionsCustomizer() {
        initComponents();
    }
    
    public void setObject(LifeStageParametersInterface obj, String cat){
        this.obj = obj;
        this.cat = cat;
        Set<String> funcNames = obj.getIBMFunctionKeysByCategory(cat);
        if (funcNames.isEmpty()){
            logger.info("No functions defined for category '"+cat+"'");
            doEvents=false;         //no response to events
            mapOfCustomizers = null;
        } else {
            mapOfCustomizers = new LinkedHashMap<>(2*funcNames.size());
            doEvents = false;
            jcbFunctions.removeAllItems();
            for (String name: funcNames) {
                jcbFunctions.addItem(name);
                mapOfCustomizers.put(name,null);//delay creation of customizers until needed
            }
            doEvents = true;
            IBMFunctionInterface sfi = obj.getSelectedIBMFunctionForCategory(cat);
            if (sfi==null){
                logger.info("No function selected for category '"+cat+"'");
                jcbFunctions.setSelectedIndex(0);
            } else {
                logger.info("Function selected for category '"+cat+"' is '"+sfi.getFunctionName()+"'");
                jcbFunctions.setSelectedItem(sfi.getFunctionName());
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpJCB = new javax.swing.JPanel();
        jcbFunctions = new javax.swing.JComboBox();
        jspFunction = new javax.swing.JScrollPane();
        jpFV = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jpJCB.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpJCB.setLayout(new java.awt.BorderLayout());

        jcbFunctions.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFunctions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbFunctionsActionPerformed(evt);
            }
        });
        jpJCB.add(jcbFunctions, java.awt.BorderLayout.NORTH);

        add(jpJCB, java.awt.BorderLayout.NORTH);

        jpFV.setLayout(new java.awt.BorderLayout());
        jspFunction.setViewportView(jpFV);

        add(jspFunction, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jcbFunctionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbFunctionsActionPerformed
        if (doEvents){
            String name = (String) jcbFunctions.getSelectedItem();
            IBMFunctionCustomizer czr = mapOfCustomizers.get(name);
            if (czr==null){
                IBMFunctionInterface ifi = obj.getIBMFunction(cat, name);
                logger.info("Creating IBMFunctionCustomizer for category '"+cat+"', name '"+name+"'");
                czr = new IBMFunctionCustomizer();
                czr.setObject(ifi);
                mapOfCustomizers.put(name,czr);
            }
            jpFV.removeAll();
            jpFV.add(czr,BorderLayout.CENTER);
            validate();
            repaint();
            obj.setSelectedIBMFunctionForCategory(cat, name);
        }
    }//GEN-LAST:event_jcbFunctionsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jcbFunctions;
    private javax.swing.JPanel jpFV;
    private javax.swing.JPanel jpJCB;
    private javax.swing.JScrollPane jspFunction;
    // End of variables declaration//GEN-END:variables
}
