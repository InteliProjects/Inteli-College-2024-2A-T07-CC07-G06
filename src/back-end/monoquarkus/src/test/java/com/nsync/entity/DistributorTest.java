package com.nsync.entity;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * Unit test for the {@link Distributor} entity class.
 * This class contains several test cases to validate the functionality and constraints of the Distributor entity.
 */
@QuarkusTest
public class DistributorTest {

    @Inject
    EntityManager entityManager;

    @Inject
    Validator validator;

    /**
     * Tests the valid scenario of the {@link Distributor} entity.
     * Verifies that a valid Distributor instance passes validation and can be persisted in the database.
     */
    @Test
    @Transactional
    public void testDistributorValid() {
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";
        distributor.cep = "12345678";

        Set<ConstraintViolation<Distributor>> violations = validator.validate(distributor);
        Assertions.assertTrue(violations.isEmpty());

        entityManager.persist(distributor);
        entityManager.flush();

        Distributor persistedDistributor = entityManager.find(Distributor.class, distributor.id);
        Assertions.assertNotNull(persistedDistributor);
        Assertions.assertEquals("Distributor Name", persistedDistributor.name);
        Assertions.assertEquals("12345678", persistedDistributor.cep);
    }

    /**
     * Tests an invalid scenario for the {@link Distributor} entity when the 'name' is null.
     * Verifies that the validation fails and the appropriate constraint violation message is returned.
     */
    @Test
    @Transactional
    public void testDistributorInvalidName() {
        Distributor distributor = new Distributor();
        distributor.cep = "12345678";

        Set<ConstraintViolation<Distributor>> violations = validator.validate(distributor);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Name cannot be null")));
    }

    /**
     * Tests an invalid scenario for the {@link Distributor} entity when the 'cep' is null.
     * Verifies that the validation fails and the appropriate constraint violation message is returned.
     */
    @Test
    @Transactional
    public void testDistributorInvalidCep() {
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";

        Set<ConstraintViolation<Distributor>> violations = validator.validate(distributor);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("CEP cannot be null")));
    }

    /**
     * Tests an invalid scenario for the {@link Distributor} entity when the 'name' is an empty string.
     * Verifies that the validation fails and the appropriate constraint violation message is returned.
     */
    @Test
    @Transactional
    public void testDistributorEmptyName() {
        Distributor distributor = new Distributor();
        distributor.name = "";
        distributor.cep = "12345678";

        Set<ConstraintViolation<Distributor>> violations = validator.validate(distributor);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Name cannot be null")));
    }

    /**
     * Tests an invalid scenario for the {@link Distributor} entity when the 'cep' is an empty string.
     * Verifies that the validation fails and the appropriate constraint violation message is returned.
     */
    @Test
    @Transactional
    public void testDistributorEmptyCep() {
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";
        distributor.cep = "";

        Set<ConstraintViolation<Distributor>> violations = validator.validate(distributor);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("CEP cannot be null")));
    }

    /**
     * Tests an invalid scenario for the {@link Distributor} entity when the 'name' is null.
     * Verifies that the validation fails and the appropriate constraint violation message is returned.
     */
    @Test
    @Transactional
    public void testDistributorNullName() {
        Distributor distributor = new Distributor();
        distributor.name = null;
        distributor.cep = "12345678";

        Set<ConstraintViolation<Distributor>> violations = validator.validate(distributor);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Name cannot be null")));
    }

    /**
     * Tests an invalid scenario for the {@link Distributor} entity when the 'cep' is null.
     * Verifies that the validation fails and the appropriate constraint violation message is returned.
     */
    @Test
    @Transactional
    public void testDistributorNullCep() {
        Distributor distributor = new Distributor();
        distributor.name = "Distributor Name";
        distributor.cep = null;

        Set<ConstraintViolation<Distributor>> violations = validator.validate(distributor);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("CEP cannot be null")));
    }
}
