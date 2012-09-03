package org.neugen.simpletriangulation;

/*
 * Triangle.java
 *
 * Created on 17. Februar 2007
 *
 */
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * A very simple implementation of a triangle in 3D space.
 * 
 * @author Jens P Eberhard, Simone Eberhard
 */
public class Triangle {

    org.neugen.simpletriangulation.Point3D p1;
    org.neugen.simpletriangulation.Point3D p2;
    org.neugen.simpletriangulation.Point3D p3;
    Point3f p1f, p2f, p3f;

    /** Creates a new instance of Triangle */
    public Triangle(org.neugen.simpletriangulation.Point3D p12, org.neugen.simpletriangulation.Point3D p22,
            org.neugen.simpletriangulation.Point3D point3D) {
        this.p1 = p12;
        this.p2 = p22;
        this.p3 = point3D;
    }

    public Triangle(Point3f p00, Point3f p11, Point3f p22) {
        this.p1f = p00;
        this.p2f = p11;
        this.p3f = p22;
    }

    public Point3f getP1AsScaledPoint3f(float scaleX, float scaleY, float scaleZ) {
        return new Point3f(scaleX * p1.x, scaleY * p1.y, scaleZ * p1.z);
    }

    public Point3f getP2AsScaledPoint3f(float scaleX, float scaleY, float scaleZ) {
        return new Point3f(scaleX * p2.x, scaleY * p2.y, scaleZ * p2.z);
    }

    public Point3f getP3AsScaledPoint3f(float scaleX, float scaleY, float scaleZ) {
        return new Point3f(scaleX * p3.x, scaleY * p3.y, scaleZ * p3.z);
    }

    /**
     * Checks if Triangle p1 equals this The triangles are not ordered, so
     * check all possible points
     */
    public boolean similarTo(Triangle tr) {
        return (p1 == tr.p1 && p2 == tr.p2 && p3 == tr.p3
                || p1 == tr.p1 && p2 == tr.p3 && p3 == tr.p2
                || p1 == tr.p2 && p2 == tr.p1 && p3 == tr.p3
                || p1 == tr.p2 && p2 == tr.p3 && p3 == tr.p1
                || p1 == tr.p3 && p2 == tr.p2 && p3 == tr.p1
                || p1 == tr.p3 && p2 == tr.p1 && p3 == tr.p2);
    }

    public final void printData() {
        System.out.print("Triangle: ");
        p1.printData();
        p2.printData();
        p3.printData();
        System.out.print("\n");
    }

    /**
     * computes the normal of the triangle surface
     *
     */
    public Vector3f getNormal() {
        Vector3f normal = new Vector3f();
        Vector3f a = new Vector3f(this.p1.x, this.p1.y, this.p1.z);
        Vector3f b = new Vector3f(this.p2.x, this.p2.y, this.p2.z);
        Vector3f c = new Vector3f(this.p3.x, this.p3.y, this.p3.z);

        Vector3f ab = new Vector3f(b.x - a.x, b.y - a.y, b.z - a.z);
        Vector3f ac = new Vector3f(c.x - a.x, c.y - a.y, c.z - a.z);

        // computation of normal by solving equation system
        // I normal.x * ab.x + normal.y * ab.y + normal.z * ab.z = 0;
        // II normal.x * ac.x + normal.y * ac.y + normal.z * ac.z = 0;

        // vector calcNormal(Vector v1, vector v2) {
        // vector normal;
        // normal.x = v1.y * v2.z - v1.z * v2.y;
        // normal.y = v1.z * v2.x - v1.x * v2.z;
        // normal.z = v1.x * v2.y - v1.y * v2.x;
        // float length = sqrt(normal.x*normal.x + normal.y*normal.y +
        // normal.z*normal.z);
        // normal.x /= length;
        // normal.y /= length;
        // normal.z /= length;
        // return normal;
        // }

        normal.x = ab.y * ac.z - ab.z * ac.y;
        normal.y = ab.z * ac.x - ab.x * ac.z;
        normal.z = ab.x * ac.y - ab.y * ac.x;

        System.out.println("normal: " + normal);

        return normal;
    }

    public Vector3f turnNormalOutwards(Vector3f edgeOfNeighbourTriangle) {
        Vector3f outwardsNormal;

        if ((this.getNormal()).dot(edgeOfNeighbourTriangle) < 0.0f) {
            float x = (this.getNormal()).x * -1.0f;
            float y = (this.getNormal()).y * -1.0f;
            float z = (this.getNormal()).z * -1.0f;

            outwardsNormal = new Vector3f(x, y, z);
        } else {
            outwardsNormal = this.getNormal();
        }

        return outwardsNormal;
    }

    public Point3f getP1f() {
        return p1f;
    }

    public Point3f getP2f() {
        return p2f;
    }

    public Point3f getP3f() {
        return p3f;
    }
}
