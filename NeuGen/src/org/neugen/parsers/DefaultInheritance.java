/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2007–2012 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
 * 
 * This file is part of NeuGen.
 *
 * NeuGen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/NeuGen/LICENSE
 *
 * NeuGen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of NeuGen includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of NeuGen. The copyright statement/attribution may not be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do the following regarding copyright
 * notice and author attribution.
 *
 * Add an additional notice, stating that you modified NeuGen. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "NeuGen source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * S. Wolf, S. Grein, G. Queisser. NeuGen 2.0 -
 * Employing NeuGen 2.0 to automatically generate realistic
 * morphologies of hippocapal neurons and neural networks in 3D.
 * Neuroinformatics, 2013, 11(2), pp. 137-148, doi: 10.1007/s12021-012-9170-1
 *
 *
 * J. P. Eberhard, A. Wanner, G. Wittum. NeuGen -
 * A tool for the generation of realistic morphology 
 * of cortical neurons and neural networks in 3D.
 * Neurocomputing, 70(1-3), pp. 327-343, doi: 10.1016/j.neucom.2006.01.028
 *
 */
package org.neugen.parsers;

import org.neugen.datastructures.xml.XMLString;
import org.neugen.datastructures.xml.XMLNode;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.datastructures.xml.XMLObjectInterface;
import org.neugen.datastructures.xml.XMLBool;
import org.neugen.datastructures.xml.XMLInt;
import org.neugen.datastructures.xml.XMLReal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.neugen.datastructures.DataStructureConstants;
import org.neugen.datastructures.Pair;

/**
 * @author Sergei Wolf
 */
public final class DefaultInheritance {

    public XMLObject process(XMLObject rootObject) {
        //List with all dependance trees
        List<XMLObject> beginningTreeObjects = new ArrayList<XMLObject>();
        XMLObject root = rootObject;
        //get all initial dependance trees.
        getDependanceTrees(beginningTreeObjects, root);
        //PrintTrees print = new PrintTrees();
        //print.printArrayList("Ketten ohne 2 Ordnung" , beginningTreeObjects);
		/* 
         * iterate over all dependance trees and copy
         * append, and if new dependance trees are created -> update the List
         * with all dependance trees
         */
        genDependancies(1, beginningTreeObjects);
        Map<XMLObject, Set<XMLObject>> innerDependances = new HashMap<XMLObject, Set<XMLObject>>();
        Map<XMLObject, Set<XMLObject>> dependanceTreeMap = getDependanceTrees(beginningTreeObjects, innerDependances);
        Set<XMLObject> rootCandidateSet = getRootCandidate(dependanceTreeMap);
        //print.printHashSet("rootCandidate" ,rootCandidateSet);
        updateInnerDependances(beginningTreeObjects, dependanceTreeMap, innerDependances);
        Set<XMLObject> secondOrderTrees = createSecondOrderTrees(dependanceTreeMap, innerDependances, rootCandidateSet);
        //print.printHashSet("2.Ordnung trees", secondOrderTrees);
        List<XMLObject> copySecondOrderTrees = new ArrayList<XMLObject>(secondOrderTrees);
        genDependancies(2, copySecondOrderTrees);
        inherit(root, 1);
        inherit(root, 2);

        beginningTreeObjects.clear();
        beginningTreeObjects = null;
        innerDependances.clear();
        innerDependances = null;
        dependanceTreeMap.clear();
        dependanceTreeMap = null;
        rootCandidateSet.clear();
        rootCandidateSet = null;
        copySecondOrderTrees.clear();
        copySecondOrderTrees = null;
        //System.gc();
        //print.printAllDependance(2, root);
        return root;
    }

    private void getDependanceTrees(List<XMLObject> beginningTreeObjects, XMLObject root) {
        LinkedList<XMLObject> processingQueue = new LinkedList<XMLObject>();
        processingQueue.addFirst(root);
        while (processingQueue.size() > 0) {
            getSibChain(processingQueue, beginningTreeObjects);
            XMLObject current = processingQueue.getLast();
            processingQueue.removeLast();
            for (int k = 0; k < current.getChildrenCount(); k++) {
                XMLNode child = current.getChild(k);
                XMLObject objChild = XMLObject.convert(child);
                if (objChild != null) {
                    if (!isSibling(objChild)) {
                        processingQueue.addFirst(objChild);
                    }
                }
            }
        }
    }

