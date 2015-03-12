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
package org.neugen.utils;

import com.sun.jimi.core.Jimi;
import com.sun.jimi.core.JimiException;
import com.sun.jimi.core.JimiReader;
import com.sun.jimi.core.JimiWriter;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import org.apache.log4j.Logger;

/**
 *
 * @author Sergei Wolf
 */
public class NGImageIO {

    private final static Logger logger = Logger.getLogger(NGImageIO.class.getName());

    public ArrayList<ImageProducer> getFileList(File dir) {
        ArrayList<ImageProducer> fileList = new ArrayList<ImageProducer>();
        if (dir != null) {
            File[] listFiles = dir.listFiles();
            for (File file : listFiles) {
                if (isImage(file.getName())) {
                    try {
                        FileInputStream fis = new FileInputStream(file.getAbsolutePath());                                           
                        JimiReader reader = Jimi.createJimiReader(fis);
                      
                        //ImageProducer quelle = reader.getImage().getSource();

                        //fileList.add(quelle);

                        fileList.add(reader.getImageProducer());
                        reader.close();
                        fis.close();
                    } catch (Exception ex) {
                        logger.error(ex, ex);
                    }
                }
            }
        }
        return fileList;
    }

    public ArrayList<Image> getFileListImg(File dir) {
        ArrayList<Image> fileList = new ArrayList<Image>();
        if (dir != null) {
            File[] listFiles = dir.listFiles();
            for (File file : listFiles) {
                if (isImage(file.getName())) {
                    try {

                        FileInputStream fis = new FileInputStream(file.getAbsolutePath());

                        /*
                        JimiReader reader = Jimi.createJimiReader(fis);
                        ImageProducer quelle = reader.getImage().getSource();
                        fileList.add(quelle);
                        reader.close();
                         *
                         */

                        Image img = Jimi.getImage(fis);
                        fileList.add(img);

                    } catch (Exception ex) {
                        logger.error(ex, ex);
                    }
                }
            }
        }
        return fileList;
    }


    public static boolean isImage(final String filename) {
        String[] fileTypes = {".jpg", ".png", ".bmp", ".gif", ".tif", ".tiff"};
        for (String type : fileTypes) {
            if (filename.toLowerCase().endsWith(type)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGeom(final String filename) {
        String[] fileTypes = {".txt", ".obj"};
        for (String type : fileTypes) {
            if (filename.toLowerCase().endsWith(type)) {
                return true;
            }
        }
        return false;
    }

    public void writeTIF(Image img, String dest, String dirPath) throws JimiException {
        if (!dest.toLowerCase().trim().endsWith("tif")) {
            dest += ".tif";
            //System.out.println("Overriding to TIF, output file: " + dest);
        }

        dest = dest.substring(0, dest.lastIndexOf(".")) + ".jpg";
        JimiWriter writer = Jimi.createJimiWriter(dest);
        writer.setSource(img);
        dest = dest.substring(0, dest.lastIndexOf(".")) + ".tif";
        dest = dirPath + "/" + dest;
        writer.putImage(dest);
    }

    public int[][] getData(Image in, int width, int height) {
        /* Image => Matrix */

        /*
        if (in.getWidth(this) == -1 || in.getHeight(this) == -1) {
        //   in.flush();
        }

        int breite = in.getWidth(this); //Bildbreite
        int hoehe = in.getHeight(this); //Bildhoehe

        logger.info("breite: " + breite);
        logger.info("hoehe: " + hoehe);
         *
         */

        int pixelFeld[] = new int[width * height];
        byte bildPalette[] = new byte[256];

        //Farbpalette fuellen (8Bit)
        for (int i = 0; i < 256; i++) {
            bildPalette[i] = (byte) i;
        }//for

        //Image Objekt in Array grabben
        try {
            PixelGrabber pixelGrabber = new PixelGrabber(in, 0, 0, width, height, pixelFeld, 0, width);
            pixelGrabber.grabPixels();

        } catch (InterruptedException ie) {
            System.out.println(ie.toString());//Fehlermeldung ausgeben
        }//try-catch

        for (int i = 0; i < (width * height); i++) {
            //System.out.println("pixelFeld before: " + pixelFeld[i]);
            pixelFeld[i] &= 0xff; //in Grauwerte wandeln
            //  System.out.println("pixelFeld after: " + pixelFeld[i]);

        }//for

        //Array zu Matrix konvertieren
        int[][] bildMatrix = new int[width][height];
        int x = 0; //Anfangswert fuer x
        int y = 0; //Anfangswert fuer y
        //fuer alle Elemente des Feldes
        for (int i = 0; i < width * height; i++) {
            //wenn alle Elemente der Reihe durchlaufen
            if (x == width) {
                x = 0; //mit dem ersten Element
                y++;   //der naechsten Reihe beginnen
            }//if
            bildMatrix[x][y] = pixelFeld[i];
            x++;       //naechstes Element
        }//for

        return bildMatrix;
    }//erstelleMatrix
}
