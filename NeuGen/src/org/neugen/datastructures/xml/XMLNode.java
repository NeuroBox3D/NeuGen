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
package org.neugen.datastructures.xml;

import java.util.LinkedList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import org.apache.log4j.Logger;
import org.neugen.datastructures.DataStructureConstants;
import org.neugen.gui.MenuHandler;

/**
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public class XMLNode extends DefaultMutableTreeNode
        implements XMLNodeInterface, TreeNode {

    /** use to log messages */
    private final static Logger logger = Logger.getLogger(XMLNode.class.getName());
    private static final long serialVersionUID = 10990790877L;
    protected boolean inherited = false;
    protected Object appendix;
    protected MenuHandler handler;
    protected XMLObject parent;
    protected Object value;
    protected String key;

    public XMLNode(String name, XMLObject parent, boolean inheritanceFlag) {
        this.key = name;
        this.inherited = inheritanceFlag;
        if (parent != null) {
            parent.add(this);
            parent.addChild(this);
        }
        this.parent = parent;

    }

    /**
     * Get the value of handler
     *
     * @return the value of handler
     */
    public MenuHandler getHandler() {
        return handler;
    }

    /**
     * Set the value of handler
     *
     * @param handler new value of handler
     */
    public void setHandler(MenuHandler handler) {
        this.handler = handler;
    }

    /**
     * Get the value of key
     *
     * @return the value of key
     */
    @Override
    public String getKey() {
        return key;
    }

    /**
     * Set the value of key
     *
     * @param key new value of key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Get the value of parent
     *
     * @return the value of parent
     */
    @Override
    public XMLObject getParent() {
        return parent;
    }

    /**
     * Set the value of parent
     *
     * @param parent new value of parent
     */
    public void setParent(XMLObject parent) {
        this.parent = parent;
    }

    /**
     * Get the value of appendix
     *
     * @return the value of appendix
     */
    public Object getAppendix() {
        return appendix;
    }

    /**
     * Set the value of appendix
     *
     * @param appendix new value of appendix
     */
    public void setAppendix(Object appendix) {
        this.appendix = appendix;
    }

    @Override
    public String toString() {
        // DefaultTreeCellRenderer
        String ret = "<html>";
        ret += key;
        if (!this.inherited) {
            ret += "<r><font color=#ff>";
        }
        if (value != null) {
            ret += ": " + value.toString();
        }
        if (!this.inherited) {
            ret += "</font></r>";
        }

        ret += "</hmtl>";

        return ret;
    }

    /**
     * Get the value of value
     *
     * @return the value of value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set the value of value
     *
     * @param value new value of value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String isA() {
        return DataStructureConstants.XML_NODE_TYPE;
    }

    @Override
    public boolean isInherited() {
        return inherited;
    }

    @Override
    public void setInherited() {
        inherited = true;
    }

    @Override
    public void setUnInherited() {
        inherited = false;
    }

    public String getPathLocal() {
        XMLNode node = this;
        LinkedList<String> queue = new LinkedList<String>();
        while (node != null) {
            queue.addFirst(node.getKey());
            node = node.getParent();
        }
        String path = "";
        while (queue.size() > 0) {
            String tmp = queue.getFirst();
            queue.removeFirst();
            path = path + "/" + tmp;
        }
        return path;
    }

    public String getPathForProperties() {
        XMLNode node = this;
        LinkedList<String> queue = new LinkedList<String>();
        while (node != null) {
            queue.addFirst(node.getKey());
            node = node.getParent();
        }
        String firstNodeKey = queue.getFirst();
        if(queue.size() == 1) {
            return firstNodeKey;
        }
        String path = null;
        queue.removeFirst(); // first node is project node
        while (queue.size() > 0) {
            String tmp = queue.getFirst();
            queue.removeFirst();
            if(path == null) {
                path = tmp;
            } else {
                path += "." + tmp;
            }
        }
        return path;
    }

    public Object[] getMenu() {
        return this.handler.getMenu();
    }

    public void doIt(Object order) throws Exception {
        handler.doIt(order);
    }
}
