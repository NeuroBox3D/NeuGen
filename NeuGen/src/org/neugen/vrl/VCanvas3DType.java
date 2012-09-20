/*
 * Shape3DType.java
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2009 Michael Hoffer <info@michaelhoffer.de>
 *
 * Supported by the Goethe Center for Scientific Computing of Prof. Wittum
 * (http://gcsc.uni-frankfurt.de)
 *
 * This file is part of Visual Reflection Library (VRL).
 *
 * VRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3
 * as published by the Free Software Foundation.
 *
 * VRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obligated to do so.  If you do not wish to do so, delete this
 * exception statement from your version.
 */
package org.neugen.vrl;

import eu.mihosoft.vrl.annotation.TypeInfo;
import eu.mihosoft.vrl.reflection.CustomParamData;
import eu.mihosoft.vrl.reflection.RepresentationType;
import eu.mihosoft.vrl.reflection.TypeRepresentationBase;
import eu.mihosoft.vrl.types.UniverseCreator;
import eu.mihosoft.vrl.types.VCanvas3D;
import eu.mihosoft.vrl.types.VOffscreenCanvas3D;
import eu.mihosoft.vrl.types.VUniverseCreator;
import eu.mihosoft.vrl.visual.VBoxLayout;
import eu.mihosoft.vrl.visual.VContainer;
import eu.mihosoft.vrl.visual.VGraphicsUtil;
import eu.mihosoft.vrl.visual.VSwingUtil;
import groovy.lang.Script;
import java.awt.Color;
import java.awt.Dimension;
import javax.media.j3d.*;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * TypeRepresentation for <code>javax.media.j3d.Shape3D</code>. The easiest
 * way to create 3D shapes is to use {@link eu.mihosoft.vrl.v3d.VTriangleArray}
 * or {@link eu.mihosoft.vrl.v3d.TxT2Geometry}. For simple scenarios, Instead
 * of directly using Java3D objects (Shape3D) it is suggested to use
 * {@link eu.mihosoft.vrl.v3d.VGeometry3D} objects and the corresponding type
 * representation.
 * 
 * <p>Sample:</p>
 * <br/>
 * <img src="doc-files/shape3d-default-01.png"/>
 * <br/>
 *
 * @see eu.mihosoft.vrl.v3d.VTriangleArray
 * @see eu.mihosoft.vrl.types.VGeometry3DType
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
@TypeInfo(type=void.class, input = false, output = true, style="vcanvas3d")
public final class VCanvas3DType extends TypeRepresentationBase {

    private static final long serialVersionUID = 4412789368035724900L;
    private VCanvas3D canvas;
    private VContainer container;
    public static final String ORIENTATION_KEY = "orientation";
    private Dimension previousVCanvas3DSize;
    protected Dimension minimumVCanvas3DSize;

    /**
     * Constructor.
     */
    public VCanvas3DType() {
        init();

        if (!VGraphicsUtil.NO_3D) {

            // TODO causes VRL freeze if used in Shape3DArrayOutputType
            init3DView(new VCanvas3D(this));

        } else {
            add(new JLabel("Java3D support disabled!"));
        }
    }

    /**
     * Constructor.
     * @param canvas the 3D canvas
     * @param universeCreator the universe creator
     */
    public VCanvas3DType(VCanvas3D canvas) {
        init();
        init3DView(canvas);
    }

    /**
     * Initializes this type representation.
     */
    protected void init() {
        setUpdateLayoutOnValueChange(false);
        VBoxLayout layout = new VBoxLayout(this, VBoxLayout.X_AXIS);
        setLayout(layout);

        nameLabel.setText("");
        nameLabel.setAlignmentY(0.5f);
        this.add(nameLabel);
    }

    /**
     * Initializes the 3D view of this type representation.
     * @param canvas the 3D canvas
     * @param universeCreator the universe creator
     */
    protected void init3DView(VCanvas3D canvas) {
        dispose3D(); // very important to prevent memory leaks of derived classes!

        if (container != null) {
            this.remove(container);
        }

        this.canvas = canvas;
        
        getCanvas().setBackground(VSwingUtil.TRANSPARENT_COLOR);

        setValueOptions("width=160;height=120;blurValue=0.7F;"
                + "renderOptimization=false;realtimeOptimization=false");

        minimumVCanvas3DSize = new Dimension(160, 120);

        container = new VContainer();

        container.add(canvas);

        this.add(container);

        this.setInputComponent(container);

        setHideConnector(true);
    }

    /**
     * Returns a wired java 3d appearance.
     * @param color the color of the appearance
     * @returnwired a java 3d appearance
     */
    private Appearance getLinedAppearance(Color color) {
        Appearance a = new Appearance();
        PolygonAttributes pa = new PolygonAttributes();
        pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        pa.setBackFaceNormalFlip(true);
        a.setPolygonAttributes(pa);

        LineAttributes la = new LineAttributes();
        la.setLineAntialiasingEnable(true);
        la.setLineWidth(1.5f);

        a.setLineAttributes(la);

        Material mat = new Material();

        float r = color.getRed() / 255.f;
        float g = color.getGreen() / 255.f;
        float b = color.getBlue() / 255.f;

        mat.setDiffuseColor(r, g, b);
//        mat.setDiffuseColor(1.f, 0.4f, 0.1f);
        mat.setSpecularColor(1.f, 1.f, 1.f);
        mat.setShininess(20.f);

        mat.setLightingEnable(true);
        a.setMaterial(mat);

        return a;
    }

