/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.topcomponents.ModelFunctionTester;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionCustomizer;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//wts.models.DisMELS.topcomponents.ModelFunctionTester//ModelFunctionTester//EN",
autostore = false)
@TopComponent.Description(preferredID = "ModelFunctionTesterTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "wts.models.DisMELS.topcomponents.ModelFunctionTester.ModelFunctionTesterTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_ModelFunctionTesterAction",
preferredID = "ModelFunctionTesterTopComponent")
@Messages({
    "CTL_ModelFunctionTesterAction=Model function tester",
    "CTL_ModelFunctionTesterTopComponent=Model function tester",
    "HINT_ModelFunctionTesterTopComponent=This is the model function tester window."
})
public final class ModelFunctionTesterTopComponent extends TopComponent {

    private final Map<String,Class<? extends IBMFunctionInterface>> classMap = new TreeMap<>();
    
    private boolean doActions = true;

    public ModelFunctionTesterTopComponent() {
        initComponents();
        setName(Bundle.CTL_ModelFunctionTesterTopComponent());
        setToolTipText(Bundle.HINT_ModelFunctionTesterTopComponent());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jcbModelFunctions = new javax.swing.JComboBox();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setLayout(new java.awt.BorderLayout());
        add(jPanel1, java.awt.BorderLayout.CENTER);

        jcbModelFunctions.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbModelFunctions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbModelFunctionsActionPerformed(evt);
            }
        });
        add(jcbModelFunctions, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void jcbModelFunctionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbModelFunctionsActionPerformed
        if (doActions){
            String key = (String) jcbModelFunctions.getSelectedItem();
            if (classMap.get(key)!=null){
                try {
                    IBMFunctionInterface mf = classMap.get(key).newInstance();
                    IBMFunctionCustomizer czr = new IBMFunctionCustomizer();
                    czr.setObject(mf);
                    jPanel1.removeAll();//remove old customizer, if any
                    jPanel1.add(czr);   //add new customizer
                    validate();
                    repaint();
                } catch (InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(ModelFunctionTesterTopComponent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_jcbModelFunctionsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox jcbModelFunctions;
    // End of variables declaration//GEN-END:variables

    private void initComponents1(){
        doActions = false;
        jcbModelFunctions.removeAllItems();
        Lookup.Result<IBMFunctionInterface> lr = Lookup.getDefault().lookupResult(IBMFunctionInterface.class);
        Set<Class<? extends IBMFunctionInterface>> allClasses = lr.allClasses();
        Iterator<Class<? extends IBMFunctionInterface>> it = allClasses.iterator();
        if (it.hasNext()) {
            while(it.hasNext()){
                try {
                    Class cl = it.next();
                    Field f = cl.getField("type");//concrete classes will be non-null
                    if (f!=null){
                        String type = (String) f.get(null);
                        String name = (String) cl.getField("name").get(null);
                        String desc = (String) cl.getField("description").get(null);
                        String key = type+": "+name;
                        jcbModelFunctions.addItem(key);
                        classMap.put(key, cl);
                    }
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
                    Logger.getLogger(ModelFunctionTesterTopComponent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            jcbModelFunctions.addItem("--no model functions available--");
        }
        doActions = true;
    }
    
    @Override
    public void componentOpened() {
        initComponents1();
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}