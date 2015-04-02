package org.weebly.generator.services;

import org.weebly.generator.model.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Service to load file templates.
 */
public class TemplateLoader {

    private static String templatePath = "/templates/";
    private String[] componentTypes = {"Controller", "Directive", "Service", "Factory", "Filter"};

    private HashMap<String, Component> Components = new HashMap<>();

    public boolean isLoaded = false;

    public TemplateLoader() {
        initComponent();
    }

    public final void initComponent() {
        try {
            for (String obj : componentTypes) {
                Component tmp = new Component();
                tmp.code = FileHandler.readFile(templatePath + obj + ".js");
                tmp.spec = FileHandler.readFile(templatePath + obj + "Spec.js");

                Components.put(obj, tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        isLoaded = true;
    }

    /**
     * Gets the map of initialized Components
     *
     * @return map
     */
    public HashMap<String, Component> getComponents() {
        return Components;
    }
    public String[] getComponentTypes() {return componentTypes;}
}
