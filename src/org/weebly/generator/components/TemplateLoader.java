package org.weebly.generator.components;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.util.PathUtil;
import org.jetbrains.annotations.NotNull;
import org.weebly.generator.exceptions.FileNotFoundException;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Application Component to load the template templates.
 * Created by IronMan on 7/9/14.
 */
public class TemplateLoader implements ApplicationComponent {

    private HashMap<String, String> docTemplates;
    private HashMap<String, String> codeTemplates;

    public TemplateLoader() {
        docTemplates = new HashMap<String, String>();
        codeTemplates = new HashMap<String, String>();
    }

    public void initComponent() {
        try{
            //System.out.println("I am invoked");
            String docPath = "/templates/docs/";
            String codePath = "/templates/code/";
            String[] docFiles = {"controller", "directive", "service", "controllerSpec", "directiveSpec", "serviceSpec"};
            String[] codeFiles = {"controller", "directive", "service", "controllerSpec", "directiveSpec", "serviceSpec"};
            String docExt = "ngdoc";
            String codeExt = "js";
            String basePath = new File(PathUtil.getJarPathForClass(getClass())).getAbsolutePath();
            //System.out.println(Arrays.asList(new File(basePath).listFiles()));

            // keep these for loops separate till we come up with a common file extension.
            for(String docFile: docFiles){
                String fileName = basePath + docPath + docFile + "." + docExt;

                File file = new File(fileName);
                //System.out.println(file.getAbsolutePath());
                if(file.exists()){
                    String contents = readFile(fileName);
                    docTemplates.put(docFile, contents);
                }
            }


            for(String codeFile: codeFiles){
                String fileName = basePath + codePath + codeFile + "." + codeExt;
                File file = new File(fileName);
                if(file.exists()){
                    String contents = readFile(fileName);
                    codeTemplates.put(codeFile, contents);
                }
            }

            //System.out.println(codeTemplates);
            //System.out.println(docTemplates);

        }

        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void disposeComponent() {
        docTemplates.clear();
        codeTemplates.clear();
    }

    @NotNull
    public String getComponentName() {
        return "TemplateLoader";
    }

//    public static void main(String[] args){
//        File file = new File("templates/templates.docs/controller.ngdoc");
//        //System.out.println(file.exists());
//    }

    /**
     * Reads the contents of the file and returns as a string
     * @param filePath the file path
     * @return the string contents
     * @throws IOException
     * @throws FileNotFoundException
     */
    private String readFile(String filePath)
            throws IOException,
            FileNotFoundException{
        File file = new File(filePath);
        BufferedReader bf = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder builder = new StringBuilder();
        while((line =  bf.readLine()) != null){
            builder.append(line).append("\n");
        }

        return builder.toString();
    }

    /**
     * Gets the map of documentation templates
     * @return the doc template map
     */
    public HashMap<String, String> getDocTemplates() {
        return docTemplates;
    }


    /**
     * Gets the map of templates.code templates
     * @return the templates.code template map
     */
    public HashMap<String, String> getCodeTemplates() {
        return codeTemplates;
    }

}
