package com.nsync.microservice.model;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import jakarta.ws.rs.FormParam;
import java.io.InputStream;

/**
 * Represents a form for uploading files.
 * <p>
 * This class is used to handle file uploads in a multipart form-data request. It provides a single
 * field for the file and getter and setter methods to access and modify the file.
 * </p>
 */
public class FileUploadForm {

    /**
     * The file to be uploaded.
     * <p>
     * This field is annotated with {@link PartType} to specify the MIME type of the file being uploaded.
     * </p>
     */
    @FormParam("file")
    @PartType("application/octet-stream")
    private InputStream file;

    /**
     * Gets the file to be uploaded.
     *
     * @return the file to be uploaded.
     */
    public InputStream getFile() {
        return file;
    }

    /**
     * Sets the file to be uploaded.
     *
     * @param file the file to be uploaded.
     */
    public void setFile(InputStream file) {
        this.file = file;
    }
}
