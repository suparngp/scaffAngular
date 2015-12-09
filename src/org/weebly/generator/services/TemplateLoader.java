package org.weebly.generator.services;

import org.weebly.generator.model.Component;

import java.util.HashMap;

/**
 * Service to load file templates.
 */
public class TemplateLoader {

    private String[] componentTypes = {"Controller", "Directive", "Service", "Factory", "Filter"};

    private boolean isLoaded = false;
    private HashMap<String, Component> Components = new HashMap<>();

    public String[] getComponentTypes() {return componentTypes;}

    /**
     * Lazy loads and gets the map of initialized Components
     *
     * @return map
     */
    public HashMap<String, Component> getComponents() {
        if(!isLoaded) {
            initComponent();
        }
        return Components;
    }

    private void initComponent() {
        String templatePath = "/templates/";
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


}
