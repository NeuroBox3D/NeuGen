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
package org.neugen.quickhull3d;

/**
 * Maintains a double-linked list of vertices for use by QuickHull3D
 */
class VertexList {

    private Vertex head;
    private Vertex tail;

    /**
     * Clears this list.
     */
    public void clear() {
        head = tail = null;
    }

    /**
     * Adds a vertex to the end of this list.
     */
    public void add(Vertex vtx) {
        if (head == null) {
            head = vtx;
        } else {
            tail.next = vtx;
        }
        vtx.prev = tail;
        vtx.next = null;
        tail = vtx;
    }

    /**
     * Adds a chain of vertices to the end of this list.
     */
    public void addAll(Vertex vtx) {
        if (head == null) {
            head = vtx;
        } else {
            tail.next = vtx;
        }
        vtx.prev = tail;
        while (vtx.next != null) {
            vtx = vtx.next;
        }
        tail = vtx;
    }

    /**
     * Deletes a vertex from this list.
     */
    public void delete(Vertex vtx) {
        if (vtx.prev == null) {
            head = vtx.next;
        } else {
            vtx.prev.next = vtx.next;
        }
        if (vtx.next == null) {
            tail = vtx.prev;
        } else {
            vtx.next.prev = vtx.prev;
        }
    }

    /**
     * Deletes a chain of vertices from this list.
     */
    public void delete(Vertex vtx1, Vertex vtx2) {
        if (vtx1.prev == null) {
            head = vtx2.next;
        } else {
            vtx1.prev.next = vtx2.next;
        }
        if (vtx2.next == null) {
            tail = vtx1.prev;
        } else {
            vtx2.next.prev = vtx1.prev;
        }
    }

    /**
     * Inserts a vertex into this list before another specificed vertex.
     */
    public void insertBefore(Vertex vtx, Vertex next) {
        vtx.prev = next.prev;
        if (next.prev == null) {
            head = vtx;
        } else {
            next.prev.next = vtx;
        }
        vtx.next = next;
        next.prev = vtx;
    }

    /**
     * Returns the first element in this list.
     */
    public Vertex first() {
        return head;
    }

    /**
     * Returns true if this list is empty.
     */
    public boolean isEmpty() {
        return head == null;
    }
}
