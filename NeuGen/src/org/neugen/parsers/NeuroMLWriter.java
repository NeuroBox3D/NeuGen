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

/// package's name
package org.neugen.parsers;

/// imports
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.vecmath.Point3f;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.neugen.datastructures.Axon;
import org.neugen.datastructures.Cellipsoid;
import org.neugen.datastructures.Cons;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.NeuronalTreeStructure;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.datastructures.Pair;
import org.neugen.datastructures.Section;
import org.neugen.datastructures.Segment;
import static org.neugen.gui.NeuGenConstants.VERSION;
import org.neugen.gui.NeuGenView;
import org.neugen.gui.NeuroMLLevelSelector;
import org.neugen.parsers.NeuroML.NetworkML.NetworkMLElement;
import org.neugen.parsers.NeuroML.NetworkML.NeuroMLConnections;
import org.neugen.parsers.NeuroML.NetworkML.NeuroMLProjection;
import org.neugen.parsers.NeuroML.NetworkML.NeuroMLProjections;
import org.neugen.parsers.NeuroML.NetworkML.NeuroMLSynapse;
import org.neugen.parsers.NeuroML.NetworkML.NeuroMLSynapseBilateral;
import org.neugen.parsers.NeuroML.NetworkML.NeuroMLSynapseProperty;
import org.neugen.parsers.NeuroML.NetworkML.NeuroMLSynapseUnilateral;
import org.neugen.utils.Utils;

/**
 *
 * @author Alexander Wanner
 * @author Sergei Wolf
 * @author Stephan Grein <stephan@syntaktischer-zucker.de>
 */
final class Neuroml {

	public String xmlns;
	public String mml;
	public String meta;
	public String bio;
	public String cml;
	public String xsi;
	public String schemaLocation;
	public String name;
	public String lengthUnits;

	/**
	 * Level 1: Anatomy only cells specified in MorphML Level 2: ChannelML
	 * and biophysical cell models Level 3: Networks of biologically
	 * realistic cells using NetworkML
	 *
	 * @param schemaLocation
	 * @param name
	 * @param lengthUnits
	 *
	 */
	public void setHeader(String schemaLocation, String name, String lengthUnits) {
		xmlns = "http://morphml.org/morphml/schema";
		mml = "http://morphml.org/morphml/schema";
		bio = "http://morphml.org/biophysics/schema";
		cml = "http://morphml.org/channelml/schema";
		meta = "http://morphml.org/metadata/schema";
		xsi = "http://www.w3.org/2001/XMLSchema-instance";
		this.schemaLocation = schemaLocation;
		this.name = name;
		this.lengthUnits = lengthUnits;
	}
}

/**
 *
 * Defines the smallest unit within a possibly branching structure, such as a
 * dendrite or axon. The parent attribute is used to define connectivity. A
 * segment would be mapped to a compartment in a compartmental modelling
 * application such as GENESIS
 *
 */
final class NeuroMLSegment {

	/**
	 * The ID of the segment, which should be unique within the cell
	 */
	public int id;
	/**
	 * A unique name can be given to the segment (Use: optional)
	 */
	public String name;
	/**
	 * Used to indicate logical connectivity between segments (Use:
	 * optional)
	 */
	public Integer parent;
	/**
	 * The cable ID of which this segment is part (Use: optional)
	 */
	public Integer cable;
	/**
	 * The start point
	 */
	public Proximal proximal;
	/**
	 * The end point
	 */
	public Distal distal;
}

/**
 * Definition of a cell
 */
final class NeuroMLCell {
	// a name can be given to the cell (Use: optional)

	public String name;
	// first segment should represent the soma
	private NeuroMLSegments segments;
	private NeuroMLCables cables;

	public NeuroMLSegments getSegments() {
		return segments;
	}

	public void setSegments(NeuroMLSegments segments) {
		this.segments = segments;
	}

	public NeuroMLCables getCables() {
		return cables;
	}

	public void setCables(NeuroMLCables cables) {
		this.cables = cables;
	}

}

final class NeuroMLCables {

