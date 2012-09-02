package org.neugen.gui;

import org.apache.log4j.Logger;
import org.jdesktop.application.Application;

/**
 * @author Sergei
 */
public final class Trigger extends org.jdesktop.application.Task<Void, Void> {

    private NeuGenView ngView;
    private static final Logger logger = Logger.getLogger(Trigger.class.getName());
    private static Trigger instance;
    private String message = "";

    public Trigger(Application application) {
        super(application);
        ngView = NeuGenView.getInstance();
    }
    
    public void setTextMessage(String mes) {
        message = mes;
    }

    public static Trigger getInstance() {
        if (instance == null) {
            instance = new Trigger(NeuGenApp.getApplication());
        } 
        return instance;
    }

    public void outPrint(String message) {
        if (ngView != null) {
            ngView.outPrint(message);
        } else {
            logger.info(message);
        }
    }

    public void outPrintln(String message) {
        if (ngView != null) {
            ngView.outPrintln(message);
        } else {
            logger.info(message);
        }
    }

    public void outPrintln() {
        if (ngView != null) {
            ngView.outPrintln();
        }
    }


 

    @Override
    protected Void doInBackground() throws InterruptedException  {
        /*for (int i = 0; i < 10; i++) {
        setMessage("Working... [" + i + "]");
        Thread.sleep(150L);
        setProgress(i, 0, 9);
        }*/
        /*
        System.out.println("sleep");
        Thread.sleep(150L);
        
        System.out.println("after sleep");
        
         */
        setMessage(message);
        return null;
    }

}
