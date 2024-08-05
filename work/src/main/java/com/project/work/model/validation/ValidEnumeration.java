package com.project.work.model.validation;

import com.project.work.model.validation.validator.EnumerationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumerationValidator.class)
public @interface ValidEnumeration {
    String message() default "해당 값은 Enum 형에 맞지 않은 값이 아닙니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Enum<?>> enumClass();
}
