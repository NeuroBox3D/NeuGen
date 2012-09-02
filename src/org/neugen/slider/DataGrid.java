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
