package org.neugen.parsers;

import org.neugen.utils.Utils;
import org.neugen.gui.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.neuron.Neuron;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * @author Sergei Wolf
 */
public final class NeuroMLReaderTask extends Task<Void, Void> {

    private final static Logger logger = Logger.getLogger(NeuroMLReaderTask.class.getName());
    private final File file;
    public int fileLength = 0;
    public NeuGenView ngView;

    public NeuroMLReaderTask(Application app, File file) {
        super(app);
        this.file = file;
        fileLength = (int) file.length();
        ngView = NeuGenView.getInstance();
    }

    public void setMyProgress(int value, int min, int max) {
        setProgress(value, min, max);
    }

    @Override
    protected Void doInBackground() throws IOException {
        try {
            FileInputStream instream = null;
            InputSource is = null;
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            XMLReader xmlReader = spf.newSAXParser().getXMLReader();
            MorphMLReader mmlBuilder = new MorphMLReader(this);
            xmlReader.setContentHandler(mmlBuilder);
            instream = new FileInputStream(file);
            is = new InputSource(instream);
            ngView.outPrintln("reading neuroML file: " + file.getPath() + "\n");
            xmlReader.parse(is);
            Net net = mmlBuilder.getNet();
            for (Neuron neuron : net.getNeuronList()) {
                neuron.infoNeuron();
            }
            net.setTotalNumOfSegments();
            ngView.setNet(net);
            /*
            ngView.outPrintln("Total number of segments(net): " + net.getTotalNumberOfSegments() + "\n");
            ngView.outPrintln("Total number of segments(reader): " + mmlBuilder.getTotalNumSegs());
            ngView.outPrintln("Total number of sections(reader): " + mmlBuilder.getTotalNumSecs());*/
        } catch (Exception e) {
            logger.error(e, e);
        }
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
