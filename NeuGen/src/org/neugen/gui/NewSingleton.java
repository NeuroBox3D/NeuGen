package org.neugen.gui;

/**
 *
 * @author Sergei
 */
public class NewSingleton {

    private NewSingleton() {
    }

    public static NewSingleton getInstance() {
        return NewSingletonHolder.INSTANCE;
    }

    private static class NewSingletonHolder {
        private static final NewSingleton INSTANCE = new NewSingleton();
    }
 }
