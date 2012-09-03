/*
 * File: J3dSegments.java
 * Created on 04.11.2009, 15:11:02
 *
 */

package org.neugen.visual;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3d;

/**
 * @author Sergei Wolf
 */

public class Segment3D extends Shape3D {

    public Segment3D() {
        super();
        Appearance ap = new Appearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(Utils3D.grey);
        ap.setColoringAttributes(ca);
        /*TransparencyAttributes myTA = new TransparencyAttributes();
        myTA.setTransparency(0.7f);
        myTA.setTransparencyMode(TransparencyAttributes.FASTEST);
        ap.setTransparencyAttributes(myTA);*/
//		render the Box as a wire frame
/*PolygonAttributes polyAttrbutes = new PolygonAttributes();
        polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
        ap.setPolygonAttributes(polyAttrbutes);*/
        setAppearance(ap);
    }

    public void setVerts(Point3d verts[]) {      
        QuadArray box = new QuadArray(verts.length, QuadArray.COORDINATES);
        box.setCoordinates(0, verts);
        //setGeometry(box);
        this.addGeometry(box);
    }

    public void setLines() {
        LineArray ver_Line = new LineArray(8, LineArray.COORDINATES);
        Point3d verts2[] = new Point3d[8];

        //bottom two lines
        verts2[0] = new Point3d(0, 0, 0);
        verts2[1] = new Point3d(0, 1.6, 0);
        verts2[2] = new Point3d(0.6, 0, 0);
        verts2[3] = new Point3d(0.6, 1.6, 0);

        //top two lines
        verts2[4] = new Point3d(0, 0, -0.6);
        verts2[5] = new Point3d(0, 1.6, -0.6);
        verts2[6] = new Point3d(0.6, 0, -0.6);
        verts2[7] = new Point3d(0.6, 1.6, -0.6);

        ver_Line.setCoordinates(0, verts2);
        setGeometry(ver_Line);
    }
}