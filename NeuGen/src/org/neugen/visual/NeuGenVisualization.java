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
package org.neugen.visual;

import org.neugen.gui.VisualizationTask;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.pickfast.PickCanvas;
import com.sun.j3d.utils.pickfast.PickIntersection;
import com.sun.j3d.utils.picking.PickResult;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.awt.Dimension;
import java.awt.GraphicsConfigTemplate;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.beans.PropertyChangeListener;
import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.LineArray;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PickInfo;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import org.apache.log4j.Logger;
import org.neugen.datastructures.Axon;
import org.neugen.datastructures.Cellipsoid;
import org.neugen.datastructures.Cons;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.Region;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.datastructures.Section;
import org.neugen.datastructures.Segment;
import org.neugen.gui.NeuGenApp;
import org.neugen.gui.NeuGenView;
import org.neugen.gui.VisualizationDialog;
import org.neugen.utils.Utils;

/**
 * @author Jens P Eberhard
 * @author Sergei Wolf
 */
public final class NeuGenVisualization {

    /** use to log message */
    private final static Logger logger = Logger.getLogger(NeuGenVisualization.class.getName());
    /** size of panel */
    private static final int PWIDTH = 512;
    private static final int PHEIGHT = 512;
    private static final int BOUNDSIZE = 250;  // larger than world
    //private Point3d USERPOSN;
    // private static final Point3d USERPOSN = new Point3d(500 * scale, 500 * scale, 500 * scale); // in mm
    //private static final float scale = 0.001f;
    private float scale;

    public float getScale() {
        return scale;
    }
    private static final Point3d USERPOSN = new Point3d(0, 4, 6); // in mm
    //private static final float scale = 1f;
    private BoundingSphere bounds;  // for environment nodes
    private SimpleUniverse simpleU = null;
    private Canvas3D canvas3D;
    private BranchGroup sceneBG;
    private VisualizationTask task;
    private Net net;
    private NeuGenView ngView;
    private JScrollPane C3DScrollPane;
    private JSplitPane C3DSplitPane;
    private PropertyChangeListener guiListener;
    //private VisualBehaviorListener mouseBehaviorListener;
    private boolean collide;
    private static SynchronBehavior synBehavior;
    private Shape3D axonsShape3D, dendritesShape3D;
    private CanvasSynchronListener canvasList;
    private CanvasPickInfoListener canvasPickInfoList;
    private RotationInterpolator rotator;
    private TransformGroup mover, spinner;
    private VisualizationDialog.VisualMethod visMethod;

    public static SynchronBehavior getSynBehavior() {
        return synBehavior;
    }

    public void init1() {
        ngView = NeuGenView.getInstance();
        C3DScrollPane = ngView.getVisualScrollPane();
        C3DSplitPane = ngView.getVisualDensSplitPane();
        C3DScrollPane.setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        C3DSplitPane.setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        /*if (C3DSplitPane.getDividerLocation() < PWIDTH) {
        C3DSplitPane.setDividerLocation(PWIDTH);
        }*/
        if (C3DSplitPane.getRightComponent() != null) {
            C3DSplitPane.remove(C3DSplitPane.getRightComponent());
        }
        bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);

        /*
        //GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        //canvas3D = new Canvas3D(config);
        GraphicsConfigTemplate3D template = new GraphicsConfigTemplate3D();
        template.setSceneAntialiasing(GraphicsConfigTemplate3D.PREFERRED);

        canvas3D = new Canvas3D(GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().
                getBestConfiguration(template));

        canvas3D.setFocusable(true);
        canvas3D.requestFocus();
        simpleU = new SimpleUniverse(canvas3D);
        simpleU.getViewer().getView().setMinimumFrameCycleTime(5);
        simpleU.getViewer().getView().setLocalEyeLightingEnable(true);

        //canvas3D.getView().setSceneAntialiasingEnable(true);
         *
         */
        GraphicsConfigTemplate3D gc3D = new GraphicsConfigTemplate3D();

        if (NeuGenApp.antiAliasing) {
            gc3D.setSceneAntialiasing(GraphicsConfigTemplate3D.PREFERRED);
        }

        gc3D.setSceneAntialiasing(GraphicsConfigTemplate.PREFERRED);
        GraphicsDevice gd[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        canvas3D = new Canvas3D(gd[0].getBestConfiguration(gc3D));
        simpleU = new SimpleUniverse(canvas3D);

        orbitControls(canvas3D);

        synBehavior = new SynchronBehavior(simpleU.getViewingPlatform().getViewPlatformTransform(), null);
        synBehavior.setSchedulingBounds(bounds);
    }

    public void setViewPosition(Vector3f mov, int angle) {
        TransformGroup tg = this.simpleU.getViewingPlatform().getViewPlatformTransform();

        Transform3D t3dTrans = new Transform3D();
        tg.getTransform(t3dTrans);
        t3dTrans.setTranslation(mov);
        tg.setTransform(t3dTrans);

        Transform3D t3dRot = new Transform3D();
        tg.getTransform(t3dRot);
        //t3dRot.setTranslation(mov);
        t3dRot.setRotation(new AxisAngle4f(1f, 0f, 0f, (float) Math.toRadians(angle)));
        //ViewT3D.setRotation(new AxisAngle4f(0, 0, 1, (float) Math.toRadians(45)));
        tg.setTransform(t3dRot);
    }

