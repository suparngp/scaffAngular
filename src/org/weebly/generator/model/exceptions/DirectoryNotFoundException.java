package org.weebly.generator.model.exceptions;

/**
 * An exception thrown when the required directory is not found.
 * Created by IronMan on 7/8/14.
 */
public class DirectoryNotFoundException extends AngularIUnitException {


    /**
     * Creates an instance of DirectoryNotFoundException
     */
    public DirectoryNotFoundException(){
        super("DirectoryNotFoundException",
                "The required directory was not found in the path.",
                ExceptionCode.DIRECTORY_NOT_FOUND);
    }

}
