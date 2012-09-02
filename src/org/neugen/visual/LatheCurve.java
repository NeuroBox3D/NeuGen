package org.neugen.visual;

// LatheCurve.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A Lathe Curve is a series of (x,y) points representing a
curve made up of curve segments and straight lines. 

The lathe curve starts at y==0, and moves upwards. All the coords
must be positive, since the intention is to rotate the lathe curve
around the y-axis to make a shape.

The curve segments in a Lathe curve are interpolated from (x,y) pts
using Hermite interpolation. This requires tangents to be supplied
for the points, which are calculated using a variant of the
Catmull-Rom algorithm for splines. 

For a given pair of coordinates (e.g. (x1,y1), (x2,y2)) a curve segment
is created by adding an additional STEP points between them.

Thus, the supplied sequence of (x,y) points is extended to make
a sequence representing the Lathe curve. This extended sequence
is accessible by calling getXs() and getYs().

A given point (x,y) may start a curve segment or a straight line going 
to the next point. We distinguish by making the x-value negative
to mean that it should start a straight line. The negative sign
is removed when the resulting sequence is created.
 *  This is a bit of a hack, but makes a Lathe curve easy to specify
without requiring complex d.s for the input sequence.
 */
import javax.vecmath.*;

public class LatheCurve {
    // number of interpolation points added between two input points

    private final static int STEP = 5;
    double xs[], ys[];     // sequences of (x,y)'s representing the curve
    double height;         // max height of curve (the largest y-value)

    public LatheCurve(double xsIn[], double ysIn[]) {
        checkLength(xsIn, ysIn);
        checkYs(ysIn);
        int numVerts = xsIn.length;

        // set startTangent to be heading to the right
        Point2d startTangent = // tangent for first pt in input sequence
                new Point2d((Math.abs(xsIn[1]) - Math.abs(xsIn[0])) * 2, 0);

        // set endTangent to be heading to the left
        Point2d endTangent = // tangent for last pt in input sequence
                new Point2d((Math.abs(xsIn[numVerts - 1])
                - Math.abs(xsIn[numVerts - 2])) * 2, 0);

        /* Multiplying the tangents by 2 makes them stronger, which makes the
        curve move more in the specified direction. */

        makeCurve(xsIn, ysIn, startTangent, endTangent);
    }  // end of LatheCurve()

    public LatheCurve(double xsIn[], double ysIn[],
            Point2d startTangent, Point2d endTangent) // let the user supply the starting and ending tangents
    {
        checkLength(xsIn, ysIn);
        checkYs(ysIn);
        checkTangent(startTangent);
        checkTangent(endTangent);
        makeCurve(xsIn, ysIn, startTangent, endTangent);
    } // end of LatheCurve()

    private void checkLength(double xsIn[], double ysIn[]) // tests related to the sequences's length
    {
        int numVerts = xsIn.length;

        if (numVerts < 2) {
            System.out.println("Not enough points to make a curve");
            System.exit(0);
        } else if (numVerts != ysIn.length) {
            System.out.println("xsIn[] and ysIn[] do not have the same number of points");
            System.exit(0);
        }
    } // end of checkLength()

    private void checkYs(double[] ysIn) /* Tests of the y-components of the sequence.
    Also find the largest y-value, which becomes the curve's height.
     */ {
        if (ysIn[0] != 0) {
            System.out.println("The first y-coordinate must be 0; correcting it");
            ysIn[0] = 0;
        }

        // find max height and make all y-coords >= 0
        height = ysIn[0];
        for (int i = 1; i < ysIn.length; i++) {
            if (ysIn[i] >= height) {
                height = ysIn[i];
            }
            if (ysIn[i] < 0) {
                System.out.println("Found a negative y-coord; changing it to be 0");
                ysIn[i] = 0;
            }
        }
        if (height != ysIn[ysIn.length - 1]) {
            System.out.println("Warning: max height is not the last y-coordinate");
        }

    }  // end of checkYs()

    private void checkTangent(Point2d tangent) {
        if ((tangent.x == 0) && (tangent.y == 0)) {
            System.out.println("A tangent cannot be (0,0)");
            System.exit(0);
        }
    }  // end of checkTangent()

