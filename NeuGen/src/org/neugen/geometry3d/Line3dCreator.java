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
/*
 * Line3dCreator.java
 *
 * Created on 31. Mai 2007, 22:48
 *
 */
package org.neugen.geometry3d;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

/**
 * Factory class to create three-dimensional lines for use in a scene graph
 * 
 * @author Jens P Eberhard
 */
public class Line3dCreator {

    private Color3f lineColor;
    private Appearance lineAppearance;
    private Shape3D lineContainer;

    /**
     * Creates a new instance of Line3dCreator color the color of the line
     */
    public Line3dCreator(Color3f color) {
        this.lineColor = color;
        this.lineAppearance = new Appearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(lineColor);
        this.lineAppearance.setColoringAttributes(ca);
        this.lineContainer = new Shape3D();
        this.lineContainer.removeGeometry(0);
    }

    public Shape3D getLineAsShapeOfLineArray(Point3d p1, Point3d p2) {
        Shape3D lineArrayShape = new Shape3D();
        lineArrayShape.removeGeometry(0);
        addLineToContainer(lineArrayShape, p1, p2);
        return lineArrayShape;
    }

    private LineArray createLineArrayFromPoints(Point3d p1, Point3d p2) {
        LineArray la = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        la.setCoordinate(0, p1);
        la.setCoordinate(1, p2);
        for (int i = 0; i < 2; ++i) {
            la.setColor(i, lineColor);
        }
        return la;
    }

    public void addLineToContainer(Point3d p1, Point3d p2) {
        addLineToContainer(lineContainer, p1, p2);
    }

    private void addLineToContainer(Shape3D container, Point3d p1, Point3d p2) {
        container.addGeometry(createLineArrayFromPoints(p1, p2));
    }

    public Shape3D getLineContainer() {
        return this.lineContainer;
    }
}
