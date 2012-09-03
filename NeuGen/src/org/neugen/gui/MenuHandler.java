package org.neugen.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import org.apache.log4j.Logger;

/**
 * This is an interface for all classes which are a menu source.
 * @author Alexander Wanner
 */
public interface MenuHandler {

    public Object[] getMenu();

    public void doIt(Object order) throws Exception;

    public class MenuHandlerAction extends AbstractAction {

        private static final long serialVersionUID = 44645645734L;

        private static final Logger logger = Logger.getLogger(MenuHandlerAction.class.getName());
        protected MenuHandler handler;
        protected Object order;

        public MenuHandlerAction(MenuHandler handler, Object order) {
            super(order.toString());
            this.handler = handler;
            this.order = order;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                handler.doIt(order);
            } catch (Exception ex) {
                logger.error("actionPerformed: " + ex + ' ' + order, ex);
            }
        }
    }
}
