package org.weebly.generator.services;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import org.jetbrains.annotations.Nullable;

/**
 * Loads the plugin configuration
 * Created by suparngupta on 7/10/14.
 */
@State(name = "Configuration", reloadable = true, storages = {
        @Storage(id = "other", file = StoragePathMacros.APP_CONFIG + "/other.xml")
})
public class ConfigurationLoader implements PersistentStateComponent<Configuration> {
    private Configuration configuration;

    @Nullable
    @Override
    public Configuration getState() {
        return configuration;
    }

    @Override
    public void loadState(Configuration _configuration) {
        if (_configuration == null) {
            configuration = new Configuration();
        } else {
            configuration = _configuration;
        }
    }
}
