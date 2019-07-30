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
package org.neugen.vrl;
/*
 * NeuGenDensityVisualization.java
 *
 * Created on March 10, 2007
 *
 */

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;

import java.util.ArrayList;
import java.util.Iterator;

import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import org.neugen.datastructures.MiniVoxel;
import org.neugen.datastructures.VolumeOfVoxels;
import org.neugen.geometry3d.Cube3dCreator;
import org.neugen.geometry3d.Line3dCreator;
import org.neugen.geometry3d.Triangle3dCreator;
import org.neugen.quickhull3d.Point3dQH;
import org.neugen.quickhull3d.QuickHull3D;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.awt.GraphicsEnvironment;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;
import org.apache.log4j.Logger;
import org.neugen.datastructures.Region;
import org.neugen.makemovie.MovieMaker;
import com.sun.j3d.utils.pickfast.behaviors.*;
import com.sun.j3d.utils.scenegraph.io.SceneGraphFileWriter;
import java.awt.GraphicsConfigTemplate;
import java.awt.GraphicsDevice;
import java.awt.GridLayout;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.PickInfo;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.neugen.gui.NeuGenApp;
import org.neugen.visual.CanvasSynchronListener;
import org.neugen.visual.NeuGenVisualization;
import org.neugen.visual.SynchronBehavior;
import org.neugen.visual.Utils3D;
import org.neugen.vrl.VRLDensityVisualizationTask.Density;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
@SuppressWarnings("serial")
public final class VRLDensityVisualization {

    /**
     * use to log messages
     */
    private static Logger logger = Logger.getLogger(VRLDensityVisualization.class.getName());
    /**
     * if true visualization will be done with cubes
     */
    private boolean VisualizeWithCubes;
    /**
     * if true visualization will be done with convex hull
     */
    private boolean VisualizeWithConvexHull;
    /**
     * if true visualization will be done with a divided convex hull
     */
    private boolean VisualizeWithDividedConvexHull;
    /**
     * tolerance for the Divided Convex Hull
     */
    private double tolerance;
    /**
     * number of different values to be visualized at once (1-4)
     */
    private int numberOfVisualizations;
    /**
     * values to be visualized
     */
    private float value1;
    private float value2;
    private float value3;
    private float value4;
    private Color3f color1;
    private Color3f color2;
    private Color3f color3;
    private Color3f color4;// ColorUtil.midnightBlue;
    private float transparency0;
    private float transparency1;
    private float transparency2;
    private float transparency3;
    private float transparency4;
    private Color3f backgroundColor;// ColorUtil.white;
    /**
     * the simple universe containing all things to be visualized
     */
    private SimpleUniverse simpleUniverse;
    /**
     * a spherical bounding region which is defined by a center point and a
     * radius. Bounds objects define a convex, closed volume that is used for
     * various intersection and culling operations
     */
    private BoundingSphere boundingSphere;
    /**
     * Volume of Voxels containing the density values
     */
    private VolumeOfVoxels volumeOfVoxels;
//    private NeuGenView ngView;
    /**
     * size of panel
     */
    private static final int PWIDTH = 512;
    private static final int PHEIGHT = 512;
    private static final int BOUNDSIZE = 300;  // larger than world
    public float scale, scaleZ;
    private Canvas3D canvas3D;
//    private VRLDensityVisualizationTask task;
    private BranchGroup scene;
    private PropertyChangeListener guiListener;
    private static VRLDensityVisualization INSTANCE;
    private SynchronBehavior synBehavior;
    private CanvasSynchronListener canvasList;
    //private Transform3D rotate1 = new Transform3D();
    private PickRotateBehavior behavior1;
    private PickZoomBehavior behavior2;
    private PickTranslateBehavior behavior3;
    private boolean makeVideo = true;
    private RotationInterpolator rotator = null;
    private final DensityVisualizationParams densityParams;

    public static VRLDensityVisualization getInstance() {
        return INSTANCE;
    }

    private static void setInstance(VRLDensityVisualization INSTANCE) {
        VRLDensityVisualization.INSTANCE = INSTANCE;
    }

    public BranchGroup getScene() {
        return scene;
    }

