package org.neugen.backend;

import org.neugen.datastructures.Net;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.gui.NeuGenConstants;
import org.neugen.parsers.MorphMLReader;
import org.neugen.parsers.SWCReader;
import org.neugen.parsers.SimpleHocReader;
import org.neugen.utils.NeuGenLogger;

import java.io.File;
import java.util.Arrays;

public class NGNeuronLoad {

    //private Net net;//shoc,morphML
    //private Neuron neuron;//swc
    private File file;


    public enum ImportType{
        SWC("txt"), MorphML("morphML"), sHOC("shoc"); //shoc: don't support

        private final String value;

        ImportType(String value) {
            this.value = value;
        }

        public String getText() {
            return this.value;
        }

        /**
         * @return the Enum representation for the given string.
         * @throws IllegalArgumentException if unknown string.
         */
        public static NGNeuronLoad.ImportType fromString(String s) throws IllegalArgumentException {
            return Arrays.stream(NGNeuronLoad.ImportType.values())
                    .filter(v -> v.value.equals(s))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("unknown value: " + s));
        }
    }
    public NGNeuronLoad(File file){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.file=file;
    }

    ///////////////////////////////////////////////////////////////////
    /// basic functions: static loading function
    ///////////////////////////////////////////////////////////
    public static Neuron load_neuron_from_swc(File file){
        SWCReader swc=new SWCReader();
        //System.out.println(file);
        swc.readSWC(file);

        return swc.getNeuron();
    }

    public static Net load_net_from_morphML(File file){
        MorphMLReader morphML=new MorphMLReader();
        morphML.runMorphMLReader(file.getAbsolutePath());

        return morphML.getNet();
    }

    /*public static Net load_net_from_shoc(File file){
        SimpleHocReader shoc=new SimpleHocReader();
        shoc.loadFromSHOCFile(file);

        return shoc.getNet();
    }*/


    ///////////////////////////////////////////////////////////////////
    ///  saving function
    ///////////////////////////////////////////////////////////
    public Neuron load_neuron_from_swc(){return load_neuron_from_swc(file);}

    public Net load_net_from_morphML(){ return load_net_from_morphML(file);}

    //public Net load_net_from_shoc(){return load_net_from_shoc(file);}


}
