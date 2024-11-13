package com.nsync.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductIdsRequest {
    private List<Long> productsIds;
}
