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
import org.apache.log4j.Logger;
import org.neugen.datastructures.Region;

/**
 *
 * @author sergeiwolf
 *
 *
 * Die Test-Region mißt 600 x 600 x 900 Miktometer.
 * 900 Mikrometer hoch, ref: The hippocampus book S134, 135
 *
 * stratum oriens ca.:      200 um
 * stratum pyramidale ca.:  100 um
 * stratum radiatum ca. :    500 um
 * stratum lacunosum/moleculare ca.: 100 um.
 */
public final class RegionCA1 extends Shape3D {

    //private final static float scale = 0.001f;
    private final float lengthX;
    private final float widthY;
    private final float heightZ;
    private final float stratumOriens;
    private final float stratumPyramidale;
    private final float stratumRadiatum;
    private final float stratumLacunosum;
    private float scale;
    private static final Logger logger = Logger.getLogger(RegionCA1.class.getName());

    public RegionCA1(float scale) {
        super();
        this.scale = scale;
        Region.Param.CA1Param regPar = Region.Param.getInstance().getCa1Param();
        lengthX = regPar.getLength() * scale;
        widthY = regPar.getWidth() * scale;
        stratumOriens = regPar.getStratumOriens() * scale;
        stratumPyramidale = regPar.getStratumPyramidale() * scale;
        stratumRadiatum = regPar.getStratumRadiatum() * scale;
        stratumLacunosum = regPar.getStratumLacunosum() * scale;
        heightZ = regPar.getHeight() * scale;

        Appearance ap = new Appearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(Utils3D.grey);
        ap.setColoringAttributes(ca);
        TransparencyAttributes myTA = new TransparencyAttributes();
        myTA.setTransparency(0.7f);
        myTA.setTransparencyMode(TransparencyAttributes.FASTEST);
        ap.setTransparencyAttributes(myTA);
        //render the Box as a wire frame
        PolygonAttributes polyAttrbutes = new PolygonAttributes();
        polyAttrbutes.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
        ap.setPolygonAttributes(polyAttrbutes);
        setAppearance(ap);
    }

    public void addBox(TransformGroup scene) {
        QuadArray box = new QuadArray(20, QuadArray.COORDINATES);
        Point3d verts[] = new Point3d[20];
        //float scaleText = 0.025f;
        float scaleText = 3.0f * scale;
        //float leftPos = -3.0f * scale;
        float textPosX = (lengthX + 250.0f * scale);
        logger.info("textPosX " + textPosX);
        //unten
        // stratum oriens 0.002 mm
        Vector3f textPos = new Vector3f(textPosX, widthY / 2.0f, stratumOriens / 2.0f);
        Utils3D.addText3D(scene, "stratum oriens", textPos, scaleText, Utils3D.grey);
        verts[0] = new Point3d(0, 0, 0);
        verts[1] = new Point3d(lengthX, 0, 0);
        verts[2] = new Point3d(lengthX, widthY, 0);
        verts[3] = new Point3d(0, widthY, 0);

        // stratum pyramidale 0.001 mm
        textPos = new Vector3f(textPosX, widthY / 2.0f, stratumOriens + stratumPyramidale / 2.0f);
        Utils3D.addText3D(scene, "stratum pyramidale", textPos, scaleText, Utils3D.grey);
        verts[4] = new Point3d(0, 0, stratumOriens);
        verts[5] = new Point3d(lengthX, 0, stratumOriens);
        verts[6] = new Point3d(lengthX, widthY, stratumOriens);
        verts[7] = new Point3d(0, widthY, stratumOriens);

        float stratumPyramidaleEnd = stratumOriens + stratumPyramidale;
        // stratum radiatum 0.005 mm
        textPos = new Vector3f(textPosX, widthY / 2.0f, stratumPyramidaleEnd + stratumRadiatum / 2.0f);
        Utils3D.addText3D(scene, "stratum radiatum", textPos, scaleText, Utils3D.grey);
        verts[8] = new Point3d(0, 0, stratumPyramidaleEnd);
        verts[9] = new Point3d(lengthX, 0, stratumPyramidaleEnd);
        verts[10] = new Point3d(lengthX, widthY, stratumPyramidaleEnd);
        verts[11] = new Point3d(0, widthY, stratumPyramidaleEnd);

        float stratumRadiatumEnd = stratumPyramidaleEnd + stratumRadiatum;
        // stratum lacunosum/moleculare 0.001 mm hoch
        textPos = new Vector3f(textPosX, widthY / 2.0f, stratumRadiatumEnd + stratumLacunosum / 1.5f);
        Utils3D.addText3D(scene, "stratum lacunosum-moleculare", textPos, scaleText, Utils3D.grey);
        verts[12] = new Point3d(0, 0, stratumRadiatumEnd);
        verts[13] = new Point3d(lengthX, 0, stratumRadiatumEnd);
        verts[14] = new Point3d(lengthX, widthY, stratumRadiatumEnd);
        verts[15] = new Point3d(0, widthY, stratumRadiatumEnd);

        // oben
        verts[16] = new Point3d(0, 0, heightZ);
        verts[17] = new Point3d(lengthX, 0, heightZ);
        verts[18] = new Point3d(lengthX, widthY, heightZ);
        verts[19] = new Point3d(0, widthY, heightZ);

        box.setCoordinates(0, verts);
        setGeometry(box);
    }

    public void addLines() {
        LineArray ver_Line = new LineArray(8, LineArray.COORDINATES);
        Point3d verts2[] = new Point3d[8];

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
