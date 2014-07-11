package org.weebly.generator.actions;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;

/**
 * A Common AP I for shared actions.
 * Created by IronMan on 7/11/14.
 */
public class Commons {

    /**
     * Opens a file in the editor in the current project
     * @param project the current project
     * @param filePath the file's path
     * @return true if the file is opened, otherwise false
     */
    public static boolean openFileInEditor(Project project, String filePath){
        File toBeOpened;
        VirtualFile vfToBeOpened;
        toBeOpened = new File(filePath);
        vfToBeOpened = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(toBeOpened);
        if (vfToBeOpened != null) {
            FileEditorManager.getInstance(project).openFile(vfToBeOpened, true);
            return true;
        }
        return false;
    }
}
