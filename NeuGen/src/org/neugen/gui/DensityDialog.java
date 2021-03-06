/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2012-2015 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
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
 * DensityDialog.java
 *
 * Created on 12.08.2010, 12:22:45
 */
package org.neugen.gui;

import org.neugen.utils.NeuGenLogger;
import org.apache.log4j.Logger;

/**
 *
 * @author Sergei Wolf
 */
public class DensityDialog extends javax.swing.JDialog {

    /** use to log messages */
    private static Logger logger = Logger.getLogger(DensityDialog.class.getName());
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    private int voxelLenght = -1;

    /** Creates new form DensityDialog */
    public DensityDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        partButtonGroup.add(dendriticRadioButton);
        partButtonGroup.add(axonalRadioButton);
        partButtonGroup.add(synapticRadioButton);

        methodButtonGroup.add(volumeRadioButton);
        methodButtonGroup.add(numberRadioButton);
        methodButtonGroup.add(lengthRadioButton);
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        partButtonGroup = new javax.swing.ButtonGroup();
        methodButtonGroup = new javax.swing.ButtonGroup();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        lengthTextField = new javax.swing.JTextField();
        lengthLabel = new javax.swing.JLabel();
        partPanel = new javax.swing.JPanel();
        dendriticRadioButton = new javax.swing.JRadioButton();
        axonalRadioButton = new javax.swing.JRadioButton();
        synapticRadioButton = new javax.swing.JRadioButton();
        methodPanel = new javax.swing.JPanel();
        volumeRadioButton = new javax.swing.JRadioButton();
        lengthRadioButton = new javax.swing.JRadioButton();
        numberRadioButton = new javax.swing.JRadioButton();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.neugen.gui.NeuGenApp.class).getContext().getResourceMap(DensityDialog.class);
        okButton.setText(resourceMap.getString("okButton.text")); // NOI18N
        okButton.setName("okButton"); // NOI18N
        okButton.setSelected(true);
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

        lengthTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        lengthTextField.setText(resourceMap.getString("lengthTextField.text")); // NOI18N
        lengthTextField.setName("lengthTextField"); // NOI18N
        lengthTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lengthTextFieldKeyReleased(evt);
            }
        });

        lengthLabel.setText(resourceMap.getString("lengthLabel.text")); // NOI18N
        lengthLabel.setName("lengthLabel"); // NOI18N

        partPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("partPanel.border.title"))); // NOI18N
        partPanel.setName("partPanel"); // NOI18N

        dendriticRadioButton.setSelected(true);
        dendriticRadioButton.setText(resourceMap.getString("dendriticRadioButton.text")); // NOI18N
        dendriticRadioButton.setName("dendriticRadioButton"); // NOI18N
        dendriticRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dendriticRadioButtonActionPerformed(evt);
            }
        });

        axonalRadioButton.setText(resourceMap.getString("axonalRadioButton.text")); // NOI18N
        axonalRadioButton.setName("axonalRadioButton"); // NOI18N
        axonalRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                axonalRadioButtonActionPerformed(evt);
            }
        });

        synapticRadioButton.setText(resourceMap.getString("synapticRadioButton.text")); // NOI18N
        synapticRadioButton.setName("synapticRadioButton"); // NOI18N
        synapticRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                synapticRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout partPanelLayout = new javax.swing.GroupLayout(partPanel);
        partPanel.setLayout(partPanelLayout);
        partPanelLayout.setHorizontalGroup(
            partPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(partPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(partPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dendriticRadioButton)
                    .addComponent(axonalRadioButton)
                    .addComponent(synapticRadioButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        partPanelLayout.setVerticalGroup(
            partPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(partPanelLayout.createSequentialGroup()
                .addComponent(dendriticRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(axonalRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(synapticRadioButton))
        );

        methodPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("methodPanel.border.title"))); // NOI18N
        methodPanel.setName("methodPanel"); // NOI18N

        volumeRadioButton.setSelected(true);
        volumeRadioButton.setText(resourceMap.getString("volumeRadioButton.text")); // NOI18N
        volumeRadioButton.setName("volumeRadioButton"); // NOI18N

        lengthRadioButton.setText(resourceMap.getString("lengthRadioButton.text")); // NOI18N
        lengthRadioButton.setName("lengthRadioButton"); // NOI18N

        numberRadioButton.setText(resourceMap.getString("numberRadioButton.text")); // NOI18N
        numberRadioButton.setEnabled(false);
        numberRadioButton.setName("numberRadioButton"); // NOI18N

        javax.swing.GroupLayout methodPanelLayout = new javax.swing.GroupLayout(methodPanel);
        methodPanel.setLayout(methodPanelLayout);
        methodPanelLayout.setHorizontalGroup(
            methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(methodPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(volumeRadioButton)
                    .addComponent(lengthRadioButton)
                    .addComponent(numberRadioButton))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        methodPanelLayout.setVerticalGroup(
            methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(methodPanelLayout.createSequentialGroup()
                .addComponent(volumeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lengthRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numberRadioButton))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lengthLabel)
                        .addGap(18, 18, 18)
                        .addComponent(lengthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(partPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addComponent(cancelButton))
                    .addComponent(methodPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(partPanel, 0, 98, Short.MAX_VALUE)
                    .addComponent(methodPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lengthLabel)
                    .addComponent(okButton)
                    .addComponent(cancelButton)
                    .addComponent(lengthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void synapticRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_synapticRadioButtonActionPerformed
        numberRadioButton.setEnabled(true);
        numberRadioButton.setSelected(true);
        volumeRadioButton.setEnabled(false);
        lengthRadioButton.setEnabled(false);
    }//GEN-LAST:event_synapticRadioButtonActionPerformed

    private void dendriticRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dendriticRadioButtonActionPerformed
        numberRadioButton.setEnabled(false);
        volumeRadioButton.setEnabled(true);
        volumeRadioButton.setSelected(true);
        lengthRadioButton.setEnabled(true);
    }//GEN-LAST:event_dendriticRadioButtonActionPerformed

    private void axonalRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_axonalRadioButtonActionPerformed
        numberRadioButton.setEnabled(false);
        volumeRadioButton.setEnabled(true);
        volumeRadioButton.setSelected(true);
        lengthRadioButton.setEnabled(true);
    }//GEN-LAST:event_axonalRadioButtonActionPerformed

    private void lengthTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lengthTextFieldKeyReleased
        setLabel();
    }//GEN-LAST:event_lengthTextFieldKeyReleased

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    public DensityPart getDensityPart() {
        if (dendriticRadioButton.isSelected()) {
            return DensityPart.DENDRITIC;
        } else if (axonalRadioButton.isSelected()) {
            return DensityPart.AXONAL;
        } else {
            return DensityPart.SYNAPTIC;
        }
    }

    public DensityMethod getDensityMethod() {
        if (volumeRadioButton.isSelected()) {
            return DensityMethod.VOLUME;
        } else if (lengthRadioButton.isSelected()) {
            return DensityMethod.LENGTH;
        } else {
            return DensityMethod.NUMBER;
        }
    }

    public void setLabel() {
        String length = lengthTextField.getText();
        if (isValid(length)) {
            int value = getValue(length);
            voxelLenght = value;
            //lengthTextField.setText(Integer.toString(value));
        } else {
            lengthTextField.setText("");
            voxelLenght = 0;
        }
    }

    public int getVoxelLenght() {
        if(voxelLenght < 0) {
            setLabel();
        }
        if(voxelLenght < 5) {
            voxelLenght = 5;
        }
        logger.info("voxel lenght: " + voxelLenght);
        return voxelLenght;
    }

    public int getValue(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean isValid(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        NeuGenLogger.initLogger();
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                DensityDialog dialog = new DensityDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
                if (dialog.getReturnStatus() == DensityDialog.RET_CANCEL
                        || dialog.getReturnStatus() == DensityDialog.RET_OK) {
                    dialog.dispose();
                    System.exit(0);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton axonalRadioButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JRadioButton dendriticRadioButton;
    private javax.swing.JLabel lengthLabel;
    private javax.swing.JRadioButton lengthRadioButton;
    private javax.swing.JTextField lengthTextField;
    private javax.swing.ButtonGroup methodButtonGroup;
    private javax.swing.JPanel methodPanel;
    private javax.swing.JRadioButton numberRadioButton;
    private javax.swing.JButton okButton;
    private javax.swing.ButtonGroup partButtonGroup;
    private javax.swing.JPanel partPanel;
    private javax.swing.JRadioButton synapticRadioButton;
    private javax.swing.JRadioButton volumeRadioButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;

    public enum DensityPart {

        AXONAL, DENDRITIC, SYNAPTIC, IMAGE;
    }

    public enum DensityMethod {

        LENGTH, NUMBER, VOLUME;
    }
}
