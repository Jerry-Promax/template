package com.example.demo.common;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE,ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = PasswordConstraintValidator.class)
@Constraint(validatedBy = {PasswordConstraintValidator.class})
@Documented
public @interface ValidPassword {
    String message() default "密码验证错误";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
