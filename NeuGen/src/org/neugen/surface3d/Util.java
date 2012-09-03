package org.neugen.surface3d;

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
 */
//package simpletriangulation;
import java.util.ArrayList;
import java.util.List;

import org.neugen.simpletriangulation.Point3D;
import org.neugen.simpletriangulation.Triangle;

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
            return (isBackground(d, x - 1, y - 1, z - 1) || isBackground(d, x - 1, y - 1, z) || isBackground(d, x - 1, y - 1, z + 1) || isBackground(d, x - 1, y, z - 1) || isBackground(d, x - 1, y, z) || isBackground(d, x - 1, y, z + 1) || isBackground(d, x - 1, y + 1, z - 1) || isBackground(d, x - 1, y + 1, z) || isBackground(d, x - 1, y + 1, z + 1)
                    || isBackground(d, x, y - 1, z - 1) || isBackground(d, x, y - 1, z) || isBackground(d, x, y - 1, z + 1) || isBackground(d, x, y, z - 1) || isBackground(d, x, y, z + 1) || isBackground(d, x, y + 1, z - 1) || isBackground(d, x, y + 1, z) || isBackground(d, x, y + 1, z + 1)
                    || isBackground(d, x + 1, y - 1, z - 1) || isBackground(d, x + 1, y - 1, z) || isBackground(d, x + 1, y - 1, z + 1) || isBackground(d, x + 1, y, z - 1) || isBackground(d, x + 1, y, z) || isBackground(d, x + 1, y, z + 1) || isBackground(d, x + 1, y + 1, z - 1) || isBackground(d, x + 1, y + 1, z) || isBackground(d,
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
    @SuppressWarnings({"unused", "unchecked", "cast"})
    public static void triangulation(float[][][] data, int width, int height, int depth, List<Triangle> t) {
        int x, y, z;
        System.out.println("Triangulation into triangles started...");

        /*
        for (z = 0; z < depth; z++) {
        for (y = 0; y < height; y++) {
        for (x = 0; x < width; x++) {
        System.out.println("data[" + x + "][" + y + "][" + z + "]: " + data[x][y][z]);
        }
        }
        }
         * 
         */

        // a vector to save all boundary points found in data
        List<Point3D> boundaryPoints = new ArrayList<Point3D>(0);

        for (z = 1; z < depth - 1; z++) {
            for (y = 1; y < height - 1; y++) {
                for (x = 1; x < width - 1; x++) {
                    if (Util.isBoundary(data, x, y, z)) {
                        boundaryPoints.add(new Point3D(x, y, z));
                    }
                }
            }
        }

        System.out.println("boundaryPoints.size(): " + boundaryPoints.size());
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
        List<List<Point3D>> neighbours_vector = new ArrayList<List<Point3D>>(boundaryPoints.size());
        List<Point3D> neighbours;
        Point3D p1, p2, p3;

        // create list of neighbours for each point
        for (int i = 0; i < boundaryPoints.size(); i++) {
            neighbours = new ArrayList<Point3D>(0);

            p1 = (Point3D) boundaryPoints.get(i);
            for (int j = i + 1; j < boundaryPoints.size(); j++) {
                p2 = (Point3D) boundaryPoints.get(j);
                if (p1.isNeighbour(p2)) {
                    neighbours.add((Point3D) boundaryPoints.get(j));
                }
            }
            neighbours_vector.add(i, neighbours);
        }

        // allright, we have a vector points with
        // all boundary points

        // we have a vector neighbours_vector
        // with a vector of all neighbours of points[i]
        // position neighbours_vector[i]

        List tmp, tmp2;
        @SuppressWarnings("unused")
        boolean similar;
        int i, j, k;

        // go through the entired list of boundary points.
        // the observed point shall be p1
        for (i = 0; i < boundaryPoints.size(); i++) {
            p1 = (Point3D) boundaryPoints.get(i);

            // go through the list of neighbours of p1
            tmp = neighbours_vector.get(i);
            for (j = 0; j < tmp.size(); j++) {
                p2 = (Point3D) tmp.get(j);

                // go through the list of neighbours of each
                // neighbour p2 and check if it is neighbour
                // of p1, too
                // if it is a neighbour then create a new
                // triangle
                tmp2 = neighbours_vector.get(boundaryPoints.indexOf(p2));
                for (k = 0; k < tmp2.size(); k++) {
                    if (p1.isNeighbour((Point3D) tmp2.get(k))) {
                        t.add(new Triangle(p1, p2, (Point3D) tmp2.get(k)));
                    }
                }
            }
        }
        // System.out.println("t.size(): " + t.size());
        for (int g = 0; g < t.size(); g++) {
            // System.out.println("t.get(g): " + t.get(g));
        }

        System.out.println("Triangulation into triangles ended.");
    }
}
