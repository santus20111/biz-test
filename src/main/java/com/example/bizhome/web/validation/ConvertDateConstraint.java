package com.example.bizhome.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ConvertDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConvertDateConstraint {
    String message() default "Дата конвертации не может быть позже текущей";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
