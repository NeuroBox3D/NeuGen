/*
 * MainApp.java
 *
 * Created on March 10, 2007
 *
 */
package org.neugen.surface3d;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ImageComponent2D;
import javax.vecmath.Point3d;

import org.neugen.datastructures.VolumeOfVoxels;
import org.neugen.geometry3d.Cube3dCreator;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.util.Vector;
import javax.media.j3d.Material;
import org.neugen.geometry3d.Triangle3dCreator;
import org.neugen.simpletriangulation.Triangle;

/**
 *
 * @author Jens P Eberhard
 */
@SuppressWarnings("serial")
public final class MainApp extends Applet {

    private static int FrameWidth = 600;
    private static int FrameHeight = 700;
    private SimpleUniverse simpleU;

    /////////////////////////////////////////////////
    //
    // create scene graph branch group
    //
    public BranchGroup createSceneGraph(boolean wireFrame) {

        int VOV_L = 42; // dimensions of volume of voxels 50
        int VOV_W = 42;
        int VOV_H = 42;

        BranchGroup contentRoot = new BranchGroup();
        ColorUtil.addCoordinateSphereAxesToSceneGraph(contentRoot, 0.01f);

        VolumeOfVoxels vv = new VolumeOfVoxels(VOV_L, VOV_W, VOV_H);
        vv.fillVolumeOfSphere(true);

        /* Simple visualization by triangles */

        Vector<Triangle> triangleVector = new Vector<Triangle>();
        Util.triangulation(vv.getVoxelsValueAsFloatArray(), VOV_L, VOV_W, VOV_H, triangleVector);
        Triangle3dCreator triangleCreator = new Triangle3dCreator(ColorUtil.grey);

        for (Triangle t : triangleVector) {
            triangleCreator.addTriangleToContainer(
                    t.getP1AsScaledPoint3f(1.0f / VOV_L, 1.0f / VOV_W, 1.0f / VOV_H),
                    t.getP2AsScaledPoint3f(1.0f / VOV_L, 1.0f / VOV_W, 1.0f / VOV_H),
                    t.getP3AsScaledPoint3f(1.0f / VOV_L, 1.0f / VOV_W, 1.0f / VOV_H));
            //t.printData();
        }
        contentRoot.addChild(triangleCreator.getTriangleContainer());


        /* Simple visualization by boxes */
        Material mat = new Material();
        mat.setEmissiveColor(ColorUtil.red);
        Cube3dCreator cubeCreator = new Cube3dCreator(0.05f, 0.05f, 0.05f, mat, 0.0f);
        // vv.addAllTagedVoxelsToContainer(cubeCreator);
        contentRoot.addChild(cubeCreator.getCubeContainer());

        /* For cube testing */
        contentRoot.addChild(cubeCreator.getCubeAsShapeOfQuadArrays(0.1f, 0.1f, 0.1f));
        contentRoot.addChild(cubeCreator.getCubeAsBox(0.15f, 0.15f, 0.15f));

        Background background = new Background(new ImageComponent2D(ImageComponent2D.FORMAT_RGB,
                ColorUtil.createColoredGradientImage(FrameWidth, FrameHeight)));
        background.setApplicationBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        contentRoot.addChild(background);

        contentRoot.compile();
        return contentRoot;
    }

    /** Creates a new instance of MainApp
     * Create a simple scene and attach it to the virtual universe
     *
     */
    public MainApp(String[] args) {

        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);

        // SimpleUniverse is a Convenience Utility class
        simpleU = new SimpleUniverse(canvas3D);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        BranchGroup scene = createSceneGraph(args.length > 0);
        orbitControls(canvas3D);
        simpleU.addBranchGraph(scene);
    }

    private void orbitControls(Canvas3D canvas3D) {
        OrbitBehavior orbit = new OrbitBehavior(canvas3D, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(new BoundingSphere());
        ViewingPlatform vp = simpleU.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
    }

    /**
     * The following allows this to be run as an application as well as an applet
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        @SuppressWarnings("unused")
        Frame frame = new MainFrame(new MainApp(args), FrameWidth, FrameHeight);
    }
}
