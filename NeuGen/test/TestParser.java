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
