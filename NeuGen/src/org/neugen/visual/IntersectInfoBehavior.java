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
 * $RCSfile: IntersectInfoBehavior.java,v $
 *
 * Copyright (c) 2007 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 *
 * $Revision: 1.3 $
 * $Date: 2010-09-20 16:00:26 $
 * $State: Exp $
 */
package org.neugen.visual;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.pickfast.*;
import com.sun.j3d.utils.geometry.*;

/**
 * Class:       IntersectInfoBehavior
 * 
 * Description: Used to respond to mouse pick and drag events
 *              in the 3D window. Displays information about the pick.
 *
 * Version:     1.0
 *
 */
public class IntersectInfoBehavior extends Behavior {

    float size;
    PickCanvas pickCanvas;
    PickInfo[] pickInfoArr;
    Appearance oldlook, redlookwf, redlook, greenlook, bluelook;
    Node oldNode = null;
    GeometryArray oldGeom = null;
    Color3f redColor = new Color3f(1.0f, 0.0f, 0.0f);
    TransformGroup[] sphTrans = new TransformGroup[6];
    Sphere[] sph = new Sphere[6];
    Transform3D spht3 = new Transform3D();

    public IntersectInfoBehavior(Canvas3D canvas3D, BranchGroup branchGroup, float size) {
        pickCanvas = new PickCanvas(canvas3D, branchGroup);
        pickCanvas.setTolerance(5.0f);
        pickCanvas.setMode(PickInfo.PICK_GEOMETRY);
        pickCanvas.setFlags(PickInfo.LOCAL_TO_VWORLD | PickInfo.CLOSEST_GEOM_INFO);
        this.size = size;
        // Create an Appearance.
        redlook = new Appearance();
        Color3f objColor = new Color3f(0.5f, 0.0f, 0.0f);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        redlook.setMaterial(new Material(objColor, black, objColor, white, 50.0f));
        redlook.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

        redlookwf = new Appearance();
        redlookwf.setMaterial(new Material(objColor, black, objColor, white, 50.0f));
        PolygonAttributes pa = new PolygonAttributes();
        pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        redlookwf.setPolygonAttributes(pa);

        oldlook = new Appearance();
        objColor = new Color3f(1.0f, 1.0f, 1.0f);
        oldlook.setMaterial(new Material(objColor, black, objColor, white, 50.0f));

        greenlook = new Appearance();
        objColor = new Color3f(0.0f, 0.8f, 0.0f);
        greenlook.setMaterial(new Material(objColor, black, objColor, white, 50.0f));
        bluelook = new Appearance();
        objColor = new Color3f(0.0f, 0.0f, 0.8f);
        bluelook.setMaterial(new Material(objColor, black, objColor, white, 50.0f));
        for (int i = 0; i < 6; i++) {
            switch (i) {
                case 0:
                    sph[i] = new Sphere(size * 1.15f, redlook);
                    break;
                case 1:
                    sph[i] = new Sphere(size * 1.1f, greenlook);
                    break;
                default:
                    sph[i] = new Sphere(size, bluelook);
                    break;
            }
            sph[i].setPickable(false);
            sphTrans[i] = new TransformGroup();
            sphTrans[i].setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            sphTrans[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

            // Add sphere, transform
            branchGroup.addChild(sphTrans[i]);
            sphTrans[i].addChild(sph[i]);
        }
    }

    @Override
    public void initialize() {
        wakeupOn(new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED));
    }

