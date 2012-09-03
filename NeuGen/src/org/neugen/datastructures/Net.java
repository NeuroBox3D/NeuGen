package org.neugen.datastructures;

import org.neugen.datastructures.neuron.Neuron;
import java.util.List;
import java.util.Map;

/**
 * @author Sergei Wolf
 */
public interface Net {

    public void interconnect();

    public void generate();

    public Map<String, Float> computeAPSN();

    public int getTypeOfNeuron(int indexOfNeuron);

    public WriteToHoc getHocData();

    public int createNonFunSynapses();

    public int getNumSynapse();

    public int getNumNonFunSynapses();

    public long getNumOfSynapses(int presynapticType, int postSynapticType);

    public List<String> getTypeCellNames();

    public List<Neuron> getNeuronList();

    public int[] getCellOffsets();

    public void destroy();

    public int getTotalNumOfAxonalSegments();

    public int getTotalNumOfDenSegments();

    public int getTotalNumOfSomataSegments();

    public List<Cons> getSynapseList();

    public int getNumNeurons();

    public void setTotalNumOfSegments();

    public int getTotalNumOfSegments();

    public Region getRegion();
}