    private void getSibChain(LinkedList<XMLObject> processingQueue,
            List<XMLObject> beginningTreeObjects) {
        //System.out.println("generateDepTree");
        XMLObject root = processingQueue.getLast();
        //no dublicate trees in beginningTreeObjects
        if (root.getAllDependants().size() > 0) {
            return;
        }
        LinkedList<XMLObject> queue = new LinkedList<XMLObject>();
        LinkedList<XMLObject> rootQueue = new LinkedList<XMLObject>();
        queue.addFirst(root);
        //find all dependant subtrees and save in rootQueue
        while (queue.size() > 0) {
            XMLObject current = queue.getLast();
            //System.out.println("current genDep while: " + current.getPath());
            queue.removeLast();
            //put only not processed nodes in processingQueue
            if (current != root) {
                processingQueue.addFirst(current);
            }
            XMLObject siblingNode = getSibChild(current);
            if (siblingNode != null) {
                for (int i = 0; i < siblingNode.getChildrenCount(); i++) {
                    XMLNode child = siblingNode.getChild(i);
                    XMLObject objChild = XMLObject.convert(child);
                    queue.addFirst(objChild);
                    //save Relation
                    current.addDepentants(1, objChild);
                    /*
                    System.out.println("current in genDepTree: " + current.getPath());
                    System.out.println("child von current in genDepTree: " + objChild.getPath());
                    System.out.println();
                     */
                }
                if (rootQueue.size() == 0) {
                    rootQueue.addFirst(current);
                }
            }
        }
        if (rootQueue.size() == 1) {
            beginningTreeObjects.add(rootQueue.getLast());
        }
    }

    //generate Depenadncies
    private void genDependancies(int order, List<XMLObject> beginningTreeObjects) {
        LinkedList<XMLObject> processingQueue = new LinkedList<XMLObject>();
        for (int j = 0; j < beginningTreeObjects.size(); j++) {
            XMLObject current = beginningTreeObjects.get(j);
            processingQueue.addFirst(current);
            while (processingQueue.size() > 0) {
                current = processingQueue.getLast();
                /*
                if(order == 2) {
                new PrintTrees().printAllDependance(2, current);
                }
                 */
                processingQueue.removeLast();
                Set<XMLObjectInterface> dependantSet = current.getDependants(order);
                if (dependantSet == null) {
                    continue;
                }
                LinkedList<XMLObject> childrenList = new LinkedList<XMLObject>();
                //add linked in queues
                for (int i = 0; i < current.getChildrenCount(); i++) {
                    XMLNode child = current.getChild(i);
                    XMLObject objChild = XMLObject.convert(child);
                    if (objChild == null || isSibling(objChild)) {
                        continue;
                    }
                    processingQueue.addFirst(objChild);
                    childrenList.addFirst(objChild);
                }
                for (XMLObjectInterface target : dependantSet) {
                    //target node is a dependant from current node
                    XMLObject targetParent = (XMLObject) target;
                    processingQueue.addFirst(targetParent);
                    //in linked list are all linked from current node
                    for (XMLObject sourceChild : childrenList) {
                        /*
                        if(order == 2) {
                        System.out.println("current: " + current.getPath());
                        System.out.println("soureChild: " + sourceChild.getPath());
                        System.out.println("targetParent: " + targetParent.getPath());
                        }
                         */
                        localProcess(order, targetParent, sourceChild, beginningTreeObjects);
                    }
                }
                childrenList.clear();
            }
        }
    }