    @Override
    public void processStimulus(Enumeration criteria) {
        WakeupCriterion wakeup;
        AWTEvent[] event;
        int eventId;

        while (criteria.hasMoreElements()) {
            wakeup = (WakeupCriterion) criteria.nextElement();
            if (wakeup instanceof WakeupOnAWTEvent) {
                event = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
                for (int i = 0; i < event.length; i++) {
                    eventId = event[i].getID();
                    if (eventId == MouseEvent.MOUSE_PRESSED) {
                        int x = ((MouseEvent) event[i]).getX();
                        int y = ((MouseEvent) event[i]).getY();
                        pickCanvas.setShapeLocation(x, y);
                        pickInfoArr = pickCanvas.pickAllSorted();
                        // Use this to do picking benchmarks
	    /*
                        long start = System.currentTimeMillis();
                        for (int l=0;l<2;l++) {
                        if (l == 0) System.out.print ("BOUNDS: ");
                        if (l == 1) System.out.print ("GEOMETRY: ");

                        for (int k=0;k<1000;k++) {
                        if (l == 0) {
                        pickCanvas.setMode(PickTool.BOUNDS);
                        pickResult = pickCanvas.pickAllSorted();
                        }
                        if (l == 1) {
                        pickCanvas.setMode(PickTool.GEOMETRY);
                        pickResult = pickCanvas.pickAllSorted();
                        }
                        }
                        long delta = System.currentTimeMillis() - start;
                        System.out.println ("\t"+delta+" ms / 1000 picks");
                        }
                         */
                        if (pickInfoArr != null) {

                            // Get closest intersection results
                            Transform3D l2vw = pickInfoArr[0].getLocalToVWorld();
                            PickInfo.IntersectionInfo[] iInfoArr = pickInfoArr[0].getIntersectionInfos();
                            PickIntersection pi = new PickIntersection(l2vw, iInfoArr[0]);

                            // Safe to assume the return geometry is of GeometryArray type.
                            GeometryArray curGeomArray = (GeometryArray) iInfoArr[0].getGeometry();

                            // Position sphere at intersection point
                            Vector3d v = new Vector3d();
                            Point3d intPt = pi.getPointCoordinatesVW();
                            v.set(intPt);
                            spht3.setTranslation(v);
                            sphTrans[0].setTransform(spht3);

                            // Position sphere at closest vertex
                            Point3d closestVert = pi.getClosestVertexCoordinatesVW();
                            v.set(closestVert);
                            spht3.setTranslation(v);
                            sphTrans[1].setTransform(spht3);

                            Point3d[] ptw = pi.getPrimitiveCoordinatesVW();
                            Point3d[] pt = pi.getPrimitiveCoordinates();

                            int[] coordidx = pi.getPrimitiveCoordinateIndices();
                            Point3d ptcoord = new Point3d();
                            for (int k = 0; k < pt.length; k++) {
                                v.set(ptw[k]);
                                spht3.setTranslation(v);
                                sphTrans[k + 2].setTransform(spht3);
                            }

                            // Get interpolated color (if available)
                            Color4f iColor4 = null;
                            Color3f iColor = null;
                            Vector3f iNormal = null;

                            if (curGeomArray != null) {
                                int vf = curGeomArray.getVertexFormat();

                                if (((vf
                                        & (GeometryArray.COLOR_3
                                        | GeometryArray.COLOR_4)) != 0)
                                        && (null != (iColor4 =
                                        pi.getPointColor()))) {
                                    iColor = new Color3f(iColor4.x, iColor4.y, iColor4.z);
                                    // Change the point's color
                                    redlook.setMaterial(new Material(iColor, new Color3f(0.0f, 0.0f, 0.0f), iColor, new Color3f(1.0f, 1.0f, 1.0f), 50.0f));
                                }
                                if (((vf & GeometryArray.NORMALS) != 0)
                                        && (null != (iNormal =
                                        pi.getPointNormal()))) {
                                    System.out.println("Interpolated normal: " + iNormal);
                                }
                            }

                            System.out.println("=============");
                            System.out.println("Coordinates of intersection pt:" + intPt);
                            System.out.println("Coordinates of vertices: ");
                            for (int k = 0; k < pt.length; k++) {
                                System.out.println(k + ":" + ptw[k].x + " " + ptw[k].y + " " + ptw[k].z);
                            }
                            System.out.println("Closest vertex: " + closestVert);
                            if (iColor != null) {
                                System.out.println("Interpolated color: " + iColor);
                            }
                            if (iNormal != null) {
                                System.out.println("Interpolated normal: " + iNormal);
                            }
                        }
                    }
                }
            }
        }
        wakeupOn(new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED));
    }
}
