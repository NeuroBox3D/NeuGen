package org.neugen.vrl;


import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import org.neugen.backend.NGBackend;
import org.neugen.backend.NGNetExport;
import org.neugen.datastructures.Net;

import java.io.File;
import java.io.Serializable;

@ComponentInfo(name="Neuron Net Save", category = "NeuGen", description = "...")
public class VRLNeuGenNetSave implements Serializable {
    private static final long serialVersionUID=1L;

    public void exportNetInfo_asCSV(
            @ParamInfo(name="Neuron Net") Net net,
            @ParamInfo(name="File", style="save-dialog") File file
    ){
        NGNetExport.export_network_info_in_csv(net, file);
    }

    public void exportNet (
            @ParamInfo(name="Neuron Net")Net net,
            @ParamInfo(name="File", style="save-dialog") File file,
            @ParamInfo(name="Export Type",style="selection", options="value=[\"NGX\", \"NeuroML\", \"HOC\", \"sHOC\"]")String stype
    )throws Exception {
        NGNetExport.ExportType type=NGNetExport.ExportType.valueOf(stype.toLowerCase());
        switch(type){
            case NGX:
                NGNetExport.export_network_in_ngx(net, file);
                break;
            case HOC:
                NGNetExport.export_network_in_hoc(net,file);
                break;
            case sHOC:
                NGNetExport.export_network_in_shoc(net,file);
                break;
            case NeuroML:
                NGNetExport.export_network_in_neuroML(net,file);
        }
    }


    public void exportNet_asTXT(
            @ParamInfo(name="Neuron Net")Net net,
            @ParamInfo(name="File", style="save-dialog") File file,
            @ParamInfo(name="With Cell Type")boolean withCellType
    ){
        NGNetExport.export_network_in_txt(net,file, withCellType);
    }

}