	private List<Cable> cables;
	private String xmlns;

	public NeuroMLCables(List<Cable> cables, String xmlns) {
		this.cables = cables;
		this.xmlns = xmlns;
	}
}

final class NeuroMLSegments {

	private List<NeuroMLSegment> segments;
	private String xmlns;

	public NeuroMLSegments(List<NeuroMLSegment> segments, String xmlns) {
		this.segments = segments;
		this.xmlns = xmlns;
	}
}

/**
 * NeuroML Level 2 Biophysics
 *
 * @author sergejwolf
 *
 */
// TODO:
final class NeuroMLBiophysics {

	private BioSpecCapacitance specificCapacitance;
}

final class BioSpecCapacitance {

	protected BioSpec parameter;
}

final class BioSpecAxialResistance {

	private BioSpec parameter;
}

final class BioSpec {

	private int value;
	private String group;
}

// TODO:
final class NeuroMLChannel {
}

/**
 *
 * The start point (and diameter) of the segment. If absent, it is assumed that
 * the distal point of the parent is the start point of this segment.
 *
 */
final class Proximal {

	private Float x, y, z, diameter;

	public Proximal(Float x, Float y, Float z, Float diam) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.diameter = diam;
	}
}

/**
 *
 * The end point (and diameter) of the segment. Note if the 3D location of the
 * distal point is the same as the proximal point, the segment is assumed to be
 * spherical.
 */
final class Distal {

	private Float x, y, z, diameter;

	public Distal(Float x, Float y, Float z, Float diam) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.diameter = diam;
	}
}

class Cable {

	private int id;

	public Cable(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public String name;
//	public int parent;
	public String metaGroup;
}

final class CableFAP extends Cable {

	public CableFAP(int id, String name) {
		super(id, name);
	}
	public int fractAlongParent;
}

public final class NeuroMLWriter {

	private static final long serialVersionUID = 1L;

	/**
	 * use to log messages
	 */
	private final static Logger logger = Logger.getLogger(NeuroMLWriter.class.getName());
	//private int segId = 0;
	//protected XMLGeometry geometry;
	protected final int d = 3;
	private static XStream xstream;
	//private int sectionID = 0;
	private int neuromlLevel = 1;

	private void initXStream() {
		xstream = new XStream(new DomDriver());
		//xstream.setMode(XStream.NO_REFERENCES);
	}

	public int getNeuromlLevel() {
		return neuromlLevel;
	}

	public void setNeuromlLevel(int neuromlLevel) {
		this.neuromlLevel = neuromlLevel;
	}

