package com.app.onlinebookstore.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StringsEqualValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ElementsEqual.List.class)
public @interface ElementsEqual {
    String message() default "Elements are not equal";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String firstFieldName();
    String secondFieldName();

    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ElementsEqual[] value();
    }
}
