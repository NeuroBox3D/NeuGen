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
package org.neugen.geometry3d;

/*
 * Cube3dFactory.java
 *
 * Created on 17. Februar 2007
 *
 */

/* Example for cube testing */
/*
 * contentRoot.addChild(cubeCreator.getCubeAsShapeOfQuadArrays(0.1f,
 * 0.1f, 0.1f));
 * contentRoot.addChild(cubeCreator.getCubeAsBox(0.15f, 0.15f,
 * 0.15f));
 */
//package surface3d;
import javax.media.j3d.Appearance;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.neugen.datastructures.MiniVoxel;
import org.neugen.datastructures.Voxel;

import com.sun.j3d.utils.geometry.Box;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;

/**
 * Factory class to create three-dimensional cubes for use in a scene graph
 * 
 * @author Jens P Eberhard
 */
public class Cube3dCreator {

    private float cubeLength;
    private float cubeWidth;
    private float cubeHeight;
    //private Color3f cubeColor;
    private Appearance cubeAppearance;
    private Shape3D cubeContainer;

    /**
     * Creates a new instance of Cube3dFactory
     *
     * length the edge length of the cube (in all directions) color the
     * color of the cube
     */
    public Cube3dCreator(float length, float width, float height, Material mat, float transparency) {
        this.cubeLength = length;
        this.cubeWidth = width;
        this.cubeHeight = height;

        //this.cubeColor = color;
        // ca werden nicht benoetigt
        //ColoringAttributes ca = new ColoringAttributes();
        //ca.setColor(cubeColor);
        // this.cubeAppearance.setColoringAttributes(ca);
        this.cubeAppearance = new Appearance();

        //PolygonAttributes pa = new PolygonAttributes();
        //pa.setCullFace(PolygonAttributes.CULL_NONE);
        //this.cubeAppearance.setPolygonAttributes(pa);

        /*
        ColoringAttributes colorAtt = new ColoringAttributes();
        colorAtt.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
        this.cubeAppearance.setColoringAttributes(colorAtt);
         * 
         */

        this.cubeAppearance.setMaterial(mat);
        TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.NICEST, transparency);
        this.cubeAppearance.setTransparencyAttributes(ta);
        this.cubeContainer = new Shape3D();
        this.cubeContainer.setAppearance(cubeAppearance);
        this.cubeContainer.removeGeometry(0);
    }

    public void setCubeLength(float length) {
        this.cubeLength = length;
    }

    public Shape3D getCubeContainer() {
        return this.cubeContainer;
    }

    /*
     * (x, y, z) is the lower left front corner of the cube
     *
     */
    public TransformGroup getCubeAsBox(float x, float y, float z) {
        Transform3D translate = new Transform3D();
        translate.set(new Vector3f(x + 0.5f * cubeLength, y + 0.5f * cubeWidth, z + 0.5f * cubeHeight));
        TransformGroup translateBox = new TransformGroup(translate);

        Box cube = new Box(0.5f * cubeLength, 0.5f * cubeWidth, 0.5f * cubeHeight, cubeAppearance);
        translateBox.addChild(cube);
        return translateBox;
    }

    /*
     * v is the lower left front corner of the cube
     *
     */
    public TransformGroup getCubeAsBox(Voxel v) {
        return getCubeAsBox(v.getX(), v.getY(), v.getZ());
    }

    /*
     * (x, y, z) is the lower left front corner of the cube
     *
     */
    public Shape3D getCubeAsShapeOfQuadArrays(float x, float y, float z) {
        Shape3D quadArrayContainer = new Shape3D();
        quadArrayContainer.removeGeometry(0);
        addCubeToContainer(quadArrayContainer, x, y, z);
        return quadArrayContainer;
    }

    public void addCubeToContainer(float x, float y, float z) {
        addCubeToContainer(cubeContainer, x, y, z);
    }

    public void addCubeToContainer(Voxel v) {
        addCubeToContainer(cubeContainer, v.getX(), v.getY(), v.getZ());
    }

    public void addCubeToContainer(MiniVoxel miniVoxel) {
        addCubeToContainer(cubeContainer, new Float(miniVoxel.getXCoord()).floatValue(),
                new Float(miniVoxel.getYCoord()).floatValue(), new Float(miniVoxel.getZCoord()).floatValue());
    }

    private void addCubeToContainer(Shape3D container, float x, float y, float z) {

        // float test = -100.0f;

        // x=x-100;
        // y=y-100;
        // z=z+test;

        // System.out.println("!!!!!!!!!!!!!!!!!!!");
        //System.out.println("cube point:  " + x + " " + y + " " + z);
        //System.out.println("!!!!!!!!!!!!!!!!!!!");

        Point3f p1 = new Point3f(x, y, z);
        Point3f p2 = new Point3f(x + cubeLength, y, z);
        Point3f p3 = new Point3f(x + cubeLength, y + cubeWidth, z);
        Point3f p4 = new Point3f(x, y + cubeWidth, z);
        Point3f p5 = new Point3f(x, y, z + cubeHeight);
        Point3f p6 = new Point3f(x + cubeLength, y, z + cubeHeight);
        Point3f p7 = new Point3f(x + cubeLength, y + cubeWidth, z + cubeHeight);
        Point3f p8 = new Point3f(x, y + cubeWidth, z + cubeHeight);

        container.addGeometry(createQuadArrayFromPoints(p1, p4, p3, p2)); // bottom
        // face
        container.addGeometry(createQuadArrayFromPoints(p5, p6, p7, p8)); // up
        // face
        container.addGeometry(createQuadArrayFromPoints(p1, p2, p6, p5)); // front
        // face
        container.addGeometry(createQuadArrayFromPoints(p2, p3, p7, p6)); // right
        // face
        container.addGeometry(createQuadArrayFromPoints(p3, p4, p8, p7)); // back
        // face
        container.addGeometry(createQuadArrayFromPoints(p4, p1, p5, p8)); // left
        // face
    }

    private QuadArray createQuadArrayFromPoints(Point3f p1, Point3f p2, Point3f p3, Point3f p4) {
        QuadArray qa = new QuadArray(4, QuadArray.COORDINATES | QuadArray.NORMALS);
        qa.setCoordinate(0, p1);
        qa.setCoordinate(1, p2);
        qa.setCoordinate(2, p3);
        qa.setCoordinate(3, p4);
        return qa;
    }
    /*
    private QuadArray createQuadArrayFromPoints(Point3f p1, Point3f p2, Point3f p3, Point3f p4) {
        QuadArray qa = new QuadArray(4, QuadArray.COORDINATES | QuadArray.COLOR_3);
        qa.setCoordinate(0, p1);
        qa.setCoordinate(1, p2);
        qa.setCoordinate(2, p3);
        qa.setCoordinate(3, p4);
        for (int i = 0; i < 4; ++i) {
            qa.setColor(i, cubeColor);
        }
        return qa;
    }
     *
     */
}
