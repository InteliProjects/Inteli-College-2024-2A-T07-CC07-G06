package com.nsync.microservice.model;

import org.jboss.resteasy.annotations.providers.multipart.PartType;
import jakarta.ws.rs.FormParam;
import java.io.InputStream;

public class MultipleFileUploadForm {

    @FormParam("fileDistributor")
    @PartType("application/octet-stream")
    private InputStream fileDistributor;

    @FormParam("fileProduct")
    @PartType("application/octet-stream")
    private InputStream fileProduct;

    @FormParam("fileProductDistributor")
    @PartType("application/octet-stream")
    private InputStream fileProductDistributor;

    @FormParam("fileSalesOrders")
    @PartType("application/octet-stream")
    private InputStream fileSalesOrders;

    @FormParam("fileSalesProducts")
    @PartType("application/octet-stream")
    private InputStream fileSalesProducts;

    // Getters e Setters
    public InputStream getFileDistributor() {
        return fileDistributor;
    }

    public void setFileDistributor(InputStream fileDistributor) {
        this.fileDistributor = fileDistributor;
    }

    public InputStream getFileProduct() {
        return fileProduct;
    }

    public void setFileProduct(InputStream fileProduct) {
        this.fileProduct = fileProduct;
    }

    public InputStream getFileProductDistributor() {
        return fileProductDistributor;
    }

    public void setFileProductDistributor(InputStream fileProductDistributor) {
        this.fileProductDistributor = fileProductDistributor;
    }

    public InputStream getFileSalesOrders() {
        return fileSalesOrders;
    }

    public void setFileSalesOrders(InputStream fileSalesOrders) {
        this.fileSalesOrders = fileSalesOrders;
    }

    public InputStream getFileSalesProducts() {
        return fileSalesProducts;
    }

    public void setFileSalesProducts(InputStream fileSalesProducts) {
        this.fileSalesProducts = fileSalesProducts;
    }
}

