package org.neugen.datastructures;

import java.io.Serializable;
import javax.vecmath.Point3f;


/**
 * @author Sergei Wolf
 */
public class CellBase implements Cell, Serializable {

    private static final long serialVersionUID = -4623546484283033478L;
    /** The name of the cell. */
    protected String name;
    /** The type of cell. */
    protected String type;
    /** The soma of neuron. */
    protected final Cellipsoid soma;

    public CellBase() {
        soma = new Cellipsoid();
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param instanceName new value of name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    /** Return type name of the neuron. */
    public String getType() {
        return type;
    }

    /**
     * Get the value of soma
     *
     * @return the value of soma
     */
    @Override
    public Cellipsoid getSoma() {
        return soma;
    }

    /**
     * Define a neuron. It defines its axon with its radius and number of compartments,
     * and calls the function setNeuron.
     * @param somaMid new mid point of soma.
     * @param somaRadius new radius of soma.
     */
    @Override
    public void setSoma(Point3f somaMid, float somaRadius) {
        soma.setCellipsoid(somaMid, somaRadius);
    }
}
