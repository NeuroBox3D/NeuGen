package org.neugen.utils;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author Sergei Wolf
 */
public final class NeuGenLogger {

    private static boolean loaded;

    public static void initLogger() {
        if (!loaded) {
            init();
        }
    }

    private static void init() {
        try {
            PropertyConfigurator.configureAndWatch("log4j-3.properties", 60 * 1000);
            loaded = true;
        } catch (Exception ex) {
            ex.getCause();
        }
    }
}
