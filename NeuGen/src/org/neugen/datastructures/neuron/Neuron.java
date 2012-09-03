package org.neugen.datastructures.neuron;

import java.util.List;
import org.neugen.datastructures.Axon;
import org.neugen.datastructures.Cell;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.Section;
import org.neugen.datastructures.parameter.NeuronParam;

/**
 *
 * @author Sergei Wolf
 */
public interface Neuron extends Cell {

    public Axon getAxon();

    public NeuronParam getParam();

    public void infoNeuron();

    public int getIndex();

    public List<Dendrite> getDendrites();

    public int getNumberOfAllDenSegments();

    public void setIndex(int index);

    public void setNeuron();
    
    public List<Section> getUndefinedSections();

    public boolean collide();

    public void setCollide(boolean collide);

}
