package org.neugen.gui;

/**
 * @author Alexander Wanner
 */
public interface MenuHandlerFactory {

    public MenuHandler getHandler(Object param) throws Exception;
}
