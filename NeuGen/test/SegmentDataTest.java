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
