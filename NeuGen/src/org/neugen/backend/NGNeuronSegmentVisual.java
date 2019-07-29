package org.neugen.backend;

import com.sun.j3d.utils.geometry.Cylinder;
import eu.mihosoft.vrl.v3d.Node;
import eu.mihosoft.vrl.v3d.Triangle;
import org.apache.log4j.Logger;
import org.neugen.datastructures.Segment;
import org.neugen.gui.NeuGenConstants;
import org.neugen.utils.NeuGenLogger;
import org.neugen.utils.Utils;
import org.neugen.visual.Utils3D;

import javax.media.j3d.*;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * visualisation of a basis element, i.e. segment
 * as Line with color3f
 * as VTriangle
 * as TransformGroup with appearance
 *
 */
public class NGNeuronSegmentVisual {

    public static final Logger logger = Logger.getLogger(NGNeuronSegmentVisual.class.getName());

    private Segment segment;
    private float scale;

    public NGNeuronSegmentVisual(Segment segment){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.segment=segment;
        this.scale=0.0001f;
    }

    /////////////////////////////////////////////////////////////
    ////// parameter: setting and getting funtions:
    ////////////////////////////////////////////////////////////
    public void setSegment(Segment segment){this.segment=segment;}

    public Segment getSegment(){return segment;}

    public void setScale(float scale){this.scale=scale;}

    public float getScale(){return scale;}


    //////////////////////////////////////////////////////////
    ///  core functions for creating the basis visual elements
    //////////////////////////////////////////////////////////

    /**
     * 2D line model: LINE
     * LineArray is the basis element of Shape3D: shape3D
     * through the function addGeometry we can add LineArray into Shape3D:
     * i.e: shape3D.addGeometry(la)
     * @param segment
     * @param color
     * @return LineArray: la
     */
    private LineArray linefromSegment(Segment segment, Color3f color){
        Point3f segStart=segment.getStart();
        segStart.scale(scale);

        Point3f segEnd=segment.getEnd();
        segEnd.scale(scale);

        LineArray la=new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);

        la.setCoordinate(0,segStart);
        la.setColor(0, color);
        la.setCoordinate(1,segEnd);
        la.setColor(1, color);

        return la;
    }

    /**
     * 2D VRL model with Triangle
     * Triangle is the basis element of VTriangleArray: vta
     * through the function: addTriangle we can add Triangle into VTriangleArray
     * i.e: vta.addTriangle(triangle)
     * @param segment
     * @return
     */
    private Triangle trianglefromSegment(Segment segment){
        Point3f segStart=segment.getStart();
        segStart.scale(scale);

        Point3f segEnd=segment.getEnd();
        segEnd.scale(scale);

        eu.mihosoft.vrl.v3d.Node nodeStart=new eu.mihosoft.vrl.v3d.Node(segStart);
        eu.mihosoft.vrl.v3d.Node nodeEnd=new eu.mihosoft.vrl.v3d.Node(segEnd);
        Triangle triangle=new Triangle(nodeStart, nodeEnd, nodeEnd);

        return triangle;
    }

    /**
     * 3D Solid model: SOLID
     * Cylinder TransformGroup is the basis element for TransformGroup objRoot
     * through the function addChild we can add this segmental cylinder TransformGroup into objRoot:
     * i.e.: objRoot.addChild(tg)
     * @param segment
     * @param color
     * @return TransformGroup: tg
     */
    private TransformGroup tgCylinderfromSegment(Segment segment, Appearance app){
        Point3f segStart=segment.getStart();
        Point3f segEnd=segment.getEnd();

        Vector3f len = new Vector3f(segEnd);
        len.sub(segStart);

        float hight = len.length()*scale;
        float rad=(float) (segment.getStartRadius()+segment.getEndRadius())*0.5f*scale;

        Cylinder cyl=new Cylinder(rad,hight,app);
        //cyl.setAppearance(app);

        Vector3f start=new Vector3f(0,hight/scale, 0);
        Vector3f target=new Vector3f(len);
        Vector3f cross=new Vector3f();
        cross.cross(start, target);
        float angle=start.angle(target);

        if (angle < 1e-3) {
            System.err.println("Mach' nix, die sind schon (fast) richtig ausgerichtet: " + angle);
        }

        Point3f center = segment.getCenter();
        center.scale(scale);
        Vector3f dir = new Vector3f(center);
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(dir);
        t3d.setRotation(new AxisAngle4f(cross, angle));

        TransformGroup tg = new TransformGroup(t3d);
        tg.addChild(cyl);

        return tg;

    }


    ///////////////////////////////////////
    ///result: getting functions
    //////////////////////////////////////
    public LineArray getLineArray(Color3f color){
        if(color==null){
            color= Utils3D.white;
        }
        return linefromSegment(segment, color);
    }

    public Triangle getTriangle(){
        return trianglefromSegment(segment);
    }

    public TransformGroup getCylinderTG(Appearance app){
        if(app==null){
            app=new Appearance();
        }
        return tgCylinderfromSegment(segment,app);
    }



}