    public void init2() {
        sceneBG.setCapability(BranchGroup.ALLOW_DETACH);
        sceneBG.addChild(synBehavior);
        // pick info! TODO
        //canvasPickInfoList = new CanvasPickInfoListener();
        //canvas3D.addMouseListener(canvasPickInfoList);
        VisualizationTask.Visualization vis = ngView.getVisual();

        switch (vis) {
            case NET:
                Point3f upRightCorner = null;
                Vector3f mov = null;
                if (Region.getInstance() != null) {
                    if (Region.getInstance().getUpRightCorner() != null) {
                        upRightCorner = new Point3f(Region.getInstance().getUpRightCorner());
                        upRightCorner.scale(0.5f);
                        float zoom = scale / 0.001f;
                        float tmp = -25.0f / zoom;
                        if (tmp > -1.0f) {
                            tmp = -1.0f;
                        }
                        mov = new Vector3f(upRightCorner.x, tmp * upRightCorner.y, upRightCorner.z);
                        mov.scale(scale);
                        setViewPosition(mov, 90);
                    }
                }

                if (upRightCorner == null) {
                    net = ngView.getNet();
                    Point3f mid = net.getNeuronList().get(0).getSoma().getMid();
                    //upRightCorner = new Point3f(300.0f, -1000.0f, 500.0f);
                    //upRightCorner = new Point3f(0.0f, -1000.0f, 0.0f);
                    //mov = new Vector3f(upRightCorner);
                    float zoom = scale / 0.001f;
                    float tmp = 25.0f / zoom;
                    if (tmp < 1.0f) {
                        tmp = 1.0f;
                    }
                    //mov = new Vector3f(upRightCorner.x, upRightCorner.y, upRightCorner.z);
                    logger.info("soma mid: " + mid.toString());
                    if (mid.z < 1.0f) {
                        mid.z = 150.0f;
                    }

                    mov = new Vector3f(mid.x, mid.y, tmp * mid.z);
                    mov.scale(scale);
                    setViewPosition(mov, 0);
                }
                break;
            case RECONSTRUCTION:
                mov = new Vector3f(0.0f, 0.0f, 8.0f);
                mov.scale(scale);
                setViewPosition(mov, 0);
                break;
            case LOADED_GRAPH:
                mov = new Vector3f(3.0f, 0.0f, 8.0f);
                mov.scale(scale);
                setViewPosition(mov, 0);
                break;
        }

        sceneBG.compile();
        ngView.outPrintln(" rendering");
        task.setMyProgress(1.0f);
        //logger.info("USERPOS: " + USERPOSN.x + " " + USERPOSN.y + " " + USERPOSN.z);
        simpleU.addBranchGraph(sceneBG);

        C3DScrollPane.getViewport().add(canvas3D);
        C3DScrollPane.setBorder(null);
        C3DScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        C3DScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        guiListener = new VisualPropListener(canvas3D, C3DScrollPane, C3DSplitPane, this.getClass().toString());
        C3DSplitPane.addPropertyChangeListener(guiListener);
        logger.info("length: " + C3DSplitPane.getPropertyChangeListeners().length);
        ngView.getVisualOutputSplitPane().addPropertyChangeListener(guiListener);

        canvasList = new CanvasSynchronListener(synBehavior);
        canvas3D.addMouseListener(canvasList.getMouseListener());
        canvas3D.addMouseWheelListener(canvasList.getMouseWheelListener());
        canvas3D.addKeyListener(canvasList.getKeyListener());

        /*
        MovieMaker.generateVideo(canvas3D, simpleU, 1000, // Animation time in msec
        new Alpha(-1, 10000), new File("Movie.mov"), 10, 1920, 1200);
         *
         */
    }

