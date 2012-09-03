package org.neugen.parsers;

import org.neugen.utils.Utils;
import java.io.BufferedInputStream;
import org.neugen.gui.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.Region;
import org.neugen.datastructures.neuron.Neuron;

/**
 * @author Sergei Wolf
 */
public final class NeuGenReaderTask extends Task<Void, Void> {

    private final static Logger logger = Logger.getLogger(NeuroMLReaderTask.class.getName());

    public NeuGenView ngView;

    public NeuGenReaderTask(Application app) {
        super(app);
        ngView = NeuGenView.getInstance();
    }

    public void setMyProgress(int value, int min, int max) {
        setProgress(value, min, max);
    }

    @Override
    protected Void doInBackground() throws IOException {
        try {
            ngView.outPrintln("Reading NeuGen Project data..");
            String netSer = ngView.getProjectDirPath() + System.getProperty("file.separator") + "net.ser";
            ObjectInputStream netIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream(netSer)));
            try {
                Net net = (Net) netIn.readObject();
                if(net != null) {
                    Region.setInstance(net.getRegion());
                    for (Neuron neuron : net.getNeuronList()) {
                        neuron.infoNeuron();
                    }
                    //net.setTotalNumberOfSegments();
                    ngView.setNet(net);
                    String synMes = "\n";
                    synMes += " number of synapses: " + net.getNumSynapse() + "\n";
                    long nbilSyn = net.getNumSynapse() - net.getNumNonFunSynapses();
                    synMes += " number of bilateral synapses: " + nbilSyn + "\n";
                    synMes += " nonfunctional synapses: " + net.getNumNonFunSynapses() + "\n";
                    ngView.outPrintln(synMes);
                }
            } catch (ClassNotFoundException ex) {
                logger.error(ex, ex);
            }
            netIn.close();
        } catch (IOException ex) {
            logger.error(ex, ex);
        }
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
