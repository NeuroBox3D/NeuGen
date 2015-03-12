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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neugen.datastructures.Cellipsoid;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.Section;
import org.neugen.datastructures.Segment;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.datastructures.xml.XMLObject;

import org.neugen.parsers.MorphMLReader;
import org.neugen.parsers.NeuGenConfigStreamer;
import org.neugen.utils.NeuGenLogger;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 *
 * @author sergeiwolf
 */
public class ImportNeuroMLTest {

    private static String filePath;
    private final static Logger logger = Logger.getLogger(ImportNeuroMLTest.class.getName());
    private NeuGenConfigStreamer ngStream;
    private XMLObject paramTree, internaTree;
    private String paramPath;
    private String internaPath;

    public ImportNeuroMLTest() {
        ngStream = new NeuGenConfigStreamer();
        paramPath = "Param.xml";
        internaPath = "Interna.xml";
        File paramFile = new File("Neocortex" + System.getProperty("file.separator") + paramPath);
        File internaFile = new File("Neocortex" + System.getProperty("file.separator") + internaPath);
        try {
            paramTree = ngStream.streamIn(paramFile);
            internaTree = ngStream.streamIn(internaFile);
        } catch (FileNotFoundException ex) {
            logger.error("could not find the file: " + ex);
        } catch (IOException ex) {
            logger.error("could not read the file: " + ex);
        }
    }

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
          filePath  = "demo/1L4Stellate.xml";
          NeuGenLogger.initLogger();
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public static void importNeuroML() {
         try {
             FileInputStream instream;
             InputSource is;
             //this.fileLength = (int) file.length();
             //neuen Sax Parser erzeugen
             SAXParserFactory spf = SAXParserFactory.newInstance();
             spf.setNamespaceAware(true);
             XMLReader xmlReader = spf.newSAXParser().getXMLReader();
             MorphMLReader mmlBuilder = new MorphMLReader();
             xmlReader.setContentHandler(mmlBuilder);
             File file = new File(filePath);
             instream = new FileInputStream(file);
             is = new InputSource(instream);
             //setProgress(fileLength/4, 0, fileLength);
             //System.out.println(instream.available());
             //System.out.print(fileLength);
             logger.info("reading neuroML file: " + file.getPath() + "\n");
             xmlReader.parse(is);
             Net net = mmlBuilder.getNet();
             //ngView.setNet(mmlBuilder.getNet());
             //System.out.println("total number of segments: " + mmlBuilder.getNet().getTotalNumberOfSegments() + "\n");
             //setProgress(fileLength, fileLength/2 , fileLength);
             Neuron neuron = net.getNeuronList().get(0);
             neuron.infoNeuron();
             Cellipsoid soma = neuron.getSoma();
             Section somaSec = soma.getCylindricRepresentant();
             if(somaSec == null) {
                 logger.info("soma section is null!: " + soma.getSections().size());
             } else {
                 ArrayList<Segment> segments = (ArrayList<Segment>) somaSec.getSegments();
             }

         } catch (Exception e) {
             System.err.println(e);
             e.printStackTrace();
         }
     }
}