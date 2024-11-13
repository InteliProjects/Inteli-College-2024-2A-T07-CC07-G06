package com.nsync.service.csv;

import com.nsync.exception.custom.MissingCsvFieldException;
import com.nsync.generic.service.csv.GenericCsvProcessorService;
import com.nsync.service.csv.mapper.DistributorMapper;
import com.nsync.service.csv.mapper.ProductDistributorMapper;
import com.nsync.service.csv.mapper.ProductMapper;
import com.nsync.service.csv.mapper.SalesOrdersMapper;
import com.nsync.service.csv.mapper.SalesProductsMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

/**
 * Resource for processing CSV files and populating the database with data.
 * <p>
 * This resource provides endpoints for processing various CSV files related to
 * different entities. Each endpoint uses a specific mapper to read the CSV file,
 * map its records to entities, and persist them to the database.
 * </p>
 *
 * @author mauroDasChagas
 */
@Path("/csv-processor")
public class CsvProcessorResource {

    @Inject
    GenericCsvProcessorService genericCsvProcessorService;

    /**
     * Processes the CSV file for Distributor entities.
     * <p>
     * Reads the CSV file for distributors, maps each record to a Distributor
     * entity using the {@link DistributorMapper}, and persists the entities to the database.
     * </p>
     *
     * @return a {@link Response} indicating the result of the operation.
     */
    @POST
    @Path("/distributor")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response processDistributorCsv(@MultipartForm FileUploadForm form) {
        try {
            // Process the CSV file using the DistributorMapper
            genericCsvProcessorService.processCsvFile(form.getFile(), new DistributorMapper());
            return Response.accepted().build();
        } catch (MissingCsvFieldException e) {
            // Return a 400 Bad Request with the error message
            throw e;
        } catch (Exception e) {
            // Return a 500 Internal Server Error for unexpected errors
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    /**
     * Processes the CSV file for Product entities.
     * <p>
     * Reads the CSV file for products, maps each record to a Product entity
     * using the {@link ProductMapper}, and persists the entities to the database.
     * </p>
     *
     * @return a {@link Response} indicating the result of the operation.
     */
    @POST
    @Path("/product")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response processProductCsv(@MultipartForm FileUploadForm form) {
        try {
            genericCsvProcessorService.processCsvFile(form.getFile(), new ProductMapper());
            return Response.accepted().build();
        } catch (MissingCsvFieldException e) {
            // Return a 400 Bad Request with the error message
            throw e;
        } catch (Exception e) {
            // Return a 500 Internal Server Error for unexpected errors
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    /**
     * Processes the CSV file for ProductDistributor entities.
     * <p>
     * Reads the CSV file for product distributors, maps each record to a ProductDistributor
     * entity using the {@link ProductDistributorMapper}, and persists the entities to the database.
     * </p>
     *
     * @return a {@link Response} indicating the result of the operation.
     */
    @POST
    @Path("/product-distributor")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response processProductDistributorCsv(@MultipartForm FileUploadForm form) {
        try {
            genericCsvProcessorService.processCsvFile(form.getFile(), new ProductDistributorMapper());
            return Response.accepted().build();
        } catch (MissingCsvFieldException e) {
            throw e;
        } catch (Exception e) {
            // Return a 500 Internal Server Error for unexpected errors
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    /**
     * Processes the CSV file for SalesOrders entities.
     * <p>
     * Reads the CSV file for sales orders, maps each record to a SalesOrders entity
     * using the {@link SalesOrdersMapper}, and persists the entities to the database.
     * </p>
     *
     * @return a {@link Response} indicating the result of the operation.
     */
    @POST
    @Path("/sales-orders")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response processSalesOrdersCsv(@MultipartForm FileUploadForm form) {
        try {
            genericCsvProcessorService.processCsvFile(form.getFile(), new SalesOrdersMapper());
            return Response.accepted().build();
        } catch (MissingCsvFieldException e) {
            throw e;
        } catch (Exception e) {
            // Return a 500 Internal Server Error for unexpected errors
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    /**
     * Processes the CSV file for SalesProducts entities.
     * <p>
     * Reads the CSV file for sales products, maps each record to a SalesProducts entity
     * using the {@link SalesProductsMapper}, and persists the entities to the database.
     * </p>
     *
     * @return a {@link Response} indicating the result of the operation.
     */
    @POST
    @Path("/sales-products")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response processSalesProductsCsv(@MultipartForm FileUploadForm form) {
        try {
            genericCsvProcessorService.processCsvFile(form.getFile(), new SalesProductsMapper());
            return Response.accepted().build();
        } catch (MissingCsvFieldException e) {
            // Return a 400 Bad Request with the error message
            throw e;
        } catch (Exception e) {
            // Return a 500 Internal Server Error for unexpected errors
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    /**
     * Processes all CSV files for various entities.
     * <p>
     * Reads and processes the CSV files for all supported entities (Distributor, Product,
     * ProductDistributor, SalesOrders, SalesProducts), maps each record to the
     * corresponding entity using the appropriate mapper, and persists the entities to the database.
     * </p>
     *
     * @return a {@link Response} indicating the result of the operation. Returns a success message if all
     *         CSV files are processed successfully, otherwise returns an error message.
     */
    @POST
    @Path("/populate-all")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response processAllCsvs(@MultipartForm MultipleFileUploadForm form) {
        try {
            // Process each file with the corresponding mapper
            if (form.getFileDistributor() != null) { genericCsvProcessorService.processCsvFile(form.getFileDistributor() , new DistributorMapper()); } // Torna o campo opcional
            if (form.getFileProduct() != null) { genericCsvProcessorService.processCsvFile(form.getFileProduct(), new ProductMapper()); }
            if (form.getFileProductDistributor() != null) { genericCsvProcessorService.processCsvFile(form.getFileProductDistributor(), new ProductDistributorMapper()); }
            if (form.getFileSalesOrders() != null) { genericCsvProcessorService.processCsvFile(form.getFileSalesOrders(), new SalesOrdersMapper()); }
            if (form.getFileSalesProducts() != null) { genericCsvProcessorService.processCsvFile(form.getFileSalesProducts(), new SalesProductsMapper()); }

            return Response.accepted("All CSVs processed successfully").build();
        } catch (MissingCsvFieldException e) {
            // Return a 400 Bad Request with the error message
            throw e;
        }catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Failed to process CSVs").build();
        }
    }
}
