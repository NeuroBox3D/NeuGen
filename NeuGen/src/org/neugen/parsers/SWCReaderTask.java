package org.neugen.parsers;

import java.io.File;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.neugen.gui.NeuGenView;
import org.neugen.utils.Utils;
import org.neugen.visual.NeuGenVisualization;

/**
 * @author Sergei Wolf
 */
public class SWCReaderTask extends Task<Void, Void> {

    private NeuGenView ngView;
    private File file;

    public SWCReaderTask(Application app, File file) {
        super(app);
        ngView = NeuGenView.getInstance();
        this.file = file;
    }

    @Override
    protected Void doInBackground() throws Exception {
        SWCReader readSWC = new SWCReader();
        readSWC.readSWC(file);
        readSWC.getNet().setTotalNumOfSegments();
        ngView.setNet(readSWC.getNet());
        return null;
    }

    @Override
    protected void succeeded(Void result) {
        ngView.setNetExist(true);
        ngView.enableButtons();
        System.gc();
        ngView.outPrintln(Utils.getMemoryStatus());    
        ngView.visualizeData().run();
    }
}
