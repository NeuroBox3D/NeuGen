package org.neugen.parsers;

import org.neugen.gui.*;
import java.io.File;
import org.jdesktop.application.Task;
import org.jdesktop.application.Application;
import org.neugen.datastructures.Net;

/**
 * @author Sergei Wolf
 */
public final class SimpleHocWriterTask extends Task<Object, Void> {

    private final File file;
    private NeuGenView nfcView;

    public SimpleHocWriterTask(Application app, File f, NeuGenView nfcView) {
        super(app);
        this.file = f;
        this.nfcView = nfcView;
    }

    public void setMyProgress(int value, int min, int max) {
        setProgress(value, min, max);
    }

    @Override
    protected Object doInBackground() {
        // Your Task's code here.  This method runs
        // on a background thread, so don't reference
        // the Swing GUI from here.
        Net net = nfcView.getNet();
        nfcView.outPrintln("writing shoc file: " + file.getPath() + "\n");
        SimpleHocWriter exportSHoc = new SimpleHocWriter();
        exportSHoc.writeNet(net, file);
        nfcView.outPrintln("end write");
        setMessage("Hallo simplefied hoc");
        return null;  // return your result
    }

    @Override
    protected void succeeded(Object result) {
        // Runs on the EDT.  Update the GUI based on
        // the result computed by doInBackground().
    }
}
