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
package org.neugen.gui;

import com.sun.j3d.loaders.Scene;
import org.neugen.gui.DensityVisualizationTask.Density;
import org.neugen.utils.Utils;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.io.FileNotFoundException;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import org.neugen.parsers.HocWriterTask;
import org.neugen.parsers.SimpleHocWriterTask;
import org.neugen.parsers.NeuroMLReaderTask;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.util.Map.Entry;
import java.util.Properties;
import javax.media.j3d.BranchGroup;

import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultTreeCellRenderer;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import org.jdesktop.application.Application.ExitListener;
import org.jdesktop.application.Task;
import org.neugen.slider.SliderGeneratorTask;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.Region;
import org.neugen.datastructures.parameter.AxonParam;
import org.neugen.datastructures.parameter.DendriteParam;
import org.neugen.datastructures.parameter.NetParam;
import org.neugen.datastructures.parameter.NeuronParam;
import org.neugen.datastructures.parameter.SubCommonTreeParam;
import org.neugen.datastructures.parameter.SubCommonTreeParam.GNSubCommonTreeParam;
import org.neugen.parsers.DefaultInheritance;
import org.neugen.parsers.NeuGenConfigStreamer;
import org.neugen.datastructures.xml.XMLNode;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.gui.NGFileFilter.TXTFileFilter;
import org.neugen.help.NeuGenHelp;
import org.neugen.parsers.CSVWriterTask;
import org.neugen.parsers.NeuGenReaderTask;
import org.neugen.parsers.NeuGenWriterTask;
import org.neugen.parsers.NeuroMLWriterTask;
import org.neugen.parsers.SWCReaderTask;
import org.neugen.parsers.SimpleHocReaderTask;
import org.neugen.parsers.OBJReader;
import org.neugen.visual.NeuGenDensityVisualization;
import org.neugen.gui.VisualizationTask.Visualization;
import org.neugen.parsers.NGX.NGXWriterTask;
import org.neugen.parsers.NeuGenVisualWriterTask;
import org.neugen.parsers.TXT.TXTWriterTask;
//import org.neugen.visual.OBJWriter;

