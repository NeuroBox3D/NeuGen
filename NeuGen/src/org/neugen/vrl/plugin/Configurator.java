/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neugen.vrl.plugin;

import com.sun.j3d.exp.swing.JCanvas3D;
import eu.mihosoft.vrl.io.IOUtil;
import eu.mihosoft.vrl.system.InitPluginAPI;
import eu.mihosoft.vrl.system.PluginAPI;
import eu.mihosoft.vrl.system.PluginDependency;
import eu.mihosoft.vrl.system.PluginIdentifier;
import eu.mihosoft.vrl.system.VPluginAPI;
import eu.mihosoft.vrl.system.VPluginConfigurator;
import eu.mihosoft.vrl.visual.ActionDelelator;
import eu.mihosoft.vrl.visual.VAction;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neugen.gui.NeuGenApp;
import org.neugen.vrl.JCanvas3DType;

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

        addDependency(new PluginDependency("VRL", "0.4.2", "0.4.x"));
    }
    
    

    @Override
    public void register(PluginAPI api) {
        //
        VPluginAPI vapi = (VPluginAPI) api;
        
        vapi.addTypeRepresentation(JCanvas3DType.class);
        
        vapi.addAction(new VAction("NeuGen-Test") {

            @Override
            public void actionPerformed(ActionEvent e, Object owner) {
                NeuGenApp.main(logPropFile, new String[]{});
            }
        }, ActionDelelator.TOOL_MENU);
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
