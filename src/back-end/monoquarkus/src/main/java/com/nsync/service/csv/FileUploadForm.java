package com.nsync.service.csv;

import jakarta.ws.rs.FormParam;
import lombok.Getter;
import lombok.Setter;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import java.io.InputStream;

/**
 * Represents a form for uploading files.
 * <p>
 * This class is used to handle file uploads in a multipart form-data request. It provides a single
 * field for the file and getter and setter methods to access and modify the file.
 * </p>
 */
@Getter
@Setter
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
}
