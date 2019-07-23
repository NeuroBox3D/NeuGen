package org.neugen.backend;

import org.apache.log4j.Logger;
import org.neugen.datastructures.Net;
import org.neugen.gui.NeuGenConstants;
import org.neugen.parsers.*;
import org.neugen.parsers.NGX.NGXWriter;
import org.neugen.parsers.TXT.TXTWriter;
import org.neugen.utils.NeuGenLogger;

import java.io.File;
import java.util.Arrays;


/**
 * export neuronal network into different file format: TXT, ngx, hoc, shoc
 *
 * in csv-file:  network information is summaried. for example: length of neuron, dendrites
 *
 */
public final class NGNetExport {

    public static final Logger logger = Logger.getLogger(NGProject.class.getName());

    private Net net;
    private File file;

    public enum ExportType{
        TXT("txt"), NGX("ngx"), CSV("csv"), HOC("hoc"), sHOC("shoc"), NeuroML("neuroml");

        private final String value;

        ExportType(String value) {
            this.value = value;
        }

        public String getText() {
            return this.value;
        }

        /**
         * @return the Enum representation for the given string.
         * @throws IllegalArgumentException if unknown string.
         */
        public static ExportType fromString(String s) throws IllegalArgumentException {
            return Arrays.stream(ExportType.values())
                    .filter(v -> v.value.equals(s))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("unknown value: " + s));
        }
    }

    public NGNetExport(Net net){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.net=net;
        this.file=null;
    }

    public NGNetExport(Net net, File file){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.net=net;
        this.file=file;
    }

    public NGNetExport(Net net, String filePath){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.net=net;
        this.file=new File(filePath);
    }

    public void setFile(File file){this.file=file;}

    public void setFilePath(String filePath){this.file=new File(filePath);}

    public File getFile(){return file;}

    public Net getNet(){return net;}

    ///////////////////////////////////////////////////////////////////
    /// basic functions: static saving function
    ///////////////////////////////////////////////////////////
    public static void export_parallel_network(Net net, File file, int nProcs){
        if(file==null){
            System.err.println("Please import a file");
            return;
        }
        logger.info("Exporting parallel HOC data to " + file);
        ParHocWriter pHocWriter=new ParHocWriter(net, file, nProcs);
        pHocWriter.exportNet();
    }

    public static void export_network_in_ngx(Net net, File file){
        if(file==null){
            System.err.println("Please import a file");
            return;
        }

        logger.info("Exporting NGX data to... " + file.getAbsolutePath());
        NGXWriter ngxWriter=new NGXWriter(net, file);
        ngxWriter.exportNetToNGX();
    }

    public static void export_network_in_txt(Net net, File file, boolean withCellType){
        if(file==null){
            System.err.println("Please import a file");
            return;
        }

        logger.info("Exporting TXT data to... " + file.getAbsolutePath());
        TXTWriter txtWriter = new TXTWriter(net, file);
        txtWriter.setCompressed(false); //?
        txtWriter.setUncompressed(true); //?
        txtWriter.setWithCellType(withCellType); //? why?
        txtWriter.exportNetToTXT();
    }

    public static void export_network_info_in_csv(Net net, File file){
        if(file==null){
            System.err.println("Please import a file");
            return;
        }

        logger.info("Exporting CSV data to... " + file.getAbsolutePath());
        CSVWriter csvWriter=new CSVWriter(net,file);
        csvWriter.writeNeuronInfo();
    }

    public static void export_network_in_shoc(Net net, File file){
        if(file==null){
            System.err.println("Please import a file");
            return;
        }

        logger.info("Exporting sHOC data to... " + file.getAbsolutePath());
        SimpleHocWriter shocWriter=new SimpleHocWriter();
        shocWriter.writeNet(net, file);
    }

    public static void export_network_in_hoc(Net net, File file){
        if(file==null){
            System.err.println("Please import a file");
            return;
        }

        logger.info("Exporting sHOC data to... " + file.getAbsolutePath());
        HocWriter hocWriter=new HocWriter(net, file);
        hocWriter.exportNet();
    }

    public static void export_network_in_neuroML(Net net, File file) throws Exception{
        if(file==null){
            System.err.println("Please import a file");
            return;
        }

        logger.info("Exporting sHOC data to... " + file.getAbsolutePath());
        NeuroMLWriter write = new NeuroMLWriter(); // see NeuronMLWriterTask.java
        write.exportData(file, net);
    }


    /////////////////////////////////////////////////////////////////////
    //// saving functions for this class
    ////////////////////////////////////////////////////////////////////
    public void export_parallel_network(int nProcs){
        export_parallel_network(net, file, nProcs);
    }

    public void export_network_in_ngx() {
        export_network_in_ngx(net, file);
    }

    public void export_network_in_txt(boolean withCellType){
        export_network_in_txt(net, file, withCellType);
    }

    public void export_network_info_in_csv(){
        export_network_info_in_csv(net, file);
    }

    public void export_network_in_shoc(){
        export_network_in_shoc(net,file);
    }

    public void export_network_in_hoc(){
        export_network_in_hoc(net,file);
    }

    public void export_network_in_neuroML()throws Exception{
        export_network_in_neuroML(net,file);
    }


}
