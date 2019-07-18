/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2012-2015 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.neugen.datastructures.DataStructureConstants;
import org.neugen.datastructures.xml.XMLInt;
import org.neugen.datastructures.xml.XMLNode;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.datastructures.xml.XMLReal;
import org.neugen.datastructures.xml.XMLString;
import org.neugen.gui.NeuGenConstants;
import org.neugen.utils.Utils;

/**
 * @author Sergei Wolf
 */
final class XMLRoot {
    //private List<DefaultXMLNode> children = new ArrayList<DefaultXMLNode>();

    private XMLObject root;
    private String minor;
    private String major;
    private String key;

    public XMLRoot(String key) {
        this.key = key;
        root = new XMLObject(key, null, "root_node");
    }

    public void setRoot(XMLObject root) {
        this.root = root;
    }

    public XMLObject getRoot() {
        return root;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public void setRootProperties(String major, String minor) {
        this.major = major;
        this.minor = minor;
    }

    public String getKey() {
        return key;
    }
}

/**
 * Class to convert the root of the tree.
 * 
 */
final class XMLRootCoverter implements Converter {

    private String filePrefix;

    public XMLRootCoverter() {
    }

    public XMLRootCoverter(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(XMLRoot.class);
    }

    // write data
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        //System.out.println("root converter");
        XMLRoot rootContainer = (XMLRoot) source;
        writer.addAttribute(DataStructureConstants.MAJOR, rootContainer.getMajor());
        writer.addAttribute(DataStructureConstants.MINOR, rootContainer.getMinor());

        XMLObject root = rootContainer.getRoot();
        for (int k = 0; k < root.getChildrenCount(); k++) {
            XMLNode xmlNode = root.getChild(k);
            XMLObject xmlObj = XMLObject.convert(xmlNode);
            if (xmlObj != null) {
                context.convertAnother(xmlObj, new XMLObjectConverter());
            } else if (xmlNode instanceof XMLString) {
                context.convertAnother(xmlNode, new XMLStringConverter());
            } else if (xmlNode instanceof XMLInt) {
                context.convertAnother(xmlNode, new XMLIntConverter());
            } else if (xmlNode instanceof XMLReal) {
                context.convertAnother(xmlNode, new XMLRealConverter());
            }
        }
    }

    //read data
    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        //System.out.println("unmarshal: Root");
        XMLRoot rootContainer = new XMLRoot(filePrefix);
        XMLObject root = rootContainer.getRoot();
        String nodeName;
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            nodeName = reader.getNodeName();
            if (nodeName.equals(DataStructureConstants.STRING)) {
                context.convertAnother(null, XMLString.class, new XMLStringConverter(root));
            } else if (nodeName.equals(DataStructureConstants.OBJECT)) {
                context.convertAnother(null, XMLObject.class, new XMLObjectConverter(root));
            } else if (nodeName.equals(DataStructureConstants.REAL)) {
                context.convertAnother(null, XMLReal.class, new XMLRealConverter(root));
            } else if (nodeName.equals(DataStructureConstants.INT)) {
                context.convertAnother(null, XMLInt.class, new XMLIntConverter(root));
            }
            reader.moveUp();
        }
        rootContainer.setRoot(root);
        return rootContainer;
    }
}

/**
 * Class to convert XMLOjects.
 */
final class XMLObjectConverter implements Converter {

    private XMLObject parent;

    public XMLObjectConverter() {
    }

    public XMLObjectConverter(XMLObject parent) {
        this.parent = parent;
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(XMLObject.class);
    }

