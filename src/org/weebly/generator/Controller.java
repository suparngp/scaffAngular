package org.weebly.generator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.weebly.generator.exceptions.AngularIUnitException;
import org.weebly.generator.forms.CreateFile;
import org.weebly.generator.forms.ErrorDialog;
import org.weebly.generator.services.FileHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Controller Generator for angular.
 * Created by suparngupta on 7/7/14.
 */
public class Controller extends AnAction {
    private static String currentPath = "";
    private static FileHandler fileHandler = new FileHandler();
    AnActionEvent e;
    Project project;

    public void actionPerformed(AnActionEvent e) {
        currentPath = e.getData(PlatformDataKeys.VIRTUAL_FILE).getPath();
        project = e.getData(PlatformDataKeys.PROJECT);
        System.out.println(currentPath);
        new CreateFile(this).showDialog();
        this.e = e;
    }

    public void createHandler(String fileName, String fileType) {
        System.out.println(fileName + " creating at " + currentPath);
        try {
            String mainFileName = getSrcFilename(fileName, fileType);
            String testFileName = getTestFilename(fileName, fileType);
            File mainFile = fileHandler.createFile(mainFileName, currentPath);
            File testFile = fileHandler.createFile(testFileName, currentPath);
            File currentDirectory = new File(currentPath);

            VirtualFile fileByIoFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(currentDirectory);

            if (fileByIoFile != null) {
                fileByIoFile.getChildren();
                fileByIoFile.refresh(false, true);

                processFile(mainFileName);
                fileHandler.writeFileContent(mainFile, getSrcContentByType(fileType));

                processFile(testFileName);
                fileHandler.writeFileContent(testFile, getTestContentByType(fileType));
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

    private void processFile(String filename) {
        File toBeOpened;
        VirtualFile vfToBeOpened;
        toBeOpened = new File(currentPath + "/" + filename);
        vfToBeOpened = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(toBeOpened);
        if (vfToBeOpened != null) {
            FileEditorManager.getInstance(project).openFile(vfToBeOpened, true);
        }
    }

    /**
     * Gets the file name with the required type based on if its a controller or a directive or a service
     *
     * @param fileName the filename
     * @param type the type of the file
     * @return the file name with file type
     */
    private String getFilenameWithSuffix(String fileName, String type) {
        if (type.equals("Controller")) {
            fileName += "Ctrl";

        } else if (type.equals("Directive")) {
            fileName += "Directive";

        } else if (type.equals("Service")) {
            fileName += "Service";
        }

        return fileName;
    }

    private String getSrcContentByType(String type) {
        if (type.equals("Controller")) {
            return "";
        } else if (type.equals("Directive")) {
            return "";
        } else if (type.equals("Service")) {
            return "";
        }

        return "";
    }

    private String getTestContentByType(String type) {
        if (type.equals("Controller")) {
            return "";
        } else if (type.equals("Directive")) {
            return "";
        } else if (type.equals("Service")) {
            return "";
        }

        return "";
    }

    public String getSrcFilename(String baseName, String fileType ) {
        return getFilenameWithSuffix(baseName, fileType) + ".js";
    }

    public String getTestFilename(String baseName, String fileType ) {
        return getFilenameWithSuffix(baseName, fileType) + "Spec.js";
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
     * @return true if the files don't exist, otherwise false
     */
    public boolean checkIfFileExists(String fileName) {
        return fileHandler.fileExists(currentPath + "/" + fileName);
    }
}
