package org.weebly.generator.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.weebly.generator.model.Component;
import org.weebly.generator.services.Configuration;
import org.weebly.generator.services.ConfigurationLoader;
import org.weebly.generator.services.FileHandler;
import org.weebly.generator.services.TemplateLoader;
import org.weebly.generator.model.exceptions.AngularIUnitException;
import org.weebly.generator.forms.CreateFile;
import org.weebly.generator.forms.ErrorDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateAction extends AnAction {
    private static Project project;

    private static TemplateLoader templateLoader;
    private static ConfigurationLoader configurationLoader;

    private static String currentPath = "";
    private static AnActionEvent e;

    public CreateAction() {
        templateLoader = ServiceManager.getService(TemplateLoader.class);
        configurationLoader = ServiceManager.getService(ConfigurationLoader.class);

        if(configurationLoader.getState() == null){
            configurationLoader.loadState(new Configuration());
        }
    }

    public void actionPerformed(AnActionEvent _e_) {
        e = _e_;

        VirtualFile data = e.getData(PlatformDataKeys.VIRTUAL_FILE);

        if(data == null){
            showError("Unknown Error", "Please file an issue if you think this is a bug");
            return;
        }

        currentPath = data.getPath();
        project = e.getData(PlatformDataKeys.PROJECT);

        new CreateFile(this, templateLoader).showDialog();
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        if(file == null || !file.isDirectory()){
            e.getPresentation().setEnabled(false);
        }
        else{
            e.getPresentation().setEnabled(true);
        }
    }

    /**
     * Handles the callback from the UI to create the component
     * @param componentName name of component
     * @param componentType type of component
     * @param moduleName name of module
     */
    public void createAngularComponentsHandler(String componentName, String componentType, String moduleName) {

        try {
            String codeFileName = templateLoader.getSrcFilename(componentName, componentType);
            String specFileName = templateLoader.getTestFilename(componentName, componentType);

            File codeFile = FileHandler.createFile(codeFileName, currentPath);
            File specFile = FileHandler.createFile(specFileName, currentPath);

            File currentDirectory = new File(currentPath);

            VirtualFile fileByIoFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(currentDirectory);

            if (fileByIoFile != null) {
                fileByIoFile.getChildren();
                fileByIoFile.refresh(false, true);

                Component tmp = templateLoader.getComponents().get(componentType);

                FileHandler.writeFileContent(codeFile, templateLoader.formatContent(tmp.code, componentName, moduleName));
                FileHandler.writeFileContent(specFile, templateLoader.formatContent(tmp.spec, componentName, moduleName));

                //open the file after writing content to it.
                fileByIoFile.refresh(false, true);
                fileByIoFile.getFileSystem().refresh(false);
                openFileInEditor(specFileName);
                openFileInEditor(codeFileName);

                //since all the files have been created successfully, add the data to persistent state.
                persistModuleName(moduleName, codeFileName, specFileName);

            } else {
                System.out.println("File not refreshed");
            }

        } catch (AngularIUnitException ae) {
            showError(ae.getName(), ae.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unknown Error", "Please report in case you think that this is a bug.");
        }
    }



    private void persistModuleName(String moduleName, String codeFileName, String specFileName) {
        Configuration state = configurationLoader.getState();
        if(state != null){
            if(state.getMainSpecFilesMap() == null){
                state.setMainSpecFilesMap(new HashMap<String, String>());
            }
            state.getMainSpecFilesMap().put(codeFileName, currentPath + "/" + specFileName);

            if(state.getModuleNameSuggestions() == null){
                state.setModuleNameSuggestions(new ArrayList<String>());
            }
            if(!state.getModuleNameSuggestions().contains(moduleName)){
                state.getModuleNameSuggestions().add(moduleName);
            }
        }
    }

    private void openFileInEditor(String filename) {
        Commons.openFileInEditor(project, currentPath + "/" + filename);
    }



    /**
     * Displays the error dialog
     *
     * @param title       the title of the error
     * @param description the description of the error
     */
    private void showError(String title, String description) {
        new ErrorDialog(title, description).display();
    }

    /**
     * Checks if the main JS file and test file exists
     *
     * @param fileName the filename
     * @return true if the templates don't exist, otherwise false
     */
    public boolean checkIfFileExists(String fileName) {
        return FileHandler.fileExists(currentPath + "/" + fileName);
    }

    /**
     * Returns the suggestions for the module names
     *
     * @return the list of module names used so far
     */
    public List<String> getModuleNameSuggestions() {
        return configurationLoader.getState() == null
                ? new ArrayList<String>()
                : configurationLoader.getState().getModuleNameSuggestions();
    }
}
