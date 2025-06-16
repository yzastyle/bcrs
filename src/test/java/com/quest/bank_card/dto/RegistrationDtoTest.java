package com.quest.bank_card.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistrationDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void registrationDtoTest() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("testUser");
        registrationDto.setName("Test Name");
        registrationDto.setPassword("anyPassword");

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(registrationDto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void registrationDtoTest_loginIsBlank() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("");
        registrationDto.setName("Test Name");
        registrationDto.setPassword("anyPassword");

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(registrationDto);

        assertEquals(2, violations.size());

        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());

        assertTrue(messages.contains("Login information must not be blank"));
        assertTrue(messages.contains("Login information must be between at 2 or 50 characters long"));
    }

    @Test
    void registrationDtoTest_loginIsNull() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin(null);
        registrationDto.setName("Test Name");
        registrationDto.setPassword("anyPassword");

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(registrationDto);

        assertEquals(1, violations.size());
        ConstraintViolation<RegistrationDto> violation = violations.iterator().next();
        assertEquals("login", violation.getPropertyPath().toString());
        assertEquals("Login information must not be blank", violation.getMessage());
    }

    @Test
    void registrationDtoTest_loginIsTooShort() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("a");
        registrationDto.setName("Test Name");
        registrationDto.setPassword("anyPassword");

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(registrationDto);

        assertEquals(1, violations.size());
        ConstraintViolation<RegistrationDto> violation = violations.iterator().next();
        assertEquals("login", violation.getPropertyPath().toString());
        assertEquals("Login information must be between at 2 or 50 characters long", violation.getMessage());
    }

    @Test
    void registrationDtoTest_loginIsTooLong() {
        RegistrationDto registrationDto = new RegistrationDto();
        String longLogin = "a".repeat(51);
        registrationDto.setLogin(longLogin);
        registrationDto.setName("Test Name");
        registrationDto.setPassword("anyPassword");

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(registrationDto);

        assertEquals(1, violations.size());
        ConstraintViolation<RegistrationDto> violation = violations.iterator().next();
        assertEquals("login", violation.getPropertyPath().toString());
        assertEquals("Login information must be between at 2 or 50 characters long", violation.getMessage());
    }

    @Test
    void registrationDtoTest_nameIsBlank() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("testUser");
        registrationDto.setName("");
        registrationDto.setPassword("anyPassword");

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(registrationDto);

        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());

        assertEquals(2, violations.size());
        assertTrue(messages.contains("Name information must not be blank"));
        assertTrue(messages.contains("Name information must be between at 2 or 50 characters long"));
    }

    @Test
    void registrationDtoTest_nameIsNull() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("testUser");
        registrationDto.setName(null);
        registrationDto.setPassword("anyPassword");

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(registrationDto);

        assertEquals(1, violations.size());
        ConstraintViolation<RegistrationDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("Name information must not be blank", violation.getMessage());
    }

    @Test
    void registrationDtoTest_nameIsTooShort() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("testUser");
        registrationDto.setName("a");
        registrationDto.setPassword("anyPassword");

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(registrationDto);

        assertEquals(1, violations.size());
        ConstraintViolation<RegistrationDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("Name information must be between at 2 or 50 characters long", violation.getMessage());
    }

    @Test
    void registrationDtoTest_nameIsTooLong() {
        RegistrationDto registrationDto = new RegistrationDto();
        String longName = "a".repeat(51);
        registrationDto.setLogin("testUser");
        registrationDto.setName(longName);
        registrationDto.setPassword("anyPassword");

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(registrationDto);

        assertEquals(1, violations.size());
        ConstraintViolation<RegistrationDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("Name information must be between at 2 or 50 characters long", violation.getMessage());
    }

    @Test
    void registrationDtoTest_multipleErrors() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("");
        registrationDto.setName("");
        registrationDto.setPassword("anyPassword");

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(registrationDto);

        assertEquals(4, violations.size());
    }

    @Test
    void registrationDtoTest_acceptBoundaryValues() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("ab");
        registrationDto.setName("cd");
        registrationDto.setPassword("anyPassword");

        Set<ConstraintViolation<RegistrationDto>> violations = validator.validate(registrationDto);

        assertTrue(violations.isEmpty(), "Минимальная длина должна проходить валидацию");

        String maxLengthString = "a".repeat(50);
        registrationDto.setLogin(maxLengthString);
        registrationDto.setName(maxLengthString);

        violations = validator.validate(registrationDto);

        assertTrue(violations.isEmpty(), "Максимальная длина должна проходить валидацию");
    }
}