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
package org.neugen.visual;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.LineArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.TransparencyAttributes;
import org.apache.log4j.Logger;

/**
 * 
 * @author J. P. Eberhard, Simone Eberhard, Sergei Wolf
 * @created July 7, 2005
 */
public final class Utils3D {

    /** use to log message */
    private final static Logger logger = Logger.getLogger(Utils3D.class.getName());
    public final static Color3f IndianRed1 = new Color3f(255.0f / 255.0f, 106.0f / 255.0f, 106.0f / 255.0f);
    public final static Color3f saddleBrown = new Color3f(139.0f / 255.0f, 69.0f / 255.0f, 19.0f / 255.0f);
    public final static Color3f peru = new Color3f(205.0f / 255.0f, 133.0f / 255.0f, 63.0f / 255.0f);
    public final static Color3f burlywood = new Color3f(222.0f / 255.0f, 133.0f / 255.0f, 63.0f / 255.0f);
    public final static Color3f wheat = new Color3f(245.0f / 255.0f, 222.0f / 255.0f, 179.0f / 255.0f);
    public final static Color3f mediumSpringGreen = new Color3f(0.0f / 255.0f, 250.0f / 255.0f, 154.0f / 255.0f);
    public final static Color3f brown = new Color3f(165.0f / 255.0f, 42.0f / 255.0f, 42.0f / 255.0f);
    public final static Color3f brown1 = new Color3f(255.0f / 255.0f, 64.0f / 255.0f, 64.0f / 255.0f);
    public final static Color3f darkSalmon = new Color3f(233.0f / 255.0f, 150.0f / 255.0f, 122.0f / 255.0f);
    public final static Color3f darkOrange = new Color3f(255.0f / 255.0f, 140.0f / 255.0f, 0.0f / 255.0f);
    /** Color DarkBlue          0 0 139 */
    public final static Color3f darkBlue = new Color3f(0.0f / 255.0f, 0.0f / 255.0f, 139.0f / 255.0f);
    /**  Color DarkOliveGreen3	162 205 90 */
    public final static Color3f darkOliveGreen3 = new Color3f(162.0f / 255.0f, 205.0f / 255.0f, 90.0f / 255.0f);
    /** Color Turquoise1	0 245 255 */
    public final static Color3f turquoise1 = new Color3f(0.0f / 255.0f, 245.0f / 255.0f, 255.0f / 255.0f);
    /** Color LightBlue1	191 239 255 */
    public final static Color3f lightBlue1 = new Color3f(191.0f / 255.0f, 239.0f / 255.0f, 255.0f / 255.0f);
    /** Color DeepSkyBlue1	0 191 255 */
    public final static Color3f deepSkyBlue1 = new Color3f(0.0f / 255.0f, 191.0f / 255.0f, 255.0f / 255.0f);
    /** Color DodgerBlue3	24 116 205 */
    public final static Color3f dodgerBlue3 = new Color3f(24.0f / 255.0f, 116.0f / 255.0f, 205.0f / 255.0f);
    /** Color Blue3             0 0 205 */
    public final static Color3f blue3 = new Color3f(0.0f / 255.0f, 0.0f / 255.0f, 205.0f / 255.0f);
    /** Color MidnightBlue	25 25 112 */
    public final static Color3f midnightBlue = new Color3f(25.0f / 255.0f, 25.0f / 255.0f, 112.0f / 255.0f);
    /** Color DodgerBlue	30 144 255 */
    public final static Color3f dodgerBlue = new Color3f(30.0f / 255.0f, 144.0f / 255.0f, 255.0f / 255.0f);
    /** Color DeepSkyBlue	0 191 255 */
    public final static Color3f deepSkyBlue = new Color3f(0.0f / 255.0f, 191.0f / 255.0f, 255.0f / 255.0f);
    /** Color DeepSkyBlue2	0 178 238 */
    public final static Color3f deepSkyBlue2 = new Color3f(0.0f / 255.0f, 178.0f / 255.0f, 238.0f / 255.0f);
    /** Color skyBlue1	0 178 238 */
    public final static Color3f skyBlue1 = new Color3f(135.0f / 255.0f, 206.0f / 255.0f, 255.0f / 255.0f);
    /** Color darkgreyblue */
    public final static Color3f darkgreyblue = new Color3f(0.1f, 0.3f, 0.5f);
    /** Color skyblue  */
    public final static Color3f skyblue = new Color3f(0.0f, 0.5f, 1.0f);
    /** Color red */
    public final static Color3f red = new Color3f(1.0f, 0.0f, 0.0f);
    public final static Color3f orangeRed = new Color3f(1.0f, 69.0f / 255.0f, 0.0f);
    public final static Color3f coral = new Color3f(1.0f, 127.0f / 255.0f, 80.0f / 255.0f);
    /** Color green */
    public final static Color3f green = new Color3f(0.0f, 1.0f, 0.0f);
    /** Color blue */
    public final static Color3f blue = new Color3f(0.0f, 0.0f, 1.0f);
    /** Color yellow */
    public final static Color3f yellow = new Color3f(1.0f, 1.0f, 0.0f);
    public final static Color3f goldenrod = new Color3f(205.0f / 255.0f, 133.0f / 255.0f, 63.0f / 255.0f);
    /** Color cyan */
    public final static Color3f cyan = new Color3f(0.0f, 1.0f, 1.0f);
    /** Color magenta */
    public final static Color3f magenta = new Color3f(1.0f, 0.0f, 1.0f);
    /** Color white */
    public final static Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
    /** Color black */
    public final static Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
    /** Color grey */
    public final static Color3f grey = new Color3f(0.5f, 0.5f, 0.5f);
    /** Color whitegrey */
    public final static Color3f whitegrey = new Color3f(0.9f, 0.9f, 0.9f);

