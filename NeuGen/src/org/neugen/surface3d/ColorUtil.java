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
package org.neugen.surface3d;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.geometry.Sphere;

/**
 * Color utility and color constants class
 * 
 * @author J. P. Eberhard
 * @created July 7, 2005
 */
public class ColorUtil {

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
     */
    public static void addCoordinateSphereAxesToSceneGraph(BranchGroup scene, float radius) {
        float rad = (radius <= 0.0f) ? 0.1f : radius;
        Appearance ap = new Appearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(white);
        ap.setColoringAttributes(ca);
        scene.addChild(new Sphere(rad, ap));

        LineArray la1 = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        la1.setCoordinate(0, new Point3f());
        la1.setCoordinate(1, new Point3f(5.0f * rad, 0.0f, 0.0f));
        for (int i = 0; i < 2; ++i) {
            la1.setColor(i, white);
        }
        scene.addChild(new Shape3D(la1));
        LineArray la2 = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        la2.setCoordinate(0, new Point3f());
        la2.setCoordinate(1, new Point3f(0.0f, 5.0f * rad, 0.0f));
        for (int i = 0; i < 2; ++i) {
            la2.setColor(i, white);
        }
        scene.addChild(new Shape3D(la2));
        LineArray la3 = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        la3.setCoordinate(0, new Point3f());
        la3.setCoordinate(1, new Point3f(0.0f, 0.0f, 5.0f * rad));
        for (int i = 0; i < 2; ++i) {
            la3.setColor(i, black);
        }
        scene.addChild(new Shape3D(la3));
    }
    /**
     * Color red
     */
    public final static Color3f red = new Color3f(1.0f, 0.0f, 0.0f);
    /**
     * Color green
     */
    public final static Color3f green = new Color3f(0.0f, 1.0f, 0.0f);
    /**
     * Color blue
     */
    public final static Color3f blue = new Color3f(0.0f, 0.0f, 1.0f);
    /**
     * Color yellow
     */
    public final static Color3f yellow = new Color3f(1.0f, 1.0f, 0.0f);
    /**
     * Color cyan
     */
    public final static Color3f cyan = new Color3f(0.0f, 1.0f, 1.0f);
    /**
     * Color magenta
     */
    public final static Color3f magenta = new Color3f(1.0f, 0.0f, 1.0f);
    /**
     * Color white
     */
    public final static Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
    /**
     * Color black
     */
    public final static Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
    /**
     * Color grey
     */
    public final static Color3f grey = new Color3f(0.5f, 0.5f, 0.5f);
    /**
     * Color whitegrey
     */
    public final static Color3f whitegrey = new Color3f(0.9f, 0.9f, 0.9f);
}
