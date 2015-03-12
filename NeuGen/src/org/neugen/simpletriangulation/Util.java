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
 * Util.java
 *
 * Created on 17. Februar 2007
 *
 * Another idea for the visualization of iso-surfaces is the marching cube idea.
 * This is a very straight forward implementation of a surface triangulation of a cloud
 * of points in 3D in Java. ( the inner points of the object are not considered )
 *
 * The input data is a 3d array of float values (float[ ][ ][ ] data).
 * The algorithm for the surface triangulation involves three basic steps:
 *
 * - find all boundary points in the data set
 * - find all neighbouring boundary points for each boundary point
 * - find three neighbouring boundary points and assign them to a new triangle.
 * It obtains a list of triangles.
 * 
 * 
 * 
 * // //dimensions of volume of voxels
 * //		int VOV_L = this.volumeOfVoxels.getNumberOfVoxelsX();
 * //		int VOV_W = this.volumeOfVoxels.getNumberOfVoxelsY();
 * //		int VOV_H = this.volumeOfVoxels.getNumberOfVoxelsZ();
 * // // test of visualization with sphere
 * // VolumeOfVoxels vv1 = new VolumeOfVoxels(10, 10, 10);
 * // vv1.fillVolumeOfSphere(false);
 * // // Simple visualization by triangles 
 * // Vector<Triangle> triangleVector = new Vector<Triangle>();
 * // Util.triangulation(this.vv.getVoxelsValueAsFloatArray(),
 * // VOV_L, VOV_W, VOV_H, triangleVector);
 * // Triangle3dCreator triangleCreator = new
 * // Triangle3dCreator(ColorUtil.grey);
 * // for (Triangle t : triangleVector) {
 * // triangleCreator.addTriangleToContainer((t.getP1AsScaledPoint3f(1.0f
 * // /
 * // VOV_L, 1.0f / VOV_W, 1.0f / VOV_H)),
 * // (t.getP2AsScaledPoint3f(1.0f / VOV_L, 1.0f / VOV_W, 1.0f /
 * // VOV_H)), (t .getP3AsScaledPoint3f(1.0f / VOV_L, 1.0f / VOV_W,
 * // 1.0f / VOV_H)));
 * // t.printData();
 * // }
 * // contentRoot.addChild(triangleCreator.getTriangleContainer());
 * 
 * 
 * 
 */
package org.neugen.simpletriangulation;

import java.util.Vector;

/**
 * A util class of useful functions for three dimensions
 * 
 * @author Jens Eberhard
 */
public class Util {

    /** Creates a new instance of Util */
    public Util() {
    }

