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
 * NeuGenDensityVisualizationConfiguration.java
 *
 * Created on 18. Dezember 2007, 17:49
 */
package org.neugen.vrl;

import org.neugen.gui.*;

import java.awt.GridBagConstraints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;
import org.neugen.datastructures.VolumeOfVoxels;
//import org.neugen.gui.resources.Resources;

/**
 * @author Simone Eberhard
 */
@SuppressWarnings("serial")
public final class DensityVisualizationConfigDialog extends JFrame {

    /** use to log messages */
    private static Logger logger = Logger.getLogger(DensityVisualizationConfigDialog.class.getName());
    private VolumeOfVoxels volumeOfVoxels;
    private static DensityVisualizationConfigDialog instance;
    private ButtonGroup buttonGroup1;
    private JButton jButtonVisualize;
    private JComboBox jComboBoxBackgroundColor;
    private JComboBox jComboBoxColor1;
    private JComboBox jComboBoxColor2;
    private JComboBox jComboBoxColor3;
    private JComboBox jComboBoxColor4;
    private JComboBox jComboBoxNumberOfValues;
    private JLabel jLabelBackGroundColor;
    private JLabel jLabelColor1;
    private JLabel jLabelColor2;
    private JLabel jLabelColor3;
    private JLabel jLabelColor4;
    private JLabel jLabelNumberOfValues;
    private JLabel jLabelPercentT1;
    private JLabel jLabelPercentT2;
    private JLabel jLabelPercentT3;
    private JLabel jLabelPercentT4;
    private JLabel jLabelPercentTolerance;
    private JLabel jLabelPercentV1;
    private JLabel jLabelPercentV2;
    private JLabel jLabelPercentV3;
    private JLabel jLabelPercentV4;
    private JLabel jLabelToleranceOfDivision;
    private JLabel jLabelTransparency1;
    private JLabel jLabelTransparency2;
    private JLabel jLabelTransparency3;
    private JLabel jLabelTransparency4;
    private JLabel jLabelValue1;
    private JLabel jLabelValue2;
    private JLabel jLabelValue3;
    private JLabel jLabelValue4;
    private JPanel jPanelCOlorsOfVisualization;
    private JPanel jPanelTypeOfVisualization;
    private JPanel jPanelValuesOfVisualization;
    private JRadioButton jRadioButtonVisualizeWithConvexHull;
    private JRadioButton jRadioButtonVisualizeWithCubes;
    private JRadioButton jRadioButtonVisualizeWithDividedConvexHull;
    //Transparency
    private JTextField jTextFieldT1; //50
    private JTextField jTextFieldT2; //30
    private JTextField jTextFieldT3; //20
    private JTextField jTextFieldT4; //0
    private JTextField jTextFieldToleranceOfDivision;
    // Percentage
    private JTextField jTextFieldValue1; //80
    private JTextField jTextFieldValue2; //25
    private JTextField jTextFieldValue3; //10
    private JTextField jTextFieldValue4; //5
    private BranchGroup scene;
    private Properties properties;
    private File propFile;
    private JPanel canvasParent;
    private Canvas3D canvas3D;
    private VRLDensityVisualizationTask.Density dens;
    private final VRLDensityVisualizationTask task;
    

    public String getJarFolder() {
        // get name and path
        String name = getClass().getName().replace('.', '/');
        name = getClass().getResource("/" + name + ".class").toString();
        // remove junk
        name = name.substring(0, name.indexOf(".jar"));
        name = name.substring(name.lastIndexOf(':') - 1, name.lastIndexOf('/') + 1).replace('%', ' ');
        // remove escape characters
        String s = "";
        for (int k = 0; k < name.length(); k++) {
            s += name.charAt(k);
            if (name.charAt(k) == ' ') {
                k += 2;
            }
        }
        // replace '/' with system separator char
        return s.replace('/', File.separatorChar);
    }

