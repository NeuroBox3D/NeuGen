package org.neugen.parsers;

import org.neugen.datastructures.xml.XMLString;
import org.neugen.datastructures.xml.XMLNode;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.datastructures.xml.XMLObjectInterface;
import org.neugen.datastructures.xml.XMLBool;
import org.neugen.datastructures.xml.XMLInt;
import org.neugen.datastructures.xml.XMLReal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javax.swing.tree.DefaultTreeModel;
import org.neugen.datastructures.Pair;

/**
 * @author Sergei Wolf
 */
public final class SimplifiedInheritance {

    private DefaultTreeModel treeModel;

    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(DefaultTreeModel treeModel) {
        this.treeModel = treeModel;
    }

    public void setInheritedValue(Number order, XMLNode source, XMLNode target) {
        /*
        System.out.println("setInheritedValue: " +order);
        System.out.println("source: "  +source.getPathLocal());
        System.out.println("target: "  +target.getPathLocal());
         */
        XMLBool boolSource = XMLBool.convert(source);
        XMLInt intSource = XMLInt.convert(source);
        XMLReal realSource = XMLReal.convert(source);
        XMLString stringSource = XMLString.convert(source);

        XMLBool boolTarget = XMLBool.convert(target);
        XMLInt intTarget = XMLInt.convert(target);
        XMLReal realTarget = XMLReal.convert(target);
        XMLString stringTarget = XMLString.convert(target);

        target.setAppendix(order);
        if (boolSource != null) {
            Boolean val = boolSource.value();
            boolTarget.setValue(val);
            treeModel.nodeChanged(target);
        } else if (intSource != null) {
            //System.out.println("int");
            int val = intSource.value();
            intTarget.setValue(val);
            //System.out.println("int change: " + intTarget.getPathLocal() + " ," + val);
            //System.out.println("end int");
            treeModel.nodeChanged(intTarget);
        } else if (realSource != null) {
            //System.out.println("real");
            float val = realSource.value();
            //System.out.println("real value: " + val);
            realTarget.setValue(val);
            //System.out.println("real change: " + realTarget.getPathLocal() + " ," + val);
            treeModel.nodeChanged(realTarget);
            //System.out.println("end real");
        } else if (stringSource != null) {
            String val = stringSource.value();
            stringTarget.setValue(val);
            //System.out.println("string change: " + stringTarget.getPathLocal() + " , " + val);
            treeModel.nodeChanged(stringTarget);
        }
        // System.out.println("end setInheritedValue");
    }

    private boolean isTreeRoot(XMLObject current) {
        int order = 1;
        XMLObject supreme = (XMLObject) current.getSupreme(order);
        Set<XMLObjectInterface> depSet = current.getDependants(order);
        if (depSet == null) {
            return false;
        }
        if (supreme == null) {
            if (depSet.size() > 0) {
                return true;
            } else {
                return false; //current is not a root node
            }
        }
        if (current.getSupreme(order + 1) != null) {
            return false;
        }
        for (XMLObjectInterface currentDep : depSet) {
            if (currentDep.getSupreme(order + 1) != null) {
                return true;
            }
        }
        return false;
    }

    public Object getInheritedValue(XMLNode current) {
        int maxOrder = 2;
        Pair<XMLNode, Integer> pair = getHeritage(maxOrder, current);
        if (pair == null) {
            return null;
        }
        XMLNode supreme = pair.first;
        XMLBool boolSource = XMLBool.convert(supreme);
        XMLInt intSource = XMLInt.convert(supreme);
        XMLReal realSource = XMLReal.convert(supreme);
        XMLString stringSource = XMLString.convert(supreme);
        if (boolSource != null) {
            Boolean val = boolSource.value();
            return val;
        } else if (intSource != null) {
            int val = intSource.value();
            return val;
        } else if (realSource != null) {
            float val = realSource.value();
            return val;
        } else if (stringSource != null) {
            String val = stringSource.value();
            return val;
        }
        return null;
    }

    public Pair<XMLNode, Integer> getHeritage(int maxOrder, XMLNode current) {
        XMLObject parent = current.getParent();
        for (int order = 1; order <= maxOrder; order++) {
            XMLObject supreme = (XMLObject) parent.getSupreme(order);
            if (supreme == null) {
                return null;
            }
            XMLNode supremeChild = supreme.getChild(current.getKey());
            if (!supremeChild.isInherited()) {
                return new Pair<XMLNode, Integer>(supremeChild, new Integer(order));
            }
            Object appendix = supremeChild.getAppendix();
            if (appendix instanceof Number) {
                int locOrder = ((Number) appendix).intValue();
                if (locOrder <= order) {
                    if (!isTreeRoot(supreme)) {
                        return new Pair<XMLNode, Integer>(supremeChild, new Integer(order));
                    } else if (isTreeRoot(supreme) && isTreeRoot(parent)) {
                        return new Pair<XMLNode, Integer>(supremeChild, new Integer(order));
                    }
                }
            }
        }
        return null;
    }

