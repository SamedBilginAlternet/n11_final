package payment.validation;

import org.springframework.stereotype.Component;
import payment.exception.PaymentException;
import payment.exception.ValidationException;
import payment.model.PaymentRequest;

@Component
public class AmountHandler extends PaymentHandler {
    @Override
    public void handle(PaymentRequest request) throws PaymentException {
        if (request.getAmount() <= 0)
            throw new ValidationException("Tutar sifirdan buyuk olmalidir.");
        super.handle(request);
    }
}
