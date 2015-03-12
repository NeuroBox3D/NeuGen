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
 * SliderDialog.java
 *
 * Created on 12.08.2010, 12:22:29
 */
package org.neugen.gui;

import org.neugen.utils.NeuGenLogger;
import javax.swing.JTextField;
import javax.vecmath.Point3f;
import org.apache.log4j.Logger;

/**
 *
 * @author Sergei Wolf
 */
public class SliderDialog extends javax.swing.JDialog {

    /** use to log messages */
    private static Logger logger = Logger.getLogger(SliderDialog.class.getName());
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private Point3f coordOrigin;
    private Point3f spacing;
    private int imgWidth;
    private int imgHeight;

    /** Creates new form SliderDialog */
    public SliderDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        coordOrigin = new Point3f(-1, -1, -1);
        spacing = new Point3f(-1, -1, -1);
        imgWidth = -1;
        imgHeight = -1;
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
        imagePanel = new javax.swing.JPanel();
        widthLabel = new javax.swing.JLabel();
        widthTextField = new javax.swing.JTextField();
        heightLabel = new javax.swing.JLabel();
        heightTextField = new javax.swing.JTextField();
        coordOriginPanel = new javax.swing.JPanel();
        coordXLabel = new javax.swing.JLabel();
        coordYLabel = new javax.swing.JLabel();
        coordZLabel = new javax.swing.JLabel();
        coordXTextField = new javax.swing.JTextField();
        coordYTextField = new javax.swing.JTextField();
        coordZTextField = new javax.swing.JTextField();
        spacingPanel = new javax.swing.JPanel();
        spacingXLabel = new javax.swing.JLabel();
        spacingYLabel = new javax.swing.JLabel();
        spacingZLabel = new javax.swing.JLabel();
        spacingXTextField = new javax.swing.JTextField();
        spacingYTextField = new javax.swing.JTextField();
        spacingZTextField = new javax.swing.JTextField();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.neugen.gui.NeuGenApp.class).getContext().getResourceMap(SliderDialog.class);
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

        imagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("imagePanel.border.title"))); // NOI18N
        imagePanel.setName("imagePanel"); // NOI18N

        widthLabel.setText(resourceMap.getString("widthLabel.text")); // NOI18N
        widthLabel.setToolTipText(resourceMap.getString("widthLabel.toolTipText")); // NOI18N
        widthLabel.setName("widthLabel"); // NOI18N

        widthTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        widthTextField.setText(resourceMap.getString("widthTextField.text")); // NOI18N
        widthTextField.setName("widthTextField"); // NOI18N
        widthTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                widthTextFieldKeyReleased(evt);
            }
        });

        heightLabel.setText(resourceMap.getString("heightLabel.text")); // NOI18N
        heightLabel.setToolTipText(resourceMap.getString("heightLabel.toolTipText")); // NOI18N
        heightLabel.setName("heightLabel"); // NOI18N

        heightTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        heightTextField.setText(resourceMap.getString("heightTextField.text")); // NOI18N
        heightTextField.setName("heightTextField"); // NOI18N
        heightTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                heightTextFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(heightLabel)
                    .addComponent(widthLabel))
                .addGap(18, 18, 18)
                .addGroup(imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(heightTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(widthTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addContainerGap())
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(widthLabel)
                    .addComponent(widthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(heightLabel)
                    .addComponent(heightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        coordOriginPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("coordOriginPanel.border.title"))); // NOI18N
        coordOriginPanel.setName("coordOriginPanel"); // NOI18N

        coordXLabel.setText(resourceMap.getString("coordXLabel.text")); // NOI18N
        coordXLabel.setName("coordXLabel"); // NOI18N

        coordYLabel.setText(resourceMap.getString("coordYLabel.text")); // NOI18N
        coordYLabel.setName("coordYLabel"); // NOI18N

        coordZLabel.setText(resourceMap.getString("coordZLabel.text")); // NOI18N
        coordZLabel.setName("coordZLabel"); // NOI18N

        coordXTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        coordXTextField.setText(resourceMap.getString("coordXTextField.text")); // NOI18N
        coordXTextField.setName("coordXTextField"); // NOI18N
        coordXTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                coordXTextFieldKeyReleased(evt);
            }
        });

        coordYTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        coordYTextField.setText(resourceMap.getString("coordYTextField.text")); // NOI18N
        coordYTextField.setName("coordYTextField"); // NOI18N
        coordYTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                coordYTextFieldKeyReleased(evt);
            }
        });

        coordZTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        coordZTextField.setText(resourceMap.getString("coordZTextField.text")); // NOI18N
        coordZTextField.setName("coordZTextField"); // NOI18N
        coordZTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                coordZTextFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout coordOriginPanelLayout = new javax.swing.GroupLayout(coordOriginPanel);
        coordOriginPanel.setLayout(coordOriginPanelLayout);
        coordOriginPanelLayout.setHorizontalGroup(
            coordOriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(coordOriginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(coordOriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(coordZLabel)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, coordOriginPanelLayout.createSequentialGroup()
                        .addGroup(coordOriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(coordYLabel)
                            .addComponent(coordXLabel))
                        .addGap(18, 18, 18)
                        .addGroup(coordOriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(coordXTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(coordZTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                            .addComponent(coordYTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))))
                .addGap(36, 36, 36))
        );
        coordOriginPanelLayout.setVerticalGroup(
            coordOriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(coordOriginPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(coordOriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(coordXLabel)
                    .addComponent(coordXTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(coordOriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(coordYLabel)
                    .addComponent(coordYTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(coordOriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(coordZLabel)
                    .addComponent(coordZTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        spacingPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("spacingPanel.border.title"))); // NOI18N
        spacingPanel.setName("spacingPanel"); // NOI18N

        spacingXLabel.setText(resourceMap.getString("spacingXLabel.text")); // NOI18N
        spacingXLabel.setName("spacingXLabel"); // NOI18N

        spacingYLabel.setText(resourceMap.getString("spacingYLabel.text")); // NOI18N
        spacingYLabel.setName("spacingYLabel"); // NOI18N

        spacingZLabel.setText(resourceMap.getString("spacingZLabel.text")); // NOI18N
        spacingZLabel.setName("spacingZLabel"); // NOI18N

        spacingXTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        spacingXTextField.setText(resourceMap.getString("spacingXTextField.text")); // NOI18N
        spacingXTextField.setName("spacingXTextField"); // NOI18N
        spacingXTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                spacingXTextFieldKeyReleased(evt);
            }
        });

        spacingYTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        spacingYTextField.setText(resourceMap.getString("spacingYTextField.text")); // NOI18N
        spacingYTextField.setName("spacingYTextField"); // NOI18N
        spacingYTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                spacingYTextFieldKeyReleased(evt);
            }
        });

        spacingZTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        spacingZTextField.setText(resourceMap.getString("spacingZTextField.text")); // NOI18N
        spacingZTextField.setName("spacingZTextField"); // NOI18N
        spacingZTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                spacingZTextFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout spacingPanelLayout = new javax.swing.GroupLayout(spacingPanel);
        spacingPanel.setLayout(spacingPanelLayout);
        spacingPanelLayout.setHorizontalGroup(
            spacingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(spacingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(spacingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(spacingPanelLayout.createSequentialGroup()
                        .addComponent(spacingZLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spacingZTextField))
                    .addGroup(spacingPanelLayout.createSequentialGroup()
                        .addComponent(spacingYLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spacingYTextField))
                    .addGroup(spacingPanelLayout.createSequentialGroup()
                        .addComponent(spacingXLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spacingXTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        spacingPanelLayout.setVerticalGroup(
            spacingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(spacingPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(spacingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spacingXLabel)
                    .addComponent(spacingXTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(spacingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spacingYLabel)
                    .addComponent(spacingYTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(spacingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spacingZTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spacingZLabel)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(imagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(coordOriginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spacingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cancelButton)
                        .addGap(24, 24, 24))))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(imagePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(coordOriginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(spacingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
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

    private void widthTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_widthTextFieldKeyReleased
        imgWidth = setLabel(widthTextField, imgWidth);
        if (imgWidth < 10 || imgWidth > 3000) {
            imgWidth = 512;
        }
        //logger.info("imgWidth: " + imgWidth);
    }//GEN-LAST:event_widthTextFieldKeyReleased

    private void heightTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_heightTextFieldKeyReleased
        imgHeight = setLabel(heightTextField, imgHeight);
        if (imgHeight < 10 || imgHeight > 3000) {
            imgHeight = 512;
        }
        //logger.info("imgHeight: " + imgHeight);
    }//GEN-LAST:event_heightTextFieldKeyReleased

    private void coordXTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_coordXTextFieldKeyReleased
        coordOrigin.x = setLabel(coordXTextField, coordOrigin.x);
        if (coordOrigin.x < 0) {
            coordOrigin.x = 0;
        }
        //logger.info("x origin: " + coordOrigin.x);
    }//GEN-LAST:event_coordXTextFieldKeyReleased

    private void coordYTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_coordYTextFieldKeyReleased
        coordOrigin.y = setLabel(coordYTextField, coordOrigin.y);
        if (coordOrigin.y < 0) {
            coordOrigin.y = 0;
        }
        //logger.info("y origin: " + coordOrigin.y);
    }//GEN-LAST:event_coordYTextFieldKeyReleased

    private void coordZTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_coordZTextFieldKeyReleased
        coordOrigin.z = setLabel(coordZTextField, coordOrigin.z);
        if (coordOrigin.z < 0) {
            coordOrigin.z = 0;
        }
        //logger.info("z origin: " + coordOrigin.z);
    }//GEN-LAST:event_coordZTextFieldKeyReleased

    private void spacingXTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spacingXTextFieldKeyReleased
        spacing.x = setLabel(spacingXTextField, spacing.x);
        if (spacing.x < 0.1f) {
            spacing.x = 0.2f;
        }

    }//GEN-LAST:event_spacingXTextFieldKeyReleased

    private void spacingYTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spacingYTextFieldKeyReleased
        spacing.y = setLabel(spacingYTextField, spacing.y);
        if (spacing.y < 0.1f) {
            spacing.y = 0.2f;
        }
    }//GEN-LAST:event_spacingYTextFieldKeyReleased

    private void spacingZTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spacingZTextFieldKeyReleased
        spacing.z = setLabel(spacingZTextField, spacing.z);
        if (spacing.z < 0.1f) {
            spacing.z = 0.2f;
        }
    }//GEN-LAST:event_spacingZTextFieldKeyReleased

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    public int getIntValue(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public float getFloatValue(String text) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    public boolean intIsValid(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean floatIsValid(String text) {
        try {
            Float.parseFloat(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int setLabel(JTextField textField, int intValue) {
        String length = textField.getText();
        if (intIsValid(length)) {
            int value = getIntValue(length);
            intValue = value;
            //textField.setText(Integer.toString(value));
            return intValue;
        } else {
            textField.setText("");
            return intValue = 0;
        }
    }

    public float setLabel(JTextField textField, float floatValue) {
        String length = textField.getText();
        if (floatIsValid(length)) {
            float value = getFloatValue(length);
            floatValue = value;
            //textField.setText(Float.toString(value));
            return floatValue;
        } else {
            textField.setText("");
            return floatValue = 0.0f;
        }
    }

    public Point3f getCoordOrigin() {
        if (coordOrigin.x < 0) {
            coordOrigin.x = setLabel(coordXTextField, coordOrigin.x);
        }

        if (coordOrigin.y < 0) {
            coordOrigin.y = setLabel(coordYTextField, coordOrigin.y);
        }

        if (coordOrigin.z < 0) {
            coordOrigin.z = setLabel(coordZTextField, coordOrigin.z);
        }
        return coordOrigin;
    }

    public int getImgHeight() {
        if(imgHeight < 0) {
            imgHeight = setLabel(heightTextField, imgHeight);
        }
        return imgHeight;
    }

    public int getImgWidth() {
        if(imgWidth < 0) {
            imgWidth = setLabel(widthTextField, imgWidth);
        }
        return imgWidth;
    }

    public Point3f getSpacing() {
        if(spacing.x < 0) {
            spacing.x = setLabel(spacingXTextField, spacing.x);
        }
        if(spacing.y < 0) {
            spacing.y = setLabel(spacingYTextField, spacing.y);
        }
        if (spacing.z < 0) {
            spacing.z = setLabel(spacingZTextField, spacing.z);
        }
        return spacing;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        NeuGenLogger.initLogger();
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                SliderDialog dialog = new SliderDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel coordOriginPanel;
    private javax.swing.JLabel coordXLabel;
    private javax.swing.JTextField coordXTextField;
    private javax.swing.JLabel coordYLabel;
    private javax.swing.JTextField coordYTextField;
    private javax.swing.JLabel coordZLabel;
    private javax.swing.JTextField coordZTextField;
    private javax.swing.JLabel heightLabel;
    private javax.swing.JTextField heightTextField;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel spacingPanel;
    private javax.swing.JLabel spacingXLabel;
    private javax.swing.JTextField spacingXTextField;
    private javax.swing.JLabel spacingYLabel;
    private javax.swing.JTextField spacingYTextField;
    private javax.swing.JLabel spacingZLabel;
    private javax.swing.JTextField spacingZTextField;
    private javax.swing.JLabel widthLabel;
    private javax.swing.JTextField widthTextField;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
