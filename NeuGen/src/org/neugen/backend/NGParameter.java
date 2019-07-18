package org.neugen.backend;

import org.apache.log4j.Logger;
import org.neugen.datastructures.xml.XMLNode;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.gui.NeuGenConstants;
import org.neugen.utils.NeuGenLogger;

import java.util.*;

/**
 * resetting parameters by finding the parameter item in paramTree
 */
public class NGParameter {
    /// public static members
    public static final Logger logger = Logger.getLogger(NGProject.class.getName());

    ////////
    private Map<String, XMLObject> params;

    public NGParameter(Map<String, XMLObject> params){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.params=params;
    }

    public Map<String, XMLObject> getParamTree(){
        return params;
    }

    ///////////////////////////////////////////////////////////////////////
    ///// basic functions for resetting a double/ integer parameter or parameters in one of silbings
    /////////////////////////////////////////////////////////////////////

    /**
     * @brief modifies a double parameter  (NeuGen parameter)
     * @param root
     * @param param
     * @param identifier
     */
    @SuppressWarnings("unchecked")
    public void modifyDoubleParameter(XMLNode root, double param, ArrayList<String> identifier) {
        Enumeration<XMLNode> childs = root.children();
        while (childs.hasMoreElements()) {

            XMLNode node = childs.nextElement();
            //System.out.println("node: " + node.getKey());

            if (identifier.get(0).equals(node.getKey())) {
                if (identifier.size() == 1) {
                    System.out.println();
                    node.setValue(param);
                    System.out.println(node.getKey() + ": is reset to "+node.getValue());
                    break;

                } else {
                    identifier.remove(0);
                    modifyDoubleParameter(node, param, identifier);
                }
            }
        }
    }


    /**
     * @brief modifies an integer parameter: for example: cell number
     * @param root
     * @param param
     * @param identifier
     */
    @SuppressWarnings("unchecked")
    public void modifyIntegerParameter(XMLNode root, int param, ArrayList<String> identifier) {
        Enumeration<XMLNode> childs = root.children();
        while (childs.hasMoreElements()) {

            XMLNode node = childs.nextElement();
            //System.out.println("node: " + node.getKey());

            if (identifier.get(0).equals(node.getKey())) {
                if (identifier.size() == 1) {
                    System.out.println();
                    node.setValue(param);
                    System.out.println(node.getKey() + ": is reset to "+node.getValue());
                    break;
                } else {
                    identifier.remove(0);
                    modifyIntegerParameter(node, param, identifier);
                }
            }
        }
    }

    /**
     * adjust the parameters in sibling (XMLNode)
     * @param child
     * @param identifier
     * @param replacement
     * @param Indices
     * @param iterI
     */
    private void adjustParameter_siblings(XMLNode child, ArrayList<String> identifier, double replacement, ArrayList<Integer> Indices, int iterI){
        if(identifier==null)
            return;

        if(iterI> Collections.max(Indices))
            return;

        System.out.println(identifier);
        Enumeration<XMLNode> childs = child.children();
        XMLNode sibling = childs.nextElement();
        Enumeration<XMLNode> childs_of_sibling = sibling.children();

        while (childs_of_sibling.hasMoreElements()) {
            /// current child of current sibling's child
            XMLNode current_child = childs_of_sibling.nextElement();

            /// replace node's content
            if(Indices.contains(iterI)) {
                if (identifier.get(0).equals(current_child.getKey())) {
                    if (identifier.size() == 1) {
                        current_child.setValue(replacement);
                        System.out.println("siblings"+iterI+"(" + current_child.getKey() + "): is reset to " + current_child.getValue());
                    } else {
                        ArrayList<String> identifierCopy = (ArrayList) identifier.clone();
                        identifierCopy.remove(0);
                        modifyDoubleParameter(current_child, replacement, identifierCopy);
                    }
                }

            }
            /// more siblings?
            if ("siblings".equals(current_child.getKey())) {
                //System.out.println("siblings:"+identifier);
                adjustParameter_siblings(current_child, identifier, replacement, Indices, ++iterI);
            }
        }
    }

