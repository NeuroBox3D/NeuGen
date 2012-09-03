package org.neugen.datastructures.xml;

import org.neugen.datastructures.DataStructureConstants;

/**
 * @author Alexander Wanner
 */
public class XMLBool extends XMLNode {

    private static final long serialVersionUID = 66745634634L;

    public XMLBool(String name, XMLObject parent,
            boolean inheritanceFlag, boolean value) {
        super(name, parent, inheritanceFlag);
        super.setValue(value);
    }

    public boolean value() {
        return (Boolean.valueOf(super.value.toString())).booleanValue();
    }

    @Override
    public String isA() {
        return DataStructureConstants.XML_BOOL_TYPE;
    }

    public void setValue(boolean value) {
        super.value = value;
    }

    public static XMLBool convert(XMLNode node) {
        XMLBool boolChild = (node instanceof XMLBool ? (XMLBool) node : null);
        return boolChild;
    }
}
