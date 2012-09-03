package org.neugen.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.neugen.gui.NeuGenConstants;
import org.neugen.gui.NeuGenView;
import org.neugen.utils.Utils;
import org.neugen.visual.NeuGenDensityVisualization;

/**
 *
 * @author Sergei
 */
public class NeuGenVisualWriterTask extends Task<Void, Void> {

    private final static Logger logger = Logger.getLogger(NeuGenWriterTask.class.getName());
    public NeuGenView ngView;
    private File file;

    public NeuGenVisualWriterTask(Application app, File f) {
        super(app);
        ngView = NeuGenView.getInstance();
        this.file = f;
    }

    public void setMyProgress(int value, int min, int max) {
        setProgress(value, min, max);
    }

    @Override
    protected Void doInBackground() throws FileNotFoundException, IOException {
        ngView.outPrintln("writing visual data to " + file.getAbsolutePath());
        ngView.disableButtons();

        String bg = NeuGenConstants.EXTENSION_BG;
        String extension = Utils.getExtension(file);
        if (!bg.equals(extension)) {
            file = new File(file.getAbsolutePath() + "." + bg);
        }

        NeuGenDensityVisualization dv = NeuGenDensityVisualization.getInstance();
        dv.write(file);
        return null;
    }

    @Override
    protected void succeeded(Void result) {
        ngView.enableButtons();
        System.gc();
        ngView.outPrintln(Utils.getMemoryStatus());
    }
}
