package org.neugen.parsers;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.neugen.datastructures.Net;
import org.neugen.gui.NeuGenView;
import org.neugen.utils.Utils;

/**
 *
 * @author Sergei
 */
public class NeuGenWriterTask extends Task<Void, Void> {

    private final static Logger logger = Logger.getLogger(NeuGenWriterTask.class.getName());
    public NeuGenView ngView;

    public NeuGenWriterTask(Application app) {
        super(app);
        ngView = NeuGenView.getInstance();
    }

    public void setMyProgress(int value, int min, int max) {
        setProgress(value, min, max);
    }

    @Override
    protected Void doInBackground() throws FileNotFoundException, IOException {
        ngView.outPrintln("writing data to NeuGen Project..");
        ngView.disableButtons();
        Net net = ngView.getNet();
        String netSer = ngView.getProjectDirPath() + System.getProperty("file.separator") + "net.ser";
        ObjectOutputStream netOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(netSer)));
        netOut.writeObject(net);
        netOut.close();
        ngView.setNewNet(false);
        return null;
    }

    @Override
    protected void succeeded(Void result) {
        ngView.setNetExist(true);
        ngView.enableButtons();
        System.gc();
        ngView.outPrintln(Utils.getMemoryStatus());
    }
}
