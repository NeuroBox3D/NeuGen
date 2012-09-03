
package org.neugen.parsers;

import java.io.File;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.neugen.datastructures.Net;
import org.neugen.gui.NeuGenView;

/**
 *
 * @author Sergei
 */
public final class NeuroMLWriterTask extends Task<Void, Void> {

    private final File file;
    protected static NeuroMLWriterTask instance;
    private static final Logger logger = Logger.getLogger(NeuroMLWriterTask.class.getName());

    public NeuroMLWriterTask(Application app, File f) {
        super(app);
        file = f;
        setInstance(this);
    }

    /**
     * Get the value of instance
     *
     * @return the value of instance
     */
    public static NeuroMLWriterTask getInstance() {
        return instance;
    }

    /**
     * Set the value of instance
     *
     * @param instance new value of instance
     */
    public static void setInstance(NeuroMLWriterTask instance) {
        NeuroMLWriterTask.instance = instance;
    }


    @Override
    protected Void doInBackground() throws Exception {
        NeuGenView.getInstance().disableButtons();
        Net net = NeuGenView.getInstance().getNet();
        setMessage("Exporting... " + file.getName());
        NeuroMLWriter write = new NeuroMLWriter();
        write.exportData(file, net);
        return null;
    }

    public void setMyProgress(float percentage) {
        if (Float.isInfinite(percentage)) {
            logger.info("percentage infinite: " + percentage);
            percentage = 1.0f;
        }
        setProgress(percentage);
    }

    @Override
    protected void succeeded(Void result) {
        NeuGenView.getInstance().enableButtons();
        // Runs on the EDT.  Update the GUI based on
        // the result computed by doInBackground().
    }


}