    public void updateAllInheritedValues(XMLNode currentChild) {
        //System.out.println("updateAllInheritedValues");
        Set<XMLObject> proc_set = new HashSet<XMLObject>();
        XMLObject parent = currentChild.getParent();
        LinkedList<Pair<XMLObject, Number>> processQueue = new LinkedList<Pair<XMLObject, Number>>();

        if (currentChild.isInherited()) {
            //System.out.println(currentChild.getPath());
            Pair<XMLNode, Integer> supremePair = getHeritage(2, currentChild);
            //DefaultXMLNode supremeChild = supremePair.first;
            if (supremePair == null) {
                //System.out.println("supremePair ist null");
            }
            Integer supremeOrder = supremePair.second;
            processQueue.addFirst(new Pair<XMLObject, Number>(parent, supremeOrder));
        } else {
            processQueue.addFirst(new Pair<XMLObject, Number>(parent, 0));
        }
        while (processQueue.size() > 0) {
            XMLObject current = processQueue.getLast().first;
            Number supremeOrder = processQueue.getLast().second;	//from which order will be inherited
            processQueue.removeLast();
            currentChild = current.getChild(currentChild.getKey()); //node to be changed
            //System.out.println(currentChild.getPath());
            //new PrintTrees().printAllDependance(2, current);
            //inherit from supreme or no inheritance
            if (supremeOrder.intValue() != 0) {
                XMLObject supreme = (XMLObject) current.getSupreme(supremeOrder.intValue());
                XMLNode supremeChild = supreme.getChild(currentChild.getKey());
                setInheritedValue(supremeOrder, supremeChild, currentChild); //inherit values
            }
            int maxOrder = current.getAllDependants().keySet().size(); //maximal order
            for (int order = 1; order <= maxOrder; order++) {
                Set<XMLObjectInterface> depSet = current.getDependants(order);
                //last object of a chain does not have dependent nodes of first order => getDependants second order
                if (depSet == null) {
                    if (maxOrder < 2) {
                        depSet = current.getDependants(order + 1);
                        order = order + 1;
                    }
                }
                if (depSet == null) {
                    continue;
                }
                for (XMLObjectInterface currentDep : depSet) {
                    XMLNode currentDepChild = (XMLNode) currentDep.getChild(currentChild.getKey());
                    //System.out.println(currentDepChild.getPath());
                    if (currentDepChild == null) {
                        //System.out.println("currentDepChild is null");
                    }
                    Object appendixObj = currentDepChild.getAppendix();
                    if (appendixObj == null) {
                        //System.out.println("appendix is null");
                    }
                    Number appendix = (Number) currentDepChild.getAppendix();
                    if (supremeOrder == null) {
                        //System.out.println("supremeOrder is null");
                    }
                    int inheritedOrder = 0;
                    if (!currentDepChild.isInherited()) {
                        inheritedOrder = 0;
                    } else if (order == 2) {
                        inheritedOrder = appendix.intValue();
                    } //inherited and order 1
                    else if (order == 1) {
                        //from which order currentChild ihnerited value
                        if (supremeOrder.intValue() == 0) {
                            inheritedOrder = 1;
                        } //curret is a chain head, and inherited
                        else if (supremeOrder.intValue() == 1 && isTreeRoot(current)) {
                            //currentDep is too a chain head
                            if (isTreeRoot((XMLObject) currentDep)) {
                                inheritedOrder = 1;
                            } //currentDep is an "normal" node
                            else {
                                inheritedOrder = 2;
                            }
                        } else if (supremeOrder.intValue() == 1) {
                            inheritedOrder = 1;
                        } else if (supremeOrder.intValue() == 2) {
                            inheritedOrder = 2;
                        }
                    }
                    if (!proc_set.contains((XMLObject) currentDep)) {
                        processQueue.addFirst(new Pair<XMLObject, Number>((XMLObject) currentDep, inheritedOrder));
                        proc_set.add((XMLObject) currentDep);
                    }
                }
            }
        }
        //System.out.println("End UpdateAllInheritedValues");
    }
}