    private void localProcess(int order, XMLObject targetParent, XMLObject sourceChild,
            List<XMLObject> beginningTreeObjects) {
        if (targetParent != null && sourceChild != null) {
            String name = sourceChild.getKey();
            XMLNode targetChild = targetParent.getChild(name);
            XMLObject targetChildObj = XMLObject.convert(targetChild);
            if (targetChild == null && isAlias(sourceChild)) {
                for (int k = 0; k < sourceChild.getChildrenCount(); k++) {
                    XMLNode aliasChild = sourceChild.getChild(k);
                    XMLObject aliasChildObj = XMLObject.convert(aliasChild);
                    if (aliasChildObj == null) {
                        continue;
                    }
                    String aliasChildName = aliasChildObj.getKey();
                    targetChild = targetParent.getChild(aliasChildName);
                    if (targetChild == null) {
                        LinkedList<XMLNode> copyQueue = copyAppend(targetParent, sourceChild);
                        while (copyQueue.size() > 0) {
                            targetChild = copyQueue.getLast();
                            copyQueue.removeLast();
                            targetChildObj = XMLObject.convert(targetChild);
                            //LinkedList<DefaultXMLObject> rootQueue = new LinkedList<DefaultXMLObject>();
                            //rootQueue.addFirst(targetChildObj);
                            //System.out.println("copyAppend alias");
                            getDependanceTrees(beginningTreeObjects, targetChildObj);
                            //System.out.println("copyAppend -end alias");
                        }
                    }
                    targetChildObj = XMLObject.convert(targetChild);
                    if (beginningTreeObjects.contains(aliasChildObj)) {
                        if (!beginningTreeObjects.contains(targetChildObj)) {
                            beginningTreeObjects.add(targetChildObj);
                        }
                    }
                    aliasChildObj.addDepentants(order, targetChildObj);
                }
            } else {
                if (targetChild == null) {
                    LinkedList<XMLNode> copyQueue = copyAppend(targetParent, sourceChild);
                    targetChild = copyQueue.getLast();
                    targetChildObj = XMLObject.convert(targetChild);
                    //LinkedList<DefaultXMLObject> rootQueue = new LinkedList<DefaultXMLObject>();
                    //rootQueue.addFirst(targetChildObj);
                    //System.out.println("copyAppend");
                    getDependanceTrees(beginningTreeObjects, targetChildObj);
                    //System.out.println("copyAppend end");
                }
                //no dublicate in beginningTreeObjects
                if (!isAlias(targetChildObj)) {
                    if (beginningTreeObjects.contains(sourceChild)) {
                        if (!beginningTreeObjects.contains(targetChildObj)) {
                            beginningTreeObjects.add(targetChildObj);
                        }
                    }
                    sourceChild.addDepentants(order, targetChildObj);
                } else {
                    for (int k = 0; k < targetChildObj.getChildrenCount(); k++) {
                        XMLNode aliasChild = targetChildObj.getChild(k);
                        XMLObject aliasChildObj = XMLObject.convert(aliasChild);
                        if (beginningTreeObjects.contains(sourceChild)) {
                            if (!beginningTreeObjects.contains(aliasChildObj)) {
                                beginningTreeObjects.add(aliasChildObj);
                            }
                        }
                        if (aliasChildObj == null) {
                            continue;
                        }
                        sourceChild.addDepentants(order, aliasChildObj);
                    }
                }
            }
        }
    }

    private Map<XMLObject, Set<XMLObject>> getDependanceTrees(List<XMLObject> beginningTreeObjects,
            Map<XMLObject, Set<XMLObject>> innerDependances) {

        Set<XMLObject> beginningTreeObjectsSet = new HashSet<XMLObject>(beginningTreeObjects);
        Map<XMLObject, Set<XMLObject>> dependanceTreeMap = new TreeMap<XMLObject, Set<XMLObject>>(new XMLObject.CompareObjects());
        for (XMLObject current : beginningTreeObjects) {
            Set<XMLObjectInterface> set = current.getDependants(1);
            if (set == null) {
                continue;
            }
            for (XMLObjectInterface currentDep : set) {
                XMLObject currentDependance = (XMLObject) currentDep;
                boolean found = false;
                /*for(DefaultXMLObject currentCompare : beginningTreeObjects) {
                if(current == currentCompare) continue;
                if(currentCompare == currentDependance) {
                if(!dependanceTreeMap.containsKey(current)) {
                dependanceTreeMap.put(current, new HashSet<DefaultXMLObject>());
                }
                found = true;
                dependanceTreeMap.get(current).add(currentCompare);
                break;
                }
                }*/
                if (beginningTreeObjectsSet.contains(currentDependance)) {
                    if (!dependanceTreeMap.containsKey(current)) {
                        dependanceTreeMap.put(current, new HashSet<XMLObject>());
                    }
                    dependanceTreeMap.get(current).add(currentDependance);
                } else {
                    if (!innerDependances.containsKey(current)) {
                        innerDependances.put(current, new HashSet<XMLObject>());
                    }
                    innerDependances.get(current).add(currentDependance);
                }
            }
        }
        return dependanceTreeMap;
    }

