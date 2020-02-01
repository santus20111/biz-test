package com.example.bizhome.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConvertDateValidator implements ConstraintValidator<ConvertDateConstraint, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s != null) {
            try {
                LocalDate date = LocalDate.parse(s);
                return date.isEqual(LocalDate.now()) || date.isBefore(LocalDate.now());
            } catch (Throwable e) {
                return false;
            }
        } return true;
    }
}
