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
package org.neugen.geometry3d;

/*
 * Triangle3dCreator.java
 *
 * Created on 06.03.2007
 *
 */
//package surface3d;
import javax.media.j3d.Appearance;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

/**
 * Factory class to create three-dimensional triangles for use in a scene graph
 * 
 * @author Jens P Eberhard
 */
public class Triangle3dCreator {

    private Color3f triangleColor;
    private Appearance triangleAppearance;
    private Shape3D triangleContainer;

    /**
     * Creates a new instance of Triangle3dCreator color the color of the
     * triangle
     */
    public Triangle3dCreator(Color3f color) {
        this.triangleColor = color;
        this.triangleAppearance = new Appearance();

        // ca werden nicht benoetigt
        // ColoringAttributes ca = new ColoringAttributes();
        // ca.setColor(triangleColor);
        // this.triangleAppearance.setColoringAttributes(ca);

        TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.5f);
        this.triangleAppearance.setTransparencyAttributes(ta);

//		Material material = new Material();
//		material.setAmbientColor(color);
//		material.setShininess(0.5f);
//		material.setLightingEnable(true);
//		this.triangleAppearance.setMaterial(material);

        this.triangleContainer = new Shape3D();
        this.triangleContainer.setAppearance(triangleAppearance);
        triangleContainer.removeGeometry(0);
    }

    public Shape3D getTriangleAsShapeOfTriangleArray(Point3f p1, Point3f p2, Point3f p3) {
        Shape3D triangleArrayShape = new Shape3D();
        triangleArrayShape.removeGeometry(0);
        addTriangleToContainer(triangleArrayShape, p1, p2, p3);
        return triangleArrayShape;
    }

    private TriangleArray createTriangleArrayFromPoints(Point3f p1, Point3f p2, Point3f p3) {
        TriangleArray ta = new TriangleArray(3, TriangleArray.COORDINATES | TriangleArray.COLOR_3);
        ta.setCoordinate(0, p1);
        ta.setCoordinate(1, p2);
        ta.setCoordinate(2, p3);
        for (int i = 0; i < 3; ++i) {
            ta.setColor(i, triangleColor);
        }
        return ta;
    }

    public void addTriangleToContainer(Point3f p1, Point3f p2, Point3f p3) {
        addTriangleToContainer(triangleContainer, p1, p2, p3);
    }

    private void addTriangleToContainer(Shape3D container, Point3f p1, Point3f p2, Point3f p3) {
        container.addGeometry(createTriangleArrayFromPoints(p1, p2, p3));
    }

    public Shape3D getTriangleContainer() {
        return this.triangleContainer;
    }
}
