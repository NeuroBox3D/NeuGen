/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2007–2012 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
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
package org.neugen.parsers;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.SceneBase;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.scenegraph.io.SceneGraphFileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.neugen.gui.*;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.StringTokenizer;
import javax.media.j3d.BranchGroup;
import org.apache.log4j.Logger;
import org.jdesktop.application.Task;
import org.jdesktop.application.Application;
import org.neugen.utils.Utils;

/**
 *
 * @author Sergei
 */
public final class OBJReader extends Task<Scene, Void> {

    private final File file;
    private static final Logger logger = Logger.getLogger(OBJReader.class.getName());
    private Scene s;
    private ReaderType rt;

    public enum ReaderType {

        TXT, OBJ, BG;
    }

    public OBJReader(Application app, File f, ReaderType rt) {
        super(app);
        this.file = f;
        this.rt = rt;

    }

    public void setMyProgress(int value, int min, int max) {
        setProgress(value, min, max);
    }

    public Scene getScene() {
        return s;
    }

    @Override
    protected Scene doInBackground() throws FileNotFoundException, IOException {
        setMessage("simple obj");
        Reader in = new FileReader(file);
        BufferedReader lineReader = new BufferedReader(in);
        String nextLine = null;
        nextLine = lineReader.readLine().trim();

        switch (rt) {
            case TXT: {
                String[] dataArray = nextLine.split(" ");
                int verticesVal = Integer.parseInt(dataArray[0]);
                int facesVal = Integer.parseInt(dataArray[1]);
                logger.info("first line: " + nextLine);
                logger.info("vertices: " + verticesVal);
                logger.info("faces:" + facesVal);

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < verticesVal; i++) {
                    nextLine = lineReader.readLine().trim();
                    StringTokenizer st = new StringTokenizer(nextLine);
                    st.nextToken();
                    sb.append('v').append(' ');
                    sb.append(st.nextToken()).append(' ');
                    sb.append(st.nextToken()).append(' ');
                    sb.append(st.nextToken()).append('\n');
                }

                for (int i = 0; i < facesVal; i++) {
                    nextLine = lineReader.readLine().trim();
                    StringTokenizer st = new StringTokenizer(nextLine);
                    st.nextToken();
                    int v1 = Integer.parseInt(st.nextToken()) + 1;
                    int v2 = Integer.parseInt(st.nextToken()) + 1;
                    int v3 = Integer.parseInt(st.nextToken()) + 1;
                    sb.append('f').append(' ');
                    sb.append(v1).append(' ');
                    sb.append(v2).append(' ');
                    sb.append(v3).append('\n');
                }

                Reader sr = new StringReader(sb.toString());
                int flags = ObjectFile.RESIZE | ObjectFile.STRIPIFY | ObjectFile.TRIANGULATE;
                ObjectFile f = new ObjectFile(flags, (float) (60.0 * Math.PI / 180.0));
                try {
                    s = f.load(sr);
                } catch (FileNotFoundException e) {
                    logger.error(e);
                    System.exit(1);
                } catch (ParsingErrorException e) {
                    logger.error(e);
                    System.exit(1);
                } catch (IncorrectFormatException e) {
                    logger.error(e);
                    System.exit(1);
                }
                break;
            }
            case OBJ: {
                StringBuilder sb = new StringBuilder();
                while ((nextLine = lineReader.readLine()) != null) {
                    nextLine = nextLine.trim();
                    if (nextLine.length() > 0) {
                        if (nextLine.startsWith("o")) {
                            continue;
                        }
                        sb.append(nextLine).append('\n');
                    }
                }
                Reader sr = new StringReader(sb.toString());
                int flags = ObjectFile.RESIZE | ObjectFile.STRIPIFY | ObjectFile.TRIANGULATE;
                ObjectFile f = new ObjectFile(flags, (float) (60.0 * Math.PI / 180.0));
                try {
                    s = f.load(sr);
                } catch (FileNotFoundException e) {
                    logger.error(e);
                    System.exit(1);
                } catch (ParsingErrorException e) {
                    logger.error(e);
                    System.exit(1);
                } catch (IncorrectFormatException e) {
                    logger.error(e);
                    System.exit(1);
                }
                break;
            }
            case BG: {
                SceneGraphFileReader reader = new SceneGraphFileReader(file);
                int size = reader.getBranchGraphCount();
                SceneBase sb = new SceneBase();
                for (int i = 0; i < size; i++) {
                    BranchGroup bg = reader.readBranchGraph(i)[0];
                    sb.setSceneGroup(bg);
                }
                s = (Scene) sb;
                reader.close();
            }
        }
        in.close();
        lineReader.close();
        return s;  // return your result
    }

    @Override
    protected void succeeded(Scene result) {
        NeuGenView ngView = NeuGenView.getInstance();
        ngView.setScene(s);
        ngView.enableButtons();
        System.gc();
        ngView.outPrintln(Utils.getMemoryStatus());
        if (ngView.visualizeData() == null) {
            logger.info("is null!");
        }
        ngView.visualizeData().run();
    }
}
