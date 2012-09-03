package org.neugen.datastructures.xml;

/**
 * @author Alexander Wanner
 */
public interface XMLNodeInterface {

    public abstract String getKey();

    public abstract XMLObjectInterface getParent();

    public abstract String isA();

    public abstract boolean isInherited();

    public abstract void setInherited();

    public abstract void setUnInherited();
}
