package org.neugen.datastructures.neuron;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.apache.log4j.Logger;
import org.neugen.utils.Vrand;
import org.neugen.datastructures.Axon;
import org.neugen.datastructures.CellBase;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.DendriteSection;
import org.neugen.datastructures.Pair;
import org.neugen.datastructures.Region;
import org.neugen.datastructures.Section;
import org.neugen.datastructures.parameter.NeuronParam;
import org.neugen.gui.Trigger;

/**
 * Contains the base class for the construction of a neuron. It also contains
 * the lower left corner and upper right corner of the cortical column which
 * are static class elements.
 * The soma of the neuron is constructed by a d-dimensional sphere.
 * The axon is given by the class Axon, and the dendrites are given by a list of
 * dendrites of the class Dendrite.
 *
 * @author Jens Eberhard
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public class NeuronBase extends CellBase implements Serializable, Neuron {

    private static final long serialVersionUID = -4623546473283033478L;
    /** Use to log messages. */
    protected static Logger logger = Logger.getLogger(NeuronBase.class.getName());
    //public final static int d = DataConstants.NEUGEN_DIM;
    /** The random number generator. */
    protected static transient Vrand drawNumber;
    protected static transient Vrand basalRandomNumber;
    /** The axon of neuron. */
    protected final Axon axon;
    /** The list of dendrites of neuron. */
    protected final List<Dendrite> dendrites;
    /** The list of undefined section of neuron. */
    protected final List<Section> undefinedSections;
    /** The index of neuron. */
    protected int index;
    /** The size of neuron. */
    protected Point3f neuronSize;
    protected boolean collide;


    /**
     * Constructor.
     * It initializes the list of dendrites.
     */
    public NeuronBase() {
        axon = new Axon();
        dendrites = new ArrayList<Dendrite>();
        undefinedSections = new ArrayList<Section>();
    }

    /**
     * Function to get the distance distribution of furkations (branchpoints)
     * for neuron. The distance is related to the soma of the neuron. It returns an
     * array with num_interval + 1 components where the zeroth component is the total
     * number of forks of the neuron.
     * @param max_dist_from_soma the maximal distance from the soma which is scanned.
     * @param num_interval the number of interval between the distance 0 and max_dist_from_soma.
     * @param den_or_axon for den_or_axon = 0 scan the dendritic tree, for den_or_axon = 1 scan the axonal
     * tree, and for den_or_axon = 2 scan both trees of neuron.
     */
    public List<Integer> getForkDistribution(float max_dist_from_soma, int num_interval, int den_or_axon) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Function to get the neuronal distance distribution of furkations (branchpoints)
     * for neuron. The distance is measured along the dendrites or axon in three dimensions. It returns an
     * array with num_interval + 1 components where the zeroth component is the total
     * number of forks of the neuron.
     * @param max_dist the maximal distance along the dendrite or axon which is scanned.
     * @param num_interval the number of interval between the distance 0 and max_dist_from_soma.
     * @param den_or_axon for den_or_axon = 0 scan the dendritic tree, for den_or_axon = 1 scan the axonal
     * tree, and for den_or_axon = 2 scan both trees of neuron.
     */
    public List<Integer> getForkDistribution2(float max_dist, int num_interval, int den_or_axon) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Function to get the width of the neural tree as a distribution of the distance
     * of the compartments for neuron. The distance is related to the soma of the neuron. It returns an
     * array with num_interval + 1 components where the zeroth component is the total
     * number of segments of the neuron.
     * @param max_dist_from_soma the maximal distance from the soma which is scanned.
     * @param num_interval the number of interval between the distance 0 and max_dist_from_soma.
     * @param den_or_axon for den_or_axon = 0 scan the dendritic tree, for den_or_axon = 1 scan the axonal
     * tree, and for den_or_axon = 2 scan both trees of neuron.
     * The function counts the number of all compartments intersecting with a given distance from the soma.
     */
    public List<Integer> getTotalwidthDistribution(float max_dist_from_soma, int num_interval, int den_or_axon) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Function to get the width of the neural tree as a distribution of the distance
     * of the intersecting compartments only. The distance is related to the soma of the neuron. It returns an
     * array with num_interval + 1 components where the zeroth component is the
     * number of counted segments of the neuron.
     * @param max_dist_from_soma the maximal distance from the soma which is scanned.
     * @param num_interval the number of interval between the distance 0 and max_dist_from_soma.
     * @param den_or_axon for den_or_axon = 0 scan the dendritic tree, for den_or_axon = 1 scan the axonal
     * tree, and for den_or_axon = 2 scan both trees of neuron.
     * The function counts the number of compartments intersecting with a given distance from the soma. It counts only
     * one segment per interval.
     */
    public List<Integer> getWidthSistribution(float max_dist_from_soma, int num_interval, int den_or_axon) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Section> getUndefinedSections() {
        return undefinedSections;
    }

    public void getSize() {

        // x coord, min z and max z coord (width)
        Map<Integer, Pair<Float, Float>> xData = new HashMap<Integer, Pair<Float, Float>>();

        // y coord, min x and max x coord (length)
        Map<Integer, Pair<Float, Float>> yData = new HashMap<Integer, Pair<Float, Float>>();

        // key: z coord, value: min and max y coord (height)
        Map<Integer, Pair<Float, Float>> zData = new HashMap<Integer, Pair<Float, Float>>();

        for (Dendrite den : dendrites) {
            den.getNTSSize(xData, yData, zData);
        }

        axon.getNTSSize(xData, yData, zData);

        /*
        int minX = 0;
        int maxX = 0;
        for(int x : xData.keySet()) {
        if(x > maxX) {
        maxX = x;
        } else if(x < minX) {
        minX = x;
        }
        }

        int minY = 0;
        int maxY = 0;
        for(int y : yData.keySet()) {
        if(y > maxY) {
        maxY = y;
        } else if(y < minY) {
        minY = y;
        }
        }

        int minZ = 0;
        int maxZ = 0;
        for(int z : zData.keySet()) {
        if(z > maxZ) {
        maxZ = z;
        } else if(z < minZ) {
        minZ = z;
        }
        }

        Point3f lowLeftCorner = new Point3f(minX, minY, minZ);
        Point3f upRightCorner = new Point3f(maxX, maxY, maxZ);
        Pair<Point3f, Point3f> cornerValues = new Pair<Point3f, Point3f>(lowLeftCorner, upRightCorner);
         *
         */
        float length = 0.0f;
        for (Pair<Float, Float> minMaxXCoord : yData.values()) {
            float localWidth = Math.abs(minMaxXCoord.second - minMaxXCoord.first);
            if (length < localWidth) {
                length = localWidth;
            }
        }

        float width = 0.0f;
        for (Pair<Float, Float> minMaxXCoord : xData.values()) {
            float localWidth = Math.abs(minMaxXCoord.second - minMaxXCoord.first);
            if (width < localWidth) {
                width = localWidth;
            }
        }

        float height = 0.0f;
        for (Pair<Float, Float> minMaxXCoord : zData.values()) {
            float localWidth = Math.abs(minMaxXCoord.second - minMaxXCoord.first);
            if (height < localWidth) {
                height = localWidth;
            }
        }

        neuronSize = new Point3f(length, width, height);
        //return cornerValues;
    }

    public float getVolume() {
        float volume = 0.0f;
        for (Dendrite curDen : dendrites) {
            volume += curDen.getNTSVolume();
        }
        //volume += axon.getNTSVolume();

        return volume;
    }

    /**
     * Get the value of index
     *
     * @return the value of index
     */
    @Override
    public int getIndex() {
        return index;
    }

    /**
     * Set the value of index
     *
     * @param index new value of index
     */
    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Get the value of drawNumber
     *
     * @return the value of drawNumber
     */
    public Vrand getDrawNumber() {
        return drawNumber;
    }

    public static void setDrawNumber(Vrand drawNumber) {
        NeuronBase.drawNumber = drawNumber;
    }

    public static void deleteData() {
        //drawNumber = null;
        basalRandomNumber = null;
        Axon.setDrawNumber(null);
    }

    /**
     * Get the value of axon
     *
     * @return the value of axon
     */
    @Override
    public Axon getAxon() {
        return axon;
    }

    /**
     * Get the value of denList
     *
     * @return the value of denList
     */
    @Override
    public List<Dendrite> getDendrites() {
        return dendrites;
    }

    @Override
    public int getNumberOfAllDenSegments() {
        int numberOfAllDenSegments = 0;
        int numDendrites = dendrites.size();
        for (int j = 0; j < numDendrites; j++) {
            Dendrite dendrite = dendrites.get(j);
            numberOfAllDenSegments += dendrite.getNumOfSegments();
        }
        return numberOfAllDenSegments;
    }

    public int getNumberOfAllDenSections() {
        int numberOfAllDenSections = 0;
        int numDendrites = dendrites.size();
        for (int j = 0; j < numDendrites; j++) {
            Dendrite dendrite = dendrites.get(j);
            numberOfAllDenSections += dendrite.getNumOfSections();
        }
        return numberOfAllDenSections;
    }

    /**
     * Function for setting a neuron. It sets the axon and creates the dendrites.
     * For the subclasses of Neuron this function must be over-written.
     */
    @Override
    public void setNeuron() {
        logger.info("set for neuron");
        Point3f somaMid = new Point3f(soma.getMid());
        Point3f axonEnd = new Point3f(somaMid);
        Point3f axonStart = new Point3f(somaMid);
        float somaRadius = soma.getMeanRadius();
        Vector3f deviation = new Vector3f(getParam().getDeviation().getX(), getParam().getDeviation().getY(), getParam().getDeviation().getZ());
        deviation.scale(somaRadius);
        if (drawNumber == null) {
            logger.info("random number is null!");
            drawNumber = new Vrand(getParam().getSeedValue());
        }
        int up_down = drawNumber.pm_onedraw();
        axonEnd.x += getParam().getAxonParam().getFirstGen().getLenParam().getX() * drawNumber.fpm_onedraw();
        axonEnd.y += getParam().getAxonParam().getFirstGen().getLenParam().getY() * drawNumber.fpm_onedraw();
        axonEnd.z = somaMid.z + up_down * getParam().getAxonParam().getFirstGen().getLenParam().getZ() * (drawNumber.fdraw() + 0.5f);
        axonStart.z += up_down * somaRadius;
        //logger.info("set axon");
        axon.set(axonStart, axonEnd, getParam().getAxonParam());
        //logger.info("set dendirte");
        for (int i = 0; i < getParam().getNumberOfDendrites(); ++i) {
            Dendrite curDen = new Dendrite();
            curDen.setDendrite(getParam().getDendriteParam(), soma, deviation, false);
            dendrites.add(curDen);
        }
    }

    @Override
    public NeuronParam getParam() {
        return NeuronParam.getInstance();
    }

    /**
     * Function for information of neuron. It gives the total length of dendrites and
     * the length of the axon, and further informations.
     */
    @Override
    public void infoNeuron() {
        // axon measurements
        Point3f axonRadius = axon.getRadius();
        //logger.info("axon avg rad: " + axonRadius.toString());
        float axonSurfaceArea = axon.getSurfaceArea();
        float totalAxonLength = axon.getTotalLength();
        int totalAxonSec = axon.getNumOfSections();
        int totalAxonBranch = axon.getNBranch();

        // dendrite measurements
        // The total length of all dendrites including their branches.
        Point3f basalDenRadius = new Point3f();
        Point3f apicalRadius = new Point3f();
        float totalDenLength = 0.0f;
        float denSurfaceArea = 0.0f;
        int denNParts = 0;
        int denNBranch = 0;
        int denNSec = 0;
        int denNSeg = 0;

        int numApical = 0;
        int numBasal = 0;

        for (int i = 0; i < dendrites.size(); ++i) {
            Dendrite curDen = dendrites.get(i);
            //logger.info("den radius: " + curDen.getRadius().toString());
            if (curDen.getFirstSection() != null) {
                if (curDen.getFirstSection().getSectionType() != null) {
                    if (curDen.getFirstSection().getSectionType().equals(Section.SectionType.APICAL)) {
                        numApical++;
                        apicalRadius.add(curDen.getRadius());
                    } else if (curDen.getFirstSection().getSectionType().equals(Section.SectionType.BASAL)) {
                        numBasal++;
                        basalDenRadius.add(curDen.getRadius());
                        //logger.info("basal dendrite!");
                    }
                }

                totalDenLength += curDen.getTotalLength();
                denSurfaceArea += curDen.getSurfaceArea();
                denNParts += curDen.getNParts();
                denNBranch += curDen.getNBranch();
                denNSec += curDen.getNumOfSections();
                denNSeg += curDen.getNumOfSegments();
            }

        }
        if (numApical != 0) {
            apicalRadius.scale(1.0f / (float) numApical);
        }

        if (numBasal != 0) {
            basalDenRadius.scale(1.0f / (float) numBasal);
        }

        /*
        float averageRadius = 0.0f;
        averageDenRadius /= dendrites.size();
        if(Float.isNaN(averageAxonRadius) || Float.isInfinite(averageAxonRadius) || averageAxonRadius == 0.0f) {
        averageAxonRadius = 0.0f;
        averageRadius = averageDenRadius;
        } else {
        averageRadius = (averageAxonRadius + averageDenRadius) / 2.0f;
        }
         */

        // soma measurements
        float somaSurfaceArea = soma.getSurfaceArea();
        String somaMid = soma.getMid().toString();
        float totalSomaLength = 0.0f;
        if (soma.getCylindricRepresentant() == null) {
            totalSomaLength = soma.cylindricRepresentant().getLength();
        } else {
            totalSomaLength = soma.getCylindricRepresentant().getLength();
        }

        int somaNSeg = 0;
        if (soma.getCylindricRepresentant() != null) {
            somaNSeg = soma.getCylindricRepresentant().getSegments().size();
        }

        // neuron measurements
        float neuronLength = totalAxonLength + totalDenLength + totalSomaLength;
        float neuronSurfaceArea = axonSurfaceArea + denSurfaceArea + somaSurfaceArea;

        float somaRad = soma.getAvgRadius();

        getSize();
        StringBuilder ret = new StringBuilder();
        ret.append("Info for ").append(index + 1).append(".th neuron:" + "\n"
                + " total length of neuron: ").append(neuronLength).append(" µm\n"
                + " total length of dendrites: ").append(totalDenLength).append(" µm\n"
                + " total length of axon: ").append(totalAxonLength).append(" µm\n"
                + " surface area of neuron: ").append(neuronSurfaceArea).append(" µm^2\n"
                + " surface area of dendrites: ").append(denSurfaceArea).append(" µm^2\n"
                + " surface area of axon: ").append(axonSurfaceArea).append(" µm^2\n"
                + " surface area of soma: ").append(somaSurfaceArea).append(" (µm^2)\n"
                + " soma at: ").append(somaMid).append("\n"
                + " number of dendrites starting from soma: ").append(dendrites.size()).append("\n"
                + " number of sections of all dendrites: ").append(denNSec).append("\n"
                + " number of segments of all dendrites: ").append(denNSeg).append("\n"
                + " number of branches of all dendrites: ").append(denNBranch).append("\n"
                + " number of branches of axon: ").append(totalAxonBranch).append("\n"
                + " number of sections of axon: ").append(totalAxonSec).append("\n"
                + " number of segments of axon: ").append(axon.getNumOfSegments()).append("\n"
                + " number of segments of soma: ").append(somaNSeg).append("\n"
                + " total number of bifurcation: ").append(denNBranch + totalAxonBranch).append("\n"
                + " total number of brunch: ").append(denNSec + totalAxonSec).append("\n"
                + " length (width) of the neuron: ").append(neuronSize.x).append("\n"
                + " width (depth) of the neuron: ").append(neuronSize.y).append("\n"
                + " height of the neuron: ").append(neuronSize.z).append("\n"
                + " (neuroMorpho.org measurements)").append("\n"
                + " soma avg radius: ").append(somaRad).append("\n"
                + " axon radius (max,min,avg): ").append(axonRadius.toString()).append("\n"
                + " apical radius (max,min,avg): ").append(apicalRadius.toString()).append("\n"
                + " basal radius (max,min,avg): ").append(basalDenRadius.toString()).append("\n"
                + " volume of the neuron: ").append(getVolume()).append(" µm^3\n");


        Trigger trigger = Trigger.getInstance();
        trigger.outPrintln(ret.toString());
    }

    @Override
    public boolean collide() {
        return collide;
    }

    @Override
    public void setCollide(boolean c) {
        this.collide = c;
    }
}