    /**
     * Checks if the voxel at (x,y,z) in dataset d is background ( P(x,y,z) <
     * threshold ) treshold is e.g. 0.05
     */
    public static boolean isBackground(float[][][] d, int x, int y, int z) {
        try {
            return (d[x][y][z] < 0.05);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if the voxel at (x,y,z) in dataset d is a boundary voxel
     *
     * P(x,y,z) is boundary <- : - P(x,y,z) != background && - at least one
     * neighbour is background
     */
    public static boolean isBoundary(float[][][] d, int x, int y, int z) {
        if (isBackground(d, x, y, z)) {
            return false;
        }
        try {
            return (isBackground(d, x - 1, y - 1, z - 1) || isBackground(d, x - 1, y - 1, z) || isBackground(d, x - 1, y - 1, z + 1) || isBackground(d, x - 1, y, z - 1) || isBackground(d, x - 1, y, z) || isBackground(d, x - 1, y, z + 1) || isBackground(d, x - 1, y + 1, z - 1) || isBackground(d, x - 1, y + 1, z) || isBackground(d, x - 1, y + 1, z + 1) ||
                    isBackground(d, x, y - 1, z - 1) || isBackground(d, x, y - 1, z) || isBackground(d, x, y - 1, z + 1) || isBackground(d, x, y, z - 1) || isBackground(d, x, y, z + 1) || isBackground(d, x, y + 1, z - 1) || isBackground(d, x, y + 1, z) || isBackground(d, x, y + 1, z + 1) ||
                    isBackground(d, x + 1, y - 1, z - 1) || isBackground(d, x + 1, y - 1, z) || isBackground(d, x + 1, y - 1, z + 1) || isBackground(d, x + 1, y, z - 1) || isBackground(d, x + 1, y, z) || isBackground(d, x + 1, y, z + 1) || isBackground(d, x + 1, y + 1, z - 1) || isBackground(d, x + 1, y + 1, z) || isBackground(d,
                    x + 1,
                    y + 1,
                    z + 1));
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * triangulation of the surfaces given in the dataset data - find all
     * boundary points in the dataset - determine for each boundary point p1
     * all neighbouring boundary points - check if one of the neighbouring
     * boundary points of p1 has a neighbouring boundary point which is a
     * neighbour of p1, too -> save the found triangle
     */
    @SuppressWarnings({"unused", "cast"})
    public static void triangulation(float[][][] data, int width, int height, int depth, Vector<Triangle> t) {
        int x, y, z;
        System.out.println("Triangulation into triangles started...");

        // a vector to save all boundary points found in data
        Vector<Point3D> boundaryPoints = new Vector<Point3D>(0);

        for (z = 1; z < depth - 1; z++) {
            for (y = 1; y < height - 1; y++) {
                for (x = 1; x < width - 1; x++) {
                    if (Util.isBoundary(data, x, y, z)) {
                        boundaryPoints.add(new Point3D(x, y, z));
                    }
                }
            }
        }

        if (t.size() > 0) {
            t.clear();
        }

        // neighbours_vector is a vector wich should
        // hold a vector off all neighbour points
        // -> all neighbours of the fifth point
        // in the points vector will be stored in
        // a vector at the 5th position in the
        // neighbours_vector
        // (n.b.: only points wich are at a later position
        // in the points vector to avoid redundancy )
        Vector<Vector<Point3D>> neighbours_vector = new Vector<Vector<Point3D>>(boundaryPoints.size());
        Vector<Point3D> neighbours;
        Point3D p1,  p2,  p3;

        // create list of neighbours for each point
        for (int i = 0; i < boundaryPoints.size(); i++) {
            neighbours = new Vector<Point3D>(0);
            p1 = (Point3D) boundaryPoints.elementAt(i);
            for (int j = i + 1; j < boundaryPoints.size(); j++) {
                p2 = (Point3D) boundaryPoints.elementAt(j);
                if (p1.isNeighbour(p2)) {
                    neighbours.add((Point3D) boundaryPoints.elementAt(j));
                }
            }
            neighbours_vector.add(i, neighbours);
        }

        // allright, we have a vector points with
        // all boundary points

        // we have a vector neighbours_vector
        // with a vector of all neighbours of points[i]
        // position neighbours_vector[i]

        Vector tmp, tmp2;
        @SuppressWarnings("unused")
        boolean similar;
        int i, j, k;

        // go through the entired list of boundary points.
        // the observed point shall be p1
        for (i = 0; i < boundaryPoints.size(); i++) {
            p1 = (Point3D) boundaryPoints.elementAt(i);

            // go through the list of neighbours of p1
            tmp = (Vector) neighbours_vector.elementAt(i);
            for (j = 0; j < tmp.size(); j++) {
                p2 = (Point3D) tmp.elementAt(j);

                // go through the list of neighbours of each
                // neighbour p2 and check if it is neighbour
                // of p1, too
                // if it is a neighbour then create a new
                // triangle
                tmp2 = (Vector) neighbours_vector.elementAt(boundaryPoints.indexOf(p2));
                for (k = 0; k < tmp2.size(); k++) {
                    if (p1.isNeighbour((Point3D) tmp2.elementAt(k))) {
                        t.add(new Triangle(p1, p2, (Point3D) tmp2.elementAt(k)));
                    }
                }
            }
        }
        System.out.println("Triangulation into triangles ended.");
    }
}