	/**
	 * @brief set the xstream options to allow for NeuroML Level 1, 2 and 3 output
	 * @todo XML header for Level 1, 2 and 3 is different from the universal neuroml header!
	 *
	 * @return xstream object with output options for xml format
	 */
	private void setXStreamOptions(int level) {
		///////////////////////////////////////////////
		/// MorphML (Level 1)
		///////////////////////////////////////////////
		if (level >= 1) {
			// aliases for class names
			xstream.alias("segment", NeuroMLSegment.class);
			xstream.alias("segments", NeuroMLSegments.class);
			xstream.addImplicitCollection(NeuroMLSegments.class, "segments");
			xstream.alias("proximal", Proximal.class);
			xstream.alias("distal", Distal.class);
			xstream.alias("cell", NeuroMLCell.class);
			xstream.alias("morphml", Neuroml.class);
			xstream.alias("cable", NeuroMLCables.class);
			xstream.addImplicitCollection(NeuroMLCables.class, "cables");
			xstream.alias("cable", Cable.class);
			xstream.alias("cable", CableFAP.class);

			// aliases for variables
			xstream.aliasAttribute(Neuroml.class, "mml", "\n\txmlns:mml");
			xstream.aliasAttribute(Neuroml.class, "meta", "\n\txmlns:meta");
			xstream.aliasAttribute(Neuroml.class, "xsi", "\n\txmlns:xsi");
			xstream.aliasAttribute(Neuroml.class, "bio", "\n\txmlns:bio");
			xstream.aliasAttribute(Neuroml.class, "cml", "\n\txmlns:cml");
			xstream.aliasAttribute(Neuroml.class, "schemaLocation", "\n\txsi:schemaLocation");
			xstream.aliasAttribute(Neuroml.class, "name", "\n\tname");
			xstream.aliasAttribute(Neuroml.class, "lengthUnits", "\n\tlengthUnits");

			/*
			 if(level == 1) {
			 xstream.omitField(Neuroml.class, "bio");
			 xstream.omitField(Neuroml.class, "cml");
			 }
			 */
			// set the Neuroml memeber variables as na XML attribute
			xstream.useAttributeFor(Neuroml.class, "xmlns");
			xstream.useAttributeFor(Neuroml.class, "mml");
			xstream.useAttributeFor(Neuroml.class, "meta");
			xstream.useAttributeFor(Neuroml.class, "bio");
			xstream.useAttributeFor(Neuroml.class, "cml");
			xstream.useAttributeFor(Neuroml.class, "xsi");
			xstream.useAttributeFor(Neuroml.class, "schemaLocation");
			xstream.useAttributeFor(Neuroml.class, "name");
			xstream.useAttributeFor(Neuroml.class, "lengthUnits");

			//set the NeuroML member variables as an XML attribute
			xstream.useAttributeFor(NeuroMLSegment.class, "id");
			xstream.useAttributeFor(NeuroMLSegment.class, "cable");
			xstream.useAttributeFor(NeuroMLSegment.class, "parent");
			xstream.useAttributeFor(NeuroMLSegment.class, "name");

			xstream.useAttributeFor(NeuroMLSegments.class, "xmlns");

			//set the Proximal member variables as an XML attribute
			xstream.useAttributeFor(Proximal.class, "x");
			xstream.useAttributeFor(Proximal.class, "y");
			xstream.useAttributeFor(Proximal.class, "z");
			xstream.useAttributeFor(Proximal.class, "diameter");
			//set the Distal member variables as an XML attribute
			xstream.useAttributeFor(Distal.class, "x");
			xstream.useAttributeFor(Distal.class, "y");
			xstream.useAttributeFor(Distal.class, "z");
			xstream.useAttributeFor(Distal.class, "diameter");
			xstream.useAttributeFor(NeuroMLCell.class, "name");

			xstream.useAttributeFor(NeuroMLCables.class, "xmlns");
			xstream.useAttributeFor(Cable.class, "id");
			xstream.useAttributeFor(CableFAP.class, "fractAlongParent");
			xstream.aliasAttribute(Cable.class, "name", "name");
			xstream.aliasField("meta:group", Cable.class, "metaGroup");

			xstream.aliasField("connections", NeuroMLProjection.class, "connections");
			xstream.addImplicitCollection(NeuroMLConnections.class, "elements");
			
			xstream.autodetectAnnotations(true);
		}
			

		///////////////////////////////////////////////
		/// NetworkML (Level 2)
		///////////////////////////////////////////////
		if (level >= 2) {
			/**
			 * @brief todo add xstream features for ChannelML
			 */
		}
		
		///////////////////////////////////////////////
		/// NetworkML (Level 3)
		///////////////////////////////////////////////
		if (level >= 3) {
			String[] attributes = new String[]{ "id", "pre_cell_id", "pre_segment_id", "pre_fraction_along",  
						      "post_cell_id", "post_segment_id", "post_fraction_along" };
			
			String[] attributes2 = new String[]{ "id", "pre_cell_id", "pre_segment_id", "pre_fraction_along"};
			
			for (String attribute : attributes) {
				xstream.useAttributeFor(NeuroMLSynapseBilateral.class, attribute);
			}
			
			for (String attribute2 : attributes2) {
				xstream.useAttributeFor(NeuroMLSynapseUnilateral.class, attribute2);
			}
			
			xstream.useAttributeFor(NeuroMLSynapseBilateral.class);
			xstream.useAttributeFor(NeuroMLSynapseBilateral.class, "from");
			xstream.useAttributeFor(NeuroMLSynapseBilateral.class, "to");
			xstream.useAttributeFor(NeuroMLSynapseUnilateral.class, "injection");
		}
	}

	
	/**
	 * @brief get the Xstream
	 * @author stephanmg
	 * 
	 * @return 
	 */
	public static XStream getXstream() {
		return xstream;
	}