    public void setParent(XMLObject parent) {
        this.parent = parent;
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer,
            MarshallingContext context) {
        //System.out.println("Object converter");
        XMLObject xmlObj = (XMLObject) source;
        String name = xmlObj.getKey();
        String classdesc = xmlObj.getObjectClassName();
        //System.out.println("name: " + name + " ,classdesc: " + classdesc);
        writer.startNode(DataStructureConstants.OBJECT);
        writer.addAttribute(DataStructureConstants.KEY, name);
        writer.addAttribute(DataStructureConstants.CLASSDESCRIPTOR, classdesc);

        for (int i = 0; i < xmlObj.getChildrenCount(); i++) {
            XMLNode child = xmlObj.getChild(i);
            XMLObject childObj = XMLObject.convert(child);
            if (childObj != null) {
                context.convertAnother(childObj, new XMLObjectConverter());
            } else if (child instanceof XMLString) {
                context.convertAnother(child, new XMLStringConverter());
            } else if (child instanceof XMLInt) {
                context.convertAnother(child, new XMLIntConverter());
            } else if (child instanceof XMLReal) {
                context.convertAnother(child, new XMLRealConverter());
            }
        }
        //empty tag for libxml parser: <tag></tag>, not </tag>. TODO: DTD allow empty tag for objects
        if (xmlObj.getChildrenCount() == 0) {
            writer.setValue("\n");
        }
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader,
            UnmarshallingContext context) {
        //System.out.println("unmarshal: object converter");
        String name = reader.getAttribute(DataStructureConstants.KEY);
        String classdescriptor = reader.getAttribute(DataStructureConstants.CLASSDESCRIPTOR);
        XMLObject currentObj = new XMLObject(name, parent, classdescriptor);
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            name = reader.getNodeName();
            if (name.equals(DataStructureConstants.STRING)) {
                context.convertAnother(null, XMLString.class, new XMLStringConverter(currentObj));
            } else if (name.equals(DataStructureConstants.OBJECT)) {
                context.convertAnother(null, XMLObject.class, new XMLObjectConverter(currentObj));
            } else if (name.equals(DataStructureConstants.REAL)) {
                context.convertAnother(null, XMLReal.class, new XMLRealConverter(currentObj));
            } else if (name.equals(DataStructureConstants.INT)) {
                context.convertAnother(null, XMLInt.class, new XMLIntConverter(currentObj));
            }
            reader.moveUp();
        }
        return currentObj;
    }
}

/**
 * Class to convert string nodes.
 */
final class XMLStringConverter implements Converter {

    private XMLObject parent;

    public XMLStringConverter() {
    }

    public XMLStringConverter(XMLObject parent) {
        this.parent = parent;
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(XMLString.class);
    }

    //write data
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer,
            MarshallingContext context) {
        //System.out.println("string converter");
        XMLString strNode = (XMLString) source;
        writer.startNode(DataStructureConstants.STRING);
        writer.addAttribute(DataStructureConstants.KEY, strNode.getKey());
        //System.out.println("string key " + strNode.getName());
        writer.setValue(strNode.value());
        writer.endNode();
    }

    //read Data
    @Override
    public Object unmarshal(HierarchicalStreamReader reader,
            UnmarshallingContext context) {
        //System.out.println("unmarshal string converter");
        String name = reader.getAttribute(DataStructureConstants.KEY);
        String value = reader.getValue();
        //System.out.println("name: " + name + " ,value: " + value);
        XMLString xmlStr = new XMLString(name, parent, false, value);
        return xmlStr;
    }
}

/**
 * Class to read/write int xml nodes.
 */
final class XMLIntConverter implements Converter {

    private XMLObject parent;

    public XMLIntConverter() {
    }

    public XMLIntConverter(XMLObject parent) {
        this.parent = parent;
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(XMLInt.class);
    }

    //write data
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer,
            MarshallingContext context) {
//        System.out.println("string converter");
        XMLInt strNode = (XMLInt) source;
        writer.startNode(DataStructureConstants.INT);
        writer.addAttribute(DataStructureConstants.KEY, strNode.getKey());
        writer.setValue(String.valueOf(strNode.value()));
        writer.endNode();
    }

    //read Data
    @Override
    public Object unmarshal(HierarchicalStreamReader reader,
            UnmarshallingContext context) {
        //System.out.println("unmarshal: int converter");
        String name = reader.getAttribute(DataStructureConstants.KEY);
        String value = reader.getValue();
        int lVal = (Integer.valueOf(value)).intValue();
//        System.out.println("name: " + name + " ,value: " + value);
        XMLInt xmlInt = new XMLInt(name, parent, false, lVal);
        return xmlInt;
    }
}

