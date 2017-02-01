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
package org.neugen.datastructures;

import org.neugen.datastructures.neuron.Neuron;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.vecmath.Point3f;
import org.apache.log4j.Logger;
import org.neugen.utils.Frand;
import org.neugen.datastructures.parameter.NetParam;
import org.neugen.parsers.NGX.WriteToNGX;
import org.neugen.parsers.TXT.WriteToTXT;

/**
 * This class contains the construction of a neural net, for the plotting
 * routines, and for a neural velocity field.
 *
 * @author Jens Eberhard
 * @author Simone Eberhard
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public class NetBase implements Serializable, Net {

    private static final long serialVersionUID = -7041930069294524614L;
    /** use to log messages */
    private final static Logger logger = Logger.getLogger(NetBase.class.getName());
    /** random number generator */
    protected transient Frand drawNumber;
    protected int totalNumberOfDenSegments;
    protected int totalNumberOfSomataSegments;
    protected int totalNumberOfAxonalSegments;
    protected final List<Neuron> neuronList;
    protected final List<Cons> synapseList;
    protected final List<String> typeCellNames;
    protected transient NetParam netParam;
    protected Region region;
    protected int[] typeCellNumbers;
    protected int[] cellOffsets;
    protected long[][] synNumbers;
    /** number of synapses */
    protected int nsynapse;
    protected int nnf_synapses;
    /** number of neurons */
    protected int nneuron;

    /**
     * Constructor. It initializes the neural net and uses the various subclasses
     * of neuron to create a cortical column.
     */
    public NetBase() {
        Section.resetSecCounter();
        Segment.resetSegCounter();
        neuronList = new ArrayList<Neuron>();
        synapseList = new ArrayList<Cons>();
        typeCellNames = new ArrayList<String>();
        netParam = NetParam.getInstance();

        Region.setInstance(null);
        region = new Region();
        Region.setInstance(region);

        cellOffsets = new int[1];
        cellOffsets[0] = 0;
        typeCellNames.add("imported cell");
    }

    @Override
    public void destroy() {
        drawNumber = null;
        netParam = null;
        if (neuronList != null) {
            for (Neuron neuron : neuronList) {
                List<Dendrite> dendriteList = neuron.getDendrites();
                for (Dendrite dendrite : dendriteList) {
                    Section.Iterator secIterator = dendrite.getFirstSection().getIterator();
                    while (secIterator.hasNext()) {
                        Section section = secIterator.next();
                        section.getSegments().clear();
                    }
                    dendrite = null;
                }
                dendriteList.clear();
                Axon axon = neuron.getAxon();
                if (axon.getFirstSection() != null) {
                    Section.Iterator secIterator = axon.getFirstSection().getIterator();
                    while (secIterator.hasNext()) {
                        Section section = secIterator.next();
                        section.getSegments().clear();
                    }
                }
                axon = null;
                neuron = null;
            }
            neuronList.clear();
        }
        if (synapseList != null) {
            synapseList.clear();
        }

        if (typeCellNames != null) {
            typeCellNames.clear();
        }
        System.gc();
    }

    @Override
    public List<String> getTypeCellNames() {
        return typeCellNames;
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public int[] getCellOffsets() {
        return cellOffsets;
    }

    @Override
    public List<Cons> getSynapseList() {
        return synapseList;
    }

    /** 
     * Get number of synapses
     *
     * @return number of synapses
     */
    @Override
    public long getNumOfSynapses(int presynapticType, int postSynapticType) {
        return synNumbers[presynapticType][postSynapticType];
    }

    public Frand getDrawNumber() {
        return drawNumber;
    }

    public void setDrawNumber(Frand drawNumber) {
        this.drawNumber = drawNumber;
    }

    @Override
    public List<Neuron> getNeuronList() {
        return neuronList;
    }

    @Override
    public int getNumNeurons() {
        return neuronList.size();
    }

    @Override
    public int getTotalNumOfAxonalSegments() {
        return totalNumberOfAxonalSegments;
    }

    @Override
    public int getTotalNumOfDenSegments() {
        return totalNumberOfDenSegments;
    }

    @Override
    public int getTotalNumOfSomataSegments() {
        return totalNumberOfSomataSegments;
    }

    @Override
    public int getTotalNumOfSegments() {
        return totalNumberOfAxonalSegments + totalNumberOfDenSegments + totalNumberOfSomataSegments;
    }

    /**
     * Function sets the total number of dendrite, axon
     * and soma segments of the neural net
     */
    @Override
    public void setTotalNumOfSegments() {
        totalNumberOfSomataSegments = 0;
        totalNumberOfAxonalSegments = 0;
        totalNumberOfDenSegments = 0;
        for (Neuron neuron : neuronList) {
            Axon axon = neuron.getAxon();
            Cellipsoid soma = neuron.getSoma();
            //totalNumberOfAxonalSegments += axon.getNumberOfAxonalSegments();
            totalNumberOfAxonalSegments += axon.getNumOfSegments();
            totalNumberOfSomataSegments += soma.getNumberOfSomaSegments();
            totalNumberOfDenSegments += neuron.getNumberOfAllDenSegments();
        }
    }

    @Override
    public void interconnect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void generate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Float> computeAPSN() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getTypeOfNeuron(int indexOfNeuron) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WriteToHoc getHocData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WriteToParHoc getParHocData(int nProcs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WriteToNGX getNGXData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WriteToTXT getTXTData() {
        throw new UnsupportedOperationException("Not supported yet.");

    }

    /**
     * @brief get types of cells supported in this network
     * @return 
     */
    @Override
    public List<String> getCellTypesOfNetwork() {
        return new ArrayList<String>();
    }

    public class AxonSegmentData {

        public Neuron neuron;
        public Section section;
        public Segment segment;
        public Point3f end;

        public AxonSegmentData(Neuron neuron, Section section, Segment segment) {
            this.neuron = neuron;
            this.section = section;
            this.segment = segment;
            end = segment.getEnd();
        }

        public Point3f get3DVec() {
            return end;
        }

        @Override
        public String toString() {
            String ret;
            ret = "neuron_idx = " + neuron.getIndex() + " local_segment=" + segment.getId();
            if (section == null) {
                ret += "section = null";
            } else {
                ret += "section = " + section.getId();
            }
            ret += "end = ";
            ret += end.toString();
            return ret;
        }

        public Segment getSegment() {
            return segment;
        }
    }

    public void calculateSomaticDistance(Cons synapse) {
        //logger.info("neuron1_idx: " + iter.neuron1_idx);
		//logger.info("neuron2_idx: " + iter.neuron2_idx);       
		float ax_soma_dist = 0;
		Section axonSec = synapse.getNeuron1AxSection();
		if (axonSec.getParentalLink() != null) {
			ax_soma_dist = axonSec.getParentalLink().getPolySomaDist();
			//logger.info("ax_soma_dist: " + ax_soma_dist);
		}
		int axSegPos = 0;
		for (Segment axSeg : axonSec.getSegments()) {
			if (axSeg.getId() == synapse.getNeuron1AxSegment().getId()) {
				break;
			}
			axSegPos++;
		}
		ax_soma_dist += ((float) axSegPos) / (axonSec.getSegments().size()) * axonSec.getLength();
		synapse.setDendriticSomaDistance(ax_soma_dist);
		//logger.info("dendritic_soma_distance(neuron1): " + iter.dendritic_soma_distance);

		float denSomaDist = 0.0f;
		Section denSec = synapse.getNeuron2DenSection();
		if (denSec != null) {
			if (denSec.getParentalLink() != null) {
				denSomaDist = denSec.getParentalLink().getPolySomaDist();
				//logger.info("den_soma_dist: " + den_soma_dist);
			}
			/*
			 logger.info("sec id: " + iter.neuron2_den_sectionId);
			 logger.info("number of segments: " + sec.getSegments().size());
			 logger.info("sec name: " + sec.getSectionName());
			 logger.info("sec length: " + sec.getLength());
			 */
			float secLength = denSec.getLength();
			int nsegs = denSec.getSegments().size();
			int segNum = 0;
			for (Segment denSeg : denSec.getSegments()) {
				if (denSeg.getId() == synapse.getNeuron2DenSectionSegment().getId()) {
					break;
				}
				segNum++;
			}

			denSomaDist += ((float) segNum) / nsegs * secLength;
			synapse.setDendriticSomaDistance(denSomaDist);
			//logger.info("dendritic_soma_distance(neuron2): " + iter.dendritic_soma_distance + "\n");
		}
	}

	/**
	 * Function to generate non-functional synapses on dendrite trees of
	 * neurons. Returns number of input synapses generated.
	 */
	@Override
	public int createNonFunSynapses() {
		int nsyn = 0;
		for (Neuron neuron : neuronList) {
			nsyn += createNonFunDendriticSynapses(synapseList, neuron);
			logger.debug("synapses for " + neuron.getIndex() + "th neuron generated");
		}
		nsynapse += nsyn;
		nnf_synapses += nsyn;
		return nsyn;
	}

	/**
	 * A function to generate non-functional dendritic synapses for this
	 * neuron. It saves synapses in nf_synapses and Net::synapse_list
	 * Probability to have a synapse in a Segment is here = length of
	 * Segment * density value given by Param.neu.
	 *
	 * @param synapse_list is the net list to save synapses into
	 * @param i index of this neuron in the net. Returns number of
	 * nf-synapses. Apical dendrites have to be saved at the beginning of
	 * neurons dendrite list !!!!!!!!!!!!!!!! (TODO: Mark apical dendrites!)
	 */
	public int createNonFunDendriticSynapses(List<Cons> synapse_list, Neuron neuron) {
        //logger.info("synapse_list size before: " + synapse_list.size());
		//get minmax distances to soma as treshold
		float den_bas = neuron.getParam().getDendriteParam().getNonFunctionalSynapses().getSimpleDistr().getDensity();
		//logger.info("den_bas: " + den_bas);
		float somaDistMinBas = neuron.getParam().getDendriteParam().getNonFunctionalSynapses().getSimpleDistr().getSomaDistance().getMin();
		float somaDistMaxBas = neuron.getParam().getDendriteParam().getNonFunctionalSynapses().getSimpleDistr().getSomaDistance().getMax();

		float somaDistMinApi = 0.0f;
		float somaDistMaxApi = 0.0f;

		float den_api = 1000.0f;
		int napiden = neuron.getParam().getNumberOfApicalDendrites();
		//logger.info("number of apical dendrites: " + napiden);
		if (napiden != 0 && neuron.getParam().getApicalParam() != null) {
			den_api = neuron.getParam().getApicalParam().getNonFunctionalSynapses().getSimpleDistr().getDensity();
			somaDistMinApi = neuron.getParam().getApicalParam().getNonFunctionalSynapses().getSimpleDistr().getSomaDistance().getMin();
			somaDistMaxApi = neuron.getParam().getApicalParam().getNonFunctionalSynapses().getSimpleDistr().getSomaDistance().getMax();
		}

		int nbasden = neuron.getParam().getNumberOfDendrites() - napiden;
		int nden = nbasden + napiden;
		int i;
		float clen;
		int ret = 0;
		/*
		 for (int f = 0; f < dist_api.size(); f++) {
		 logger.info("dist_api: " + dist_api.get(f));
		 }
		 */
		/*
		 logger.info("den_bas: " + den_bas);
		 logger.info("den_api: " + den_api);
		 logger.info("napiden: " + napiden);
		 logger.info("nbasden: " + nbasden);
		 */
		for (i = 0; i < napiden; ++i) {
			Section denSec;
			Section.Iterator secit = neuron.getDendrites().get(i).getFirstSection().getIterator();
			///< loop over all section of dendrite j of neuron2
			while (secit.hasNext()) {
				denSec = secit.next();
                // segmentl_size = denSec.getSegments().size();
				//logger.info("sec name: " + sec.getSectionName());
				//logger.info("segment1_size: " + segmentl_size);
				for (Segment denSeg : denSec.getSegments()) {
					clen = denSeg.getLength();
					float distToSoma = denSeg.getDistToSoma();
					// logger.info("distToSoma: " + distToSoma);
					float randNum = drawNumber.fdraw();
					float bla = clen * den_api;
                    //logger.info("randNum: " + randNum);
                    /*
					 if(segmentl_size == 111 && sec.getSectionName().equals("dendrite: 595")) {
					 logger.info("clen: " + clen);
					 logger.info("den_api: " + den_api);
					 logger.info("neuron param fullKey: " + neuronParam.fullKey().toString());
					 logger.info("bla: " + bla);
					 logger.info("randNum: " + randNum);
					 logger.info("distToSoma > (dist_api).get(0): " + distToSoma + " > " + (dist_api).get(0));
					 logger.info("distToSoma > (dist_api).get(1): " + distToSoma + " < " + (dist_api).get(1));
					 }
					 */
					if (randNum < bla && distToSoma > somaDistMinApi && distToSoma < somaDistMaxApi) {
                        //logger.info("add cons to synapse list (first loop)!");
                        /*
						 logger.info("randNum: " + randNum);
						 logger.info("bla: " + bla);
						 logger.info("distToSoma > (dist_api).get(0): " + distToSoma + " > " + (dist_api).get(0));
						 logger.info("distToSoma > (dist_api).get(1): " + distToSoma + " < " + (dist_api).get(1));
						 */
						//synapse_list.add(new Cons(-1, k, null, null, i, denSec, j));
						Cons syn = new Cons.Builder(null, denSeg).neuron2(neuron).neuron2DenSection(denSec).build();
						synapse_list.add(syn);
						denSeg.has_nf_synapse(true);
						++ret;
					}
				}
			}
		}
		//logger.info("ret after first loop: " + ret);
		for (i = napiden; i < nden; ++i) {
			Section denSec;
			Section.Iterator secit = neuron.getDendrites().get(i).getFirstSection().getIterator();
			///< loop over all section of dendrite j of neuron2
			while (secit.hasNext()) {
				denSec = secit.next();
				for (Segment denSeg : denSec.getSegments()) {
					float randNum = drawNumber.fdraw();
					clen = denSeg.getLength();
					float bla = clen * den_bas;
					float distToSoma = denSeg.getDistToSoma();
					if (randNum < bla && distToSoma > somaDistMinBas && distToSoma < somaDistMaxBas) {
						//logger.info("add cons to synapse list (second loop)!");
						Cons syn = new Cons.Builder(null, denSeg).neuron2(neuron).neuron2DenSection(denSec).build();
						synapse_list.add(syn);
						denSeg.has_nf_synapse(true);
						++ret;
					}
				}
			}
		}
        //logger.info("ret after second for loop: " + ret);
		//logger.info("synapse_list size after: " + synapse_list.size());
		// Set polygonal distance estimations.
		int synListSize = 0;
		if (synapse_list.size() > 0) {
			synListSize = synapse_list.size() - 1;
		}
		for (int ii = 0; ii < ret; ii++) {
			float den_soma_dist = 0;
			int idx = synListSize - ii;
			Cons co = synapse_list.get(idx);
			/*
			 logger.info("denList.size:" + denList.size());
			 logger.info("co.neuron1_idx" + co.neuron1_idx);
			 logger.info("co.neuron2_idx" + co.neuron2_idx);
			 logger.info("co.neuron2_den_dix: " + co.neuron2_den_idx);
			 */
			Section denSec = co.getNeuron2DenSection();
			if (denSec != null) {
				if (denSec.getParentalLink() != null) {
					den_soma_dist = denSec.getParentalLink().getPolySomaDist();
				}
				float secLength = denSec.getLength();
				int nsegs = denSec.getSegments().size();
				int segNum = 0;
				for (Segment denSeg : denSec.getSegments()) {
					if (denSeg.getId() == co.getNeuron2DenSectionSegment().getId()) {
						break;
					}
					segNum++;
				}
				//den_soma_dist += ((float) iter.neuron2_den_section_segment_idx) / (sec.getSegments().size()) * sec.getLength();
				den_soma_dist += ((float) segNum) / nsegs * secLength;
				co.setDendriticSomaDistance(den_soma_dist);
			}
		}
        //String mes = "non-functional synapses: " + ret;
		//logger.info(mes);
		return ret;
	}

	/**
	 * Get number of synapses of net.
	 */
	@Override
	public int getNumSynapse() {
		if (nsynapse != synapseList.size()) {
			logger.debug("synapse list size is different of nsynapse! ");
			logger.debug("nsynapse = " + nsynapse + " synapse list size = " + synapseList.size());
		}
		return nsynapse;
	}

	@Override
	public int getNumNonFunSynapses() {
		return nnf_synapses;
	}
}