    private void writeProp() {
        try {
            logger.info("write dens prop to: " + propFile.getAbsolutePath());
            FileOutputStream propOutFile = new FileOutputStream(propFile);
            properties.store(propOutFile, "Dens Dialog");
            properties.clear();
            propOutFile.flush();
            propOutFile.close();
        } catch (FileNotFoundException e) {
            logger.error("Can’t find " + propFile, e);
        } catch (IOException e) {
            logger.error("I/O failed.", e);
        }
    }

    private void initParam() {
        this.jTextFieldT1.setText(properties.getProperty("jTextFieldT1"));
        this.jTextFieldT1.setToolTipText(properties.getProperty("jTextFieldT1.toolTipText"));
        this.jTextFieldT1.setName("jTextFieldT1");

        this.jTextFieldT2.setText(properties.getProperty("jTextFieldT2"));
        this.jTextFieldT2.setToolTipText(properties.getProperty("jTextFieldT2.toolTipText"));
        this.jTextFieldT2.setName("jTextFieldT2");

        this.jTextFieldT3.setText(properties.getProperty("jTextFieldT3"));
        this.jTextFieldT3.setToolTipText(properties.getProperty("jTextFieldT3.toolTipText"));
        this.jTextFieldT3.setName("jTextFieldT3");

        this.jTextFieldT4.setText(properties.getProperty("jTextFieldT4"));
        this.jTextFieldT4.setToolTipText(properties.getProperty("jTextFieldT4.toolTipText"));
        this.jTextFieldT4.setName("jTextFieldT4");

        this.jTextFieldToleranceOfDivision.setText(properties.getProperty("jTextFieldToleranceOfDivision"));
        this.jTextFieldToleranceOfDivision.setToolTipText(properties.getProperty("jTextFieldToleranceOfDivision.toolTipText"));
        this.jTextFieldToleranceOfDivision.setName("jTextFieldToleranceOfDivision");

        this.jTextFieldValue1.setText(properties.getProperty("jTextFieldValue1"));
        this.jTextFieldValue1.setToolTipText(properties.getProperty("jTextFieldValue1.toolTipText"));
        this.jTextFieldValue1.setName("jTextFieldValue1");

        this.jTextFieldValue2.setText(properties.getProperty("jTextFieldValue2"));
        this.jTextFieldValue2.setToolTipText(properties.getProperty("jTextFieldValue2.toolTipText"));
        this.jTextFieldValue2.setName("jTextFieldValue2");

        this.jTextFieldValue3.setText(properties.getProperty("jTextFieldValue3"));
        this.jTextFieldValue3.setToolTipText(properties.getProperty("jTextFieldValue3.toolTipText"));
        this.jTextFieldValue3.setName("jTextFieldValue3");

        this.jTextFieldValue4.setText(properties.getProperty("jTextFieldValue4"));
        this.jTextFieldValue4.setToolTipText(properties.getProperty("jTextFieldValue4.toolTipText"));
        this.jTextFieldValue4.setName("jTextFieldValue4");
    }

    private void readProp() {
        BufferedInputStream stream;
        try {
            properties = new Properties();
            File locVoxelProFile = new File("config/gui/DensityConfiguration.properties");
            logger.info("read file: " + locVoxelProFile.getAbsolutePath());
            if (locVoxelProFile.exists()) {
                propFile = locVoxelProFile;
                logger.info("read local file: " + propFile.getAbsolutePath());
                stream = new BufferedInputStream(new FileInputStream(locVoxelProFile));
                properties.load(stream);
                stream.close();
            } else {
                InputStream is = DensityVisualizationConfigDialog.class.getResourceAsStream("resources/DensityConfiguration.properties");
                properties.load(is);
                is.close();
                propFile = locVoxelProFile;
            }
        } catch (FileNotFoundException ex) {
            logger.error(ex, ex);
        } catch (IOException ex) {
            logger.error(ex, ex);
        }
    }

