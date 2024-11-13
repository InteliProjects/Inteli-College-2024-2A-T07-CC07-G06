package com.nsync.cache;

import com.nsync.models.ProductIdsRequest;
import io.quarkus.cache.CacheKeyGenerator;
import jakarta.enterprise.context.ApplicationScoped;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductIdsCacheKeyGenerator implements CacheKeyGenerator {

    @Override
    public Object generate(Method method, Object... methodParams) {
        ProductIdsRequest request = (ProductIdsRequest) methodParams[0];
        List<Long> ids = request.getProductsIds();

        return ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("-"));
    }
}
