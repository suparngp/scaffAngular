package org.weebly.generator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the configuration of the plugin.
 * Created by suparngupta on 7/10/14.
 */
public class Configuration {
    private List<String> moduleNameSuggestions = new ArrayList<>();

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
