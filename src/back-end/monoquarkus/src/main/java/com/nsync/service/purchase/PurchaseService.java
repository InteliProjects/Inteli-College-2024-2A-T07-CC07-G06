package com.nsync.service.purchase;

import com.nsync.entity.Distributor;
import com.nsync.entity.ProductDistributor;
import com.nsync.models.ProductRequest;
import com.nsync.models.PurchaseRequest;
import com.nsync.service.cep.CepDeliveryEstimator;
import com.nsync.service.cep.CepPurchaseResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class PurchaseService {
    @Inject
    public EntityManager entityManager;

    @Inject
    CepDeliveryEstimator cepDeliveryEstimator;

    @Transactional
    public boolean processPurchase(PurchaseRequest purchaseRequest) {
        List<ProductRequest> productRequestList = purchaseRequest.getProducts();
        List<CepPurchaseResponse> cepPurchaseResponseList = cepDeliveryEstimator.calculateDaysPurchase(purchaseRequest);
        try {
            for (int i = 0; i < cepPurchaseResponseList.size(); i++) {
                Long productId = cepPurchaseResponseList.get(i).getProductId();
                Distributor distributor = cepPurchaseResponseList.get(i).getDistributor();
                Integer quantity = productRequestList.get(i).getQuantity();

                // Query to find the ProductDistributor based on productId and distributor
                ProductDistributor productDistributor = entityManager.createQuery(
                                "SELECT pd FROM ProductDistributor pd WHERE pd.product.id = :productId AND pd.distributor = :distributor",
                                ProductDistributor.class)
                        .setParameter("productId", productId)
                        .setParameter("distributor", distributor)
                        .getSingleResult();

                if (productDistributor == null || productDistributor.quantityAvailable < quantity) {
                    // If any purchase request fails, return false
                    return false;
                }

                // Deduct the quantity
                productDistributor.quantityAvailable -= quantity;

                // Persist the changes
                entityManager.merge(productDistributor);
            }

            // If all purchase requests are successfully processed, return true
            return true;
        } catch (NoResultException e) {
            System.out.println("Problem Purchase Service");
            return false;
        }
    }
}