    /** Creates new form NeuGenDensityVisualizationConfiguration */
    public DensityVisualizationConfigDialog(VRLDensityVisualizationTask task, VolumeOfVoxels volumeOfVoxels,
            VRLDensityVisualizationTask.Density dens, JPanel canvasParent, Canvas3D canvas3D) {
        this.task = task;
        this.volumeOfVoxels = volumeOfVoxels;
        this.canvasParent = canvasParent;
        this.dens = dens;
        this.canvas3D = canvas3D;
        
        initComponents();
        readProp();
        initParam();

    }

    /** Creates new form NeuGenDensityVisualizationConfiguration */
    public DensityVisualizationConfigDialog(VRLDensityVisualizationTask task, VolumeOfVoxels volumeOfVoxels,
            VRLDensityVisualizationTask.Density dens, BranchGroup bg, JPanel canvasParent, Canvas3D canvas3D) {
        this.task = task;
        this.volumeOfVoxels = volumeOfVoxels;
        this.scene = bg;
        this.canvasParent = canvasParent;
        this.dens = dens;
        this.canvas3D = canvas3D;
        
        initComponents();
        readProp();
        initParam();

    }

    public float setFloatLabel(JTextField textField) {
        String length = textField.getText();
        if (isFloatValid(length)) {
            float value = getFloatValue(length);
            properties.setProperty(textField.getName(), length);
            return value;
            //lengthTextField.setText(Integer.toString(value));
        } else {
            textField.setText("");
            properties.setProperty(textField.getName(), "");
            return 0.0f;
        }
    }

    public boolean isFloatValid(String text) {
        try {
            Float.parseFloat(text);
            return true;
        } catch (NumberFormatException e) {
            logger.error(e);
            return false;
        }
    }

