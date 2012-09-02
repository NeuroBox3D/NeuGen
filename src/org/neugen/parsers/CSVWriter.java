package org.neugen.parsers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.List;
import javax.vecmath.Point3f;
import org.apache.log4j.Logger;
import org.neugen.datastructures.Axon;
import org.neugen.datastructures.Cellipsoid;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.Section;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.gui.NeuGenConstants;
import org.neugen.utils.Utils;

/**
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public final class CSVWriter {

    /** use to log messages */
    private final static Logger logger = Logger.getLogger(CSVWriter.class.getName());
    private final List<String> cellTypes;
    private final List<Neuron> neurons;
    private final int[] cellOffsets;
    private File file;

    public CSVWriter(Net net, File file) {
        this.file = file;
        this.cellTypes = net.getTypeCellNames();
        this.neurons = net.getNeuronList();
        this.cellOffsets = net.getCellOffsets();
    }

    public void writeNeuronInfo() {
        String csv = NeuGenConstants.EXTENSION_CSV;
        String extension = Utils.getExtension(file);
        if (!csv.equals(extension)) {
            this.file = new File(file.getAbsolutePath() + "." + csv);
        }
        Writer fw = null;
        Character sep = ';';
        try {
            logger.info("file path: " + file.getAbsolutePath());
            fw = new FileWriter(file);
            fw.append("file" + sep
                    + "length_neuron" + sep
                    + "length_dendrites" + sep
                    + "length_axon" + sep
                    + "surface_area_neuron" + sep
                    + "surface_area_dendrites" + sep
                    + "surface_area_axon" + sep
                    + "surface_area_soma" + sep
                    + "num_dendrites" + sep
                    + "num_den_sec" + sep
                    + "num_den_seg" + sep
                    + "num_den_branch" + sep
                    + "num_ax_sec" + sep
                    + "num_ax_seg" + sep
                    + "num_ax_branch" + sep
                    + "num_bifurcation" + sep
                    + "num_branch" + sep
                    + "soma_radius" + sep
                    + "ax_max_rad" + sep
                    + "ax_min_rad" + sep
                    + "ax_avg_rad" + sep
                    + "apical_max_rad" + sep
                    + "apical_min_rad" + sep
                    + "apical_avg_rad" + sep
                    + "basal_max_rad" + sep
                    + "basal_min_rad" + sep
                    + "basal_avg_rad" + sep
                    + "\n\n");


            float total_avg_length_neuron = 0.0f;
            float total_avg_length_dendrites = 0.0f;
            float total_avg_length_axon = 0.0f;
            float total_avg_surface_area_neuron = 0.0f;
            float total_avg_surface_area_dendrites = 0.0f;
            float total_avg_surface_area_axon = 0.0f;
            float total_avg_surface_area_soma = 0.0f;

            float total_avg_num_dendrites = 0.0f;
            float total_avg_num_den_sec = 0.0f;
            float total_avg_num_den_seg = 0.0f;
            float total_avg_num_den_branch = 0.0f;
            float total_avg_num_ax_sec = 0.0f;
            float total_avg_num_ax_seg = 0.0f;
            float total_avg_num_ax_branch = 0.0f;
            float total_avg_num_bifurcation = 0.0f;
            float total_avg_num_brunch = 0.0f;

            float total_avg_somaRad = 0.0f;
            float total_avg_axonRadius_max = 0.0f;
            float total_avg_axonRadius_min = 0.0f;
            float total_avg_axonRadius_avg = 0.0f;
            float total_avg_apicalRadius_max = 0.0f;
            float total_avg_apicalRadius_min = 0.0f;
            float total_avg_apicalRadius_avg = 0.0f;
            float total_avg_basalDenRadius_max = 0.0f;
            float total_avg_basalDenRadius_min = 0.0f;
            float total_avg_basalDenRadius_avg = 0.0f;


            for (Neuron neuron : neurons) {
                //Neuron neuron = neurons.get(0);
                List<Dendrite> dendrites = neuron.getDendrites();
                Axon axon = neuron.getAxon();
                Cellipsoid soma = neuron.getSoma();

                float length_neuron = 0.0f;
                float length_dendrites = 0.0f;
                float length_axon = axon.getTotalLength();
                float surface_area_neuron = 0.0f;
                float surface_area_dendrites = 0.0f;
                float surface_area_axon = axon.getSurfaceArea();
                float surface_area_soma = soma.getSurfaceArea();
                //float averageAxonRadius = axon.getAverageRadius();

                float roundVal = 0.000111f;
                Point3f pRoundVal = new Point3f(roundVal, roundVal, roundVal);
                Point3f axonRadius = axon.getRadius();
                if (axonRadius.x != 0.0f && axonRadius.y != 0.0f && axonRadius.z != 0.0f) {
                    axonRadius.add(pRoundVal);
                }

                float somaRad = soma.getAvgRadius();
                if (somaRad != 0.0f) {
                    somaRad += roundVal;
                }

                Point3f basalDenRadius = new Point3f();
                Point3f apicalRadius = new Point3f();
                int numApical = 0;
                int numBasal = 0;

                int num_dendrites = dendrites.size();
                int num_den_sec = 0;
                int num_den_seg = 0;
                int num_den_branch = 0;
                int num_ax_sec = axon.getNumOfSections();
                int num_ax_seg = axon.getNumOfSegments();
                int num_ax_branch = axon.getNBranch();

                for (int i = 0; i < dendrites.size(); ++i) {
                    Dendrite curDen = dendrites.get(i);
                    length_dendrites += curDen.getTotalLength();
                    surface_area_dendrites += curDen.getSurfaceArea();
                    //denNParts += curDen.getNParts();
                    num_den_branch += curDen.getNBranch();
                    num_den_sec += curDen.getNumOfSections();
                    num_den_seg += curDen.getNumOfSegments();

                    if (curDen.getFirstSection().getSectionType().equals(Section.SectionType.APICAL)) {
                        numApical++;
                        apicalRadius.add(curDen.getRadius());
                    } else if (curDen.getFirstSection().getSectionType().equals(Section.SectionType.BASAL)) {
                        numBasal++;
                        basalDenRadius.add(curDen.getRadius());
                        //logger.info("basal dendrite!");
                    }
                }

                if (numApical != 0) {
                    apicalRadius.scale(1.0f / (float) numApical);
                }

                if (numBasal != 0) {
                    basalDenRadius.scale(1.0f / (float) numBasal);
                }

                if (basalDenRadius.x != 0.0f && basalDenRadius.y != 0.0f && basalDenRadius.z != 0.0f) {
                    basalDenRadius.add(pRoundVal);
                }

                if (apicalRadius.x != 0.0f && apicalRadius.y != 0.0f && apicalRadius.z != 0.0f) {
                    apicalRadius.add(pRoundVal);
                }

                float totalSomaLength = 0.0f;
                if (soma.getCylindricRepresentant() == null) {
                    totalSomaLength = soma.cylindricRepresentant().getLength();
                } else {
                    totalSomaLength = soma.getCylindricRepresentant().getLength();
                }

                // neuron measurements
                length_neuron = length_axon + length_dendrites + totalSomaLength;
                surface_area_neuron = surface_area_axon + +surface_area_dendrites + surface_area_soma;

                int num_bifurcation = num_den_branch + num_ax_branch;
                int num_brunch = num_den_sec + num_ax_sec;

                /*
                float averageRadius = 0.0f;
                averageDenRadius /= dendrites.size();
                if (Float.isNaN(averageAxonRadius) || Float.isInfinite(averageAxonRadius) || averageAxonRadius == 0.0f) {
                averageAxonRadius = 0.0f;
                averageRadius = averageDenRadius;
                } else {
                averageRadius = (averageAxonRadius + averageDenRadius) / 2.0f;
                }
                 */

                /*
                logger.info("somaRad: " + somaRad);
                logger.info("axonRad:" + axonRadius.toString());
                logger.info("apicalRad: " + apicalRadius.toString());
                 * 
                 */

                DecimalFormat df2 = new DecimalFormat("0.0000000");

                fw.append(neuron.getName() + sep.toString()
                        + ((int) length_neuron) + sep.toString()
                        + ((int) length_dendrites) + sep.toString()
                        + ((int) length_axon) + sep.toString()
                        + ((int) surface_area_neuron) + sep.toString()
                        + ((int) surface_area_dendrites) + sep.toString()
                        + ((int) surface_area_axon) + sep.toString()
                        + ((int) surface_area_soma) + sep.toString()
                        + (num_dendrites) + sep.toString()
                        + (num_den_sec) + sep.toString()
                        + (num_den_seg) + sep.toString()
                        + (num_den_branch) + sep.toString()
                        + (num_ax_sec) + sep.toString()
                        + (num_ax_seg) + sep.toString()
                        + (num_ax_branch) + sep.toString()
                        + (num_bifurcation) + sep.toString()
                        + (num_brunch) + sep.toString()
                        //+ (averageRadius) + sep.toString()                     
                        + ((float) somaRad) + sep.toString()
                        + ((float) axonRadius.x) + sep.toString()
                        + ((float) axonRadius.y) + sep.toString()
                        + ((float) axonRadius.z) + sep.toString()
                        + ((float) apicalRadius.x) + sep.toString()
                        + ((float) apicalRadius.y) + sep.toString()
                        + ((float) apicalRadius.z) + sep.toString()
                        + ((float) basalDenRadius.x) + sep.toString()
                        + ((float) basalDenRadius.y) + sep.toString()
                        + ((float) basalDenRadius.z) + sep.toString()
                        + " \n\n");

                total_avg_length_neuron += length_neuron;
                total_avg_length_dendrites += length_dendrites;

                total_avg_length_axon += length_axon;
                total_avg_surface_area_neuron += surface_area_neuron;
                total_avg_surface_area_dendrites += surface_area_dendrites;
                total_avg_surface_area_axon += surface_area_axon;
                total_avg_surface_area_soma += surface_area_soma;

                total_avg_num_dendrites += num_dendrites;
                total_avg_num_den_sec += num_den_sec;
                total_avg_num_den_seg += num_den_seg;
                total_avg_num_den_branch += num_den_branch;
                total_avg_num_ax_sec += num_ax_sec;
                total_avg_num_ax_seg += num_ax_seg;
                total_avg_num_ax_branch += num_ax_branch;
                total_avg_num_bifurcation += num_bifurcation;
                total_avg_num_brunch += num_brunch;

                total_avg_somaRad += somaRad;
                total_avg_axonRadius_max += axonRadius.x;
                total_avg_axonRadius_min += axonRadius.y;
                total_avg_axonRadius_avg += axonRadius.z;
                total_avg_apicalRadius_max += apicalRadius.x;
                total_avg_apicalRadius_min += apicalRadius.y;
                total_avg_apicalRadius_avg += apicalRadius.z;
                total_avg_basalDenRadius_max += basalDenRadius.x;
                total_avg_basalDenRadius_min += basalDenRadius.y;
                total_avg_basalDenRadius_avg += basalDenRadius.z;
            }

            int numNeuron = neurons.size();

            total_avg_length_neuron /= numNeuron;
            total_avg_length_dendrites /= numNeuron;

            total_avg_length_axon /= numNeuron;
            total_avg_surface_area_neuron /= numNeuron;
            total_avg_surface_area_dendrites /= numNeuron;
            total_avg_surface_area_axon /= numNeuron;
            total_avg_surface_area_soma /= numNeuron;

            total_avg_num_dendrites /= numNeuron;
            logger.info("avg num dendrites: " + total_avg_num_dendrites);

            total_avg_num_den_sec /= numNeuron;
            total_avg_num_den_seg /= numNeuron;
            total_avg_num_den_branch /= numNeuron;
            total_avg_num_ax_sec /= numNeuron;
            total_avg_num_ax_seg /= numNeuron;
            total_avg_num_ax_branch /= numNeuron;
            total_avg_num_bifurcation /= numNeuron;
            total_avg_num_brunch /= numNeuron;

            total_avg_somaRad /= numNeuron;
            total_avg_axonRadius_max /= numNeuron;
            total_avg_axonRadius_min /= numNeuron;
            total_avg_axonRadius_avg /= numNeuron;
            total_avg_apicalRadius_max /= numNeuron;
            total_avg_apicalRadius_min /= numNeuron;
            total_avg_apicalRadius_avg /= numNeuron;
            total_avg_basalDenRadius_max /= numNeuron;
            total_avg_basalDenRadius_min /= numNeuron;
            total_avg_basalDenRadius_avg /= numNeuron;

            fw.append("average " + sep.toString()
                    + (int) total_avg_length_neuron + sep.toString()
                    + (int) total_avg_length_dendrites + sep.toString()
                    + (int) total_avg_length_axon + sep.toString()
                    + (int) total_avg_surface_area_neuron + sep.toString()
                    + (int) total_avg_surface_area_dendrites + sep.toString()
                    + (int) total_avg_surface_area_axon + sep.toString()
                    + (int) total_avg_surface_area_soma + sep.toString()
                    + ((float) total_avg_num_dendrites) + sep.toString()
                    + (float) total_avg_num_den_sec + sep.toString()
                    + (float) total_avg_num_den_seg + sep.toString()
                    + (float) total_avg_num_den_branch + sep.toString()
                    + (float) total_avg_num_ax_sec + sep.toString()
                    + (float) total_avg_num_ax_seg + sep.toString()
                    + (float) total_avg_num_ax_branch + sep.toString()
                    + (float) total_avg_num_bifurcation + sep.toString()
                    + (float) total_avg_num_brunch + sep.toString()
                    + (float) total_avg_somaRad + sep.toString()
                    + (float) total_avg_axonRadius_max + sep.toString()
                    + (float) total_avg_axonRadius_min + sep.toString()
                    + (float) total_avg_axonRadius_avg + sep.toString()
                    + (float) total_avg_apicalRadius_max + sep.toString()
                    + (float) total_avg_apicalRadius_min + sep.toString()
                    + (float) total_avg_apicalRadius_avg + sep.toString()
                    + (float) total_avg_basalDenRadius_max + sep.toString()
                    + (float) total_avg_basalDenRadius_min + sep.toString()
                    + (float) total_avg_basalDenRadius_avg + sep.toString()
                    + " \n\n");
            fw.close();



        } catch (IOException ex) {
            logger.error(ex, ex);
        }
    }

    /**
     * Function to write a CSV file for the average neuronal data of the net.
     * @param fname the name of the name.
     */
    public void writeAverageNeuronInfo() {
        String csv = NeuGenConstants.EXTENSION_CSV;
        String extension = Utils.getExtension(file);
        if (!csv.equals(extension)) {
            this.file = new File(file.getAbsolutePath() + "." + csv);
        }
        int ntypes = cellTypes.size();
        Writer fw = null;
        Character sep = ';';
        try {
            logger.info("file path: " + file.getAbsolutePath());
            fw = new FileWriter(file);
            fw.append("cell_type" + sep + "number of dendrites"
                    + sep + "total_neuronlength" + sep + "deviation"
                    + sep + "total_denlength" + sep + "deviation"
                    + sep + "total_axlength" + sep + "deviation"
                    + sep + "total_densurface_area" + sep + "deviation"
                    + sep + "total_axsurface_area" + sep + "deviation"
                    + sep + "number_axnparts" + sep + "deviation"
                    + sep + "total_dennparts" + sep + "deviation"
                    + sep + "number_axbranches" + sep + "deviation"
                    + "\n");

            for (int h = 0; h < ntypes; h++) {
                double total_denlength = 0.0d;
                double total_axlength = 0.0d;
                double total_densurface_area = 0.0d;
                double total_axsurface_area = 0.0d;
                double number_axnparts = 0.0d;
                int total_dennparts = 0;
                double number_axbranches = 0.0d;

                int number = 0;
                if (cellOffsets.length > (h + 1)) {
                    number = cellOffsets[h + 1] - cellOffsets[h];
                }

                if (number == 0) {
                    number = 1;
                }
                //logger.info("number is: " + number);


                int numCells = cellOffsets[h] + 1;
                if (cellOffsets.length > (h + 1)) {
                    numCells = cellOffsets[h + 1];
                }
                for (int i = cellOffsets[h]; i < numCells; i++) {
                    Neuron neuron = neurons.get(i);
                    List<Dendrite> dendrites = neuron.getDendrites();
                    for (int j = 0; j < dendrites.size(); ++j) {
                        total_denlength += dendrites.get(j).getTotalLength();
                        total_densurface_area += dendrites.get(j).getSurfaceArea();
                        total_dennparts += dendrites.get(j).getNParts();
                    }
                    total_axlength += neuron.getAxon().getTotalLength();
                    total_axsurface_area += neuron.getAxon().getSurfaceArea();
                    number_axnparts += neuron.getAxon().getNParts();
                    number_axbranches += neuron.getAxon().getNBranch();
                }

                double total_denlength_av = total_denlength / number;
                double total_densurface_area_av = total_densurface_area / number;
                double total_dennparts_av = total_dennparts / number;

                // deviations
                double total_denlength_dev = 0.0d;
                double total_axlength_dev = 0.0d;
                double total_densurface_area_dev = 0.0d;
                double total_axsurface_area_dev = 0.0d;
                double number_axnparts_dev = 0.0d;
                double total_dennparts_dev = 0.0d;
                double number_axbranches_dev = 0.0d;
                int ndens = 0;

                numCells = cellOffsets[h] + 1;
                if (cellOffsets.length > (h + 1)) {
                    numCells = cellOffsets[h + 1];
                }
                for (int i = cellOffsets[h]; i < numCells; i++) {
                    total_denlength = 0.0d;
                    total_densurface_area = 0.0d;
                    total_dennparts = 0;
                    Neuron neuron = neurons.get(i);
                    List<Dendrite> dendrites = neuron.getDendrites();
                    for (int j = 0; j < dendrites.size(); ++j) {
                        total_denlength += dendrites.get(j).getTotalLength();
                        total_densurface_area += dendrites.get(j).getSurfaceArea();
                        total_dennparts += dendrites.get(j).getNParts();
                    }
                    total_denlength_dev += Math.abs(total_denlength - total_denlength_av);
                    total_densurface_area_dev += Math.abs(total_densurface_area - total_densurface_area_av);
                    total_dennparts_dev += Math.abs(total_dennparts - total_dennparts_av);
                    total_axlength_dev += Math.abs(total_axlength / number - neuron.getAxon().getTotalLength());
                    total_axsurface_area_dev += Math.abs(total_axsurface_area / number - neuron.getAxon().getSurfaceArea());
                    number_axnparts_dev += Math.abs(number_axnparts / number - neuron.getAxon().getNParts());
                    number_axbranches_dev += Math.abs(number_axbranches / number - neuron.getAxon().getNBranch());
                }

                if (number > 0) {
                    int numDen;
                    if (h >= neurons.size()) {
                        numDen = 0;
                    } else {
                        int numNeuron = 0;
                        if (cellOffsets[h] > 0) {
                            numNeuron = cellOffsets[h] - 1;
                        }
                        numDen = neurons.get(numNeuron).getDendrites().size();
                    }
                    fw.append(cellTypes.get(h) + sep + numDen
                            + sep + (int) total_denlength_av
                            + sep + ((int) total_denlength_dev / number)
                            + sep + ((int) total_axlength / number)
                            + sep + ((int) total_axlength_dev / number)
                            + sep + ((int) total_densurface_area_av)
                            + sep + ((int) total_densurface_area_dev / number)
                            + sep + ((int) total_axsurface_area / number)
                            + sep + ((int) total_axsurface_area_dev / number)
                            + sep + ((int) number_axnparts / number)
                            + sep + ((int) number_axnparts_dev / number)
                            + sep + (int) total_dennparts_av
                            + sep + ((int) total_dennparts_dev / number)
                            + sep + ((int) number_axbranches / number)
                            + sep + ((int) number_axbranches_dev / number)
                            + "\n");
                }
                //logger.info("number_axbranches: " + number_axbranches);
                //logger.info("number_axbranches dev: " + number_axbranches_dev);
            }
            fw.close();
        } catch (IOException ex) {
            logger.error(ex, ex);
        }
    }
}
