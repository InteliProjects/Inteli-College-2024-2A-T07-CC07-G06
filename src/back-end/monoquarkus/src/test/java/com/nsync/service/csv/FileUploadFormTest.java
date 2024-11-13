package com.nsync.service.csv;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test for the {@link FileUploadForm} class.
 * This class contains several test cases to validate the behavior of file upload handling.
 */
@QuarkusTest
public class FileUploadFormTest {

    /**
     * Tests the getter for the file input stream in {@link FileUploadForm}.
     * Verifies that the file can be retrieved correctly after being set.
     */
    @Test
    public void testGetFile() {
        FileUploadForm form = new FileUploadForm();
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        form.setFile(inputStream);
        assertEquals(inputStream, form.getFile());
    }

    /**
     * Tests the setter for the file input stream in {@link FileUploadForm}.
     * Verifies that the file can be set and retrieved correctly.
     */
    @Test
    public void testSetFile() {
        FileUploadForm form = new FileUploadForm();
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        form.setFile(inputStream);
        assertNotNull(form.getFile());
        assertEquals(inputStream, form.getFile());
    }

    /**
     * Tests the behavior when setting the file input stream to null in {@link FileUploadForm}.
     * Verifies that the file is set to null.
     */
    @Test
    public void testSetFileWithNull() {
        FileUploadForm form = new FileUploadForm();
        form.setFile(null);
        assertNull(form.getFile());
    }

    /**
     * Tests the getter for the file input stream in {@link FileUploadForm} when no file has been set.
     * Verifies that the returned value is null.
     */
    @Test
    public void testGetFileWithNoFileSet() {
        FileUploadForm form = new FileUploadForm();
        assertNull(form.getFile());
    }

    /**
     * Tests the setter for the file input stream in {@link FileUploadForm} with different input streams.
     * Verifies that the latest set input stream is correctly retrieved.
     */
    @Test
    public void testSetFileWithDifferentStreams() {
        FileUploadForm form = new FileUploadForm();
        InputStream inputStream1 = new ByteArrayInputStream("test data 1".getBytes());
        InputStream inputStream2 = new ByteArrayInputStream("test data 2".getBytes());
        form.setFile(inputStream1);
        assertEquals(inputStream1, form.getFile());
        form.setFile(inputStream2);
        assertEquals(inputStream2, form.getFile());
    }
}
