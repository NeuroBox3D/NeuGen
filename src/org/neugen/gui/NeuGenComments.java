/*
 * File: NeuGenComments.java
 * Created on 30.07.2009, 09:52:27
 *
 */
package org.neugen.gui;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.neugen.datastructures.xml.XMLNode;
import org.neugen.datastructures.xml.XMLObject;

/**
 * @author Sergei Wolf 
 */
public final class NeuGenComments {

    private static final Logger logger = Logger.getLogger(NeuGenComments.class.getName());
    private final XMLObject configRoot;
    private final File comDir;
    private final String propName;

    public NeuGenComments(XMLObject root, File commentDir, String propName) {
        configRoot = root;
        this.comDir = commentDir;
        this.propName = propName;
    }

    public static Properties getProjectPropFromXML(String projectDir) {
        String path = projectDir + System.getProperty("file.separator") + NeuGenConstants.NEUGEN_PROJECT_FILE;
        Properties properties = new Properties();
        File projectFile = new File(path);
        try {
            InputStream is = new FileInputStream(projectFile);
            properties.loadFromXML(is);
            is.close();
        } catch (IOException ex) {
            logger.error(ex, ex);
        }
        return properties;
    }

    public static void saveProjectPropToXML(String projectDir, Properties properties) throws FileNotFoundException {
        String path = projectDir + System.getProperty("file.separator") + NeuGenConstants.NEUGEN_PROJECT_FILE;
        OutputStream propOut;
        File file = new File(path);
        propOut = new FileOutputStream(file);
        try {
            properties.storeToXML(propOut, "neugen project properties");
            propOut.close();
        } catch (IOException ex) {
            logger.error(ex, ex);
        }
    }

    public static void saveComments(Properties prop, File dir, String propName) {
        try {
            String commentFilePath = dir.getPath() + System.getProperty("file.separator") + propName + NeuGenConstants.PROPERTIES_SUFFIX;
            logger.debug("save comments: " + commentFilePath);
            File commentFile = new File(commentFilePath);
            OutputStream propOut = new FileOutputStream(commentFile);
            prop.store(propOut, "comments for " + propName + "." + NeuGenConstants.PROPERTIES_SUFFIX);
            propOut.close();
        } catch (IOException ex) {
            logger.error(ex, ex);
        }
    }

    public static Properties completeComments(XMLObject root, Properties properties) {
        // compare data of .neu with data of .properties
        Properties newProp = new Properties();
        LinkedList<XMLObject> queue = new LinkedList<XMLObject>();
        queue.addFirst(root);
        logger.info("complete property for comments: " + root.getKey());
        while (queue.size() != 0) {
            //System.out.println("laufe den Baum ab");
            XMLNode current = (XMLNode) queue.getLast();
            queue.removeLast();
            XMLObject currentObj = XMLObject.convert(current);
            String currentPropKey = currentObj.getPathForProperties();

            if (!properties.containsKey(currentPropKey)) {
                //logger.info("currentPropKey not found: " + currentPropKey);
                //properties.setProperty(currentPropKey, "no comment exists for: " + currentObj.getKey());
                newProp.setProperty(currentPropKey, "");
            } else {
                newProp.setProperty(currentPropKey, properties.getProperty(currentPropKey));
            }

            if (currentObj != null) {
                for (int i = 0; i < currentObj.getChildrenCount(); i++) {
                    XMLNode currentChild = currentObj.getChild(i);
                    XMLObject currentChildObj = XMLObject.convert(currentChild);
                    if (currentChildObj != null) {
                        queue.addFirst(currentChildObj);
                    } else {
                        currentPropKey = currentChild.getPathForProperties();
                        if (!properties.containsKey(currentPropKey)) {
                           // logger.info("currentPropKey not found unten: " + currentChild.getPathForProperties());
                           // logger.info("key not in properties: " + currentChild.getPathForProperties());
                            newProp.setProperty(currentChild.getPathForProperties(), "");
                        } else {
                            newProp.setProperty(currentChild.getPathForProperties(), properties.getProperty(currentPropKey));
                        }
                    }
                }
            }
        }
        properties.clear();
        return newProp;
    }

    public Properties getComments() {
        String commentFilePath = comDir.getPath() + System.getProperty("file.separator") + propName + NeuGenConstants.PROPERTIES_SUFFIX;
        logger.info("load comments: " + commentFilePath);
        File commentFile = new File(commentFilePath);
        Properties properties = new Properties();
        if (!commentFile.exists()) {
            try {
                XMLObject dummyProjectRoot = new XMLObject("dummy", null, null);
                configRoot.setParent(dummyProjectRoot);
                dummyProjectRoot.addChild(configRoot);
                properties = completeComments(configRoot, properties);
                configRoot.setParent(null);
                OutputStream propOut = new FileOutputStream(commentFile);
                properties.store(propOut, "comments for " + propName + "." + NeuGenConstants.PROPERTIES_SUFFIX);
                propOut.close();
            } catch (IOException ex) {
                logger.error(ex, ex);
            }
        } else {
            try {
                //System.out.print("lese comment file");
                FileInputStream stream = new FileInputStream(commentFile);
                properties.load(stream);
                stream.close();
                XMLObject dummyProjectRoot = new XMLObject("dummy", null, null);
                configRoot.setParent(dummyProjectRoot);
                dummyProjectRoot.addChild(configRoot);
                properties = completeComments(configRoot, properties);
                configRoot.setParent(null);
                //properties = completeComments(configRoot, properties);
            } catch (IOException e) {
                logger.error(e, e);
            }
        }
        return properties;
    }
}
