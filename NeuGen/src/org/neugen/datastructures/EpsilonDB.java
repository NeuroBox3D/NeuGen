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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.vecmath.Point3f;
import javax.vecmath.Point3i;
import javax.vecmath.Vector3f;
import org.apache.log4j.Logger;

/**
 * @author Alexander Wanner
 */
public final class EpsilonDB  {

    //static final long serialVersionUID = -7041930069184524614L;

    /** use to log messages */
    private final static Logger logger = Logger.getLogger(EpsilonDB.class.getName());
    private float epsilon;
    //private HashMap<int[], Cube> db = new HashMap<int[], Cube>();
    private final Map<String, Cube> db;
    //private Hashtable<String, Cube> db = new Hashtable<String, Cube>();
    private final Set<Integer> coordX;
    private final Set<Integer> coordY;
    private final Set<Integer> coordZ;

    public EpsilonDB(float eps) {
        epsilon = eps;
        db = new HashMap<String, Cube>();
        coordX = new HashSet<Integer>();
        coordY = new HashSet<Integer>();
        coordZ = new HashSet<Integer>();
    }

    public class Cube {

        private List<NetBase.AxonSegmentData> points;

        public Cube() {
            points = new ArrayList<NetBase.AxonSegmentData>();
        }

        public List<NetBase.AxonSegmentData> getPoints() {
            return points;
        }

        public void setPoints(List<NetBase.AxonSegmentData> points) {
            this.points = points;
        }

        @Override
        public String toString() {
            String mes = new String();
            for (int i = 0; i < points.size(); i++) {
                //  mes += i + "th " + points.get(i);
            }
            // if(points.size() > 1) {
            mes += "size of points: " + points.size();
            // }
            return mes;
        }
    }

    public Point3i getCubeCoord(Point3f toCalc) {
        Point3f v = new Point3f(toCalc);
        Point3i ret = new Point3i();
        v.x /= epsilon;
        v.y /= epsilon;
        v.z /= epsilon;
        ret.x = (int) v.x;
        ret.y = (int) v.y;
        ret.z = (int) v.z;

        if (v.x < 0) {
            ret.x -= 1;
        }
        if (v.y < 0) {
            ret.y -= 1;
        }
        if (v.z < 0) {
            ret.z -= 1;
        }
        return ret;
    }

    public void insert(NetBase.AxonSegmentData record) {
        Point3i refCoord = getCubeCoord(record.get3DVec());
        //String key = new String();
        coordX.add(refCoord.x);
        coordY.add(refCoord.y);
        coordZ.add(refCoord.z);
        StringBuilder key = new StringBuilder();
        key.append(refCoord.x).append(" ").append(refCoord.y).append(" ").append(refCoord.z);
        Cube cube = db.get(key.toString());
        if (cube == null) {
            cube = new Cube();
            //System.out.println("insert record to cube");
            cube.getPoints().add(record);
            db.put(key.toString(), cube);
        } else {
            cube.getPoints().add(record);
        }
    }

    public List<NetBase.AxonSegmentData> getEpsilonEnv(Point3f point) {
        Point3i refCoord = getCubeCoord(point);
        Point3i current = new Point3i();
        StringBuilder key = new StringBuilder(20);
        List<NetBase.AxonSegmentData> ret = new ArrayList<NetBase.AxonSegmentData>();
        for (int i = -1; i < 2; i++) {
            current.x = refCoord.x + i;
            if (coordX.contains(current.x)) {
                // if (true) {
                key.append(current.x).append(" ");
                //logger.info("key: " + key.toString());
                int firstKeyLenght = key.length();
                for (int j = -1; j < 2; j++) {
                    current.y = refCoord.y + j;
                    if (coordX.contains(current.y)) {
                        //if (true) {
                        key.append(current.y).append(" ");
                        int currentKeyLenght = key.length();
                        for (int k = -1; k < 2; k++) {
                            current.z = refCoord.z + k;
                            if (coordZ.contains(current.z)) {
                                //if (true) {
                                key.append(current.z);
                                /*
                                logger.info("current[0]: " + current[0]);
                                logger.info("current[1]: " + current[1]);
                                logger.info("current[2]: " + current[2]);
                                 */
                                //String key = new String();
                                //StringBuffer key = new StringBuffer();
                                /*
                                for (int m = 0; m < current.length; m++) {
                                //key += current[m] + " ";
                                key.append(current[m]).append(" ");
                                }
                                 */
                                //logger.info("key.lenght: " + key.length());
                                Cube cube = db.get(key.toString());
                                //key.delete(0, key.length());
                                if (cube != null) {
                                    //logger.info("cube is not null!!!!!!!");
                                    for (int l = 0; l < cube.getPoints().size(); l++) {
                                        //float[] v2 = cube.points.get(l).get3DVec();
                                        Point3f v2 = cube.points.get(l).get3DVec();
                                        //logger.info("key: " + key[0] + " " + key[1] + " " + key[2]);
                                        //logger.info("point: " + point[0] + " " + point[1] + " " + point[2]);
                                        //logger.info("v2: " + v2[0] + " " + v2[1] + " " + v2[2]);
                                        Vector3f direction = new Vector3f();
                                        direction.sub(point, v2);
                                        float dist = direction.length();
                                        if (dist < epsilon) {
                                            ret.add(cube.getPoints().get(l));
                                        }
                                    }
                                }
                                //logger.info("key before delete: " + key.toString());
                                key.delete(currentKeyLenght, key.length());
                                //logger.info("key after delete: " + key.toString());
                            }
                        }  // 3. for loop end
                        key.delete(firstKeyLenght, key.length());
                    }
                } // 2. for loop end
                key.delete(0, key.length());
            }
        } // 1. for loop end
        return ret;
    }

    public void toDisplay() {
        for (Map.Entry<String, Cube> e : db.entrySet()) {
            String xyz = e.getKey();
            Cube cube = e.getValue();
            //System.out.println("intern coordinate xyz: " + xyz[0] + "," + xyz[1] + "," + xyz[2]);
            logger.info("cube: " + cube.toString());
        }
    }
}
