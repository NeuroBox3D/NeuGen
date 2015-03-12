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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.gui.NeuGenLib;
import org.neugen.parsers.DefaultInheritance;
import org.neugen.parsers.NeuGenConfigStreamer;
import org.neugen.parsers.NeuroMLWriter;

/**
 *
 * @author sergeiwolf
 */
public class TestParser {
    private final static Logger logger = Logger.getLogger(TestParser.class.getName());
    private NeuGenConfigStreamer ngStream;
    private XMLObject paramTree, internaTree, outputTree;
    private String paramPath, internaPath, outputOptionsPath;


    public TestParser() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {

        try {
            PropertyConfigurator.configureAndWatch("log4j-3.properties", 60 * 1000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ngStream = new NeuGenConfigStreamer();
        paramPath = "Param.neu";
        internaPath = "Interna.neu";
        outputOptionsPath = "OutputOptions.neu";

        File paramFile = new File("Neocortex" + System.getProperty("file.separator") + paramPath);
        File internaFile = new File("Neocortex" + System.getProperty("file.separator") + internaPath);
        File outputFile = new File("Neocortex" + System.getProperty("file.separator") + outputOptionsPath);
        try {
            paramTree = ngStream.streamIn(paramFile);
            internaTree = ngStream.streamIn(internaFile);
            outputTree = ngStream.streamIn(outputFile);
            DefaultInheritance inhProcess = new DefaultInheritance();
            inhProcess.process(paramTree);
            inhProcess.process(internaTree);
        } catch (FileNotFoundException ex) {
            logger.error("kann die Datei nicht laden!: " + ex);
        } catch (IOException ex) {
            logger.error("kann die Datein nicht lesen!: " + ex);
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void hello() {

    }

}
