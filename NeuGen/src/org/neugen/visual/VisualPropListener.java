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
package org.neugen.visual;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.media.j3d.Canvas3D;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import org.apache.log4j.Logger;

public final class VisualPropListener implements PropertyChangeListener {

    /** use to log message */
    private final static Logger logger = Logger.getLogger(VisualPropListener.class.getName());
    private Canvas3D canvas3D;
    private String key;
    private JScrollPane C3DScrollPane;
    private JSplitPane C3DSplitPane;

    public VisualPropListener(Canvas3D canvas3D, JScrollPane C3DScrollPane, JSplitPane C3DSplitPane, String key) {
        this.canvas3D = canvas3D;
        this.C3DScrollPane = C3DScrollPane;
        this.C3DSplitPane = C3DSplitPane;
        this.key = key;
        removePropertyCL();
    }

    public String getKey() {
        return key;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        Dimension scrollPaneDim = new Dimension(), canvas3D_DIM;
        scrollPaneDim = C3DScrollPane.getSize();

        if ((scrollPaneDim.width > 0) && (scrollPaneDim.height > 0)) {
            if (canvas3D != null) {
                canvas3D_DIM = canvas3D.getSize();
                scrollPaneDim.width -= (C3DScrollPane.getInsets().right + C3DScrollPane.getInsets().left);
                scrollPaneDim.height -= (C3DScrollPane.getInsets().top + C3DScrollPane.getInsets().bottom);
                if ((canvas3D_DIM.width != scrollPaneDim.width) || (canvas3D_DIM.height != scrollPaneDim.height)) {
                    C3DSplitPane.validate();
                    C3DSplitPane.repaint();
                    canvas3D.setSize(scrollPaneDim);
                    /*logger.info("***************** begin**************");
                    logger.info("C3DScrollPane.width: " + C3DScrollPane.getSize().width);
                    logger.info("C3DScrollPane.width: " + C3DScrollPane.getSize().height);*/
                }
            }
        }
    }

    private void removePropertyCL() {
        if (C3DSplitPane != null) {
            for (PropertyChangeListener proCL : C3DSplitPane.getPropertyChangeListeners()) {
                //logger.info("class name: " + proCL.getClass().toString());
                //logger.info("class ng listener: " + NeuGenVisualization.VisualPropListener.class.toString());
                if (proCL.getClass().toString().equals(VisualPropListener.class.toString())) {
                    if (key.equals(((VisualPropListener) proCL).getKey())) {
                        C3DSplitPane.removePropertyChangeListener(proCL);
                        logger.info("remove old listener!");
                    }
                }
            }
        }
    }
}
