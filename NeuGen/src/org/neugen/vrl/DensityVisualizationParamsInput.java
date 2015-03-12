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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neugen.vrl;

import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ObjectInfo;
import eu.mihosoft.vrl.annotation.OutputInfo;
import eu.mihosoft.vrl.annotation.ParamGroupInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import java.awt.Color;
import java.io.Serializable;
import javax.vecmath.Color3f;
import org.neugen.datastructures.VolumeOfVoxels;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
@ComponentInfo(name = "Density Params", category = "NeuGen")
@ObjectInfo(name = "Density Input Params")
public class DensityVisualizationParamsInput implements Serializable {

    private static long serialVersionUID = 1L;
    private static final String CUBES = "Cubes";
    private static final String CONVEX_HULL = "Convex Hull";
    private static final String DEVIDED_CONVEX_HULL = "Divided Convex Hull";

    @OutputInfo(style = "silent", name = "params")
    public DensityVisualizationParams setParams(
            @ParamGroupInfo(group = "General|false|Visualization Type, Number of Visualizations etc.")
            @ParamInfo(name = "Visualization Type",
            style = "selection",
            options = "value=[\"Cubes\",\"Convex Hull\",\"Divided Convex Hull\"]") String selectionInput,
            @ParamGroupInfo(group = "General")
            @ParamInfo(name = "Tolerance") double tolerance,
            @ParamGroupInfo(group = "General")
            @ParamInfo(name = "Number of Visualizations",
            style = "selection",
            options = "value=[1,2,3,4]") int numberOfVisualizations,
            @ParamGroupInfo(group = "Colors|false|Visualization Colors")
            @ParamInfo(name = "BGColor", style="color-chooser") Color backgroundColor,
            @ParamGroupInfo(group = "Colors")
            @ParamInfo(name = "Color 1", style="color-chooser") Color color1,
            @ParamGroupInfo(group = "Colors")
            @ParamInfo(name = "Color 2", style="color-chooser") Color color2,
            @ParamGroupInfo(group = "Colors")
            @ParamInfo(name = "Color 3", style="color-chooser") Color color3,
            @ParamGroupInfo(group = "Colors")
            @ParamInfo(name = "Color 4", style="color-chooser") Color color4,
            @ParamGroupInfo(group = "Values|false|Values")
            @ParamInfo(name = "Value 1") float value1,
            @ParamGroupInfo(group = "Values")
            @ParamInfo(name = "Value 2") float value2,
            @ParamGroupInfo(group = "Values")
            @ParamInfo(name = "Value 3") float value3,
            @ParamGroupInfo(group = "Values")
            @ParamInfo(name = "Value 4") float value4,
            @ParamGroupInfo(group = "Transparencies|false|Visualization Transparencies")
            @ParamInfo(name = "Transparency 1") float transparency1,
            @ParamGroupInfo(group = "Transparencies")
            @ParamInfo(name = "Transparency 2") float transparency2,
            @ParamGroupInfo(group = "Transparencies")
            @ParamInfo(name = "Transparency 3") float transparency3,
            @ParamGroupInfo(group = "Transparencies")
            @ParamInfo(name = "Transparency 4") float transparency4) {

        boolean visualizeWithCubes = CUBES.equals(selectionInput);
        boolean visualizeWithConvexHull = CONVEX_HULL.equals(selectionInput);
        boolean visualizeWithDividedConvexHull = DEVIDED_CONVEX_HULL.equals(selectionInput);

        return new DensityVisualizationParams(
                VRLDensityVisualizationTask.Density.IMAGE,
                visualizeWithCubes,
                visualizeWithConvexHull,
                visualizeWithDividedConvexHull,
                tolerance,
                numberOfVisualizations,
                color2Color3f(backgroundColor),
                color2Color3f(color1),
                color2Color3f(color2),
                color2Color3f(color3),
                color2Color3f(color4),
                value1, value2, value3, value4,
                transparency1,
                transparency2,
                transparency3,
                transparency4);
    }

    private Color3f color2Color3f(Color c) {
        return new Color3f(c.getRed() / 255f,
                c.getGreen() / 255f,
                c.getBlue() / 255f);
    }
}


