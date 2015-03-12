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
 * VoxelDialog.java
 *
 * Created on 09.03.2011, 22:57:24
 */
package org.neugen.gui;

import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.JTextField;
import javax.vecmath.Point3i;
import org.apache.log4j.Logger;
import org.neugen.utils.NeuGenLogger;

/**
 *
 * @author Sergei
 */
public class VoxelDialog extends javax.swing.JDialog {

    /** use to log messages */
    private static Logger logger = Logger.getLogger(VoxelDialog.class.getName());
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    /** Creates new form VoxelDialog */
    public VoxelDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.properties = new Properties();
        this.dialogUtil = new Utils(logger, properties);
        initComponents();
        //okButton.setMnemonic(KeyEvent.VK_ENTER);
        NeuGenLogger.initLogger();      
        readProp();
        initParam();
    }

    private void initParam() {
        String text = ".text";
        this.lengthTextFieldX.setText(properties.getProperty(lengthTextFieldX.getName() + text));
        //logger.info("voxel x: " + lengthTextFieldX.getText());
        this.lengthTextFieldY.setText(properties.getProperty(lengthTextFieldY.getName()+ text));
        //logger.info("voxel y: " + lengthTextFieldY.getText());
        this.lengthTextFieldZ.setText(properties.getProperty(lengthTextFieldZ.getName()+ text));
        //logger.info("voxel z: " + lengthTextFieldZ.getText());
        this.weightTextField.setText(properties.getProperty(weightTextField.getName()+ text));
        this.widthTextField.setText(properties.getProperty(widthTextField.getName()+ text));
        this.depthTextField.setText(properties.getProperty(depthTextField.getName()+ text));
        this.heightTextField.setText(properties.getProperty(heightTextField.getName()+ text));
        this.thresholdTextField.setText(properties.getProperty(thresholdTextField.getName()+ text));
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
        lengthLabel = new javax.swing.JLabel();
        lengthTextFieldX = new javax.swing.JTextField();
        lengthLabel1 = new javax.swing.JLabel();
        weightTextField = new javax.swing.JTextField();
        lengthTextFieldY = new javax.swing.JTextField();
        lengthTextFieldZ = new javax.swing.JTextField();
        widthLabel = new javax.swing.JLabel();
        lengthLabel3 = new javax.swing.JLabel();
        widthTextField = new javax.swing.JTextField();
        heightTextField = new javax.swing.JTextField();
        lengthLabel4 = new javax.swing.JLabel();
        depthTextField = new javax.swing.JTextField();
        lengthLabel2 = new javax.swing.JLabel();
        thresholdTextField = new javax.swing.JTextField();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.neugen.gui.NeuGenApp.class).getContext().getResourceMap(VoxelDialog.class);
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

        lengthLabel.setText(resourceMap.getString("lengthLabel.text")); // NOI18N
        lengthLabel.setName("lengthLabel"); // NOI18N

        lengthTextFieldX.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        lengthTextFieldX.setText(resourceMap.getString("lengthTextFieldX.text")); // NOI18N
        lengthTextFieldX.setToolTipText(resourceMap.getString("lengthTextFieldX.toolTipText")); // NOI18N
        lengthTextFieldX.setName("lengthTextFieldX"); // NOI18N
        lengthTextFieldX.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lengthTextFieldXKeyReleased(evt);
            }
        });

        lengthLabel1.setText(resourceMap.getString("lengthLabel1.text")); // NOI18N
        lengthLabel1.setName("lengthLabel1"); // NOI18N

        weightTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        weightTextField.setText(resourceMap.getString("weightTextField.text")); // NOI18N
        weightTextField.setToolTipText(resourceMap.getString("weightTextField.toolTipText")); // NOI18N
        weightTextField.setName("weightTextField"); // NOI18N
        weightTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                weightTextFieldKeyReleased(evt);
            }
        });

        lengthTextFieldY.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        lengthTextFieldY.setText(resourceMap.getString("lengthTextFieldY.text")); // NOI18N
        lengthTextFieldY.setToolTipText(resourceMap.getString("lengthTextFieldY.toolTipText")); // NOI18N
        lengthTextFieldY.setName("lengthTextFieldY"); // NOI18N
        lengthTextFieldY.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lengthTextFieldYKeyReleased(evt);
            }
        });

        lengthTextFieldZ.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        lengthTextFieldZ.setText(resourceMap.getString("lengthTextFieldZ.text")); // NOI18N
        lengthTextFieldZ.setToolTipText(resourceMap.getString("lengthTextFieldZ.toolTipText")); // NOI18N
        lengthTextFieldZ.setName("lengthTextFieldZ"); // NOI18N
        lengthTextFieldZ.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lengthTextFieldZKeyReleased(evt);
            }
        });

        widthLabel.setText(resourceMap.getString("widthLabel.text")); // NOI18N
        widthLabel.setName("widthLabel"); // NOI18N

        lengthLabel3.setText(resourceMap.getString("lengthLabel3.text")); // NOI18N
        lengthLabel3.setName("lengthLabel3"); // NOI18N

        widthTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        widthTextField.setText(resourceMap.getString("widthTextField.text")); // NOI18N
        widthTextField.setName("widthTextField"); // NOI18N
        widthTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                widthTextFieldKeyReleased(evt);
            }
        });

        heightTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        heightTextField.setText(resourceMap.getString("heightTextField.text")); // NOI18N
        heightTextField.setName("heightTextField"); // NOI18N
        heightTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                heightTextFieldKeyReleased(evt);
            }
        });

        lengthLabel4.setText(resourceMap.getString("lengthLabel4.text")); // NOI18N
        lengthLabel4.setName("lengthLabel4"); // NOI18N

        depthTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        depthTextField.setText(resourceMap.getString("depthTextField.text")); // NOI18N
        depthTextField.setName("depthTextField"); // NOI18N
        depthTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                depthTextFieldKeyReleased(evt);
            }
        });

        lengthLabel2.setText(resourceMap.getString("lengthLabel2.text")); // NOI18N
        lengthLabel2.setName("lengthLabel2"); // NOI18N

        thresholdTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        thresholdTextField.setText(resourceMap.getString("thresholdTextField.text")); // NOI18N
        thresholdTextField.setToolTipText(resourceMap.getString("thresholdTextField.toolTipText")); // NOI18N
        thresholdTextField.setName("thresholdTextField"); // NOI18N
        thresholdTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                thresholdTextFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lengthLabel1)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lengthLabel, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(widthLabel)
                                        .addComponent(lengthLabel3)
                                        .addComponent(lengthLabel4)
                                        .addComponent(lengthLabel2))
                                    .addGap(24, 24, 24)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(thresholdTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(depthTextField)
                                            .addComponent(weightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(lengthTextFieldX, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lengthTextFieldY, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(widthTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                                            .addComponent(heightTextField)))))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lengthTextFieldZ, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lengthLabel)
                    .addComponent(lengthTextFieldX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lengthTextFieldY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lengthTextFieldZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lengthLabel2)
                    .addComponent(thresholdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lengthLabel1)
                    .addComponent(weightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(widthLabel)
                    .addComponent(widthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lengthLabel3)
                    .addComponent(heightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(depthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lengthLabel4))
                .addGap(53, 53, 53)
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

    private void lengthTextFieldXKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lengthTextFieldXKeyReleased
        this.voxelLenght.x = dialogUtil.setIntegerLabel(this.lengthTextFieldX, ".text");
}//GEN-LAST:event_lengthTextFieldXKeyReleased

    private void weightTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_weightTextFieldKeyReleased
        this.pixelWeight = dialogUtil.setIntegerLabel(this.weightTextField, ".text");
    }//GEN-LAST:event_weightTextFieldKeyReleased

    private void lengthTextFieldYKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lengthTextFieldYKeyReleased
        this.voxelLenght.y = dialogUtil.setIntegerLabel(this.lengthTextFieldY, ".text");
    }//GEN-LAST:event_lengthTextFieldYKeyReleased

    private void lengthTextFieldZKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lengthTextFieldZKeyReleased
        this.voxelLenght.z = dialogUtil.setIntegerLabel(this.lengthTextFieldZ, ".text");
    }//GEN-LAST:event_lengthTextFieldZKeyReleased

    private void widthTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_widthTextFieldKeyReleased
        this.width = dialogUtil.setFloatLabel(this.widthTextField, ".text");
    }//GEN-LAST:event_widthTextFieldKeyReleased

    private void heightTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_heightTextFieldKeyReleased
        this.height = dialogUtil.setFloatLabel(this.heightTextField, ".text");
    }//GEN-LAST:event_heightTextFieldKeyReleased

    private void depthTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_depthTextFieldKeyReleased
        this.depth = dialogUtil.setFloatLabel(this.depthTextField, ".text");
    }//GEN-LAST:event_depthTextFieldKeyReleased

    private void thresholdTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thresholdTextFieldKeyReleased
       this.threshold = dialogUtil.setIntegerLabel(this.thresholdTextField, ".text");
    }//GEN-LAST:event_thresholdTextFieldKeyReleased

    private void doClose(int retStatus) {
        if (retStatus == RET_OK) {
            writeProp();
        }
        returnStatus = retStatus;
        setVisible(false);        
        dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                final VoxelDialog dialog = new VoxelDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField depthTextField;
    private javax.swing.JTextField heightTextField;
    private javax.swing.JLabel lengthLabel;
    private javax.swing.JLabel lengthLabel1;
    private javax.swing.JLabel lengthLabel2;
    private javax.swing.JLabel lengthLabel3;
    private javax.swing.JLabel lengthLabel4;
    private javax.swing.JTextField lengthTextFieldX;
    private javax.swing.JTextField lengthTextFieldY;
    private javax.swing.JTextField lengthTextFieldZ;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField thresholdTextField;
    private javax.swing.JTextField weightTextField;
    private javax.swing.JLabel widthLabel;
    private javax.swing.JTextField widthTextField;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
    private Point3i voxelLenght = new Point3i(-1, -1, -1);
    private int pixelWeight = -1, threshold=-1;
    private float width = -1, height = -1, depth = -1;
    private Properties properties;
    private File f;
    private Utils dialogUtil;

    private void writeProp() {
        try {
            logger.info("write voxel prop to: " + f.getAbsolutePath());
            FileOutputStream propOutFile = new FileOutputStream(f);
            properties.store(propOutFile, "Voxel Dialog");
            properties.clear();
            propOutFile.flush();
            propOutFile.close();
        } catch (FileNotFoundException e) {
            logger.error("Can’t find " + f, e);
        } catch (IOException e) {
            logger.error("I/O failed.", e);
        }
    }

    private void readProp() {
        BufferedInputStream stream;
        try {
            File locVoxelProFile = new File("config/gui/VoxelDialog.properties");
            logger.info("read file: " + locVoxelProFile.getAbsolutePath());
            if(locVoxelProFile.exists()) {
                f = locVoxelProFile;
                logger.info("read local file: " + f.getAbsolutePath());
                stream = new BufferedInputStream(new FileInputStream(locVoxelProFile));
                properties.load(stream);
                stream.close();
            } else {
                InputStream is = VoxelDialog.class.getResourceAsStream("resources/VoxelDialog.properties");
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

    public float getDepth() {
        if (depth < 0) {
            depth = this.dialogUtil.setFloatLabel(depthTextField, ".text");
        }
        return depth;
    }

    public float getImgHeight() {
        if (height < 0) {
            height = this.dialogUtil.setFloatLabel(heightTextField, ".text");
        }
        return height;
    }

    public float getImgWidth() {
        if (width < 0) {
            width = this.dialogUtil.setFloatLabel(widthTextField, ".text");
        }
        return width;
    }

    public Point3i getVoxelLenght() {

        if (voxelLenght.x < 0) {
            voxelLenght.x = this.dialogUtil.setIntegerLabel(lengthTextFieldX, ".text");
        }

        if (voxelLenght.y < 0) {
            voxelLenght.y = this.dialogUtil.setIntegerLabel(lengthTextFieldY, ".text");
        }

        if (voxelLenght.z < 0) {
            voxelLenght.z = this.dialogUtil.setIntegerLabel(lengthTextFieldZ, ".text");
        }

        if (voxelLenght.x < 1) {
            voxelLenght.x = 1;
        }

        if (voxelLenght.y < 1) {
            voxelLenght.y = 1;
        }

        if (voxelLenght.z < 1) {
            voxelLenght.z = 1;
        }
        return voxelLenght;
    }

    public int getWeight() {
        if (pixelWeight < 0) {
            pixelWeight = this.dialogUtil.setIntegerLabel(weightTextField, ".text");
        }
        if (pixelWeight < 1) {
            pixelWeight = 1;
        }
        logger.info("pixelWeight: " + pixelWeight);
        return pixelWeight;
    }

    public int getThreshold() {
        if (threshold < 0) {
            threshold = this.dialogUtil.setIntegerLabel(thresholdTextField, ".text");
        }
        if (threshold < 1) {
            threshold = 1;
        }
        logger.info("threshold: " + threshold);
        return threshold;
    }
}
