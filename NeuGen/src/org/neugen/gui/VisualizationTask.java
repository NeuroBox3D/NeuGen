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
package org.neugen.gui;

import com.sun.j3d.loaders.Scene;
import javax.media.j3d.BranchGroup;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.neugen.utils.Utils;
import org.neugen.visual.NeuGenVisualization;

/**
 * @author Sergei Wolf
 */
public final class VisualizationTask extends org.jdesktop.application.Task<Void, Void> {

    private static final Logger logger = Logger.getLogger(VisualizationTask.class.getName());
    private NeuGenVisualization ngVisual;
    private Scene s;
    private static VisualizationTask instance;

    public static VisualizationTask getInstance() {
        return instance;
    }

    public static void setInstance(VisualizationTask instance) {
        VisualizationTask.instance = instance;
    }

    public enum Visualization {

        NET, RECONSTRUCTION, LOADED_GRAPH;
    }
    private Visualization vis;

    public VisualizationTask(Application application, Visualization vis, Scene s) {
        super(application);
        this.vis = vis;
        this.s = s;
    }

    public VisualizationTask(Application application, Visualization vis) {
        super(application);
        this.vis = vis;
    }

    public NeuGenVisualization getNGVisualization() {
        return ngVisual;
    }

    public void setMyProgress(int value, int min, int max) {
        setProgress(value, min, max);
    }

    public void setMyProgress(float percentage) {
        if (Float.isInfinite(percentage)) {
            logger.info("percentage infinite: " + percentage);
        } else if (Float.isNaN(percentage)) {
            logger.info("percentage isNaN: " + percentage);
        } else {
            setProgress(percentage);
        }
    }

    @Override
    protected Void doInBackground() {
        setMessage("Visualizing...");
        NeuGenView.getInstance().outPrintln(Utils.getMemoryStatus());
        switch (vis) {
            case RECONSTRUCTION: {
                OBJDialog objD = new OBJDialog(NeuGenView.getInstance().getFrame(), true);
                objD.setVisible(true);
                objD.setLocationRelativeTo(null);
                if (objD.getReturnStatus() == VisualizationDialog.RET_OK) {
                    ngVisual = new NeuGenVisualization(this, (BranchGroup) s.getSceneGroup().cloneTree(), 10, objD.getVisualMethod(), false);
                } else {
                    NeuGenView.getInstance().enableButtons();
                }
                break;
            }
            case LOADED_GRAPH: {
                ngVisual = new NeuGenVisualization(this, (BranchGroup) s.getSceneGroup(), 1, null, true);
                break;
            }
            case NET: {
                VisualizationDialog visDialog = new VisualizationDialog(NeuGenView.getInstance().getFrame(), true);
                visDialog.setVisible(true);
                visDialog.setLocationRelativeTo(null);
                if (visDialog.getReturnStatus() == VisualizationDialog.RET_OK) {
                    ngVisual = new NeuGenVisualization(this, visDialog.getSliderValue(), visDialog.getVisualMethod());
                    break;
                } else {
                    NeuGenView.getInstance().enableButtons();
                }
            }
        }
        return null;  // return your result
    }

    @Override
    protected void succeeded(Void result) {
        NeuGenView ngView = NeuGenView.getInstance();
        ngView.enableButtons();
        ngView.enableVisualize();
        setMessage("End-visualize");
        ngView.outPrintln("End-visualize\n\n");
        System.gc();
        ngView.outPrintln(Utils.getMemoryStatus());
    }
}
