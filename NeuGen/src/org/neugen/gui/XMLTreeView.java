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
