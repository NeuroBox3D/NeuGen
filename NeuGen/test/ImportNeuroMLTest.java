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