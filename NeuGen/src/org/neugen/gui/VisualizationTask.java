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
