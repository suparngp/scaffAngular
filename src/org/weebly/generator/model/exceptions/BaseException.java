package org.weebly.generator.model.exceptions;

/**
 * Defines the generic exception used by the Angular IUnit Module
 * Created by IronMan on 7/8/14.
 */
public class BaseException extends Exception {

    private String name;
    private String description;
    private ExceptionCode code;

    /**
     * Creates a BaseException instance
     * @param name the name of the exception
     * @param description the description of the exception
     * @param code the templates.code associated with the exception
     */
    public BaseException(String name, String description, ExceptionCode code){
        this.name = name;
        this.description = description;
        this.code = code;
    }

    /**
     * Gets the name of the exception
     *
     * @return the name of the exception
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the exception
     *
     * @return the description of the exception
     */
    public String getDescription() {
        return description;
    }


    /**
     * Gets the exception templates.code
     *
     * @return the exception templates.code
     */
    public ExceptionCode getCode() {
        return code;
    }
}
