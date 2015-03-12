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