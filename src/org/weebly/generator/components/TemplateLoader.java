package org.weebly.generator.components;

import org.weebly.generator.exceptions.FileNotFoundException;

import java.io.*;
import java.util.HashMap;

/**
 * Service to load file templates.
 * Created by IronMan on 7/11/14.
 */
public class TemplateLoader {
    private HashMap<String, String> docTemplates;
    private HashMap<String, String> codeTemplates;
    public boolean isLoaded = false;

    public TemplateLoader() {
        docTemplates = new HashMap<String, String>();
        codeTemplates = new HashMap<String, String>();
        initComponent();
    }

    public final void initComponent() {
        try {
            isLoaded = true;
            //System.out.println("I am invoked");
            String docPath = "/templates/docs/";
            String codePath = "/templates/code/";
            String[] docFiles = {"controller", "directive", "service", "filter", "controllerSpec", "directiveSpec", "serviceSpec", "filterSpec"};
            String[] codeFiles = {"controller", "directive", "service", "filter", "controllerSpec", "directiveSpec", "serviceSpec", "filterSpec"};
            String docExt = "ngdoc";
            String codeExt = "js";
//            String basePath = new File(PathUtil.getJarPathForClass(getClass())).getAbsolutePath();
//            String basePath = "";

            // keep these for loops separate till we come up with a common file extension.
            for (String docFile : docFiles) {
                String fileName = docPath + docFile + "." + docExt;
                String contents = readFile(fileName);
                docTemplates.put(docFile, contents);
            }


            for (String codeFile : codeFiles) {
                String fileName = codePath + codeFile + "." + codeExt;
//
                String contents = readFile(fileName);
                codeTemplates.put(codeFile, contents);
            }
            //System.out.println(codeTemplates);
            //System.out.println(docTemplates);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the contents of the file and returns as a string
     *
     * @param filePath the file path
     * @return the string contents
     * @throws java.io.IOException
     * @throws org.weebly.generator.exceptions.FileNotFoundException
     */
    private String readFile(String filePath)
            throws IOException,
            FileNotFoundException {
        ClassLoader cl = getClass().getClassLoader();
        InputStream in = cl.getResourceAsStream(filePath);
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = bf.readLine()) != null) {
            builder.append(line).append("\n");
        }

        return builder.toString();
    }

    /**
     * Gets the map of documentation templates
     *
     * @return the doc template map
     */
    public HashMap<String, String> getDocTemplates() {
        return docTemplates;
    }


    /**
     * Gets the map of templates.code templates
     *
     * @return the templates.code template map
     */
    public HashMap<String, String> getCodeTemplates() {
        return codeTemplates;
    }
}
