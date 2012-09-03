/*
 * File: AxonSection.java
 * Created on 15.10.2009, 10:42:12
 *
 */
package org.neugen.datastructures;

import java.io.Serializable;
import javax.vecmath.Point3f;

/**
 * @author Alexander Wanner
 */
public final class AxonSection extends Section implements Serializable {

    private static final long serialVersionUID = -4611946473283033478L;

    public AxonSection() {
        super();
        secType = SectionType.NOT_MYELINIZED;
    }

    public AxonSection(float startRadius, Point3f start, float endRadius, Point3f end, int nsegs) {
        super(startRadius, start, endRadius, end, nsegs);
        secType = SectionType.NOT_MYELINIZED;
    }

    public AxonSection(Section source) {
        super(source);
        secType = SectionType.NOT_MYELINIZED;
    }
}
