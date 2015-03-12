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

// LatheShape3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A LatheCurve is rotated around the y-axis to make a shape.
Its appearance is set to a colour or a texture. The texture
is wrapped around the shape and stretched to its max height.

The rotation of the curve to make the shape uses code derived
from the SurfaceOfRevolution class by Chris Buckalew.

The colour of the surface is pink, or some specified colour,
or some specified texture.
 */
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.*;
import javax.vecmath.*;
import java.text.DecimalFormat;

public class LatheShape3D extends Shape3D {

    private final static double RADS_DEGREE = Math.PI / 180.0;
    private static final double ANGLE_INCR = 15.0;   // 20, 15, 10, 5
    // the angle turned through to create a face of the solid
    private static final int NUM_SLICES = (int) (360.0 / ANGLE_INCR);
    // default dark and light colours for shape
    private static final Color3f pink = new Color3f(1.0f, 0.75f, 0.8f);
    private static final Color3f darkPink = new Color3f(0.25f, 0.18f, 0.2f);
    private static final Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
    private static final Color3f test = new Color3f(0.25f, 0.41f, 0.88f);
    private double height;    // height of the shape

    public LatheShape3D(double xsIn[], double ysIn[], Texture tex) {
        LatheCurve lc = new LatheCurve(xsIn, ysIn);
        buildShape(lc.getXs(), lc.getYs(), lc.getHeight(), tex);
    }

    public LatheShape3D(double xsIn[], double ysIn[],
            Color3f darkCol, Color3f lightCol) // two colours required: a dark and normal version of the colour
    {
        LatheCurve lc = new LatheCurve(xsIn, ysIn);
        buildShape(lc.getXs(), lc.getYs(), lc.getHeight(),
                darkCol, lightCol);
    }

    // -------------------- build the shape -------------------------------
    private void buildShape(double[] xs, double[] ys, double h) {
        height = h;
        createGeometry(xs, ys, false);
        createAppearance(darkPink, pink);
    } // end of buildShape()

    private void buildShape(double[] xs, double[] ys, double h,
            Color3f darkCol, Color3f lightCol) {
        height = h;
        createGeometry(xs, ys, false);

        if ((darkCol == null) || (lightCol == null)) {
            System.out.println("One of the colours is null; using defaults");
            createAppearance(darkPink, pink);
        } else {
            createAppearance(darkCol, lightCol);
        }
    } // end of buildShape()

    private void buildShape(double[] xs, double[] ys, double h, Texture tex) {
        height = h;
        if (tex == null) {
            System.out.println("The texture is null; using default colours");
            createGeometry(xs, ys, false);
            createAppearance(darkPink, pink);
        } else {
            createGeometry(xs, ys, true);
            createAppearance(tex);
        }
    } // end of buildShape()

    private void createGeometry(double[] xs, double[] ys, boolean usingTexture) /* Create the surface, using the curve defined by the
    (x,y) coords in xs[] and ys[].

    The surface is a QuadArray, which is given normals so
    it will reflect light.

    Texture coordinates may be defined to wrap the image around
    the outside of the shape, starting from the back, wrapping
    counter-clockwise (left to right) around the front, and back
    to the back.
     */ {
        double verts[] = surfaceRevolve(xs, ys);
        // printVerts(verts);

        // use GeometryInfo to compute normals
        GeometryInfo geom = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
        geom.setCoordinates(verts);

        if (usingTexture) {
            geom.setTextureCoordinateParams(1, 2);    // set up texture coords
            TexCoord2f[] texCoords = initTexCoords(verts);
            // printTexCoords(texCoords);
            correctTexCoords(texCoords);
            geom.setTextureCoordinates(0, texCoords);
        }

        NormalGenerator norms = new NormalGenerator();
        norms.generateNormals(geom);

        setGeometry(geom.getGeometryArray());   // convert back to geo array
    }  // end of createGeometry()

    private void printVerts(double[] verts) // for debugging purposes
    {
        DecimalFormat df = new DecimalFormat("0.###");  // 3 dp

        int numPerLine = 6;  // multiple of 3
        int count = 0;
        System.out.println("No. vertices: " + verts.length + "\n");

        for (int i = 0; i < verts.length; i = i + 3) {
            if (count == numPerLine) {
                System.out.println();
                count = 0;
            }
            System.out.print("(" + df.format(verts[i]) + ", "
                    + df.format(verts[i + 1]) + ", "
                    + df.format(verts[i + 2]) + ")  ");
            count += 3;
        }
        System.out.println("\n");
    }  // end of printVerts()

    private TexCoord2f[] initTexCoords(double[] verts) /*
    Wrap the texture around the shape, the left edge starting at
    the back, going counter-clockwise round the front.
    Th texture is stretched along the y-axis so a t value of 1
    equals the max height of the shape.

    s is obtained from the angle made by the (x,z) coordinate;
    t is the scaled height of a coordinate.
     */ {
        int numVerts = verts.length;
        // System.out.println("No. verts.: " + numVerts)

        TexCoord2f[] tcoords = new TexCoord2f[numVerts / 3];

        double x, y, z;
        float sVal, tVal;
        double angle, frac;

        int idx = 0;
        for (int i = 0; i < numVerts / 3; i++) {
            x = verts[idx];
            y = verts[idx + 1];
            z = verts[idx + 2];

            angle = Math.atan2(x, z);       // -PI to PI
            frac = angle / Math.PI;          // -1.0 to 1.0
            sVal = (float) (0.5 + frac / 2);   // 0.0f to 1.0f

            tVal = (float) (y / height);    // 0.0f to 1.0f; uses global height value

            tcoords[i] = new TexCoord2f(sVal, tVal);
            idx += 3;
        }

        return tcoords;
    }  // end of initTexCoords()

