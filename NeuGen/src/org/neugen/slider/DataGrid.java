/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2007–2012 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
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

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * Class for a data grid with integer coordinates.
 * The coordinate order serves to map the coordinates
 * at keys of maps used to store data.
 * 
 * @author alwa
 *
 * @param <Float>
 */
public final class DataGrid<Float> {

    public enum CoordinateType {

        X(0), Y(1), Z(2);

        private CoordinateType(int tripleIndex) {
            this.tupleIndex = tripleIndex;
        }
        int tupleIndex = 0;

        public int getTripleIndex() {
            return tupleIndex;
        }
    };
    CoordinateType order[];

    /**
     * @param order is the mapping order for the data grid.
     */
    public DataGrid(CoordinateType[] order) {
        super();
        this.order = order;
    }
    /**
     * Data encoding (coordinate->(coordinate->(coordinate->data)))
     */
    public Map<Integer, Map<Integer, Map<Integer, Float>>> dataGrid = new TreeMap<Integer, Map<Integer, Map<Integer, Float>>>();

    public void set(int coord[], Float d) {
        int coordinate1 = coord[order[0].tupleIndex];
        Map<Integer, Map<Integer, Float>> layer = dataGrid.get(coordinate1);
        if (layer == null) {
            layer = new TreeMap<Integer, Map<Integer, Float>>();
            dataGrid.put(coordinate1, layer);
        }

        int coordinate2 = coord[order[1].tupleIndex];
        Map<Integer, Float> line = layer.get(coordinate2);
        if (line == null) {
            line = new TreeMap<Integer, Float>();
            layer.put(coordinate2, line);
        }

        int coordinate3 = coord[order[2].tupleIndex];
        line.put(coordinate3, d);
    }

    public Map<Integer, Map<Integer, Map<Integer, Float>>> getData() {
        return dataGrid;
    }

    public long getNDataPoints() {
        long ret = 0;
        Set<Entry<Integer, Map<Integer, Map<Integer, Float>>>> entrySet1 = dataGrid.entrySet();
        for (Entry<Integer, Map<Integer, Map<Integer, Float>>> e1 : entrySet1) {
            Map<Integer, Map<Integer, Float>> layer = e1.getValue();
            Set<Entry<Integer, Map<Integer, Float>>> lines = layer.entrySet();
            for (Entry<Integer, Map<Integer, Float>> line : lines) {
                ret += line.getValue().size();
            }
        }
        return ret;
    }
}
