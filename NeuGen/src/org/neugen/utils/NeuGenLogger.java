package org.neugen.utils;

import java.io.File;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author Sergei Wolf
 */
public final class NeuGenLogger {

    private static boolean loaded;

    public static void initLogger() {
        if (!loaded) {
            init(new File("log4j-3.properties"));
        }
    }
    
    public static void initLogger(File f) {
        if (!loaded) {
            init(f);
        }
    }

    private static void init(File f) {
        try {
            PropertyConfigurator.configureAndWatch(f.getAbsolutePath(), 60 * 1000);
            loaded = true;
        } catch (Exception ex) {
            ex.getCause();
        }
    }
}
