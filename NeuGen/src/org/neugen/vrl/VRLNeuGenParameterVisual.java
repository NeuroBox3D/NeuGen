package org.neugen.vrl;

import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import org.neugen.backend.NGParameter;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.gui.NeuGenConstants;
import org.neugen.gui.XMLTreeView;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.io.Serializable;
import java.util.Map;


@ComponentInfo(name="Parameter Visualise", category = "NeuGen", description = "...")
public class VRLNeuGenParameterVisual implements Serializable {
    private static final long serialVersionUID=1L;

    public JComponent showParameterMap(
            @ParamInfo(name="Parameter Map") Map<String, XMLObject> params
    ){
        XMLTreeView tree = new XMLTreeView(root -> {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        });

        String projectType= NGParameter.getProjectTypefromXMLObject(params.get("Param"));

        XMLObject top = new XMLObject(projectType, null, XMLObject.class.toString());
        top.add(params.get(NeuGenConstants.PARAM));
        top.add(params.get(NeuGenConstants.INTERNA));

        DefaultTreeModel model = new DefaultTreeModel(top);
        tree.setModel(model);

        return new JScrollPane(tree);
    }

    public JComponent showParameterXMLObjects(
            @ParamInfo(name="Param XMLObject") XMLObject paramRoot,
            @ParamInfo(name="Interna XMLObject") XMLObject internaRoot

    ){
        XMLTreeView tree = new XMLTreeView(root -> {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        });

        String projectTypeP= NGParameter.getProjectTypefromXMLObject(paramRoot);
        String projectTypeI= NGParameter.getProjectTypefromXMLObject(internaRoot);

        if(projectTypeI!=projectTypeP){
            System.err.println("The project types are not identifical (Param for "+projectTypeP+", Interna for "+projectTypeI+")");
        }

        XMLObject top = new XMLObject(projectTypeI, null, XMLObject.class.toString());
        top.add(paramRoot);
        top.add(internaRoot);

        DefaultTreeModel model = new DefaultTreeModel(top);
        tree.setModel(model);

        return new JScrollPane(tree);
    }

    public JComponent showXMLObject(
            @ParamInfo(name="XMLObject") XMLObject Root
    ){
        XMLTreeView tree = new XMLTreeView(root -> {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        });

        DefaultTreeModel model = new DefaultTreeModel(Root);
        tree.setModel(model);

        return new JScrollPane(tree);
    }

}
