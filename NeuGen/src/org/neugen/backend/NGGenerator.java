package org.neugen.backend;

import org.apache.log4j.Logger;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.Region;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.gui.NeuGenConstants;
import org.neugen.gui.NeuGenLib;
import org.neugen.utils.NeuGenLogger;

import java.util.Map;

/**
 * Generating a neuronal network with paramter tree (Map<String, XMLObject>) including Param and Interna
 *
 * Code from NGBackend @author stephanmg <stephan@syntaktischer-zucker.de>
 *
 * @author junxi <junxi.wang@gcsc.uni-frankfurt.de>
 */
public final class NGGenerator {
    /// public static members
    public static final Logger logger = Logger.getLogger(NGGenerator.class.getName());

    private Map<String, XMLObject> params;
    private String projectType;
    private static final NeuGenLib ngLib = new NeuGenLib();
    private Net net;

    public NGGenerator(Map<String, XMLObject> params){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.params=params;
        this.projectType=NGParameter.getProjectTypefromXMLObject(params.get(NeuGenConstants.INTERNA));
    }

    public NGGenerator(Map<String, XMLObject> params, String projectType){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.params=params;
        this.projectType=projectType;
    }

    public Map<String, XMLObject> getParamTree(){
        return params;
    }

    public Net getNet(){return net;}

    /////////////////////////////////////////////////

    private static void initRegion(String projectType){
        switch (projectType) {
            case NeuGenConstants.NEOCORTEX_PROJECT:
                Region.setCortColumn(true);
                Region.setCa1Region(false);
                break;
            case NeuGenConstants.HIPPOCAMPUS_PROJECT:
                Region.setCortColumn(false);
                Region.setCa1Region(true);
                break;
            default:
                Region.setCortColumn(false);
                Region.setCa1Region(false);
                logger.fatal("Wrong project type specified aborting: "
                        + projectType + ". Supported project types are "
                        + NeuGenConstants.NEOCORTEX_PROJECT + " and "
                        + NeuGenConstants.HIPPOCAMPUS_PROJECT + ".");
                break;
        }
    }

    public void run(){
        initRegion(projectType);
        NeuGenLib.initParamData(params);
        ngLib.run(projectType);
        this.net=ngLib.getNet();
    }

}
