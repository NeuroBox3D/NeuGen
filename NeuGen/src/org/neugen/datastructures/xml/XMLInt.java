package org.neugen.datastructures.xml;

import org.neugen.datastructures.DataStructureConstants;

/**
 * @author Alexander Wanner
 */
public class XMLInt extends XMLNode {

    private static final long serialVersionUID = 189689689L;

    public XMLInt(String name, XMLObject parent, boolean inheritanceFlag, int value) {
        super(name, parent, inheritanceFlag);
        super.setValue(new Integer(value));
    }

    public int value() {
        return (new Integer(super.value.toString())).intValue();
    }

    @Override
    public String isA() {
        return DataStructureConstants.XML_INT_TYPE;
    }

    public void setValue(int value) {
        super.value = new Integer(value);
    }

    public static XMLInt convert(XMLNode node) {
        XMLInt intChild = (node instanceof XMLInt ? (XMLInt) node : null);
        return intChild;
    }
}