/**
 * Class to convert real nodes.
 */
final class XMLRealConverter implements Converter {

    private XMLObject parent;

    public XMLRealConverter(XMLObject parent) {
        this.parent = parent;
    }

    public XMLRealConverter() {
    }

    //write data
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer,
            MarshallingContext context) {
//        System.out.println("string converter");
        XMLReal strNode = (XMLReal) source;
        writer.startNode(DataStructureConstants.REAL);
        writer.addAttribute(DataStructureConstants.KEY, strNode.getKey());
        writer.setValue(String.valueOf(strNode.value()));
        writer.endNode();
    }

    //read Data
    @Override
    public Object unmarshal(HierarchicalStreamReader reader,
            UnmarshallingContext context) {
        //System.out.println("unmarshal: real converter");
        String name = reader.getAttribute(DataStructureConstants.KEY);
        String value = reader.getValue();
        float dVal = (Float.valueOf(value)).floatValue();
//      System.out.println("name: " + name + " ,value: " + value);
        XMLReal xmlReal = new XMLReal(name, parent, false, dVal);
        return xmlReal;
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(XMLReal.class);
    }
}

public final class NeuGenConfigStreamer {

    private final static Logger logger = Logger.getLogger(NeuGenConfigStreamer.class.getName());
    private String absProjectPath;

    public NeuGenConfigStreamer(String absPath) {
        absProjectPath = absPath;
    }

    public NeuGenConfigStreamer() {
        super();
    }

    public XMLObject streamIn(File file) throws FileNotFoundException, IOException {
        String fileName = file.getPath();
        String filePrefix = Utils.getPrefix(file);
        XStream xstream = new XStream();
        xstream.registerConverter(new XMLRootCoverter(filePrefix));
        xstream.alias(DataStructureConstants.XED, XMLRoot.class);
        XMLRoot rootContainer = (XMLRoot) xstream.fromXML(new FileInputStream(fileName));
        XMLObject root = rootContainer.getRoot();
        return root;
    }

    // writes tree with xstream
    public void streamOut(XMLObject root, File file) throws IOException {
        XStream xstream = new XStream();
        xstream.registerConverter(new XMLRootCoverter());
        xstream.registerConverter(new XMLObjectConverter());
        xstream.registerConverter(new XMLStringConverter());
        xstream.registerConverter(new XMLIntConverter());
        xstream.registerConverter(new XMLRealConverter());
        xstream.alias(DataStructureConstants.XED, XMLRoot.class);
        //write dtd.txt
        //File fileIn = new File(fileName);
        try {
            /*String absDTDPath = absProjectPath + System.getProperty("file.separator")
                    + NeuGenConstants.DTD_FNAME;
            if (absProjectPath == null) {
                absDTDPath = NeuGenConstants.DTD_FNAME;
            }*/

            URL inputUrl = NeuGenConfigStreamer.class.getResource("/org/neugen/gui/resources/"+NeuGenConstants.DTD_FNAME);
            String absDTDPath=inputUrl.getPath();
            logger.info("path of DTD.txt: " + absDTDPath);
            FileInputStream in = new FileInputStream(absDTDPath);
            FileOutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (IOException e) {
            logger.error(e, e);
        }
        FileWriter writer = new FileWriter(file, true);
        writer.write("\n");
        //serialize
        String filePrefix = Utils.getPrefix(file);
        XMLRoot rootContainer = new XMLRoot(filePrefix);
        rootContainer.setRootProperties("1", "0");
        rootContainer.setRoot(root);
        xstream.toXML(rootContainer, writer);
    }

    public static void main(String args[]) throws IOException {
        //NGStreamer test = new NGStreamer();
        //read from file
        //DefaultXMLObject root = test.streamIn("Param.neu");
        //write to file
        //test.streamOut(root, "testTarget.xml");
    }
}
