/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neugen.vrl;

import eu.mihosoft.vrl.annotation.ComponentInfo;
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
@ComponentInfo(name = "DensityVisualizationParamsInput", category = "NeuGen")
public class DensityVisualizationParamsInput implements Serializable {

    private static long serialVersionUID = 1L;
    private static final String CUBES = "Cubes";
    private static final String CONVEX_HULL = "Convex Hull";
    private static final String DEVIDED_CONVEX_HULL = "Divided Convex Hull";

    @OutputInfo(style = "silent", name = "params")
    public DensityVisualizationParams setParams(
            @ParamInfo(name = "Visualization",
            style = "selection",
            options = "value=[\"Cubes\",\"Convex Hull\",\"Divided Convex Hull\"]") String selectionInput,
            @ParamInfo(name = "Tolerance") double tolerance,
            @ParamInfo(name = "Number of Visualizations",
            style = "selection",
            options = "value=[1,2,3,4]") int numberOfVisualizations,
            @ParamGroupInfo(group = "Colors|false|Visualization Colors")
            @ParamInfo(name = "Background Color") Color backgroundColor,
            @ParamGroupInfo(group = "Colors")
            @ParamInfo(name = "Color 1") Color color1,
            @ParamGroupInfo(group = "Colors")
            @ParamInfo(name = "Color 2") Color color2,
            @ParamGroupInfo(group = "Colors")
            @ParamInfo(name = "Color 3") Color color3,
            @ParamGroupInfo(group = "Colors")
            @ParamInfo(name = "Color 4") Color color4,
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
