package org.neugen.datastructures.xml;

import org.neugen.datastructures.DataStructureConstants;

/**
 * @author Alexander Wanner
 *
 * tstet cvs
 *
 */
public class XMLReal extends XMLNode {

    private static final long serialVersionUID = 55745634634L;

    public XMLReal(String name, XMLObject parent, boolean inheritanceFlag, double value) {
        super(name, parent, inheritanceFlag);
        super.setValue(new Float(value));
    }

    @Override
    public String isA() {
        return DataStructureConstants.XML_REAL_TYPE;
    }

    public float value() {
        //String val = super.value.toString() + "f";
        float ret = Float.parseFloat(super.value.toString());
        return ret;
    }

    public void setValue(float value) {
        super.value = value;
    }

    public static XMLReal convert(XMLNode node) {
        XMLReal realChild = (node instanceof XMLReal ? (XMLReal) node : null);
        return realChild;
    }
}
