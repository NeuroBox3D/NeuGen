package org.neugen.parsers;

import org.neugen.datastructures.DataStructureConstants;
import org.neugen.datastructures.xml.XMLString;
import org.neugen.datastructures.xml.XMLNode;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.datastructures.xml.XMLBool;
import org.neugen.datastructures.xml.XMLInt;
import org.neugen.datastructures.xml.XMLReal;

/**
 * @author Alexander Wanner
 */
public final class RecursiveInheritance {

    public XMLObject process(XMLNode node) {
        return XMLObject.convert(processNode(node));
    }

    private XMLNode processNode(XMLNode node) {
        if (node == null) {
            return node;
        }
        XMLObject current = XMLObject.convert(node);
        //System.out.println("path: " + current.getPath());
        if (current == null) {
            return node;
        }
        XMLObject sNode = null;
        boolean found = false;
        //look for sibling among the linked of node
        for (int i = 0; i < current.getChildrenCount(); i++) {
            XMLNode child = current.getChild(i);
            XMLObject childObj = XMLObject.convert(child);
            found = isSibling(childObj);
            if (found) {
                sNode = childObj;
                for (int j = 0; j < sNode.getChildrenCount(); j++) {
                    processNode(sNode.getChild(j));
                }
            } else {
                processNode(childObj);
            }
        }
        if (sNode != null) {
            for (int i = 0; i < current.getChildrenCount(); i++) {
                XMLNode child = current.getChild(i);
                XMLObject childObj = XMLObject.convert(child);
                if (isSibling(childObj)) {
                    continue;
                }
                for (int j = 0; j < sNode.getChildrenCount(); j++) {
                    XMLNode childSib = sNode.getChild(j);
                    XMLObject childSibObj = XMLObject.convert(childSib);
                    XMLNode targetChild = childSibObj.getChild(child.getKey());
                    if (targetChild == null) {
                        copyAppend(childSibObj, child);
                    } else {
                        complete(child, targetChild);
                    }
                }
            }
            for (int i = 0; i < sNode.getChildrenCount(); i++) {
                processNode(sNode.getChild(i));
            }
        }
        return node;
    }

    private void copyAppend(XMLObject targetParent, XMLNode source) {
        /*
        System.out.println("copyAppend");
        System.out.println("targetParent: " + targetParent.getPath());
        System.out.println("source: " + source.getPath());
         */
        XMLObject objSource = XMLObject.convert(source);
        if (isAlias(objSource)) {
            for (int i = 0; i < objSource.getChildrenCount(); i++) {
                XMLNode schild = objSource.getChild(i);
                XMLNode tchild = targetParent.getChild(schild.getKey());
                if (tchild == null) {
                    copyAppend(targetParent, schild);
                } else {
                    complete(schild, tchild);
                }
            }
        } else {
            XMLBool boolSource = XMLBool.convert(source);
            XMLInt intSource = XMLInt.convert(source);
            XMLReal realSource = XMLReal.convert(source);
            XMLString stringSource = XMLString.convert(source);
            String key = source.getKey();
            if (boolSource != null) {
                XMLBool xMLBool = new XMLBool(key, targetParent, true, boolSource.value());
            } else if (intSource != null) {
                XMLInt xMLInt = new XMLInt(key, targetParent, true, intSource.value());
            } else if (realSource != null) {
                XMLReal xMLReal = new XMLReal(key, targetParent, true, realSource.value());
            } else if (stringSource != null) {
                XMLString xMLString = new XMLString(key, targetParent, true, stringSource.value());
            } else if (objSource != null) {
                XMLObject obj = new XMLObject(key, targetParent, objSource.getObjectClassName());
                for (int i = 0; i < objSource.getChildrenCount(); i++) {
                    copyAppend(obj, objSource.getChild(i));
                }
            }
        }
    }

    private void complete(XMLNode source, XMLNode target) {
        XMLObject otarget = XMLObject.convert(target);
        if (otarget == null) {
            return;
        }
        XMLObject osource = XMLObject.convert(source);
        if (isAlias(otarget)) {
            for (int i = 0; i < otarget.getChildrenCount(); i++) {
                complete(source, otarget.getChild(i));
            }
        } else {
            for (int i = 0; i < osource.getChildrenCount(); i++) {
                XMLNode schild = osource.getChild(i);
                XMLObject schildObj = XMLObject.convert(schild);
                if (isAlias(schildObj)) {
                    complete(schild, target);
                    continue;
                }
                XMLNode tchild = otarget.getChild(schild.getKey());
                XMLObject tchildObj = XMLObject.convert(tchild);
                if (tchild == null) {
                    copyAppend(otarget, schild);
                } else if (!isAlias(tchildObj)) {
                    complete(schild, tchild);
                } else {
                    for (int j = 0; j < tchildObj.getChildrenCount(); j++) {
                        complete(schild, tchildObj.getChild(j));
                    }
                }
            }
        }
    }

    private boolean isSibling(XMLObject toTest) {
        boolean val = false;
        XMLObject op = toTest;
        if (op != null) {
            String oClassName = op.getObjectClassName();
            val = oClassName.equals(DataStructureConstants.SIBLINGS_TYPE);
            return val;
        } else {
            return false;
        }
    }

    private boolean isAlias(XMLObject toTest) {
        boolean val = false;
        XMLObject op = toTest;
        if (op != null) {
            String oClassName = op.getObjectClassName();
            val = oClassName.equals(DataStructureConstants.ALIAS_TYPE);
            return val;
        } else {
            return false;
        }
    }

    public XMLNode reverseProcess(XMLNode root) {
        XMLObject rootObj = XMLObject.convert(root);
        if (root.isInherited() && rootObj == null) {
            root.getParent().removeChild(root);
            return null;
        }
        if (rootObj != null) {
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
                    return null;
                }
            }
        }
        return root;
    }
}
