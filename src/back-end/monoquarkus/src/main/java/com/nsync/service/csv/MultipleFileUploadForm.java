package com.nsync.service.csv;

import jakarta.ws.rs.FormParam;
import lombok.Getter;
import lombok.Setter;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import java.io.InputStream;

@Getter
@Setter
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
}
