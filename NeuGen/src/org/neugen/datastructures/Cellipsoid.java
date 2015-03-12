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
package org.neugen.datastructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.apache.log4j.Logger;
import org.neugen.datastructures.parameter.NeuronParam;

/**
 * Contains the class for the construction of a soma by an
 * ellipsoid. The semi-axes of the ellipsoid are of lengths given
 * by the vector s_radii in Cartesian coordinates.
 * 
 * @author Jens Eberhard
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public class Cellipsoid implements Serializable {

    private final static long serialVersionUID = -7031930069194524614L;
    /** Use to log messages. */
    private final static Logger logger = Logger.getLogger(Cellipsoid.class.getName());
    public final static int d = 3;
    private Point3f c_mid;
    private Point3f c_radii;
    private Section cylindricRepresentant;
    private Section ellipsoidRepresentant;
    private final List<Section> sections;

    /** 
     * Constructor.
     * It initializes only the spatial dimension.
     */
    public Cellipsoid() {
        c_mid = new Point3f();
        c_radii = new Point3f();
        sections = new ArrayList<Section>();
        ellipsoidRepresentant = new Section();
    }

    public boolean collide(Point3f target, float targetRad) {
        float dist = c_mid.distance(target);
        if (dist < c_radii.x + targetRad) {
            logger.info("die Soma berühren sich");
            return true;
        } else {
            return false;
        }

        //System.out.println(Math.sqrt((ax + start.x - bx) * (ax + start.x - bx) + (ay + start.y - by) * (ay + start.y - by) + (az + start.z - bz) * (az + start.z - bz)));


        /*
        if (Math.sqrt((ax + start.x - bx) * (ax + start.x - bx) + (ay + start.y - by) * (ay + start.y - by) + (az + start.z - bz) * (az + start.z - bz)) <= 2) {
        collided = true;
        return true;

        } else {
        return false;
        }
         *
         */
    }

    public float getMaxRadius() {
        float maxRadius = 0.0f;
        if (cylindricRepresentant != null) {
            for (Segment segment : cylindricRepresentant.getSegments()) {
                float sRad = segment.getStartRadius();
                float eRad = segment.getEndRadius();
                if (sRad > maxRadius) {
                    maxRadius = sRad;
                }
                if (eRad > maxRadius) {
                    maxRadius = eRad;
                }
            }
        }

        if (maxRadius == 0.0f) {
            return c_radii.x;
        }
        return maxRadius;
    }

    public float getAvgRadius() {
        float radius = 0.0f;
        float averageRadius = 0.0f;
        int numSeg = 0;
        if (cylindricRepresentant != null) {
            for (Segment segment : cylindricRepresentant.getSegments()) {
                numSeg++;
                float sRad = segment.getStartRadius();
                float eRad = segment.getEndRadius();
                //length += segment.getLength();
                radius = sRad;
                radius += eRad;
                radius /= 2.0f;
                averageRadius += radius;
            }
        }
        if (averageRadius != 0.0f) {
            averageRadius /= numSeg;
        }

        return averageRadius;
    }

    /**
     * Return the cylindric representant of soma.
     * @return cylindricRepresentant The section of soma.
     */
    public Section getCylindricRepresentant() {
        return cylindricRepresentant;
    }

    /**
     * Set the soma as a section.
     * @param cylindricRep The section of soma.
     */
    public void setCylindricRepresentant(Section cylindricRep) {
        cylindricRepresentant = cylindricRep;
    }

    /**
     * Return section list of soma.
     * @return sections The section list of soma.
     */
    public List<Section> getSections() {
        return sections;
    }

    /**
     * Return the number of soma segments.
     * @return numberOfSomaSegments The number of soma segments.
     */
    public int getNumberOfSomaSegments() {
        int numberOfSomaSegments = 0;
        int numSections = sections.size();
        for (int i = 0; i < numSections; i++) {
            Section section = sections.get(i);
            numberOfSomaSegments += section.getSegments().size();
        }
        return numberOfSomaSegments;
    }

    /**
     * Function to deliver a cylindric representant of a cellipsoid.
     * @return cylindricRepresentant The section of soma.
     */
    public Section cylindricRepresentant() {
        if (cylindricRepresentant != null) {
            return cylindricRepresentant;
        } else {
            cylindricRepresentant = new Section();
            cylindricRepresentant.setId(ellipsoidRepresentant.getId());
            cylindricRepresentant.setName("soma" + cylindricRepresentant.getId());
            List<Segment> segments = cylindricRepresentant.getSegments();
            float r = getMeanRadius();
            float delta = r / 5;
            for (int i = 0; i < 10; i++) {
                Point3f sstart = new Point3f(c_mid);
                Point3f send = new Point3f(c_mid);
                sstart.z += i * delta - r;
                send.z += (i + 1) * delta - r;
                Segment segment = new Segment();
                segment.setSegment(sstart, send, r);
                segments.add(segment);
            }
            return cylindricRepresentant;
        }
    }

    /**
     * Set a cellipsoid.
     * @param smid the mid point.
     * @param sradii the radii.
     */
    public void setCellipsoid(Point3f smid, Point3f sradii) {
        c_mid = smid;
        c_radii = sradii;
    }

    /**
     * Set a cellipsoid as a sphere.
     * @param smid smid the mid point.
     * @param sradius sradius the radius.
     */
    public void setCellipsoid(Point3f smid, float sradius) {
        c_mid = smid;
        c_radii.set(sradius, sradius, sradius);
    }

    public Section getEllipsoid() {
        //ungerade, wegen der Symmetrie der Ellipse
        //int nsegs = 2 * 11 - 1;
        List<Segment> segmentList = ellipsoidRepresentant.getSegments();
        segmentList.clear();

        //segmentList.ensureCapacity(nsegs);
        // in kleinen Schritten
        float step = 0.0f;
        float a = 5.0f;    // in NeuGen: Länge der Zylinderhaelfte
        int ellipsePoints = 2 * (int) a + 1;
        step = a / (ellipsePoints - 1);
        //std::cout << "Schrittweite: " << step << std::endl;
        // Mittelpunktsgleichung 3D der Ellipse
        // x^2/a^2 + y^2/b^2  = 1

        float b = 7.0f;    // // In NeuGen: Radius
        float x = 0.0f;    // Koordinaten, x bewegt sich, entspricht z in NeuGen.
        float y = 0.0f;
        float[] ellipseRadius = new float[ellipsePoints];
        // std::valarray<float> ellipseRadius(ellipsePoints); // sonst ist es wieder eine Kugel
        //y=+(b/a)sqrt(a²-x²)
        //y=-(b/a)sqrt(a²-x²).
        for (int i = 0; i < ellipsePoints; i++) {
            //float det = sqrt(pow(a,2) - pow(x,2));
            float det = (float) Math.sqrt(Math.pow(a, 2) - Math.pow(x, 2));
            //if(det==0) det = 1;
            y = +(b / a) * det;
            //std::cout << "x: " << x << " ," << "y: " << y << std::endl;
            ellipseRadius[i] = y;
            x += step;
        }
        Point3f m = new Point3f(c_mid);
        float r = getMeanRadius();
        m.z -= r;
        Point3f l = new Point3f(m); //start
        m.z += step; //end
        //std::cout << "ellipseRadius.size(): " << ellipseRadius.size() << std::endl;
        for (int j = ellipseRadius.length - 1; j > 0; j--) {
            r = ellipseRadius[j];
            //std::cout << "start: " << l[d - 1] << ", ";
            //std::cout << "end: " << m[d - 1] << ", ";
            //std::cout << "radius: " << r << std::endl;
            //(*ret)[k].set_segment(l, m, r); //start, end, radius
            Segment segment = new Segment();
            segment.setSegment(new Point3f(l), new Point3f(m), r);
            segmentList.add(segment);
            l.z += step;
            m.z += step;
        }

        for (int j = 0; j < ellipseRadius.length; j++) {
            r = ellipseRadius[j];
            //std::cout << "start: " << l[d - 1] << ", ";
            //std::cout << "end: " << m[d - 1] << ", ";
            //std::cout << "radius: " << r << std::endl;
            Segment segment = new Segment();
            segment.setSegment(new Point3f(l), new Point3f(m), r);
            segmentList.add(segment);
            l.z += step;
            m.z += step;
        }
        //std::cout << "ret.size(): " << (*ret).size() << std::endl;
        return ellipsoidRepresentant;
    }

    /**
     * Get mid point of cellipsoid
     * @return s_mid
     */
    public Point3f getMid() {
        return c_mid;
    }

    public void setMid(Point3f mid) {
        c_mid = mid;
    }

    /**
     * Get volume of cellipsoid.
     * @return the volume of cellipsoid
     */
    public float getVolume() {
        if (d == 3) {
            return (float) (4.0 * Math.PI * c_radii.x * c_radii.y * c_radii.z / 3.0);
        }
        if (d == 2) {
            return (float) (Math.PI * c_radii.x * c_radii.y);
        }
        return 0.0f;
    }

    /**
     * Get surface area of cellipsoid
     * @return the surface area of cellipsoid
     */
    public float getSurfaceArea() {
        float area = 0.0f;
        if (getCylindricRepresentant() == null) {
            return 0.0f;
        }
        for (Segment seg : getCylindricRepresentant().getSegments()) {
            area += seg.getSurfaceArea();
        }
        return area;
    }

    /**
     * Get the mean radius of cellipsoid. It returns the geometric mean of a s_radii.
     * @return The mean radius of cellipsoid.
     */
    public float getMeanRadius() {
        if (d == 3) {
            return (float) Math.pow(c_radii.x * c_radii.y * c_radii.z, 0.33333333f);
        }
        if (d == 2) {
            return (float) Math.sqrt(c_radii.x * c_radii.y);
        }
        return 0.0f;
    }

    /**
     * Change the radii of a cellipsoid.
     * @param sradii the radii.
     */
    public void changeRadii(Point3f sradii) {
        c_radii = sradii;
    }

    /**
     * Find out if cellipsoid includes a point given by a space vector. It returns
     * the corresponding value for the cellipsoid or for the environment, respectively.
     * @param x the space vector of the point.
     */
    public float findCellipsoid(Point3f x) {
        x.sub(c_mid);
        Vector3f v = new Vector3f();
        v.x = x.x / c_radii.x;
        v.y = x.y / c_radii.y;
        v.z = x.z / c_radii.z;

        Vector3f w = new Vector3f();
        w.add(v);
        float length = w.lengthSquared();
        if (length <= 1.0) {
            return NeuronParam.getInstance().getSomaParam().getVal();
        } else {
            return 0.0f;
        }
    }

    /**
     * Get the radius, i.e. the distance between the mid point and the surface point of
     * cellipsoid, in a given direction.
     * @param vd the direction vector.
     */
    public float getDirectionRadius(Vector3f vd) {
        Vector3f directionVec = new Vector3f(vd);
        directionVec.normalize();
        if (d == 3) {
            // angles of the vector of direction
            float vd_phi = (float) Math.atan2(directionVec.y, directionVec.x);
            float vd_theta = (float) Math.acos(directionVec.z);
            float sin_phi = (float) Math.sin(vd_phi);
            float cos_phi = (float) Math.cos(vd_phi);
            float sin_theta = (float) Math.sin(vd_theta);
            float cos_theta = (float) Math.cos(vd_theta);
            float v;
            v = cos_phi * cos_phi * sin_theta * sin_theta / (c_radii.x * c_radii.x);
            v += sin_phi * sin_phi * sin_theta * sin_theta / (c_radii.y * c_radii.y);
            v += cos_theta * cos_theta / (c_radii.z * c_radii.z);
            return 1.0f / (float) Math.sqrt(v);
        }
        if (d == 2) {
            float vd_phi = (float) Math.atan2(directionVec.y, directionVec.x);
            float sin_phi = (float) Math.sin(vd_phi);
            float cos_phi = (float) Math.cos(vd_phi);
            float v;
            v = c_mid.x * c_mid.x * c_mid.y * c_mid.y;
            v /= c_mid.x * c_mid.x * sin_phi * sin_phi + c_mid.y * c_mid.y * cos_phi * cos_phi;
            return (float) Math.sqrt(v);
        }
        return 0.0f;
    }

    /**
     * Function to correct the start point of dendrite to the surface of the
     * given soma.
     * @param s the soma.
     * @param start of the dendrite
     * @param end of the dendrite
     */
    public static Point3f correctStart(Cellipsoid s, Point3f start, Point3f end) {
        if (s.findCellipsoid(start) != 0.0f) {
            Vector3f v = new Vector3f();
            v.sub(end, start);
            v.normalize();
            float vDirRad = s.getDirectionRadius(v);
            start.scaleAdd(vDirRad, v, s.getMid());
            /*logger.info("s.getmid: " + s.getmid()[0] + " " + s.getmid()[1] + " " + s.getmid()[2]);
            logger.info("v: " + v[0] + " " + v[1] + " " + v[2]);
            logger.info("vLength: " + vLength);
            logger.info("vDirRad: " + vDirRad);*/
            //logger.info("start: " + start[0] + " " + start[1] + " " + start[2]);
            return start;
        }
        return start;
    }
}
