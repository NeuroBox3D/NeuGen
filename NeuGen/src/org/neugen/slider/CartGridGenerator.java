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
package org.neugen.slider;

import javax.vecmath.Point3f;
import org.neugen.datastructures.Segment;

/**
 * Cartesian data grid generator.
 * 
 * @author alwa
 *
 */
public final class CartGridGenerator<Float> {

    protected DataGrid<Float> grid = new DataGrid<Float>(
            new DataGrid.CoordinateType[]{
                DataGrid.CoordinateType.Z,
                DataGrid.CoordinateType.X,
                DataGrid.CoordinateType.Y
            });
    private float spacings[];
    protected float gridsOrigins[];

    public CartGridGenerator(Point3f gridOrigins, Point3f spacings) {
        gridsOrigins = new float[]{
                    gridOrigins.x,
                    gridOrigins.y,
                    gridOrigins.z
                };
        this.spacings = new float[]{
                    spacings.x,
                    spacings.y,
                    spacings.z
                };
    }

    public void resolve(Segment segment, Float data) {
        float minVertex[] = new float[3];
        float maxVertex[] = new float[3];
        float points[][] = new float[2][3];
        float radii[] = {segment.getStartRadius(), segment.getEndRadius()};

        Point3f tmp = segment.getStart();
        float[] sstart = new float[3];
        sstart[0] = tmp.x;
        sstart[1] = tmp.y;
        sstart[2] = tmp.z;

        tmp = segment.getEnd();
        float[] send = new float[3];
        send[0] = tmp.x;
        send[1] = tmp.y;
        send[2] = tmp.z;

        points[0] = sstart;
        points[1] = send;
        //segment.startPoint.get(points[0]);
        //segment.endPoint.get(points[1]);

        // Init edges of the local data grid
        for (int cIndex = 0; cIndex < 3; ++cIndex) {
            maxVertex[cIndex] = Math.max(points[0][cIndex] + radii[0], points[1][cIndex] + radii[1]);
        }

        for (int cIndex = 0; cIndex < 3; ++cIndex) {
            minVertex[cIndex] = Math.min(points[0][cIndex] - radii[0], points[1][cIndex] - radii[1]);
        }
        // correct on spacing
        for (int cIndex = 0; cIndex < 3; ++cIndex) {
            minVertex[cIndex] = (float) Math.ceil((minVertex[cIndex] - gridsOrigins[cIndex]) / spacings[cIndex]) * spacings[cIndex];
            maxVertex[cIndex] = (float) Math.ceil((maxVertex[cIndex] - gridsOrigins[cIndex]) / spacings[cIndex]) * spacings[cIndex];
        }
        resolve(segment, minVertex, maxVertex, data);
    }

    public void resolve(Segment segment, float minVertex[], float maxVertex[], Float data) {
        int coord[] = new int[3];
        float coordF[] = new float[3];
        for (float x = minVertex[0]; !(x > maxVertex[0]); x += spacings[0]) {
            for (float y = minVertex[1]; !(y > maxVertex[1]); y += spacings[1]) {
                for (float z = minVertex[2]; !(z > maxVertex[2]); z += spacings[2]) {
                    Point3f p = new Point3f(x, y, z);
                    if (Seeker.isInside(p, segment)) {
                        p.get(coordF);
                        for (int cIndex = 0; cIndex < 3; ++cIndex) {
                            coord[cIndex] = Math.round((coordF[cIndex] - gridsOrigins[cIndex]) / spacings[cIndex]);
                        }
                        grid.set(coord, data);
                    }
                }
            }
        }
    }

    public DataGrid<Float> getGrid() {
        return grid;
    }
}
