package com.app.onlinebookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class StringsEqualValidator implements ConstraintValidator<ElementsEqual, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(ElementsEqual constraintAnnotation) {
        firstFieldName = constraintAnnotation.firstFieldName();
        secondFieldName = constraintAnnotation.secondFieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object firstElement = new BeanWrapperImpl(value)
                .getPropertyValue(firstFieldName);
        Object secondElement = new BeanWrapperImpl(value)
                .getPropertyValue(secondFieldName);
        return Objects.equals(firstElement, secondElement);
    }
}
