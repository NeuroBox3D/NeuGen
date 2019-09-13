package org.neugen.vrl;

import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.OutputInfo;
import eu.mihosoft.vrl.annotation.ParamGroupInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import org.neugen.backend.NGParameter;
import org.neugen.datastructures.xml.XMLObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@ComponentInfo(name="Parameter Change", category = "NeuGen/Parameter", description = "...")
public class VRLNeuGenParameterChange {
    private transient NGParameter paramChange;

    @OutputInfo(name="Parameter Map")
    public Map<String, XMLObject> ChangeParameter(
            @ParamInfo(name="Parameter Map") Map<String, XMLObject> params,
            @ParamInfo(name="XMLObject Name", style="selection", options="value=[\"Param\", \"Interna\"]")String xmlnode,
            @ParamInfo(name="Item Path", style = "array") String[] name,
            @ParamInfo(name="Number", options="value=1.0")Double number
    ){
        paramChange=new NGParameter(params);
        ArrayList<String> items=new ArrayList<>();
        for(int i=0; i<name.length; i++){
            items.add(name[i]);
        }

        //ArrayList<String> items=new ArrayList<>(Arrays.asList(name.split(">")));

        paramChange.modifyParameter(params.get(xmlnode), number.doubleValue(),items);

        return paramChange.getParamTree();
    }

    public Map<String, XMLObject> ChangeSiblingParameter(
            @ParamInfo(name="Parameter Map") Map<String, XMLObject> params,
            @ParamInfo(name="XMLObject Name", style="selection", options="value=[\"Param\", \"Interna\"]")String xmlnode,
            @ParamInfo(name="Item Path", style = "array") String[] name,
            //@ParamInfo(name="Item Path (split mit >)", options="value=net>dist_synapse") String name,
            @ParamInfo(name="Number", options="value=1.0")Double number,
            @ParamInfo(name="Sibling number",style = "array") Integer[] index

    ){
        paramChange=new NGParameter(params);
        ArrayList<Integer> indices=new ArrayList<>(Arrays.asList(index));

        //ArrayList<String> items=new ArrayList<>(Arrays.asList(name.split(">")));
        ArrayList<String> items=new ArrayList<>();
        for(int i=0; i<name.length; i++){
            items.add(name[i]);
        }

        paramChange.modifySiblingParameter(params.get("Param"), number, items, indices);

        return paramChange.getParamTree();
    }




}