    public float getFloatValue(String text) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            logger.error(e);
            return 0.0f;
        }
    }

    public void setVolumeOfVoxels(VolumeOfVoxels volumeOfVoxels) {
        this.volumeOfVoxels = volumeOfVoxels;
    }

    public static DensityVisualizationConfigDialog getInstance() {
        return instance;
    }

    public static void setInstance(DensityVisualizationConfigDialog instance) {
        DensityVisualizationConfigDialog.instance = instance;
    }

    private void initComponents() {
        GridBagConstraints gridBagConstraints;
        // buttonGroup1 = new ButtonGroup();
        jPanelTypeOfVisualization = new JPanel();
        jRadioButtonVisualizeWithCubes = new JRadioButton();
        jRadioButtonVisualizeWithConvexHull = new JRadioButton();
        jRadioButtonVisualizeWithDividedConvexHull = new JRadioButton();
        jLabelToleranceOfDivision = new JLabel();
        jTextFieldToleranceOfDivision = new JTextField();
        jLabelPercentTolerance = new JLabel();
        jPanelValuesOfVisualization = new JPanel();
        jLabelNumberOfValues = new JLabel();
        jComboBoxNumberOfValues = new JComboBox();
        jLabelValue1 = new JLabel();
        jTextFieldValue1 = new JTextField();
        jLabelPercentV1 = new JLabel();
        jLabelValue2 = new JLabel();
        jTextFieldValue2 = new JTextField();
        jLabelPercentV2 = new JLabel();
        jLabelValue3 = new JLabel();
        jTextFieldValue3 = new JTextField();
        jLabelPercentV3 = new JLabel();
        jLabelValue4 = new JLabel();
        jTextFieldValue4 = new JTextField();
        jLabelPercentV4 = new JLabel();
        jPanelCOlorsOfVisualization = new JPanel();
        jLabelColor1 = new JLabel();
        jComboBoxColor1 = new JComboBox();
        jLabelColor2 = new JLabel();
        jComboBoxColor2 = new JComboBox();
        jLabelColor3 = new JLabel();
        jComboBoxColor3 = new JComboBox();
        jLabelColor4 = new JLabel();
        jComboBoxColor4 = new JComboBox();
        jLabelTransparency1 = new JLabel();
        jTextFieldT1 = new JTextField();
        jLabelPercentT1 = new JLabel();
        jLabelTransparency2 = new JLabel();
        jTextFieldT2 = new JTextField();
        jLabelPercentT2 = new JLabel();
        jLabelTransparency3 = new JLabel();
        jTextFieldT3 = new JTextField();
        jLabelPercentT3 = new JLabel();
        jLabelTransparency4 = new JLabel();
        jTextFieldT4 = new JTextField();
        jLabelPercentT4 = new JLabel();
        jLabelBackGroundColor = new JLabel();
        jComboBoxBackgroundColor = new JComboBox();
        jButtonVisualize = new JButton();
        getContentPane().setLayout(new java.awt.GridBagLayout());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new NeuGenConfigWindowListener());
        setTitle("NeuGen Density Visualization Configuration");
        setMinimumSize(new java.awt.Dimension(300, 500));
        setResizable(false);
        jPanelTypeOfVisualization.setLayout(new java.awt.GridBagLayout());
        jPanelTypeOfVisualization.setBorder(BorderFactory.createTitledBorder(null, "Type of Visualization",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(
                0, 0, 0)));
        jPanelTypeOfVisualization.setName("");
        if (jRadioButtonVisualizeWithCubes.isSelected() == true) {
            jRadioButtonVisualizeWithCubes.setSelected(false);
            jRadioButtonVisualizeWithCubes.setSelected(false);
        }
        jRadioButtonVisualizeWithCubes.setSelected(true);
        jRadioButtonVisualizeWithCubes.setText("Visualize with Cubes  ");
        jRadioButtonVisualizeWithCubes.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonVisualizeWithCubes.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonVisualizeWithCubes.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonVisualizeWithCubesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        jPanelTypeOfVisualization.add(jRadioButtonVisualizeWithCubes, gridBagConstraints);
        jRadioButtonVisualizeWithConvexHull.setText("Visualize with Convex Hull");
        jRadioButtonVisualizeWithConvexHull.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonVisualizeWithConvexHull.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonVisualizeWithConvexHull.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonVisualizeWithConvexHullActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        jPanelTypeOfVisualization.add(jRadioButtonVisualizeWithConvexHull, gridBagConstraints);
        jRadioButtonVisualizeWithDividedConvexHull.setText("Visualize with Divided Convex Hull");
        jRadioButtonVisualizeWithDividedConvexHull.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonVisualizeWithDividedConvexHull.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonVisualizeWithDividedConvexHull.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonVisualizeWithDividedConvexHullActionPerformed(evt);
            }
        });

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        jPanelTypeOfVisualization.add(jRadioButtonVisualizeWithDividedConvexHull, gridBagConstraints);
        jLabelToleranceOfDivision.setText("Tolerance of Division");
        jLabelToleranceOfDivision.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 5, 5);
        jPanelTypeOfVisualization.add(jLabelToleranceOfDivision, gridBagConstraints);
        jTextFieldToleranceOfDivision.setText("20");
        jTextFieldToleranceOfDivision.setEnabled(false);
        jTextFieldToleranceOfDivision.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldToleranceOfDivisionKeyReleased(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelTypeOfVisualization.add(jTextFieldToleranceOfDivision, gridBagConstraints);
        jLabelPercentTolerance.setText("%");
        jLabelPercentTolerance.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelTypeOfVisualization.add(jLabelPercentTolerance, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanelTypeOfVisualization, gridBagConstraints);
        jPanelValuesOfVisualization.setLayout(new java.awt.GridBagLayout());
        jPanelValuesOfVisualization.setBorder(BorderFactory.createTitledBorder(null, "Values of Visualization",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(
                0, 0, 0)));
        jLabelNumberOfValues.setText("Number of Values to be Visualized");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 20, 5);
        jPanelValuesOfVisualization.add(jLabelNumberOfValues, gridBagConstraints);
        jComboBoxNumberOfValues.setModel(new DefaultComboBoxModel(new String[]{"4", "3", "2", "1"}));
        jComboBoxNumberOfValues.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxNumberOfValuesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 20, 5);
        jPanelValuesOfVisualization.add(jComboBoxNumberOfValues, gridBagConstraints);
        jLabelValue1.setText("Value 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelValuesOfVisualization.add(jLabelValue1, gridBagConstraints);
        //jTextFieldValue1.setText("80");
        jTextFieldValue1.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldValue1KeyReleased(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelValuesOfVisualization.add(jTextFieldValue1, gridBagConstraints);
        jLabelPercentV1.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelValuesOfVisualization.add(jLabelPercentV1, gridBagConstraints);
        jLabelValue2.setText("Value 2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelValuesOfVisualization.add(jLabelValue2, gridBagConstraints);
        //jTextFieldValue2.setText("25");
        jTextFieldValue2.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldValue2KeyReleased(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelValuesOfVisualization.add(jTextFieldValue2, gridBagConstraints);
        jLabelPercentV2.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelValuesOfVisualization.add(jLabelPercentV2, gridBagConstraints);
        jLabelValue3.setText("Value 3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelValuesOfVisualization.add(jLabelValue3, gridBagConstraints);
        //jTextFieldValue3.setText("10");
        jTextFieldValue3.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldValue3KeyReleased(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelValuesOfVisualization.add(jTextFieldValue3, gridBagConstraints);
        jLabelPercentV3.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelValuesOfVisualization.add(jLabelPercentV3, gridBagConstraints);
        jLabelValue4.setText("Value 4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelValuesOfVisualization.add(jLabelValue4, gridBagConstraints);
        //jTextFieldValue4.setText("5");
        jTextFieldValue4.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldValue4KeyReleased(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelValuesOfVisualization.add(jTextFieldValue4, gridBagConstraints);
        jLabelPercentV4.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 5, 6, 5);
        jPanelValuesOfVisualization.add(jLabelPercentV4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanelValuesOfVisualization, gridBagConstraints);
        jPanelCOlorsOfVisualization.setLayout(new java.awt.GridBagLayout());
        jPanelCOlorsOfVisualization.setBorder(BorderFactory.createTitledBorder(null, "Colors of Visualization",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(
                0, 0, 0)));
        jLabelColor1.setText("Color for Value 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jLabelColor1, gridBagConstraints);
        jComboBoxColor1.setModel(new DefaultComboBoxModel(new String[]{"skyBlue1", "deepSkyBlue2", "dodgerBlue3", "midnightBlue"}));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jComboBoxColor1, gridBagConstraints);
        jLabelColor2.setText("Color for Value 2");
        jLabelColor2.addContainerListener(new java.awt.event.ContainerAdapter() {

            @Override
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                jLabelColor2ComponentAdded(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jLabelColor2, gridBagConstraints);
        jComboBoxColor2.setModel(new DefaultComboBoxModel(new String[]{"deepSkyBlue2", "skyBlue1", "dodgerBlue3", "midnightBlue"}));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jComboBoxColor2, gridBagConstraints);
        jLabelColor3.setText("Color for Value 3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jLabelColor3, gridBagConstraints);
        jComboBoxColor3.setModel(new DefaultComboBoxModel(new String[]{"dodgerBlue3", "skyBlue1", "deepSkyBlue2", "midnightBlue"}));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jComboBoxColor3, gridBagConstraints);
        jLabelColor4.setText("Color for Value 4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jLabelColor4, gridBagConstraints);
        jComboBoxColor4.setModel(new DefaultComboBoxModel(new String[]{"midnightBlue", "skyBlue1", "deepSkyBlue2", "dodgerBlue3"}));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jComboBoxColor4, gridBagConstraints);
        jLabelTransparency1.setText("Transparency for Value 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jLabelTransparency1, gridBagConstraints);
        //jTextFieldT1.setText("50");
        jTextFieldT1.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldT1KeyReleased(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jTextFieldT1, gridBagConstraints);
        jLabelPercentT1.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jLabelPercentT1, gridBagConstraints);
        jLabelTransparency2.setText("Transparency for Value 2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jLabelTransparency2, gridBagConstraints);
        //jTextFieldT2.setText("30");
        jTextFieldT2.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldT2KeyReleased(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jTextFieldT2, gridBagConstraints);
        jLabelPercentT2.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jLabelPercentT2, gridBagConstraints);
        jLabelTransparency3.setText("Transparency for Value 3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jLabelTransparency3, gridBagConstraints);
        //jTextFieldT3.setText("20");
        jTextFieldT3.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldT3KeyReleased(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jTextFieldT3, gridBagConstraints);
        jLabelPercentT3.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jLabelPercentT3, gridBagConstraints);
        jLabelTransparency4.setText("Transparency for Value 4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jLabelTransparency4, gridBagConstraints);
        //jTextFieldT4.setText("0");
        jTextFieldT4.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldT4KeyReleased(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jTextFieldT4, gridBagConstraints);
        jLabelPercentT4.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanelCOlorsOfVisualization.add(jLabelPercentT4, gridBagConstraints);
        jLabelBackGroundColor.setText("Background Color");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 5, 20, 5);
        jPanelCOlorsOfVisualization.add(jLabelBackGroundColor, gridBagConstraints);
        jComboBoxBackgroundColor.setModel(new DefaultComboBoxModel(new String[]{"black", "white"}));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 5, 20, 5);
        jPanelCOlorsOfVisualization.add(jComboBoxBackgroundColor, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanelCOlorsOfVisualization, gridBagConstraints);
        jButtonVisualize.setText("Visualize");
        jButtonVisualize.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVisualizeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 5);
        getContentPane().add(jButtonVisualize, gridBagConstraints);

        pack();
    }

    private void jTextFieldToleranceOfDivisionKeyReleased(java.awt.event.KeyEvent evt) {
        setFloatLabel(this.jTextFieldToleranceOfDivision);
    }

    private void jTextFieldValue1KeyReleased(java.awt.event.KeyEvent evt) {
        setFloatLabel(this.jTextFieldValue1);
    }

    private void jTextFieldValue2KeyReleased(java.awt.event.KeyEvent evt) {
        setFloatLabel(this.jTextFieldValue2);
    }

    private void jTextFieldValue3KeyReleased(java.awt.event.KeyEvent evt) {
        setFloatLabel(this.jTextFieldValue3);
    }

    private void jTextFieldValue4KeyReleased(java.awt.event.KeyEvent evt) {
        setFloatLabel(this.jTextFieldValue4);
    }

    private void jTextFieldT1KeyReleased(java.awt.event.KeyEvent evt) {
        setFloatLabel(this.jTextFieldT1);
    }

    private void jTextFieldT2KeyReleased(java.awt.event.KeyEvent evt) {
        setFloatLabel(this.jTextFieldT2);
    }

    private void jTextFieldT3KeyReleased(java.awt.event.KeyEvent evt) {
        setFloatLabel(this.jTextFieldT3);
    }

    private void jTextFieldT4KeyReleased(java.awt.event.KeyEvent evt) {
        setFloatLabel(this.jTextFieldT4);
    }

    private void jButtonVisualizeActionPerformed(java.awt.event.ActionEvent evt) {
//        this.setVisible(false);
//        VRLDensityVisualization densVis = new VRLDensityVisualization(task, volumeOfVoxels, dens, scene, canvasParent, canvas3D);
//        VRLDensityVisualization.setInstance(densVis);
//        writeProp();
    }

    private void jComboBoxNumberOfValuesActionPerformed(
            java.awt.event.ActionEvent evt) {
        if (jComboBoxNumberOfValues.getSelectedItem().equals("1")) {
            jTextFieldValue1.setEnabled(true);
            jLabelPercentV1.setEnabled(true);
            jTextFieldValue2.setEnabled(false);
            jLabelPercentV2.setEnabled(false);
            jTextFieldValue3.setEnabled(false);
            jLabelPercentV3.setEnabled(false);
            jTextFieldValue4.setEnabled(false);
            jLabelPercentV4.setEnabled(false);

            jLabelColor2.setEnabled(false);
            jComboBoxColor2.setEnabled(false);
            jLabelTransparency2.setEnabled(false);
            jTextFieldT2.setEnabled(false);
            jLabelPercentT2.setEnabled(false);

            jLabelColor3.setEnabled(false);
            jComboBoxColor3.setEnabled(false);
            jLabelTransparency3.setEnabled(false);
            jTextFieldT3.setEnabled(false);
            jLabelPercentT3.setEnabled(false);

            jLabelColor4.setEnabled(false);
            jComboBoxColor4.setEnabled(false);
            jLabelTransparency4.setEnabled(false);
            jTextFieldT4.setEnabled(false);
            jLabelPercentT4.setEnabled(false);

        } else if (jComboBoxNumberOfValues.getSelectedItem().equals("2")) {
            jTextFieldValue1.setEnabled(true);
            jLabelPercentV1.setEnabled(true);
            jTextFieldValue2.setEnabled(true);
            jLabelPercentV2.setEnabled(true);
            jTextFieldValue3.setEnabled(false);
            jLabelPercentV3.setEnabled(false);
            jTextFieldValue4.setEnabled(false);
            jLabelPercentV4.setEnabled(false);

            jLabelColor2.setEnabled(true);
            jComboBoxColor2.setEnabled(true);
            jLabelTransparency2.setEnabled(true);
            jTextFieldT2.setEnabled(true);
            jLabelPercentT2.setEnabled(true);

            jLabelColor3.setEnabled(false);
            jComboBoxColor3.setEnabled(false);
            jLabelTransparency3.setEnabled(false);
            jTextFieldT3.setEnabled(false);
            jLabelPercentT3.setEnabled(false);

            jLabelColor4.setEnabled(false);
            jComboBoxColor4.setEnabled(false);
            jLabelTransparency4.setEnabled(false);
            jTextFieldT4.setEnabled(false);
            jLabelPercentT4.setEnabled(false);

        } else if (jComboBoxNumberOfValues.getSelectedItem().equals("3")) {
            jTextFieldValue1.setEnabled(true);
            jLabelPercentV1.setEnabled(true);
            jTextFieldValue2.setEnabled(true);
            jLabelPercentV2.setEnabled(true);
            jTextFieldValue3.setEnabled(true);
            jLabelPercentV3.setEnabled(true);
            jTextFieldValue4.setEnabled(false);
            jLabelPercentV4.setEnabled(false);

            jLabelColor2.setEnabled(true);
            jComboBoxColor2.setEnabled(true);
            jLabelTransparency2.setEnabled(true);
            jTextFieldT2.setEnabled(true);
            jLabelPercentT2.setEnabled(true);

            jLabelColor3.setEnabled(true);
            jComboBoxColor3.setEnabled(true);
            jLabelTransparency3.setEnabled(true);
            jTextFieldT3.setEnabled(true);
            jLabelPercentT3.setEnabled(true);

            jLabelColor4.setEnabled(false);
            jComboBoxColor4.setEnabled(false);
            jLabelTransparency4.setEnabled(false);
            jTextFieldT4.setEnabled(false);
            jLabelPercentT4.setEnabled(false);

        } else if (jComboBoxNumberOfValues.getSelectedItem().equals("4")) {
            jTextFieldValue1.setEnabled(true);
            jLabelPercentV1.setEnabled(true);
            jTextFieldValue2.setEnabled(true);
            jLabelPercentV2.setEnabled(true);
            jTextFieldValue3.setEnabled(true);
            jLabelPercentV3.setEnabled(true);
            jTextFieldValue4.setEnabled(true);
            jLabelPercentV4.setEnabled(true);

            jLabelColor2.setEnabled(true);
            jComboBoxColor2.setEnabled(true);
            jLabelTransparency2.setEnabled(true);
            jTextFieldT2.setEnabled(true);
            jLabelPercentT2.setEnabled(true);

            jLabelColor3.setEnabled(true);
            jComboBoxColor3.setEnabled(true);
            jLabelTransparency3.setEnabled(true);
            jTextFieldT3.setEnabled(true);
            jLabelPercentT3.setEnabled(true);

            jLabelColor4.setEnabled(true);
            jComboBoxColor4.setEnabled(true);
            jLabelTransparency4.setEnabled(true);
            jTextFieldT4.setEnabled(true);
            jLabelPercentT4.setEnabled(true);

        }
    }

    private void jRadioButtonVisualizeWithDividedConvexHullActionPerformed(
            java.awt.event.ActionEvent evt) {
        jRadioButtonVisualizeWithCubes.setSelected(false);
        jRadioButtonVisualizeWithConvexHull.setSelected(false);
        jLabelToleranceOfDivision.setEnabled(true);
        jTextFieldToleranceOfDivision.setEnabled(true);
        jLabelPercentTolerance.setEnabled(true);
    }

    private void jRadioButtonVisualizeWithConvexHullActionPerformed(
            java.awt.event.ActionEvent evt) {
        jRadioButtonVisualizeWithCubes.setSelected(false);
        jRadioButtonVisualizeWithDividedConvexHull.setSelected(false);
        jLabelToleranceOfDivision.setEnabled(false);
        jTextFieldToleranceOfDivision.setEnabled(false);
        jLabelPercentTolerance.setEnabled(false);
    }

    private void jLabelColor2ComponentAdded(java.awt.event.ContainerEvent evt) {
        // TODO add your handling code here:
    }

    private void jRadioButtonVisualizeWithCubesActionPerformed(
            java.awt.event.ActionEvent evt) {
        jRadioButtonVisualizeWithConvexHull.setSelected(false);
        jRadioButtonVisualizeWithDividedConvexHull.setSelected(false);
        jLabelToleranceOfDivision.setEnabled(false);
        jTextFieldToleranceOfDivision.setEnabled(false);
        jLabelPercentTolerance.setEnabled(false);
    }

    public JComboBox getJComboBoxBackgroundColor() {
        return jComboBoxBackgroundColor;
    }

    public JComboBox getJComboBoxColor1() {
        return jComboBoxColor1;
    }

    public JComboBox getJComboBoxColor2() {
        return jComboBoxColor2;
    }

    public JComboBox getJComboBoxColor3() {
        return jComboBoxColor3;
    }

    public JComboBox getJComboBoxColor4() {
        return jComboBoxColor4;
    }

    public JComboBox getJComboBoxNumberOfValues() {
        return jComboBoxNumberOfValues;
    }

    public JTextField getJTextFieldTransparency1() {
        return jTextFieldT1;
    }

    public JTextField getJTextFieldTransparency2() {
        return jTextFieldT2;
    }

    public JTextField getJTextFieldTransparency3() {
        return jTextFieldT3;
    }

    public JTextField getJTextFieldTransparency4() {
        return jTextFieldT4;
    }

    public JTextField getJTextFieldToleranceOfDivision() {
        return jTextFieldToleranceOfDivision;
    }

    public JTextField getJTextFieldValueOfVisualization1() {
        return jTextFieldValue1;
    }

    public JTextField getJTextFieldValueOfVisualization2() {
        return jTextFieldValue2;
    }

    public JTextField getJTextFieldValueOfVisualization3() {
        return jTextFieldValue3;
    }

    public JTextField getJTextFieldValueOfVisualization4() {
        return jTextFieldValue4;
    }

    public JRadioButton getJRadioButtonVisualizeWithConvexHull() {
        return jRadioButtonVisualizeWithConvexHull;
    }

    public JRadioButton getJRadioButtonVisualizeWithCubes() {
        return jRadioButtonVisualizeWithCubes;
    }

    public JRadioButton getJRadioButtonVisualizeWithDividedConvexHull() {
        return jRadioButtonVisualizeWithDividedConvexHull;
    }

    private class NeuGenConfigWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
        }

        @Override
        public void windowClosed(WindowEvent e) {
            NeuGenView.getInstance().enableButtons();
            dispose();
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }
    }
}
