package org.neugen.backend;

import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import eu.mihosoft.vrl.v3d.VTriangleArray;
import org.neugen.gui.NeuGenConstants;
import org.neugen.utils.NeuGenLogger;
import org.neugen.visual.Utils3D;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Visualise a Node as Sphere:
 *
 */
public class NGNeuronNodeVisual {
    private Point3f loc;
    private float radius;

    /*private Material mat;
    private TransparencyAttributes ta;*/

    public NGNeuronNodeVisual(Point3f loc){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.loc=loc;
        this.radius=0.0f;
    }

    public NGNeuronNodeVisual(Point3f loc, float radius){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.loc=loc;
        this.radius=radius;
    }


    public void setLocation(Point3f loc){this.loc=loc;}

    public void setRadius(float radius){this.radius=radius;}

    /////////////////////////////////////////////////////
    /// Core functions
    /////////////////////////////////////////////////////

    /**
     * 2D line model: LINE
     * Creating a line with another point
     *
     * @param loc
     * @param loc1
     * @param color
     * @return
     */

    private LineArray linefromSegment(Point3f loc, Point3f loc1, Color3f color){

        LineArray la=new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);

        la.setCoordinate(0,loc);
        la.setColor(0, color);
        la.setCoordinate(1,loc1);
        la.setColor(1, color);

        return la;
    }


    /**
     * 3D Solid model: SOLID
     * Creating a Spherical TransformGroup
     *
     * @param point
     * @param rad
     * @return
     */

    private TransformGroup tgSpherefromNode(Point3f loc, float rad, Appearance app){
        Transform3D t3d = new Transform3D();
        t3d.set(new Vector3f(loc));
        TransformGroup tg = new TransformGroup(t3d);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        Sphere sphere=new Sphere(rad, Primitive.GENERATE_NORMALS, 100, app);
        tg.addChild(sphere);

        return tg;
    }

    /**
     * 2D VRL Model
     * Segment seg=new Segment();
     * NGNeuronSegmentVisual segVis=new NGNeuronSegmentVisual(segment);
     * segVis.getTriangle()
     */
    //.....


    /**
     * 3D VRL Model /2D VRL Model?
     * Creating a VRL Sphere and saving as VTriangleArray
     *
     * @param loc
     * @param rad
     * @return
     */
    private VTriangleArray vtriangleArrayfromNode(Point3f loc, float rad){
        eu.mihosoft.vrl.v3d.Sphere sphere= new eu.mihosoft.vrl.v3d.Sphere(loc.x, loc.y, loc.z, rad);
        VTriangleArray vta=sphere.getGeometry();
        return vta;
    }

    ////////////////////////////////////////////////////////////
    ////result: getting functions
    //////////////////////////////////////////////
    public LineArray getLineArray(Point3f loc1, Color3f color){
        if(color==null){
            color= Utils3D.white;
        }


        return linefromSegment(loc, loc1, color);
    }

    public VTriangleArray getVTriangleArray(){
        if(radius==0.0f) {
            System.err.println("Please input radius.");
        }
            return vtriangleArrayfromNode(loc, radius);

    }

    public TransformGroup getTransformGroup(Appearance app){
        if(radius==0.0f) {
            System.err.println("Please input radius.");
        }
        if(app==null){
            app=new Appearance();
        }

        return tgSpherefromNode(loc, radius, app);
    }


}
