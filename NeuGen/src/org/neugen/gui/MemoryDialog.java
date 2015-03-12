/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2007–2012 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
 * 
 * This file is part of NeuGen.
 *
 * NeuGen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/NeuGen/LICENSE
 *
 * NeuGen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of NeuGen includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of NeuGen. The copyright statement/attribution may not be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do the following regarding copyright
 * notice and author attribution.
 *
 * Add an additional notice, stating that you modified NeuGen. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "NeuGen source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * S. Wolf, S. Grein, G. Queisser. NeuGen 2.0 -
 * Employing NeuGen 2.0 to automatically generate realistic
 * morphologies of hippocapal neurons and neural networks in 3D.
 * Neuroinformatics, 2013, 11(2), pp. 137-148, doi: 10.1007/s12021-012-9170-1
 *
 *
 * J. P. Eberhard, A. Wanner, G. Wittum. NeuGen -
 * A tool for the generation of realistic morphology 
 * of cortical neurons and neural networks in 3D.
 * Neurocomputing, 70(1-3), pp. 327-343, doi: 10.1016/j.neucom.2006.01.028
 *
 */
/*
 * MemoryDialog.java
 *
 * Created on 16.04.2011, 20:31:00
 */
package org.neugen.gui;

import java.awt.event.ItemEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.neugen.utils.NeuGenLogger;

/**
 *
 * @author Sergei
 */
public class MemoryDialog extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    private static final String SELECTED = "1";
    private static final String DESELECTED = "0";

    /** Creates new form MemoryDialog */
    public MemoryDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.properties = new Properties();
        initComponents();
        readProp();
        this.dialogUtil = new Utils(logger, properties);
        initParam();
    }

    private void initParam() {
        this.heapMemField.setText(properties.getProperty(heapMemField.getName() + ".text"));
        String aaVal = properties.getProperty(this.aaCheckBox.getName());
        logger.info("antialiasing name: " + aaCheckBox.getName());
        logger.info("antialiasing: " + aaVal);
        if (aaVal.equals(SELECTED)) {
            this.aaCheckBox.setSelected(true);
        } else {
            this.aaCheckBox.setSelected(false);
        }
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        heapMemField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        aaCheckBox = new javax.swing.JCheckBox();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.neugen.gui.NeuGenApp.class).getContext().getResourceMap(MemoryDialog.class);
        okButton.setText(resourceMap.getString("okButton.text")); // NOI18N
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        heapMemField.setText(resourceMap.getString("heapMemField.text")); // NOI18N
        heapMemField.setName("heapMemField"); // NOI18N
        heapMemField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                heapMemFieldKeyReleased(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        aaCheckBox.setText(resourceMap.getString("aaCheckBox.text")); // NOI18N
        aaCheckBox.setName("aaCheckBox"); // NOI18N
        aaCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                aaCheckBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(aaCheckBox)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(okButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(heapMemField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cancelButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(heapMemField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aaCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void aaCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_aaCheckBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            anitaiasingValue = true;
            dialogUtil.setCheckBox(aaCheckBox.getName(), SELECTED);
        } else {
            anitaiasingValue = false;
            dialogUtil.setCheckBox(aaCheckBox.getName(), DESELECTED);
        }
    }//GEN-LAST:event_aaCheckBoxItemStateChanged

    private void heapMemFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_heapMemFieldKeyReleased
        this.heapSize = dialogUtil.setIntegerLabel(heapMemField, ".text");
    }//GEN-LAST:event_heapMemFieldKeyReleased

    private void doClose(int retStatus) {
        if (retStatus == RET_OK) {
            dialogUtil.writeProp(f);
        }
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        NeuGenLogger.initLogger();
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                final MemoryDialog dialog = new MemoryDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        dialog.dispose();
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox aaCheckBox;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField heapMemField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
    /** use to log messages */
    private static Logger logger = Logger.getLogger(MemoryDialog.class.getName());
    private Properties properties;
    private File f;
    private int heapSize = -1;
    boolean anitaiasingValue = false;
    private Utils dialogUtil;

    public int getHeapSize() {
        if (heapSize < 0) {
            heapSize = this.dialogUtil.setIntegerLabel(heapMemField, ".text");
        }
        if (heapSize < 1) {
            heapSize = 200;
        }
        return heapSize;
    }

    public boolean getAnitaiasing() {
        return anitaiasingValue;
    }

    private void readProp() {
        BufferedInputStream stream;
        try {
            properties = new Properties();
            File locVoxelProFile = new File("config/gui/MemoryDialog.properties");
            logger.info("read file: " + locVoxelProFile.getAbsolutePath());
            if (locVoxelProFile.exists()) {
                f = locVoxelProFile;
                logger.info("read local file: " + f.getAbsolutePath());
                stream = new BufferedInputStream(new FileInputStream(locVoxelProFile));
                properties.load(stream);
                stream.close();
            } else {
                InputStream is = VoxelDialog.class.getResourceAsStream("resources/MemoryDialog.properties");
                properties.load(is);
                is.close();
                f = locVoxelProFile;
            }
        } catch (FileNotFoundException ex) {
            logger.error(ex, ex);
        } catch (IOException ex) {
            logger.error(ex, ex);
        }
    }
}
