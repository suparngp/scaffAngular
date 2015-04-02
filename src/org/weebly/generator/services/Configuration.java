package org.weebly.generator.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Defines the configuration of the plugin.
 * Created by suparngupta on 7/10/14.
 */
public class Configuration {
    private List<String> moduleNameSuggestions;
    private HashMap<String, String> mainSpecFilesMap;
    public Configuration(){
        this.moduleNameSuggestions = new ArrayList<>();
        this.mainSpecFilesMap = new HashMap<>();
    }


    /**
     * Sets new moduleNameSuggestions.
     *
     * @param moduleNameSuggestions New value of moduleNameSuggestions.
     */
    public void setModuleNameSuggestions(List<String> moduleNameSuggestions) {
        this.moduleNameSuggestions = moduleNameSuggestions;
    }

    /**
     * Gets moduleNameSuggestions.
     *
     * @return Value of moduleNameSuggestions.
     */
    public List<String> getModuleNameSuggestions() {
        return moduleNameSuggestions;
    }

    /**
     * Gets the mainSpecFilesMap
     * @return Value of mainSpecFilesMap
     */
    public HashMap<String, String> getMainSpecFilesMap() {
        return mainSpecFilesMap;
    }

    /**
     * Gets the mainSpecFilesMap
     * @param mainSpecFilesMap New value of main
     */
    public void setMainSpecFilesMap(HashMap<String, String> mainSpecFilesMap) {
        this.mainSpecFilesMap = mainSpecFilesMap;
    }
}
