package org.neugen.parsers;

import java.io.File;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.neugen.datastructures.Net;
import org.neugen.gui.NeuGenView;

/**
 *
 * @author Sergei Wolf
 */
public class CSVWriterTask extends Task<Void, Void> {

    private final File file;

    public CSVWriterTask(Application app, File f) {
        super(app);
        file = f;
    }

    @Override
    protected Void doInBackground() {
        Net net = NeuGenView.getInstance().getNet();
        CSVWriter csvW = new CSVWriter(net, file);
        //csvW.writeAverageNeuronInfo();
        //setMessage("Write average neuron info to... " + file.getName());
        csvW.writeNeuronInfo();
        setMessage("Write neuron info to... " + file.getName());
        return null;
    }
}
