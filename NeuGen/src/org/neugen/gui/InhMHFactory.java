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

import org.neugen.datastructures.xml.XMLNode;
import org.neugen.datastructures.xml.XMLObject;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import org.neugen.datastructures.DataStructureConstants;
import org.neugen.parsers.SimplifiedInheritance;

/**
 * @author Alexander Wanner
 */

class ComposedMenuHandler implements MenuHandler {

    protected MenuHandler[] menus;
    protected int mnumber;

    public ComposedMenuHandler(MenuHandler[] menus) {
        this.menus = menus;
        mnumber = 0;
        for (int i = 0; i < menus.length; ++i) {
            mnumber += menus[i].getMenu().length;
        }
    }

    @Override
    public Object[] getMenu() {
        Object[] menu = new Object[mnumber];
        int counter = 0;
        for (int i = 0; i < menus.length; ++i) {
            Object[] cur = menus[i].getMenu();
            for (int j = 0; j < cur.length; j++) {
                menu[counter] = cur[j];
                counter++;
            }
        }
        return menu;
    }

    @Override
    public void doIt(Object order) throws Exception {
        for (int i = 0; i < menus.length; ++i) {
            menus[i].doIt(order);
        }
    }
}

class DummyHandler implements MenuHandler {

    @Override
    public Object[] getMenu() {
        return new Object[0];
    }

    @Override
    public void doIt(Object order) throws Exception {
    }
}

class EditMH implements MenuHandler {

    protected static String[] menu = {"Edit"};
    protected XMLNode node;
    protected XMLObject root;
    protected XMLTreeView treeView;
    protected MenuHandlerFactory mhFactory;

    public EditMH(XMLObject root, XMLNode node, XMLTreeView treeView, MenuHandlerFactory mhFactory) {
        this.node = node;
        this.mhFactory = mhFactory;
        this.node.setHandler(this);
        this.treeView = treeView;
        this.root = root;
    }

    @Override
    public Object[] getMenu() {
        return menu;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doIt(Object order) throws Exception {
        if (!((String) order).equals(menu[0])) {
            return;
        } else if (((String) order).equals(menu[0])) {
            boolean complete = false;
            String inputValue = "";
            while (!complete) {
                try {
                    inputValue = JOptionPane.showInputDialog("Please input a value for " + node.getKey(), node.getValue());
                    if (inputValue == null) {
                        return;
                    }
                    Class vClass = node.getValue().getClass();
                    //System.out.println("class: " + vClass.toString());
                    //Class vPoss[] = {Boolean.class, Long.class, Double.class, String.class};
                    Class aClasses[] = {String.class};
                    Object args[] = {inputValue};
                    if (vClass.equals(String.class)) {
                        //System.out.println("vClass is a string.class");
                        node.setValue(inputValue);
                    } else {
                        Object val = vClass.getMethod("valueOf", aClasses).invoke(node.getValue(), args);
                        //System.out.println("val: " + val.toString());
                        node.setValue(val);
                        //System.out.println("node Value: " + node.getValue());
                    }
                    complete = true;
                    treeView.setContentChanged(true);
                    // NeuGenView.getSaveButton().setEnabled(true);
                    ((DefaultTreeModel) treeView.getModel()).nodeChanged(node);
                    //treeView.validate();
                    //treeView.repaint();
                   
                } catch (InvocationTargetException e) {
                    JOptionPane.showMessageDialog(null, "Can't invoke input conversion! "
                            + "Wonder if it is a correct input : " + inputValue.toString(),
                            "Input error", JOptionPane.ERROR_MESSAGE);
                }
            }

            DefaultTreeModel model = (DefaultTreeModel) treeView.getModel();
            SimplifiedInheritance simpleInh = new SimplifiedInheritance();
            node.setUnInherited();
            model = (DefaultTreeModel) treeView.getModel();
            simpleInh.setTreeModel(model);
            simpleInh.updateAllInheritedValues(node);
        }
    }
}

class InheritanceMH implements MenuHandler {

    protected static String[] inhmenu = {"set inherited"};
    protected static String[] uninhmenu = {"set uninherited"};
    protected static int ihnherited = 0;
    protected static int uninherited = 1;
    protected XMLTreeView treeView;
    protected XMLObject root;
    protected XMLNode node;
    protected MenuHandlerFactory mhFactory;

    public InheritanceMH(XMLObject root, XMLNode node, XMLTreeView treeView, MenuHandlerFactory mhFactory) {
        this.node = node;
        this.mhFactory = mhFactory;
        this.root = root;
        this.node.setHandler(this);
        this.treeView = treeView;
    }

