package payment.validation;

import payment.exception.PaymentException;
import payment.exception.ValidationException;
import payment.model.PaymentRequest;

public class AmountHandler extends PaymentHandler {
    @Override
    public void handle(PaymentRequest request) throws PaymentException {
        if (request.getAmount() <= 0)
            throw new ValidationException("Tutar sifirdan buyuk olmalidir.");
        super.handle(request);
    }
}
