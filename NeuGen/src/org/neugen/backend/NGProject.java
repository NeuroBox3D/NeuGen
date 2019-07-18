package org.neugen.backend;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.net.URL;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.IOUtils;

import org.apache.log4j.Logger;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.gui.NeuGenConstants;
import org.neugen.utils.NeuGenLogger;
import org.neugen.backend.NGBackendUtil;
import org.neugen.parsers.NeuGenConfigStreamer;
import org.neugen.parsers.DefaultInheritance;
import org.neugen.gui.NeuGenProject;



/**
 * creating, opening, saving project (i.e parameter files: param.neu and internau.neu)
 *
 * Code from NGBackend
 */
public final class NGProject {
    /// public static members
    public static final Logger logger = Logger.getLogger(NGProject.class.getName());

    ////////////////////////////////////////////////////////////////
    private Map<String, XMLObject> params;
    // projectPath= sourceTemplate +File.separator+projecName;
    private String projectName;
    private String sourceTemplate;
    private String projectType;

    public NGProject(){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.params=new HashMap<>();
    }

    public  NGProject(String projectName, String sourceTemplate, String projectType){
        NeuGenConstants.WITH_GUI = false;
        NeuGenLogger.initLogger();
        this.params=new HashMap<String, XMLObject>();

        this.projectName=projectName;
        this.sourceTemplate=sourceTemplate;
        this.projectType=projectType;
    }

    /**
     *  setting ProjectName
     * @param projectName
     */
    public void setProjectName(String projectName){
        this.projectName=projectName;
    }

    /**
     *
     * @return projectName
     */
    public String getProjectName(){
        return projectName;
    }

    /**
     * setting source template
     * @param sourceTemplate
     */
    public void setSourceTemplate(String sourceTemplate){
        this.sourceTemplate=sourceTemplate;
    }

    /**
     *
     * @return SourceTemplate
     */
    public String getSourceTemplate(){
        return sourceTemplate;
    }

    /**
     * setting projectPath and separating into projectName and sourceTemplate
     *
     * @param projectPath
     */
    public void setProjectPath(String projectPath){
        if(projectPath.contains("/")){
            int indexLastSlash=projectPath.lastIndexOf("/");
            this.projectName=projectPath.substring(indexLastSlash+1);
            this.sourceTemplate=projectPath.substring(0,indexLastSlash-1);
        }else{
            this.projectName=projectPath;
            this.sourceTemplate="";
        }
    }

    /**
     *
     * @return sourceTemplate+File.separator+projectName
     */
    public String getProjectPath(){
        return sourceTemplate+File.separator+projectName;
    }

    /**
     *
     * @return fild path of param.neu
     */
    public String getParamPath(){
        return getProjectPath()+File.separator+NeuGenConstants.PARAM_FNAME;
    }

    /**
     *
     * @return file path of interna.neu
     */
    public String getInternaPath(){
        return getProjectPath()+File.separator+NeuGenConstants.INTERNA_FNAME;
    }

    /**
     * setting projectType
     * @param projectType
     */
    public void setProjectType(String projectType){
        this.projectType=projectType;
    }

    /**
     *
     * @return projectType
     */
    public String setProjectType(){
        return projectType;
    }

    //////////////////////////////////////////////////////////////////////////////////
    //// creating a project by copying and unzipping params.zip
    /////////////////////////////////////////////////////////////////////////////////
    public void createProject(boolean force){
        String projectPath=getProjectPath();
        if(projectPath==null) {
            logger.error("please input the project path!");
            return;
        }
            String zipPath = copyParamZip(projectType, projectPath, force);
            unzipFile(zipPath, projectPath);
            loadParamTree();
    }