    @Override
    public Object[] getMenu() {
        if (!node.isInherited()) {
            return inhmenu;
        } else {
            return uninhmenu;
        }
    }

    @Override
    public void doIt(Object order) throws Exception {
        String o = (String) order;
        DefaultTreeModel model = (DefaultTreeModel) treeView.getModel();
        SimplifiedInheritance simpleInh = new SimplifiedInheritance();
        //inherit
        if (o.equals(inhmenu[0])) {
            //System.out.println("set inherited");
            node.setInherited();
            model = (DefaultTreeModel) treeView.getModel();
            simpleInh.setTreeModel(model);
            simpleInh.updateAllInheritedValues(node);
        } //uninherited
        else if (o.equals(uninhmenu[0])) {
            //System.out.println("set uninherited");
            node.setUnInherited();
            model = (DefaultTreeModel) treeView.getModel();
            simpleInh.setTreeModel(model);
            simpleInh.updateAllInheritedValues(node);
        } else {
            return;
        }
        treeView.setContentChanged(true);
        //NeuGenView.getSaveButton().setEnabled(true);
    }
}

class SimpleObjectMH implements MenuHandler {

    protected static String[] menu = {/*"info"*/};
    protected XMLNode tnode;

    public SimpleObjectMH(XMLNode node) {
        tnode = node;
        tnode.setHandler(this);
        //System.out.println("Simple für: " + node.getPathLocal());
    }

    @Override
    public Object[] getMenu() {
        return menu;
    }

    @Override
    public void doIt(Object order) throws Exception {
        //JOptionPane.showMessageDialog(null, "" + tnode.getChildCount() + "linked");
    }
}

public class InhMHFactory implements MenuHandlerFactory {

    protected XMLTreeView treeView;
    protected XMLObject root;

    public InhMHFactory(XMLTreeView tv, XMLObject root) {
        this.treeView = tv;
        this.root = root;
    }

    @Override
    public MenuHandler getHandler(Object param) throws Exception {
        //System.out.println("inhMhFactory getHandler");
        XMLNode node = ((XMLNode) param);
        //System.out.println("getHadler für: " + node.getPathLocal());
        if (node.getValue() == null) {
            //System.out.println("Simple für: " + node.getPathLocal());
            return new SimpleObjectMH(node);
        }
        if (hasHeritage(node) == null) {
            //System.out.println("EditMH für: " + node.getPathLocal());
            return new EditMH(root, node, treeView, this);
        }
        //System.out.println("Edit + InheritMH für: " + node.getPathLocal());
        MenuHandler mh[];
        MenuHandler imh[] = {
            //XMLTreeView, MenuHandlerFactory, DefaultXMLNode, root, liste
            new EditMH(root, node, treeView, this),
            new InheritanceMH(root, node, treeView, this)
        };
        mh = imh;
        ComposedMenuHandler cmh = new ComposedMenuHandler(mh);
        return cmh;
    }

    public static XMLNode hasHeritage(XMLNode node) {
        List<XMLNode> path = new ArrayList<XMLNode>();
        path.add(node);
        XMLNode parentNode = node.getParent();
        XMLObject parent = null;
        if (parentNode != null) {
            parent = XMLObject.convert(parentNode);
        }

        boolean found = false;
        while (parent != null) {
            if ((parent.getValue() == null) && (parent.getObjectClassName().equals(DataStructureConstants.SIBLINGS_TYPE))) {
                path.remove(path.size() - 1);
                path.add((XMLNode) parent.getParent());
                parent = null;
                found = true;
            } else {
                if (parent.getObjectClassName().equals(DataStructureConstants.ALIAS_TYPE)) {
                    path.remove(path.size() - 1);
                }
                path.add(parent);
                parent = parent.getParent();
            }
        }

        if (!found) {
            return null;
        }
        XMLNode child = null;
        int i;
        parent = XMLObject.convert(path.get(path.size() - 1));

        path_iteration:
        for (i = path.size() - 1; i > 0; i--) {
            XMLNode aliasNode = null;
            do {
                Enumeration e = parent.children();
                while (e.hasMoreElements()) {
                    child = (XMLNode) e.nextElement();
                    XMLObject childObj = XMLObject.convert(child);
                    if (childObj != null) {
                        if (childObj.getObjectClassName().equals(DataStructureConstants.SIBLINGS_TYPE)) {
                            aliasNode = child;
                            continue path_iteration;
                        }
                    }
                    if (child.getKey().equals((path.get(i - 1)).getKey())) {
                        parent = childObj;
                        continue path_iteration;
                    }
                }
            } while (aliasNode != null);
            return null;
        }
        return child;
    }
}

