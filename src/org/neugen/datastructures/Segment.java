package org.neugen.datastructures;

import java.io.Serializable;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.apache.log4j.Logger;

/**
 * Contains the basic class for the construction of a neuron.
 * Segment which is the basic compartment for the axon and dendrites of a neuron.
 * The compartment is a frustum in general, but it can be used as a cylinder for
 * the simplest case.
 *
 * @author Jens Eberhard
 * @author Simone Eberhard
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public class Segment implements Serializable {

    private static final long serialVersionUID = -7611946473283033478L;
    /** use to log messages */
    private static final Logger logger = Logger.getLogger(Segment.class.getName());
    /** Start of segment. */
    protected Point3f startPoint;
    /** End of segment. */
    protected Point3f endPoint;
    /** Vector from the star in the direction of the end. */
    protected Vector3f direction;
    /** Length of segment. */
    protected float length;
    /** Radius at start of segment. */
    protected float startRadius;
    /** Radius at end of segment. */
    protected float endRadius;
    /** Id of segment. */
    protected int id = -1;
    /** Counter for all segments. */
    protected static int segmentCounter;
    /** Reference to parent segment. */
    protected Segment parent;
    protected int parentId;
    /** Reference to child segment. */
    protected Segment child;
    /** Name given to Segment, unique within a neuron. */
    protected String name;
    /** true exactly if this segment carries a synapse. */
    protected boolean synapse;
    /** true exactly if this segment carries a non_functional synapse. */
    protected boolean nfSynapse;
    /** geometric distance of segment to the soma. */
    protected float distToSoma;

    protected boolean test;

    /** Constructor. It initializes only the spatial dimension. */
    public Segment() {
        nfSynapse = false;
        length = 0.0f;
        startRadius = -1.0f;
        endRadius = 0.0f;
        setId(segmentCounter++);
    }

    public Point3f getCenter() {
        Point3f center = new Point3f();
        center.add(startPoint, endPoint);
        center.scale(0.5f);
        return center;
    }

    public static void resetSegCounter() {
        Segment.segmentCounter = 0;
        logger.info("segment counter is:" + Segment.segmentCounter);
    }

    /**
     * Interpolation function.
     * @param startRadius start radius of section
     * @param endRadius of section
     * @param n number of segments
     * @param interpType type of interpolation (unused).
     * By default linear interpolation by number.
     */
    public static float interpolateRadii(float startRadius, float endRadius, int n, int pos, int interpType) {
        return startRadius - (startRadius - endRadius) / n * (pos + 1);
    }

    /**
     * Interpolation function for branch points.
     * To a given startradius, endradius, number of segments, branch point and
     * the ralls coefficients it calculates the radii of the
     * connected sections. Returns a 3D array of the radii: [radParent,radBranch1,radBranch2]
     * The ralls rule is radParent^rall.a=radBranch1^rall.a + radBranch^rall.a and radBranch1= radParent*rall.c
     * @param startRadius is the start radius of the reference string
     * @param endRadius is the end radius of the reference string
     * @param n number of segments in the reference string
     * @param pos is the position of the branch point, number of the segment
     * of main string after branch point
     */
    public static float[] interpolateRadii(float startRadius, float endRadius, int n, int pos, float rallA, float rallC) {
        float[] radii = new float[3];
        if (pos < 1) {
            radii[0] = startRadius;
        }
        if (pos > n) {
            radii[0] = endRadius;
        } else {
            radii[0] = interpolateRadii(startRadius, endRadius, n, pos - 1, 0);
        }
        radii[1] = rallC * radii[0];
        radii[2] = radii[0] * (float) Math.pow(1 - Math.pow(rallC, rallA), 1 / rallA);
        return radii;
    }

    /**
     * Get the value of parentId
     *
     * @return the value of parentId
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * Set the value of parentId
     *
     * @param parentId new value of parentId
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }


    /** 
     * Return the id of segment.
     *
     * @return id of segment.
     */
    public int getId() {
        if (id < 0) {
            id = segmentCounter;
        }
        return id;
    }

    /**
     * Return the distance to soma of segment.
     * 
     * @return distToSoma distance to soma of segment.
     */
    public float getDistToSoma() {
        return distToSoma;
    }

    /**
     * Set the id of the segment.
     *
     * @param id The id of segment.
     */
    public final void setId(int segmentId) {
        this.id = segmentId;
    }


    /**
     * Get the value of child
     *
     * @return the value of child
     */
    public Segment getChild() {
        return child;
    }

    /**
     * Set the value of child.
     *
     * @param child new value of child
     */
    public void setChild(Segment child) {
        this.child = child;
    }

    /**
     * Get the parent of segment.
     *
     * @return parent The parent of segment.
     */
    public Segment getParent() {
        return parent;
    }

    /**
     * Set the parent of the segment.
     *
     * @param parentSegment The parent of segment.
     */
    public void setParent(Segment parentSegment) {
        this.parent = parentSegment;
    }

    /**
     * Get the name of the segment.
     *
     * @return name The name of segment
     */
    public String getName() {
        return name;
    }

    public void setName(String segmentName) {
        this.name = segmentName;
    }

    /**
     * Set a segment.
     * It computes its length and direction.
     *
     * @param sstart The start of segment.
     * @param send The end of segment.
     * @param sradius The radius of segment.
     */
    public void setSegment(Point3f sstart, Point3f send, float sradius) {
        setSegment(sstart, send, sradius, sradius);
    }

    /**
     * Set a segment. 
     * It computes its length and direction.
     *
     * @param sstart The start of segment.
     * @param send The end of segment.
     * @param sradiusstart The radius at start.
     * @param sradiusend The radius at end.
     */
    public void setSegment(Point3f sstart, Point3f send, float sradiusstart, float sradiusend) {
        startPoint = sstart;
        endPoint = send;
        /*
        if (sstart != null && send != null) {
            direction = new Vector3f();
            direction.sub(endPoint, startPoint);
            length = direction.length();
            direction.normalize();
        }
         * 
         */
        startRadius = sradiusstart;
        endRadius = sradiusend;
        distToSoma = 0.0f;
    }

    /**
     * Set a segment. 
     * It computes its length, direction, and its distance to the soma.
     *
     * @param cstart The start of segment.
     * @param cend The end of segment.
     * @param cradiusstart The radius at start.
     * @param cradiusend The radius at end.
     * @param soma_x The location of the soma.
     */
    public void setSegment(Point3f cstart, Point3f cend, float cradiusstart, float cradiusend, Point3f soma_x) {
        startPoint = cstart;
        endPoint = cend;
        startRadius = cradiusstart;
        endRadius = cradiusend;
        direction = new Vector3f();
        direction.sub(endPoint, startPoint);
        length = direction.length();
        direction.normalize();
        Vector3f tmp = new Vector3f();
        tmp.add(startPoint, endPoint);
        tmp.scale(0.5f);
        tmp.sub(soma_x);
        distToSoma = tmp.length();

        logger.debug("Segment from " + startPoint + " to " + endPoint
                + " \n\tstart radius " + startRadius
                + " end radius " + endRadius + " length " + length);
    }


    public void setStart(Point3f start) {
        this.startPoint = start;
    }

    /** 
     * Get start point of segment.
     *
     * @return startPoin The start point of segment.
     */
    public Point3f getStart() {
        if (startPoint == null) {
            if(parent != null) {
                startPoint = parent.getEnd();
            }
        }
        return startPoint;
    }


    public void setEnd(Point3f end) {
        this.endPoint = end;
    }

    /**
     * Get end point of segment.
     *
     * @return endPoint The end point of segment.
     */
    public Point3f getEnd() {
        if (endPoint == null) {
            if (direction != null) {
                Point3f ret = new Point3f();
                ret.scale(length, direction);
                ret.add(startPoint);
                endPoint = ret;
            } else {
                return null;
            }
        }
        return endPoint;
    }

    /**
     * Get length of segment.
     *
     * @return length The length of segment.
     */
    public float getLength() {
        if(length == 0.0f) {
            if(endPoint == null || startPoint == null) {
                logger.error("segment id(endPoint == null || startPoint == null): " + id);
                //throw new NullPointerException();
                return 0.0f;
            }
            direction = new Vector3f();
            direction.sub(endPoint, startPoint);
            length = direction.length();
            direction.normalize();
        }
        return length;
    }

    /**
     * Get start radius of segment.
     *
     * @return start The radius of segment.
     */
    public float getStartRadius() {
        return startRadius;
    }


    public void setEndRadius(float endRadius) {
        this.endRadius = endRadius;
    }

    /**
     * Get end radius of segment.
     *
     * @return endRadius The end radius of segment.
     */
    public float getEndRadius() {
        return endRadius;
    }

    /**
     * Get direction of segment.
     *
     * @return direction The direction of segment.
     */
    public Vector3f getDirection() {
        if(direction == null) {
            direction = new Vector3f();
            direction.sub(endPoint, startPoint);
            direction.normalize();
        }
        return direction;
    }

    /**
     * Get surface area of segment.
     *
     * @return surfaceArea The surface area of segment.
     */
    public float getSurfaceArea() {
        double surfaceArea = Math.PI * (startRadius + endRadius) * Math.sqrt(length * length) + (startRadius - endRadius) * (startRadius - endRadius);
        return (float) surfaceArea;
    }

    /** 
     * Get volume of segment.
     * (volume of frustum: 1/3 PI h (r_1^2 + r_1*r_2 + r_2^2)) (simone)
     * 
     * @return volumeOfSegment The volume of segment
     */
    public float getVolume() {
        float volumeOfSegment = 0.0f;
        //volume= 1/3 PI h (r_1^2 + r_1*r_2 + r_2^2)
        volumeOfSegment = (1.0f / 3.0f) * (float) Math.PI * getLength() * ((startRadius * startRadius) + (startRadius * endRadius) + (endRadius * endRadius));
        //volumeOfSegment = (float) (Math.PI * getLength() * startRadius * startRadius);
        //logger.info( "volumeOfSegment: " + volumeOfSegment );
        if(test) {
            //logger.info("volumen dieses segments wurde schon abgefragt: " + this.id);
        }
        test = true;
        return volumeOfSegment;
    }

    @Override
    public String toString() {
        String ret;
        float sradiusstart, sradiusend;
        sradiusstart = getStartRadius();
        sradiusend = getEndRadius();
        ret = (" " + getStart().x + " " + getStart().y + " " + " " + getStart().z + " " + sradiusstart);
        ret += "\n";
        ret += (" " + getEnd().x + " " + getEnd().y + " " + getEnd().z + " " + sradiusend);
        logger.info(ret);
        return ret;
    }

    /**
     * Help func. for the MorphMLReader class.
     *
     * @param x The x coordinate of the segment.
     * @param y The y coordinate of the segment.
     * @param z The z coordinate of the segment.
     * @param diam The diameter of the segment.
     */
    public void setStart(String x, String y, String z, String diam) {
        startPoint = new Point3f();
        startPoint.x = Float.parseFloat(x);
        startPoint.y = Float.parseFloat(y);
        startPoint.z = Float.parseFloat(z);
        startRadius = (Float.parseFloat(diam)) / 2.0f;
    }

    public void setEnd(String x, String y, String z, String diam) {
        endPoint = new Point3f();
        endPoint.x = Float.parseFloat(x);
        endPoint.y = Float.parseFloat(y);
        endPoint.z = Float.parseFloat(z);
        if (startPoint == null) {
            startPoint = parent.getEnd();
            startRadius = parent.getEndRadius();
        }
        direction = new Vector3f();
        direction.sub(endPoint, startPoint);
        length = direction.length();
        direction.normalize();
        endRadius = (Float.parseFloat(diam)) / 2.0f;
    }

    /** Call this to say this segment has a double sided synapse. */
    public void has_ds_synapse(boolean input) {
        synapse = input;
    }

    /** 
     * Returns true if segment has a double sided synapse.
     *
     * @return synapse The value of synapse.
     */
    public boolean has_ds_synapse() {
        return synapse;
    }

    /** 
     * Returns true if segment has a non functional synapse.
     *
     * @return nfSynapse The value of nfSynapse.
     */
    public boolean has_nf_synapse() {
        return nfSynapse;
    }

    /** Call this to say this segment has a non functional synapse. */
    public void has_nf_synapse(boolean input) {
        nfSynapse = input;
    }
}