    /**
     * find XMLNode of the first "siblings" and adjust the paramters in siblings
     * @param root
     * @param identifier
     * @param value
     * @param indices
     */
    @SuppressWarnings("unchecked")
    public void modifySiblingParameter(XMLNode root, double value, ArrayList<String> identifier, ArrayList<Integer> indices) {
        if(!identifier.contains("siblings")){
            logger.error("This parameter is not in siblings.");
            return;
        }

        Enumeration<XMLNode> childs = root.children();
        while (childs.hasMoreElements()) {

            XMLNode node = childs.nextElement();
            //System.out.println("node: " + node.getKey());

            if (identifier.get(0).equals(node.getKey())) {
                identifier.remove(0);
                 if("siblings".equals(node.getKey())){
                     adjustParameter_siblings(node, identifier, value, indices, 0);
                 }else {
                     modifySiblingParameter(node, value, identifier, indices);
                 }
            }
        }

    }

    /**
     * @brief modifies n parts density (NeuGen property)
     * @param density
     */
    @SuppressWarnings("unchecked")
    public void modifyNPartsDensity(double density) {

        String name="neuron,axon,gen_0,nparts_density";
        ArrayList<String> items=new ArrayList<>(Arrays.asList(name.split(",")));
        modifyDoubleParameter(params.get("Param"),density, items);

        ArrayList<Integer> indices=new ArrayList<>();
        for(int i=0;i<10;++i ){
            indices.add(i);
        }

        name="neuron,axon,gen_0,siblings,nparts_density";
        items=new ArrayList<>(Arrays.asList(name.split(",")));
        modifySiblingParameter(params.get("Param"), density, items, indices);

        name="neuron,dendrite,gen_0,nparts_density";
        items=new ArrayList<>(Arrays.asList(name.split(",")));
        modifyDoubleParameter(params.get("Param"),density, items);

        name="neuron,dendrite,gen_0,siblings,nparts_density";
        items=new ArrayList<>(Arrays.asList(name.split(",")));
        modifySiblingParameter(params.get("Param"), density, items, indices);

    }


    ///////////////////////////////////////////////////////////////////////
    ///// parameters interested by users
    ///////////////////////////////////////////////////////////////////////

    /**
     * @brief correct synapse dist to custom value (NeuGen property)
     * @param dist_synapse
     */
    public void modifySynapseDistance(double dist_synapse) {
        String name="net,dist_synapse";
        ArrayList<String> items=new ArrayList<>(Arrays.asList(name.split(",")));

        modifyDoubleParameter(params.get("Param"),dist_synapse, items);

    }


    /**
     * @brief correct identifer in Region Column for Neocortex to custom value (NeuGen property)
     * @param double value
     * @param identifier
     */
    public void modifyRegionColumn(double value, String identifier, String projectType){
        String name="region,column,"+identifier;
        ArrayList<String> items=new ArrayList<>(Arrays.asList(name.split(",")));

        modifyDoubleParameter(params.get("Param"),value, items);
    }
    /**
     * @brief correct identifer in Region Column for Hippocampus to custom value (NeuGen property)
     * @param double value
     * @param identifier
     */
    public void modifyRegionCA1(double value, String identifier){

        String name="region,ca1,"+identifier;
        ArrayList<String> items=new ArrayList<>(Arrays.asList(name.split(",")));

        modifyDoubleParameter(params.get("Param"),value, items);
    }


    /**
     * adjust number of neurons in neocortex net
     * @param numberOfCells
     * @param neuron_type: L23pyramidal, L4stellate, L5Apyramidal, L5Bpyramidal, starpyramidal
     */
    public void adjust_number_of_neocortex_neuron(int numberOfCells, String neuron_type) {
        String name="net,"+"n"+neuron_type;
        ArrayList<String> items=new ArrayList<>(Arrays.asList(name.split(",")));

        modifyIntegerParameter(params.get("Param"), numberOfCells,items);
    }

    /**
     * adjust number of neurons in neocortex net
     * @param numberOfCells
     * @param neuron_type: CA1pyramidal, Calretinin(CR), Calbindin(CB), Cholecystokinin(CCK), Parvalbumin(PV), Somatostatin(SOM)
     * @param location_name: oriens, pyramidale, proximal_radiatum, distal_radiatum, lacunosum/moleculare
     */
    public void adjust_number_of_hippocampus_neuron(int numberOfCells, String neuron_type, String location_name){
        String name="net,"+"n"+neuron_type+","+location_name;
        ArrayList<String> items=new ArrayList<>(Arrays.asList(name.split(",")));

        modifyIntegerParameter(params.get("Param"), numberOfCells,items);
    }



}
