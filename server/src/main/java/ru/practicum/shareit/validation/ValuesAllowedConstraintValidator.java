package ru.practicum.shareit.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ValuesAllowedConstraintValidator implements ConstraintValidator<ValuesAllowedConstraint, String> {

    private String message;
    private List<String> allowable;

    @Override
    public void initialize(ValuesAllowedConstraint requiredIfChecked) {
        this.message = requiredIfChecked.message();
        this.allowable = Arrays.asList(requiredIfChecked.values());
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean valid = value == null || this.allowable.contains(value.toLowerCase());

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }

        return valid;
    }
}
