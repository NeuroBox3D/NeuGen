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
