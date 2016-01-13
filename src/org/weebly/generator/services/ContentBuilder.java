package org.weebly.generator.services;

import com.intellij.openapi.components.ServiceManager;
import org.weebly.generator.model.Component;
import org.weebly.generator.model.exceptions.DirectoryNotFoundException;
import org.weebly.generator.model.exceptions.DuplicateFileException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ContentBuilder {

    /**
     * Handles the callback from the UI to create the component
     */
    public ArrayList<String> createAngularComponentsHandler(String currentPath, String componentType, String moduleName, String componentBasename) throws DirectoryNotFoundException, DuplicateFileException, IOException {

        ArrayList<String> createdFiles = new ArrayList<>();
        TemplateLoader t = ServiceManager.getService(TemplateLoader.class);
        Component tmp = t.getComponents().get(componentType);
        String componentName = ContentBuilder.getComponentName(componentBasename, componentType);

        String codeFileName = ContentBuilder.getSrcFilename(componentBasename, componentType);
        File codeFile = FileHandler.createFile(codeFileName, currentPath);
        FileHandler.writeFileContent(codeFile, this.formatContent(tmp.code, componentName, moduleName));
        createdFiles.add(codeFileName);

        String specFileName = ContentBuilder.getTestFilename(componentBasename, componentType);
        File specFile = FileHandler.createFile(specFileName, currentPath);
        FileHandler.writeFileContent(specFile, this.formatContent(tmp.spec, componentName, moduleName));
        createdFiles.add(specFileName);

        return createdFiles;
    }

    public String formatContent(String content, String componentName, String moduleName) {
        return content.replaceAll("#COMPONENTNAME#", componentName).replaceAll("#MODULENAME#", moduleName);
    }

    /**
     * Gets the file name with the required type based on if its a controller or a directive or a service
     *
     * @param basename the filename
     * @param type     the type of the file
     * @return the file name with file type
     */
    public static String getComponentName(String basename, String type) {
        if (type.equals("Controller")) {
            return basename + "Ctrl";
        } if (type.equals("Service")) {
            return basename;
        } else {
            return basename + type;
        }
    }

    public static String getSrcFilename(String baseName, String fileType) {
        return getComponentName(baseName, fileType) + ".js";
    }

    public static String getTestFilename(String baseName, String fileType) {
        return getComponentName(baseName, fileType) + "Spec.js";
    }
}