    /**
     * Function to map v = 0.0 to blue rgb[0.0, 0.0, 1.0] and v = 1.0 to red
     * rgb[1.0, 0.0, 0.0] taking the rainbow colors in between.
     */
    public static Color3f mapFloatToRGB(float v) {
        Color3f rgb = new Color3f(Math.max(0.0f, Math.min(-3.5f * v + 4, Math.min(1.0f, Math.max(0.0f,
                7.0f * v - 3)))), Math.max(0.0f, Math.min(-3.5f * v + 3, Math.min(1.0f, Math.max(0.0f,
                7.0f * v - 1)))), Math.max(0.0f, Math.min(-7.0f * v + 3, Math.min(1.0f, Math.max(0.0f,
                3.5f * v + 0.5f)))));
        return rgb;
    }

    /**
     * Function to map v = 0.0 to black rgb[0.0, 0.0, 0.0] and v = 1.0 to
     * white rgb[1.0, 1.0, 1.0] taking greyscale colors in between.
     */
    public static Color3f mapFloatToGreyAdditive(float v) {
        return new Color3f(v, v, v);
    }

    /**
     * Function to map v = 0.0 to white rgb[1.0, 1.0, 1.0] and v = 1.0 to
     * black rgb[0.0, 0.0, 0.0] taking greyscale colors in between.
     */
    public static Color3f mapFloatToGreySubtractive(float v) {
        return new Color3f(1.0f - v, 1.0f - v, 1.0f - v);
    }

    /**
     * Convert HSV -> RGB. To get a pure rainbow set saturation close to
     * 1.0, and value to 1.0, then vary hue from 0.0 (red) to 1.0 (which is
     * red again). Note: Should be used as ColorUtil.convertHSVToRGB(v,
     * 0.9f, 1.0f));
     */
    public static Color3f convertHSVToRGB(float hue, float saturation, float value) {
        int rgbValue = java.awt.Color.HSBtoRGB(hue, saturation, value);
        Color3f rgb = new Color3f((rgbValue & 0x00ff0000) >> 16, (rgbValue & 0x0000ff00) >> 8,
                (rgbValue & 0x000000ff));
        return rgb;
    }

