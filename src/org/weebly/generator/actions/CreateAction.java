package org.weebly.generator.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.weebly.generator.actions.dialogs.CreateFile;
import org.weebly.generator.services.TemplateLoader;

public class CreateAction extends AnAction {

    private static TemplateLoader templateLoader;

    public CreateAction() {
        templateLoader = ServiceManager.getService(TemplateLoader.class);
    }

    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);

        VirtualFile data = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        String currentPath = data.getPath();

        new CreateFile(project, currentPath, templateLoader.getComponentTypes()).showDialog();
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);

        //display only if clicked on directory
        e.getPresentation().setEnabled(file != null && file.isDirectory());
    }
}
