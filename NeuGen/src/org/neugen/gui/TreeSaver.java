package org.neugen.gui;

import org.neugen.datastructures.xml.XMLNode;

/**
 * @author Alexander Wanner
 */
public interface TreeSaver {

    public void save(XMLNode root) throws Exception;
}
