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
package org.neugen.makemovie;
/*
 * Based upon OffScreenCanvas3D source code from
 * book Java 3D Jump-Start, by AARON E. WALSH and DOUG GEHRINGER
 * http://www.web3dbooks.com/java3d/jumpstart/Java3DExplorer.html
 *
 */

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * OffScreenCanvas3D is used to do off screen rendering for screen captures.
 * The images for the book were produced using this class.
 * Several changes were made on top of "Java3D Jump Start" book.
 * <pre>
 * usage:
 * ScreenShot screenShot = new ScreenShot(canvas,universe,1.0f);
 * JPanel control = screenShot.getControlPanel();
 * this.add(control,"South");
 * // you can also put the control in a floating Frame;
 * Frame f = new Frame("ScreenShot Control");
 * f.setSize(240,200);
 * f.add(control);
 * f.pack();
 * f.setVisible(true);
 *
 * </pre>
 */
public class ScreenShot extends Canvas3D {

    private Canvas3D canvas;
    private SimpleUniverse universe;
    private Dimension dim;
    private float offScreenScale = 1.0f;
    private int counter = 1;
    private String imageType = "jpg";
    private JPanel controlPanel = null;

    public ScreenShot(Canvas3D canvas, SimpleUniverse universe, float scale) {
        super(canvas.getGraphicsConfiguration(), true);
        this.canvas = canvas;
        this.universe = universe;
        universe.getViewer().getView().addCanvas3D(this);
        setOffScreenScale(offScreenScale);
    }

    /**
     * render a Image from canvas
     * @param width Image width
     * @param height Image height
     * @return the image
     */
    public BufferedImage doRender(int width, int height) {

        BufferedImage bImage =
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        ImageComponent2D buffer =
                new ImageComponent2D(ImageComponent.FORMAT_RGB, bImage);
//buffer.setYUp(true);

        setOffScreenBuffer(buffer);
        renderOffScreenBuffer();
        waitForOffScreenRendering();
        bImage = getOffScreenBuffer().getImage();
        return bImage;
    }

    /**
     * save a image from current Canvas3D view
     * @param filename
     * @param width
     * @param height
     */
    public void snapImageFile(String filename) {
        BufferedImage bImage = doRender(dim.width, dim.height);
        try {
            FileOutputStream fos = new FileOutputStream(filename + "." + imageType);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            ImageIO.write(bImage, imageType, bos);

            bos.flush();
            fos.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * set the size of the off-screen canvas based on a scale
     * of the on-screen size
     * @param scale to be applied.
     * */
    public void setOffScreenScale(float offScreenScale) {
// set the size of the off-screen canvas based on a scale
// of the on-screen size
        this.offScreenScale = offScreenScale;
        Screen3D sOn = canvas.getScreen3D();
        Screen3D sOff = this.getScreen3D();

        this.dim = sOn.getSize();
        dim.width *= offScreenScale;
        dim.height *= offScreenScale;
        sOff.setSize(dim);
        sOff.setPhysicalScreenWidth(sOn.getPhysicalScreenWidth()
                * offScreenScale);
        sOff.setPhysicalScreenHeight(sOn.getPhysicalScreenHeight()
                * offScreenScale);
    }

    /**
     * return the scale applied
     * @return
     */
    public float getOffScreenScale() {
        return this.offScreenScale;
    }

    /**
     * remove this canvas from universe
     */
    public void removeCanvas() {
        universe.getViewer().getView().removeCanvas3D(this);
    }

    /**
     * return a clone of current screen dimension
     * @return Dimension of this screen
     */
    public Dimension getDimension() {
        return (Dimension) this.dim.clone();
    }
    JTextField filenameTF;
    JTextField scaleTF;
    JLabel sizeLabel;

    /**
     * creates and return a Control panel for this offscreen renderer
     * example:
     * <pre>
     * usage:
     *	 ScreenShot screenShot = new ScreenShot(canvas,u,1.0f);
     * JPanel control = screenShot.getControlPanel();
     * this.add(control,"South");
     *
     * // you can also put the control in a floating Frame;
     * Frame f = new Frame("ScreenShot Control");
     * f.setSize(240,200);
     * f.add(control);
     * f.pack();
     * f.setVisible(true);
     * <pre>
     * @return a panel to control this renderer
     */
    public JPanel getControlPanel() {
        if (controlPanel == null) {
            controlPanel = new JPanel();
            JButton btSave = new JButton("Save");
            JLabel label2 = new JLabel("filename");
            JLabel label3 = new JLabel("Scale");
            sizeLabel = new JLabel("ScreenShot - " + dim.width
                    + " x " + dim.height);
            filenameTF = new JTextField("screenshot_1." + imageType);
            scaleTF = new JTextField("1.0f", 4);

            filenameTF.setToolTipText("<HTML>Supports several image file formats,"
                    + " as<br> .png, .jpg, .bmp, etc.");
            scaleTF.setToolTipText("set scale based upon your video screen.");
            btSave.setToolTipText("Save image on disk.");

            btSave.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    System.gc();
                    String name = filenameTF.getText();
//check if imageType changed
                    imageType = name.substring(name.lastIndexOf(".") + 1, name.length());

                    name = name.substring(0, name.lastIndexOf("."));
                    snapImageFile(name);
                    counter++;
                    int pos = name.lastIndexOf("_");
                    if (pos > 0) {
                        name = name.substring(0, pos) + "_" + counter;
                    } else {
                        name = name + "_" + counter;
                    }
                    filenameTF.setText(name + "." + imageType);
                    System.gc();
                }
            }); // btSave listener

            scaleTF.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String scale = scaleTF.getText();
                    float temp = offScreenScale;
                    try {
                        offScreenScale = Float.parseFloat(scale);
                        setOffScreenScale(offScreenScale);
                        sizeLabel.setText("ScreenShot - " + dim.width
                                + " x " + dim.height);
                    } catch (Exception ex) {
                        offScreenScale = temp;
                        scaleTF.setText(offScreenScale + "");
                    }
                }
            });

            controlPanel.setLayout(new BorderLayout());
            controlPanel.add(sizeLabel, "North");

            JPanel pan2 = new JPanel();
            pan2.setLayout(new GridLayout(2, 2, 2, 2));

            pan2.add(label2);
            pan2.add(filenameTF);
            pan2.add(label3);
            pan2.add(scaleTF);

            controlPanel.add(pan2, "Center");
            controlPanel.add(btSave, "South");
        }
        return controlPanel;
    }
}
