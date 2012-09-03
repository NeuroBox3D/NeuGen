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
