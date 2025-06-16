package com.quest.bank_card.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CardCreateDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void cardCreateDtoTest() {
        CardCreateDto dto = new CardCreateDto();
        dto.setOwner("IVAN PETROV");

        Set<ConstraintViolation<CardCreateDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void cardCreateDtoTest_ownerIsNull() {
        CardCreateDto dto = new CardCreateDto();
        dto.setOwner(null);

        Set<ConstraintViolation<CardCreateDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        ConstraintViolation<CardCreateDto> violation = violations.iterator().next();
        assertEquals("owner", violation.getPropertyPath().toString());
        assertEquals("Owner information must not be blank", violation.getMessage());
    }

    @Test
    void cardCreateDtoTest_ownerIsEmpty() {
        CardCreateDto dto = new CardCreateDto();
        dto.setOwner("");

        Set<ConstraintViolation<CardCreateDto>> violations = validator.validate(dto);

        assertEquals(2, violations.size());

        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());

        assertTrue(messages.contains("Owner information must not be blank"));
        assertTrue(messages.contains("Owner information must be between at 2 or 50 characters long"));
    }

    @Test
    void cardCreateDtoTest_ownerIsBlank() {
        CardCreateDto dto = new CardCreateDto();
        dto.setOwner("   ");

        Set<ConstraintViolation<CardCreateDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        ConstraintViolation<CardCreateDto> violation = violations.iterator().next();
        assertEquals("owner", violation.getPropertyPath().toString());
        assertEquals("Owner information must not be blank", violation.getMessage());
    }

    @Test
    void cardCreateDtoTest_ownerTooShort() {
        CardCreateDto dto = new CardCreateDto();
        dto.setOwner("A");

        Set<ConstraintViolation<CardCreateDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        ConstraintViolation<CardCreateDto> violation = violations.iterator().next();
        assertEquals("owner", violation.getPropertyPath().toString());
        assertEquals("Owner information must be between at 2 or 50 characters long", violation.getMessage());
    }

    @Test
    void cardCreateDtoTest_ownerTooLong() {
        CardCreateDto dto = new CardCreateDto();
        dto.setOwner("A".repeat(51));

        Set<ConstraintViolation<CardCreateDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        ConstraintViolation<CardCreateDto> violation = violations.iterator().next();
        assertEquals("owner", violation.getPropertyPath().toString());
        assertEquals("Owner information must be between at 2 or 50 characters long", violation.getMessage());
    }

    @Test
    void cardCreateDtoTest_ownerHasMinimumLength() {
        CardCreateDto dto = new CardCreateDto();
        dto.setOwner("AB");

        Set<ConstraintViolation<CardCreateDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void cardCreateDtoTest_ownerHasMaximumLength() {
        CardCreateDto dto = new CardCreateDto();
        dto.setOwner("A".repeat(50));

        Set<ConstraintViolation<CardCreateDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}