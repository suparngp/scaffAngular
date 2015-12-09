package org.weebly.generator.services;

import org.weebly.generator.model.exceptions.DirectoryNotFoundException;
import org.weebly.generator.model.exceptions.DuplicateFileException;
import org.weebly.generator.model.exceptions.FileNotFoundException;

import java.io.*;

/**
 * Contains APIs to handle the templates
 */

public class FileHandler {

    /**
     * Creates a file with the specified name and path
     *
     * @param fileName the file name
     * @param path     the path where the file needs to be created
     * @return true if the file is successfully, otherwise false
     * @throws java.io.IOException                                              if the file wrote operation fails
     * @throws org.weebly.generator.model.exceptions.DirectoryNotFoundException if the directory is not found
     * @throws org.weebly.generator.model.exceptions.DuplicateFileException     if the file already exists in the path
     */
    public static File createFile(String fileName, String path)
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
    public static boolean deleteFile(String fileName, String path)
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
     *
     * @param filePath the file path
     * @return true if the file exists otherwise returns false
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * writes text to file
     *
     * @param file    file object to write to
     * @param content text to write to file
     */
    public static void writeFileContent(File file, String content) {
        try (FileWriter fw = new FileWriter(file.getAbsoluteFile())) {
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(content);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Reads the contents of the file and returns as a string
     *
     * @param filePath the file path
     * @return the string contents
     * @throws java.io.IOException
     * @throws org.weebly.generator.model.exceptions.FileNotFoundException
     */
    public static String readFile(String filePath)
            throws IOException,
            FileNotFoundException {
        ClassLoader cl = FileHandler.class.getClassLoader();
        StringBuilder builder = new StringBuilder();

        try (InputStream in = cl.getResourceAsStream(filePath)) {
            try (BufferedReader bf = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while ((line = bf.readLine()) != null) {
                    builder.append(line).append("\n");
                }
            }
        }
        return builder.toString();
    }

}