    /**
     * Defines the Vcanvas3D size by evaluating a groovy script.
     * @param script the script to evaluate
     */
    private void setVCanvas3DSizeFromValueOptions(Script script) {
        Integer w = null;
        Integer h = null;
        Object property = null;

        if (getValueOptions() != null) {

            if (getValueOptions().contains("width")) {
                property = script.getProperty("width");
            }

            if (property != null) {
                w = (Integer) property;
            }

            property = null;

            if (getValueOptions().contains("height")) {
                property = script.getProperty("height");
            }

            if (property != null) {
                h = (Integer) property;
            }
        }

        if (w != null && h != null) {
            // TODO find out why offset is 10
            getCanvas().setPreferredSize(new Dimension(w - 10, h));
            getCanvas().setSize(new Dimension(w - 10, h));
        }

        System.out.println(getValueOptions());
    }

    /**
     * Defines render options by evaluating a groovy script.
     * @param script the script to evaluate
     */
    private void setRenderOptionsFromValueOptions(Script script) {

        Object property = null;
        Boolean enableRenderOptimization = true;
        Boolean enableRealtimeOptimization = false;
        Float blurValue = 0.7f;

        if (getValueOptions() != null) {

            if (getValueOptions().contains("renderOptimization")) {
                property = script.getProperty("renderOptimization");
            }

            if (property != null) {
                enableRenderOptimization = (Boolean) property;
                canvas.setRenderOptimizationEnabled(
                        enableRenderOptimization);
            }

            property = null;

            if (getValueOptions().contains("realtimeOptimization")) {
                property = script.getProperty("realtimeOptimization");
            }

            if (property != null) {
                enableRealtimeOptimization = (Boolean) property;
                canvas.setRealTimeRenderOptimization(
                        enableRealtimeOptimization);
            }

            property = null;

            if (getValueOptions().contains("blurValue")) {
                property = script.getProperty("blurValue");
            }

            if (property != null) {
                blurValue = (Float) property;
                canvas.setBlurValue(blurValue);
            }
        }
    }

    @Override
    protected void evaluationRequest(Script script) {
        setVCanvas3DSizeFromValueOptions(script);
        setRenderOptionsFromValueOptions(script);
    }

    @Override
    public CustomParamData getCustomData() {

        if (VGraphicsUtil.NO_3D) {
            return new CustomParamData();
        }

        CustomParamData result = super.getCustomData();

        Transform3D t3d = new Transform3D();
//        getUniverseCreator().getRootGroup().getTransform(t3d);
        double[] values = new double[16];
        t3d.get(values);

        result.put(ORIENTATION_KEY, values);

        return result;
    }

    @Override
    public void evaluateCustomParamData() {
        Transform3D t3d = new Transform3D();
        double[] values = (double[]) super.getCustomData().get(ORIENTATION_KEY);
        if (values != null) {
            t3d.set(values);
//            getUniverseCreator().getRootGroup().setTransform(t3d);
        }
    }

    /**
     * Returns the canvas used for rendering
     * @return the canvas used for rendering
     */
    public VCanvas3D getCanvas() {
        return canvas;
    }

    /**
     * Disposes 3D resources.
     */
    private void dispose3D() {
        if (!VGraphicsUtil.NO_3D) {
            emptyView();
            value = null;

            if (getCanvas() != null) {
                getCanvas().getOffscreenCanvas3D().stopRenderer();
            }

            canvas = null;
        }
    }

    @Override
    public void dispose() {
        dispose3D();
        super.dispose();
    }

    @Override
    public void enterFullScreenMode(Dimension size) {
        super.enterFullScreenMode(size);
        previousVCanvas3DSize = canvas.getSize();
        container.setPreferredSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        container.setMinimumSize(null);
        container.setMaximumSize(null);

        canvas.setPreferredSize(null);
        canvas.setMinimumSize(minimumVCanvas3DSize);
        canvas.setMaximumSize(null);

        revalidate();
    }

    @Override
    public void leaveFullScreenMode() {
        super.leaveFullScreenMode();
        container.setPreferredSize(null);
        container.setMinimumSize(null);
        container.setMaximumSize(null);

        canvas.setSize(previousVCanvas3DSize);
        canvas.setPreferredSize(previousVCanvas3DSize);
        canvas.setMinimumSize(minimumVCanvas3DSize);

        revalidate();
    }

    @Override
    public JComponent customViewComponent() {

//        final BufferedImage img = plotPane.getImage();
//
//        JPanel panel = new JPanel() {
//            @Override
//            public void paintComponent(Graphics g) {
//                g.drawImage(img, 0, 0, 640, 480,  null);
//            }
//        };
//
//        return panel;
        return null;
    }
    
    @Override
    public boolean noSerialization() {
        // we cannot serialize shape3d objects
        return true;
    }
}
