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
