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
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.vecmath.Point3f;
import org.apache.log4j.Logger;
import org.neugen.datastructures.Axon;
import org.neugen.datastructures.Cellipsoid;
import org.neugen.datastructures.Cons;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.datastructures.Pair;
import org.neugen.datastructures.Section;
import org.neugen.datastructures.Segment;
import org.neugen.parsers.NeuroML.NetworkML.NetworkMLElement;
import org.neugen.parsers.NeuroML.NetworkML.NeuroMLNetwork;
import org.neugen.parsers.NeuroML.NetworkML.NeuroMLSynapse;
import org.neugen.parsers.NeuroML.NetworkML.NeuroMLSynapseBilateral;
import org.neugen.parsers.NeuroML.NetworkML.NeuroMLSynapseUnilateral;
import org.neugen.utils.Utils;

/**
 *
 * @author Alexander Wanner
 * @author Sergei Wolf
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
	 * Function to get neuroml output options
	 *
	 * @return xstream object with output options for xml format
	 */
	private void setXStreamOptions(int level) {
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
		
		if (level < 4) {
			/**
			 * do we really need the level output? we could also remove the option here ...
			 * since in the GUI this option isnt present at all
			 * @todo set the appropriate tags and attributes, given the NetworkML NeuroML Level 3 XML schema specification
			 */
			xstream.alias("UniLateralSynapse", NeuroMLSynapseUnilateral.class);
			xstream.alias("BiLateralSynapse", NeuroMLSynapseBilateral.class);
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
	 * @brief write the synapses (unilateral (nf synapses) and bilateral
	 * (functional synapses))
	 * @author stephanmg
	 * 
	 * @param oos
	 * @param net
	 */
	private void writeXMLSynapses(ObjectOutputStream oos, Net net) {
		/**
		 * @todo build a NetworkMLNetwork, with the NeuroMLSynapse as elements
		 * within teh NetworkMLNetwork, then we can use teh NetworkML NeuroML Level 3 
		 * XML schema specification to generate the correct XML output
		 */
		@SuppressWarnings("unchecked")
		NeuroMLNetwork network = new NeuroMLNetwork(3, new ArrayList<NetworkMLElement>());
		List<Cons> synapseList = net.getSynapseList();

		logger.info("Number of synapses: " + synapseList.size());

		ArrayList<NeuroMLSynapse> neuroMLSynapses = new ArrayList<NeuroMLSynapse>();

		/// iterate over all synapses in the network
		/**
		 * @todo also consider the biophysics of the synapse stored in 
		 * the synapse objects!
		 */
		for (Cons synapse : synapseList) {
			if (synapse.getNeuron1() == null && synapse.getNeuron2() == null) {
				continue;
			}

			if (synapse.getNeuron1() == null && synapse.getNeuron2() != null) {
				/// unilateral synapse
				Point3f injection = synapse.getNeuron2DenSectionSegment().getEnd();
				neuroMLSynapses.add(new NeuroMLSynapseUnilateral(injection));
			} else if (synapse.getNeuron1() != null && synapse.getNeuron2() == null) {
				/// unilateral synapse
			} else {
				/// functional/bilateral synapse 
				Point3f axon_end = synapse.getNeuron1AxSegment().getEnd(); /// Start of synapse
				Point3f dendrite_start = synapse.getNeuron2DenSectionSegment().getStart(); /// End of synapse
				neuroMLSynapses.add(new NeuroMLSynapseBilateral(axon_end, dendrite_start));
			}
		}

		NeuroMLWriterTask task = NeuroMLWriterTask.getInstance("Synapses");
		task.setMyProgress(0.0f);
			int i = 0;
			for (NeuroMLSynapse synapse : neuroMLSynapses) {
				logger.info("Synapse: " + synapse.toString());

				try {
					oos.writeObject(synapse);
					oos.flush();
				} catch (IOException e) {
					logger.error(e, e);
				}

				if (task != null) {
					i++;
					logger.info("progress: " + i / (float) neuroMLSynapses.size());
					task.setMyProgress( i / (float) neuroMLSynapses.size());
				}
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
			//trigger.trigger((i + 1) / (float) nneuron);
		}
		if (task != null) {
			task.setMyProgress(1.0f);
		}
	}

	/**
	 * @brief exports the NeuroML data, i. e. we need also to export the
	 * synapses below
	 * @author stephanmg
	 * 
	 * @param file
	 * @param net
	 * @throws IOException
	 */
	public static void exportData(File file, Net net) throws IOException {
		NeuroMLWriter exportML = new NeuroMLWriter();
		exportML.initXStream();
		int level = exportML.getNeuromlLevel();
		exportML.setXStreamOptions(level);

		String head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		String name = "NeuroML Level " + level + " file exported from NeuGen";
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
			String xml = "xml";
			String extension = Utils.getExtension(file);
			File net_output = null;
			File synapses_output = null;
			if (!xml.equals(extension)) {
				net_output = new File(file.getAbsolutePath() + "." + xml);
				synapses_output = new File(file.getAbsoluteFile() + "_synapses." + xml);
			}
			FileWriter writer = new FileWriter(net_output);
			writer.write(head);

			PrettyPrintWriter writerP = new PrettyPrintWriter(writer);
			writerP.startNode("neuroml");
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

			/// output level 3 (Network ML)
			XStream xstreamLoc2 = getXstream();
			FileWriter fw = new FileWriter(synapses_output);
			fw.write(head);
			PrettyPrintWriter writerP2 = new PrettyPrintWriter(fw);
			writerP2.startNode("neuroml");
			writerP2.addAttribute("xmlns", neuroml.xmlns);
			writerP2.addAttribute("\n\t\t xmlns:mml", neuroml.mml);
			writerP2.addAttribute("\n\t\t xmlns:meta", neuroml.meta);
			writerP2.addAttribute("\n\t\t xmlns:bio", neuroml.bio);
			writerP2.addAttribute("\n\t\t xmlns:cml", neuroml.cml);
			writerP2.addAttribute("\n\t\t xmlns:xsi", neuroml.xsi);
			writerP2.addAttribute("\n\t\t xsi:schemaLocation", schemaLocation);
			writerP2.addAttribute("\n\t\t name", name);
			writerP2.addAttribute("\n\t\t lengthUnits", lengthUnits);
			ObjectOutputStream oos2 = xstreamLoc2.createObjectOutputStream(writerP2, "network");
			exportML.writeXMLSynapses(oos2, net);
			writerP2.endNode();
			
			/// flush all streams and close
			oos.flush();
			oos.close();
			oos2.flush();
			oos2.close();
			
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
}