	/**
	 * @brief prepares the XML output for cells output (Level 1)
	 * @author stephanmg
	 * 
	 * @param sections
	 * @param neuroMLList
	 * @param cables
	 * @param nn
	 * @param cableName 
	 */
	private void copyToNeuroML(List<Section> sections, List<NeuroMLSegment> neuroMLList, List<Cable> cables, int nn, String cableName) {
		Point3f sstart;
		Point3f send;
		float sradiusstart, sradiusend;
		Map<Section, Pair<Integer, Integer>> sectionToStartEndSegment = new HashMap<Section, Pair<Integer, Integer>>();

		// fuelle die Liste: jetzt ist nur die erste Sektion des Teilbaumes in der Liste
		Section.Iterator secIterator = sections.get(0).getIterator();
		//Section section = secIterator.getNext();
		sections.clear();
		while (secIterator.hasNext()) {
			Section section = secIterator.next();
			sections.add(section);
			//section = secIterator.next();
		}

		for (Section section : sections) {
			//logger.info("section id: " + section.getId() + " | section name: " + section.getName());
			float fractAlongParent = -1.0f;
			List<Segment> segments = section.getSegments();

			if (segments.size() > 0) {
				int startSegId = segments.get(0).getId();
				int endSegId = startSegId;
				if (segments.size() > 0) {
					endSegId = segments.get((segments.size() - 1)).getId();
				}

				sectionToStartEndSegment.put(section, new Pair<Integer, Integer>(startSegId, endSegId));
				int counter = 0;
				for (Segment segment : segments) {
					sstart = segment.getStart();
					send = segment.getEnd();
					sradiusstart = segment.getStartRadius();
					sradiusend = segment.getEndRadius();
					NeuroMLSegment neuroMLSegment = new NeuroMLSegment();
					neuroMLSegment.id = segment.getId();
					neuroMLSegment.cable = section.getId();
					neuroMLSegment.name = segment.getName();
					if (segment.getName() == null) {
						neuroMLSegment.name = "N" + nn + cableName + section.getId() + "_seg_id_" + segment.getId();
					}
					if (counter == 0) {
						if (section.getParentalLink() != null && section.getParentalLink().getParental() != null) {
							Section parentSection = section.getParentalLink().getParental();
							fractAlongParent = parentSection.getFractAlongParentForChild(section);
							int parentID = -1;
							if (fractAlongParent > 0.0f) {
								parentID = sectionToStartEndSegment.get(parentSection).second;
								//logger.info("parentID if: " + parentID);
							} else {
								parentID = sectionToStartEndSegment.get(parentSection).first;
								//logger.info("parentID else: " + parentID);
							}
							neuroMLSegment.parent = parentID;
						}
					} else {
						neuroMLSegment.parent = neuroMLSegment.id - 1;
					}

					if (counter == 0) {
						neuroMLSegment.proximal = new Proximal(sstart.x, sstart.y, sstart.z, sradiusstart * 2);
					}
					neuroMLSegment.distal = new Distal(send.x, send.y, send.z, sradiusend * 2);
					neuroMLList.add(neuroMLSegment);
					counter++;
				}
				int sectionID = section.getId();
				Cable cable = null;
				if (fractAlongParent > -1.0f) {
					//CableFAP cableFAP = new CableFAP(sectionID, "N" + nn + cableName + sectionID);
					CableFAP cableFAP = new CableFAP(sectionID, section.getName());
					cableFAP.fractAlongParent = (int) fractAlongParent;
					cable = cableFAP;
				} else {
					//cable = new Cable(sectionID, "N" + nn + cableName + sectionID);
					cable = new Cable(sectionID, section.getName());
				}

				cables.add(cable);
              			}
		}
	}