    /**
     * Function to create a colored gradient sheet by a QuadArray, could be
     * used in Background.
     */
    public static Shape3D createColoredGradientSheet(Color3f colorTop, Color3f colorBottom) {
        QuadArray qa = new QuadArray(4, QuadArray.COORDINATES | QuadArray.COLOR_3);
        qa.setCoordinate(0, new Point3f(-1, -1, 0));
        qa.setCoordinate(1, new Point3f(1, -1, 0));
        qa.setCoordinate(2, new Point3f(1, 1, 0));
        qa.setCoordinate(3, new Point3f(-1, 1, 0));
        for (int i = 0; i < 2; ++i) {
            qa.setColor(i, colorBottom);
        }
        for (int i = 2; i < 4; ++i) {
            qa.setColor(i, colorTop);
        }
        return new Shape3D(qa);
    }

    /**
     * Function to create a colored gradient BufferedImage from blue to
     * white, can be used in Background.
     */
    public static BufferedImage createColoredGradientImage(int width, int height) {
        BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = (Graphics2D) bImage.getGraphics();
        g2d.setPaint(new GradientPaint(0, 0, Color.BLUE, 0, height, Color.WHITE));
        g2d.fillRect(0, 0, width, height);
        return bImage;
    }

    /**
     * Function to add a white sphere and axes at the origin of the
     * coordinate system for better orientation. The z-axis is in black.
     *
     * @param scene
     * @param radius
     */
    public static void addCoordinateSphereAxesToSceneGraph(TransformGroup scene, float scale, Point3f coordPos) {
        if (coordPos == null) {
            coordPos = new Point3f();
        }

        Transform3D t3d = new Transform3D();
        Vector3f spherePos = new Vector3f(coordPos);
        t3d.set(spherePos);
        TransformGroup tg = new TransformGroup(t3d);

        float rad = 10.0f * scale;
        Appearance ap = new Appearance();

        TransparencyAttributes myTA = new TransparencyAttributes();
        myTA.setTransparency(0.1f);
        myTA.setTransparencyMode(TransparencyAttributes.NICEST);
        ap.setTransparencyAttributes(myTA);

        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(grey);
        ap.setColoringAttributes(ca);
        tg.addChild(new Sphere(rad, ap));
        scene.addChild(tg);

        LineArray la1 = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        la1.setCoordinate(0, coordPos);
        float axeSize = 100.0f * scale;
        // la1.setCoordinate(1, new Point3f(0.5f * rad, 0.0f, 0.0f));
        la1.setCoordinate(1, new Point3f(coordPos.x + axeSize, coordPos.y, coordPos.z));
        for (int i = 0; i < 2; ++i) {
            la1.setColor(i, yellow);
        }
        scene.addChild(new Shape3D(la1));
        LineArray la2 = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        la2.setCoordinate(0, coordPos);
        la2.setCoordinate(1, new Point3f(coordPos.x, coordPos.y + axeSize, coordPos.z));
        for (int i = 0; i < 2; ++i) {
            la2.setColor(i, red);
        }
        scene.addChild(new Shape3D(la2));
        LineArray la3 = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        la3.setCoordinate(0, coordPos);
        la3.setCoordinate(1, new Point3f(coordPos.x, coordPos.y, coordPos.z + axeSize));
        for (int i = 0; i < 2; ++i) {
            la3.setColor(i, blue);
        }
        scene.addChild(new Shape3D(la3));
    }

