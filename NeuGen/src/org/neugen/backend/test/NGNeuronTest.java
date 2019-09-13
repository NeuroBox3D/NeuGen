package org.neugen.backend.test;

import org.neugen.backend.NGNeuronVisual;
import org.neugen.parsers.SWCReader;

import java.io.File;

public class NGNeuronTest {

    public static void main(String... args) {
        SWCReader swc=new SWCReader();
        File f=new File("/Users/jwang/GCSC/Project/Neuron/HBP733/Programm/NeuGen1907/NeuGen/NeuGen/cellData/CA1/amaral/c8076e.CNC.swc");
        swc.readSWC(f);
        System.out.println(swc.getNeuron().getNumberOfAllDenSegments());
        NGNeuronVisual visual=new NGNeuronVisual(swc.getNeuron());

    }
}
