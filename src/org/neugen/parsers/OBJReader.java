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