    /**
     * Function to add a white sphere and axes at the origin of the
     * coordinate system for better orientation. The z-axis is in black.
     *
     * @param scene
     * @param radius
     */
    public static void addCoordinateSphereAxesToSceneGraphOld(BranchGroup scene, float radius) {
        float rad = (radius <= 0.0f) ? 0.1f : radius;
        Appearance ap = new Appearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(grey);
        ap.setColoringAttributes(ca);
        //scene.addChild(new Sphere(rad, ap));

        LineArray la1 = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        la1.setCoordinate(0, new Point3f());
        // la1.setCoordinate(1, new Point3f(0.5f * rad, 0.0f, 0.0f));
        la1.setCoordinate(1, new Point3f(1f, 0.0f, 0.0f));
        for (int i = 0; i < 2; ++i) {
            la1.setColor(i, yellow);
        }
        scene.addChild(new Shape3D(la1));
        LineArray la2 = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        la2.setCoordinate(0, new Point3f());
        la2.setCoordinate(1, new Point3f(0.0f, 1f, 0.0f));
        for (int i = 0; i < 2; ++i) {
            la2.setColor(i, red);
        }
        scene.addChild(new Shape3D(la2));
        LineArray la3 = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        la3.setCoordinate(0, new Point3f());
        la3.setCoordinate(1, new Point3f(0.0f, 0.0f, 1f));
        for (int i = 0; i < 2; ++i) {
            la3.setColor(i, blue);
        }
        scene.addChild(new Shape3D(la3));


        Text3D text3d = new Text3D(new Font3D(new Font("plain", java.awt.Font.PLAIN, 1), new FontExtrusion()), "x",
                new Point3f(1f, 0.0f, 0.0f));
        // text3d.setString(new String("100 µm"));
        // text3d.setPosition(new Point3f(0.7f, 0.0f, 0.1f));

        Shape3D text3dShape3d = new Shape3D(text3d);
        TransformGroup tg = new TransformGroup();
        Transform3D t3d = new Transform3D();
        //t3d.rotX(Math.PI / 2);
        t3d.setTranslation(new Vector3f(1.0f, 0.0f, 0.0f));
        t3d.setScale(0.025);
        tg.setTransform(t3d);
        tg.addChild(text3dShape3d);
        scene.addChild(tg);


        text3d = new Text3D(new Font3D(new Font("plain", java.awt.Font.PLAIN, 1), new FontExtrusion()), "y",
                new Point3f(0.0f, 1.0f, 0.0f));
        text3dShape3d = new Shape3D(text3d);
        tg = new TransformGroup();
        t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(0.0f, 1.0f, 0.0f));
        t3d.setScale(0.025);
        tg.setTransform(t3d);
        tg.addChild(text3dShape3d);
        scene.addChild(tg);


