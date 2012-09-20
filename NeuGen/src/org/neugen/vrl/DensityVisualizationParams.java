/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neugen.vrl;

import java.io.Serializable;
import javax.vecmath.Color3f;
import org.neugen.vrl.VRLDensityVisualizationTask.Density;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class DensityVisualizationParams implements Serializable {

    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    private Density densityType;
    private boolean visualizeWithCubes;
    private boolean visualizeWithConvexHull;
    private boolean visualizeWithDividedConvexHull;
    private double tolerance;
    private int numberOfVisualizations;
    private Color3f backgroundColor;
    private Color3f color1;
    private Color3f color2;
    private Color3f color3;
    private Color3f color4;
    private float value1;
    private float value2;
    private float value3;
    private float value4;
    private float transparency1;
    private float transparency2;
    private float transparency3;
    private float transparency4;

    public DensityVisualizationParams() {
    }

    public DensityVisualizationParams(
            Density densityType,
            boolean visualizeWithCubes,
            boolean visualizeWithConvexHull,
            boolean visualizeWithDividedConvexHull,
            double tolerance,
            int numberOfVisualizations,
            Color3f backgroundColor,
            Color3f color1,
            Color3f color2,
            Color3f color3,
            Color3f color4,
            float value1,
            float value2,
            float value3,
            float value4,
            float transparency1,
            float transparency2,
            float transparency3,
            float transparency4) {
        this.densityType = densityType;
        this.visualizeWithCubes = visualizeWithCubes;
        this.visualizeWithConvexHull = visualizeWithConvexHull;
        this.visualizeWithDividedConvexHull = visualizeWithDividedConvexHull;
        this.tolerance = tolerance;
        this.numberOfVisualizations = numberOfVisualizations;
        this.backgroundColor = backgroundColor;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.transparency1 = transparency1;
        this.transparency2 = transparency2;
        this.transparency3 = transparency3;
        this.transparency4 = transparency4;
    }

    /**
     * @return the color1
     */
    public Color3f getColor1() {
        return color1;
    }

    /**
     * @param color1 the color1 to set
     */
    public void setColor1(Color3f color1) {
        this.color1 = color1;
    }

    /**
     * @return the color2
     */
    public Color3f getColor2() {
        return color2;
    }

    /**
     * @param color2 the color2 to set
     */
    public void setColor2(Color3f color2) {
        this.color2 = color2;
    }

    /**
     * @return the color3
     */
    public Color3f getColor3() {
        return color3;
    }

    /**
     * @param color3 the color3 to set
     */
    public void setColor3(Color3f color3) {
        this.color3 = color3;
    }

    /**
     * @return the color4
     */
    public Color3f getColor4() {
        return color4;
    }

    /**
     * @param color4 the color4 to set
     */
    public void setColor4(Color3f color4) {
        this.color4 = color4;
    }

    /**
     * @return the value1
     */
    public float getValue1() {
        return value1;
    }

    /**
     * @param value1 the value1 to set
     */
    public void setValue1(float value1) {
        this.value1 = value1;
    }

    /**
     * @return the value2
     */
    public float getValue2() {
        return value2;
    }

    /**
     * @param value2 the value2 to set
     */
    public void setValue2(float value2) {
        this.value2 = value2;
    }

    /**
     * @return the value3
     */
    public float getValue3() {
        return value3;
    }

    /**
     * @param value3 the value3 to set
     */
    public void setValue3(float value3) {
        this.value3 = value3;
    }

    /**
     * @return the value4
     */
    public float getValue4() {
        return value4;
    }

    /**
     * @param value4 the value4 to set
     */
    public void setValue4(float value4) {
        this.value4 = value4;
    }

    /**
     * @return the backgroundColor
     */
    public Color3f getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor the backgroundColor to set
     */
    public void setBackgroundColor(Color3f backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @return the transparency1
     */
    public float getTransparency1() {
        return transparency1;
    }

    /**
     * @param transparency1 the transparency1 to set
     */
    public void setTransparency1(float transparency1) {
        this.transparency1 = transparency1;
    }

    /**
     * @return the transparency2
     */
    public float getTransparency2() {
        return transparency2;
    }

    /**
     * @param transparency2 the transparency2 to set
     */
    public void setTransparency2(float transparency2) {
        this.transparency2 = transparency2;
    }

    /**
     * @return the transparency3
     */
    public float getTransparency3() {
        return transparency3;
    }

    /**
     * @param transparency3 the transparency3 to set
     */
    public void setTransparency3(float transparency3) {
        this.transparency3 = transparency3;
    }

    /**
     * @return the transparency4
     */
    public float getTransparency4() {
        return transparency4;
    }

    /**
     * @param transparency4 the transparency4 to set
     */
    public void setTransparency4(float transparency4) {
        this.transparency4 = transparency4;
    }

    /**
     * @return the densityType
     */
    public Density getDensityType() {
        return densityType;
    }

    /**
     * @param densityType the densityType to set
     */
    public void setDensityType(Density densityType) {
        this.densityType = densityType;
    }

    /**
     * @return the visualizeWithCubes
     */
    public boolean isVisualizeWithCubes() {
        return visualizeWithCubes;
    }

    /**
     * @param visualizeWithCubes the visualizeWithCubes to set
     */
    public void setVisualizeWithCubes(boolean visualizeWithCubes) {
        this.visualizeWithCubes = visualizeWithCubes;
    }

    /**
     * @return the visualizeWithConvexHull
     */
    public boolean isVisualizeWithConvexHull() {
        return visualizeWithConvexHull;
    }

    /**
     * @param visualizeWithConvexHull the visualizeWithConvexHull to set
     */
    public void setVisualizeWithConvexHull(boolean visualizeWithConvexHull) {
        this.visualizeWithConvexHull = visualizeWithConvexHull;
    }

    /**
     * @return the visualizeWithDividedConvexHull
     */
    public boolean isVisualizeWithDividedConvexHull() {
        return visualizeWithDividedConvexHull;
    }

    /**
     * @param visualizeWithDividedConvexHull the visualizeWithDividedConvexHull
     * to set
     */
    public void setVisualizeWithDividedConvexHull(boolean visualizeWithDividedConvexHull) {
        this.visualizeWithDividedConvexHull = visualizeWithDividedConvexHull;
    }

    /**
     * @return the tolerance
     */
    public double getTolerance() {
        return tolerance;
    }

    /**
     * @param tolerance the tolerance to set
     */
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    /**
     * @return the numberOfVisualizations
     */
    public int getNumberOfVisualizations() {
        return numberOfVisualizations;
    }

    /**
     * @param numberOfVisualizations the numberOfVisualizations to set
     */
    public void setNumberOfVisualizations(int numberOfVisualizations) {
        this.numberOfVisualizations = numberOfVisualizations;
    }
}
