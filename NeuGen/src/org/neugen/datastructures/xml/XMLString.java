package org.neugen.datastructures.xml;

import org.neugen.datastructures.DataStructureConstants;

/**
 * @author Alexander Wanner
 */
public class XMLString extends XMLNode {

    private static final long serialVersionUID = 44645634634L;

    public XMLString(String name, XMLObject parent,
            boolean inheritanceFlag, String value) {
        super(name, parent, inheritanceFlag);
        //this.value = value;
        super.setValue(value);
    }

    @Override
    public String isA() {
        return DataStructureConstants.XML_STRING_TYPE;
    }

    public String value() {
        return (String) super.value;
    }

    public void setValue(String value) {
        super.value = value;
    }

    public static XMLString convert(XMLNode node) {
        XMLString stringChild = (node instanceof XMLString ? (XMLString) node : null);
        return stringChild;
    }
}