    private static String copyParamZip(String projectType,String projectPath,boolean force){
        //String projectPath=sourceTemplate+System.getProperty("file.separator") +projectName;
        System.out.println("project path (project type: " + projectType + "): " + projectPath);
        File projectDir = new File(projectPath);
        System.err.println("projectPath: " + projectPath);

        File dest= null;
        if (!NGBackendUtil.fileExists(projectDir, force)) {
            URL inputUrl = NGProject.class.getResource("/org/neugen/gui/resources/" + projectType.toLowerCase() + ".zip");
            dest=new File(projectDir +System.getProperty("file.separator")+ projectType.toLowerCase() + ".zip");

            try {
                FileUtils.copyURLToFile(inputUrl, dest);
                System.err.println("Copying file from: " + inputUrl + " to " + dest);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(NeuGenProject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return dest.toString();
    }


    private static void unzipFile(String zipFilePath, String projectPath) {
        File dir = new File(projectPath);
        System.out.println("projectPath:"+projectPath);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();

        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            System.out.println("zipFile:"+zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            System.out.println("ze:"+ze.getName());
            while(ze != null){
               if(!ze.getName().contains("/")){
                String fileName = ze.getName();
                File newFile = new File(dir.toString()+File.separator+ fileName);
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
               }
               //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();   
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////
    //// loading Map<String, XMLObject> based on param.neu and interna.neu ///
    ///////////////////////////////////////////////////////////////////////////////////

    /**
     * @brief loads the initial parameters from project directory
     * @param file
     * @param root
     * @return parameter list
     */
    public static XMLObject loadParam(File file) {
        XMLObject root = null;
        try {
            System.out.println("Load file: "+file);
            NeuGenConfigStreamer stream = new NeuGenConfigStreamer(null);
            root = stream.streamIn(file);
            DefaultInheritance inhProzess = new DefaultInheritance();
            root = inhProzess.process(root);
        } catch (IOException ioe) {
            logger.fatal("Error when opening input file parameter: " + ioe.toString());
            ioe.printStackTrace();
        }
        return root;
    }

    /**
     *
     * @param paramPath
     * @param internaPath
     */
    public void loadParamTree(){
        XMLObject paramRoot = loadParam(new File(getParamPath()));
        XMLObject internaRoot = loadParam(new File(getInternaPath()));
        params.put(NeuGenConstants.PARAM, paramRoot);
        params.put(NeuGenConstants.INTERNA, internaRoot);
    }
    /**
     *
     * @return parameter tree
     */
    public Map<String, XMLObject> getParamTree(){ //emtry Map: params.size()==0
        return params;
    }

    ////////////////////////////////////////////////////////////////////////////
    /// save  Map<String, XMLObject>
    ///////////////////////////////////////////////////////////////////////////

    /**
     * save XMLObject
     * @param currentRoot
     * @param projectPath
     * @param paramName
     */
    public static void saveXMLObject(XMLObject currentRoot, String projectPath, String paramName){
        /**
         * @todo brief: tied to GUI! (implement the pseudocode below - more reasonable)
         * workaround: 1) alter parameters in project file before generating the net
         *             2) generate net
         * 	       3) save the file in a gui-independent way (i. e.
         * 		dont use the XML tree representations in the GUI
         * 		since those values are not availabel for us)
         */
        ///XMLObject rootCopy = XMLObject.getCopyXMLObject(XMLObject.convert(currentRoot));
        XMLObject rootCopy = currentRoot; /// This should fix already the issue (concerning cannot save because tied to GUI)
        DefaultInheritance.reverseProcess(rootCopy);
        File dir = new File(projectPath);
        if(!dir.exists())dir.mkdirs();
        NeuGenConfigStreamer streamer = new NeuGenConfigStreamer(projectPath);

        File neuFile = new File(projectPath + File.separator + paramName + ".neu");
        try {
            logger.info("Write *** " + paramName + " *** file to: " + neuFile.getAbsolutePath());
            streamer.streamOut(rootCopy, neuFile);
        } catch (IOException ex) {
            logger.error(ex, ex);
        }

    }

    /**
     *  save ParamTree
     * @param paramTrees
     * @param projectPath
     */
    public static void saveParamTree(Map<String, XMLObject> paramTrees, String projectPath){
        for(Map.Entry<String, XMLObject> entry : paramTrees.entrySet()){
            saveXMLObject(entry.getValue(), projectPath, entry.getKey());
        }
    }

    public void saveParamTree(){
        Map<String, XMLObject> paramTrees=getParamTree();
        String projectPath=getProjectPath();
        if(paramTrees.size()==0){
            logger.error("Please load/create a parameter tree.");
        }

        saveParamTree(paramTrees, projectPath);
    }


    public static void main(String... args) {
        try {
            NGProject project = new NGProject();
            project.setProjectName("Neo1");
            project.setSourceTemplate("/Users/jwang/GCSC/Project/Neuron/HBP733/Test/Test");
            project.setProjectType(NeuGenConstants.NEOCORTEX_PROJECT);
            project.loadParamTree();
            //project.createProject(true);

            System.out.println(project.getParamTree());
            project.saveParamTree(project.getParamTree(),"/Users/jwang/GCSC/Project/Neuron/HBP733/Test/Test/Neo2");

        } catch (Exception e) {
            logger.fatal("Make sure you selected a valid project directory: " + e);
            e.printStackTrace();
        }
    }

}