    public void write(File f) {
        try {
            SceneGraphFileWriter s =
                    new SceneGraphFileWriter(
                    f, simpleUniverse, false, "Simple Test", null);
            s.writeBranchGraph(scene);
            s.close();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    public void setViewPosition(Vector3f mov, int angle) {
        TransformGroup tg = this.simpleUniverse.getViewingPlatform().
                getViewPlatformTransform();
        Transform3D t3dTrans = new Transform3D();
        tg.getTransform(t3dTrans);
        t3dTrans.setTranslation(mov);
        tg.setTransform(t3dTrans);

        Transform3D t3dRot = new Transform3D();
        tg.getTransform(t3dRot);
        t3dRot.setRotation(
                new AxisAngle4f(1f, 0f, 0f, (float) Math.toRadians(angle)));
        //ViewT3D.setRotation(new AxisAngle4f(0, 0, 1, (float) Math.toRadians(45)));
        tg.setTransform(t3dRot);
    }

    /**
     * Creates a new instance of NeuGenDensityVisualization. Create a simple
     * scene and attach it to the virtual universe public
     *
     * @param vov the Volume Of Voxels whichs values will be visualized
     */
    public VRLDensityVisualization(
            Canvas3D canvas3D,
            VisualizationContext context,
            DensityVisualizationParams densityParams) {

        if (getInstance() != null) {
            getInstance().destroy();
        }

        setInstance(this);

        this.densityParams = densityParams;
        this.scene = context.getBranchGroup();
        this.scale = context.getScale();
        this.scaleZ = context.getScaleZ();
        this.canvas3D = canvas3D;

        logger.info("scale:" + scale);

        System.out.println("SCALE: " + scale);
        System.out.println("SCALE-Z: " + scaleZ);

//        if (this.scene != null) {
//            Shape3D shape = (Shape3D) this.scene.getChild(0);
//            shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
//            shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
//            shape.setCapability(Shape3D.ENABLE_PICK_REPORTING);
//            //create an Appearance and Material
//            Appearance app = new Appearance();
//            app.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
//            app.setCapability(Appearance.ALLOW_MATERIAL_READ);
//            Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
//            Color3f objColor = new Color3f(1.0f, 0.7f, 0.8f);
//            Material mat = new Material(objColor, black, objColor, black, 100.0f);
//            app.setMaterial(mat);
//
//            PolygonAttributes pa = new PolygonAttributes();
//            pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
//            app.setPolygonAttributes(pa);
//
//
//            ColoringAttributes colorAtt = new ColoringAttributes();
//            colorAtt.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
//            app.setColoringAttributes(colorAtt);
//            //shape.setAppearance(app);
//            //scene.addChild(shape);
//
//            TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.NICEST, 0.8f);
//            app.setTransparencyAttributes(ta);
//            shape.setAppearance(app);
//
//            Transform3D trans3DTmp = new Transform3D();
//            Transform3D transRot = new Transform3D();
//            TransformGroup tg = new TransformGroup();
//            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
//            tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
//
//            TransformGroup spinTg = new TransformGroup();
//            spinTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//            spinTg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
//            spinTg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
//
//            //float x = (512.0f/2.0f + 90) * scale;
//            //float x = (512.0f / 2.0f + 75) * scale;
//            float x = (512.0f / 2.0f) * scale;
//            //float y = 10.0f * scale;
//            float y = 0.0f;
//            float z = (512.0f / 2.0f) * scale;
//            //float z = (512.0f / 2.0f + -68) * scale;
//            //float z = (512.0f / 2.0f - 200) * scale;
//            Point3f center = new Point3f(x, y, z);
//            Vector3f dir = new Vector3f(center);
//
//            Vector3f cross2 = new Vector3f(1.0f, 0.0f, 0.0f);
//            transRot.setRotation(new AxisAngle4f(cross2, (float) Math.toRadians(90)));
//            trans3DTmp.mul(transRot);
//
//            Vector3f cross3 = new Vector3f(0.0f, 0.0f, 1.0f);
//            transRot.setRotation(new AxisAngle4f(cross3, (float) Math.toRadians(180)));
//            //trans3DTmp.mul(transRot);
//
//            Vector3f cross1 = new Vector3f(0.0f, 1.0f, 0.0f);
//            transRot.setRotation(new AxisAngle4f(cross1, (float) Math.toRadians(90)));
//            //trans3DTmp.mul(transRot);
//            //trans3DTmp.setTranslation(dir);
//            tg.setTransform(trans3DTmp);
//
//            this.scene.removeChild(shape);
//            spinTg.addChild(shape);
//            tg.addChild(spinTg);
//            this.scene.addChild(tg);
//        }

        VisualizeWithCubes = densityParams.isVisualizeWithCubes();
        VisualizeWithConvexHull = densityParams.isVisualizeWithConvexHull();
        VisualizeWithDividedConvexHull = densityParams.isVisualizeWithDividedConvexHull();
        tolerance = densityParams.getTolerance() / 100.0;
        numberOfVisualizations = densityParams.getNumberOfVisualizations();

        value1 = densityParams.getValue1();
        value2 = densityParams.getValue2();
        value3 = densityParams.getValue3();
        value4 = densityParams.getValue4();

        color1 = densityParams.getColor1();
        color2 = densityParams.getColor2();
        color3 = densityParams.getColor3();
        color4 = densityParams.getColor4();

        backgroundColor = densityParams.getBackgroundColor();

        transparency1 = densityParams.getTransparency1() / 100f;
        transparency2 = densityParams.getTransparency2() / 100f;
        transparency3 = densityParams.getTransparency3() / 100f;
        transparency4 = densityParams.getTransparency4() / 100f;

        //task.setMyProgress(0.1f);

        volumeOfVoxels = context.getVolumeOfVoxels();
        //logger.info("number of voxels x: " + vov.getNumberOfVoxelsX());
        boundingSphere = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);

        GraphicsConfigTemplate3D gc3D = new GraphicsConfigTemplate3D();

        if (NeuGenApp.antiAliasing) {
            gc3D.setSceneAntialiasing(GraphicsConfigTemplate3D.PREFERRED);
        }

//        gc3D.setSceneAntialiasing(GraphicsConfigTemplate.PREFERRED);
//        GraphicsDevice gd[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
//        canvas3D = new Canvas3D(gd[0].getBestConfiguration(gc3D));
        simpleUniverse = new SimpleUniverse(canvas3D);


        synBehavior = new SynchronBehavior(simpleUniverse.getViewingPlatform().getViewPlatformTransform(), null);
        synBehavior.setSchedulingBounds(boundingSphere);

        orbitControls(canvas3D);

        if (this.scene == null) {
            this.scene = new BranchGroup();
            //Allow the BranchGroup to have children added and removed
            //at runtime
            scene.setCapability(Group.ALLOW_CHILDREN_EXTEND);
            scene.setCapability(Group.ALLOW_CHILDREN_READ);
            scene.setCapability(Group.ALLOW_CHILDREN_WRITE);
            scene.setCapability(BranchGroup.ALLOW_DETACH);
        }

        scene = createSceneGraph(false);


        switch (densityParams.getDensityType()) {
            case NET:
                Point3f upRightCorner = new Point3f(Region.getInstance().getUpRightCorner());
                Vector3f mov;
                if (upRightCorner != null) {
                    upRightCorner.scale(0.5f);
                    float zoom = scale / 0.001f;
                    float tmp = -25.0f / zoom;
                    if (tmp > -1.0f) {
                        tmp = -1.0f;
                    }

                    if (zoom == 1.0f) {
                        tmp = -25.0f;
                    }
                    mov = new Vector3f(upRightCorner.x, tmp * upRightCorner.y, upRightCorner.z);
                } else {
                    upRightCorner = new Point3f(300.0f, -1000.0f, 1000.0f);
                    mov = new Vector3f(upRightCorner);
                }
                mov.scale(scale);
                setViewPosition(mov, 90);
                break;
            case IMAGE:
                System.out.println(">> VRLDensityVisualization.IMAGE");
                //Verschiebe die Sicht nach hinten!
                //Vector3f mov2 = new Vector3f(200f, 0.0f, 300.0f);
                mov = new Vector3f(200.0f, 50.0f, 700.0f);
                mov.scale(scale);
//                mov2.scale(scale);
//                mov2.x *= scale;
//                mov2.y *= scale;
//                mov2.z *= scaleZ;
                setViewPosition(mov, 0);
                break;
        }

        behavior1 = new PickRotateBehavior(this.scene, this.canvas3D, this.boundingSphere);
        //scene.addChild(behavior1);

        behavior2 = new PickZoomBehavior(this.scene, this.canvas3D, this.boundingSphere);
        this.scene.addChild(behavior2);

        behavior3 = new PickTranslateBehavior(this.scene, this.canvas3D, this.boundingSphere);
        this.scene.addChild(behavior3);

        setPickMode(PickInfo.PICK_BOUNDS);
        setPickTolerance(0.0f);

        this.scene.compile();
        // BranchGroup scene = createSceneGraph(args.length > 0);
        //scene.compile();
        simpleUniverse.addBranchGraph(this.scene);

//        canvasParent.removeAll();
//        canvasParent.setLayout(new GridLayout());
//        canvasParent.add(canvas3D);





//        C3DScrollPane.getViewport().add(canvas3D);
//        C3DScrollPane.setBorder(null);
//        C3DScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        C3DScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
//
//        C3DScrollPane.setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
//        C3DSplitPane.setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
//        C3DSplitPane.setRightComponent(C3DScrollPane);
//        C3DSplitPane.setDividerLocation(PWIDTH);
//
//        guiListener = new VisualPropListener(canvas3D, C3DScrollPane, C3DSplitPane, this.getClass().toString());
//        C3DSplitPane.addPropertyChangeListener(guiListener);

        canvasList = new CanvasSynchronListener(synBehavior);
        canvas3D.addMouseListener(canvasList.getMouseListener());
        canvas3D.addMouseWheelListener(canvasList.getMouseWheelListener());
        canvas3D.addKeyListener(canvasList.getKeyListener());

        switch (densityParams.getDensityType()) {
            case NET:
                //VisualBehaviorListener.getInstance().setSimpleValide(simpleUniverse);
                SynchronBehavior visualSynB = NeuGenVisualization.getSynBehavior();
                if (visualSynB != null) {
                    visualSynB.setTransfornGroupTarget(simpleUniverse.getViewingPlatform().getViewPlatformTransform());
                    synBehavior.setTransfornGroupTarget(visualSynB.getTransformGroupSource());
                }
                break;
            case IMAGE:
                break;
        }


//        ngView.getVisualOutputSplitPane().addPropertyChangeListener(guiListener);
//        task.setMyProgress(1.0f);
//        ngView.enableButtons();
//        ngView.enableDensMovieButton();
//        ngView.outPrintln(Utils.getMemoryStatus());
//        ngView.outPrintln("end-visualize\n\n");
        System.gc();
    }

    private void setPickMode(int mode) {
        behavior1.setMode(mode);
        behavior2.setMode(mode);
        behavior3.setMode(mode);
    }

    private void setPickTolerance(float tolerance) {
        behavior1.setTolerance(tolerance);
        behavior2.setTolerance(tolerance);
        behavior3.setTolerance(tolerance);
    }

    public void makeVideo() {
        MovieMaker.generateVideo(canvas3D, simpleUniverse, 10000, // Animation time in msec
                new Alpha(-1, 32000), new File("MovieDensNuc.mov"), 15, 1920, 1200);

    }

    /**
     * allows for navigation with the mouse
     *
     * @param canvas3D the canvas through which shall be navigated
     */
    private void orbitControls(Canvas3D canvas3D) {
        //logger.info("orbitControls");
        OrbitBehavior orbit = new OrbitBehavior(canvas3D, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(boundingSphere);
        ViewingPlatform vp = simpleUniverse.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
    }

    public void destroy() {
        // Cleanup reference to Java3D
        if (canvas3D == null || canvasList == null || simpleUniverse == null) {
            return;
        }
        synBehavior = null;

        behavior1 = null;
        behavior2 = null;
        behavior3 = null;
        try {
            canvas3D.removeMouseListener(canvasList.getMouseListener());
            canvas3D.removeKeyListener(canvasList.getKeyListener());
            canvas3D.removeMouseWheelListener(canvasList.getMouseWheelListener());

            simpleUniverse.getViewer().getView().removeAllCanvas3Ds();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        canvasList = null;
        simpleUniverse.cleanup();
        canvas3D = null;
        scene = null;
        boundingSphere = null;
    }

    private TransformGroup createSpinner() {
        TransformGroup spinner = new TransformGroup();
        spinner.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        return spinner;
    }

    private TransformGroup moveBack() {
        Transform3D transform3D = new Transform3D();
        transform3D.setRotation(new AxisAngle4f(1f, 0f, 0f, (float) Math.toRadians(270)));
        return new TransformGroup(transform3D);
    }

    public void createRegion(TransformGroup objRoot) {
        if (Region.isCortColumn()) {
            Point3f upRightCorner = new Point3f(Region.getInstance().getUpRightCorner());
            upRightCorner.scale(scale);
            upRightCorner.setZ(upRightCorner.z / 2.0f);
            upRightCorner.setY(upRightCorner.y / 2.0f);
            Utils3D.addColumn(objRoot, scale);
            Utils3D.addCoordinateSphereAxesToSceneGraph(objRoot, scale, null);
            //logger.info("upRightCorner: " + upRightCorner.toString());
            Utils3D.addScaleBarToSceneGraph(objRoot, upRightCorner, scale);
        } else if (Region.isCa1Region()) {
            Point3f upRightCorner = new Point3f(Region.getInstance().getUpRightCorner());
            upRightCorner.scale(scale);
            upRightCorner.setZ(upRightCorner.z / 2.0f);
            upRightCorner.setY(upRightCorner.y / 2.0f);
            Utils3D.addCA1Region(objRoot, scale);
            Utils3D.addCoordinateSphereAxesToSceneGraph(objRoot, scale, null);
            Utils3D.addScaleBarToSceneGraph(objRoot, upRightCorner, scale);
        } else {
            //Utils3D.addCoordinateSphereAxesToSceneGraph(objRoot, 0.01f, new Point3f(-0.5f, 0, 0));
            //Utils3D.addScaleBarToSceneGraph(objRoot, new Point3f(0.5f, 0, 0.5f));
        }
    }

    private void setLighting(TransformGroup objMove) {
        // Set up the ambient light
        Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(boundingSphere);
        objMove.addChild(ambientLightNode);

        // Set up the directional lights
        Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
        Vector3f light1Direction = new Vector3f(1.0f, 1.0f, 1.0f);

        //light1Direction.scale(scale * 10.0f);
        Color3f light2Color = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f light2Direction = new Vector3f(-1.0f, -1.0f, -1.0f);
        //light2Direction.scale(scale * 10.0f);

        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(boundingSphere);
        objMove.addChild(light1);

        DirectionalLight light2 = new DirectionalLight(light2Color, light2Direction);
        light2.setInfluencingBounds(boundingSphere);
        objMove.addChild(light2);
    }

    private TransformGroup getRotation() {

        /*
         * Create a TransformGroup to rotate the objects in the scene
         * and set the capability bits on the TransformGroup so that
         * it can be modified at runtime by the rotation behavior.
         */
        TransformGroup objTrans = new TransformGroup();
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        //Create a spherical bounding volume that will define the volume
        //within which the rotation behavior is active.
        BoundingSphere bounds = new BoundingSphere(
                new Point3d(0.0, 0.0, 0.0), 100.0);

        //Create a 4 × 4 transformation matrix
        Transform3D yAxis = new Transform3D();
        double rot = Math.toRadians(90);
        yAxis.rotZ(rot);

        /*
         * Create an Alpha interpolator to automatically generate
         * modifications to the rotation component of the transformation
         * matrix. This Alpha loops indefinitely and generates numbers
         * from 0 to 1 every 4000 milliseconds.
         */
        /*
         Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE,
         0, 0,
         32000, 0, 0,
         0, 0, 0);
         *
         */

        Alpha rotationAlpha = new Alpha(-1, 0);
        /*
         * Create a RotationInterpolator behavior to effect the
         * TransformGroup. Here we will rotate from 0 to 2p degrees about
         * the Y-axis based on the output of rotationAlpha.
         */
        rotator = new RotationInterpolator(rotationAlpha,
                objTrans, yAxis, 0.0f,
                (float) Math.PI * 2.0f);

        //Set the scheduling bounds on the behavior. This defines the
        //volume within which this behavior will be active.
        rotator.setSchedulingBounds(bounds);

        //Add the behavior to the scenegraph so that Java 3D
        //can schedule it for activation.
        objTrans.addChild(rotator);
        return objTrans;
    }

    /*
     private RotationInterpolator makeSpin(TransformGroup spinner) {
     RotationInterpolator rotator = new RotationInterpolator(new Alpha(-1, 0), spinner);
     //rotator.setTransformAxis(rotate());
     double rot = Math.toRadians(90);
     Transform3D rotate1 = new Transform3D();
     rotate1.rotX(rot);
     //this.rotate1.setTranslation(new Vector3f(0.3f, 0.3f, 0.0f));
     rotator.setTransformAxis(rotate1);
     rotator.setSchedulingBounds(boundingSphere);
     return rotator;
     }
     *
     */
    private void addBackground(BranchGroup objRoot) {
        Background background = new Background(backgroundColor);
        background.setApplicationBounds(boundingSphere);
        objRoot.addChild(background);
    }

    /**
     * create scene graph branch group
     *
     * @param wireFrame false (not used)
     * @return contentRoot the BranchGroup containing all things to be
     * visualized
     */
    @SuppressWarnings("unchecked")
    public BranchGroup createSceneGraph(boolean wireFrame) {
        BranchGroup objRoot = new BranchGroup();
        objRoot.setCapability(BranchGroup.ALLOW_DETACH);
        TransformGroup tg = new TransformGroup();
        TransformGroup spinner = new TransformGroup();
        //spinner.addChild(createTextShape());

        //video
        if (makeVideo) {
            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            //spinner.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            //spinner.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            //spinner.addChild(makeSpin(spinner));
            spinner = getRotation();
        }

        //tg.addChild(spinner);
        scene.addChild(tg);

        setLighting(tg);
        addBackground(scene);
        createRegion(tg);

        scene.addChild(synBehavior);
        TransformGroup vpTrans = simpleUniverse.getViewingPlatform().getViewPlatformTransform();
        KeyNavigatorBehavior keybehavior = new KeyNavigatorBehavior(vpTrans);
        keybehavior.setSchedulingBounds(boundingSphere);
        scene.addChild(keybehavior);

        if (logger.isDebugEnabled()) {
            logger.info(this.volumeOfVoxels.getMidpointX());
            logger.info(this.volumeOfVoxels.getMidpointY());
            logger.info(this.volumeOfVoxels.getMidpointZ());
            logger.info(this.volumeOfVoxels.getNumberOfVoxelsX());
            logger.info(this.volumeOfVoxels.getNumberOfVoxelsY());
            logger.info(this.volumeOfVoxels.getNumberOfVoxelsZ());
            logger.info(this.volumeOfVoxels.getBiggestVoxelValue());
            logger.info(this.volumeOfVoxels.getNumberOfVoxelsRadius());
            logger.info(this.volumeOfVoxels.getRadius());
            logger.info(this.volumeOfVoxels.getTotalNumberOfVoxels());
        }

        // the MiniVoxelArrayLists
        List arrayListOfMiniVoxelsWhichAddUpTo1 = null;
        List arrayListOfMiniVoxelsWhichAddUpTo2 = null;
        List arrayListOfMiniVoxelsWhichAddUpTo3 = null;
        List arrayListOfMiniVoxelsWhichAddUpTo4 = null;

        if ((numberOfVisualizations == 1) || (numberOfVisualizations == 2)
                || (numberOfVisualizations == 3) || (numberOfVisualizations == 4)) {
            //logger.info("1. if");
            // get the MiniVoxelArrayList from the VolumeOfVoxels
            List<MiniVoxel> miniVoxelArrayList1 = this.volumeOfVoxels.getMiniVoxelArrayList(50.0f, 50.0f);
            // sort the MiniVoxelArrayList MiniVoxels with biggest values first
            this.volumeOfVoxels.getOrderedMiniVoxelArrayList(miniVoxelArrayList1);
            // get all MiniVoxels with biggest values which add up to a certain value
            arrayListOfMiniVoxelsWhichAddUpTo1 = this.volumeOfVoxels.getArrayListOfMiniVoxelsWhichAddUpTo(miniVoxelArrayList1, value1);
        }
        if ((numberOfVisualizations == 2) || (numberOfVisualizations == 3)
                || (numberOfVisualizations == 4)) {
            //logger.info("2. if");
            // get the MiniVoxelArrayList from the VolumeOfVoxels
            List<MiniVoxel> miniVoxelArrayList2 = this.volumeOfVoxels.getMiniVoxelArrayList(50.0f, 50.0f);
            // sort the MiniVoxelArrayList MiniVoxels with biggest values first
            this.volumeOfVoxels.getOrderedMiniVoxelArrayList(miniVoxelArrayList2);
            // get all MiniVoxels with biggest values which add up to a certain value
            arrayListOfMiniVoxelsWhichAddUpTo2 = this.volumeOfVoxels.getArrayListOfMiniVoxelsWhichAddUpTo(miniVoxelArrayList2, value2);
        }
        if ((numberOfVisualizations == 3) || (numberOfVisualizations == 4)) {
            //logger.info("3. if");
            // get the MiniVoxelArrayList from the VolumeOfVoxels
            List<MiniVoxel> miniVoxelArrayList3 = this.volumeOfVoxels.getMiniVoxelArrayList(50.0f, 50.0f);
            // sort the MiniVoxelArrayList MiniVoxels with biggest values first
            this.volumeOfVoxels.getOrderedMiniVoxelArrayList(miniVoxelArrayList3);
            // get all MiniVoxels with biggest values which add up to a certain value
            arrayListOfMiniVoxelsWhichAddUpTo3 = this.volumeOfVoxels.getArrayListOfMiniVoxelsWhichAddUpTo(miniVoxelArrayList3, value3);
        }
        if (numberOfVisualizations == 4) {
            //logger.info("4. if");
            // get the MiniVoxelArrayList from the VolumeOfVoxels
            List<MiniVoxel> miniVoxelArrayList4 = this.volumeOfVoxels.getMiniVoxelArrayList(50.0f, 50.0f);
            // sort the MiniVoxelArrayList MiniVoxels with biggest values first
            this.volumeOfVoxels.getOrderedMiniVoxelArrayList(miniVoxelArrayList4);
            // get all MiniVoxels with biggest values which add up to a certain value
            arrayListOfMiniVoxelsWhichAddUpTo4 = this.volumeOfVoxels.getArrayListOfMiniVoxelsWhichAddUpTo(miniVoxelArrayList4, value4);
        }

        // length of Voxels
        int VOV_X = this.volumeOfVoxels.getNumberOfVoxelsX();
        int VOV_Y = this.volumeOfVoxels.getNumberOfVoxelsY();
        int VOV_Z = this.volumeOfVoxels.getNumberOfVoxelsZ();

        Point3f regionUpRightCorner = Region.getInstance().getUpRightCorner();
        float length = regionUpRightCorner.x * scale;
        //float width = regionUpRightCorner.y * scale;
        float height = regionUpRightCorner.z * scale;

        float width = regionUpRightCorner.y * scaleZ;

        logger.info("lenght of voxels: " + VOV_X);
        logger.info("cube lenght: " + width / VOV_X);

        float cubeLength = length / VOV_X;
        float cubeWidth = width / VOV_Y;
        float cubeHeight = height / VOV_Z;

        // visualization with cubes    
        if (VisualizeWithCubes == true) {
            BranchGroup cubesBG = new BranchGroup();
            cubesBG.setCapability(BranchGroup.ALLOW_DETACH);
            
            logger.info("visualization with cubes");
            if ((numberOfVisualizations == 1) || (numberOfVisualizations == 2) || (numberOfVisualizations == 3) || (numberOfVisualizations == 4)) {
                logger.info("visualization with cubes: number of visual 1-4 (color: )" + color1.toString());
                Material mat = new Material();
                mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
                mat.setEmissiveColor(color1);
                mat.setDiffuseColor(color1);
                mat.setAmbientColor(color1);
                mat.setSpecularColor(color1);
                mat.setShininess(80.0f);
                Cube3dCreator cube3dCreator1 = new Cube3dCreator(cubeLength, cubeWidth, cubeHeight, mat, this.transparency1);
                Iterator arrayListOfMiniVoxelsWhichAddUpTo1Iterator = arrayListOfMiniVoxelsWhichAddUpTo1.iterator();
                while (arrayListOfMiniVoxelsWhichAddUpTo1Iterator.hasNext()) {
                    cube3dCreator1.addCubeToContainer((MiniVoxel) arrayListOfMiniVoxelsWhichAddUpTo1Iterator.next());
                }
                cubesBG.addChild(cube3dCreator1.getCubeContainer());
            }
            if ((numberOfVisualizations == 2) || (numberOfVisualizations == 3) || (numberOfVisualizations == 4)) {
                logger.info("visualization with cubes: number of visual 2-4");
                Material mat = new Material();
                mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
                mat.setEmissiveColor(color2);
                mat.setDiffuseColor(color2);
                mat.setAmbientColor(color2);
                mat.setSpecularColor(color2);
                mat.setShininess(80.0f);
                Cube3dCreator cube3dCreator2 = new Cube3dCreator(cubeLength, cubeWidth, cubeHeight, mat, transparency2);
                Iterator arrayListOfMiniVoxelsWhichAddUpTo2Iterator = arrayListOfMiniVoxelsWhichAddUpTo2.iterator();
                while (arrayListOfMiniVoxelsWhichAddUpTo2Iterator.hasNext()) {
                    cube3dCreator2.addCubeToContainer((MiniVoxel) arrayListOfMiniVoxelsWhichAddUpTo2Iterator.next());
                }
                cubesBG.addChild(cube3dCreator2.getCubeContainer());
            }
            if ((numberOfVisualizations == 3) || (numberOfVisualizations == 4)) {
                logger.info("visualization with cubes: number of visual 3-4");
                Material mat = new Material();
                mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
                mat.setEmissiveColor(color3);
                mat.setDiffuseColor(color3);
                mat.setAmbientColor(color3);
                mat.setSpecularColor(color3);
                mat.setShininess(80.0f);
                Cube3dCreator cube3dCreator3 = new Cube3dCreator(cubeLength, cubeWidth, cubeHeight, mat, transparency3);
                Iterator arrayListOfMiniVoxelsWhichAddUpTo3Iterator = arrayListOfMiniVoxelsWhichAddUpTo3.iterator();
                while (arrayListOfMiniVoxelsWhichAddUpTo3Iterator.hasNext()) {
                    cube3dCreator3.addCubeToContainer((MiniVoxel) arrayListOfMiniVoxelsWhichAddUpTo3Iterator.next());
                }
                cubesBG.addChild(cube3dCreator3.getCubeContainer());
            }
            if (numberOfVisualizations == 4) {
                logger.info("visualization with cubes: number of visual 4");
                Material mat = new Material();
                mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
                mat.setEmissiveColor(color4);
                mat.setDiffuseColor(color4);
                mat.setAmbientColor(color4);
                mat.setSpecularColor(color4);
                mat.setShininess(80.0f);
                Cube3dCreator cube3dCreator4 = new Cube3dCreator(cubeLength, cubeWidth, cubeHeight, mat, transparency4);
                Iterator arrayListOfMiniVoxelsWhichAddUpTo4Iterator = arrayListOfMiniVoxelsWhichAddUpTo4.iterator();
                while (arrayListOfMiniVoxelsWhichAddUpTo4Iterator.hasNext()) {
                    cube3dCreator4.addCubeToContainer((MiniVoxel) arrayListOfMiniVoxelsWhichAddUpTo4Iterator.next());
                }
                cubesBG.addChild(cube3dCreator4.getCubeContainer());
            }
            scene.addChild(cubesBG);
        }
        // visualization with convex hull
        if (VisualizeWithConvexHull == true) {
            BranchGroup chBG = new BranchGroup();
            chBG.setCapability(BranchGroup.ALLOW_DETACH);
            logger.info("visualization with convex hull");
            if ((numberOfVisualizations == 1) || (numberOfVisualizations == 2) || (numberOfVisualizations == 3) || (numberOfVisualizations == 4)) {
                Triangle3dCreator triangle3dCreator1 = new Triangle3dCreator(this.color1);
                Line3dCreator line3dCreator1 = new Line3dCreator(Utils3D.black);
                Material mat = new Material();
                mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
                mat.setEmissiveColor(Utils3D.black);
                mat.setShininess(80.0f);
                Point3f cubeSize = new Point3f(5.0f, 5.0f, 5.0f);
                cubeSize.scale(scale);
                //Cube3dCreator cube3dCreator1 = new Cube3dCreator(0.005f, 0.005f, 0.005f, mat, this.transparency0);
                Cube3dCreator cube3dCreator1 = new Cube3dCreator(cubeSize.x, cubeSize.y, cubeSize.z, mat, this.transparency0);
                this.computeConvexHull(arrayListOfMiniVoxelsWhichAddUpTo1, triangle3dCreator1, line3dCreator1, cube3dCreator1);
                chBG.addChild(cube3dCreator1.getCubeContainer());
                chBG.addChild(triangle3dCreator1.getTriangleContainer());
                chBG.addChild(line3dCreator1.getLineContainer());
            }
            if ((numberOfVisualizations == 2) || (numberOfVisualizations == 3) || (numberOfVisualizations == 4)) {
                Triangle3dCreator triangle3dCreator2 = new Triangle3dCreator(this.color2);
                Line3dCreator line3dCreator2 = new Line3dCreator(Utils3D.black);
                Material mat = new Material();
                mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
                mat.setEmissiveColor(Utils3D.black);
                mat.setShininess(80.0f);
                Point3f cubeSize = new Point3f(5.0f, 5.0f, 5.0f);
                cubeSize.scale(scale);
                //Cube3dCreator cube3dCreator1 = new Cube3dCreator(0.005f, 0.005f, 0.005f, mat, this.transparency0);
                Cube3dCreator cube3dCreator2 = new Cube3dCreator(cubeSize.x, cubeSize.y, cubeSize.z, mat, this.transparency1);
                //Cube3dCreator cube3dCreator2 = new Cube3dCreator(0.005f, 0.005f, 0.005f, mat, this.transparency0);
                this.computeConvexHull(arrayListOfMiniVoxelsWhichAddUpTo2, triangle3dCreator2, line3dCreator2, cube3dCreator2);
                chBG.addChild(cube3dCreator2.getCubeContainer());
                chBG.addChild(triangle3dCreator2.getTriangleContainer());
                chBG.addChild(line3dCreator2.getLineContainer());
            }
            if ((numberOfVisualizations == 3) || (numberOfVisualizations == 4)) {
                Triangle3dCreator triangle3dCreator3 = new Triangle3dCreator(this.color3);
                Line3dCreator line3dCreator3 = new Line3dCreator(Utils3D.black);
                Material mat = new Material();
                mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
                mat.setEmissiveColor(Utils3D.black);
                mat.setShininess(80.0f);
                Point3f cubeSize = new Point3f(5.0f, 5.0f, 5.0f);
                cubeSize.scale(scale);
                //Cube3dCreator cube3dCreator1 = new Cube3dCreator(0.005f, 0.005f, 0.005f, mat, this.transparency0);
                Cube3dCreator cube3dCreator3 = new Cube3dCreator(cubeSize.x, cubeSize.y, cubeSize.z, mat, this.transparency2);
                //Cube3dCreator cube3dCreator3 = new Cube3dCreator(0.005f, 0.005f, 0.005f, mat, this.transparency0);
                this.computeConvexHull(arrayListOfMiniVoxelsWhichAddUpTo3, triangle3dCreator3, line3dCreator3, cube3dCreator3);
                chBG.addChild(cube3dCreator3.getCubeContainer());
                chBG.addChild(triangle3dCreator3.getTriangleContainer());
                chBG.addChild(line3dCreator3.getLineContainer());
            }
            if (numberOfVisualizations == 4) {
                Triangle3dCreator triangle3dCreator4 = new Triangle3dCreator(this.color4);
                Line3dCreator line3dCreator4 = new Line3dCreator(Utils3D.black);
                Material mat = new Material();
                mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
                mat.setEmissiveColor(Utils3D.black);
                mat.setShininess(80.0f);
                Point3f cubeSize = new Point3f(5.0f, 5.0f, 5.0f);
                cubeSize.scale(scale);
                //Cube3dCreator cube3dCreator1 = new Cube3dCreator(0.005f, 0.005f, 0.005f, mat, this.transparency0);
                Cube3dCreator cube3dCreator4 = new Cube3dCreator(cubeSize.x, cubeSize.y, cubeSize.z, mat, this.transparency3);
                //Cube3dCreator cube3dCreator4 = new Cube3dCreator(0.005f, 0.005f, 0.005f, mat, this.transparency0);
                this.computeConvexHull(arrayListOfMiniVoxelsWhichAddUpTo4, triangle3dCreator4, line3dCreator4, cube3dCreator4);
                chBG.addChild(cube3dCreator4.getCubeContainer());
                chBG.addChild(triangle3dCreator4.getTriangleContainer());
                chBG.addChild(line3dCreator4.getLineContainer());
            }
            scene.addChild(chBG);
        }
        // visualization with divided convex hull
        if (VisualizeWithDividedConvexHull == true) {
            BranchGroup dchBG = new BranchGroup();
            dchBG.setCapability(BranchGroup.ALLOW_DETACH);
            logger.info("visualization with divided convex hull");
            if ((numberOfVisualizations == 1) || (numberOfVisualizations == 2) || (numberOfVisualizations == 3) || (numberOfVisualizations == 4)) {
                Triangle3dCreator triangle3dCreator1;
                Line3dCreator line3dCreator1;
                Cube3dCreator cube3dCreator1;
                List dividedMiniVoxelArrayList1 = this.volumeOfVoxels.getDivideMiniVoxelArrayList(arrayListOfMiniVoxelsWhichAddUpTo1, tolerance);
                Iterator dividedMiniVoxelArrayListIterator1 = dividedMiniVoxelArrayList1.iterator();
                while (dividedMiniVoxelArrayListIterator1.hasNext()) {
                    List partCloud1 = (ArrayList) dividedMiniVoxelArrayListIterator1.next();
                    if (!partCloud1.isEmpty()) {
                        try {
                            triangle3dCreator1 = new Triangle3dCreator(this.color1);
                            line3dCreator1 = new Line3dCreator(Utils3D.black);
                            Material mat = new Material();
                            mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
                            mat.setEmissiveColor(Utils3D.black);
                            Point3f cubeSize = new Point3f(5.0f, 5.0f, 5.0f);
                            cubeSize.scale(scale);
                            //Cube3dCreator cube3dCreator1 = new Cube3dCreator(0.005f, 0.005f, 0.005f, mat, this.transparency0);
                            cube3dCreator1 = new Cube3dCreator(cubeSize.x, cubeSize.y, cubeSize.z, mat, this.transparency0);
                            //cube3dCreator1 = new Cube3dCreator(0.005f, 0.005f, 0.005f, mat, this.transparency0);
                            this.computeConvexHull(partCloud1, triangle3dCreator1, line3dCreator1, cube3dCreator1);
                            dchBG.addChild(triangle3dCreator1.getTriangleContainer());
                            dchBG.addChild(line3dCreator1.getLineContainer());
                            dchBG.addChild(cube3dCreator1.getCubeContainer());
                        } catch (Exception e) {
                            logger.error(e, e);
                        }
                    }
                }
            }
            if ((numberOfVisualizations == 2) || (numberOfVisualizations == 3) || (numberOfVisualizations == 4)) {
                Triangle3dCreator triangle3dCreator2;
                Line3dCreator line3dCreator2;
                Cube3dCreator cube3dCreator2;
                List dividedMiniVoxelArrayList2 = this.volumeOfVoxels.getDivideMiniVoxelArrayList(arrayListOfMiniVoxelsWhichAddUpTo2, tolerance);
                Iterator dividedMiniVoxelArrayListIterator2 = dividedMiniVoxelArrayList2.iterator();
                while (dividedMiniVoxelArrayListIterator2.hasNext()) {
                    List partCloud2 = (ArrayList) dividedMiniVoxelArrayListIterator2.next();
                    if (!partCloud2.isEmpty()) {
                        try {
                            triangle3dCreator2 = new Triangle3dCreator(this.color2);
                            line3dCreator2 = new Line3dCreator(Utils3D.black);
                            Material mat = new Material();
                            mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
                            mat.setEmissiveColor(Utils3D.black);
                            Point3f cubeSize = new Point3f(5.0f, 5.0f, 5.0f);
                            cubeSize.scale(scale);
                            //Cube3dCreator cube3dCreator1 = new Cube3dCreator(0.005f, 0.005f, 0.005f, mat, this.transparency0);
                            cube3dCreator2 = new Cube3dCreator(cubeSize.x, cubeSize.y, cubeSize.z, mat, this.transparency0);
                            //cube3dCreator2 = new Cube3dCreator(0.005f, 0.005f, 0.005f, mat, this.transparency0);
                            this.computeConvexHull(partCloud2, triangle3dCreator2, line3dCreator2, cube3dCreator2);
                            dchBG.addChild(triangle3dCreator2.getTriangleContainer());
                            dchBG.addChild(line3dCreator2.getLineContainer());
                            dchBG.addChild(cube3dCreator2.getCubeContainer());
                        } catch (Exception e) {
                            logger.error(e, e);
                        }
                    }
                }
            }
            if ((numberOfVisualizations == 3) || (numberOfVisualizations == 4)) {
                Triangle3dCreator triangle3dCreator3;
                Line3dCreator line3dCreator3;
                Cube3dCreator cube3dCreator3;
                List dividedMiniVoxelArrayList3 = this.volumeOfVoxels.getDivideMiniVoxelArrayList(arrayListOfMiniVoxelsWhichAddUpTo3, tolerance);
                Iterator dividedMiniVoxelArrayListIterator3 = dividedMiniVoxelArrayList3.iterator();
                while (dividedMiniVoxelArrayListIterator3.hasNext()) {
                    List partCloud3 = (ArrayList) dividedMiniVoxelArrayListIterator3.next();
                    if (!partCloud3.isEmpty()) {
                        try {
                            triangle3dCreator3 = new Triangle3dCreator(this.color3);
                            line3dCreator3 = new Line3dCreator(Utils3D.black);
                            Material mat = new Material();
                            mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
                            mat.setEmissiveColor(Utils3D.black);
                            Point3f cubeSize = new Point3f(5.0f, 5.0f, 5.0f);
                            cubeSize.scale(scale);
                            //Cube3dCreator cube3dCreator1 = new Cube3dCreator(0.005f, 0.005f, 0.005f, mat, this.transparency0);
                            cube3dCreator3 = new Cube3dCreator(cubeSize.x, cubeSize.y, cubeSize.z, mat, this.transparency0);
                            //cube3dCreator3 = new Cube3dCreator(0.005f, 0.005f, 0.005f, mat, this.transparency0);
                            this.computeConvexHull(partCloud3, triangle3dCreator3, line3dCreator3, cube3dCreator3);
                            dchBG.addChild(cube3dCreator3.getCubeContainer());
                            dchBG.addChild(triangle3dCreator3.getTriangleContainer());
                            dchBG.addChild(line3dCreator3.getLineContainer());
                        } catch (Exception e) {
                            logger.error(e, e);
                        }
                    }
                }
            }
            if (numberOfVisualizations == 4) {
                Triangle3dCreator triangle3dCreator4;
                Line3dCreator line3dCreator4;
                Cube3dCreator cube3dCreator4;
                List dividedMiniVoxelArrayList4 = this.volumeOfVoxels.getDivideMiniVoxelArrayList(arrayListOfMiniVoxelsWhichAddUpTo4, tolerance);
                Iterator dividedMiniVoxelArrayListIterator4 = dividedMiniVoxelArrayList4.iterator();
                while (dividedMiniVoxelArrayListIterator4.hasNext()) {
                    List partCloud4 = (ArrayList) dividedMiniVoxelArrayListIterator4.next();
                    if (!partCloud4.isEmpty()) {
                        try {
                            triangle3dCreator4 = new Triangle3dCreator(this.color4);
                            line3dCreator4 = new Line3dCreator(Utils3D.black);
                            Material mat = new Material();
                            mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
                            mat.setEmissiveColor(Utils3D.black);
                            Point3f cubeSize = new Point3f(5.0f, 5.0f, 5.0f);
                            cubeSize.scale(scale);
                            //Cube3dCreator cube3dCreator1 = new Cube3dCreator(0.005f, 0.005f, 0.005f, mat, this.transparency0);
                            cube3dCreator4 = new Cube3dCreator(cubeSize.x, cubeSize.y, cubeSize.z, mat, this.transparency0);
                            //cube3dCreator4 = new Cube3dCreator(0.005f, 0.005f, 0.005f, mat, this.transparency0);
                            this.computeConvexHull(partCloud4, triangle3dCreator4, line3dCreator4, cube3dCreator4);
                            dchBG.addChild(cube3dCreator4.getCubeContainer());
                            dchBG.addChild(triangle3dCreator4.getTriangleContainer());
                            dchBG.addChild(line3dCreator4.getLineContainer());
                        } catch (Exception e) {
                            logger.error(e, e);
                        }
                    }
                }
            }
            scene.addChild(dchBG);
        }

        spinner.addChild(scene);
        objRoot.addChild(spinner);
        return objRoot;
    }

    public TransformGroup rotateNode(Node n) {
        Transform3D transRot = new Transform3D();
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

        Vector3f cross2 = new Vector3f(1.0f, 0.0f, 0.0f);
        transRot.setRotation(new AxisAngle4f(cross2, (float) Math.toRadians(90)));
        tg.setTransform(transRot);
        return tg;
    }

    /**
     * method for computing the convex hull
     *
     * @param miniVoxelArrayList list containing coordinates and values
     * @param triangle3dCreator creator for triangles for visualizing the
     * surface of the convex hull
     * @param line3dCreator creator for lines surrounding the triangles
     * @param cube3dCreator creator for cubes marking the points of the convex
     * hull
     */
    @SuppressWarnings("unchecked")
    private void computeConvexHull(List<MiniVoxel> miniVoxelArrayList,
            Triangle3dCreator triangle3dCreator, Line3dCreator line3dCreator, Cube3dCreator cube3dCreator) {
        logger.info("compute convex hull!");
        // ArrayList containing the points for the computation of the convex hull
        List<Point3dQH> pointCloud = new ArrayList<Point3dQH>();
        // Iterator for iterating over the ArrayList of MiniVoxels
        Iterator miniVoxelArrayListIterator = miniVoxelArrayList.iterator();
        // extract point coordinates from MiniVoxels and add them to pointCloud int i = 0;
        while (miniVoxelArrayListIterator.hasNext()) {
            MiniVoxel miniVoxel = (MiniVoxel) miniVoxelArrayListIterator.next();
            double x = miniVoxel.getXCoord();
            double y = miniVoxel.getYCoord();
            double z = miniVoxel.getZCoord();
            Point3dQH pointOfPointCloud = new Point3dQH(x, y, z);
            pointCloud.add(pointOfPointCloud);
        }
        // point array to give to QuickHull3D
        Point3dQH[] points = new Point3dQH[pointCloud.size()];
        // Iterator for iterating over the points of the point cloud
        Iterator pointCloudIterator = pointCloud.iterator();
        // // print Point3D data of pointCloud
        // while (pointCloudIterator.hasNext())
        // {
        // System.out.println("((Point3D)pointCloudIterator.next()).dataToString():
        // " +
        // ((Point3D) pointCloudIterator.next()).dataToString());
        // }
        // copy points of point cloud in point array
        int pointIndex = 0;
        while (pointCloudIterator.hasNext()) {
            Point3dQH pointOfPointCloud = ((Point3dQH) pointCloudIterator.next());
            // System.out.println("coords: " + "xCoord: " + xCoord +
            // " yCoord: " + yCoord + " zCoord: " + zCoord);
            // System.out.println("pointIndex: " + pointIndex);
            // Point3dQH of quickhull
            points[pointIndex] = pointOfPointCloud;
            pointIndex++;
        }
        QuickHull3D hull = new QuickHull3D();
        // build hull with points of point cloud
        hull.build(points);
        // triangulate hull in case of merged faces
        hull.triangulate();
        // ArrayLists containing the indices of of the faces and the
        // indices of the points of the faces of the convex hull
        ArrayList<ArrayList<Integer>> faces = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> pointsOfFace = new ArrayList<Integer>();
        // get vertices of hull
        // System.out.println("Vertices:");
        Point3dQH[] vertices = hull.getVertices();
        for (int vi = 0; vi < vertices.length; vi++) {
            Point3dQH pnt = vertices[vi];
            // System.out.println("point3d: " + pnt.x + " " + pnt.y
            // + " " + pnt.z);
            float x = (Double.valueOf(pnt.x)).floatValue();
            float y = (Double.valueOf(pnt.y)).floatValue();
            float z = (Double.valueOf(pnt.z)).floatValue();
            cube3dCreator.addCubeToContainer(x, y, z);
        }

        // get faces of hull and store their points in
        // "ArrayList<ArrayList<Integer>> faces"
        // System.out.println("Faces:");
        int[][] faceIndices = hull.getFaces();
        for (int fi = 0; fi < faceIndices.length; fi++) // !!!
        // ERROR IN DOCUMENTATION !!!
        {
            for (int k = 0; k < faceIndices[fi].length; k++) {
                // System.out.print(faceIndices[i2][k] + " ");
                // System.out.println("k: " + k);
                pointsOfFace.add(faceIndices[fi][k]);
                // System.out.println("pointsOfFace " + pointsOfFace);
            }
            faces.add((ArrayList<Integer>) pointsOfFace.clone());
            pointsOfFace.clear();
        }
        Iterator facesIterator = faces.iterator();
        // iterate over faces and build triangles and surrounding lines with face points
        while (facesIterator.hasNext()) {
            List face = (ArrayList) facesIterator.next();
            // System.out.println("face: " + face);
            // System.out.println("face.get(0): " + face.get(0));
            Integer vertex0ID = (Integer) face.get(0);
            int vertex0IDint = vertex0ID.intValue();
            // System.out.println("vertex0ID: " + vertex0IDint);
            // System.out.println("vertices[vertex0IDint]: " +
            // vertices[vertex0IDint]);
            Point3dQH point0 = vertices[vertex0IDint];
            Point3f p0 = new Point3f((float)point0.x,
                    (float) point0.y, (float)point0.z);
            Integer vertex1ID = (Integer) face.get(1);
            int vertex1IDint = vertex1ID.intValue();
            // System.out.println("vertex1ID: " + vertex1IDint);
            // System.out.println("vertices[vertex1IDint]: " + vertices[vertex1IDint]);
            Point3dQH point1 = vertices[vertex1IDint];
            Point3f p1 = new Point3f((float)point1.x,
                    (float) point1.y, (float) point1.z);
            Integer vertex2ID = (Integer) face.get(2);
            int vertex2IDint = vertex2ID.intValue();
            // System.out.println("vertex2ID: " + vertex2IDint);
            // System.out.println("vertices[vertex2IDint]: " + vertices[vertex2IDint]);
            Point3dQH point2 = vertices[vertex2IDint];
            Point3f p2 = new Point3f((float)point2.x,
                    (float)point2.y, (float)point2.z);
            triangle3dCreator.addTriangleToContainer(p0, p1, p2);
            Point3d pd0 = new Point3d(p0.x, p0.y, p0.z);
            Point3d pd1 = new Point3d(p1.x, p1.y, p1.z);
            Point3d pd2 = new Point3d(p2.x, p2.y, p2.z);
            line3dCreator.addLineToContainer(pd0, pd1);
            line3dCreator.addLineToContainer(pd1, pd2);
            line3dCreator.addLineToContainer(pd2, pd0);
        }
    }
}