	/**
	 * @brief write for level 3 of NeuroML 
	 */
	private void writeXMLCells(ObjectOutputStream oos, Net net) {
		writeXMLNet(oos, net);
	}
	
	/**
	 * @brief write the synapses to xml
	 * @author stephanmg
	 * 
	 * @todo add biophysics of synapses for Level 3 output
	 * 
	 * @param oos
	 * @param net
	 */
	private void writeXMLSynapses(ObjectOutputStream oos, Net net) {
		@SuppressWarnings("unchecked")
		List<Cons> synapseList = net.getSynapseList();

		logger.info("Number of synapses: " + synapseList.size());

		ArrayList<NeuroMLSynapse> neuroMLSynapses = new ArrayList<NeuroMLSynapse>();

		/// iterate over all synapses in the network
		int counter = 0;
		for (Cons synapse : synapseList) {
			if (synapse.getNeuron1() == null && synapse.getNeuron2() == null) {
				continue;
			}

			if (synapse.getNeuron1() == null && synapse.getNeuron2() != null) {
				/// unilateral synapse
				Point3f injection = synapse.getNeuron2DenSectionSegment().getEnd();
				NeuroMLSynapseUnilateral neuromlSynapse = new NeuroMLSynapseUnilateral(injection);
				neuromlSynapse.setPre_cell_id(synapse.getNeuron2().getIndex());
				neuromlSynapse.setPre_segment_id(synapse.getNeuron2DenSectionSegment().getId());
				
				int c_idx = 0;
				for (Segment denSeg : synapse.getNeuron2DenSection().getSegments()) {
					if (denSeg.getId() == synapse.getNeuron2DenSectionSegment().getId()) {
						break;
					}
					c_idx++;
				}

				float dd = NeuronalTreeStructure.getDendriteSectionData(synapse.getNeuron2DenSection(), c_idx);
				neuromlSynapse.setPre_fraction_along(dd);
				neuromlSynapse.setId(counter);
				counter++;
				neuroMLSynapses.add(neuromlSynapse);
			} else if (synapse.getNeuron1() != null && synapse.getNeuron2() == null) {
				/// unilateral synapse
			} else if (synapse.getNeuron1() != null && synapse.getNeuron2() != null) {
				/// functional/bilateral synapse 
				Point3f axon_end = synapse.getNeuron1AxSegment().getEnd(); /// Start of synapse
				Point3f dendrite_start = synapse.getNeuron2DenSectionSegment().getStart(); /// End of synapse
				
				int c_idx = 0;
				for (Segment denSeg : synapse.getNeuron2DenSection().getSegments()) {
					if (denSeg.getId() == synapse.getNeuron2DenSectionSegment().getId()) {
						break;
					}
					c_idx++;
				}

				float dd = NeuronalTreeStructure.getDendriteSectionData(synapse.getNeuron2DenSection(), c_idx);
				
				Section ax_section = synapse.getNeuron1AxSection();
				assert (ax_section.getLength() > 0.0);
				int axSegPos = 0;
				for (Segment axSeg : ax_section.getSegments()) {
					if (axSeg.getId() == synapse.getNeuron1AxSegment().getId()) {
						break;
					}
					axSegPos++;
				}

				float ff = NeuronalTreeStructure.getDendriteSectionData(synapse.getNeuron1AxSection(), axSegPos);
				NeuroMLSynapseBilateral neuromlSynapse = new NeuroMLSynapseBilateral(axon_end, dendrite_start);
				neuromlSynapse.setPre_cell_id(synapse.getNeuron2().getIndex());
				neuromlSynapse.setPre_segment_id(synapse.getNeuron2DenSectionSegment().getId());
				neuromlSynapse.setPost_cell_id(synapse.getNeuron1().getIndex());
				neuromlSynapse.setPost_segment_id(synapse.getNeuron1AxSegment().getId());
				neuromlSynapse.setPre_fraction_along(dd);
				neuromlSynapse.setPost_fraction_along(ff);
				neuromlSynapse.setId(counter);
				counter++;
				neuroMLSynapses.add(neuromlSynapse);
			}
		}

		NeuroMLWriterTask task = NeuroMLWriterTask.getInstance("Synapses");
		NeuroMLProjections projections = new NeuroMLProjections();
		ArrayList<NeuroMLProjection> projection = new ArrayList<NeuroMLProjection>();
		NeuroMLConnections current_connections = new NeuroMLConnections();
		NeuroMLProjection current_projection = new NeuroMLProjection();
		
		ArrayList<NetworkMLElement> elements = new ArrayList<NetworkMLElement>();
		task.setMyProgress(0.0f);
			int i = 0;
			for (NeuroMLSynapse synapse : neuroMLSynapses) {
				logger.info("Synapse: " + synapse.toString());
				elements.add(synapse);
			}
			
			current_connections.setElements(elements);
			current_connections.setSize(elements.size());
			current_projection.setConnections(current_connections);
			projection.add(current_projection);
			projections.setProjection(projection);

			try {
				oos.writeObject(projections);
				oos.flush();
			} catch (IOException e) {
					logger.error(e, e);
			}

			if (task != null) {
				i++;
				logger.info("progress: " + i / (float) neuroMLSynapses.size());
				task.setMyProgress( i / (float) neuroMLSynapses.size());
			}
			
			if (task != null) {
				task.setMyProgress(1.0f);
			}
	}

