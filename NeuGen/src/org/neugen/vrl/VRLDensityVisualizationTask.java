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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neugen.vrl;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import eu.mihosoft.vrl.system.VParamUtil;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import ij.process.StackConverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Point3f;
import javax.vecmath.Point3i;
import javax.vecmath.Vector3f;
import org.apache.log4j.Logger;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.Region;
import org.neugen.datastructures.VolumeOfVoxels;
import org.neugen.datastructures.VoxelVolume;
import org.neugen.datastructures.VoxelVolumeIterator;
import org.neugen.gui.DensityDialog;
import org.neugen.gui.NeuGenView;
import org.neugen.gui.VisualizationTask;
import org.neugen.utils.NGImageIO;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public final class VRLDensityVisualizationTask {

    DensityVisualizationConfigDialog ngVisConfig;
    private static final Logger logger =
            Logger.getLogger(VRLDensityVisualizationTask.class.getName());
    private File dir, selectedFile;
    private float scale = 0.001f;
    private float scaleZ = 0.001f;
    private Scene s;

    public float getScaleZ() {
        return scaleZ;
    }

    public float getScale() {
        return scale;
    }

    void setMyProgress(int thisindex, int i, int voxelArraySize) {
        //
    }

    void setMyProgress(float f) {
        //
    }

    public enum Density {

        NET, IMAGE;
    }
    private Density dens;

    public VRLDensityVisualizationTask() {
        //
    }

    public VisualizationContext run(File f, Density dens, VoxelParams vDialog) throws Exception {

        this.dir = f.getParentFile();
        this.selectedFile = f;

        VParamUtil.validate(VParamUtil.VALIDATOR_EXISTING_FILE, selectedFile);

        VisualizationContext context = null;

        switch (dens) {
            case NET: {
                DensityDialog densDialog = new DensityDialog(NeuGenView.getInstance().getFrame(), true);
                densDialog.setVisible(true);
                densDialog.setLocationRelativeTo(null);
                if (densDialog.getReturnStatus() == DensityDialog.RET_OK) {
                    Point3f volumeEnd = new Point3f(Region.getInstance().getUpRightCorner());

                    if (VisualizationTask.getInstance() != null) {
                        if (VisualizationTask.getInstance().getNGVisualization() != null) {
                            scale = VisualizationTask.getInstance().getNGVisualization().getScale();
                        }
                    }

                    volumeEnd.scale(scale);
                    logger.info(volumeEnd.toString());
                    NeuGenView ngView = NeuGenView.getInstance();
                    Net net = ngView.getNet();
                    VoxelVolume vv = new VoxelVolume(net, densDialog.getVoxelLenght(), densDialog.getDensityPart(), densDialog.getDensityMethod());
                    VoxelVolumeIterator voxelVolumeIterator = new VoxelVolumeIterator(vv);
                    Point3i voxelNumber = voxelVolumeIterator.getVoxelNumber();
                    VolumeOfVoxels volumeOfVoxels = new VolumeOfVoxels(voxelNumber, volumeEnd);
                    int thisindex = 0;
                    float value;
                    int voxelArraySize = vv.getVoxelArray().size();
                    while (voxelVolumeIterator.hasNext()) {
                        value = voxelVolumeIterator.next();
                        volumeOfVoxels.setVoxelValue(thisindex, value); // same index
                        thisindex++;
                        setMyProgress(thisindex, 0, voxelArraySize);
                    }

//                    ngVisConfig = new DensityVisualizationConfigDialog(this,volumeOfVoxels, dens, canvasParent, canvas3D);
//                    DensityVisualizationConfigDialog.setInstance(ngVisConfig);
//                    ngVisConfig.setLocationRelativeTo(null);
//                    ngVisConfig.setVisible(true);

                } else {
                    NeuGenView.getInstance().enableButtons();
                }

                break;
            }

            case IMAGE: {

                System.out.println("NeuGen.visualType: IMAGE");

                //NGImageIO imageIO = new NGImageIO();
                //List<ImageProducer> fileList = imageIO.getFileList(dir);
                logger.info("image density task, dir path: " + dir.getAbsolutePath());

                boolean imageSequence = false; // indicates whether processing image sequence

                ArrayList<int[][]> dataList = new ArrayList<int[][]>();
                //int numFiles = fileList.size();
                //logger.info("anzahl bilder: " + numFiles);
                int count = 0;
                int width = 0;
                int height = 0;
                int stackSize = 0;

                BranchGroup bg = null;
                //ArrayList<ImageIcon> fileList = new ArrayList<ImageIcon>();
                if (dir != null) {
                    File[] listFiles = dir.listFiles();
                    boolean stackLoaded = false;
                    for (File file : listFiles) {
                        if (NGImageIO.isImage(file.getName())) {

                            if (!imageSequence && !selectedFile.getName().equals(file.getName())) {
                                continue;
                            }

                            System.out.println("image stack name: " + file.getName());
                            if (stackLoaded && !imageSequence) {
                                logger.info("stack schon geladen!");
                                continue;
                            }
                            try {
                                /*
                                 Image img = Jimi.getImage(file.getAbsolutePath());
                                 ImageIcon imgIcon = new ImageIcon(img);
                                 fileList.add(imgIcon);
                                 * 
                                 */
                                logger.info("file name: " + file.getName());
                                ImagePlus imp = ij.IJ.openImage(file.getAbsolutePath());

                                stackSize = imp.getStackSize();

                                if (stackSize > 1) {
                                    StackConverter sc = new StackConverter(imp);
                                    sc.convertToGray8();
                                } else {
                                    count++;
                                    ImageConverter im = new ImageConverter(imp);
                                    im.convertToGray8();
                                }

                                ImageStack stack = imp.getStack();

                                if (stack.getSize() > 1) {
                                    stackLoaded = true;
                                }

                                for (int i = 1; i <= stack.getSize(); i++) {
                                    ImageProcessor ip = stack.getProcessor(i);
                                    byte[] pixels = (byte[]) ip.getPixels();

                                    width = ip.getWidth();
                                    height = ip.getHeight();

                                    //Array zu Matrix konvertieren
                                    int[][] bildMatrix = new int[width][height];
                                    int x = 0; //Anfangswert fuer x
                                    int y = 0; //Anfangswert fuer y
                                    //fuer alle Elemente des Feldes
                                    for (int j = 0; j < width * height; j++) {
                                        //wenn alle Elemente der Reihe durchlaufen
                                        if (x == width) {
                                            x = 0; //mit dem ersten Element
                                            y++;   //der naechsten Reihe beginnen
                                        }//if
                                        bildMatrix[x][y] = pixels[j];
                                        x++;       //naechstes Element
                                    }//for
                                    dataList.add(bildMatrix);
                                }
                            } catch (Exception ex) {
                                logger.error(ex, ex);
                            }
                        } else if (NGImageIO.isGeom(file.getName())) {
                            bg = getGeom(file);
                        }

                        System.out.println(">> stack loaded");
                    }

                    /*
                     for (ImageIcon image : fileList) {
                     //Image image = ngView.getFrame().createImage(imageProd);
                     //image.flush();
                     //System.out.println(image.getIconHeight());
                     //System.out.println(image.getIconWidth());
                     try {
                     PixelGrabber grabber = new PixelGrabber(image.getImage(), 0, 0, -1, -1, false);
                     if (grabber.grabPixels()) {
                     int width = grabber.getWidth();
                     int height = grabber.getHeight();
                     int[][] data = imageIO.getData(image.getImage(), width, height);
                     dataList.add(data);
                     //setMyProgress(count, 0, numFiles);
                     } else {
                     logger.info("geht nichts..");
                     }
                     } catch (InterruptedException ex) {
                     logger.error(ex, ex);
                     }
                     count++;
                     }
                     * 
                     */

//                    ngView.outPrintln("image stack size: " + dataList.size());
                    logger.info("image density task, anzahl bilder: " + dataList.size());
                    Point3i voxelLength = vDialog.getVoxelLength();
                    logger.info("voxelLenght: " + voxelLength.toString());
                    int intensity = vDialog.getThreshold();
                    int weight = vDialog.getWeight();
                    float scannedImgHeight = vDialog.getHeight();
                    float scannedImgWidth = vDialog.getWidth();
                    float scannedDepth = vDialog.getDepth();

                    logger.info("scanned height: " + scannedImgHeight);
                    logger.info("scanned width: " + scannedImgWidth);
                    logger.info("scanned depth: " + scannedDepth);

                    //this.scale = 0.0065f;
                    //this.scale = 0.01f;
                    //this.scale = 0.0077f;

                    this.scale = scannedImgHeight / height;  //um pro pixel
                    this.scale /= 10.0f;
                    //float scaleZ = 0.0498f / 5.0f;
                    float optDepth = 0.1f;
                    float faktor = scannedDepth / optDepth;

                    scaleZ = 0.0f;
                    if (stackSize > 0) {
                        float stackDepth = stackSize * scannedDepth;
                        logger.info("tiefe: " + stackDepth);
                        scaleZ = scannedDepth;
                        scaleZ /= 10.0f;
                    } else {
                        scaleZ = scannedDepth / faktor;
                        scaleZ /= 10.0f;
                    }
                    
//                    scale *= 2.f;
//                    scaleZ *= 2.f;

                    logger.info("scale: " + scale);
                    logger.info("scaleZ: " + scaleZ);
                    logger.info("stack size: " + stackSize);

                    VoxelVolume vv = new VoxelVolume(dataList, voxelLength, intensity, weight);
                    Point3f volumeEnd = new Point3f(Region.getInstance().getUpRightCorner());
                    logger.info("volumeEnd: " + volumeEnd.toString());

                    volumeEnd.x *= scale;
                    volumeEnd.z *= scale;
                    volumeEnd.y *= scaleZ;

                    VoxelVolumeIterator voxelVolumeIterator = new VoxelVolumeIterator(vv);
                    Point3i voxelNumber = voxelVolumeIterator.getVoxelNumber();
                    VolumeOfVoxels volumeOfVoxels = new VolumeOfVoxels(voxelNumber, volumeEnd);
                    int thisindex = 0;
                    float value = 0.0f;
                    int voxelArraySize = vv.getVoxelArray().size();
                    while (voxelVolumeIterator.hasNext()) {
                        value = voxelVolumeIterator.next();
                        volumeOfVoxels.setVoxelValue(thisindex, value); // same index
                        thisindex++;
                        setMyProgress(thisindex, 0, voxelArraySize);
                    }

                    context = new VisualizationContext(bg, volumeOfVoxels, scale, scaleZ);

                } else {
                    NeuGenView.getInstance().enableButtons();
                }

                break;
            }
        }

        return context;

    }

    private BranchGroup getGeom(File file) throws FileNotFoundException, IOException {
        Reader in = new FileReader(file);
        BufferedReader lineReader = new BufferedReader(in);
        String nextLine = null;
        nextLine = lineReader.readLine().trim();
        String[] dataArray = nextLine.split(" ");

        int numVertices = Integer.parseInt(dataArray[0]);
        int numTriangles = Integer.parseInt(dataArray[1]);
        logger.info("first line: " + nextLine);
        logger.info("vertices: " + numVertices);
        logger.info("faces:" + numTriangles);

        System.out.println("first line: " + nextLine);
        System.out.println("vertices: " + numVertices);
        System.out.println("faces:" + numTriangles);

        // compute min/max
//        TxT2Geometry loader = new TxT2Geometry();
//        VTriangleArray geometry = loader.loadAsVTriangleArray(file);
//        geometry.centerNodes();

//        Appearance app = AppearanceGenerator.getSolidAppearance(Color.red, true);
//        BranchGroup result = new BranchGroup();
//        result.addChild(new Shape3D(geometry.getTriangleArray(),app));

//        if (true) {
//            return result;
//        }
        
       

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numVertices; i++) {
            nextLine = lineReader.readLine().trim();
            StringTokenizer st = new StringTokenizer(nextLine);
            st.nextToken();
            sb.append('v').append(' ');
            sb.append(st.nextToken()).append(' ');
            sb.append(st.nextToken()).append(' ');
            sb.append(st.nextToken()).append('\n');
        }

        for (int i = 0; i < numTriangles; i++) {
            nextLine = lineReader.readLine().trim();
            StringTokenizer st = new StringTokenizer(nextLine);
            st.nextToken();
            int v1 = Integer.parseInt(st.nextToken()) + 1;
            int v2 = Integer.parseInt(st.nextToken()) + 1;
            int v3 = Integer.parseInt(st.nextToken()) + 1;
            sb.append('f').append(' ');
            sb.append(v1).append(' ');
            sb.append(v2).append(' ');
            sb.append(v3).append('\n');
        }

        Reader sr = new StringReader(sb.toString());
        int flags = ObjectFile.RESIZE | ObjectFile.STRIPIFY | ObjectFile.TRIANGULATE;
        ObjectFile f = new ObjectFile(flags, (float) (60.0 * Math.PI / 180.0));
        try {
            s = f.load(sr);
        } catch (FileNotFoundException e) {
            logger.error(e);
            System.exit(1);
        } catch (ParsingErrorException e) {
            logger.error(e);
            System.exit(1);
        } catch (IncorrectFormatException e) {
            logger.error(e);
            System.exit(1);
        }

        /*
         TransformGroup objScale = new TransformGroup();
         Transform3D t3d = new Transform3D();
         t3d.setScale(1.0);
         objScale.setTransform(t3d);
         s.getSceneGroup().addChild(objScale);
         * 
         */

        Node bg = s.getSceneGroup();//.cloneTree();
        
        Transform3D transRot = new Transform3D();
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        
        Vector3f cross2 = new Vector3f(1.0f, 0.0f, 0.0f);
        transRot.setRotation(new AxisAngle4f(cross2, (float) Math.toRadians(90)));
        tg.setTransform(transRot);
        
        tg.addChild(bg);
        
        BranchGroup result = new BranchGroup();
        result.addChild(tg);
        
        return result;
    }
//    protected void succeeded(Void result) {
//        System.gc();
//        NeuGenView ngView = NeuGenView.getInstance();
//        ngView.outPrintln(Utils.getMemoryStatus());
//        ngView.enableVisualizeDensity();
//        ngView.enableVisualize();
//        switch (dens) {
//            case IMAGE: {
//                ngView.enableWriteNeuGenBG();
//            }
//        }
//    }
}
