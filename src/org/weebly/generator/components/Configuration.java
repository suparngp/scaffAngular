package org.weebly.generator.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the configuration of the plugin.
 * Created by suparngupta on 7/10/14.
 */
public class Configuration {
    private List<String> moduleNameSuggestions;

    public Configuration(){
        this.moduleNameSuggestions = new ArrayList<String>();
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
}