    //CopyAppend
    private LinkedList<XMLNode> copyAppend(XMLObject targetParent, XMLNode source) {
        XMLObject objSource = XMLObject.convert(source);
        LinkedList<XMLNode> ret = new LinkedList<XMLNode>();
        if (isAlias(objSource)) {
            for (int i = 0; i < objSource.getChildrenCount(); i++) {
                XMLNode schild = objSource.getChild(i);
                XMLNode tchild = targetParent.getChild(schild.getKey());
                if (tchild == null) {
                    ret.addAll((copyAppend(targetParent, schild)));
                }
            }
        } else {
            String key = source.getKey();
            if (objSource != null) {
                XMLObject obj = new XMLObject(key, targetParent, objSource.getObjectClassName());
                ret.addFirst(obj);
                for (int i = 0; i < objSource.getChildrenCount(); i++) {
                    copyAppend(obj, objSource.getChild(i));
                }
            }
        }
        return ret;
    }

    private XMLNode complete(XMLNode source, XMLObject targetParent) {
        /*
        System.out.println("copyExistingLeave");
        System.out.println("source: " + source.getPath());
        System.out.println("targetParent: " + targetParent.getPath());
         */
        XMLBool boolSource = XMLBool.convert(source);
        XMLInt intSource = XMLInt.convert(source);
        XMLReal realSource = XMLReal.convert(source);
        XMLString stringSource = XMLString.convert(source);
        String key = source.getKey();
        XMLNode ret = null;

        if (boolSource != null) {
            ret = new XMLBool(key, targetParent, true, boolSource.value());
        } else if (intSource != null) {
            ret = new XMLInt(key, targetParent, true, intSource.value());
        } else if (realSource != null) {
            ret = new XMLReal(key, targetParent, true, realSource.value());
        } else if (stringSource != null) {
            ret = new XMLString(key, targetParent, true, stringSource.value());
        }
        return ret;
    }

