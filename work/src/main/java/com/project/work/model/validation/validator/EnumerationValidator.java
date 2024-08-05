package com.project.work.model.validation.validator;

import com.project.work.model.validation.ValidEnumeration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumerationValidator implements ConstraintValidator<ValidEnumeration, Enum<?>> {
    private ValidEnumeration annotation;

    @Override
    public void initialize(ValidEnumeration constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext constraintValidatorContext) {
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if( enumValues != null ) {
            for (Object enumValue : enumValues) {
                if( value == enumValue ) {
                    return true;
                }
            }
        }
        return false;
    }
}
