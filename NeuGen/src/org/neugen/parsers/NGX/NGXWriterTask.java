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

/// package's name
package org.neugen.parsers.NGX;

/// imports
import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.xml.XMLNode;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.gui.NGXDialog;
import org.neugen.gui.NeuGenView;
import org.neugen.parsers.HocWriterTask;

/**
 * @brief the NGX writer task
 * @author stephanmg <stephan@syntaktischer-zucker.de>
 */
public final class NGXWriterTask extends Task<Void, Void> {
    private static final Logger logger = Logger.getLogger(HocWriterTask.class.getName());
    private final File file;

    /**
     * @breif ctor
     * @param app
     * @param f 
     */
    public NGXWriterTask(Application app, File f) {
        super(app);
        file = f;
    }

    /**
     * @brief show progress bar
     * @param value
     * @param min
     * @param max 
     */
    public void setMyProgress(int value, int min, int max) {
        setProgress(value, min, max);
    }

    /**
     * @brief the actual export 
     * @return 
     */
    @Override
    @SuppressWarnings("deprecation")
    protected Void doInBackground() {
        Net net = NeuGenView.getInstance().getNet();
        
        NGXWriter ngxWriter = new NGXWriter(net, file);
                
        NGXDialog dialog = new NGXDialog(NeuGenView.getInstance().getFrame(), true);
        dialog.setVisible(true);
        /*
        //net.destroy();
        for (Entry<String, XMLObject> entry : NeuGenView.getInstance().getParamTrees().entrySet()) {
            System.err.println("Key:" + entry.getKey());
            XMLObject obj = entry.getValue();
            //if ("net".equals(entry.getKey())) {
               Enumeration<XMLNode> childs = obj.children();
            
               while (childs.hasMoreElements()) {
                 XMLNode node = childs.nextElement();
                 System.err.println("Node: " + node.toString());
                 if ("neuron".equals(node.toString())) {
                    Enumeration<XMLNode> childs2 = node.children();
                    while (childs2.hasMoreElements()) {
                        XMLNode node2 = childs2.nextElement();
                        if ("axon".equals(node2.toString())) {
                            Enumeration<XMLNode> childs3 = node2.children();
                            while (childs3.hasMoreElements()) {
                                XMLNode child4 = childs3.nextElement();
                                System.err.println("axon child: " + child4.toString());
                                if ("gen_0".equals(child4.toString())) {
                                    Enumeration<XMLNode> childs5 = child4.children();
                                    
                                    while (childs5.hasMoreElements()) {
                                        XMLNode child6 = childs5.nextElement();
                            
                                      
                                        if ("nparts_density".equals(child6.getKey())) {
                                            System.err.println("child6 (before): " + child6.toString());
                                            System.err.println("child6's key (before): " + child6.getKey());
                                            child6.setValue(1);
                                        
                                        
                                            System.err.println("child6 (after): " + child6.toString());
                                            System.err.println("child6's key (after): " + child6.getKey());
                                        }
                                        
                                    }
                                }
                            }
                        
                            
                            
                            } else if ("dendrite".equals(node2.toString())) {
                            
                            } else {
                            
                            }
                        }
                    }
                }
            //}
             
           System.err.println("Value: " + entry.getValue());
        }*/
        float npartsdens = dialog.getNpartsDensity();
        
        //net.generate();
        
	logger.info("Exporting NGX data to... " + file.getName());
        setMessage("Exporting NGX data to... " + file.getName());
        ngxWriter.exportNetToNGX();
        return null;  // return your result
    }

    /**
     * @brief on success
     * @param result 
     */
    @Override
    protected void succeeded(Void result) {
    }
}

