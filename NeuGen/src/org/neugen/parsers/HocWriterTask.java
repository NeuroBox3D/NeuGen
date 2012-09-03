package org.neugen.parsers;

import org.neugen.gui.*;
import java.io.File;
import org.apache.log4j.Logger;
import org.jdesktop.application.Task;
import org.jdesktop.application.Application;
import org.neugen.datastructures.Net;

/**
 * @author Sergei Wolf
 */
public final class HocWriterTask extends Task<Void, Void> {

    /** use to log messages */
    private static Logger logger = Logger.getLogger(HocWriterTask.class.getName());
    private final File file;

    public HocWriterTask(Application app, File f) {
        super(app);
        file = f;
    }

    public void setMyProgress(int value, int min, int max) {
        setProgress(value, min, max);
    }

    @Override
    protected Void doInBackground() {
        Net net = NeuGenView.getInstance().getNet();
        HocWriter hocWriter = new HocWriter(net, file);

        HocDialog hocConfigDialog = new HocDialog(NeuGenView.getInstance().getFrame(), true);
        hocConfigDialog.setVisible(true);
        String eventPostfix = hocConfigDialog.getEventsTextField().getText();
        String voltagesPostfix = hocConfigDialog.getVoltagesTextField().getText();
        hocWriter.setNetConEventsFilePostfix(eventPostfix);
        hocWriter.setVoltageFilePostfix(voltagesPostfix);
        setMessage("Exporting hoc data to... " + file.getName());
        hocWriter.exportNet();
        return null;  // return your result
    }

    @Override
    protected void succeeded(Void result) {
    }
}
