/*
 * File: ConfigTask.java
 * Created on 20.07.2009, 16:03:45
 *
 */
package org.neugen.gui;

import org.apache.log4j.Logger;
import org.jdesktop.application.Application;

import org.jdesktop.application.Task;
import org.neugen.utils.Utils;

/**
 * @author Sergei Wolf
 */
public final class NeuGenLibTask extends Task<Void, Void> {

    private static final Logger logger = Logger.getLogger(NeuGenLibTask.class.getName());
    private NeuGenLib ngLib;
    private static NeuGenLibTask instance;
    private String projectType;

    public NeuGenLibTask(Application application, String projectType) {
        super(application);
        this.projectType = projectType;
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public NeuGenLib getNGLib() {
        return ngLib;
    }

    public static NeuGenLibTask getInstance() {
        return instance;
    }

    public static void setInstance(NeuGenLibTask instance) {
        NeuGenLibTask.instance = instance;
    }

    public void setMyProgress(int value, int min, int max) {
        setProgress(value, min, max);
    }

    public void setMyProgress(float percentage) {
        if (Float.isInfinite(percentage)) {
            logger.info("percentage infinite: " + percentage);
            percentage = 1.0f;
        }
        setProgress(percentage);
    }

    @Override
    protected Void doInBackground() {
        NeuGenView.getInstance().outPrintln(Utils.getMemoryStatus());
        setMessage("generate neuronal net...");
        ngLib = new NeuGenLib();
        // netz uebergeben!
        ngLib.run(projectType);
        return null;
    }

    @Override
    protected void succeeded(Void result) {
        NeuGenView ngView = NeuGenView.getInstance();
        ngView.setNetExist(true);
        ngView.setNewNet(true);
        ngView.enableButtons();
        System.gc();
        ngView.outPrintln(Utils.getMemoryStatus());
    }
}