/**
 * The main frame of NeuGen.
 * 
 * @author Jens Eberhard
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public final class NeuGenView extends FrameView implements TreeSaver {

    private static final Logger logger = Logger.getLogger(NeuGenView.class.getName());
    private static NeuGenView instance;
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private Net net;
    private boolean netExist;
    private boolean projectExist;
    private boolean newNet;
    private XMLTreeView projectTree;
    private Map<String, XMLObject> paramTrees;
    private List<Properties> commentPropList;
    private NeuGenExitListener neuGenExitListener;
    private String currentPropKey;
    private String command = "";
    private String projectDirPath;
    private Task visualizeTask;
    private Task visualizeDensityTask;
    private Task calcTask;
    private Task sliderTask;
    private Trigger trigger;
    private static String paramPath;
    private static String internaPath;
    private static String outputOptionsPath;
    private String currentProjectType;
    private boolean importedData;
    private DensityVisualizationTask.Density density;
    private VisualizationTask.Visualization visual;
    private File imageStackFile;
    private boolean imageSequence;
    private Scene scene;

    public Visualization getVisual() {
        return visual;
    }

    public Density getDensity() {
        return density;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public NeuGenView(SingleFrameApplication app) {
        super(app);
        initComponents();
        if (visualDensSplitPane.getRightComponent() != null) {
            visualDensSplitPane.remove(visualDensSplitPane.getRightComponent());
        }
        //importMenu.remove(readNeuGenMenuItem);
        importMenu.remove(readNeuTriaMenuItem);
        importMenu.remove(readNeuronMenuItem);
        //exportMenu.remove(writeNeuGenMenuItem);
        exportMenu.remove(writeObjMenuItem);
        //helpMenu.remove(helpContentsMenuItem);
        disableButtons();
        enableButtons();
        commentEditorPane.setOpaque(true);
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);
        progressBar.setStringPainted(true);
        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        outPrintln(NeuGenConstants.VERSION);
        outPrintln(NeuGenConstants.COPYRIGHT);
        outPrintln("If you use this software for your publications please cite the following two references: ");
        outPrintln(NeuGenConstants.CITE);
        outPrintln(NeuGenConstants.CONTACT);
        outPrintln(Utils.getMemoryStatus());
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

        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        createProjectMenuItem = new javax.swing.JMenuItem();
        fileSeparator = new javax.swing.JSeparator();
        openProjectMenuItem = new javax.swing.JMenuItem();
        closeProjectMenuItem = new javax.swing.JMenuItem();
        fileSeparator2 = new javax.swing.JSeparator();
        importMenu = new javax.swing.JMenu();
        readNeuronMenuItem = new javax.swing.JMenuItem();
        readNeuroMLMenuItem = new javax.swing.JMenuItem();
        readNeuGenMenuItem = new javax.swing.JMenuItem();
        readNeuTriaMenuItem = new javax.swing.JMenuItem();
        readNeurolucidaMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        readWavefrontMenuItem = new javax.swing.JMenuItem();
        readNeuRaMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        readImageStackMenuItem = new javax.swing.JMenuItem();
        readImageSequenceMenuItem = new javax.swing.JMenuItem();
        exportMenu = new javax.swing.JMenu();
        writeNeuronMenuItem = new javax.swing.JMenuItem();
        writeNeuroMLMenuItem = new javax.swing.JMenuItem();
        writeNeuGenMenuItem = new javax.swing.JMenuItem();
        writeNeuTriaMenuItem = new javax.swing.JMenuItem();
        writeNeuronInfoMenuItem = new javax.swing.JMenuItem();
        writeObjMenuItem = new javax.swing.JMenuItem();
        writeNGXMenuItem = new javax.swing.JMenuItem();
        writeTXTMenuItem = new javax.swing.JMenuItem();
        fileSeparator3 = new javax.swing.JSeparator();
        movieMenu = new javax.swing.JMenu();
        netMovieItem = new javax.swing.JMenuItem();
        densMovieMenuItem = new javax.swing.JMenuItem();
        fileSeparator5 = new javax.swing.JPopupMenu.Separator();
        saveMenuItem = new javax.swing.JMenuItem();
        fileSeparator4 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        copyMenuItem = new javax.swing.JMenuItem();
        clearMenuItem = new javax.swing.JMenuItem();
        selectAllMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        helpContentsMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        toolBar = new javax.swing.JToolBar();
        projectToolBar = new javax.swing.JToolBar();
        createProjectButton = new javax.swing.JButton();
        openProjectButton = new javax.swing.JButton();
        saveConfigButton = new javax.swing.JButton();
        runToolBar = new javax.swing.JToolBar();
        runButton = new javax.swing.JButton();
        dataToolBar = new javax.swing.JToolBar();
        exportButton = new javax.swing.JButton();
        importButton = new javax.swing.JButton();
        visualToolBar = new javax.swing.JToolBar();
        visualizeButton = new javax.swing.JButton();
        visualizeDensity = new javax.swing.JButton();
        imageToolBar = new javax.swing.JToolBar();
        sliderButton = new javax.swing.JButton();
        helpToolBar = new javax.swing.JToolBar();
        helpButton = new javax.swing.JButton();
        importFileChooser = new javax.swing.JFileChooser(".");
        mainSplitPane = new javax.swing.JSplitPane();
        editorSplitPane = new javax.swing.JSplitPane();
        configDataScrollPane = new javax.swing.JScrollPane();
        htmlScrollPane = new javax.swing.JScrollPane();
        commentEditorPane = new javax.swing.JEditorPane();
        visualOutputSplitPane = new javax.swing.JSplitPane();
        outputScrollPane = new javax.swing.JScrollPane();
        taskOutput = new javax.swing.JTextArea();
        visualDensSplitPane = new javax.swing.JSplitPane();
        visualScrollPane = new javax.swing.JScrollPane();
        densScrollPane = new javax.swing.JScrollPane();
        outputPopupMenu = new javax.swing.JPopupMenu();
        copyMenuItemPopup = new javax.swing.JMenuItem();
        clearMenuItemPopup = new javax.swing.JMenuItem();
        popupSeparator = new javax.swing.JSeparator();
        selectAllMenuItemPopup = new javax.swing.JMenuItem();
        popupSeparator2 = new javax.swing.JSeparator();
        saveAsMenuItemPopup = new javax.swing.JMenuItem();
        commentEditorPopupMenu = new javax.swing.JPopupMenu();
        editCommentMenuItem = new javax.swing.JMenuItem();
        commentEditorDialog = new javax.swing.JDialog();
        comDialogScrollPane = new javax.swing.JScrollPane();
        comDialogEditorPane = new javax.swing.JEditorPane();
        saveDialogButton = new javax.swing.JButton();
        cancelDialogButton = new javax.swing.JButton();
        openProjectFileChooser = new javax.swing.JFileChooser(".");

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.neugen.gui.NeuGenApp.class).getContext().getResourceMap(NeuGenView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.neugen.gui.NeuGenApp.class).getContext().getActionMap(NeuGenView.class, this);
        createProjectMenuItem.setAction(actionMap.get("createProject")); // NOI18N
        createProjectMenuItem.setIcon(resourceMap.getIcon("createProjectMenuItem.icon")); // NOI18N
        createProjectMenuItem.setText(resourceMap.getString("createProjectMenuItem.text")); // NOI18N
        createProjectMenuItem.setName("createProjectMenuItem"); // NOI18N
        fileMenu.add(createProjectMenuItem);

        fileSeparator.setName("fileSeparator"); // NOI18N
        fileMenu.add(fileSeparator);

        openProjectMenuItem.setAction(actionMap.get("openProject")); // NOI18N
        openProjectMenuItem.setIcon(resourceMap.getIcon("openProjectMenuItem.icon")); // NOI18N
        openProjectMenuItem.setText(resourceMap.getString("openProjectMenuItem.text")); // NOI18N
        openProjectMenuItem.setName("openProjectMenuItem"); // NOI18N
        fileMenu.add(openProjectMenuItem);

        closeProjectMenuItem.setAction(actionMap.get("closeProject")); // NOI18N
        closeProjectMenuItem.setIcon(resourceMap.getIcon("closeProjectMenuItem.icon")); // NOI18N
        closeProjectMenuItem.setText(resourceMap.getString("closeProjectMenuItem.text")); // NOI18N
        closeProjectMenuItem.setName("closeProjectMenuItem"); // NOI18N
        fileMenu.add(closeProjectMenuItem);

        fileSeparator2.setName("fileSeparator2"); // NOI18N
        fileMenu.add(fileSeparator2);

        importMenu.setIcon(resourceMap.getIcon("importMenu.icon")); // NOI18N
        importMenu.setText(resourceMap.getString("importMenu.text")); // NOI18N
        importMenu.setName("importMenu"); // NOI18N

        readNeuronMenuItem.setAction(actionMap.get("importData")); // NOI18N
        readNeuronMenuItem.setText(resourceMap.getString("readNeuronMenuItem.text")); // NOI18N
        readNeuronMenuItem.setName("readNeuronMenuItem"); // NOI18N
        readNeuronMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readNeuronMenuItemActionPerformed(evt);
            }
        });
        importMenu.add(readNeuronMenuItem);

        readNeuroMLMenuItem.setAction(actionMap.get("importData")); // NOI18N
        readNeuroMLMenuItem.setText(resourceMap.getString("readNeuroMLMenuItem.text")); // NOI18N
        readNeuroMLMenuItem.setName("readNeuroMLMenuItem"); // NOI18N
        readNeuroMLMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readNeuroMLMenuItemActionPerformed(evt);
            }
        });
        importMenu.add(readNeuroMLMenuItem);

        readNeuGenMenuItem.setAction(actionMap.get("importData")); // NOI18N
        readNeuGenMenuItem.setText(resourceMap.getString("readNeuGenMenuItem.text")); // NOI18N
        readNeuGenMenuItem.setName("readNeuGenMenuItem"); // NOI18N
        readNeuGenMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readNeuGenMenuItemActionPerformed(evt);
            }
        });
        importMenu.add(readNeuGenMenuItem);

        readNeuTriaMenuItem.setAction(actionMap.get("importData")); // NOI18N
        readNeuTriaMenuItem.setText(resourceMap.getString("readNeuTriaMenuItem.text")); // NOI18N
        readNeuTriaMenuItem.setName("readNeuTriaMenuItem"); // NOI18N
        readNeuTriaMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readNeuTriaMenuItemActionPerformed(evt);
            }
        });
        importMenu.add(readNeuTriaMenuItem);

        readNeurolucidaMenuItem.setAction(actionMap.get("importData")); // NOI18N
        readNeurolucidaMenuItem.setText(resourceMap.getString("readNeurolucidaMenuItem.text")); // NOI18N
        readNeurolucidaMenuItem.setName("readNeurolucidaMenuItem"); // NOI18N
        readNeurolucidaMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readNeurolucidaMenuItemActionPerformed(evt);
            }
        });
        importMenu.add(readNeurolucidaMenuItem);
        importMenu.add(jSeparator2);

        readWavefrontMenuItem.setAction(actionMap.get("importData")); // NOI18N
        readWavefrontMenuItem.setText(resourceMap.getString("readWavefrontMenuItem.text")); // NOI18N
        readWavefrontMenuItem.setName("readWavefrontMenuItem"); // NOI18N
        readWavefrontMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readWavefrontMenuItemActionPerformed(evt);
            }
        });
        importMenu.add(readWavefrontMenuItem);

        readNeuRaMenuItem.setAction(actionMap.get("importData")); // NOI18N
        readNeuRaMenuItem.setText(resourceMap.getString("readNeuRaMenuItem.text")); // NOI18N
        readNeuRaMenuItem.setName("readNeuRaMenuItem"); // NOI18N
        readNeuRaMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readNeuRaMenuItemActionPerformed(evt);
            }
        });
        importMenu.add(readNeuRaMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        importMenu.add(jSeparator1);

        readImageStackMenuItem.setAction(actionMap.get("importData")); // NOI18N
        readImageStackMenuItem.setText(resourceMap.getString("readImageStackMenuItem.text")); // NOI18N
        readImageStackMenuItem.setName("readImageStackMenuItem"); // NOI18N
        readImageStackMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readImageStackMenuItemActionPerformed(evt);
            }
        });
        importMenu.add(readImageStackMenuItem);

        readImageSequenceMenuItem.setAction(actionMap.get("importData")); // NOI18N
        readImageSequenceMenuItem.setText(resourceMap.getString("readImageSequenceMenuItem.text")); // NOI18N
        readImageSequenceMenuItem.setName("readImageSequenceMenuItem"); // NOI18N
        readImageSequenceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readImageSequenceMenuItemActionPerformed(evt);
            }
        });
        importMenu.add(readImageSequenceMenuItem);

        fileMenu.add(importMenu);

        exportMenu.setIcon(resourceMap.getIcon("exportMenu.icon")); // NOI18N
        exportMenu.setText(resourceMap.getString("exportMenu.text")); // NOI18N
        exportMenu.setName("exportMenu"); // NOI18N

        writeNeuronMenuItem.setAction(actionMap.get("exportData")); // NOI18N
        writeNeuronMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        writeNeuronMenuItem.setText(resourceMap.getString("writeNeuronMenuItem.text")); // NOI18N
        writeNeuronMenuItem.setName("writeNeuronMenuItem"); // NOI18N
        writeNeuronMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeNeuronMenuItemActionPerformed(evt);
            }
        });
        exportMenu.add(writeNeuronMenuItem);

        writeNeuroMLMenuItem.setAction(actionMap.get("exportData")); // NOI18N
        writeNeuroMLMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        writeNeuroMLMenuItem.setText(resourceMap.getString("writeNeuroMLMenuItem.text")); // NOI18N
        writeNeuroMLMenuItem.setName("writeNeuroMLMenuItem"); // NOI18N
        writeNeuroMLMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeNeuroMLMenuItemActionPerformed(evt);
            }
        });
        exportMenu.add(writeNeuroMLMenuItem);

        writeNeuGenMenuItem.setAction(actionMap.get("exportData")); // NOI18N
        writeNeuGenMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        writeNeuGenMenuItem.setText(resourceMap.getString("writeNeuGenMenuItem.text")); // NOI18N
        writeNeuGenMenuItem.setName("writeNeuGenMenuItem"); // NOI18N
        writeNeuGenMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeNeuGenMenuItemActionPerformed(evt);
            }
        });
        exportMenu.add(writeNeuGenMenuItem);

        writeNeuTriaMenuItem.setAction(actionMap.get("exportData")); // NOI18N
        writeNeuTriaMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        writeNeuTriaMenuItem.setText(resourceMap.getString("writeNeuTriaMenuItem.text")); // NOI18N
        writeNeuTriaMenuItem.setName("writeNeuTriaMenuItem"); // NOI18N
        writeNeuTriaMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeNeuTriaMenuItemActionPerformed(evt);
            }
        });
        exportMenu.add(writeNeuTriaMenuItem);

        writeNeuronInfoMenuItem.setAction(actionMap.get("exportData")); // NOI18N
        writeNeuronInfoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        writeNeuronInfoMenuItem.setText(resourceMap.getString("writeNeuronInfoMenuItem.text")); // NOI18N
        writeNeuronInfoMenuItem.setName("writeNeuronInfoMenuItem"); // NOI18N
        writeNeuronInfoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeNeuronInfoMenuItemActionPerformed(evt);
            }
        });
        exportMenu.add(writeNeuronInfoMenuItem);

        writeObjMenuItem.setAction(actionMap.get("exportData")); // NOI18N
        writeObjMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        writeObjMenuItem.setText(resourceMap.getString("writeObjMenuItem.text")); // NOI18N
        writeObjMenuItem.setName("writeObjMenuItem"); // NOI18N
        writeObjMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeObjMenuItemActionPerformed(evt);
            }
        });
        exportMenu.add(writeObjMenuItem);

        writeNGXMenuItem.setAction(actionMap.get("exportData")); // NOI18N
        writeNGXMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        writeNGXMenuItem.setText(resourceMap.getString("writeNGXMenuItem.text")); // NOI18N
        writeNGXMenuItem.setName("writeNGXMenuItem"); // NOI18N
        writeNGXMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeNGXMenuItemActionPerformed(evt);
            }
        });
        exportMenu.add(writeNGXMenuItem);

        writeTXTMenuItem.setAction(actionMap.get("exportData")); // NOI18N
        writeTXTMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        writeTXTMenuItem.setText(resourceMap.getString("writeTXTMenuItem.text")); // NOI18N
        writeTXTMenuItem.setToolTipText(resourceMap.getString("writeTXTMenuItem.toolTipText")); // NOI18N
        writeTXTMenuItem.setName("writeTXTMenuItem"); // NOI18N
        writeTXTMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeTXTMenuItemActionPerformed(evt);
            }
        });
        exportMenu.add(writeTXTMenuItem);

        fileMenu.add(exportMenu);

        fileSeparator3.setName("fileSeparator3"); // NOI18N
        fileMenu.add(fileSeparator3);

        movieMenu.setIcon(resourceMap.getIcon("movieMenu.icon")); // NOI18N
        movieMenu.setText(resourceMap.getString("movieMenu.text")); // NOI18N
        movieMenu.setName("movieMenu"); // NOI18N

        netMovieItem.setText(resourceMap.getString("netMovieItem.text")); // NOI18N
        netMovieItem.setEnabled(false);
        netMovieItem.setName("netMovieItem"); // NOI18N
        netMovieItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                netMovieItemActionPerformed(evt);
            }
        });
        movieMenu.add(netMovieItem);

        densMovieMenuItem.setText(resourceMap.getString("densMovieMenuItem.text")); // NOI18N
        densMovieMenuItem.setEnabled(false);
        densMovieMenuItem.setName("densMovieMenuItem"); // NOI18N
        densMovieMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                densMovieMenuItemActionPerformed(evt);
            }
        });
        movieMenu.add(densMovieMenuItem);

        fileMenu.add(movieMenu);

        fileSeparator5.setName("fileSeparator5"); // NOI18N
        fileMenu.add(fileSeparator5);

        saveMenuItem.setAction(actionMap.get("saveData")); // NOI18N
        saveMenuItem.setIcon(resourceMap.getIcon("saveMenuItem.icon")); // NOI18N
        saveMenuItem.setText(resourceMap.getString("saveMenuItem.text")); // NOI18N
        saveMenuItem.setToolTipText(resourceMap.getString("saveMenuItem.toolTipText")); // NOI18N
        saveMenuItem.setName("saveMenuItem"); // NOI18N
        fileMenu.add(saveMenuItem);

        fileSeparator4.setName("fileSeparator4"); // NOI18N
        fileMenu.add(fileSeparator4);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setIcon(resourceMap.getIcon("exitMenuItem.icon")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setToolTipText(resourceMap.getString("exitMenuItem.toolTipText")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText(resourceMap.getString("editMenu.text")); // NOI18N
        editMenu.setName("editMenu"); // NOI18N

        copyMenuItem.setAction(actionMap.get("copyTaskOutput")); // NOI18N
        copyMenuItem.setIcon(resourceMap.getIcon("copyMenuItem.icon")); // NOI18N
        copyMenuItem.setText(resourceMap.getString("copyMenuItem.text")); // NOI18N
        copyMenuItem.setToolTipText(resourceMap.getString("copyMenuItem.toolTipText")); // NOI18N
        copyMenuItem.setName("copyMenuItem"); // NOI18N
        editMenu.add(copyMenuItem);

        clearMenuItem.setAction(actionMap.get("clearTaskOutput")); // NOI18N
        clearMenuItem.setText(resourceMap.getString("clearMenuItem.text")); // NOI18N
        clearMenuItem.setName("clearMenuItem"); // NOI18N
        editMenu.add(clearMenuItem);

        selectAllMenuItem.setAction(actionMap.get("selectAllFromTaskOutput")); // NOI18N
        selectAllMenuItem.setText(resourceMap.getString("selectAllMenuItem.text")); // NOI18N
        selectAllMenuItem.setName("selectAllMenuItem"); // NOI18N
        editMenu.add(selectAllMenuItem);

        menuBar.add(editMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        helpContentsMenuItem.setIcon(resourceMap.getIcon("helpContentsMenuItem.icon")); // NOI18N
        helpContentsMenuItem.setText(resourceMap.getString("helpContentsMenuItem.text")); // NOI18N
        helpContentsMenuItem.setName("helpContentsMenuItem"); // NOI18N
        helpMenu.add(helpContentsMenuItem);
        helpContentsMenuItem.addActionListener(NeuGenHelp.getInstance().getfDisplayHelp());

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setIcon(resourceMap.getIcon("aboutMenuItem.icon")); // NOI18N
        aboutMenuItem.setText(resourceMap.getString("aboutMenuItem.text")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(statusMessageLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
                .add(18, 18, 18)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(statusAnimationLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 945, Short.MAX_VALUE)
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(2, 2, 2)
                .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .add(statusAnimationLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .add(statusMessageLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
                .addContainerGap())
        );

        toolBar.setBorder(null);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setToolTipText(resourceMap.getString("toolBar.toolTipText")); // NOI18N
        toolBar.setName("toolBar"); // NOI18N

        projectToolBar.setRollover(true);
        projectToolBar.setName("projectToolBar"); // NOI18N

        createProjectButton.setAction(actionMap.get("createProject")); // NOI18N
        createProjectButton.setIcon(resourceMap.getIcon("createProjectButton.icon")); // NOI18N
        createProjectButton.setText(resourceMap.getString("createProjectButton.text")); // NOI18N
        createProjectButton.setToolTipText(resourceMap.getString("createProjectButton.toolTipText")); // NOI18N
        createProjectButton.setBorderPainted(false);
        createProjectButton.setFocusable(false);
        createProjectButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        createProjectButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        createProjectButton.setName("createProjectButton"); // NOI18N
        createProjectButton.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        projectToolBar.add(createProjectButton);

        openProjectButton.setAction(actionMap.get("openProject")); // NOI18N
        openProjectButton.setIcon(resourceMap.getIcon("openProjectButton.icon")); // NOI18N
        openProjectButton.setText(resourceMap.getString("openProjectButton.text")); // NOI18N
        openProjectButton.setToolTipText(resourceMap.getString("openProjectButton.toolTipText")); // NOI18N
        openProjectButton.setBorderPainted(false);
        openProjectButton.setFocusable(false);
        openProjectButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        openProjectButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        openProjectButton.setName("openProjectButton"); // NOI18N
        projectToolBar.add(openProjectButton);

        saveConfigButton.setAction(actionMap.get("save")); // NOI18N
        saveConfigButton.setIcon(resourceMap.getIcon("saveConfigButton.icon")); // NOI18N
        saveConfigButton.setText(resourceMap.getString("saveConfigButton.text")); // NOI18N
        saveConfigButton.setToolTipText(resourceMap.getString("saveConfigButton.toolTipText")); // NOI18N
        saveConfigButton.setBorderPainted(false);
        saveConfigButton.setFocusable(false);
        saveConfigButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        saveConfigButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        saveConfigButton.setName("saveConfigButton"); // NOI18N
        projectToolBar.add(saveConfigButton);

        toolBar.add(projectToolBar);

        runToolBar.setRollover(true);
        runToolBar.setName("runToolBar"); // NOI18N

        runButton.setAction(actionMap.get("startCalc")); // NOI18N
        runButton.setIcon(resourceMap.getIcon("runButton.icon")); // NOI18N
        runButton.setText(resourceMap.getString("runButton.text")); // NOI18N
        runButton.setToolTipText(resourceMap.getString("runButton.toolTipText")); // NOI18N
        runButton.setActionCommand(resourceMap.getString("runButton.actionCommand")); // NOI18N
        runButton.setBorderPainted(false);
        runButton.setFocusable(false);
        runButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        runButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        runButton.setName("runButton"); // NOI18N
        runToolBar.add(runButton);

        toolBar.add(runToolBar);

        dataToolBar.setRollover(true);
        dataToolBar.setName("dataToolBar"); // NOI18N

        exportButton.setAction(actionMap.get("exportData")); // NOI18N
        exportButton.setIcon(resourceMap.getIcon("exportButton.icon")); // NOI18N
        exportButton.setText(resourceMap.getString("exportButton.text")); // NOI18N
        exportButton.setToolTipText(resourceMap.getString("exportButton.toolTipText")); // NOI18N
        exportButton.setBorderPainted(false);
        exportButton.setFocusable(false);
        exportButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        exportButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        exportButton.setName("exportButton"); // NOI18N
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });
        dataToolBar.add(exportButton);

        importButton.setAction(actionMap.get("importData")); // NOI18N
        importButton.setIcon(resourceMap.getIcon("importButton.icon")); // NOI18N
        importButton.setText(resourceMap.getString("importButton.text")); // NOI18N
        importButton.setToolTipText(resourceMap.getString("importButton.toolTipText")); // NOI18N
        importButton.setBorderPainted(false);
        importButton.setFocusable(false);
        importButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        importButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        importButton.setName("importButton"); // NOI18N
        dataToolBar.add(importButton);

        toolBar.add(dataToolBar);

        visualToolBar.setRollover(true);
        visualToolBar.setName("visualToolBar"); // NOI18N

        visualizeButton.setAction(actionMap.get("visualizeData")); // NOI18N
        visualizeButton.setIcon(resourceMap.getIcon("visualizeButton.icon")); // NOI18N
        visualizeButton.setText(resourceMap.getString("visualizeButton.text")); // NOI18N
        visualizeButton.setToolTipText(resourceMap.getString("visualizeButton.toolTipText")); // NOI18N
        visualizeButton.setBorderPainted(false);
        visualizeButton.setFocusable(false);
        visualizeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        visualizeButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        visualizeButton.setName("visualizeButton"); // NOI18N
        visualToolBar.add(visualizeButton);

        visualizeDensity.setAction(actionMap.get("visualizeDensity")); // NOI18N
        visualizeDensity.setIcon(resourceMap.getIcon("visualizeDensity.icon")); // NOI18N
        visualizeDensity.setText(resourceMap.getString("visualizeDensity.text")); // NOI18N
        visualizeDensity.setBorderPainted(false);
        visualizeDensity.setFocusable(false);
        visualizeDensity.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        visualizeDensity.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        visualizeDensity.setName("visualizeDensity"); // NOI18N
        visualToolBar.add(visualizeDensity);

        toolBar.add(visualToolBar);

        imageToolBar.setRollover(true);
        imageToolBar.setName("imageToolBar"); // NOI18N

        sliderButton.setAction(actionMap.get("generateSlider")); // NOI18N
        sliderButton.setIcon(resourceMap.getIcon("sliderButton.icon")); // NOI18N
        sliderButton.setText(resourceMap.getString("sliderButton.text")); // NOI18N
        sliderButton.setBorderPainted(false);
        sliderButton.setFocusable(false);
        sliderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        sliderButton.setName("sliderButton"); // NOI18N
        imageToolBar.add(sliderButton);
        sliderButton.getAccessibleContext().setAccessibleName(resourceMap.getString("jButton1.AccessibleContext.accessibleName")); // NOI18N

        toolBar.add(imageToolBar);

        helpToolBar.setRollover(true);
        helpToolBar.setName("helpToolBar"); // NOI18N

        helpButton.setIcon(resourceMap.getIcon("helpButton.icon")); // NOI18N
        helpButton.setText(resourceMap.getString("helpButton.text")); // NOI18N
        helpButton.setToolTipText(resourceMap.getString("helpButton.toolTipText")); // NOI18N
        helpButton.setFocusable(false);
        helpButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        helpButton.setName("helpButton"); // NOI18N
        helpToolBar.add(helpButton);
        helpButton.addActionListener(NeuGenHelp.getInstance().getfDisplayHelp());

        toolBar.add(helpToolBar);

        importFileChooser.setAcceptAllFileFilterUsed(false);
        importFileChooser.setName("importFileChooser"); // NOI18N

        mainSplitPane.setName("mainSplitPane"); // NOI18N
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setPreferredSize(new java.awt.Dimension(606, 456));

        editorSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        editorSplitPane.setName("editorSplitPane"); // NOI18N
        editorSplitPane.setOneTouchExpandable(true);
        editorSplitPane.setPreferredSize(new java.awt.Dimension(300, 456));

        configDataScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("configDataScrollPane.border.title"))); // NOI18N
        configDataScrollPane.setMinimumSize(new java.awt.Dimension(25, 200));
        configDataScrollPane.setName("configDataScrollPane"); // NOI18N
        configDataScrollPane.setPreferredSize(new java.awt.Dimension(300, 300));
        editorSplitPane.setTopComponent(configDataScrollPane);
        configDataScrollPane.getAccessibleContext().setAccessibleParent(editorSplitPane);

        htmlScrollPane.setName("htmlScrollPane"); // NOI18N

        commentEditorPane.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("commentEditorPane.border.title"))); // NOI18N
        commentEditorPane.setContentType(resourceMap.getString("commentEditorPane.contentType")); // NOI18N
        commentEditorPane.setEditable(false);
        commentEditorPane.setName("commentEditorPane"); // NOI18N
        commentEditorPane.setPreferredSize(new java.awt.Dimension(300, 150));
        htmlScrollPane.setViewportView(commentEditorPane);

        editorSplitPane.setBottomComponent(htmlScrollPane);

        mainSplitPane.setLeftComponent(editorSplitPane);

        visualOutputSplitPane.setDividerLocation(200);
        visualOutputSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        visualOutputSplitPane.setContinuousLayout(true);
        visualOutputSplitPane.setName("visualOutputSplitPane"); // NOI18N
        visualOutputSplitPane.setPreferredSize(new java.awt.Dimension(300, 456));

        outputScrollPane.setName("outputScrollPane"); // NOI18N

        taskOutput.setColumns(20);
        taskOutput.setRows(5);
        taskOutput.setName("taskOutput"); // NOI18N
        taskOutput.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                taskOutputMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                taskOutputMouseReleased(evt);
            }
        });
        outputScrollPane.setViewportView(taskOutput);

        visualOutputSplitPane.setRightComponent(outputScrollPane);

        visualDensSplitPane.setDividerSize(4);
        visualDensSplitPane.setResizeWeight(0.5);
        visualDensSplitPane.setContinuousLayout(true);
        visualDensSplitPane.setName("visualDensSplitPane"); // NOI18N

        visualScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("visualScrollPane.border.title"))); // NOI18N
        visualScrollPane.setName("visualScrollPane"); // NOI18N
        visualDensSplitPane.setLeftComponent(visualScrollPane);

        densScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("densScrollPane.border.title"))); // NOI18N
        densScrollPane.setName("densScrollPane"); // NOI18N
        visualDensSplitPane.setRightComponent(densScrollPane);

        visualOutputSplitPane.setLeftComponent(visualDensSplitPane);

        mainSplitPane.setRightComponent(visualOutputSplitPane);

        outputPopupMenu.setName("outputPopupMenu"); // NOI18N

        copyMenuItemPopup.setAction(actionMap.get("copyTaskOutput")); // NOI18N
        copyMenuItemPopup.setIcon(resourceMap.getIcon("copyMenuItemPopup.icon")); // NOI18N
        copyMenuItemPopup.setText(resourceMap.getString("copyMenuItemPopup.text")); // NOI18N
        copyMenuItemPopup.setName("copyMenuItemPopup"); // NOI18N
        outputPopupMenu.add(copyMenuItemPopup);

        clearMenuItemPopup.setAction(actionMap.get("clearTaskOutput")); // NOI18N
        clearMenuItemPopup.setIcon(resourceMap.getIcon("clearMenuItemPopup.icon")); // NOI18N
        clearMenuItemPopup.setText(resourceMap.getString("clearMenuItemPopup.text")); // NOI18N
        clearMenuItemPopup.setName("clearMenuItemPopup"); // NOI18N
        outputPopupMenu.add(clearMenuItemPopup);

        popupSeparator.setName("popupSeparator"); // NOI18N
        outputPopupMenu.add(popupSeparator);

        selectAllMenuItemPopup.setAction(actionMap.get("selectAllFromTaskOutput")); // NOI18N
        selectAllMenuItemPopup.setIcon(resourceMap.getIcon("selectAllMenuItemPopup.icon")); // NOI18N
        selectAllMenuItemPopup.setText(resourceMap.getString("selectAllMenuItemPopup.text")); // NOI18N
        selectAllMenuItemPopup.setName("selectAllMenuItemPopup"); // NOI18N
        outputPopupMenu.add(selectAllMenuItemPopup);

        popupSeparator2.setName("popupSeparator2"); // NOI18N
        outputPopupMenu.add(popupSeparator2);

        saveAsMenuItemPopup.setAction(actionMap.get("saveOutputText")); // NOI18N
        saveAsMenuItemPopup.setIcon(resourceMap.getIcon("saveAsMenuItemPopup.icon")); // NOI18N
        saveAsMenuItemPopup.setText(resourceMap.getString("saveAsMenuItemPopup.text")); // NOI18N
        saveAsMenuItemPopup.setName("saveAsMenuItemPopup"); // NOI18N
        outputPopupMenu.add(saveAsMenuItemPopup);

        commentEditorPopupMenu.setName("commentEditorPopupMenu"); // NOI18N

        editCommentMenuItem.setText(resourceMap.getString("editCommentMenuItem.text")); // NOI18N
        editCommentMenuItem.setToolTipText(resourceMap.getString("editCommentMenuItem.toolTipText")); // NOI18N
        editCommentMenuItem.setName("editCommentMenuItem"); // NOI18N
        editCommentMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCommentMenuItemActionPerformed(evt);
            }
        });
        commentEditorPopupMenu.add(editCommentMenuItem);

        commentEditorDialog.setMinimumSize(new java.awt.Dimension(500, 300));
        commentEditorDialog.setName("commentEditorDialog"); // NOI18N
        commentEditorDialog.setResizable(false);

        comDialogScrollPane.setName("comDialogScrollPane"); // NOI18N

        comDialogEditorPane.setName("comDialogEditorPane"); // NOI18N
        comDialogScrollPane.setViewportView(comDialogEditorPane);

        saveDialogButton.setText(resourceMap.getString("saveDialogButton.text")); // NOI18N
        saveDialogButton.setToolTipText(resourceMap.getString("saveDialogButton.toolTipText")); // NOI18N
        saveDialogButton.setName("saveDialogButton"); // NOI18N
        saveDialogButton.setSelected(true);
        saveDialogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDialogButtonActionPerformed(evt);
            }
        });

        cancelDialogButton.setText(resourceMap.getString("cancelDialogButton.text")); // NOI18N
        cancelDialogButton.setName("cancelDialogButton"); // NOI18N
        cancelDialogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelDialogButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout commentEditorDialogLayout = new org.jdesktop.layout.GroupLayout(commentEditorDialog.getContentPane());
        commentEditorDialog.getContentPane().setLayout(commentEditorDialogLayout);
        commentEditorDialogLayout.setHorizontalGroup(
            commentEditorDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(commentEditorDialogLayout.createSequentialGroup()
                .addContainerGap()
                .add(commentEditorDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(comDialogScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, commentEditorDialogLayout.createSequentialGroup()
                        .add(saveDialogButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(cancelDialogButton)
                        .add(21, 21, 21)))
                .addContainerGap())
        );
        commentEditorDialogLayout.setVerticalGroup(
            commentEditorDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(commentEditorDialogLayout.createSequentialGroup()
                .addContainerGap()
                .add(comDialogScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 225, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 64, Short.MAX_VALUE)
                .add(commentEditorDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelDialogButton)
                    .add(saveDialogButton))
                .addContainerGap())
        );

        openProjectFileChooser.setApproveButtonText(resourceMap.getString("openProjectFileChooser.approveButtonText")); // NOI18N
        openProjectFileChooser.setDialogTitle(resourceMap.getString("openProjectFileChooser.dialogTitle")); // NOI18N
        openProjectFileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        openProjectFileChooser.setName("openProjectFileChooser"); // NOI18N

        setComponent(mainSplitPane);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
        setToolBar(toolBar);
    }// </editor-fold>//GEN-END:initComponents

    private void taskOutputMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taskOutputMousePressed
        if (evt.isPopupTrigger()) {
            outputPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_taskOutputMousePressed

    private void writeNeuronMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_writeNeuronMenuItemActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_writeNeuronMenuItemActionPerformed

    private void writeNeuroMLMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_writeNeuroMLMenuItemActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_writeNeuroMLMenuItemActionPerformed

    private void writeNeuGenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_writeNeuGenMenuItemActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_writeNeuGenMenuItemActionPerformed

    private void writeNeuTriaMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_writeNeuTriaMenuItemActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_writeNeuTriaMenuItemActionPerformed

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_exportButtonActionPerformed

    private void readNeuronMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readNeuronMenuItemActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_readNeuronMenuItemActionPerformed

    private void readNeuroMLMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readNeuroMLMenuItemActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_readNeuroMLMenuItemActionPerformed

    private void readNeuGenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readNeuGenMenuItemActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_readNeuGenMenuItemActionPerformed

    private void readNeuTriaMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readNeuTriaMenuItemActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_readNeuTriaMenuItemActionPerformed

    private void editCommentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCommentMenuItemActionPerformed
        logger.info("currentPropertyKey: " + currentPropKey);
        commentEditorDialog.setVisible(true);
        for (Properties comProp : commentPropList) {
            if (comProp.containsKey(currentPropKey)) {
                comDialogEditorPane.setText(comProp.getProperty(currentPropKey));
                break;
            }
        }
    }//GEN-LAST:event_editCommentMenuItemActionPerformed

    private void saveDialogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDialogButtonActionPerformed
        logger.info("save: " + currentPropKey);
        for (Properties comProp : commentPropList) {
            if (comProp.containsKey(currentPropKey)) {
                comProp.setProperty(currentPropKey, comDialogEditorPane.getText());
                //logger.info(comDialogEditorPane.getText());
                //commentEditorPane.setText(comProp.getProperty(currentPropKey).replaceAll(System.getProperty("line.separator"), "<br />"));
                String value = comProp.getProperty(currentPropKey);
                value = value.replaceAll(System.getProperty("line.separator"), "<br />");
                value = value.replaceAll("\n", "<br />");
                commentEditorPane.setText(value);
                commentEditorDialog.setVisible(false);
                commentEditorDialog.dispose();
                logger.info(projectDirPath);

                if (currentPropKey.contains(NeuGenConstants.PARAM)) {
                    NeuGenComments.saveComments(comProp, new File(projectDirPath), NeuGenConstants.PARAM);
                } else if (currentPropKey.contains(NeuGenConstants.INTERNA)) {
                    NeuGenComments.saveComments(comProp, new File(projectDirPath), NeuGenConstants.INTERNA);
                } else if (currentPropKey.contains(NeuGenConstants.OUTPUT)) {
                    //logger.info("save in outputO.neu: " + currentPropKey);
                    NeuGenComments.saveComments(comProp, new File(projectDirPath), NeuGenConstants.OUTPUT);
                } else if (currentPropKey.contains(new File(projectDirPath).getName())) {
                    try {
                        //logger.info("save to project prop");
                        NeuGenComments.saveProjectPropToXML(projectDirPath, comProp);
                    } catch (FileNotFoundException ex) {
                        logger.error(ex, ex);
                    }
                }
                break;
            }
        }
    }//GEN-LAST:event_saveDialogButtonActionPerformed

    private void cancelDialogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelDialogButtonActionPerformed
        commentEditorDialog.dispose();
    }//GEN-LAST:event_cancelDialogButtonActionPerformed

    private void taskOutputMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taskOutputMouseReleased
        if (evt.isPopupTrigger()) {
            outputPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_taskOutputMouseReleased

    private void readNeurolucidaMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readNeurolucidaMenuItemActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_readNeurolucidaMenuItemActionPerformed

    private void writeNeuronInfoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_writeNeuronInfoMenuItemActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_writeNeuronInfoMenuItemActionPerformed

    private void writeObjMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_writeObjMenuItemActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_writeObjMenuItemActionPerformed

    private void readImageStackMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readImageStackMenuItemActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_readImageStackMenuItemActionPerformed

    private void readNeuRaMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readNeuRaMenuItemActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_readNeuRaMenuItemActionPerformed

    private void readWavefrontMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readWavefrontMenuItemActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_readWavefrontMenuItemActionPerformed

    private void netMovieItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_netMovieItemActionPerformed
         command = evt.getActionCommand();
    }//GEN-LAST:event_netMovieItemActionPerformed

    private void densMovieMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_densMovieMenuItemActionPerformed
        NeuGenDensityVisualization dv = NeuGenDensityVisualization.getInstance();
        if (dv != null) {
            dv.makeVideo();
        }
    }//GEN-LAST:event_densMovieMenuItemActionPerformed

    private void readImageSequenceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readImageSequenceMenuItemActionPerformed
        command = evt.getActionCommand();
    }//GEN-LAST:event_readImageSequenceMenuItemActionPerformed

private void writeNGXMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_writeNGXMenuItemActionPerformed
   command = evt.getActionCommand();
}//GEN-LAST:event_writeNGXMenuItemActionPerformed

private void writeTXTMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_writeTXTMenuItemActionPerformed
 command = evt.getActionCommand();
}//GEN-LAST:event_writeTXTMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelDialogButton;
    private javax.swing.JMenuItem clearMenuItem;
    private javax.swing.JMenuItem clearMenuItemPopup;
    private javax.swing.JMenuItem closeProjectMenuItem;
    private javax.swing.JEditorPane comDialogEditorPane;
    private javax.swing.JScrollPane comDialogScrollPane;
    private javax.swing.JDialog commentEditorDialog;
    private javax.swing.JEditorPane commentEditorPane;
    private javax.swing.JPopupMenu commentEditorPopupMenu;
    private javax.swing.JScrollPane configDataScrollPane;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem copyMenuItemPopup;
    private javax.swing.JButton createProjectButton;
    private javax.swing.JMenuItem createProjectMenuItem;
    private javax.swing.JToolBar dataToolBar;
    private javax.swing.JMenuItem densMovieMenuItem;
    private javax.swing.JScrollPane densScrollPane;
    private javax.swing.JMenuItem editCommentMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JSplitPane editorSplitPane;
    private javax.swing.JButton exportButton;
    private javax.swing.JMenu exportMenu;
    private javax.swing.JSeparator fileSeparator;
    private javax.swing.JSeparator fileSeparator2;
    private javax.swing.JSeparator fileSeparator3;
    private javax.swing.JSeparator fileSeparator4;
    private javax.swing.JPopupMenu.Separator fileSeparator5;
    private javax.swing.JButton helpButton;
    private javax.swing.JMenuItem helpContentsMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JToolBar helpToolBar;
    private javax.swing.JScrollPane htmlScrollPane;
    private javax.swing.JToolBar imageToolBar;
    private javax.swing.JButton importButton;
    private javax.swing.JFileChooser importFileChooser;
    private javax.swing.JMenu importMenu;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSplitPane mainSplitPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu movieMenu;
    private javax.swing.JMenuItem netMovieItem;
    private javax.swing.JButton openProjectButton;
    private javax.swing.JFileChooser openProjectFileChooser;
    private javax.swing.JMenuItem openProjectMenuItem;
    private javax.swing.JPopupMenu outputPopupMenu;
    private javax.swing.JScrollPane outputScrollPane;
    private javax.swing.JSeparator popupSeparator;
    private javax.swing.JSeparator popupSeparator2;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JToolBar projectToolBar;
    private javax.swing.JMenuItem readImageSequenceMenuItem;
    private javax.swing.JMenuItem readImageStackMenuItem;
    private javax.swing.JMenuItem readNeuGenMenuItem;
    private javax.swing.JMenuItem readNeuRaMenuItem;
    private javax.swing.JMenuItem readNeuTriaMenuItem;
    private javax.swing.JMenuItem readNeuroMLMenuItem;
    private javax.swing.JMenuItem readNeurolucidaMenuItem;
    private javax.swing.JMenuItem readNeuronMenuItem;
    private javax.swing.JMenuItem readWavefrontMenuItem;
    private javax.swing.JButton runButton;
    private javax.swing.JToolBar runToolBar;
    private javax.swing.JMenuItem saveAsMenuItemPopup;
    private static javax.swing.JButton saveConfigButton;
    private javax.swing.JButton saveDialogButton;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem selectAllMenuItem;
    private javax.swing.JMenuItem selectAllMenuItemPopup;
    private javax.swing.JButton sliderButton;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTextArea taskOutput;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JSplitPane visualDensSplitPane;
    private javax.swing.JSplitPane visualOutputSplitPane;
    private javax.swing.JScrollPane visualScrollPane;
    private javax.swing.JToolBar visualToolBar;
    private javax.swing.JButton visualizeButton;
    private javax.swing.JButton visualizeDensity;
    private javax.swing.JMenuItem writeNGXMenuItem;
    private javax.swing.JMenuItem writeNeuGenMenuItem;
    private javax.swing.JMenuItem writeNeuTriaMenuItem;
    private javax.swing.JMenuItem writeNeuroMLMenuItem;
    private javax.swing.JMenuItem writeNeuronInfoMenuItem;
    private javax.swing.JMenuItem writeNeuronMenuItem;
    private javax.swing.JMenuItem writeObjMenuItem;
    private javax.swing.JMenuItem writeTXTMenuItem;
    // End of variables declaration//GEN-END:variables

    /**
     * Get the project type
     *
     * @return the value of currentProjectType
     */
    public String getCurrentProjectType() {
        return currentProjectType;
    }

    /**
     * Get the full path of Param.neu
     *
     * @return the value of paramPath
     */
    public static String getParamPath() {
        return paramPath;
    }

    /**
     * Set the value of paramPath
     *
     * @param paramPath new value of paramPath
     */
    public static void setParamPath(String paramPath) {
        NeuGenView.paramPath = paramPath;
    }

    /**
     * Get the value of internaPath
     *
     * @return the value of internaPath
     */
    public static String getInternaPath() {
        return internaPath;
    }

    /**
     * Set the value of internaPath
     *
     * @param internaPath new value of internaPath
     */
    public static void setInternaPath(String internaPath) {
        NeuGenView.internaPath = internaPath;
    }

    /**
     * Get the value of outputOptionsPath
     *
     * @return the value of outputOptionsPath
     */
    public static String getOutputOptionsPath() {
        return outputOptionsPath;
    }

    /**
     * Set the value of outputOptionsPath
     *
     * @param outputOptionsPath new value of outputOptionsPath
     */
    public static void setOutputOptionsPath(String outputOptionsPath) {
        NeuGenView.outputOptionsPath = outputOptionsPath;
    }

    public JScrollPane getDensScrollPane() {
        return densScrollPane;
    }

    public JSplitPane getVisualDensSplitPane() {
        return visualDensSplitPane;
    }

    public void setNewNet(boolean newNet) {
        this.newNet = newNet;
    }

    public Map<String, XMLObject> getParamTrees() {
        return paramTrees;
    }

    public static NeuGenView getInstance() {
        return instance;
    }

    public JSplitPane getVisualOutputSplitPane() {
        return visualOutputSplitPane;
    }

    public static void setInstance(NeuGenView instance) {
        NeuGenView.instance = instance;
    }

    public String getProjectDirPath() {
        return projectDirPath;
    }

    public static JButton getSaveButton() {
        return saveConfigButton;
    }

    public void setNetExist(boolean netExist) {
        this.netExist = netExist;
    }

    public void outPrint(String message) {
        taskOutput.append(message);
        taskOutput.setCaretPosition(taskOutput.getDocument().getLength());
    }

    public void outPrintln(String message) {
        if (taskOutput != null) {
            taskOutput.append(message + "\n");
            taskOutput.setCaretPosition(taskOutput.getDocument().getLength());
        }
    }

    public void outPrintln() {
        if (taskOutput != null) {
            taskOutput.append("\n");
        }
    }

    public void setStatusMessage(String message) {
        if (statusMessageLabel != null) {
            statusMessageLabel.setText(message);
        }
    }

    public Net getNet() {
        return net;
    }

    public void setNet(Net net) {
        this.net = net;
    }

    public JScrollPane getVisualScrollPane() {
        return visualScrollPane;
    }

    public void setVisualScrollPane(JScrollPane visualScrollPane) {
        this.visualScrollPane = visualScrollPane;
    }

    public JButton getRunButton() {
        return this.runButton;
    }

    public JButton getVisualizeButton() {
        return this.visualizeButton;
    }

    public JButton getVisualizeDensityButton() {
        return this.visualizeDensity;
    }

    @Action
    public Task generateSlider() {
        destroyVisualizeTasks();
        disableButtons();
        if (sliderTask != null) {
            sliderTask = null;
        }
        sliderTask = new SliderGeneratorTask(getApplication(), net);
        return sliderTask;
    }

    @Action
    public Task visualizeData() {

        if (visual == null) {
            visualizeButton.setEnabled(false);
            return null;
        }

        disableButtons();
        if (visualizeTask != null) {
            if (((VisualizationTask) visualizeTask).getNGVisualization() != null) {
                ((VisualizationTask) visualizeTask).getNGVisualization().destroy();
            }
            visualizeTask = null;
            System.gc();
        }

        if (currentProjectType != null) {
            if (currentProjectType.equals(NeuGenConstants.NEOCORTEX_PROJECT)) {
                Region.setCortColumn(true);
                Region.setCa1Region(false);
            } else if (currentProjectType.equals(NeuGenConstants.HIPPOCAMPUS_PROJECT)) {
                Region.setCortColumn(false);
                Region.setCa1Region(true);
            } else {
                Region.setCortColumn(false);
                Region.setCa1Region(false);
            }
        }

        if (importedData) {
            Region.setCortColumn(false);
            Region.setCa1Region(false);
        }

        switch (visual) {
            case NET:
                visualizeTask = new VisualizationTask(getApplication(), visual);
                break;
            case RECONSTRUCTION:
                visualizeTask = new VisualizationTask(getApplication(), visual, scene);
                break;
            case LOADED_GRAPH:
                visualizeTask = new VisualizationTask(getApplication(), visual, scene);
        }

        VisualizationTask.setInstance((VisualizationTask) visualizeTask);
        return visualizeTask;
    }

    public void destroyVisualizeTasks() {
        if (visualizeTask != null) {
            if (((VisualizationTask) visualizeTask).getNGVisualization() != null) {
                ((VisualizationTask) visualizeTask).getNGVisualization().destroy();
            }
            visualScrollPane.validate();
            visualScrollPane.repaint();
            visualizeTask = null;
            System.gc();
        }

        if (visualDensSplitPane.getRightComponent() != null) {
            visualDensSplitPane.remove(visualDensSplitPane.getRightComponent());
        }

        if (visualizeDensityTask != null) {
            if (NeuGenDensityVisualization.getInstance() != null) {
                NeuGenDensityVisualization.getInstance().destroy();
            }
            densScrollPane.validate();
            densScrollPane.repaint();
            visualizeDensityTask = null;
            System.gc();
        }
    }

    @Action
    public Task visualizeDensity() {
        try {
            //test if java3d installed
            Class.forName("javax.media.j3d.Canvas3D");
            if (visualizeDensityTask != null) {
                if (NeuGenDensityVisualization.getInstance() != null) {
                    NeuGenDensityVisualization.getInstance().destroy();
                }
            }
            densScrollPane.validate();
            densScrollPane.repaint();
            visualizeDensityTask = null;
            System.gc();
            disableButtons();
            if (currentProjectType != null) {
                if (currentProjectType.equals(NeuGenConstants.NEOCORTEX_PROJECT)) {
                    Region.setCortColumn(true);
                    Region.setCa1Region(false);
                } else if (currentProjectType.equals(NeuGenConstants.HIPPOCAMPUS_PROJECT)) {
                    Region.setCortColumn(false);
                    Region.setCa1Region(true);
                } else {
                    Region.setCortColumn(false);
                    Region.setCa1Region(false);
                }
            }
            if (importedData) {
                Region.setCortColumn(false);
                Region.setCa1Region(false);
            }
            switch (density) {
                case NET:
                    visualizeDensityTask = new DensityVisualizationTask(getApplication(), density);
                    break;
                case IMAGE:
                    visualizeDensityTask = new DensityVisualizationTask(getApplication(), density, imageStackFile);
            }
            DensityVisualizationTask.setInstance(visualizeDensityTask);
            return visualizeDensityTask;
        } catch (ClassNotFoundException notFound) {
            String message = NeuGenConstants.J3D_ERROR_MESSAGE;
            logger.error(message + "\n" + notFound, notFound);
            JOptionPane.showMessageDialog(super.getFrame(), message, "visualization impossible", JOptionPane.ERROR_MESSAGE);
            visualizeDensity.setVisible(false);
            visualizeButton.setVisible(false);
            return null;
        }
    }

    @Action
    public void showHelpWindow() {
        NeuGenHelp.getInstance().getfDisplayHelp();
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = NeuGenApp.getApplication().getMainFrame();
            aboutBox = new NGAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        NeuGenApp.getApplication().show(aboutBox);
    }

    @Action
    public Task importData() {
        Task task = null;
        importFileChooser.setDialogTitle("Import Data");
        importFileChooser.resetChoosableFileFilters();

        if (command.equals(readNeuronMenuItem.getActionCommand())) {
            importFileChooser.addChoosableFileFilter(new NGFileFilter.NeuronFileFilter());
        } else if (command.equals(readNeuGenMenuItem.getActionCommand())) {
            importFileChooser.addChoosableFileFilter(new NGFileFilter.NeuGenVisualFileFilter());
        } else if (command.equals(readNeuroMLMenuItem.getActionCommand())) {
            importFileChooser.addChoosableFileFilter(new NGFileFilter.NeuroMLFileFilter());
        } else if (command.equals(readNeuTriaMenuItem.getActionCommand())) {
            importFileChooser.addChoosableFileFilter(new NGFileFilter.NeuTriaFileFilter());
        } else if (command.equals(readNeurolucidaMenuItem.getActionCommand())) {
            importFileChooser.addChoosableFileFilter(new NGFileFilter.CvappFileFilter());
        } else if (command.equals(readImageStackMenuItem.getActionCommand())) {
            importFileChooser.addChoosableFileFilter(new NGFileFilter.ImageFileFilter());
        }
        else if (command.equals(readImageSequenceMenuItem.getActionCommand())) {
            importFileChooser.addChoosableFileFilter(new NGFileFilter.ImageSequenceFileFilter());
        }
        else if (command.equals(readNeuTriaMenuItem.getActionCommand())) {
            importFileChooser.addChoosableFileFilter(new NGFileFilter.NeuRAFileFilter());
        } else if (command.equals(readWavefrontMenuItem.getActionCommand())) {
            importFileChooser.addChoosableFileFilter(new NGFileFilter.ObjFileFilter());
        } else {
            importFileChooser.addChoosableFileFilter(new NGFileFilter.NeuGenVisualFileFilter());
            //importFileChooser.addChoosableFileFilter(new NGFileFilter.NeuTriaFileFilter());
            importFileChooser.addChoosableFileFilter(new NGFileFilter.NeuroMLFileFilter());
            importFileChooser.addChoosableFileFilter(new NGFileFilter.CvappFileFilter());
            importFileChooser.addChoosableFileFilter(new NGFileFilter.ImageFileFilter());
            importFileChooser.addChoosableFileFilter(new NGFileFilter.ImageSequenceFileFilter());
            importFileChooser.addChoosableFileFilter(new NGFileFilter.NeuRAFileFilter());
            importFileChooser.addChoosableFileFilter(new NGFileFilter.ObjFileFilter());
            //importFileChooser.addChoosableFileFilter(new NGFileFilter.NeuronFileFilter());
        }

        int fileChooserOption = importFileChooser.showOpenDialog(super.getFrame());
        if (fileChooserOption == JFileChooser.APPROVE_OPTION) {
            disableButtons();
            try {
                FileFilter fileFilter = importFileChooser.getFileFilter();
                if (fileFilter instanceof NGFileFilter.NeuroMLFileFilter) {
                    File f = importFileChooser.getSelectedFile();
                    task = new NeuroMLReaderTask(getApplication(), f);
                    this.visual = VisualizationTask.Visualization.NET;
                    importedData = false; //netz wurde erzeugt
                } else if (fileFilter instanceof NGFileFilter.NeuTriaFileFilter) {
                    File f = importFileChooser.getSelectedFile();
                    task = new SimpleHocReaderTask(getApplication(), f);
                    //importedData = true;
                } else if (fileFilter instanceof NGFileFilter.CvappFileFilter) {
                    File f = importFileChooser.getSelectedFile();
                    task = new SWCReaderTask(getApplication(), f);
                    this.visual = VisualizationTask.Visualization.NET;
                    importedData = false; //netz wurde erzeugt
                } else if (fileFilter instanceof NGFileFilter.NeuGenVisualFileFilter) {
                    File f = importFileChooser.getSelectedFile();
                    task = new OBJReader(getApplication(), f, OBJReader.ReaderType.BG);
                    destroyVisualizeTasks();
                    this.visualizeButton.setEnabled(true);
                    this.visualizeDensity.setEnabled(false);
                    this.visual = VisualizationTask.Visualization.LOADED_GRAPH;
                    this.importedData = true;
                } else if (fileFilter instanceof NGFileFilter.NeuronFileFilter) {
                    logger.error("TODO: Import for Neuron hoc files");
                } else if (fileFilter instanceof NGFileFilter.ImageFileFilter || fileFilter instanceof NGFileFilter.ImageSequenceFileFilter) {
                    
                    if(fileFilter instanceof NGFileFilter.ImageSequenceFileFilter) {
                        logger.info("read sequence!");
                        this.imageSequence = true;
                    } else {
                        this.imageSequence = false;
                    }

                    this.importedData = true;
                    this.density = DensityVisualizationTask.Density.IMAGE;
                    this.visualizeDensity.setEnabled(true);
                    this.imageStackFile = importFileChooser.getSelectedFile();
                    this.visualizeDensity().run();
                } else if (fileFilter instanceof NGFileFilter.NeuRAFileFilter) {
                    File f = importFileChooser.getSelectedFile();
                    task = new OBJReader(getApplication(), f, OBJReader.ReaderType.TXT);
                    destroyVisualizeTasks();
                    this.visualizeButton.setEnabled(true);
                    this.visualizeDensity.setEnabled(false);
                    this.visual = VisualizationTask.Visualization.RECONSTRUCTION;
                    this.importedData = true;
                } else if (fileFilter instanceof NGFileFilter.ObjFileFilter) {
                    File f = importFileChooser.getSelectedFile();
                    task = new OBJReader(getApplication(), f, OBJReader.ReaderType.OBJ);
                    destroyVisualizeTasks();
                    this.visualizeButton.setEnabled(true);
                    this.visualizeDensity.setEnabled(false);
                    this.visual = VisualizationTask.Visualization.RECONSTRUCTION;
                    this.importedData = true;
                }
            } catch (Exception e) {
                logger.error(e, e);
            }
            enableButtons();
        }
        return task;
    }

    public boolean isImageSequence() {
        return imageSequence;
    }

    public void enableVisualizeDensity() {
        this.visualizeDensity.setEnabled(true);
    }

    public void enableVisualize() {
        this.visualizeButton.setEnabled(true);
    }

    public void enableDensMovieButton() {
        this.densMovieMenuItem.setEnabled(true);
    }

    public void enableWriteNeuGenBG(){
        this.writeNeuGenMenuItem.setEnabled(true);
    }

    public void enableButtons() {
        // project buttons
        createProjectButton.setEnabled(true);
        createProjectMenuItem.setEnabled(true);

        openProjectButton.setEnabled(true);
        openProjectMenuItem.setEnabled(true);

        // import data buttons
        /*      
        readNeuTriaMenuItem.setEnabled(true);
        readNeuronMenuItem.setEnabled(true);
         */
        readNeuGenMenuItem.setEnabled(true);
        readNeuroMLMenuItem.setEnabled(true);
        readNeurolucidaMenuItem.setEnabled(true);
        importButton.setEnabled(true);

        if (netExist) {
            if (!importedData) {
                writeNeuGenMenuItem.setEnabled(true);
                writeNeuTriaMenuItem.setEnabled(true);
                writeNeuroMLMenuItem.setEnabled(true);
                writeNeuronMenuItem.setEnabled(true);
                writeNeuronInfoMenuItem.setEnabled(true);
                writeNGXMenuItem.setEnabled(true);
                writeTXTMenuItem.setEnabled(true);
                exportButton.setEnabled(true);
                visualizeButton.setEnabled(true);
                sliderButton.setEnabled(true);
            }
        }

        if (projectExist) {
            runButton.setEnabled(true);
            closeProjectMenuItem.setEnabled(true);
        }

        if ((projectExist && netExist)) {
            if (!importedData) {
                visualizeDensity.setEnabled(true);
            }
        }

        if (projectTree != null) {
            if (projectTree.getContentChanged() || newNet) {
                saveConfigButton.setEnabled(true);
            }
        }
    }

    public void disableButtons() {
        // project buttons
        createProjectButton.setEnabled(false);
        createProjectMenuItem.setEnabled(false);
        openProjectButton.setEnabled(false);
        openProjectMenuItem.setEnabled(false);
        closeProjectMenuItem.setEnabled(false);
        saveConfigButton.setEnabled(false);
        // import data buttons
        readNeuGenMenuItem.setEnabled(false);
        readNeuTriaMenuItem.setEnabled(false);
        readNeuroMLMenuItem.setEnabled(false);
        readNeuronMenuItem.setEnabled(false);
        readNeurolucidaMenuItem.setEnabled(false);
        importButton.setEnabled(false);
        // export data buttons
        writeNeuGenMenuItem.setEnabled(false);
        writeNeuTriaMenuItem.setEnabled(false);
        writeNeuroMLMenuItem.setEnabled(false);
        writeNeuronMenuItem.setEnabled(false);
        writeNeuronInfoMenuItem.setEnabled(false);
        writeNGXMenuItem.setEnabled(false);
        writeTXTMenuItem.setEnabled(false);
        exportButton.setEnabled(false);
        sliderButton.setEnabled(false);
        // visualize buttons
        visualizeButton.setEnabled(false);
        visualizeDensity.setEnabled(false);

        runButton.setEnabled(false);

        densMovieMenuItem.setEnabled(false);
    }

    @Action
    public Task startCalc() {
        if (calcTask != null) {
            ((NeuGenLibTask) calcTask).getNGLib().getNet().destroy();
            ((NeuGenLibTask) calcTask).getNGLib().destroy();
            net = null;
            calcTask = null;
        }
        destroyVisualizeTasks();
        disableButtons();
        NeuGenLib.initParamData(initParamTable(), currentProjectType);
        calcTask = new NeuGenLibTask(getApplication(), currentProjectType);
        NeuGenLibTask.setInstance((NeuGenLibTask) calcTask);
        importedData = false;
        density = DensityVisualizationTask.Density.NET;
        visual = VisualizationTask.Visualization.NET;
        //logger.debug("startCalc: " + projectDirPath);
        return calcTask;
    }

    @Action
    public void createProject() throws Exception {
        NeuGenProject projectDialog = new NeuGenProject(new javax.swing.JFrame(), true);
        projectDialog.setVisible(true);
        projectDialog.setLocationByPlatform(true);
        projectDirPath = projectDialog.getProjectDirectory();
        if (projectDirPath != null) {
            initProjectParam(projectDirPath);
        }
    }

    public void initProjectParam(String dirPath) throws Exception {
        closeProject();
        File param = new File(dirPath + System.getProperty("file.separator") + NeuGenConstants.PARAM_FNAME);
        setParamPath(param.getPath());

	System.err.println("PARAM: " + param.getAbsolutePath());
	
        File interna = new File(dirPath + System.getProperty("file.separator") + NeuGenConstants.INTERNA_FNAME);
        setInternaPath(interna.getPath());
        
        /// correct params dialog (get density value for debugging purposes)
        GlobalParameterDialog dialog = new GlobalParameterDialog(NeuGenView.getInstance().getFrame(), true);
        dialog.setVisible(true);
        logger.info("*** Density *** : " + dialog.getNpartsDensity());
        logger.info("*** Synapse distance *** : " + dialog.getSynapseDistance());
        
	/// needs to be corrected before writing since cached within ConfigParserContainer
	String content = IOUtils.toString(new FileInputStream(param));
	content = content.replaceAll("<real key=\"nparts_density\">\\d*.\\d*</real>", "<real key=\"nparts_density\">"+ dialog.getNpartsDensity() + "</real>");
	IOUtils.write(content, new FileOutputStream(param));
	
        Properties paramProper = new Properties();
        Properties internaProper = new Properties();
        File projectDir = new File(dirPath);
        String projectName = projectDir.getName();
        Properties projectProper = NeuGenComments.getProjectPropFromXML(dirPath);

        projectTree = new XMLTreeView(this);
        projectTree.removeAll();
        projectTree.setName(projectName);
        

        XMLObject paramRoot = loadParam(param, paramRoot = null);
        {
            NeuGenComments paramCom = new NeuGenComments(paramRoot, projectDir, Utils.getPrefix(param));
            paramProper = paramCom.getComments();
        }

        XMLObject internaRoot = loadParam(interna, internaRoot = null);
        {
            NeuGenComments paramCom = new NeuGenComments(internaRoot, projectDir, Utils.getPrefix(interna));
            internaProper = paramCom.getComments();
        }

   
     
        paramTrees = new HashMap<String, XMLObject>();
        paramTrees.put(NeuGenConstants.PARAM, paramRoot);
        paramTrees.put(NeuGenConstants.INTERNA, internaRoot);
        
        /// actually correct the parameters in the GUI view (though, the parameters
        /// are still stored in the ConfigParser somehow - to be sure we need to 
        /// save the project below again)
        ///dialog.correct_params();
        dialog.set_global_parameters();

        
   
        XMLObject top = new XMLObject(projectName, null, XMLObject.class.toString());
        top.add(paramRoot);
        paramRoot.setParent(top);
        top.addChild(paramRoot);
        top.add(internaRoot);
        internaRoot.setParent(top);
        top.addChild(internaRoot);
        initTree(top);

        projectTree.addPropertyChangeL(new ConfigTreePropertyChangeListener());
        projectTree.setModel(new DefaultTreeModel(top));

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        Icon nodeIcon = null;
        renderer.setLeafIcon(nodeIcon);
        //renderer.setClosedIcon(nodeIcon);
        //renderer.setOpenIcon(nodeIcon);
        projectTree.setCellRenderer(renderer);
        //projectTree.setOpaque(false);
        projectTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        projectTree.validate();
        commentPropList = new ArrayList<Properties>();
        //commentPropList.add(projectProper);
        commentPropList.add(paramProper);
        commentPropList.add(internaProper);

        //mouse listener to show comments
        CommentMouseListener commentMouseListener = new CommentMouseListener(commentEditorPane, commentPropList, outputPopupMenu, projectProper);
        commentMouseListener.addPropertyChangeListener(new PropertyChangeListener() {
            //get new key

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Object newPropKey = evt.getNewValue();
                currentPropKey = newPropKey.toString();
                //logger.info("(project tree)current prop key: " + currentPropKey);
            }
        });

        projectTree.addMouseListener(commentMouseListener);
        configDataScrollPane.setViewportView(projectTree);
        configDataScrollPane.validate();
        configDataScrollPane.repaint();

        commentEditorPane.removeAll();
        //popupMenu for editing comments
        commentEditorPane.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                int clicks = evt.getClickCount();
                if (clicks > 1) {
                    //commentEditorPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                    commentEditorDialog.setVisible(true);
                    for (Properties comProp : commentPropList) {
                        if (comProp.containsKey(currentPropKey)) {
                            comDialogEditorPane.setText(comProp.getProperty(currentPropKey));
                            break;
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                maybeShowPopup(evt);
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                maybeShowPopup(evt);
            }

            private void maybeShowPopup(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    commentEditorPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        });
        currentPropKey = projectDir.getName();
        commentEditorPane.setText(projectProper.getProperty(currentPropKey));
        projectExist = true;
        importedData = false;
        density = DensityVisualizationTask.Density.NET;
        visual = VisualizationTask.Visualization.NET;
        disableButtons();
        enableButtons();
        currentProjectType = projectProper.getProperty(NeuGenConstants.PROP_PROJECT_NAME_KEY);
        outPrintln("Project type: " + currentProjectType);
        outPrintln("Created on: " + projectProper.getProperty(NeuGenConstants.PROP_DATE_KEY));
       
        /// save project with new parameters
        projectTree.setContentChanged(true); 
        save();
        projectTree.setContentChanged(false);
        
        NeuGenLib.initParamData(initParamTable(), currentProjectType);  
    }

    public Map<String, XMLObject> initParamTable() { 
       /// get params
        XMLObject param = getParamTrees().get(NeuGenConstants.PARAM);
        XMLObject interna = getParamTrees().get(NeuGenConstants.INTERNA);
        Map<String, XMLObject> allParam = new HashMap<String, XMLObject>();
        
        allParam.put(getParamPath(), param);
        allParam.put(getInternaPath(), interna);
        return allParam;
    }

    public XMLObject loadParam(File file, XMLObject root) throws Exception {
        NeuGenConfigStreamer stream = new NeuGenConfigStreamer(null);
        root = stream.streamIn(file);
        DefaultInheritance inhProzess = new DefaultInheritance();
        root = inhProzess.process(root);
        return root;
    }

    public XMLObject initTree(XMLObject root) throws Exception {
        MenuHandlerFactory mhSource = new InhMHFactory(projectTree, root);
        LinkedList<XMLObject> queue = new LinkedList<XMLObject>();
        queue.addFirst(root);
        while (queue.size() != 0) {
            XMLNode current = (XMLNode) queue.getLast();
            //logger.info(current.getPathLocal());
            //current.setHandler(mhSource.getHandler(current));
            //logger.info(current.getHandler().getClass().toString());
            queue.removeLast();
            XMLObject currentObj = XMLObject.convert(current);
            if (currentObj != null) {
                for (int i = 0; i < currentObj.getChildrenCount(); i++) {
                    XMLNode currentChild = currentObj.getChild(i);
                    XMLObject currentChildObj = XMLObject.convert(currentChild);
                    if (currentChildObj != null) {
                        queue.addFirst(currentChildObj);
                    } else {
                        currentChild.setHandler(mhSource.getHandler(currentChild));
                    }
                }
            }
        }
        return root;
    }

    @Action
    public Task closeProject() {
        trigger = Trigger.getInstance();
        trigger.setTextMessage("cleaning...");
        if (calcTask != null) {
            ((NeuGenLibTask) calcTask).getNGLib().getNet().destroy();
            ((NeuGenLibTask) calcTask).getNGLib().destroy();
            net = null;
            calcTask = null;
        }
        taskOutput.setText("");
        if (projectTree != null && projectTree.ableToClose()) {
            commentEditorPane.setText("");
            configDataScrollPane.remove(projectTree);
            projectTree.setVisible(false);
            projectTree.setContentChanged(false);
            projectTree.removeAll();
            projectTree = null;
            projectExist = false;
            netExist = false;
            newNet = false;
            disableButtons();
            enableButtons();
            destroyVisualizeTasks();
        }
        return trigger;
    }

    @Action
    public Task openProject() throws IOException, Exception {
        Task task = null;
        JFileChooser fch = new JFileChooser(".");
        fch.setDialogTitle("Open Project");
        fch.resetChoosableFileFilters();
        fch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int fileChooserOption = fch.showDialog(openProjectButton, "Open Project");
        //logger.info("fileChooserOption: " + fileChooserOption);
        if (fileChooserOption == JFileChooser.APPROVE_OPTION) {
            File projectDir = fch.getSelectedFile();
            String dirPath = projectDir.getPath();
            projectDirPath = dirPath;
            density = DensityVisualizationTask.Density.NET;
            visual = VisualizationTask.Visualization.NET;
            File neuGenProjectFile = new File(dirPath + System.getProperty("file.separator") + NeuGenConstants.NEUGEN_PROJECT_FILE);
            if (!neuGenProjectFile.canRead()) {
                dirPath = projectDir.getParent();
                neuGenProjectFile = new File(dirPath + System.getProperty("file.separator") + NeuGenConstants.NEUGEN_PROJECT_FILE);
                //logger.info(neuGenProjectFile.getPath());
            }
            if (neuGenProjectFile.canRead()) {
                importedData = false;
                initProjectParam(dirPath);
                File netFile = new File(dirPath + System.getProperty("file.separator") + "net.ser");
                if (netFile.exists()) {
                    netExist = true;
                    //task = new NeuroMLReaderTask(getApplication(), netFile);
                    //NeuGenVisualization.setCortColumn(true);
                    task = new NeuGenReaderTask(getApplication());
                }
            } else {
                JOptionPane.showMessageDialog(super.getFrame(), "wrong project directory");
            }
        }
        return task;
    }

    @Action
    public Task exportData() {
        Task task = null;
        JFileChooser exportFileChooser = new javax.swing.JFileChooser(".");
        //visualOutputSplitPane.setSelectedComponent(nfcScrollPane);
        exportFileChooser.setAcceptAllFileFilterUsed(false);
        exportFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        exportFileChooser.setName("exportFileChooser");
        exportFileChooser.resetChoosableFileFilters();
        exportFileChooser.setDialogTitle("Export Data");
        logger.info("command: " + command + " and getActionCommand: " + writeNGXMenuItem.getActionCommand());
        if (command.equals(writeNeuronMenuItem.getActionCommand())) {
            exportFileChooser.addChoosableFileFilter(new NGFileFilter.NeuronFileFilter());
        } else if (command.equals(writeNGXMenuItem.getActionCommand())) {
            exportFileChooser.addChoosableFileFilter(new NGFileFilter.NGXFileFilter());
        } else if (command.equals(writeTXTMenuItem.getActionCommand())) {
            exportFileChooser.addChoosableFileFilter(new NGFileFilter.TXTFileFilter());
        } else if (command.equals(writeNeuGenMenuItem.getActionCommand())) {
            exportFileChooser.addChoosableFileFilter(new NGFileFilter.NeuGenVisualFileFilter());
        } else if (command.equals(writeNeuroMLMenuItem.getActionCommand())) {
            exportFileChooser.addChoosableFileFilter(new NGFileFilter.NeuroMLFileFilter());
        } else if (command.equals(writeNeuTriaMenuItem.getActionCommand())) {
            exportFileChooser.addChoosableFileFilter(new NGFileFilter.NeuTriaFileFilter());
        } else if (command.equals(writeNeuronInfoMenuItem.getActionCommand())) {
            exportFileChooser.addChoosableFileFilter(new NGFileFilter.CSVFileFilter());
        } else if (command.equals(writeObjMenuItem.getActionCommand())) {
            exportFileChooser.addChoosableFileFilter(new NGFileFilter.ObjFileFilter());
        } else if (command.equals(exportButton.getActionCommand())) {
            //exportFileChooser.addChoosableFileFilter(new NeuGenVisualFileFilter());
            exportFileChooser.addChoosableFileFilter(new NGFileFilter.NeuTriaFileFilter());
            exportFileChooser.addChoosableFileFilter(new NGFileFilter.NeuroMLFileFilter());
            exportFileChooser.addChoosableFileFilter(new NGFileFilter.NeuronFileFilter());
            exportFileChooser.addChoosableFileFilter(new NGFileFilter.CSVFileFilter());
        }
        int fileChooserOption = exportFileChooser.showSaveDialog(exportButton);
        if (fileChooserOption == JFileChooser.APPROVE_OPTION) {
            disableButtons();
            try {
                FileFilter fileFilter = exportFileChooser.getFileFilter();
                File f = exportFileChooser.getSelectedFile();
                if (fileFilter instanceof NGFileFilter.NeuronFileFilter) {
                    if (Utils.testExistingFile(exportFileChooser)) {
                        task = new HocWriterTask(getApplication(), f);
                        logger.info("Exporting hoc file: " + f.getName());
                    }
                } else if (fileFilter instanceof NGFileFilter.NeuTriaFileFilter) {
                    if (Utils.testExistingFile(exportFileChooser)) {
                        task = new SimpleHocWriterTask(getApplication(), f, this);
                        logger.info("Exporting simplified hoc file");
                        statusMessageLabel.setText("Exporting " + exportFileChooser.getSelectedFile().getName());
                    }
                } else if (fileFilter instanceof NGFileFilter.NeuroMLFileFilter) {
                    if (Utils.testExistingFile(exportFileChooser)) {
                        task = new NeuroMLWriterTask(getApplication(), f);
                    }
                } else if (fileFilter instanceof NGFileFilter.TXTFileFilter) {
                    logger.info("Writing TXT task now...");
                    if (Utils.testExistingFile(exportFileChooser)) {
                        task = new TXTWriterTask(getApplication(), f);
                    }
                } else if (fileFilter instanceof NGFileFilter.NGXFileFilter) {
                    logger.info("Writing NGX task now...");
                    if (Utils.testExistingFile(exportFileChooser)) {
                        task = new NGXWriterTask(getApplication(), f);
                    }
                } else if (fileFilter instanceof NGFileFilter.ObjFileFilter) {
                    if (Utils.testExistingFile(exportFileChooser)) {
                        //task = new NeuroMLWriterTask(getApplication(), f);
                        //OBJWriter writer = new OBJWriter("hallo.obj", "Test", 3);                        
                    }
                } else if (fileFilter instanceof NGFileFilter.CSVFileFilter) {
                    if (Utils.testExistingFile(exportFileChooser)) {
                        task = new CSVWriterTask(getApplication(), f);
                    }
                } else if (fileFilter instanceof NGFileFilter.NeuGenVisualFileFilter) {
                    if (Utils.testExistingFile(exportFileChooser)) {
                        task = new NeuGenVisualWriterTask(getApplication(), f);
                    }
                }
            } catch (Exception e) {
                task = new Trigger(getApplication());
                ((Trigger) task).setTextMessage("error: " + e);
                logger.error("Error writing file..: " + e, e);
            }
            enableButtons();
        } else {
            String fileName = "";
            if (exportFileChooser.getSelectedFile() != null) {
                fileName = exportFileChooser.getSelectedFile().getName();
            }
            task = new Trigger(getApplication());
            ((Trigger) task).setTextMessage("Cancelled exporting " + fileName + " due to your wish");
        }
        return task;
    }

    private class ExportDataTask extends org.jdesktop.application.Task<Object, Void> {
        ExportDataTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to ExportDataTask fields, here.
            super(app);
        }
        @Override protected Object doInBackground() {
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
            return null;  // return your result
        }
        @Override protected void succeeded(Object result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
        }
    }

    @Action
    public Task save() throws IOException {
        Task task = null;
        XMLNode root = (XMLNode) projectTree.getModel().getRoot();
        save(root);
        if (newNet) {
            task = new NeuGenWriterTask(getApplication());
        }
        return task;
    }

    @Override
    public void save(XMLNode currentRoot) {
        List<XMLNode> children = (XMLObject.convert(currentRoot)).getChildren();
        logger.info(currentRoot.getLeafCount());
        for (XMLNode child : children) {
            logger.info("child key: " + child.getKey());
            XMLObject rootCopy = XMLObject.getCopyXMLObject(XMLObject.convert(child));
            DefaultInheritance.reverseProcess(rootCopy);
            NeuGenConfigStreamer streamer = new NeuGenConfigStreamer(projectDirPath);
            //neuGenComments.setCurrentDir(saveFileChooser.getCurrentDirectory());
            String neuPath = null;
            if (child.getKey().contains(NeuGenConstants.PARAM)) {
                neuPath = projectDirPath + System.getProperty("file.separator") + NeuGenConstants.PARAM_FNAME;
            } else if (child.getKey().contains(NeuGenConstants.INTERNA)) {
                neuPath = projectDirPath + System.getProperty("file.separator") + NeuGenConstants.INTERNA_FNAME;
            }
            if (neuPath != null) {
                File neuFile = new File(neuPath);
                try {
                    logger.info("speichere Datei: " + neuFile.getAbsolutePath());
                    streamer.streamOut(rootCopy, neuFile);
                } catch (IOException ex) {
                    logger.error(ex, ex);
                }
            }
        }
        saveConfigButton.setEnabled(false);
        projectTree.setContentChanged(false);
    }

    @Action
    public Task saveOutputText() {
        trigger = Trigger.getInstance();
        JFileChooser fch = new JFileChooser(".");
        fch.resetChoosableFileFilters();
        fch.addChoosableFileFilter(new NGFileFilter.TextFileFilter());
        int fileChooserOption = 0;
        fileChooserOption = fch.showSaveDialog(outputPopupMenu);
        if (fileChooserOption == JFileChooser.APPROVE_OPTION) {
            try {
                if (Utils.testExistingFile(fch)) {
                    File currentFile = fch.getSelectedFile();
                    String extension = Utils.getExtension(currentFile);
                    if (!("." + extension).equals(".txt")) {
                        currentFile = new File(currentFile.getName() + ".txt");
                    }
                    saveText(currentFile);
                    fch.setSelectedFile(currentFile);
                    //statusMessageLabel.setText("saving: " + currentFile.getName());
                    trigger.setTextMessage("saving: " + currentFile.getName());
                }
            } catch (Exception e) {
                logger.error(e, e);
            }
        } else {
            if (fch.getSelectedFile() != null) {
                //statusMessageLabel.setText("Cancelled saving " + fch.getSelectedFile().getName() + " due to your wish");
                trigger.setTextMessage("Cancelled saving " + fch.getSelectedFile().getName() + " due to your wish");
            }
        }
        return trigger;
    }

    public void saveText(File file) {
        try {
            FileWriter writer = new FileWriter(file);
            String text = this.taskOutput.getText();
            writer.write(text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.error(e, e);
        }
    }

    @Action
    public void clearTaskOutput() {
        taskOutput.setText("");
    }

    @Action
    public void copyTaskOutput() {
        taskOutput.copy();
    }

    @Action
    public void selectAllFromTaskOutput() {
        taskOutput.requestFocus();
        taskOutput.selectAll();
    }

    @Action
    public void quit() {
        if (projectTree == null) {
//            System.exit(0);
        } else if (projectTree.ableToClose()) {
            System.gc();
//            System.exit(0);
        } else {
            System.gc();
//            System.exit(0);
        }
    }

    private class NeuGenExitListener implements ExitListener {

        @Override
        public boolean canExit(EventObject event) {
            return projectTree.ableToClose();
        }

        @Override
        public void willExit(EventObject event) {
            logger.info("bye");
        }
    }

    private class ConfigTreePropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (neuGenExitListener != null) {
                NeuGenApp.getApplication().removeExitListener(neuGenExitListener);
            }
            neuGenExitListener = new NeuGenExitListener();
            String strValue = evt.getNewValue().toString();
            Boolean boolValue = Boolean.valueOf(strValue);
            if (boolValue.booleanValue() == true) {
                NeuGenApp.getApplication().addExitListener(neuGenExitListener);
                //neuGenApp.addExitListener(neuGenExitListener);
                saveConfigButton.setEnabled(true);
                saveMenuItem.setEnabled(true);
            } else {
                saveConfigButton.setEnabled(false);
                saveMenuItem.setEnabled(false);
            }
            //logger.info("number of exit list: " + neuGenApp.getExitListeners().length);
        }
    }
}
