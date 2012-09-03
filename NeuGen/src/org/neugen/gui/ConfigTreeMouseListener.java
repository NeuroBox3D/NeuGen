package org.neugen.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import org.neugen.datastructures.xml.XMLNode;
import org.neugen.datastructures.xml.XMLObject;

/**
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public final class ConfigTreeMouseListener extends MouseAdapter {

    private int clicks;

    @Override
    public void mouseClicked(MouseEvent e) {
        clicks = e.getClickCount();
        if (clicks >= 2) {
            maybeShowPopup(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            maybeShowPopup(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            maybeShowPopup(e);
        }
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.getComponent() instanceof JTree) {
            JTree t = (JTree) (e.getComponent());
            int selectedRow = t.getRowForLocation(e.getX(), e.getY());
            //TreePath path = t.getClosestPathForLocation(e.getX(), e.getY());
            TreePath path = t.getPathForLocation(e.getX(), e.getY());
            JPopupMenu popup = new JPopupMenu();
            if (path != null) {
                if (selectedRow != -1) {
                    t.setSelectionPath(path);
                    XMLNode node = (XMLNode) path.getLastPathComponent();
                    if (node instanceof XMLObject) {
                        return;
                    }
                    Object[] menuo = node.getMenu();
                    JMenuItem menu[] = new JMenuItem[menuo.length];
                    for (int i = 0; i < menuo.length; ++i) {
                        menu[i] = new JMenuItem(new MenuHandler.MenuHandlerAction(node.getHandler(), menuo[i]));
                    }
                    /*if (menuo.length != 0) {
                    menu[0].setActionCommand("Edit");
                    }*/
                    for (int i = 0; i < menu.length; ++i) {
                        popup.add(menu[i]);
                        if (i < menu.length - 1) {
                            popup.addSeparator();
                        }
                    }
                    if (menu.length == 1 && clicks >= 2) {
                        clicks = 0;
                        try {
                            node.getHandler().doIt(menuo[0]);
                        } catch (Exception ex) {
                            ex.getCause();
                        }
                    } else {
                        popup.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        }
    }
}
