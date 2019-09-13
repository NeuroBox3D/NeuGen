package org.neugen.backend;

import org.apache.log4j.Logger;
import org.apache.tools.ant.taskdefs.Java;
import org.neugen.datastructures.xml.XMLNode;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.gui.NeuGenConstants;
import org.neugen.utils.NeuGenLogger;

import java.util.*;

/**
 * resetting parameters by finding the parameter item in paramTree
 *
 * Code based on NGBackend @author stephanmg <stephan@syntaktischer-zucker.de>
 *
 * @author junxi <junxi.wang@gcsc.uni-frankfurt.de>
 */
public class NGParameter {
    /// public static members
    public static final Logger logger = Logger.getLogger(NGParameter.class.getName());

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
    public void modifyParameter(XMLNode root, double param, ArrayList<String> identifier) {
        if(param<0){
            logger.error("Please give a positive value.");
            return ;
        }

        Enumeration childs = root.children();
        while (childs.hasMoreElements()) {

            XMLNode node =(XMLNode) childs.nextElement();
            //System.out.println("node: " + node.getKey());

            if (identifier.get(0).equals(node.getKey())) {
                if (identifier.size() == 1) {
                    //System.out.println();
                    Class valClass=node.getValue().getClass();
                    //System.out.println("Value Class: "+valClass);
                    if(valClass.equals(Integer.class)){
                       // System.out.println("isInteger: "+valClass.equals(Integer.class));
                        node.setValue((int) Math.round(param));
                    }else {
                        node.setValue(param);
                    }
                    System.out.println(node.getKey() + ": is reset to "+node.getValue());
                    break;

                } else {
                    identifier.remove(0);
                    //modifyDoubleParameter(node, param, identifier);
                    modifyParameter(node, param, identifier);
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
        Enumeration childs = child.children();
        XMLNode sibling =(XMLNode) childs.nextElement();
        Enumeration childs_of_sibling = sibling.children();

        while (childs_of_sibling.hasMoreElements()) {
            /// current child of current sibling's child
            XMLNode current_child =(XMLNode) childs_of_sibling.nextElement();

            /// replace node's content
            if(Indices.contains(iterI)) {
                if (identifier.get(0).equals(current_child.getKey())) {
                    if (identifier.size() == 1) {
                        current_child.setValue( replacement);
                        System.out.println("siblings"+iterI+"(" + current_child.getKey() + "): is reset to " + current_child.getValue());
                    } else {
                        ArrayList<String> identifierCopy = new ArrayList<String>(identifier);//(ArrayList) identifier.clone();
                        identifierCopy.remove(0);
                        //modifyDoubleParameter(current_child, replacement, identifierCopy);
                        modifyParameter(current_child, replacement, identifierCopy);
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

        Enumeration childs = root.children();
        while (childs.hasMoreElements()) {

            XMLNode node =(XMLNode) childs.nextElement();

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
        //modifyDoubleParameter(params.get("Param"),density, items);
        modifyParameter(params.get("Param"),density, items);

        ArrayList<Integer> indices=new ArrayList<>();
        for(int i=0;i<10;++i ){
            indices.add(i);
        }

        name="neuron,axon,gen_0,siblings,nparts_density";
        items=new ArrayList<>(Arrays.asList(name.split(",")));
        modifySiblingParameter(params.get("Param"), density, items, indices);

        name="neuron,dendrite,gen_0,nparts_density";
        items=new ArrayList<>(Arrays.asList(name.split(",")));
        //modifyDoubleParameter(params.get("Param"),density, items);
        modifyParameter(params.get("Param"),density, items);

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

        modifyParameter(params.get("Param"),dist_synapse, items);

    }


    /**
     * @brief correct identifer in Region Column for Neocortex to custom value (NeuGen property)
     * @param double value
     * @param identifier
     */
    public void modifyRegion_neocortex(double value, String identifier){
        String name="region,column,"+identifier;
        ArrayList<String> items=new ArrayList<>(Arrays.asList(name.split(",")));

        modifyParameter(params.get("Param"),value, items);
    }
    /**
     * @brief correct identifer in Region Column for Hippocampus to custom value (NeuGen property)
     * @param double value
     * @param identifier
     */
    public void modifyRegion_hippocampus(double value, String identifier){

        String name="region,ca1,"+identifier;
        ArrayList<String> items=new ArrayList<>(Arrays.asList(name.split(",")));

        modifyParameter(params.get("Param"),value, items);
    }


    /**
     * adjust number of neurons in neocortex net
     * @param numberOfCells
     * @param neuron_type: L23pyramidal, L4stellate, L5Apyramidal, L5Bpyramidal, starpyramidal
     */
    public void adjust_number_of_neocortex_neuron(int numberOfCells, String neuron_type) {
        String name="net,"+"n"+neuron_type;
        ArrayList<String> items=new ArrayList<>(Arrays.asList(name.split(",")));

        modifyParameter(params.get("Param"), numberOfCells,items);
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

        modifyParameter(params.get("Param"), numberOfCells,items);
    }

    ///////////////////////////////////////////////////////
    // projectType from params
    //////////////////////////////////////////////////////

    /**
     * find
     * @param root
     * @param identifier
     * @return
     */
    public static boolean findNamefromXMLNode(XMLNode root, ArrayList<String> identifier){
        boolean isfind=false;

        Enumeration childs = root.children();
        while (childs.hasMoreElements()) {

            XMLNode node =(XMLNode) childs.nextElement();

            if (identifier.get(0).equals(node.getKey())) {

                if (identifier.size() == 1) {
                    isfind =true;
                } else {
                    identifier.remove(0);
                    isfind =isfind || findNamefromXMLNode(node, identifier);
                }

            }
        }

        return isfind;
    }

    /**
     * According to the special cells (L4stellate in Neocortex and calbindin in Hippocampus),
     * projectType can be defined by XMLNode/XMLObject.
     * @param param
     * @return
     */

    public static String getProjectTypefromXMLObject(XMLNode param){
        String name="neuron,subclasses,";
        ArrayList<String> items;

        ////for Neocortex:
        String nameN=name+"L4stellate";
        items=new ArrayList<>(Arrays.asList(nameN.split(",")));
        if(findNamefromXMLNode(param, items)){
            return NeuGenConstants.NEOCORTEX_PROJECT;
        }

        ///for Hippocampus
        String nameH=name+"calbindin";
        items=new ArrayList<>(Arrays.asList(name.split(",")));
        if(findNamefromXMLNode(param, items)){
            return NeuGenConstants.HIPPOCAMPUS_PROJECT;
        }

        return null;
    }

    /**
     * Judge if an XMLObject is a Param by finding if "region" exists.
     * Since "region" exists only in Param
     *
     * @param param
     * @return
     */
    public static boolean isParamXMLObject(XMLNode param){
        boolean isParam=false;
        String name="region";
        ArrayList<String> items=new ArrayList<>(Arrays.asList(name.split(",")));
        isParam=findNamefromXMLNode(param, items);

        return isParam;
    }

    /**
     * Judge if an XMLObject is an Interna by finding if "net>seed" exists.
     * Since "net>seed" exists only in Interna
     *
     * @param param
     * @return
     */
    public static boolean isInternaXMLObject(XMLNode param){
        boolean isInterna=false;
        String name="net,seed";
        ArrayList<String> items=new ArrayList<>(Arrays.asList(name.split(",")));
        isInterna=findNamefromXMLNode(param, items);

        return isInterna;
    }

}