    /**
     * @param dependanceTreeMap: only outer dependance (beginnig objects from a chain)
     * @param innerDependances:
     * @param rootCandidateSet: Other trees inherit from these trees
     * @return secondOrderTrees:
     */
    private Set<XMLObject> createSecondOrderTrees(
            Map<XMLObject, Set<XMLObject>> dependanceTreeMap,
            Map<XMLObject, Set<XMLObject>> innerDependances,
            Set<XMLObject> rootCandidateSet) {
        Set<XMLObject> secondOrderTrees = new HashSet<XMLObject>();
        Set<XMLObject> notSecondOrderTrees = new HashSet<XMLObject>();
        int order = 1;
        for (XMLObject currentRoot : rootCandidateSet) {
            LinkedList<XMLObject> localQueue = new LinkedList<XMLObject>();
            localQueue.addFirst(currentRoot);
            while (localQueue.size() > 0) {
                XMLObject loaded = localQueue.getLast();
                localQueue.removeLast();
                LinkedList<Pair<XMLObject, XMLObject>> procQueue =
                        new LinkedList<Pair<XMLObject, XMLObject>>();
                Set<XMLObject> currentOuterDepSet = dependanceTreeMap.get(loaded);
                /*
                System.out.println("loaded: " + loaded.getPath());
                new PrintTrees().printHashSet("outer Dependance ", currentOuterDepSet);
                 */
                if (currentOuterDepSet == null) {
                    continue;
                }
                for (XMLObject outerDep : currentOuterDepSet) {
                    //outer dependance from current
                    localQueue.addFirst(outerDep);
                    //from the beginning of a chain at the beginning of another chain
                    procQueue.addFirst(new Pair<XMLObject, XMLObject>(loaded, outerDep));
                }
                while (procQueue.size() > 0) {
                    Pair<XMLObject, XMLObject> currentPair = procQueue.getLast();
                    XMLObject current = currentPair.first;       //obere Kette fuer alle siblinge (innerDep)
                    XMLObject outerDep = currentPair.second;		//untere Kette f. alle siblinge
                    procQueue.removeLast();
                    Set<XMLObject> currentInnerDepSet = innerDependances.get(current);

                    if (currentInnerDepSet == null) {
                        //System.out.println("current: " + current.getName());
                        continue;
                    }
                    //all dep. nodes within a chain
                    for (XMLObject innerDep : currentInnerDepSet) {
                        //all dep. nodes within a dep. chain
                        Set<XMLObject> outerDepInnerDepSet = innerDependances.get(outerDep);
                        //System.out.println("outerDep: " + outerDep.getPath());
                        //new PrintTrees().printHashSet("outerDepInnerDepSet", outerDepInnerDepSet);
                        boolean found = false;
                        if (outerDepInnerDepSet != null) {
                            for (XMLObject outerInnerDep : outerDepInnerDepSet) {
                                if (outerInnerDep.getKey().equals(innerDep.getKey())) {
                                    innerDep.addDepentants(order + 1, outerInnerDep);
                                    if (!notSecondOrderTrees.contains(innerDep)) {
                                        secondOrderTrees.add(innerDep);
                                    }
                                    secondOrderTrees.remove(outerInnerDep);
                                    notSecondOrderTrees.add(outerInnerDep);
                                    procQueue.addFirst(new Pair<XMLObject, XMLObject>(innerDep, outerInnerDep));
                                    found = true;
                                    break;
                                }
                            }
                        }
                        //in the chain there is no internal dependence
                        if (!found) {
                            /*
                            System.out.println("innerDep: " + innerDep.getPath());
                            System.out.println("not found inner deps: outerDep: " + outerDep.getPath());
                            System.out.println();
                             */
                            XMLObject outerInnerDep = completeDepChain(innerDep, outerDep);
                            //complete inner set too
                            if (!innerDependances.containsKey(outerDep)) {
                                innerDependances.put(outerDep, new HashSet<XMLObject>());
                            }
                            innerDependances.get(outerDep).add(outerInnerDep);
                            innerDep.addDepentants(order + 1, outerInnerDep);
                            if (!notSecondOrderTrees.contains(innerDep)) {
                                secondOrderTrees.add(innerDep);
                            }
                            secondOrderTrees.remove(outerInnerDep);
                            notSecondOrderTrees.add(outerInnerDep);
                            procQueue.addFirst(new Pair<XMLObject, XMLObject>(innerDep, outerInnerDep));
                        }
                    }
                }
            }
        }
        return secondOrderTrees;
    }

    /* The function is used in create 2. order trees	*/
    private XMLObject completeDepChain(XMLObject source, XMLObject targetDepParent) {
        XMLObject sourceParent = source.getParent();
        boolean testSib = isSibling(sourceParent);//sib/sib
        XMLObject sibTargetChild = getSibChild(targetDepParent);
        if (testSib) {
            //System.out.println("completeDepCain ");
            //sibling node in not in chain
            if (sibTargetChild == null) {
                sibTargetChild = new XMLObject(sourceParent.getKey(),
                        targetDepParent, sourceParent.getObjectClassName());
            }
            XMLObject targetDepChild = new XMLObject(source.getKey(),
                    sibTargetChild, source.getObjectClassName());
            //System.out.println("targetDepChild: " + targetDepChild.getName());
            targetDepParent.addDepentants(1, targetDepChild);
            return targetDepChild;
        }
        return null;
    }

