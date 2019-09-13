/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2012-2015 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
 * 
 * This file is part of NeuGen.
 *
 * NeuGen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/NeuGen/LICENSE
 *
 * NeuGen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of NeuGen includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of NeuGen. The copyright statement/attribution may not be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do the following regarding copyright
 * notice and author attribution.
 *
 * Add an additional notice, stating that you modified NeuGen. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "NeuGen source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * S. Wolf, S. Grein, G. Queisser. NeuGen 2.0 -
 * Employing NeuGen 2.0 to automatically generate realistic
 * morphologies of hippocapal neurons and neural networks in 3D.
 * Neuroinformatics, 2013, 11(2), pp. 137-148, doi: 10.1007/s12021-012-9170-1
 *
 *
 * J. P. Eberhard, A. Wanner, G. Wittum. NeuGen -
 * A tool for the generation of realistic morphology 
 * of cortical neurons and neural networks in 3D.
 * Neurocomputing, 70(1-3), pp. 327-343, doi: 10.1016/j.neucom.2006.01.028
 *
 */

package org.neugen.vrl.plugin;


import eu.mihosoft.vrl.io.IOUtil;
import eu.mihosoft.vrl.system.InitPluginAPI;
import eu.mihosoft.vrl.system.PluginAPI;
import eu.mihosoft.vrl.system.PluginDependency;
import eu.mihosoft.vrl.system.PluginIdentifier;
import eu.mihosoft.vrl.system.VPluginAPI;
import eu.mihosoft.vrl.system.VPluginConfigurator;
import eu.mihosoft.vrl.visual.ActionDelegator;
import eu.mihosoft.vrl.visual.VAction;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neugen.gui.NeuGenApp;
import org.neugen.vrl.*;

/**
 * 
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Configurator extends VPluginConfigurator{
    
    private File logPropFile;

    public Configurator() {
        setIdentifier(new PluginIdentifier("NeuGen", "0.1"));
        setDescription("NeuGen Plugin");
        exportPackage("org.neugen");

        // add dependencies with the other VRL-Plugins so that this project can run based on the other plugins.
        //and please store the related libraries (.jar) that are added in properties during building (see build.xml)
        addDependency(new PluginDependency("VRL", "0.4.x", "0.4.x"));
        addDependency(new PluginDependency("JCSG", "0.x", "0.x"));
    }
    
    

    @Override
    public void register(PluginAPI api) {
        //
        VPluginAPI vapi = (VPluginAPI) api;

        vapi.addAction(new VAction("NeuGen-Test") {

            @Override
            public void actionPerformed(ActionEvent e, Object owner) {
                NeuGenApp.main(logPropFile, new String[]{});
            }
        }, ActionDelegator.TOOL_MENU);


        //vapi.addTypeRepresentation(VCanvas3DType.class);
        //vapi.addComponent(DensityVisualizationParamsInput.class);

        vapi.addComponent(VRLNeuGenProjectCreate.class);
        vapi.addComponent(VRLNeuGenParameterLoad.class);
        vapi.addComponent(VRLNeuGenParameterChange.class);
        vapi.addComponent(VRLNeuGenParameterVisual.class);
        vapi.addComponent(VRLNeuGenParameterSave.class);
        vapi.addComponent(VRLNeuGenGenerator.class);

        vapi.addComponent(VRLNeuGenNeuronSelection.class);
        vapi.addComponent(VRLNeuGenNeuronVisual.class);
        vapi.addComponent(VRLNeuGenNeuronRead.class);

        vapi.addComponent(VRLNeuGenNetVisual.class);
        vapi.addComponent(VRLNeuGenVisualColor.class);
        vapi.addComponent(VRLNeuGenNetSave.class);

    }

    @Override
    public void init(InitPluginAPI iApi) {
        logPropFile = 
                new File(iApi.getResourceFolder(), "log4j-3.properties");
    }
    
    @Override
    public void install(InitPluginAPI iApi) {
        logPropFile = 
                new File(iApi.getResourceFolder(), "log4j-3.properties");
        try {
            IOUtil.saveStreamToFile(
                    Configurator.class.getResourceAsStream(
                    "log4j-3.properties"), logPropFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configurator.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Configurator.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
    
}