    private void printTexCoords(TexCoord2f[] tcoords) // for debugging purposes
    {
        System.out.println("No. tex coords: " + tcoords.length + "\n");

        for (int i = 0; i < tcoords.length; i = i + 2) {
            System.out.println(tcoords[i] + "  " + tcoords[i + 1]);
        }
        System.out.println("\n");
    }  // end of printTexCoords()

    private void correctTexCoords(TexCoord2f[] tcoords) /* Find texture squares where the texture coords are reversed,
    and un-reverse them.

    A reversal occurs at the junction between -PI and PI of tan(x/z)
    (at the back of the shape).

    The s coords on the -PI side will be near 0, the ones on the PI side
    will be near 1, which will make the square show a reversed texture.

    The correction is to change the 0 values to 1's, which will make
    the square show the texture near the s value of 1.
     */ {
        // System.out.println("Checking");
        for (int i = 0; i < tcoords.length; i = i + 4) {
            if ((tcoords[i].x < tcoords[i + 3].x)
                    && (tcoords[i + 1].x < tcoords[i + 2].x)) {  // should not increase
                // System.out.println(tcoords[i] + "  " + tcoords[i+1] );
                // System.out.println(tcoords[i+2] + "  " + tcoords[i+3] + "\n");
                // tcoords[i].x = 1.0f;
                tcoords[i].x = (1.0f + tcoords[i + 3].x) / 2;  // between x and 1.0
                // tcoords[i+1].x = 1.0f;
                tcoords[i + 1].x = (1.0f + tcoords[i + 2].x) / 2;  // between x and 1.0
            }
        }
        // System.out.println("\n");
    }  // end of correctTexCoords()

    private void createAppearance(Color3f darkCol, Color3f lightCol) /* The appearance is a colour which relects light. The dark colour is
    used for ambient effects, the light colour for diffuse. */ {
        Appearance app = new Appearance();

        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);  // see both sides of shape
        app.setPolygonAttributes(pa);

        Material mat = new Material(darkCol, black, lightCol, black, 1.0f);
        // sets ambient, emissive, diffuse, specular, shininess
        mat.setLightingEnable(true);    // lighting switched on
        app.setMaterial(mat);

        setAppearance(app);
    }  // end of createAppearance()

    private void createAppearance(Texture tex) /* The appearance is a texture which relects light.
    The texture is stretched over the shape using the textured coords
    created in createGeometry()
     */ {
        Appearance app = new Appearance();

        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);  // see both sides of shape
        app.setPolygonAttributes(pa);

        // mix the texture and the material colour
        TextureAttributes ta = new TextureAttributes();
        ta.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(ta);

        Material mat = new Material();   // set a default white material
        mat.setSpecularColor(black);     // no specular color
        mat.setLightingEnable(true);
        app.setMaterial(mat);

        app.setTexture(tex);

        setAppearance(app);
    }  // end of createAppearance()

    // --------------- surface revolution methods -----------------
  /*  Deruved from the SurfaceOfRevolution class by
    Chris Buckalew, (c) 1994-2002.
    Part of his FreeFormDef.java example.
    http://www.csc.calpoly.edu/~buckalew/474Lab6-W03.html
     */
    private double[] surfaceRevolve(double xs[], double ys[]) /* Each adjacent pairs of coords in the curve are made into
    one face of the surface.

    A face is constructed in counter-clockwise order, so that
    its normal will face outwards.

    The coords in the xs[] and ys[] arrays are assumed to be
    in increasing order.
     */ {
        checkCoords(xs);

        double[] coords = new double[(NUM_SLICES) * (xs.length - 1) * 4 * 3];

        int index = 0;
        for (int i = 0; i < xs.length - 1; i++) {
            for (int slice = 0; slice < NUM_SLICES; slice++) {
                addCorner(coords, xs[i], ys[i], slice, index);        // bottom right
                index += 3;

                addCorner(coords, xs[i + 1], ys[i + 1], slice, index);    // top right
                index += 3;

                addCorner(coords, xs[i + 1], ys[i + 1], slice + 1, index);  // top left
                index += 3;

                addCorner(coords, xs[i], ys[i], slice + 1, index);      // bottom left
                index += 3;
            }
        }
        return coords;
    } // end of SurfaceRevolve()

    private void checkCoords(double xs[]) {
        // all x points should be >= 0, since we are revolving around the y-axis
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] < 0) {
                System.out.println("Warning: setting xs[" + i + "] from -ve to 0");
                xs[i] = 0;
            }
        }
    }  // end of checkCoords()

    private void addCorner(double[] coords, double xOrig, double yOrig,
            int slice, int index) /* Create a new (x,y,z) coordinate, except when the rotation
    has come back to the start. Then use the original coords.
     */ {
        double angle = RADS_DEGREE * (slice * ANGLE_INCR);

        if (slice == NUM_SLICES) // back at start
        {
            coords[index] = xOrig;
        } else {
            coords[index] = xCoord(xOrig, angle);  // x
        }
        coords[index + 1] = yOrig;   // y

        if (slice == NUM_SLICES) {
            coords[index + 2] = 0;
        } else {
            coords[index + 2] = zCoord(xOrig, angle);   // z
        }
    }  // end of addCorner()


    /* ------------------------------------------
    The following methods rotate the radius unchanged,
    creating a circle of points.
    These methods can be overridden to vary the radii of the
    points, e.g. to make an ellipse.
     */
    protected double xCoord(double radius, double angle) {
        return radius * Math.cos(angle);
    }

    protected double zCoord(double radius, double angle) {
        return radius * Math.sin(angle);
    }
}  // end of LatheShape3D class
