/*
 * AppearanceCreator.java
 *
 * Created on 14. M�rz 2007
 *
 */
package org.neugen.surface3d;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;

/**
 * Simple utility class to provide some useful appearance creators as static functions.
 *
 * @author Jens P Eberhard
 */
public class AppearanceCreator {

    /** Creates a new instance of AppearanceCreator */
    public AppearanceCreator() {
    }

    @SuppressWarnings("unused")
    private static Appearance createMaterialAppearance() {
        Appearance materialAppear = new Appearance();
        PolygonAttributes polyAttrib = new PolygonAttributes();
        polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
        materialAppear.setPolygonAttributes(polyAttrib);
        Material material = new Material();
        material.setDiffuseColor(ColorUtil.red);
        materialAppear.setMaterial(material);
        return materialAppear;
    }

    @SuppressWarnings("unused")
    private static Appearance createWireFrameAppearance() {
        Appearance materialAppear = new Appearance();
        PolygonAttributes polyAttrib = new PolygonAttributes();
        polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        materialAppear.setPolygonAttributes(polyAttrib);
        ColoringAttributes redColoring = new ColoringAttributes();
        redColoring.setColor(ColorUtil.red);
        materialAppear.setColoringAttributes(redColoring);
        return materialAppear;
    }
}
