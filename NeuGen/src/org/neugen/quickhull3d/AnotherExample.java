/*
 * Copyright John E. Lloyd, 2003. All rights reserved. Permission
 * to use, copy, and modify, without fee, is granted for non-commercial 
 * and research purposes, provided that this copyright notice appears 
 * in all copies.
 *
 * This  software is distributed "as is", without any warranty, including 
 * any implied warranty of merchantability or fitness for a particular
 * use. The authors assume no responsibility for, and shall not be liable
 * for, any special, indirect, or consequential damages, or any damages
 * whatsoever, arising out of or in connection with the use of this
 * software.
 */
package org.neugen.quickhull3d;

/**
 * Simple example usage of QuickHull3D. Run as the command
 *
 * <pre>
 *      java quickhull3d.SimpleExample
 * </pre>
 */
public class AnotherExample {

	/**
	 * Run for a simple demonstration of QuickHull3D.
	 */
	public static void main(String[] args) {
		// x y z coordinates of 6 points
		Point3dQH[] points = new Point3dQH[]{
			new Point3dQH(14.2718, -9.26838, -5.68422),
			new Point3dQH(14.4787, -10.0995, -6.54068),
			new Point3dQH(14.2588, -9.27174, -7.39714),
			new Point3dQH(14.0519, -8.44059, -6.54068),
			new Point3dQH(9.93543, -10.5268, -13.3799),
			new Point3dQH(10.1176, -9.34946, -12.9777),
			new Point3dQH(8.15989, -9.42951, -11.8634),
			new Point3dQH(8.83809, -11.205, -12.9608)
		};

		QuickHull3D hull = new QuickHull3D();
		hull.build(points);

		System.out.println("Vertices:");
		Point3dQH[] vertices = hull.getVertices();
		for (int i = 0; i < vertices.length; i++) {
			Point3dQH pnt = vertices[i];
			System.out.println(pnt.x + " " + pnt.y + " " + pnt.z);
		}

        // WRONG: for (int i = 0; i < vertices.length; i++) !!!
		// RIGHT: for (int i = 0; i < faceIndices.length; i++) !!!
		int[][] faceIndices = hull.getFaces();
		System.out.println("Number of faces: " + faceIndices.length);
		System.out.println("Faces:");
		for (int i = 0; i < faceIndices.length; i++) {
			for (int k = 0; k < faceIndices[i].length; k++) {
				System.out.print(faceIndices[i][k] + " ");
			}
			System.out.println("");
		}
	}
}
