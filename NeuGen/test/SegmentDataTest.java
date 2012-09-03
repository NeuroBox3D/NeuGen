
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.vecmath.Point3f;
import javax.vecmath.Point3i;

import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.Segment;
import org.neugen.parsers.MorphMLReader;
import org.neugen.parsers.NeuGenConfigStreamer;
import org.neugen.slider.CartGridGenerator;
import org.neugen.slider.SegmentData;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


/**
 *
 * @author Sergei
 */
public class SegmentDataTest {

    private static String filePath;
    private final static Logger logger = Logger.getLogger(ImportNeuroMLTest.class.getName());
    private NeuGenConfigStreamer ngStream;

    @Before
    public void setUp() {
        filePath = "demo/1L4Stellate.xml";
    }
   

    @Test
    public void testSegmentData() {
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
            SegmentData d = new SegmentData(net);
            System.out.println(d.getNumberOfSegments());
            List<Segment> segments = d.getData();

            CartGridGenerator<Float> gen = new CartGridGenerator<Float>(new Point3f(0, 0, 0), new Point3f(1f, 1f, 1f));
            for (Segment segment : segments) {
                gen.resolve(segment, 1.5f);
            }
            System.out.println("grid size : " + gen.getGrid().getNDataPoints());


            // Calculate the min/max int x,y,z
            {
                int mins[] = null, maxs[] = null;
                Map<Integer, Map<Integer, Map<Integer, Float>>> dataGrid = gen.getGrid().dataGrid;
                Set<Integer> keySet = dataGrid.keySet();
                for (Integer zInt : keySet) {
                    System.out.println("" + zInt + " " + dataGrid.get(zInt).size());
                    Map<Integer, Map<Integer, Float>> zLayer = dataGrid.get(zInt);
                    Set<Integer> keySet2 = zLayer.keySet();
                    for (Integer xInt : keySet2) {
                        Set<Entry<Integer, Float>> xLine = zLayer.get(xInt).entrySet();
                        for (Entry<Integer, Float> p : xLine) {
                            int coord[] = new int[]{xInt, p.getKey(), zInt};

                            if (mins == null) {
                                mins = Arrays.copyOf(coord, coord.length);
                                maxs = Arrays.copyOf(coord, coord.length);
                            } else {
                                for (int i = 0; i < mins.length; ++i) {
                                    if (mins[i] > coord[i]) {
                                        mins[i] = coord[i];
                                    }
                                    if (maxs[i] < coord[i]) {
                                        maxs[i] = coord[i];
                                    }
                                }
                            }
                        }
                    }
                }

                System.out.println("mins " + new Point3i(mins));
                System.out.println("maxs " + new Point3i(maxs));

                //JFrame jFrame = new JFrame();
                {
                    int width = maxs[0] - mins[0];
                    int height = maxs[1] - mins[1];

                    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    Graphics graphics = bufferedImage.getGraphics();

                    // draw grid



                    for (Integer zInt = mins[2]; zInt < maxs[2] + 1; ++zInt) {
                        System.out.println("z=" + zInt);
                        Map<Integer, Map<Integer, Float>> layer0 = dataGrid.get(zInt);
                        if (layer0 == null) {
                            continue;
                        }
                        for (Integer xInt : layer0.keySet()) {
                            Map<Integer, Float> lineX = layer0.get(xInt);
                            for (Integer yInt : lineX.keySet()) {
                                graphics.drawLine(xInt - mins[0], yInt - mins[1], xInt - mins[0], yInt - mins[1]);
                            }
                        }
                        ImageIO.write(bufferedImage, "png", new File("images/z" + zInt
                                + ".png"));
                    }



//					jFrame.setLayout(new FlowLayout());
//					MyPanel myPanel = new MyPanel(bufferedImage.getScaledInstance(1000, 1000, BufferedImage.SCALE_FAST));
//					ImageIO.write(bufferedImage, "png", new File("myimage.png"));
//					myPanel.setSize(1000,1000);
//					JScrollPane jScrollPane = new JScrollPane( myPanel);
//					jScrollPane.setSize(1000,1000);
//					jFrame.add(jScrollPane);
//					jFrame.add(new JSlider(0,1000));
//					jFrame.setSize(new Dimension(1200,1000));
//					jFrame.setVisible(true);
//					JOptionPane.showConfirmDialog(null, "Close?");
                }
            }

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
    }




}
