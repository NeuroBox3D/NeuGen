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
/**
 * 
 */
package org.neugen.slider;

import java.util.List;

import javax.vecmath.Point3f;
import org.neugen.datastructures.Segment;

/**
 * @author alwa
 *
 */
public final class Seeker {

    public static boolean isInside(Point3f p, List<Segment> segments) {
        for (Segment segment : segments) {
            if (isInside(p, segment)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInside(Point3f p, Segment segment) {
        Point3f startPoint = segment.getStart();
        Point3f endPoint = segment.getEnd();
        Point3f direction = new Point3f();
        direction.sub(endPoint, startPoint);
        float segLen = endPoint.distance(startPoint);
        direction.scale(1.f / segLen);

        /*
         *  if (((x - c_start) * c_direction).sum() < 0.0) return val_env;
        if (((x - c_end) * c_direction).sum() > 0.0) return val_env;
        float t = ((x - c_start) * c_direction).sum(); // length of projection of x on c_direction
        float radius = c_radius_start + t * (c_radius_end - c_radius_start) / c_length; // linear function for radius along segment
        if (d == 2) {
        valarray<float> c_normal(2);
        c_normal[0] = c_direction[1];
        c_normal[1] = -c_direction[0];
        if (fabs((c_normal * (x - c_start)).sum()) <= radius) return val_segment;
        }
        if (d == 3) {
        valarray<float> v(3), diff(x - c_start);
        v[0] = c_direction[1] * diff[2] - c_direction[2] * diff[1];
        v[1] = c_direction[2] * diff[0] - c_direction[0] * diff[2];
        v[2] = c_direction[0] * diff[1] - c_direction[1] * diff[0];
        if (length(v) <= radius) return val_segment;
        }
        return val_env;
         */

        float projLen = 0.f;
        {
            Point3f dif = new Point3f();
            dif.sub(p, startPoint);
            projLen = mul(dif, direction);
            if (projLen < 0.f) {
                return false;
            }
        }

        {
            Point3f dif = new Point3f();
            dif.sub(p, endPoint);
            if (mul(dif, direction) > 0.f) {
                return false;
            }
        }

        {
            Point3f projection = new Point3f();
            projection.scale(projLen, direction);
            projection.add(startPoint);
            float rad = segment.getStartRadius() + projLen / segLen * (segment.getEndRadius() - segment.getStartRadius());
            float distance = projection.distance(p);
            if (rad < distance) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static float mul(Point3f p1, Point3f p2) {
        return p1.x * p2.x + p1.y * p2.y + p1.z * p2.z;
    }
}
