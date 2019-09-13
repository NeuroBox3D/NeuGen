package org.neugen.vrl;

import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.OutputInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import org.neugen.backend.NGNeuronLoad;
import org.neugen.gui.NeuGenConstants;
import org.neugen.parsers.SWCReader;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.utils.NeuGenLogger;

import java.io.File;


@ComponentInfo(name="Neuron Read", category = "NeuGen/Neuron", description = "...")
public class VRLNeuGenNeuronRead {

    @OutputInfo(name="Neuron")
    public Neuron loadNeuron(
        @ParamInfo(name="Project SWC File", style = "load-dialog", options="") File file
    ){
        NGNeuronLoad neuLoad=new NGNeuronLoad(file);

        return neuLoad.load_neuron_from_swc();
    }


}