	/**
	 * @brief Function to write the neural net to a XML file
	 * @author stephanmg
	 * 
	 * @param oos
	 * @param net
	 */
	private void writeXMLNet(ObjectOutputStream oos, Net net) {
		// get the number of neurons from neural net
		int nneuron = net.getNumNeurons();
		String schema = "http://morphml.org/morphml/schema";

		NeuroMLWriterTask task = NeuroMLWriterTask.getInstance("Neurons");

		for (int i = 0; i < nneuron; i++) {
			Neuron neuron = net.getNeuronList().get(i);
			NeuroMLCell neuroMLCell = new NeuroMLCell();
			neuroMLCell.name = "N" + i + "soma";

			//sets soma, dendrite and axon
			List<Cable> cables = new ArrayList<Cable>();
			List<NeuroMLSegment> segments = new ArrayList<NeuroMLSegment>();

			Cellipsoid soma = neuron.getSoma();
			List<Section> sections = soma.getSections();

			if (soma.getSections().isEmpty()) {
				sections.add(neuron.getSoma().cylindricRepresentant());
			}
			// soma first
			copyToNeuroML(sections, segments, cables, i, "_soma_");
			sections.clear();

			Axon axon = neuron.getAxon();
			sections.add(axon.getFirstSection());
			copyToNeuroML(sections, segments, cables, i, "_axon_");
			sections.clear();

			List<Dendrite> dendritesList = neuron.getDendrites();
			for (Dendrite dendrite : dendritesList) {
				sections.add(dendrite.getFirstSection());
				copyToNeuroML(sections, segments, cables, i, "_dendrite_");
				sections.clear();
			}

			// add soma, dendrite and axon to XML
			NeuroMLSegments neuroMLSegments = new NeuroMLSegments(segments, schema);
			neuroMLCell.setSegments(neuroMLSegments);
			NeuroMLCables neuroMLCables = new NeuroMLCables(cables, schema);
			neuroMLCell.setCables(neuroMLCables);
			try {
				oos.writeObject(neuroMLCell);
				oos.flush();
				//oos.close();
			} catch (IOException e) {
				logger.error(e, e);
			}
			if (task != null) {
				float process = (float) (i + 1) / (float) nneuron;
				task.setMyProgress(process);
			}
			/// trigger.trigger((i + 1) / (float) nneuron);
		}
		if (task != null) {
			task.setMyProgress(1.0f);
		}
	}

