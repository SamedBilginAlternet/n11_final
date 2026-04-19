package payment.validation;

import payment.exception.PaymentException;
import payment.exception.ProcessingException;
import payment.model.PaymentRequest;

public class FraudHandler extends PaymentHandler {
    @Override
    public void handle(PaymentRequest request) throws PaymentException {
        if (request.getAmount() > 50_000)
            throw new ProcessingException("Islem fraud kontrolunden gecemedi.");
        super.handle(request);
    }
}
