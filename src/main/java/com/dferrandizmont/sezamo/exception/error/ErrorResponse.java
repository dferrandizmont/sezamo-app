package com.dferrandizmont.sezamo.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse {
    private final int status;
    private final String message;
    private List<ValidationError> errors;
    private List<ValidationProductError> productErrors;

    @Getter
    @Setter
    @RequiredArgsConstructor
    private static class ValidationError {
        private final String field;
        private final String message;
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) {
            errors = new ArrayList<>();
        }
        errors.add(new ValidationError(field, message));
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    private static class ValidationProductError {
        private final String product;
        private final String message;
    }

    public void addValidationProductError(String product, String message) {
        if (Objects.isNull(productErrors)) {
            productErrors = new ArrayList<>();
        }
        productErrors.add(new ValidationProductError(product, message));
    }
}
