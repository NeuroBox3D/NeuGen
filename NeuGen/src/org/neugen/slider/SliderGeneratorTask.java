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
package org.neugen.slider;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.imageio.ImageIO;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import javax.vecmath.Point3f;
import org.apache.log4j.Logger;

import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.Segment;
import org.neugen.gui.NeuGenView;
import org.neugen.gui.SliderDialog;
import org.neugen.makemovie.MovieMaker;
import org.neugen.utils.Utils;

/**
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public final class SliderGeneratorTask extends Task<Void, Void> {

    /** use to log messages */
    private final static Logger logger = Logger.getLogger(SliderGeneratorTask.class.getName());
    private Net net;

    public final class SliderImage extends JComponent {

        private static final long serialVersionUID = 1L;
        private Image img;
        private int width;
        private int height;

        public SliderImage(Image img, int width, int height) {
            super();
            this.width = width;
            this.height = height;
            this.img = img;
            if (img != null) {
                repaint();
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, width, height, this);
        }
    }

    public SliderGeneratorTask(Application application, Net net) {
        super(application);
        this.net = net;
    }


    @Override
    protected Void doInBackground() throws Exception {
        NeuGenView ngView = NeuGenView.getInstance();
        SliderDialog sliderDialog = new SliderDialog(ngView.getFrame(), true);
        sliderDialog.setVisible(true);
        sliderDialog.setLocationRelativeTo(null);
        if (sliderDialog.getReturnStatus() == SliderDialog.RET_CANCEL) {
            return null;
        }
        
        Utils.del(new File("imagesSlider"));
        File dir = new File("imagesSlider");
        boolean mkDir = dir.mkdir();
        while(!mkDir) {
            mkDir = dir.mkdir();
        }

        int imgWidth = sliderDialog.getImgWidth();
        int imgHeight = sliderDialog.getImgHeight();
        Point3f coordOrigin = sliderDialog.getCoordOrigin();
        Point3f spacing = sliderDialog.getSpacing();
        if (ngView != null) {
            ngView.outPrintln("Image directory: " + dir.getAbsolutePath());
            ngView.outPrintln("The image width: " + imgWidth);
            ngView.outPrintln("The image height: " + imgHeight);
            ngView.outPrintln("The coordinate origin: " + coordOrigin.toString());
            ngView.outPrintln("Spacing: " + spacing.toString());
        }

        JScrollPane visualScrollPane = ngView.getVisualScrollPane();
        visualScrollPane.setBorder(null);
        visualScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        visualScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        SegmentData d = new SegmentData(net);
        logger.info(d.getNumberOfSegments());
        List<Segment> segments = d.getData();
        CartGridGenerator<Float> gen = new CartGridGenerator<Float>(coordOrigin, spacing);
        for (Segment segment : segments) {
            gen.resolve(segment, 1.5f);
        }
        logger.info("grid size : " + gen.getGrid().getNDataPoints());

        // Calculate the min/max int x,y,z
        {
            int mins[] = null, maxs[] = null;
            Map<Integer, Map<Integer, Map<Integer, Float>>> dataGrid = gen.getGrid().dataGrid;
            Set<Integer> keySet = dataGrid.keySet();
            for (Integer zInt : keySet) {
                //logger.info("" + zInt + " " + dataGrid.get(zInt).size());
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

            if(mins == null || maxs == null) {
                return null;
            }
            //logger.info("mins " + new Point3i(mins).toString());
            //logger.info("maxs " + new Point3i(maxs).toString());

            {
  
                int width = maxs[0] - mins[0];
                int height = maxs[1] - mins[1];

                //if(width > imgWidth) width = imgWidth;
                //if(height > imgHeight) height = imgHeight;
                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics graphics = bufferedImage.getGraphics();

                List<File> vFiles = new ArrayList<File>();
                for (Integer zInt = mins[2]; zInt < maxs[2] + 1; ++zInt) {
                    //logger.info("z=" + zInt);
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
                    String dirPath = dir.getAbsolutePath();
                    File file = new File(dirPath + "/z" + zInt + ".jpg");
                    ImageIO.write(bufferedImage, "jpg", file);
                    visualScrollPane.getViewport().add(new SliderImage(bufferedImage, imgWidth, imgHeight));
                    visualScrollPane.validate();
                    visualScrollPane.repaint();

                    float progress = Math.abs((float) (zInt)) / ((float) maxs[2]);
                    try {
                        setProgress(progress);
                    } catch (IllegalArgumentException ae) {
                        logger.error(ae);
                    }
                    vFiles.add(file);
                }
                ngView.outPrintln("Number of images: " + dir.list().length);


                /*
                 * GENERATE VIDEO
                 */
                width = imgWidth;
                height = imgHeight;
                int frameRate = 60;
                File[] files = vFiles.toArray(new File[0]);
                try {
                    MovieMaker movieMaker = new MovieMaker(width, height, frameRate, new File("slider.mov"), files);
                    movieMaker.makeMovie();
                } catch (Exception ex) {
                    logger.error(ex);
                }
                ngView.outPrintln("end");
            }
        }
        return null;
    }

    @Override
    protected void succeeded(Void result) {
        NeuGenView ngView = NeuGenView.getInstance();
        ngView.getVisualScrollPane().getViewport().removeAll();
        ngView.getVisualScrollPane().validate();
        ngView.getVisualScrollPane().repaint();
        ngView.getVisualScrollPane().setBorder(javax.swing.BorderFactory.createTitledBorder("Visualization"));
        ngView.enableButtons();
        System.gc();
        ngView.outPrintln(Utils.getMemoryStatus());
    }
}