    private void makeCurve(double xsIn[], double ysIn[],
            Point2d startTangent, Point2d endTangent) {
        int numInVerts = xsIn.length;
        int numOutVerts = countVerts(xsIn, numInVerts);
        xs = new double[numOutVerts];    // resulting sequence after adding extra pts
        ys = new double[numOutVerts];

        xs[0] = Math.abs(xsIn[0]);    // start of curve is initialised
        ys[0] = ysIn[0];
        int startPosn = 1;

        // holds the tangents for the current curve segment between two points
        Point2d t0 = new Point2d();
        Point2d t1 = new Point2d();

        for (int i = 0; i < numInVerts - 1; i++) {
            if (i == 0) {
                t0.set(startTangent.x, startTangent.y);
            } else // use previous t1 tangent
            {
                t0.set(t1.x, t1.y);
            }

            if (i == numInVerts - 2) // next point is the last one
            {
                t1.set(endTangent.x, endTangent.y);
            } else {
                setTangent(t1, xsIn, ysIn, i + 1);   // tangent at pt i+1
            }
            // if xsIn[i] < 0 then use a straight line to link (x,y) to next (x,y)
            if (xsIn[i] < 0) {
                xs[startPosn] = Math.abs(xsIn[i + 1]);    // in case the next pt is -ve also
                ys[startPosn] = ysIn[i + 1];
                startPosn++;
            } else {   // make a Hermite curve between the two points by adding STEP new pts
                makeHermite(xs, ys, startPosn, xsIn[i], ysIn[i], xsIn[i + 1], ysIn[i + 1], t0, t1);
                startPosn += (STEP + 1);
            }
        }
    }  // end of makeCurve()

    private int countVerts(double xsIn[], int num) /* The number of points in the new sequence depends on how many
    curve segments are to be added. Each curve segment between two points adds
    STEP points to the sequence.

    A (x,y) point where x is negative starts a straight line, so no
    new points are added. If s is positive, then STEP points will
    be added.
     */ {
        int numOutVerts = 1;   // counting last coord
        for (int i = 0; i < num - 1; i++) {
            if (xsIn[i] < 0) // straight line starts here
            {
                numOutVerts++;
            } else // curve segment starts here
            {
                numOutVerts += (STEP + 1);
            }
        }
        return numOutVerts;
    }  // end of countVerts()

    private void setTangent(Point2d tangent, double xsIn[], double ysIn[], int i) /* Calculate the tangent at position i
    using Catmull-Rom spline-based interpolation
     */ {
        double xLen = Math.abs(xsIn[i + 1]) - Math.abs(xsIn[i - 1]);   // ignore any -ve
        double yLen = ysIn[i + 1] - ysIn[i - 1];
        tangent.set(xLen / 2, yLen / 2);
    }  // end of setTangent()

    private void makeHermite(double[] xs, double[] ys, int startPosn,
            double x0, double y0, double x1, double y1, Point2d t0, Point2d t1) /* Calculate the Hermite curve's (x,y) coordinates between points
    (x0,y0) and (x1,y1). t0 and t1 are the tangents for those points.

    The (x0,y0) point is not included, since it was added at the
    end of the previous call to makeHermite().

    Store the coordinates in the xs[] an ys[] arrays, starting
    at index startPosn.
     */ {
        double xCoord, yCoord;
        double tStep = 1.0 / (STEP + 1);
        double t;

        if (x1 < 0) // next point is negative to draw a line, make it
        {
            x1 = -x1;      // +ve while making the curve
        }
        for (int i = 0; i < STEP; i++) {
            t = tStep * (i + 1);
            xCoord = (fh1(t) * x0) + (fh2(t) * x1)
                    + (fh3(t) * t0.x) + (fh4(t) * t1.x);
            xs[startPosn + i] = xCoord;

            yCoord = (fh1(t) * y0) + (fh2(t) * y1)
                    + (fh3(t) * t0.y) + (fh4(t) * t1.y);
            ys[startPosn + i] = yCoord;
        }

        xs[startPosn + STEP] = x1;
        ys[startPosn + STEP] = y1;
    }  // end of makeHermite()


    /* Hermite blending functions.
    The first two functions blend the two points, the last two
    blend the two tangents.
    For an explanation of how the functions are derived, see any
    good computer graphics text, e.g. Foley and Van Dam. */
    private double fh1(double t) {
        return (2.0) * Math.pow(t, 3) - (3.0 * t * t) + 1;
    }

    private double fh2(double t) {
        return (-2.0) * Math.pow(t, 3) + (3.0 * t * t);
    }

    private double fh3(double t) {
        return Math.pow(t, 3) - (2.0 * t * t) + t;
    }

    private double fh4(double t) {
        return Math.pow(t, 3) - (t * t);
    }

    // ----------------------------------------
    // access generated coords and height
    public double[] getXs() {
        return xs;
    }

    public double[] getYs() {
        return ys;
    }

    public double getHeight() {
        return height;
    }
}  // end of LatheCurve class
