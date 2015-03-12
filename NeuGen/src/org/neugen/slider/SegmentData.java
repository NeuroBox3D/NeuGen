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
package org.neugen.slider;

import java.util.LinkedList;
import java.util.List;
import javax.vecmath.Point3f;
import org.neugen.datastructures.Axon;
import org.neugen.datastructures.Cellipsoid;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.datastructures.Section;
import org.neugen.datastructures.Segment;

/**
 * @author Alexander Wanner
 */
public final class SegmentData {

    protected List<Segment> data = new LinkedList<Segment>();
    protected Float minX, minY, minZ, maxX, maxY, maxZ;

    public SegmentData(Net net) {
        for (Neuron neuron : net.getNeuronList()) {
            Cellipsoid soma = neuron.getSoma();
            List<Section> sections = soma.getSections();
            if (soma.getSections().isEmpty()) {
                sections.add(neuron.getSoma().cylindricRepresentant());
            }
            for (Section sec : sections) {
                Section.Iterator secIterator = sec.getIterator();
                while (secIterator.hasNext()) {
                    Section section = secIterator.next();
                    for (Segment ax_segment : section.getSegments()) {
                        updateMinMax(ax_segment);
                        data.add(ax_segment);
                    }
                }
            }

            for (Dendrite den : neuron.getDendrites()) {
                Section.Iterator secIterator = den.getFirstSection().getIterator();
                while (secIterator.hasNext()) {
                    Section section = secIterator.next();
                    for (Segment den_segment : section.getSegments()) {
                        updateMinMax(den_segment);
                        data.add(den_segment);
                    }
                }
            }

            Axon axon = neuron.getAxon();
            Section.Iterator secIterator = axon.getFirstSection().getIterator();
            while (secIterator.hasNext()) {
                Section section = secIterator.next();
                for (Segment ax_segment : section.getSegments()) {
                    updateMinMax(ax_segment);
                    data.add(ax_segment);
                }
            }

        }
        System.out.println("data size: " + data.size());
    }

    /**
     * @param segment
     */
    private void updateMinMax(Segment segment) {
        if (segment == null) {
            return;
        }

        Point3f points[] = {segment.getStart(), segment.getEnd()};
        float radii[] = {segment.getStartRadius(), segment.getEndRadius()};
        for (int i = 0; i < points.length; ++i) {
            Point3f p = points[i];
            float r = radii[i];
            if (minX == null || p.x - r < minX) {
                minX = p.x - r;
            }
            if (minY == null || p.y - r < minY) {
                minY = p.y - r;
            }
            if (minZ == null || p.z - r < minZ) {
                minZ = p.z - r;
            }

            if (maxX == null || p.x + r > maxX) {
                maxX = p.x + r;
            }
            if (maxY == null || p.y + r > maxY) {
                maxY = p.y + r;
            }
            if (maxZ == null || p.z + r > maxZ) {
                maxZ = p.z + r;
            }
        }
    }

    public int getNumberOfSegments() {
        return data.size();
    }

    public List<Segment> getData() {
        return data;
    }

    public Point3f getMins() {
        return new Point3f(minX, minY, minZ);
    }

    public Point3f getMaxs() {
        return new Point3f(maxX, maxY, maxZ);
    }
}
