package org.neugen.parsers;

import java.io.File;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.neugen.datastructures.NetBase;
import org.neugen.gui.NeuGenView;

/**
 * @author Sergei Wolf
 */
public final class SimpleHocReaderTask extends Task<Void, Void> {

    private final File file;
    public int fileLength = 0;
    private NeuGenView ngView;

    public SimpleHocReaderTask(Application app, File file) {
        super(app);
        this.file = file;
        fileLength = (int) file.length();
        ngView = NeuGenView.getInstance();
    }

    public void setMyProgress(int value, int min, int max) {
        setProgress(value, min, max);
    }

    @Override
    protected Void doInBackground() {
        SimpleHocReader shr = new SimpleHocReader();
        shr.loadFromSHOCFile(file);
        NetBase net = shr.getNet();
        ngView.setNet(net);
        net.setTotalNumOfSegments();
        //ngView.outPrintln("total number of segments: " + net.getTotalNumberOfSegments() + "\n");
        return null;
    }
}
