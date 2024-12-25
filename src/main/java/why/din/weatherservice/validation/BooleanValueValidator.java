package why.din.weatherservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BooleanValueValidator implements ConstraintValidator<BooleanValue, Boolean> {

    @Override
    public void initialize(BooleanValue constraintAnnotation) {
    }

    @Override
    public boolean isValid(Boolean value, ConstraintValidatorContext context) {
        return value != null;
    }
}