    public NeuGenVisualization(VisualizationTask task, BranchGroup bg, int zoom,
            VisualizationDialog.VisualMethod visMethod, boolean loadedGraph) {
        this.task = task;
        this.sceneBG = bg;
        this.scale = 1f;
        this.visMethod = visMethod;
        //this.scale *= zoom;
        //retrieve the Shape3D object from the scene
        if (loadedGraph) {
            init1();
            init2();
        } else {
            Shape3D shape = (Shape3D) sceneBG.getChild(0);
            //create an Appearance and Material
            Appearance app = new Appearance();
            app.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
            app.setCapability(Appearance.ALLOW_MATERIAL_READ);
            switch (visMethod) {
                case WIRE: {
                    Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
                    Color3f objColor = new Color3f(1.0f, 0.7f, 0.8f);
                    Material mat = new Material(objColor, black, objColor, black, 80.0f);
                    app.setMaterial(mat);
                    PolygonAttributes pa = new PolygonAttributes();
                    pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
                    app.setPolygonAttributes(pa);

                    ColoringAttributes colorAtt = new ColoringAttributes();
                    colorAtt.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
                    app.setColoringAttributes(colorAtt);

                    TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.NICEST, 0.5f);
                    app.setTransparencyAttributes(ta);
                    shape.setAppearance(app);
                }
            }
            init1();
            spinner = createSpinner();
            sceneBG.addChild(spinner);
            setLightingRecon(spinner);
            addBackground(sceneBG);
            addKeyNavigator(sceneBG);
            init2();
        }
    }

    public NeuGenVisualization(VisualizationTask task, int zoom, VisualizationDialog.VisualMethod visMethod) {
        logger.info("Visualize data");
        this.task = task;
        this.scale = 0.001f;
        //this.scale = 0.1f;
        this.visMethod = visMethod;
        this.scale *= zoom;
        init1();
        ngView.outPrintln(" visualizing\n\n\n");
        sceneBG = createSceneGraph();
        init2();
    }

    private void addKeyNavigator(BranchGroup objRoot) {
        TransformGroup vpTrans = simpleU.getViewingPlatform().getViewPlatformTransform();
        KeyNavigatorBehavior keybehavior = new KeyNavigatorBehavior(vpTrans);
        keybehavior.setSchedulingBounds(bounds);
        objRoot.addChild(keybehavior);
    }

    public void addRegion(TransformGroup objRoot) {
        if (Region.isCortColumn()) {
            Point3f upRightCorner = new Point3f(Region.getInstance().getUpRightCorner());
            upRightCorner.scale(scale);
            upRightCorner.setZ(upRightCorner.z / 2.0f);
            upRightCorner.setY(upRightCorner.y / 2.0f);
            Utils3D.addColumn(objRoot, scale);
            Utils3D.addCoordinateSphereAxesToSceneGraph(objRoot, scale, new Point3f(0.0f, 0.0f, 0.0f));
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
            //Utils3D.addCoordinateSphereAxesToSceneGraph(objRoot, scale, new Point3f(0.0f, 0, 0));
            //Utils3D.addScaleBarToSceneGraph(objRoot, new Point3f(0.5f, 0, 0.5f), scale);
        }
    }

    public BranchGroup createSceneGraph() {
        BranchGroup objRoot = new BranchGroup();
        /*
        mover = moveBack(1.0d);
        spinner = createSpinner();
        objRoot.addChild(mover);
        mover.addChild(spinner);
         *
         */
        spinner = createSpinner();
        objRoot.addChild(spinner);

        addNeurons(spinner);
        addRegion(spinner);
        //spinner.addChild(makeSpin(spinner));
        //setLightingNet(mover);
        setLightingNet(spinner);
        addBackground(objRoot);
        addKeyNavigator(objRoot);
        return objRoot;
    }

    private RotationInterpolator makeSpin(TransformGroup spinner) {
        //Alpha alpha = new Alpha(1, Alpha.DECREASING_ENABLE, 0, 10000, 5000, 0, 1000, 5000, 0, 1000);
        Alpha alpha = new Alpha(1, 3000);//new Alpha(-1, 10000) set speed here
        alpha.setAlphaAtOneDuration(3000);
        alpha.setIncreasingAlphaRampDuration(500);
        alpha.setMode(Alpha.INCREASING_ENABLE);

        rotator = new RotationInterpolator(alpha, spinner);
        double rot = Math.toRadians(90);
        Transform3D rotate1 = new Transform3D();
        rotate1.rotX(rot);
        rotate1.setTranslation(new Vector3f(0.3f, 0.3f, 0.0f));
        rotator.setTransformAxis(rotate1);
        rotator.setSchedulingBounds(bounds);
        return rotator;
    }

    private void setLightingRecon(TransformGroup objMove) {
        // Set up the ambient light
        Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        objMove.addChild(ambientLightNode);

        // Set up the directional lights
        Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
        Vector3f light1Direction = new Vector3f(1.0f, 1.0f, 1.0f);

        //light1Direction.scale(scale * 10.0f);
        Color3f light2Color = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f light2Direction = new Vector3f(-1.0f, -1.0f, -1.0f);
        //light2Direction.scale(scale * 10.0f);

        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        objMove.addChild(light1);

        DirectionalLight light2 = new DirectionalLight(light2Color, light2Direction);
        light2.setInfluencingBounds(bounds);
        objMove.addChild(light2);
    }

    private void setLightingNet(TransformGroup objMove) {
        // Set up the ambient light
        Color3f ambientColor = Utils3D.lightBlue1;
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        objMove.addChild(ambientLightNode);
        // Set up the directional lights
        Color3f light1Color = Utils3D.brown;
        Vector3f light1Direction = new Vector3f(1.0f, 1.0f, 1.0f);
        light1Direction.scale(scale * 10.0f);
        //Color3f light2Color = Utils3D.brown;
        //Vector3f light2Direction = new Vector3f(-1.0f, -1.0f, -1.0f);
        //light2Direction.scale(scale * 10.0f);
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        objMove.addChild(light1);
        //DirectionalLight light2 = new DirectionalLight(light2Color, light2Direction);
        //light2.setInfluencingBounds(bounds);
        //objMove.addChild(light2);

        //objMove.addChild(lgt1);
    }

    private TransformGroup createSpinner() {
        spinner = new TransformGroup();
        spinner.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        return spinner;
    }

    private TransformGroup moveBack(Vector3f back, double scale) {
        Transform3D transform3D = new Transform3D();
        //transform3D.setTranslation(new Vector3f(0.0f, 0.0f, 0.0f));
        //transform3D.setTranslation(new Vector3f(0f, -20f, 0f));
        transform3D.setRotation(new AxisAngle4f(1.0f, 0.0f, 0.0f, (float) Math.toRadians(270)));
        //new Vector3f(-0.3f, -0.8f, -3.0f)
        transform3D.setTranslation(back);
        transform3D.setScale(scale);
        return new TransformGroup(transform3D);
    }

    public SimpleUniverse getUniverse() {
        simpleU.cleanup();
        return simpleU;
    }

    public void destroy() {
        logger.info("destroy java 3d data");
        synBehavior = null;

        canvas3D.removeMouseListener(canvasPickInfoList);
        canvas3D.removeMouseListener(canvasList.getMouseListener());
        canvas3D.removeKeyListener(canvasList.getKeyListener());
        canvas3D.removeMouseWheelListener(canvasList.getMouseWheelListener());
        canvasList = null;

        if (axonsShape3D != null) {
            axonsShape3D.removeAllGeometries();
        }

        if (dendritesShape3D != null) {
            dendritesShape3D.removeAllGeometries();
        }

        simpleU.cleanup();
        mover = null;
        spinner = null;
        rotator = null;

        axonsShape3D = null;
        dendritesShape3D = null;
        // Cleanup reference to Java3D
        C3DScrollPane.getViewport().remove(canvas3D);
        C3DScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Visualization"));

        if (canvas3D.isOffScreen()) {
            canvas3D.renderOffScreenBuffer();
            canvas3D.setOffScreenBuffer(null);
        }

        /*
        Runtime.getRuntime().gc();
        System.gc();
        sceneBG.detach();
         *
         */
        //canvas3D.getView().removeAllCanvas3Ds();
        //simpleU.getViewingPlatform().getViewers()[0].getView().removeAllCanvas3Ds();
        //simpleU.getViewingPlatform().getViewers()[0].setViewingPlatform(null);
        //Primitive.clearGeometryCache();
        simpleU.cleanup();
        simpleU.removeAllLocales();
        simpleU = null;
        canvas3D.setLocale(null);
        canvas3D = null;
        sceneBG = null;
        bounds = null;
        System.gc();
        ngView.outPrintln(Utils.getMemoryStatus());
    }

    private void addBackground(BranchGroup objRoot) {
        Background bg = new Background();
        bg.setApplicationBounds(bounds);
        //bg.setColor(ColorUtil.skyBlue1); // blue sky
        bg.setColor(Utils3D.black);    // black sky
        // bg.setColor(1.0f, 1.0f, 1.0f);    // white
        objRoot.addChild(bg);
    }

    private void orbitControls(Canvas3D canvas3D) {
        OrbitBehavior orbit = new OrbitBehavior(canvas3D, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(bounds);
        ViewingPlatform vp = simpleU.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
    }

    private void addNeurons(TransformGroup objRoot) {
        net = ngView.getNet();
        ngView.outPrintln("Number of neurons: " + net.getNeuronList().size());
        addSomata(objRoot);
        addSynapse(objRoot);

        switch (visMethod) {
            case LINES: {
                logger.info("visualize neuron with lines");
                addAxons(objRoot);
                addDendrites(objRoot);
                addUndefinedSections(objRoot);
                break;
            }
            case SOLID: {
                logger.info("visualize solid axons");
                addAxonsCyl(objRoot);
                addDendritesCyl(objRoot);
                addUndefinedSectionsCyl(objRoot);
                break;
            }
        }

        ngView.outPrintln(" processing network geometry\n");
        task.setMyProgress(0.8f);
    }

    private void addUndefinedSections(TransformGroup objRoot) {
        ngView.outPrintln(" processing undefined section geometry\n");
        LineArray la;
        Shape3D undefSecShape3D = new Shape3D();
        undefSecShape3D.removeGeometry(0);
        //logger.debug("totalNumberOfAxonalSegments: " + totalNumberOfAxonalSegments);
        for (Neuron neuron : net.getNeuronList()) {
            for (Section section : neuron.getUndefinedSections()) {
                for (Segment segment : section.getSegments()) {
                    Point3f segStart = new Point3f(segment.getStart());
                    segStart.scale(scale);
                    //float r1 = scale * segment.getStartRadius();
                    Point3f segEnd = new Point3f(segment.getEnd());
                    segEnd.scale(scale);
                    //float r2 = scale * segment.getEndRadius();
                    la = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
                    la.setCoordinate(0, segStart);
                    la.setCoordinate(1, segEnd);
                    /* old color
                    la.setColor(0, new Color3f(0.25f, 0.41f, 0.88f));
                    la.setColor(1, new Color3f(0.25f, 0.41f, 0.88f));
                     */
                    la.setColor(0, Utils3D.darkgreyblue);
                    la.setColor(1, Utils3D.darkgreyblue);
                    undefSecShape3D.addGeometry(la);
                }
            }
            if (undefSecShape3D.getAllGeometries().hasMoreElements()) {
                objRoot.addChild(undefSecShape3D);
            }
        }
    }

    private void addUndefinedSectionsCyl(TransformGroup objRoot) {
        ngView.outPrintln(" processing undefined section geometry\n");
        Transform3D t3d = new Transform3D();
        Appearance app = new Appearance();
        app.setCapability(Appearance.ALLOW_MATERIAL_READ);
        Material mat = new Material();

        TransparencyAttributes ta = new TransparencyAttributes();
        ta.setTransparency(0.1f);
        ta.setTransparencyMode(TransparencyAttributes.NICEST);
        app.setTransparencyAttributes(ta);

        //logger.debug("totalNumberOfAxonalSegments: " + totalNumberOfAxonalSegments);
        for (Neuron neuron : net.getNeuronList()) {
            for (Section section : neuron.getUndefinedSections()) {
                for (Segment segment : section.getSegments()) {
                    Point3f segStart = new Point3f(segment.getStart());
                    segStart.scale(scale);
                    //float r1 = scale * segment.getStartRadius();
                    Point3f segEnd = new Point3f(segment.getEnd());
                    segEnd.scale(scale);

                    Vector3f len = new Vector3f(segEnd);
                    len.sub(segStart);

                    float hight = len.length();
                    //float hight = scale * 3.0f;
                    float rad = (segment.getEndRadius()) * scale;
                    //float rad = scale * 3.0f;
                    Cylinder cyl = new Cylinder(rad, hight);
                    Color3f color = Utils3D.darkgreyblue;// = Utils3D.darkgreyblue;
                    mat = new Material();
                    mat.setAmbientColor(color);
                    //Helligkeit
                    mat.setDiffuseColor(color);
                    //Reflektion
                    mat.setSpecularColor(color);
                    //Eine Fähigkeit wird gesetzt, damit man es während der Laufzeit ändern kann
                    mat.setCapability(Material.ALLOW_COMPONENT_READ);
                    //Setzte das Material in das Aussehen
                    app.setMaterial(mat);
                    cyl.setAppearance(app);

                    hight *= 1 / scale;
                    len.scale(1 / scale);
                    Vector3f start = new Vector3f(0, hight, 0);
                    Vector3f target = new Vector3f(len);
                    Vector3f cross = new Vector3f();
                    cross.cross(start, target);
                    float angle = start.angle(target);

                    if (angle < 1e-3) {
                    }
                    Point3f center = segment.getCenter();
                    center.scale(scale);
                    Vector3f dir = new Vector3f(center);
                    t3d.setTranslation(dir);
                    //t3d.setTranslation(len);
                    t3d.setRotation(new AxisAngle4f(cross, angle));
                    TransformGroup tg = new TransformGroup(t3d);
                    tg.addChild(cyl);

                    objRoot.addChild(tg);

                }
            }

        }
    }

    private void addAxonsCyl(TransformGroup objRoot) {
        ngView.outPrintln(" processing axons geometry\n");
        task.setMyProgress(0.3f);
        Transform3D t3d = new Transform3D();

        TransparencyAttributes ta = new TransparencyAttributes();
        ta.setTransparency(0.0f);
        ta.setTransparencyMode(TransparencyAttributes.NICEST);

        int cAx = 0, totalNumberOfAxonalSegments;
        totalNumberOfAxonalSegments = net.getTotalNumOfAxonalSegments();
        //logger.debug("totalNumberOfAxonalSegments: " + totalNumberOfAxonalSegments);
        for (Neuron neuron : net.getNeuronList()) {
            if (!neuron.collide() && collide) {
                continue;
            }
            Section firstSection = neuron.getAxon().getFirstSection();
            if (firstSection != null) {
                Section.Iterator secIterator = firstSection.getIterator();
                while (secIterator.hasNext()) {
                    Section section = secIterator.next();
                    Section.SectionType secType = section.getSectionType();
                    for (Segment segment : section.getSegments()) {
                        Point3f segStart = new Point3f(segment.getStart());
                        segStart.scale(scale);
                        Point3f segEnd = new Point3f(segment.getEnd());
                        segEnd.scale(scale);

                        Vector3f len = new Vector3f(segEnd);
                        len.sub(segStart);

                        float hight = len.length();
                        //float hight = scale * 3.0f;
                        float rad = (segment.getEndRadius()) * scale;
                        //float rad = scale * 3.0f;
                        Cylinder cyl = new Cylinder(rad, hight);

                        Color3f color;// = Utils3D.darkgreyblue;
                        if (secType == Section.SectionType.MYELINIZED) {
                            //color = Utils3D.darkgreyblue;
                            color = Utils3D.darkOrange;
                        } else {
                            //color = Utils3D.turquoise1;
                            color = Utils3D.darkSalmon;
                        }

                        Appearance app = new Appearance();
                        app.setCapability(Appearance.ALLOW_MATERIAL_READ);
                        app.setTransparencyAttributes(ta);

                        Material mat = new Material();
                        mat = new Material();
                        mat.setAmbientColor(color);
                        //Helligkeit
                        mat.setDiffuseColor(color);
                        //Reflektion
                        mat.setSpecularColor(Utils3D.black);
                        mat.setEmissiveColor(Utils3D.black);
                        mat.setShininess(50.0f);
                        //Eine Fähigkeit wird gesetzt, damit man es während der Laufzeit ändern kann
                        mat.setCapability(Material.ALLOW_COMPONENT_READ);
                        //Setzte das Material in das Aussehen
                        app.setMaterial(mat);
                        cyl.setAppearance(app);

                        hight *= 1 / scale;
                        len.scale(1 / scale);
                        Vector3f start = new Vector3f(0, hight, 0);
                        Vector3f target = new Vector3f(len);
                        Vector3f cross = new Vector3f();
                        cross.cross(start, target);
                        float angle = start.angle(target);

                        if (angle < 1e-3) {
                        }
                        Point3f center = segment.getCenter();
                        center.scale(scale);
                        Vector3f dir = new Vector3f(center);
                        t3d.setTranslation(dir);
                        //t3d.setTranslation(len);
                        t3d.setRotation(new AxisAngle4f(cross, angle));
                        TransformGroup tg = new TransformGroup(t3d);
                        tg.addChild(cyl);

                        objRoot.addChild(tg);
                        cAx++;
                        if (totalNumberOfAxonalSegments > 0) {
                            task.setMyProgress(0.3f + cAx * 0.2f / totalNumberOfAxonalSegments);
                        }
                    }
                }
            }
        }
    }

    private void addSomata(TransformGroup objRoot) {
        ngView.outPrintln(" processing somata geometry\n");
        LineArray la;
        TransformGroup tg = new TransformGroup();
        Transform3D t3d = new Transform3D();

        //tg.addChild(new Sphere(scale * 10, app));
        Appearance app = new Appearance();
        app.setCapability(Appearance.ALLOW_MATERIAL_READ);
        //Und das Material
        Material mat = new Material();
        //mat.setEmissiveColor(Utils3D.saddleBrown);
        mat.setAmbientColor(Utils3D.saddleBrown);
        //Helligkeit
        mat.setDiffuseColor(Utils3D.peru);
        //Reflektion
        //mat.setSpecularColor(Utils3D.wheat);
        mat.setSpecularColor(Utils3D.black);
        mat.setEmissiveColor(Utils3D.black);
        mat.setShininess(80.0f);
        //Eine Fähigkeit wird gesetzt, damit man es während der Laufzeit ändern kann
        mat.setCapability(Material.ALLOW_COMPONENT_READ);
        //Setzte das Material in das Aussehen
        app.setMaterial(mat);


        TransparencyAttributes myTA = new TransparencyAttributes();
        myTA.setTransparency(0.1f);
        myTA.setTransparencyMode(TransparencyAttributes.NICEST);
        //app.setTransparencyAttributes(myTA);
        //System.out.println("new Soma color !!! Soma now yellow !!!");
        //app.setColoringAttributes(new ColoringAttributes(new Color3f(0.8f, 0.0f, 0.0f), ColoringAttributes.FASTEST));

        //app.setColoringAttributes(new ColoringAttributes(Utils3D.saddleBrown, ColoringAttributes.NICEST));

        //app.setMaterial(new Material(new Color3f(0.1f, 0.1f, 0.1f), new Color3f(0.1f, 0.1f, 0.1f), ColorUtil.yellow, new Color3f(1, 1, 1), 64));
        Vector3f somaPos = new Vector3f();
        task.setMyProgress(0.1f);
        for (Neuron neuron : net.getNeuronList()) {

            if (!neuron.collide() && collide) {
                continue;
            }
            Cellipsoid soma = neuron.getSoma();
            Point3f somaMid = soma.getMid();
            somaPos.scale(scale, somaMid);
            float r1 = scale * soma.getMeanRadius();
            t3d.set(somaPos);
            tg = new TransformGroup(t3d);
            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

            ColoringAttributes colorAtt = new ColoringAttributes();
            colorAtt.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
            app.setColoringAttributes(colorAtt);
            Sphere somaSphere = new Sphere(r1, Primitive.GENERATE_NORMALS, 100, app);
            somaSphere.getShape().setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
            tg.addChild(somaSphere);
            //shapeList.add(somaSphere);
            //Sphere s = new Sphere(1, Primitive.GENERATE_NORMALS, 100, appKugel);
            objRoot.addChild(tg);

            /*
            Shape3D somaShape3D = new Shape3D();
            Section cyl = soma.getCylindricRepresentant();

            if (cyl != null) {
            for (Segment segment : cyl.getSegments()) {
            Point3f segStart = new Point3f(segment.getStart());
            segStart.scale(scale);
            Point3f segEnd = new Point3f(segment.getEnd());
            segEnd.scale(scale);
            la = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
            la.setCoordinate(0, segStart);
            la.setCoordinate(1, segEnd);
            la.setColor(0, Utils3D.white);
            la.setColor(1, Utils3D.white);
            somaShape3D.addGeometry(la);
            }
            }
            objRoot.addChild(somaShape3D);
             *
             */
        }
    }

    private void addAxons(TransformGroup objRoot) {
        ngView.outPrintln(" processing axons geometry\n");
        task.setMyProgress(0.3f);
        Appearance appearance = new Appearance();
        /*
        float width = 1.0f;
        boolean antialias = false;     
        appearance.setLineAttributes(new LineAttributes(width, LineAttributes.PATTERN_SOLID, antialias));
         */
        TransparencyAttributes myTA = new TransparencyAttributes();
        myTA.setTransparency(0.2f);
        myTA.setTransparencyMode(TransparencyAttributes.NICEST);
        appearance.setTransparencyAttributes(myTA);

        axonsShape3D = new Shape3D();
        //axonsShape3D.removeGeometry(0);
        axonsShape3D.removeAllGeometries();
        axonsShape3D.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        axonsShape3D.setAppearance(appearance);
        int cAx = 0, totalNumberOfAxonalSegments;
        totalNumberOfAxonalSegments = net.getTotalNumOfAxonalSegments();
        //logger.debug("totalNumberOfAxonalSegments: " + totalNumberOfAxonalSegments);
        for (Neuron neuron : net.getNeuronList()) {
            if (!neuron.collide() && collide) {
                continue;
            }
            Section firstSection = neuron.getAxon().getFirstSection();
            if (firstSection != null) {
                Section.Iterator secIterator = firstSection.getIterator();
                while (secIterator.hasNext()) {
                    Section section = secIterator.next();
                    Section.SectionType secType = section.getSectionType();
                    for (Segment segment : section.getSegments()) {
                        Point3f segStart = new Point3f(segment.getStart());
                        segStart.scale(scale);
                        Point3f segEnd = new Point3f(segment.getEnd());
                        segEnd.scale(scale);
                        LineArray la = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
                        la.setCoordinate(0, segStart);
                        la.setCoordinate(1, segEnd);

                        /* old color
                        la.setColor(0, new Color3f(0.25f, 0.41f, 0.88f));
                        la.setColor(1, new Color3f(0.25f, 0.41f, 0.88f));
                         */

                        Color3f color;// = Utils3D.darkgreyblue;
                        if (secType == Section.SectionType.MYELINIZED) {
                            //color = Utils3D.darkgreyblue;
                            color = Utils3D.darkOrange;
                        } else {
                            //color = Utils3D.turquoise1;
                            color = Utils3D.darkSalmon;
                        }
                        la.setColor(0, color);
                        la.setColor(1, color);
                        //lineArrayList.add(la);
                        axonsShape3D.addGeometry(la);
                        cAx++;
                        if (totalNumberOfAxonalSegments > 0) {
                            task.setMyProgress(0.3f + cAx * 0.2f / totalNumberOfAxonalSegments);
                        }
                    }
                }
            }
        }
        objRoot.addChild(axonsShape3D);
    }

    private void addDendrites(TransformGroup objRoot) {
        ngView.outPrintln(" processing dendrites geometry\n");
        Appearance appearance = new Appearance();
        //float width = 1.0f * scale;
        //boolean antialias = true;
        //appearance.setLineAttributes(new LineAttributes(width, LineAttributes.PATTERN_SOLID, antialias));
        dendritesShape3D = new Shape3D();
        dendritesShape3D.removeAllGeometries();
        dendritesShape3D.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        //dendritesShape3D.removeGeometry(0);
        //dendritesShape3D.setAppearance(appearance);
        TransparencyAttributes myTA = new TransparencyAttributes();
        myTA.setTransparency(0.2f);
        myTA.setTransparencyMode(TransparencyAttributes.NICEST);
        appearance.setTransparencyAttributes(myTA);
        dendritesShape3D.setAppearance(appearance);

        int cDen = 0, totalNumberOfDenSegments;
        totalNumberOfDenSegments = net.getTotalNumOfDenSegments();
        //logger.info("total number of dendrite segments: " + totalNumberOfDenSegments);
        task.setMyProgress(0.5f);
        for (Neuron neuron : net.getNeuronList()) {
            if (!neuron.collide() && collide) {
                continue;
            }
            for (Dendrite dendrite : neuron.getDendrites()) {
                Section firstSection = dendrite.getFirstSection();
                if (firstSection != null) {
                    Section.Iterator secIterator = firstSection.getIterator();
                    while (secIterator.hasNext()) {
                        Section section = secIterator.next();
                        Section.SectionType secType = section.getSectionType();
                        for (Segment segment : section.getSegments()) {
                            Point3f segStart = new Point3f(segment.getStart());
                            segStart.scale(scale);
                            Point3f segEnd = new Point3f(segment.getEnd());
                            segEnd.scale(scale);
                            LineArray la = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
                            la.setCoordinate(0, segStart);
                            la.setCoordinate(1, segEnd);
                            Color3f denColor = Utils3D.darkOliveGreen3;
                            if (secType != null) {
                                if (secType.equals(Section.SectionType.APICAL)) {
                                    //logger.info("this is an apical section");
                                    denColor = Utils3D.magenta;
                                } else if (secType.equals(Section.SectionType.BASAL)) {
                                    //logger.info("this is a basal section");
                                    //denColor = Utils3D.mediumSpringGreen;
                                    denColor = Utils3D.yellow;
                                } else if (secType.equals(Section.SectionType.OBLIQUE)) {
                                    //logger.info("this is an oblique section");
                                    denColor = Utils3D.brown1;
                                }
                            }
                            la.setColor(0, denColor);
                            la.setColor(1, denColor);

                            //lineArrayList.add(la);
                            /* old color
                            la.setColor(0, new Color3f(0.93f, 0.87f, 0.51f));
                            la.setColor(1, new Color3f(0.93f, 0.87f, 0.51f));
                             */
                            dendritesShape3D.addGeometry(la);
                            cDen++;
                            if (totalNumberOfDenSegments > 0 && !Float.isInfinite(totalNumberOfDenSegments)) {
                                task.setMyProgress(0.5f + cDen * 0.3f / totalNumberOfDenSegments);
                            }
                        }
                    }
                }
            }
        }
        objRoot.addChild(dendritesShape3D);
    }

    private void addDendritesCyl(TransformGroup objRoot) {
        ngView.outPrintln(" processing dendrites geometry\n");
        Transform3D t3d = new Transform3D();

        TransparencyAttributes ta = new TransparencyAttributes();
        ta.setTransparency(0.0f);
        ta.setTransparencyMode(TransparencyAttributes.NICEST);

        int cDen = 0, totalNumberOfDenSegments;
        totalNumberOfDenSegments = net.getTotalNumOfDenSegments();
        //logger.info("total number of dendrite segments: " + totalNumberOfDenSegments);
        task.setMyProgress(0.5f);
        for (Neuron neuron : net.getNeuronList()) {
            if (!neuron.collide() && collide) {
                continue;
            }
            for (Dendrite dendrite : neuron.getDendrites()) {
                Section firstSection = dendrite.getFirstSection();
                if (firstSection != null) {
                    Section.Iterator secIterator = firstSection.getIterator();
                    while (secIterator.hasNext()) {
                        Section section = secIterator.next();
                        Section.SectionType secType = section.getSectionType();
                        for (Segment segment : section.getSegments()) {
                            Point3f segStart = new Point3f(segment.getStart());
                            segStart.scale(scale);
                            Point3f segEnd = new Point3f(segment.getEnd());
                            segEnd.scale(scale);

                            Vector3f len = new Vector3f(segEnd);
                            len.sub(segStart);

                            float hight = len.length();
                            //float hight = scale * 3.0f;
                            float rad = (segment.getEndRadius()) * scale;
                            //float rad = scale * 3.0f;
                            Cylinder cyl = new Cylinder(rad, hight);

                            Color3f color = Utils3D.darkOliveGreen3;
                            if (secType != null) {
                                if (secType.equals(Section.SectionType.APICAL)) {
                                    //logger.info("this is an apical section");
                                    color = Utils3D.magenta;
                                } else if (secType.equals(Section.SectionType.BASAL)) {
                                    //logger.info("this is a basal section");
                                    //denColor = Utils3D.mediumSpringGreen;
                                    color = Utils3D.yellow;
                                } else if (secType.equals(Section.SectionType.OBLIQUE)) {
                                    //logger.info("this is an oblique section");
                                    color = Utils3D.brown1;
                                }
                            }

                            Appearance app = new Appearance();
                            app.setCapability(Appearance.ALLOW_MATERIAL_READ);
                            app.setTransparencyAttributes(ta);

                            Material mat = new Material();
                            mat.setAmbientColor(color);
                            //Helligkeit
                            mat.setDiffuseColor(color);
                            //Reflektion
                            mat.setSpecularColor(Utils3D.black);
                            mat.setEmissiveColor(Utils3D.black);
                            //Eine Fähigkeit wird gesetzt, damit man es während der Laufzeit ändern kann
                            mat.setCapability(Material.ALLOW_COMPONENT_READ);
                            mat.setShininess(50.0f);
                            //Setzte das Material in das Aussehen
                            app.setMaterial(mat);
                            cyl.setAppearance(app);

                            hight *= 1 / scale;
                            len.scale(1 / scale);
                            Vector3f start = new Vector3f(0, hight, 0);
                            Vector3f target = new Vector3f(len);
                            Vector3f cross = new Vector3f();
                            cross.cross(start, target);
                            float angle = start.angle(target);

                            if (angle < 1e-3) {
                                System.out.println("Mach' nix, die sind schon (fast) richtig ausgerichtet: " + angle);
                            }

                            Point3f center = segment.getCenter();
                            center.scale(scale);
                            Vector3f dir = new Vector3f(center);
                            t3d.setTranslation(dir);
                            //t3d.setTranslation(len);
                            t3d.setRotation(new AxisAngle4f(cross, angle));
                            TransformGroup tg = new TransformGroup(t3d);
                            tg.addChild(cyl);

                            objRoot.addChild(tg);

                            cDen++;
                            if (totalNumberOfDenSegments > 0 && !Float.isInfinite(totalNumberOfDenSegments)) {
                                task.setMyProgress(0.5f + cDen * 0.3f / totalNumberOfDenSegments);
                            }
                        }
                    }
                }
            }
        }

    }

    private void addSynapse(TransformGroup objRoot) {
        ngView.outPrintln(" processing synapses geometry\n");
        TransformGroup tg = new TransformGroup();
        Transform3D t3d = new Transform3D();
        Appearance app = new Appearance();
        //Und das Material
        Material mat = new Material();
        //mat.setEmissiveColor(Utils3D.saddleBrown);
        mat.setAmbientColor(Utils3D.red);
        //Helligkeit
        mat.setDiffuseColor(Utils3D.orangeRed);
        //Reflektion
        //mat.setSpecularColor(Utils3D.coral);
        mat.setSpecularColor(Utils3D.black);
        mat.setEmissiveColor(Utils3D.black);
        mat.setShininess(80.0f);
        //Eine Fähigkeit wird gesetzt, damit man es während der Laufzeit ändern kann
        mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
        mat.setCapability(Material.ALLOW_COMPONENT_READ);
        //Setzte das Material in das Aussehen
        app.setMaterial(mat);

        ColoringAttributes colorAtt = new ColoringAttributes();
        colorAtt.setCapability(ColoringAttributes.ALLOW_COLOR_READ);
        colorAtt.setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
        colorAtt.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
        app.setColoringAttributes(colorAtt);

        //TransparencyAttributes myTA = new TransparencyAttributes();
        //myTA.setTransparency(0.08f);
        //myTA.setTransparencyMode(TransparencyAttributes.NICEST);
        //app.setTransparencyAttributes(myTA);

        //app.setColoringAttributes(new ColoringAttributes(Utils3D.IndianRed1, ColoringAttributes.FASTEST));
        //app.setColoringAttributes(new ColoringAttributes(Utils3D.red, ColoringAttributes.NICEST));
        //app.setMaterial(new Material(new Color3f(0.1f, 0.1f, 0.1f), new Color3f(0.1f, 0.1f, 0.1f), ColorUtil.yellow, new Color3f(1, 1, 1), 64));
        Vector3f synPos = new Vector3f();
        task.setMyProgress(0.8f);
        for (Cons synapse : net.getSynapseList()) {
            if (synapse.getNeuron1() == null) {
                continue;
            }
            Segment axSeg = synapse.getNeuron1AxSegment();
            Segment denSeg = synapse.getNeuron2DenSectionSegment();
            Point3f axSegEnd = axSeg.getEnd();
            Point3f denSegEnd = denSeg.getEnd();
            Point3f center = new Point3f();
            center.add(axSegEnd, denSegEnd);
            center.scale(0.5f);
            synPos.add(center);
            synPos.scale(scale);
            t3d.set(synPos);
            tg = new TransformGroup(t3d);

            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

            float rad = scale * 3.0f;
            Sphere synapseShere = new Sphere(rad, Primitive.GENERATE_NORMALS, 100, app);
            synapseShere.getShape().setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
            synapseShere.getShape().setCapability(Shape3D.ALLOW_GEOMETRY_READ);
            String neuron1Name = synapse.getNeuron1().getName();
            String neuron2Name = synapse.getNeuron2().getName();
            //synapseShere.setName("Synapse info: Neuron 1: " + neuron1Name + " | Neuron 2: " + neuron2Name);
            synapseShere.getShape().setName("Synapse of: Neuron 1: " + neuron1Name + " | Neuron 2: " + neuron2Name);

            tg.addChild(synapseShere);
            objRoot.addChild(tg);
        }
    }

    private void addNewAxons(TransformGroup objRoot) {
        ngView.outPrintln(" processing new axons geometry\n");
        task.setMyProgress(0.3f);
        float x1, y1, z1, x2, y2, z2, r1, r2;
        int cAx = 0, totalNumberOfAxonalSegments;
        totalNumberOfAxonalSegments = net.getTotalNumOfAxonalSegments();
        //System.out.println("totalNumberOfAxonalSegments: " + totalNumberOfAxonalSegments);

        Transform3D tfTree = new Transform3D();
        tfTree.setTranslation(new Vector3f());


        Appearance app = new Appearance();
        //System.out.println("new Soma color !!! Soma now yellow !!!");
        app.setColoringAttributes(new ColoringAttributes(new Color3f(0.8f,
                0.0f, 0.0f), ColoringAttributes.FASTEST));

        Appearance a = new Appearance();
        PolygonAttributes p = new PolygonAttributes();
        p.setCullFace(PolygonAttributes.CULL_NONE);
        a.setPolygonAttributes(p);

        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(Utils3D.darkBlue);
        a.setColoringAttributes(ca);

        int nneuron = net.getNumNeurons();
        for (int i = 0; i < nneuron; i++) {
            Neuron neuron = net.getNeuronList().get(i);
            Axon axon = neuron.getAxon();
            if (axon.getFirstSection() == null) {
                return;
            }
            Section firstSection = axon.getFirstSection();
            Section.Iterator secIterator = firstSection.getIterator();
            //scale = 0.01f;
            while (secIterator.hasNext()) {
                Section section = secIterator.next();
                int nsegs = section.getSegments().size();
                Segment segment;
                for (int j = 0; j < nsegs; ++j) {
                    segment = section.getSegments().get(j);
                    Point3f segStart = segment.getStart();
                    Point3f segEnd = segment.getEnd();
                    x1 = scale * segStart.x;
                    y1 = scale * segStart.y;
                    z1 = scale * segStart.z;
                    r1 = scale * segment.getStartRadius();

                    x2 = scale * segEnd.x;
                    y2 = scale * segEnd.y;
                    z2 = scale * segEnd.z;
                    r2 = scale * segment.getEndRadius();

                    Point3f p0 = new Point3f(x1, y1 + r1, z1);
                    Point3f p1 = new Point3f(x2, y2 + r2, z2);
                    Point3f p2 = new Point3f(x2 + r2, y2, z2);
                    Point3f p3 = new Point3f(x1 + r1, y1, z1);

                    Point3f p4 = new Point3f(x1 - r1, y1, z1);
                    Point3f p5 = new Point3f(x2 - r2, y2, z2);
                    Point3f p6 = new Point3f(x2, y2 - r2, z2);
                    Point3f p7 = new Point3f(x1, y1 - r1, z1);

                    QuadArray geometry = new QuadArray(16,
                            GeometryArray.COORDINATES);

                    geometry.setCoordinate(0, p0);
                    geometry.setCoordinate(1, p1);
                    geometry.setCoordinate(2, p2);
                    geometry.setCoordinate(3, p3);

                    geometry.setCoordinate(4, p4);
                    geometry.setCoordinate(5, p5);
                    geometry.setCoordinate(6, p6);
                    geometry.setCoordinate(7, p7);

                    geometry.setCoordinate(8, p4);
                    geometry.setCoordinate(9, p5);
                    geometry.setCoordinate(10, p1);
                    geometry.setCoordinate(11, p0);

                    geometry.setCoordinate(12, p7);
                    geometry.setCoordinate(13, p6);
                    geometry.setCoordinate(14, p2);
                    geometry.setCoordinate(15, p3);

                    objRoot.addChild(new Shape3D(geometry, a));
                    cAx++;
                    task.setMyProgress(0.3f + cAx * 0.2f / totalNumberOfAxonalSegments);
                }
            }
        }
    }

    /**
     * TODO: Koordinaten, Radien, Namen ausgeben und die Farbe des Ausgewählten objekts ändern.
     */
    public class CanvasPickInfoListener implements MouseListener {

        private Sphere sphere;
        private TransformGroup sphTrans;
        private Transform3D t3d;
        private PickCanvas pickCanvas;
        private PickInfo[] pickInfoArr;
        private PickResult picked;

        public CanvasPickInfoListener() {
            Appearance app = new Appearance();
            Material mat = new Material();
            mat.setAmbientColor(Utils3D.green);
            mat.setSpecularColor(Utils3D.skyblue);
            mat.setDiffuseColor(Utils3D.darkBlue);

            //a,e,d,s

            app.setMaterial(mat);
            //greenlook.setMaterial(new Material(objColor, Utils3D.black, objColor, Utils3D.white, 128.0f));
            sphere = new Sphere(0.004f, Primitive.GENERATE_NORMALS, 100, app);
            sphere.setPickable(false);
            t3d = new Transform3D();
            sphTrans = new TransformGroup(t3d);
            //sphTrans = new TransformGroup();
            sphTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            sphTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            // Add sphere, transform
            sphTrans.addChild(sphere);
            sceneBG.addChild(sphTrans);

            pickCanvas = new PickCanvas(canvas3D, sceneBG);
            pickCanvas.setTolerance(3.5f);
            pickCanvas.setMode(PickInfo.PICK_GEOMETRY);
            pickCanvas.setFlags(PickInfo.LOCAL_TO_VWORLD | PickInfo.CLOSEST_GEOM_INFO | PickInfo.NODE);
        }

        public void handleObjectSelection(MouseEvent mousee) {
            pickCanvas.setShapeLocation(mousee);
            pickInfoArr = pickCanvas.pickAllSorted();
            //pickInfoArr = pickCanvas.pickAll();
            if (pickInfoArr != null) {
                // Get closest intersection results
                Transform3D l2vw = pickInfoArr[0].getLocalToVWorld();
                PickInfo.IntersectionInfo[] iInfoArr = pickInfoArr[0].getIntersectionInfos();
                PickIntersection pi = new PickIntersection(l2vw, iInfoArr[0]);

                // Position sphere at intersection point
                Vector3d v = new Vector3d();
                Point3d intPt = pi.getPointCoordinatesVW();
                v.set(intPt);
                t3d.setTranslation(v);
                sphTrans.setTransform(t3d);

                Point3f scalePnt = new Point3f(intPt);
                scalePnt.scale(1.0f / scale);

                for (int i = 0; i < pickInfoArr.length; i++) {
                    Node node = pickInfoArr[i].getNode();
                    if (node != null) {
                        if (node.getName() != null) {
                            ngView.outPrintln(node.getName());
                        } else {
                            ngView.outPrintln("no info for this object");
                        }
                        ngView.outPrintln("Coordinates: " + scalePnt);
                        break;
                    } else {
                        ngView.outPrintln("Coordinates: " + scalePnt);
                    }
                }
            }
        }

        /*
        public void handleObjectSelection(MouseEvent mousee) {
        PickCanvas MyPick = new PickCanvas(canvas3D, sceneBG);
        PickResult result;
        MyPick.setTolerance(0f);

        MyPick.setShapeLocation(mousee);
        result = MyPick.pickClosest();

        if (result == null) {
        System.out.println("Nothing picked");
        return;
        } else {
        Primitive p = (Primitive) result.getNode(PickResult.PRIMITIVE);
        Shape3D s = (Shape3D) result.getNode(PickResult.SHAPE3D);
        if (p != null) {
        // Get closest intersection results

        p.getLocalToVworld(t3d);
        sphTrans.setTransform(t3d);
        System.out.println("primitive");
        System.out.println(p.getName());

        } else if (s != null) {
        if(result.numIntersections() > 0) {
        PickIntersection intersect = result.getIntersection(0);
        //PickIntersection intersect = result.getIntersection(0);
        Point3d nextpoint = intersect.getPointCoordinates();
        logger.info(nextpoint);
        }

        //s.getLocalToVworld(t3d);
        sphTrans.setTransform(t3d);
        System.out.println("sphere");
        System.out.println(s.getName());
        } else {
        System.out.println("null");
        }
        }
        }

         *
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            handleObjectSelection(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //handleObjectSelection(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
