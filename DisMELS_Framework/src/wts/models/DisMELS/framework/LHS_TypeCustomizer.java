/*
 * LHS_TypeCustomizer.java
 *
 * Created on April 3, 2006, 4:21 PM
 */

package wts.models.DisMELS.framework;

import java.awt.Color;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * @author William Stockhausen
 */
public class LHS_TypeCustomizer extends javax.swing.JPanel 
                                implements java.beans.Customizer {
    
    private LHS_Type lhsType;
    private boolean updateNextLHSs = false;
    private boolean updateSpawnedLHSClass = false;

    private static final Logger logger = Logger.getLogger(LHS_TypeCustomizer.class.getName());
    
    /** Creates new customizer LHS_TypeCustomizer */
    public LHS_TypeCustomizer() {        
        initComponents();
        initComponents1();
    }
    
    public final void initComponents1() {
        /* put LHS classes into list of model classes in combo box */
        GlobalInfo globalInfo = GlobalInfo.getInstance();
        Set<Class<? extends LifeStageInterface>> clazzs = globalInfo.getLHSClasses();
        Set<String> classNames = new TreeSet<>();
        for (Class c: clazzs){classNames.add(c.getName());}
        DefaultComboBoxModel m = new DefaultComboBoxModel(classNames.toArray());
        jcbClasses.setModel(m);
        jcbClasses.setSelectedIndex(-1);
        
    }
    
    public void initializeOutputInfo() {
        GlobalInfo globalInfo = GlobalInfo.getInstance();
        LHS_Classes.LHS_ClassInfo ci = globalInfo.getLHSClassesInfo().getClassInfo(lhsType.getLHSClass());
        //set values for next LHS classes
        jtfNextLHSName.setEnabled(false);
        jtfNextLHSName.setText("<not set>");
        jcbNextLHSClasses.removeAllItems();
        jcbNextLHSClasses.setEnabled(false);
        String[] nextLHSClassNames = ci.nextLHSClasses;
        if (nextLHSClassNames.length > 0) {
            //output classes are defined
            updateNextLHSs = false;//turn off to avoid updates while adding items
            for (String nextLHSClassName : nextLHSClassNames) {
                jcbNextLHSClasses.addItem(nextLHSClassName);
            }
            jcbNextLHSClasses.setSelectedIndex(-1);
            updateNextLHSs = true;//turn updates on
            jtfNextLHSName.setEnabled(true);
            jcbNextLHSClasses.setEnabled(true);
            //set values for next LHSs in table
            DefaultTableModel dtm = (DefaultTableModel) jtblNextLHSs.getModel();
            dtm.setRowCount(0);//clear rows
            for (String name: lhsType.getNextLHSNames()){
                dtm.addRow(new Object[]{name,lhsType.getNextLHSClass(name)});
            }
        }
        //spawning classes
        jcbSpawnedLHSClasses.removeAllItems();
        jtfSpawnedLHSName.setEnabled(false);
        jcbSpawnedLHSClasses.setEnabled(false);
        String[] spawnedLHSClassNames = ci.spawnedLHSClasses;
        if (spawnedLHSClassNames.length > 0) {
            //output classes are defined
            jtfSpawnedLHSName.setEnabled(true);
            updateSpawnedLHSClass = false;//turn off to avoid updates while adding items
            for (int i = 0; i < spawnedLHSClassNames.length; i++) {
                jcbSpawnedLHSClasses.addItem(spawnedLHSClassNames[i]);
            }
            if (!lhsType.getSpawnedLHSClass().isEmpty()) {
                logger.info("initializeSpawningLHSInfo: output class is: "+lhsType.getSpawnedLHSClass());
                jcbSpawnedLHSClasses.setSelectedItem(lhsType.getSpawnedLHSClass());
            } else {
                jcbSpawnedLHSClasses.setSelectedIndex(-1);
            }
            updateSpawnedLHSClass = true;//turn updates on
            jcbSpawnedLHSClasses.setEnabled(true);
        } else {
            //no spawned classes are defined
        }
    }
    
    public void setObject(Object bean) {
        if (bean instanceof LHS_Type) {
            lhsType = (LHS_Type) bean;
            if (!lhsType.getLHSClass().isEmpty()) {
                //LHS class is not null, so fill in values
                jcbClasses.setSelectedItem(lhsType.getLHSClass());
                initializeOutputInfo();
            } else {
                //no class has been selected yet
                jcbClasses.setSelectedIndex(-1);
                jtfNextLHSName.setEnabled(false);
                jcbNextLHSClasses.setEnabled(false);
                jtfSpawnedLHSName.setEnabled(false);
                jcbSpawnedLHSClasses.setEnabled(false);
                DefaultTableModel dtm = (DefaultTableModel) jtblNextLHSs.getModel();
                dtm.setRowCount(0);
            }
            jtfName.setText(lhsType.getLHSName());
            jtfNextLHSName.setText("<not set>");
            jtfSpawnedLHSName.setText(lhsType.getSpawnedLHSName());
            jpColor.setBackground(lhsType.getColor());
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtfName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jcbClasses = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jbColorChange = new javax.swing.JButton();
        jpColor = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jtfNextLHSName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jcbNextLHSClasses = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblNextLHSs = new javax.swing.JTable();
        jbAddNextLHS = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jtfSpawnedLHSName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jcbSpawnedLHSClasses = new javax.swing.JComboBox();

        setMinimumSize(new java.awt.Dimension(300, 325));
        setPreferredSize(new java.awt.Dimension(600, 325));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Life stage"));

        jLabel1.setText("Name");

        jtfName.setText("<null>");
        jtfName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNameActionPerformed(evt);
            }
        });

        jLabel2.setText("Java class");

        jcbClasses.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbClasses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbClassesActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jtfName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 159, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(20, 20, 20)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2)
                    .add(jcbClasses, 0, 392, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jcbClasses, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Graphical attributes"));
        jPanel5.setMinimumSize(new java.awt.Dimension(10, 50));
        jPanel5.setPreferredSize(new java.awt.Dimension(100, 50));

        jbColorChange.setText("Change");
        jbColorChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbColorChangeActionPerformed(evt);
            }
        });

        jpColor.setBackground(java.awt.Color.gray);
        jpColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jpColor.setToolTipText("color");

        org.jdesktop.layout.GroupLayout jpColorLayout = new org.jdesktop.layout.GroupLayout(jpColor);
        jpColor.setLayout(jpColorLayout);
        jpColorLayout.setHorizontalGroup(
            jpColorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 33, Short.MAX_VALUE)
        );
        jpColorLayout.setVerticalGroup(
            jpColorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jpColor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jbColorChange)
                .addContainerGap(471, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jpColor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jbColorChange))
                .addContainerGap())
        );

        jPanel3.add(jPanel5, java.awt.BorderLayout.NORTH);

        jPanel4.setPreferredSize(new java.awt.Dimension(100, 50));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Subsequent life stage"));

        jLabel3.setText("Name");

        jtfNextLHSName.setText("<null>");

        jLabel4.setText("Java class");

        jcbNextLHSClasses.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jtblNextLHSs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "LHS name", "LHS class"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jtblNextLHSs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jtblNextLHSs.getTableHeader().setReorderingAllowed(false);
        jtblNextLHSs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblNextLHSsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtblNextLHSs);
        if (jtblNextLHSs.getColumnModel().getColumnCount() > 0) {
            jtblNextLHSs.getColumnModel().getColumn(0).setResizable(false);
        }

        jbAddNextLHS.setText("add");
        jbAddNextLHS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddNextLHSActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel3)
                            .add(jtfNextLHSName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 159, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(20, 20, 20)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jLabel4)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jbAddNextLHS))
                            .add(jcbNextLHSClasses, 0, 392, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jLabel4)
                    .add(jbAddNextLHS))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfNextLHSName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jcbNextLHSClasses, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 103, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Spawned life history stage"));

        jLabel5.setText("Name");

        jtfSpawnedLHSName.setText("<null>");
        jtfSpawnedLHSName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfSpawnedLHSNameActionPerformed(evt);
            }
        });

        jLabel6.setText("Java class");

        jcbSpawnedLHSClasses.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbSpawnedLHSClasses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbSpawnedLHSClassesActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel5)
                    .add(jtfSpawnedLHSName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 159, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(20, 20, 20)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel6)
                    .add(jcbSpawnedLHSClasses, 0, 392, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfSpawnedLHSName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jcbSpawnedLHSClasses, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel7, java.awt.BorderLayout.NORTH);

        jPanel4.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jbColorChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbColorChangeActionPerformed
        Color clr = JColorChooser.showDialog(this,"Choose color",lhsType.getColor());
        if (clr!=null) {
            lhsType.setColor(clr);
            jpColor.setBackground(clr);
        }
    }//GEN-LAST:event_jbColorChangeActionPerformed

    private void jtfNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfNameActionPerformed
        lhsType.setLHSName(jtfName.getText());
        logger.info("-----setting LHSName to "+lhsType.getLHSName());
    }//GEN-LAST:event_jtfNameActionPerformed

    private void jcbClassesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbClassesActionPerformed
        if (jcbClasses.getSelectedItem()!=null) {
            logger.info("------LHS_TypeCustomizer.jcbClassesActionEvent: selecting class "+(String) jcbClasses.getSelectedItem());
            lhsType.setLHSClass((String) jcbClasses.getSelectedItem());
            initializeOutputInfo();
        }
    }//GEN-LAST:event_jcbClassesActionPerformed

    private void jtfSpawnedLHSNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfSpawnedLHSNameActionPerformed
        lhsType.setSpawnedLHSName(jtfSpawnedLHSName.getText());
}//GEN-LAST:event_jtfSpawnedLHSNameActionPerformed

    private void jcbSpawnedLHSClassesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbSpawnedLHSClassesActionPerformed
        if (!updateSpawnedLHSClass) return;
        if (jcbSpawnedLHSClasses.getSelectedItem()!=null) {
            logger.info("-----SpawnedLHSClasses action event: selecting class "+(String) jcbSpawnedLHSClasses.getSelectedItem());
            lhsType.setSpawnedLHSClass((String) jcbSpawnedLHSClasses.getSelectedItem());
        }
}//GEN-LAST:event_jcbSpawnedLHSClassesActionPerformed

    private void jbAddNextLHSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddNextLHSActionPerformed
        if (!updateNextLHSs) return;
        if (jcbNextLHSClasses.getSelectedItem()!=null) {
            String name = jtfNextLHSName.getText();
            String clazz = (String) jcbNextLHSClasses.getSelectedItem();
            logger.info("------addNextLHS: adding "+name+": "+clazz);
            lhsType.addNextLHS(name,clazz);
            DefaultTableModel dtm = (DefaultTableModel) jtblNextLHSs.getModel();
            dtm.addRow(new Object[]{name,clazz});
            jtblNextLHSs.repaint();
        }

    }//GEN-LAST:event_jbAddNextLHSActionPerformed

    private void jtblNextLHSsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblNextLHSsMouseClicked
        int r = jtblNextLHSs.getSelectedRow();
        DefaultTableModel tm = (DefaultTableModel) jtblNextLHSs.getModel();
        String str = (String) tm.getValueAt(r, 0);
        int res = JOptionPane.showConfirmDialog(null, "Delete life stage '"+str+"'?");
        if (res==JOptionPane.OK_OPTION) {
            tm.removeRow(r);
            jtblNextLHSs.repaint();
        } 
    }//GEN-LAST:event_jtblNextLHSsMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAddNextLHS;
    private javax.swing.JButton jbColorChange;
    private javax.swing.JComboBox jcbClasses;
    private javax.swing.JComboBox jcbNextLHSClasses;
    private javax.swing.JComboBox jcbSpawnedLHSClasses;
    private javax.swing.JPanel jpColor;
    private javax.swing.JTable jtblNextLHSs;
    private javax.swing.JTextField jtfName;
    private javax.swing.JTextField jtfNextLHSName;
    private javax.swing.JTextField jtfSpawnedLHSName;
    // End of variables declaration//GEN-END:variables
    
}
