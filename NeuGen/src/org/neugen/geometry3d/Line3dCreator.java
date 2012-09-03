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