    /**
     *
     * @param beginningTreeObjects
     * @param dependanceTreeMap
     * @param innerDependances
     * @return rootCandidateSet
     */
    private void updateInnerDependances(List<XMLObject> beginningTreeObjects,
            Map<XMLObject, Set<XMLObject>> dependanceTreeMap,
            Map<XMLObject, Set<XMLObject>> innerDependances) {
        for (int j = 0; j < beginningTreeObjects.size(); j++) {
            LinkedList<XMLObject> procQueue = new LinkedList<XMLObject>();
            XMLObject currentRoot = beginningTreeObjects.get(j);
            Set<XMLObject> set = innerDependances.get(currentRoot);
            if (set != null) {
                procQueue.addAll(set);
            }
            while (procQueue.size() > 0) {
                XMLObject currentValue = procQueue.getLast();
                procQueue.removeLast();
                if (innerDependances.containsKey(currentValue)) {
                    continue;
                }
                Set<XMLObjectInterface> depSet = currentValue.getDependants(1);
                Set<XMLObject> depSetCopy = new HashSet<XMLObject>();
                if (depSet == null) {
                    continue;
                }

                for (XMLObjectInterface current : depSet) {
                    depSetCopy.add((XMLObject) current);
                    procQueue.addAll(depSetCopy);
                }
                /*
                if(depSet != null) {
                System.out.println("1. order:" + currentValue.getPath());
                new PrintTrees().printHashSet(depSet);
                }
                 */
                innerDependances.put(currentValue, depSetCopy);
            }
            /*
            if(set != null) {
            System.out.println("1. order:" + currentRoot.getPath());
            new PrintTrees().printHashSet("innerDepSet", set);

            }
             */
        }
    }

    private Set<XMLObject> getRootCandidate(Map<XMLObject, Set<XMLObject>> dependanceTreeMap) {
        //copy from dependanceTreeMap -> rootCandidateSet
        Set<XMLObject> keySet = dependanceTreeMap.keySet();
        Set<XMLObject> rootCandidateSet = new HashSet<XMLObject>();
        for (XMLObject current : keySet) {
            rootCandidateSet.add(current);
        }
        // remove dependance from rootCandidateSet
        Set<XMLObject> keySetCompare = dependanceTreeMap.keySet();
        for (XMLObject currentKeyCompare : keySetCompare) {
            Set<XMLObject> depTreeSet = dependanceTreeMap.get(currentKeyCompare);
            for (XMLObject dependant : depTreeSet) {
                rootCandidateSet.remove(dependant);
            }
            /*
            Set<DefaultXMLObject> keySetTmp = dependanceTreeMap.keySet();
            for(DefaultXMLObject currentKey : keySetTmp) {
            if(currentKeyCompare == currentKey) continue;
            if(dependanceTreeMap.get(currentKeyCompare).contains(currentKey)) {
            rootCandidateSet.remove(currentKey);
            continue;
            }
            }*/
        }
        return rootCandidateSet;
    }

    private void inherit(XMLObject root, int order) {
        LinkedList<XMLObject> processQueue = new LinkedList<XMLObject>();
        //System.out.println("inherit: " + order);
        processQueue.addFirst(root);
        while (processQueue.size() > 0) {
            XMLObject current = processQueue.getLast();
            processQueue.removeLast();
            for (int i = 0; i < current.getChildrenCount(); i++) {
                XMLNode currentChild = current.getChild(i);
                XMLObject currentChildObj = XMLObject.convert(currentChild);
                if (currentChildObj != null) {
                    processQueue.addFirst(currentChildObj);
                } else {
                    /*
                    if(order == 2) {
                    new PrintTrees().printAllDependance(2, current);
                    }
                     */
                    Set<XMLObjectInterface> depSet = current.getDependants(order);
                    if (depSet == null) {
                        continue;
                    }
                    for (XMLObjectInterface currentDep : depSet) {
                        XMLNode currentDepChild = (XMLNode) currentDep.getChild(currentChild.getKey());
                        if (currentDepChild == null) {
                            if (!currentChild.isInherited() || currentDep.getSupreme(order + 1) == null) {
                                //testAdress(current, (DefaultXMLObject)currentDep);
                                //System.out.println("inherit order (if): " + order);
                                currentDepChild = complete(currentChild, (XMLObject) currentDep);
                                currentDepChild.setAppendix(new Integer(order));
                            } else if (!isTreeRoot(current)) {
                                //System.out.println("inherit order (else): " + order);
                                currentDepChild = complete(currentChild, (XMLObject) currentDep);
                                currentDepChild.setAppendix(new Integer(order));
                            } //tree root
                            else if (isTreeRoot((XMLObject) currentDep)) {
                                currentDepChild = complete(currentChild, (XMLObject) currentDep);
                                currentDepChild.setAppendix(new Integer(order));
                            }
                        }
                    }
                }
            }
        }
    }

