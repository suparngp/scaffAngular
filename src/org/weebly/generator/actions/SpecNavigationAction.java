package org.weebly.generator.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.weebly.generator.components.Configuration;
import org.weebly.generator.components.ConfigurationLoader;

/**
 * Test Spec Navigation Action
 * Created by IronMan on 7/10/14.
 */
public class SpecNavigationAction extends AnAction {

    ConfigurationLoader configurationLoader;
    Project project;

    public void actionPerformed(AnActionEvent e) {
        configurationLoader = ServiceManager.getService(ConfigurationLoader.class);
        project = e.getData(PlatformDataKeys.PROJECT);
        Configuration state = configurationLoader.getState();
        if (state == null
                || state.getMainSpecFilesMap() == null
                || state.getMainSpecFilesMap().isEmpty()) {
            return;
        }

        VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        if (file == null || file.isDirectory()) {
            return;
        }

        String name = file.getName();

        if (state.getMainSpecFilesMap().get(name) != null) {
            String testFilePath = state.getMainSpecFilesMap().get(name);
            if (testFilePath == null) {
                return;
            }
            Commons.openFileInEditor(project, testFilePath);
        } else{
            String testFilePath = file.getParent().getPath() + "/" + name.replace(".js", "Spec.js");
            boolean opened = Commons.openFileInEditor(project, testFilePath);
            if (opened){
                configurationLoader.getState().getMainSpecFilesMap().put(name, testFilePath);
            }
        }
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        configurationLoader = ServiceManager.getService(ConfigurationLoader.class);
        VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        if(file == null || file.isDirectory()){
            e.getPresentation().setEnabled(false);
            return;
        }

        Configuration state = configurationLoader.getState();
        if (state == null
                || state.getMainSpecFilesMap() == null
                || state.getMainSpecFilesMap().isEmpty()) {
            e.getPresentation().setEnabled(false);
        }

        else{
            e.getPresentation().setEnabled(true);
        }
    }
}
