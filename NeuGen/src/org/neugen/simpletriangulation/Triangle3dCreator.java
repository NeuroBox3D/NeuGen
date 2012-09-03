/*
 * Triangle3dCreator.java
 *
 * Created on 6. M�rz 2007
 *
 */
package org.neugen.simpletriangulation;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
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

        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(triangleColor);
        this.triangleAppearance.setColoringAttributes(ca);

        TransparencyAttributes ta = new TransparencyAttributes();
        ta.setTransparency(0.5f);
        this.triangleAppearance.setTransparencyAttributes(ta);

        this.triangleContainer = new Shape3D();
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

    // add triangle to container
    private void addTriangleToContainer(Shape3D container, Point3f p1, Point3f p2, Point3f p3) {
        container.addGeometry(createTriangleArrayFromPoints(p1, p2, p3));
    }

    public Shape3D getTriangleContainer() {
        return this.triangleContainer;
    }
}
