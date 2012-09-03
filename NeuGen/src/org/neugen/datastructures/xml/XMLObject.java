package org.neugen.datastructures.xml;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.tree.TreeNode;
import org.apache.log4j.Logger;

/**
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public class XMLObject extends XMLNode implements XMLObjectInterface, TreeNode {

    public static final class CompareObjects implements Comparator<XMLObject> {

        @Override
        public int compare(XMLObject o1, XMLObject o2) {
            XMLObject par1 = null;
            XMLObject par2 = null;
            par1 = o1;
            par2 = o2;

            while (par1 != null && par2 != null) {
                int ret = par1.getKey().compareTo(par2.getKey());
                if (ret != 0) {
                    return ret;
                }
                par1 = par1.getParent();
                par2 = par2.getParent();
            }
            if (par1 != null) {
                return 1;
            } else if (par2 != null) {
                return -1;
            } else {
                return 0;
            }
        }

        public class CompareKeys implements Comparator<XMLObject> {

            @Override
            public int compare(XMLObject o1, XMLObject o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        }
    }
    /** use to log messages */
    private final static Logger logger = Logger.getLogger(XMLObject.class.getName());
    private static final long serialVersionUID = 1L;
    private String classdescriptor;
    private List<XMLNode> childrenXML;
    private Map<Integer, Set<XMLObjectInterface>> dependants;
    private Map<Integer, XMLObjectInterface> supreme;

    public XMLObject(String name, XMLObject parent, String classdescriptor) {
        super(name, parent, false);
        childrenXML = new ArrayList<XMLNode>();
        dependants = new TreeMap<Integer, Set<XMLObjectInterface>>();
        supreme = new TreeMap<Integer, XMLObjectInterface>();
        this.classdescriptor = classdescriptor;
        super.setValue(null);
    }

    public List<XMLNode> getChildren() {
        return childrenXML;
    }

    public static XMLObject convert(XMLNode node) {
        XMLObject objectChild = (node instanceof XMLObject ? (XMLObject) node : null);
        return objectChild;
    }

    @Override
    public void removeChild(XMLNodeInterface child) {
        childrenXML.remove((XMLNode) child);
    }

    public void removeAllSupremes() {
        supreme.clear();
    }

    public void removeAllDependants() {
        dependants.clear();
    }

    @Override
    public void addChild(XMLNodeInterface child) {
        for (XMLNode brothers : childrenXML) {
            if (brothers.getKey().equals(child.getKey())) {
                logger.info("Same Object: " + ((XMLNode) child).getPathLocal());
                //System.out.println("Depend from grandparent of child");
                ((XMLObject) child.getParent().getParent()).printDependants(1);
                //System.out.println();
            }
        }
        childrenXML.add((XMLNode) child);
        //this.add((MutableTreeNode) child);
        //this.parent.add((DefaultXMLNode) child);
    }

    @Override
    public void addDepentants(int order, XMLObjectInterface depNode) {
        //System.out.println("addDependents");
        XMLObjectInterface node = this;
        ((XMLObject) depNode).addSupreme(order, node);
        //System.out.println("Vorgaenger: " + node.getName() + "von" + depNode.getName());
        if (dependants.containsKey(order)) {
            //System.out.println(dependants.get(order).size() + "TreeSet");
            dependants.get(order).add(depNode);
        } else {
            dependants.put(order, new HashSet<XMLObjectInterface>());
            dependants.get(order).add(depNode);
        }
    }

    private void addSupreme(int order, XMLObjectInterface supNode) {
        supreme.put(order, supNode);
    }

    @Override
    public Map<Integer, Set<XMLObjectInterface>> getAllDependants() {
        return dependants;
    }

    @Override
    public Map<Integer, XMLObjectInterface> getAllSupreme() {
        return supreme;
    }

    @Override
    public XMLNode getChild(int index) {
        return childrenXML.get(index);
    }

    @Override
    public XMLNode getChild(String name) {
        Iterator<XMLNode> iter = childrenXML.iterator();
        while (iter.hasNext()) {
            XMLNode child = iter.next();
            if (child.getKey().equals(name)) {
                return child;
            }
        }
        return null;
    }

    @Override
    public int getChildrenCount() {
        return childrenXML.size();
    }

    @Override
    public Set<XMLObjectInterface> getDependants(int order) {
        return dependants.get(order);
    }

    @Override
    public String getObjectClassName() {
        return classdescriptor;
    }

    @Override
    public String toString() {
        return key;
    }

    @Override
    public XMLObjectInterface getSupreme(int order) {
        return supreme.get(order);
    }

    @Override
    public boolean hasUninheritedChildren() {
        LinkedList<XMLObject> queue = new LinkedList<XMLObject>();
        queue.addFirst(this);
        while (queue.size() > 0) {
            XMLObject dObj = queue.getLast();
            queue.removeLast();
            for (int i = 0; i < dObj.getChildrenCount(); ++i) {
                XMLNode child = dObj.getChild(i);
                if (!child.isInherited()) {
                    //System.out.println("isInherited: " + child.isInherited() + ". path: " + child.getPath());
                    return true;
                } else {
                    if (child instanceof XMLObject) {
                        queue.addFirst((XMLObject) child);
                    }
                }
            }
        }
        return false;
    }

    public void printDependants(int order) {
        XMLObject node = this;
        Set<XMLObjectInterface> set = node.getDependants(order);
        if (set != null) {
            for (XMLObjectInterface current : set) {
                logger.info("order: " + order + ". dependants: " + ((XMLObject) current).getPath());
            }
        } else {
            logger.info("no dependants for order: " + order);
        }
    }

    public void printSubTree() {
        XMLObject current = this;
        LinkedList<XMLObject> processQueue = new LinkedList<XMLObject>();
        processQueue.addFirst(current);
        while (processQueue.size() > 0) {
            current = processQueue.getLast();
            processQueue.removeLast();
            logger.info("path: " + current.getPath());
            for (int i = 0; i < current.getChildrenCount(); i++) {
                XMLNode child = current.getChild(i);
                XMLObject childObj = XMLObject.convert(child);
                if (childObj != null) {
                    processQueue.addFirst(childObj);
                } else {
                    logger.info("\t " + child.toString() + " . inherited: " + child.isInherited());
                }
            }
        }
    }

    @Override
    public XMLObject getRoot() {
        XMLObject current = this;
        LinkedList<XMLObject> processQueue = new LinkedList<XMLObject>();
        processQueue.addFirst(current);
        while (processQueue.size() > 0) {
            current = processQueue.getLast();
            processQueue.removeLast();
            XMLObject parentLocal = current.getParent();
            if (parentLocal != null) {
                processQueue.addFirst(parentLocal);
            }
        }
        return current;
    }

    //this method returns a copy of a XMLObjects
    public static XMLObject getCopyXMLObject(XMLObject source) {
        LinkedList<XMLObject> sourceQueue = new LinkedList<XMLObject>();
        sourceQueue.addFirst(source);
        XMLObject parentLocal = source.getParent();
        String name = source.getKey();
        String objectClassName = source.getObjectClassName();
        boolean inherited = source.isInherited();
        XMLObject ret = new XMLObject(name, new XMLObject(parentLocal.getKey(), null, parentLocal.getObjectClassName()), objectClassName);
        LinkedList<XMLObject> targetQueue = new LinkedList<XMLObject>();
        targetQueue.addFirst(ret);
        while (sourceQueue.size() != 0) {
            XMLObject current = sourceQueue.getLast();
            sourceQueue.removeLast();
            XMLObject target = targetQueue.getLast();
            targetQueue.removeLast();
            for (int i = 0; i < current.getChildrenCount(); i++) {
                XMLNode child = current.getChild(i);
                parentLocal = target;
                name = child.getKey();
                inherited = child.isInherited();

                XMLObject objChild = XMLObject.convert(child);
                XMLReal realChild = XMLReal.convert(child);
                XMLInt intChild = XMLInt.convert(child);
                XMLString stringChild = XMLString.convert(child);
                XMLBool boolChild = XMLBool.convert(child);

                if (realChild != null) {
                    XMLReal xMLReal = new XMLReal(name, parentLocal, inherited, realChild.value());
                } else if (intChild != null) {
                    XMLInt xMLInt = new XMLInt(name, parentLocal, inherited, intChild.value());
                } else if (stringChild != null) {
                    XMLString xMLString = new XMLString(name, parentLocal, inherited, stringChild.value());
                } else if (boolChild != null) {
                    XMLBool xMLBool = new XMLBool(name, parentLocal, inherited, boolChild.value());
                } else if (objChild != null) {
                    XMLObject targetChild = new XMLObject(name, parentLocal, objChild.getObjectClassName());
                    if (objChild.isInherited()) {
                        targetChild.setInherited();
                    }
                    targetQueue.addFirst(targetChild);
                    sourceQueue.addFirst(objChild);
                }
            }
        }
        return ret;
    }
}