	/**
	 * @brief exports the NeuroML data, i. e. level 1, 2 or 3
	 * @author stephanmg <stephan@syntaktischer-zucker.de>
	 * 
	 * @param file
	 * @param net
	 * @throws IOException
	 */
	public static void exportData(File file, Net net) throws IOException {
		/// get the NeuroML level choser dialog
		NeuroMLLevelSelector choser = new NeuroMLLevelSelector(NeuGenView.getInstance().getFrame(), true);
		choser.setVisible(true);
		System.err.println("LEVELS: " + Arrays.toString(choser.getNeuroMLLevels()));
		
		/// for each specified level we export
		for (int level : choser.getNeuroMLLevels()) {
			NeuroMLWriter exportML = new NeuroMLWriter();
			exportML.setNeuromlLevel(level);
			exportML.initXStream();
			exportML.setXStreamOptions(level);

			String head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
			String name = "NeuroML Level " + level + " file exported from " + VERSION;
			String lengthUnits = "micron";
			String schemaLocation = "";
			if (level == 1) {
				schemaLocation = "http://morphml.org/neuroml/schema NeuroML_Level1_v1.7.xsd";
			} else if (level == 2) {
				schemaLocation = "http://morphml.org/neuroml/schema NeuroML_Level2_v1.7.xsd";
			} else if (level == 3) {
				schemaLocation = "http://morphml.org/networkml/schema../../Schemata/v1.7/Level3/NetworkML_v1.7.xsd";
			} else {
			}

			Neuroml neuroml = new Neuroml();
			neuroml.setHeader(schemaLocation, name, lengthUnits);

			try {
				/// prepare fiels
				String xml = "xml";
				String extension = Utils.getExtension(file);
				File net_output = null; /// Level 1 output file
				File biophysics_output = null; /// Level 2 output file
				File synapses_output = null; /// Level 3 output file
				
				if (!xml.equals(extension)) {
					net_output = new File(file.getAbsolutePath() + "_NeuroML_Level1" + "." + xml);
					biophysics_output = new File(file.getAbsoluteFile() + "_NeuroML_Level2" + "." + xml);
					synapses_output = new File(file.getAbsoluteFile() + "_NeuroML_Level3" + "." + xml);
				}
				
				
				/// output level 1 (MorphML)
				if (level == 1) {
					FileWriter writer = new FileWriter(net_output);
					writer.write(head);

					PrettyPrintWriter writerP = new PrettyPrintWriter(writer);
					writerP.startNode("morphml");
					writerP.addAttribute("xmlns", neuroml.xmlns);
					writerP.addAttribute("\n\t\t xmlns:mml", neuroml.mml);
					writerP.addAttribute("\n\t\t xmlns:meta", neuroml.meta);
					writerP.addAttribute("\n\t\t xmlns:bio", neuroml.bio);
					writerP.addAttribute("\n\t\t xmlns:cml", neuroml.cml);
					writerP.addAttribute("\n\t\t xmlns:xsi", neuroml.xsi);
					writerP.addAttribute("\n\t\t xsi:schemaLocation", schemaLocation);
					writerP.addAttribute("\n\t\t name", name);
					writerP.addAttribute("\n\t\t lengthUnits", lengthUnits);

					XStream xstreamLoc = getXstream();
					ObjectOutputStream oos = xstreamLoc.createObjectOutputStream(writerP, "cells");
					exportML.writeXMLNet(oos, net);
					writerP.endNode();
					
					oos.flush();
					oos.close();
				}
				
				/// output level 2 (ChannelML)
				if (level == 2) {	
					XStream xstreamLoc3 = getXstream();
					FileWriter fw3 = new FileWriter(biophysics_output);
					fw3.write(head);
					PrettyPrintWriter writerP3 = new PrettyPrintWriter(fw3);
					ObjectOutputStream oos3 = xstreamLoc3.createObjectOutputStream(writerP3, "channelml");
						
					writerP3.addAttribute("xmlns", neuroml.xmlns);
					writerP3.addAttribute("\n\t\t xmlns:mml", neuroml.mml);
					writerP3.addAttribute("\n\t\t xmlns:meta", neuroml.meta);
					writerP3.addAttribute("\n\t\t xmlns:bio", neuroml.bio);
					writerP3.addAttribute("\n\t\t xmlns:cml", neuroml.cml);
					writerP3.addAttribute("\n\t\t xmlns:xsi", neuroml.xsi);
					writerP3.addAttribute("\n\t\t xsi:schemaLocation", schemaLocation);
					writerP3.addAttribute("\n\t\t name", name);
					writerP3.addAttribute("\n\t\t lengthUnits", lengthUnits);
					
					writerP3.startNode("cells");
					exportML.writeXMLCells(oos3, net);
					writerP3.endNode();
					
					/// writerP3.startNode("channels");
					exportML.writeXMLChannels(oos3, net);
					/// writerP3.endNode()
					
					oos3.flush();
					oos3.close();
				}

				/// output level 3 (NetworkML)
				if (level == 3) {
					XStream xstreamLoc2 = getXstream();
					FileWriter fw = new FileWriter(synapses_output);
					fw.write(head);
					PrettyPrintWriter writerP2 = new PrettyPrintWriter(fw);
					///writerP2.startNode("neuroml");
					xstreamLoc2.addImplicitCollection(NeuroMLProjections.class, "projection");
					xstreamLoc2.useAttributeFor(NeuroMLProjection.class, "name");
					xstreamLoc2.useAttributeFor(NeuroMLProjection.class, "source");
					xstreamLoc2.useAttributeFor(NeuroMLProjection.class, "target");
					xstreamLoc2.useAttributeFor(NeuroMLSynapseProperty.class, "type");
					xstreamLoc2.useAttributeFor(NeuroMLSynapseProperty.class, "weight");
					xstreamLoc2.useAttributeFor(NeuroMLSynapseProperty.class, "internal_delay");
					xstreamLoc2.useAttributeFor(NeuroMLSynapseProperty.class, "threshold");
					xstreamLoc2.useAttributeFor(NeuroMLConnections.class, "size");
					xstreamLoc2.useAttributeFor(NeuroMLProjections.class, "xmlns");
					xstreamLoc2.useAttributeFor(NeuroMLProjections.class, "units");
					
					ObjectOutputStream oos2 = xstreamLoc2.createObjectOutputStream(writerP2, "networkml");
					writerP2.addAttribute("xmlns", neuroml.xmlns);
					writerP2.addAttribute("\n\t\t xmlns:mml", neuroml.mml);
					writerP2.addAttribute("\n\t\t xmlns:meta", neuroml.meta);
					writerP2.addAttribute("\n\t\t xmlns:bio", neuroml.bio);
					writerP2.addAttribute("\n\t\t xmlns:cml", neuroml.cml);
					writerP2.addAttribute("\n\t\t xmlns:xsi", neuroml.xsi);
					writerP2.addAttribute("\n\t\t xsi:schemaLocation", schemaLocation);
					writerP2.addAttribute("\n\t\t name", name);
					writerP2.addAttribute("\n\t\t lengthUnits", lengthUnits);
					
					writerP2.startNode("cells");
					exportML.writeXMLCells(oos2, net);
					writerP2.endNode();
					
					/// writerP2.startNode("channels");
					exportML.writeXMLChannels(oos2, net);
					/// writerP2.endNode();
					
					exportML.writeXMLSynapses(oos2, net);
					
					oos2.flush();
					oos2.close();

					/**
					 * @note 
					 * remove duplicated underscores from synapses.xml
					 * note however that this is not the best option
					 * better would be: use new xstream version and use XmLFriendlyReplacer
					 * however: if we upgrade xstream some serialization is broken,
					 * this needs to be investigated in the future since it could
					 * garble up the output, if values contain also underscores
					 * not just nodes or attribute names...
					 */
					String content = IOUtils.toString(new FileInputStream(synapses_output));
					content = content.replaceAll("__", "_");
					IOUtils.write(content, new FileOutputStream(synapses_output)); 
				}
			} catch (Exception e) {
				logger.error(e, e);
			}
		}
	}

	/**
	 * @brief writes level 2 of NeuroML, i. e. ChannelML
	 * 
	 * @todo implement ChannelML (Level 2)
	 * 
	 * @param oos
	 * @param net 
	 */
	private void writeXMLChannels(ObjectOutputStream oos, Net net) {
	}
}
