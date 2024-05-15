package dev.miguelhiguera.chantasy.annotations;

import dev.miguelhiguera.chantasy.validators.PositionRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PositionRangeValidator.class)
public @interface ValidPositionRange {
    String message() default "El rango de posiciones no es válido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
