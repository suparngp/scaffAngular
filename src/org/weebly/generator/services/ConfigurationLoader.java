package org.weebly.generator.services;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import org.jetbrains.annotations.Nullable;
import org.weebly.generator.model.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads the plugin configuration
 */

@State(name = "scaffangular", reloadable = true, storages = {
        @Storage(id = "other", file = StoragePathMacros.APP_CONFIG + "/scaffangular.xml")
})
public class ConfigurationLoader implements PersistentStateComponent<Configuration> {
    private Configuration configuration;

    @Nullable
    @Override
    public Configuration getState() {
        if(configuration == null){
            this.loadState(new Configuration());
        }
        return configuration;
    }

    @Override
    public void loadState(Configuration configuration) {
        if (configuration != null) {
            this.configuration = configuration;
        } else {
            this.configuration = new Configuration();
        }
    }

    /**
     * Returns the suggestions for the module names
     *
     * @return the list of module names used so far
     */
    public List<String> getModuleNameSuggestions() {
        return this.getState().getModuleNameSuggestions();
    }

    public void persistModuleName(String moduleName) {
        //save module name suggestion
        if (this.configuration.getModuleNameSuggestions() == null) {
            this.configuration.setModuleNameSuggestions(new ArrayList<String>());
        }
        if (!this.configuration.getModuleNameSuggestions().contains(moduleName)) {
            this.configuration.getModuleNameSuggestions().add(moduleName);
        }
    }
}
