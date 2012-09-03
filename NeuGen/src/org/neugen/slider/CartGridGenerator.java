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
