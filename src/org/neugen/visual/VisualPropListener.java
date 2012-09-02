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
