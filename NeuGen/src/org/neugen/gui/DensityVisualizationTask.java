/*
 * File: VisualizationDensityTask.java
 * Created on 27.07.2009, 12:00:22
 *
 */
package org.neugen.gui;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
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
import javax.swing.ImageIcon;
import javax.vecmath.Point3f;
import javax.vecmath.Point3i;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.Region;
import org.neugen.datastructures.VolumeOfVoxels;
import org.neugen.datastructures.VoxelVolume;
import org.neugen.datastructures.VoxelVolumeIterator;
import org.neugen.utils.NGImageIO;
import org.neugen.utils.Utils;

/**
 * @author Sergei Wolf
 */
public final class DensityVisualizationTask extends org.jdesktop.application.Task<Void, Void> {

    /** use to log messages */
    private static Logger logger = Logger.getLogger(DensityVisualizationTask.class.getName());
    private DensityVisualizationConfiguration ngVisConfig;
    private static DensityVisualizationTask instance;
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

    public enum Density {

        NET, IMAGE;
    }
    private Density dens;

    public DensityVisualizationTask(Application application, Density dens, File f) {
        super(application);
        this.dens = dens;
        this.dir = f.getParentFile();
        this.selectedFile = f;
    }

    public DensityVisualizationTask(Application application, Density dens) {
        super(application);
        this.dens = dens;
    }

    public static DensityVisualizationTask getInstance() {
        return instance;
    }

    public static void setInstance(Task instance) {
        DensityVisualizationTask.instance = (DensityVisualizationTask) instance;
    }

    public void setMyProgress(int value, int min, int max) {
        setProgress(value, min, max);
    }

    public void setMyProgress(float percentage) {
        if (Float.isInfinite(percentage)) {
            logger.info("percentage infinite: " + percentage);
            percentage = 1.0f;
        }
        setProgress(percentage);
    }

    public DensityVisualizationConfiguration getNgVisConfig() {
        return ngVisConfig;
    }

    @Override
    protected Void doInBackground() throws Exception {

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
                    float value = 0.0f;
                    int voxelArraySize = vv.getVoxelArray().size();
                    while (voxelVolumeIterator.hasNext()) {
                        value = voxelVolumeIterator.next();
                        volumeOfVoxels.setVoxelValue(thisindex, value); // same index
                        thisindex++;
                        setMyProgress(thisindex, 0, voxelArraySize);
                    }
                    ngVisConfig = new DensityVisualizationConfiguration(volumeOfVoxels);
                    DensityVisualizationConfiguration.setInstance(ngVisConfig);
                    ngVisConfig.setVisible(true);
                    ngVisConfig.setLocationRelativeTo(null);
                } else {
                    NeuGenView.getInstance().enableButtons();
                }
                break;
            }

            case IMAGE: {
                VoxelDialog vDialog = new VoxelDialog(NeuGenView.getInstance().getFrame(), true);
                vDialog.setVisible(true);
                vDialog.setLocationRelativeTo(null);
                if (vDialog.getReturnStatus() == VoxelDialog.RET_OK) {
                    //NGImageIO imageIO = new NGImageIO();
                    //List<ImageProducer> fileList = imageIO.getFileList(dir);
                    logger.info("image density task, dir path: " + dir.getAbsolutePath());

                    NeuGenView ngView = NeuGenView.getInstance();
                    boolean imageSequence = ngView.isImageSequence();
                    ArrayList<int[][]> dataList = new ArrayList<int[][]>();
                    //int numFiles = fileList.size();
                    //logger.info("anzahl bilder: " + numFiles);
                    int count = 0;
                    int width = 0;
                    int height= 0;
                    int stackSize = 0;

                    BranchGroup bg = null;
                    //ArrayList<ImageIcon> fileList = new ArrayList<ImageIcon>();
                    if (dir != null) {
                        File[] listFiles = dir.listFiles();
                        boolean stackLoaded = false;
                        for (File file : listFiles) {
                            if (NGImageIO.isImage(file.getName())) {

                                if(!imageSequence && !selectedFile.getName().equals(file.getName())) {
                                    continue;
                                }

                                ngView.outPrintln("image stack name: " + file.getName());
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
                        }
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

                    ngView.outPrintln("image stack size: " + dataList.size());
                    logger.info("image density task, anzahl bilder: " + dataList.size());
                    Point3i voxelLength = vDialog.getVoxelLenght();
                    logger.info("voxelLenght: " + voxelLength.toString());
                    int intensity = vDialog.getThreshold();
                    int weight = vDialog.getWeight();
                    float scannedImgHeight = vDialog.getImgHeight();
                    float scannedImgWidth = vDialog.getImgWidth();
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
                    if(stackSize > 0) {
                        float stackDepth = stackSize * scannedDepth;
                        logger.info("tiefe: " + stackDepth);
                        scaleZ = scannedDepth;
                        scaleZ /= 10.0f;
                    } else {
                        scaleZ = scannedDepth / faktor;
                        scaleZ /= 10.0f;
                    }

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

                    if (bg != null) {
                        ngVisConfig = new DensityVisualizationConfiguration(volumeOfVoxels, bg);
                    } else {
                        ngVisConfig = new DensityVisualizationConfiguration(volumeOfVoxels);
                    }

                    DensityVisualizationConfiguration.setInstance(ngVisConfig);
                    ngVisConfig.setVisible(true);
                    ngVisConfig.setLocationRelativeTo(null);
                } else {
                    NeuGenView.getInstance().enableButtons();
                }
                break;
            }
        }
        return null;
    }

    private BranchGroup getGeom(File file) throws FileNotFoundException, IOException {
        Reader in = new FileReader(file);
        BufferedReader lineReader = new BufferedReader(in);
        String nextLine = null;
        nextLine = lineReader.readLine().trim();
        String[] dataArray = nextLine.split(" ");
        int verticesVal = Integer.parseInt(dataArray[0]);
        int facesVal = Integer.parseInt(dataArray[1]);
        logger.info("first line: " + nextLine);
        logger.info("vertices: " + verticesVal);
        logger.info("faces:" + facesVal);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < verticesVal; i++) {
            nextLine = lineReader.readLine().trim();
            StringTokenizer st = new StringTokenizer(nextLine);
            st.nextToken();
            sb.append('v').append(' ');
            sb.append(st.nextToken()).append(' ');
            sb.append(st.nextToken()).append(' ');
            sb.append(st.nextToken()).append('\n');
        }

        for (int i = 0; i < facesVal; i++) {
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
        return (BranchGroup) bg;
    }

    @Override
    protected void succeeded(Void result) {
        System.gc();
        NeuGenView ngView = NeuGenView.getInstance();
        ngView.outPrintln(Utils.getMemoryStatus());
        ngView.enableVisualizeDensity();
        ngView.enableVisualize();
        switch(dens) {
            case IMAGE: {
                ngView.enableWriteNeuGenBG();
            }
        }
    }
}
