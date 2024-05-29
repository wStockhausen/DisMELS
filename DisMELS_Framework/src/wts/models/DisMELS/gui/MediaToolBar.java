/*
 * MediaToolBar.java
 *
 * Created on July 14, 2006, 5:57 PM
 */

package wts.models.DisMELS.gui;

import java.util.ArrayList;
import java.util.Iterator;
import wts.models.DisMELS.events.MediaToolBarEvent;
import wts.models.DisMELS.events.MediaToolBarEventListener;

/**
 *
 * @author  William Stockhausen
 */
public class MediaToolBar extends javax.swing.JPanel {
    
    private ArrayList<MediaToolBarEventListener> eventListeners = 
                new ArrayList<MediaToolBarEventListener>();
    private Iterator<MediaToolBarEventListener> iterator;
    private MediaToolBarEvent evMTB;
    
    /** Creates new form MediaToolBar */
    public MediaToolBar() {
        evMTB = new MediaToolBarEvent(this);
        initComponents();
    }
    
    public synchronized void addMediaToolBarEventListener(MediaToolBarEventListener el) {
        eventListeners.add(el);
    }
    
    public synchronized void removeMediaToolBarEventListener(MediaToolBarEventListener el) {
        eventListeners.remove(el);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar = new javax.swing.JToolBar();
        jbPlay = new javax.swing.JButton();
        jbStop = new javax.swing.JButton();
        bRewind = new javax.swing.JButton();
        jbStepBack = new javax.swing.JButton();
        jbStepForward = new javax.swing.JButton();
        jbFastForward = new javax.swing.JButton();
        jProgressBar = new javax.swing.JProgressBar();

        setLayout(new java.awt.BorderLayout());

        jbPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/wts/models/DisMELS/resources/icons/Play24.gif"))); // NOI18N
        jbPlay.setToolTipText("Play");
        jbPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbPlayActionPerformed(evt);
            }
        });
        jToolBar.add(jbPlay);

        jbStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/wts/models/DisMELS/resources/icons/Stop24.gif"))); // NOI18N
        jbStop.setToolTipText("Stop");
        jbStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbStopActionPerformed(evt);
            }
        });
        jToolBar.add(jbStop);

        bRewind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/wts/models/DisMELS/resources/icons/Rewind24.gif"))); // NOI18N
        bRewind.setToolTipText("Rewind to start");
        bRewind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRewindActionPerformed(evt);
            }
        });
        jToolBar.add(bRewind);

        jbStepBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/wts/models/DisMELS/resources/icons/StepBack24.gif"))); // NOI18N
        jbStepBack.setToolTipText("Step backward");
        jbStepBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbStepBackActionPerformed(evt);
            }
        });
        jToolBar.add(jbStepBack);

        jbStepForward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/wts/models/DisMELS/resources/icons/StepForward24.gif"))); // NOI18N
        jbStepForward.setToolTipText("Step forward");
        jbStepForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbStepForwardActionPerformed(evt);
            }
        });
        jToolBar.add(jbStepForward);

        jbFastForward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/wts/models/DisMELS/resources/icons/FastForward24.gif"))); // NOI18N
        jbFastForward.setToolTipText("Fast forward");
        jbFastForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbFastForwardActionPerformed(evt);
            }
        });
        jToolBar.add(jbFastForward);

        jProgressBar.setMaximumSize(new java.awt.Dimension(32767, 20));
        jProgressBar.setMinimumSize(new java.awt.Dimension(10, 20));
        jProgressBar.setPreferredSize(new java.awt.Dimension(146, 20));
        jToolBar.add(jProgressBar);

        add(jToolBar, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jbFastForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbFastForwardActionPerformed
        iterator = eventListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().fastForward(evMTB);
        }
    }//GEN-LAST:event_jbFastForwardActionPerformed

    private void jbStepForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbStepForwardActionPerformed
        iterator = eventListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().stepForward(evMTB);
        }
    }//GEN-LAST:event_jbStepForwardActionPerformed

    private void jbStepBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbStepBackActionPerformed
        iterator = eventListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().stepBackward(evMTB);
        }
    }//GEN-LAST:event_jbStepBackActionPerformed

    private void bRewindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRewindActionPerformed
        iterator = eventListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().rewind(evMTB);
        }
    }//GEN-LAST:event_bRewindActionPerformed

    private void jbStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbStopActionPerformed
        iterator = eventListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().stop(evMTB);
        }
    }//GEN-LAST:event_jbStopActionPerformed

    private void jbPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbPlayActionPerformed
        iterator = eventListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().play(evMTB);
        }
    }//GEN-LAST:event_jbPlayActionPerformed
    
    public void setProgressBarParams(int min, int max) {
        jProgressBar.setMinimum(min);
        jProgressBar.setMaximum(max);
    }
    
    public void setIndeterminate(boolean b) {
        jProgressBar.setIndeterminate(b);
    }
    
    public void setString(String str) {
        jProgressBar.setString(str);
    }
    
    public void setStringPainted(boolean b) {
        jProgressBar.setStringPainted(b);
    }
    
    public void setValue(int n) {
        jProgressBar.setValue(n);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bRewind;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JToolBar jToolBar;
    private javax.swing.JButton jbFastForward;
    private javax.swing.JButton jbPlay;
    private javax.swing.JButton jbStepBack;
    private javax.swing.JButton jbStepForward;
    private javax.swing.JButton jbStop;
    // End of variables declaration//GEN-END:variables
    
}