    private void testAdress(XMLObject current, XMLObject currentDep) {
        XMLObject root = current.getRoot();
        LinkedList<String> queue = new LinkedList<String>();
        XMLObject depend = currentDep;
        while (depend != null) {
            String name = depend.getKey();
            queue.addFirst(name);
            XMLObject parent = depend.getParent();
            depend = parent;
        }
        queue.removeFirst();
        XMLObject obj = null;
        LinkedList<XMLObject> processQueue = new LinkedList<XMLObject>();
        processQueue.addFirst(root);
        while (processQueue.size() > 0) {
            obj = processQueue.getLast();
            processQueue.removeLast();
            if (queue.size() == 0) {
                break;
            }
            String name = queue.getFirst();
            queue.removeFirst();
            XMLNode child = obj.getChild(name);
            //System.out.println(name + " " + child.getName());
            if (child instanceof XMLObject) {
                processQueue.addFirst((XMLObject) child);
            }
        }
        //System.out.println("current: " + current.getPath());
        if (currentDep != obj) {
            System.out.println("currentDep: " + currentDep.getPath() + "- Adresse: " + currentDep
                    + ". Geschwister: " + currentDep.getParent().getChildrenCount());
            System.out.println("childObj von current: " + obj.getPath() + "- Adresse: " + obj
                    + ". Geschwister: " + obj.getParent().getChildrenCount());
            XMLObject parent = currentDep.getParent();
            for (int i = 0; i < parent.getChildrenCount(); i++) {
                XMLNode child = parent.getChild(i);
                System.out.println("Geschwisternamen: " + child.getKey());
            }
        }
    }

    private boolean isTreeRoot(XMLObject current) {
        //System.out.println("isTreeRoot");
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
                return false; //no root node
            }
        }
        for (XMLObjectInterface currentDep : depSet) {
            if (current.getSupreme(order + 1) != null) {
                return false;
            }
            if (currentDep.getSupreme(order + 1) != null) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSibling(XMLObject toTest) {
        boolean val = false;
        if (toTest != null) {
            String oClassName = toTest.getObjectClassName();
            val = oClassName.equals(DataStructureConstants.SIBLINGS_TYPE);
            return val;
        } else {
            return false;
        }
    }

    private static boolean isAlias(XMLObject toTest) {
        boolean val = false;
        if (toTest != null) {
            String oClassName = toTest.getObjectClassName();
            val = oClassName.equals(DataStructureConstants.ALIAS_TYPE);
            //System.out.println(oClassName + " " + alistring + " " + val);
            return val;
        } else {
            return false;
        }
    }

    private XMLObject getSibChild(XMLObject parent) {
        boolean val = false;
        if (parent != null) {
            for (int i = 0; i < parent.getChildrenCount(); i++) {
                XMLObject child = XMLObject.convert(parent.getChild(i));
                if (child != null) {
                    String oClassName = child.getObjectClassName();
                    val = oClassName.equals(DataStructureConstants.SIBLINGS_TYPE);
                }
                if (val) {
                    return child;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public static XMLNode reverseProcess(XMLNode root) {
        XMLObject rootObj = XMLObject.convert(root);
        root.setAppendix(null);
        //root.appendix = null;
        //System.out.println("reverseProcess");
        if (root.isInherited() && rootObj == null) {
            root.getParent().removeChild(root);
            return null;
        }
        if (rootObj != null) {
            rootObj.removeAllDependants();
            rootObj.removeAllSupremes();

            int counter = 0;
            while (counter < rootObj.getChildrenCount()) {
                XMLNode ret = reverseProcess(rootObj.getChild(counter));
                if (ret != null) {
                    counter = counter + 1;
                }
            }
            if (!rootObj.hasUninheritedChildren()) {
                if (rootObj.getParent() != null && !isAlias(rootObj.getParent()) && !isSibling(rootObj.getParent())) {
                    rootObj.getParent().removeChild(root);
                    //System.out.println("entferne:unten " + root.getPath());
                    return null;
                }
            }
        }
        return root;
    }
}