        text3d = new Text3D(new Font3D(new Font("plain", java.awt.Font.PLAIN, 1), new FontExtrusion()), "z",
                new Point3f(0.0f, 0.0f, 1.0f));
        text3dShape3d = new Shape3D(text3d);
        tg = new TransformGroup();
        t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(0.0f, 0.0f, 1.0f));
        t3d.setScale(0.025);
        tg.setTransform(t3d);
        tg.addChild(text3dShape3d);
        scene.addChild(tg);
    }

    /**
     * add a 100 µm scale bar to the BranchGroup
     *
     * @param scene
     *                the BranchGroup
     */
    public static void addScaleBarToSceneGraph(TransformGroup scene, Point3f scaleBarPos, float scale) {
        if (scaleBarPos == null) {
            scaleBarPos = new Point3f();
        }

        float barPos = -150.0f * scale;
        float hight = 100.0f * scale;
        //logger.info("scaleBarPos: " + scaleBarPos.toString());
        LineArray la = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        //la.setCoordinate(0, new Point3f(scaleBarPos.x + 0.7f, scaleBarPos.y + 0.0f, scaleBarPos.z + 0.9f));
        //la.setCoordinate(1, new Point3f(scaleBarPos.x + 0.7f, scaleBarPos.y + 0.0f, scaleBarPos.z + 1.0f));
        la.setCoordinate(0, new Point3f(barPos, scaleBarPos.y, scaleBarPos.z));
        la.setCoordinate(1, new Point3f(barPos, scaleBarPos.y, scaleBarPos.z + hight));
        for (int i = 0; i < 2; ++i) {
            la.setColor(i, grey);
        }
        scene.addChild(new Shape3D(la));
        Text3D text3d = new Text3D(new Font3D(new Font("plain", java.awt.Font.PLAIN, 1), new FontExtrusion()), "100 µm");
        text3d.setAlignment(Text3D.ALIGN_LAST);
        Shape3D text3dShape3d = new Shape3D(text3d);
        Appearance appearance = new Appearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(grey);
        appearance.setColoringAttributes(ca);
        text3dShape3d.setAppearance(appearance);

        TransformGroup tg = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.rotX(Math.PI / 2);
        float textScale = 30f * scale;
        //t3d.setScale(0.025);
        t3d.setScale(textScale);
        t3d.setTranslation(new Vector3f((barPos + (barPos/10.0f)), scaleBarPos.y, scaleBarPos.z + hight/2.0f));
        tg.setTransform(t3d);
        tg.addChild(text3dShape3d);
        scene.addChild(tg);
        // scene.addChild(text3dShape3d);
    }

    public static void addText3D(TransformGroup bg, String text, Vector3f vPos, float scale, Color3f color) {
        Text3D text3d = new Text3D(new Font3D(new Font("plain", java.awt.Font.PLAIN, 1), new FontExtrusion()), text);
        Shape3D text3dShape3d = new Shape3D(text3d); 
        Appearance appearance = new Appearance();

        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(color);
        appearance.setColoringAttributes(ca);

        TransparencyAttributes myTA = new TransparencyAttributes();
        myTA.setTransparency(0.3f);
        myTA.setTransparencyMode(TransparencyAttributes.NICEST);
        appearance.setTransparencyAttributes(myTA);

        text3dShape3d.setAppearance(appearance);

        TransformGroup tg = new TransformGroup();
        Transform3D t3d = new Transform3D();

        t3d.rotX(Math.PI / 2);
        t3d.setTranslation(vPos);
        t3d.setScale(scale * 15.0f);

        tg.setTransform(t3d);
        tg.addChild(text3dShape3d);
        bg.addChild(tg);
    }

    public static void addCA1Region(TransformGroup objRoot, float scale) {
        RegionCA1 A = new RegionCA1(scale);
        RegionCA1 B = new RegionCA1(scale);
        A.setPickable(false);
        B.setPickable(false);
        A.addBox(objRoot);
        B.addLines();
        TransformGroup ca1TG = new TransformGroup();
        //Transform3D ca1T3D = new Transform3D();
        //ca1T3D.setRotation(new AxisAngle4f(1f, 0f, 0f, (float) Math.toRadians(90)));
        //ca1TG.setTransform(ca1T3D);
        ca1TG.addChild(A);
        ca1TG.addChild(B);
        objRoot.addChild(ca1TG);
    }

    public static void addColumn(TransformGroup objRoot, float scale) {
        CorticalColumn A = new CorticalColumn(scale);
        CorticalColumn B = new CorticalColumn(scale);
        A.setPickable(false);
        B.setPickable(false);
        A.addBox(objRoot);
        B.addLines();
        TransformGroup columnTG = new TransformGroup();
        //Transform3D columnT3D = new Transform3D();
        //columnT3D.setRotation(new AxisAngle4f(1f, 0f, 0f, (float) Math.toRadians(90)));
        //columnTG.setTransform(columnT3D);
        columnTG.addChild(A);
        columnTG.addChild(B);
        objRoot.addChild(columnTG);
    }

    /**
     * @param rT red value between 0.0f and 255.0f
     * @param gT green value between 0.0f and 255.0f
     * @param bT blue value between 0.0f and 255.0f
     * @return a vector containing red, green and blue values between 0.0f and 1.0f
     */
    public static Vector3f convertTwoHundredFiftyFiveToOne(float rT, float gT, float bT) {
        //System.out.println("!!hier!!");
        float rO = rT / 255.0f;
        float gO = rT / 255.0f;
        float bO = rT / 255.0f;
        //System.out.println("rgb: " + rO + " " + gO + " " + bO);
        Vector3f colorVector3f = new Vector3f(rO, gO, bO);
        return colorVector3f;
    }
}
