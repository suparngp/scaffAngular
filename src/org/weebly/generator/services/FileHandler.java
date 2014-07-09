package org.weebly.generator.services;

import org.weebly.generator.exceptions.DirectoryNotFoundException;
import org.weebly.generator.exceptions.DuplicateFileException;
import org.weebly.generator.exceptions.FileNotFoundException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Contains APIs to handle the files
 * Created by IronMan on 7/8/14.
 */

public class FileHandler {

    /**
     * Creates a file with the specified name and path
     *
     * @param fileName the file name
     * @param path     the path where the file needs to be created
     * @return true if the file is successfully, otherwise false
     * @throws java.io.IOException                                        if the file wrote operation fails
     * @throws org.weebly.generator.exceptions.DirectoryNotFoundException if the directory is not found
     * @throws org.weebly.generator.exceptions.DuplicateFileException     if the file already exists in the path
     */
    public File createFile(String fileName, String path)
            throws IOException,
            DirectoryNotFoundException,
            DuplicateFileException {

        File directory = new File(path);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new DirectoryNotFoundException();
        }
        String filePath = path + "/" + fileName;
        File fileToBeCreated = new File(filePath);
        if (fileToBeCreated.exists()) {
            throw new DuplicateFileException();
        }

        boolean result = fileToBeCreated.createNewFile();
        boolean writable = fileToBeCreated.setWritable(true);

        return fileToBeCreated;
    }

    /**
     * Deletes a file
     *
     * @param fileName the file name
     * @param path     the path where the file needs to be deleted
     * @return true if the file is deleted, otherwise false
     * @throws IOException                if the delete operation failed
     * @throws DirectoryNotFoundException if the directory is not found on the path
     * @throws FileNotFoundException      if the file to be deleted is not found
     */
    public boolean deleteFile(String fileName, String path)
            throws IOException,
            DirectoryNotFoundException,
            FileNotFoundException {
        File directory = new File(path);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new DirectoryNotFoundException();
        }
        String filePath = path + "/" + fileName;
        File fileToBeDeleted = new File(filePath);
        if (!fileToBeDeleted.exists()) {
            throw new FileNotFoundException();
        }

        return fileToBeDeleted.delete();
    }


    /**
     * Checks if the file exists
     * @param filePath the file path
     * @return true if the file exists otherwise returns false
     */
    public boolean fileExists(String filePath){
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * writes text to file
     * @param file file object to write to
     * @param content text to write to file
     */
    public void writeFileContent(File file, String content) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
