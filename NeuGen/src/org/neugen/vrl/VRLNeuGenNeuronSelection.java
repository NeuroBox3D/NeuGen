package org.neugen.vrl;


import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.OutputInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import eu.mihosoft.vrl.v3d.VGeometry3D;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.neuron.Neuron;

import java.io.Serializable;
import java.util.List;

@ComponentInfo(name="Neuron Selection", category = "NeuGen", description = "...")
public class VRLNeuGenNeuronSelection implements Serializable {
    private static final long serialVersionUID=1L;

    @OutputInfo(name="Neuron")
    public Neuron NeuronSelection(
            @ParamInfo(name="Neuron Net") Net net,
            @ParamInfo(name="Neuron Index", options="value=0")int index
    ){
        List<Neuron> neuronList=net.getNeuronList();
        if(index < neuronList.size())
            return neuronList.get(index);

        System.err.println("Please give a index less than "+ neuronList.size());
        return null;
    }

}
