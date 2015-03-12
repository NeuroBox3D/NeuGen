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

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineArray;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import org.neugen.datastructures.Region;

public class CorticalColumn extends Shape3D {

    //private final static float scale = 0.001f;
    private float scale;
    private final float lengthX;
    private final float widthY;
    private final float heightZ;
    private final float layer1;
    private final float layer23;
    private final float layer4;
    private final float layer5A;
    private final float layer5B;
    private final float layer6;

    public CorticalColumn(float scale) {
        super();
        this.scale = scale;
        Region.Param.ColumnParam par = Region.Param.getInstance().getColumnParam();
        lengthX = par.getLength() * scale;
        widthY = par.getWidth() * scale;
        layer1 = par.getLayer1() * scale;
        layer23 = par.getLayer23() * scale;
        layer4 = par.getLayer4() * scale;
        layer5A = par.getLayer5A() * scale;
        layer5B = par.getLayer5B() * scale;
        layer6 = par.getLayer6() * scale;
        heightZ = par.getHeight() * scale;

        Appearance ap = new Appearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(Utils3D.grey);
        ap.setColoringAttributes(ca);
        TransparencyAttributes myTA = new TransparencyAttributes();
        myTA.setTransparency(0.7f);
        myTA.setTransparencyMode(TransparencyAttributes.NICEST);
        ap.setTransparencyAttributes(myTA);
        //render the Box as a wire frame
        PolygonAttributes polyAttrbutes = new PolygonAttributes();
        polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
        ap.setPolygonAttributes(polyAttrbutes);
        setAppearance(ap);
    }

    public void addBox(TransformGroup scene) {
        QuadArray box = new QuadArray(16, QuadArray.COORDINATES);
        Point3d verts[] = new Point3d[16];

        /*
        verts[0] = new Point3d(0, 0, 0);
        verts[1] = new Point3d(0.6, 0, 0);
        verts[2] = new Point3d(0.6, 0, -0.6);
        verts[3] = new Point3d(0, 0, -0.6);

        verts[4] = new Point3d(0, 1.6, 0);
        verts[5] = new Point3d(0.6, 1.6, 0);
        verts[6] = new Point3d(0.6, 1.6, -0.6);
        verts[7] = new Point3d(0, 1.6, -0.6);

        verts[8] = new Point3d(0, 0.8, 0);
        verts[9] = new Point3d(0.6, 0.8, 0);
        verts[10] = new Point3d(0.6, 0.8, -0.6);
        verts[11] = new Point3d(0, 0.8, -0.6);

        verts[12] = new Point3d(0, 1.0, 0);
        verts[13] = new Point3d(0.6, 1.0, 0);
        verts[14] = new Point3d(0.6, 1.0, -0.6);
        verts[15] = new Point3d(0, 1.0, -0.6);
         *
         */

        //float leftPos = -100 * scale;
        verts[0] = new Point3d(0, 0, 0);
        verts[1] = new Point3d(lengthX, 0, 0);
        verts[2] = new Point3d(lengthX, widthY, 0);
        verts[3] = new Point3d(0, widthY, 0);

        verts[4] = new Point3d(0, 0, heightZ);
        verts[5] = new Point3d(lengthX, 0, heightZ);
        verts[6] = new Point3d(lengthX, widthY, heightZ);
        verts[7] = new Point3d(0, widthY, heightZ);

        float layer4End = layer6 + layer5B + layer5A;
        verts[8] = new Point3d(0, 0, layer4End);
        verts[9] = new Point3d(lengthX, 0, layer4End);
        verts[10] = new Point3d(lengthX, widthY, layer4End);
        verts[11] = new Point3d(0, widthY, layer4End);

        float layer4Start = layer4End + layer4;
        verts[12] = new Point3d(0, 0, layer4Start);
        verts[13] = new Point3d(lengthX, 0, layer4Start);
        verts[14] = new Point3d(lengthX, widthY, layer4Start);
        verts[15] = new Point3d(0, widthY, layer4Start);

        float layerL1End = heightZ - layer1;
        float textPosX = (lengthX + 250.0f * scale);
        float scaleText = 3.0f * scale;
        Vector3f textPos = new Vector3f(textPosX, widthY / 2.0f, layerL1End + layer1 / 2.0f);
        Utils3D.addText3D(scene, "L1", textPos, scaleText, Utils3D.grey);

        float layerL23End = layer6 + layer5B + layer5A + layer4;
        textPos = new Vector3f(textPosX, widthY / 2.0f, layerL23End + layer23 / 2.0f);
        Utils3D.addText3D(scene, "L2/3", textPos, scaleText, Utils3D.grey);

        textPos = new Vector3f(textPosX, widthY / 2.0f, layer4End + layer4 / 2.0f);
        Utils3D.addText3D(scene, "L4", textPos, scaleText, Utils3D.grey);

        float layer5AEnd = layer6 + layer5B;
        textPos = new Vector3f(textPosX, widthY / 2.0f, layer5AEnd + layer5A / 2.0f);
        Utils3D.addText3D(scene, "L5A", textPos, scaleText, Utils3D.grey);

        textPos = new Vector3f(textPosX, widthY / 2.0f, layer6 + layer5B / 2.0f);
        Utils3D.addText3D(scene, "L5B", textPos, scaleText, Utils3D.grey);

        textPos = new Vector3f(textPosX, widthY / 2.0f, layer6 / 2.0f);
        Utils3D.addText3D(scene, "L6", textPos, scaleText, Utils3D.grey);

        box.setCoordinates(0, verts);
        setGeometry(box);
    }

    public void addLines() {
        LineArray ver_Line = new LineArray(8, LineArray.COORDINATES);
        Point3d verts2[] = new Point3d[8];

        /*
        //bottom two lines
        verts2[0] = new Point3d(0, 0, 0);
        verts2[1] = new Point3d(0, 1.6, 0);
        verts2[2] = new Point3d(lengthX, 0, 0);
        verts2[3] = new Point3d(lengthX, 1.6, 0);

        //top two lines
        verts2[4] = new Point3d(0, 0, -0.6);
        verts2[5] = new Point3d(0, 1.6, -0.6);
        verts2[6] = new Point3d(lengthX, 0, -0.6);
        verts2[7] = new Point3d(lengthX, 1.6, -0.6);

         *
         */

        //bottom two lines
        verts2[0] = new Point3d(0, 0, 0);
        verts2[1] = new Point3d(0, 0, heightZ);
        verts2[2] = new Point3d(lengthX, 0, 0);
        verts2[3] = new Point3d(lengthX, 0, heightZ);
        //top two lines
        verts2[4] = new Point3d(0, widthY, 0);
        verts2[5] = new Point3d(0, widthY, heightZ);
        verts2[6] = new Point3d(lengthX, widthY, 0);
        verts2[7] = new Point3d(lengthX, widthY, heightZ);

        ver_Line.setCoordinates(0, verts2);
        setGeometry(ver_Line);
    }
}
