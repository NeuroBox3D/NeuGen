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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import org.apache.log4j.Logger;
import org.neugen.datastructures.xml.XMLNode;

/**
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public final class XMLTreeView extends JTree {

    /** use to log messages */
    private final static Logger logger = Logger.getLogger(XMLTreeView.class.getName());
    private static final long serialVersionUID = 325325235L;
    private ConfigTreeMouseListener pl;
    private boolean contentChanged;
    private TreeSaver saver;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public XMLTreeView(TreeSaver saver) {
        super();
        this.saver = saver;
        pl = new ConfigTreeMouseListener();
        this.addMouseListener(pl);
        changes.firePropertyChange("value", false, false);
    }

    public XMLTreeView(TreeNodeInterface root) {
        super(root);
        pl = new ConfigTreeMouseListener();
        this.addMouseListener(pl);
        changes.firePropertyChange("value", false, false);
    }

    public XMLTreeView(XMLNode root) {
        super(root);
        pl = new ConfigTreeMouseListener();
        this.addMouseListener(pl);
        changes.firePropertyChange("value", false, false);
    }

    @Override
    public String toString() {
        return this.paramString();
    }

    public void setContentChanged(boolean ch_flag) {
        logger.debug("oldValue: " + contentChanged + " , new Value: " + ch_flag);
        changes.firePropertyChange("value", contentChanged, ch_flag);
        contentChanged = ch_flag;
    }

    public boolean getContentChanged() {
        return contentChanged;
    }

    public void addPropertyChangeL(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public void removePropertyChangeL(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

    /**
     * Is a content of the three changed, so it has possibly to be saved before closing?
     * @return true exactly if content was changed so it needs a backup.
     */
    public boolean ableToClose() {
        if (contentChanged) {
            int option = JOptionPane.showConfirmDialog(null, "Do you want to save your changes?",
                    "Saving changes", JOptionPane.YES_NO_CANCEL_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    saver.save((XMLNode) this.getModel().getRoot());
                    return true;
                } catch (Exception ex) {
                    logger.error(ex, ex);
                    return false;
                }
            } else if (option == JOptionPane.CANCEL_OPTION) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
