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

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            String mainFileName = getFileNameWithType(trimJSExtension(fileName), fileType);
            String testFileName = mainFileName + "Spec";
            fileHandler.createFile(mainFileName + ".js", currentPath);
            fileHandler.createFile(testFileName + ".js", currentPath);
            File currentDirectory = new File(currentPath);

            VirtualFile fileByIoFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(currentDirectory);

            if (fileByIoFile != null) {
                fileByIoFile.getChildren();
                fileByIoFile.refresh(false, true);

                File toBeOpened = new File(currentPath + "/" + testFileName + ".js");
                VirtualFile vfToBeOpened = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(toBeOpened);
                if (vfToBeOpened != null) {
                    FileEditorManager.getInstance(project).openFile(vfToBeOpened, true);
                }

                toBeOpened = new File(currentPath + "/" + mainFileName + ".js");
                vfToBeOpened = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(toBeOpened);
                if (vfToBeOpened != null) {
                    FileEditorManager.getInstance(project).openFile(vfToBeOpened, true);
                }

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

    /**
     * Gets the file name with the required type based on if its a controller or a directive or a service
     *
     * @param fileName the filename
     * @param fileType the type of the file
     * @return the file name with file type
     */
    private String getFileNameWithType(String fileName, String fileType) {

        if (fileType.equals("Controller")) {
            fileName += "Ctrl";

        } else if (fileType.equals("Directive")) {
            fileName += "Directive";

        } else if (fileType.equals("Service")) {
            fileName += "Service";

        }

        return fileName;
    }

    /**
     * Trims the .js from the end of the file name if exists and returns the rest of the string.
     *
     * @param fileName the filename
     * @return the file name without .js
     */
    private String trimJSExtension(String fileName) {
        //check if the file name ends with js
        Pattern pattern = Pattern.compile(".+\\.js$");
        Matcher matcher = pattern.matcher(fileName);
        String trimmedFileName;
        if (matcher.matches()) {
            trimmedFileName = fileName.trim().substring(0, fileName.length() - 3);
        } else {
            trimmedFileName = fileName.trim();
        }
        return trimmedFileName;
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
     * @param fileType the type of the file
     * @return true if the files don't exist, otherwise false
     */
    public boolean checkIfFileExists(String fileName, String fileType) {
        String mainFileName = getFileNameWithType(trimJSExtension(fileName), fileType);
        return fileHandler.fileExists(currentPath + "/" + mainFileName + ".js")
                || fileHandler.fileExists(currentPath + "/" + mainFileName + "Spec.js");
    }
}
