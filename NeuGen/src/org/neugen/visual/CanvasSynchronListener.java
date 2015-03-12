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
package org.neugen.visual;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author Sergei
 */
public class CanvasSynchronListener {

    private SynchronBehavior synchronBehavior;
    private CanvasMouseListener mouseListener;
    private CanvasMouseWheelListener wheelListener;
    private CanvasKeyListener keyListener;

    public CanvasSynchronListener(SynchronBehavior synBeh) {
        this.synchronBehavior = synBeh;
        this.mouseListener = new CanvasMouseListener();
        this.wheelListener = new CanvasMouseWheelListener();
        this.keyListener = new CanvasKeyListener();
    }

    public CanvasMouseListener getMouseListener() {
        return mouseListener;
    }

    public CanvasMouseWheelListener getMouseWheelListener() {
        return wheelListener;
    }

    public CanvasKeyListener getKeyListener() {
        return keyListener;
    }

    public class CanvasKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            synchronBehavior.move();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            synchronBehavior.move();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            synchronBehavior.move();
        }

    }

    public class CanvasMouseWheelListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            synchronBehavior.move();
        }

    }

    public class CanvasMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //System.out.println("mouse pressed");
            synchronBehavior.move();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            synchronBehavior.move();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            synchronBehavior.move();
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
