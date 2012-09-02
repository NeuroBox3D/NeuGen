package org.neugen.datastructures.xml;

import java.util.Map;
import java.util.Set;

/**
 * @author Alexander Wanner
 */
public interface XMLObjectInterface extends XMLNodeInterface {

    public abstract int getChildrenCount();

    public abstract XMLNodeInterface getChild(int index);

    public abstract XMLNodeInterface getChild(String name);

    public abstract String getObjectClassName();

    public abstract boolean hasUninheritedChildren();

    public abstract void addChild(XMLNodeInterface child);

    public abstract void removeChild(XMLNodeInterface child);

    //Wrapper Interface
    public abstract void addDepentants(int order, XMLObjectInterface depNode);

    public abstract Set<XMLObjectInterface> getDependants(int order);

    public abstract Map<Integer, Set<XMLObjectInterface>> getAllDependants();

    public abstract XMLObjectInterface getSupreme(int order);

    public abstract Map<Integer, XMLObjectInterface> getAllSupreme();
}
